/* tarefas.js
 - Reescrito: apiFetch helper, render seguro, timers centralizados, modal de edição.
 - Exige token no localStorage em "token".
 - Quando criar/editar, envia dataLimite no formato ISO (ex: 2025-12-31T14:30:00).
*/

document.addEventListener("DOMContentLoaded", () => {
  // tema
  const temaSalvo = localStorage.getItem("tema");
  if (temaSalvo === "light") document.body.classList.add("light-theme");

  // elementos
  const form = document.getElementById("formNovaTarefa");
  const tituloInput = document.getElementById("titulo");
  const descricaoInput = document.getElementById("descricao");
  const dataLimiteInput = document.getElementById("dataLimite");
  const horaLimiteInput = document.getElementById("horaLimite");
  const tarefasContainer = document.getElementById("tarefasContainer");
  const feedback = document.getElementById("formFeedback");
  const token = localStorage.getItem("token");

  // modal
  const editModal = document.getElementById("editModal");
  const editTitulo = document.getElementById("editTitulo");
  const editDescricao = document.getElementById("editDescricao");
  const editDataLimite = document.getElementById("editDataLimite");
  const editHoraLimite = document.getElementById("editHoraLimite");
  const saveEditBtn = document.getElementById("saveEdit");
  const cancelEditBtn = document.getElementById("cancelEdit");

  let tarefasCache = []; // cache local das tarefas
  let editarTaskId = null;

  // se não estiver logado
  if (!token) {
    window.location.href = "index.html";
    return;
  }

  // utilitário para chamadas à API com token
  async function apiFetch(path, opts = {}) {
    const base = "http://localhost:8080";
    const headers = opts.headers || {};
    headers["Authorization"] = `Bearer ${token}`;
    if (opts.body && !headers["Content-Type"]) {
      headers["Content-Type"] = "application/json";
    }
    try {
      const resp = await fetch(base + path, { ...opts, headers });
      return resp;
    } catch (err) {
      console.error("apiFetch error:", err);
      throw err;
    }
  }

  // form submit - criar tarefa
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    feedback.textContent = "";

    const titulo = tituloInput.value.trim();
    const descricao = descricaoInput.value.trim();

    if (!titulo || !descricao) {
      feedback.textContent = "Preencha título e descrição.";
      return;
    }

    // construir dataLimite ISO se preenchida
    let dataLimiteISO = null;
    if (dataLimiteInput.value) {
      const hora = horaLimiteInput.value || "00:00";
      dataLimiteISO = `${dataLimiteInput.value}T${hora}:00`;
    }

    const payload = { titulo, descricao };
    if (dataLimiteISO) payload.dataLimite = dataLimiteISO;

    try {
      const resp = await apiFetch("/tarefas", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      if (resp.ok) {
        tituloInput.value = "";
        descricaoInput.value = "";
        dataLimiteInput.value = "";
        horaLimiteInput.value = "";
        carregarTarefas();
        feedback.textContent = "Tarefa adicionada!";
        setTimeout(() => feedback.textContent = "", 2000);
      } else {
        const txt = await resp.text();
        feedback.textContent = txt || "Erro ao adicionar tarefa.";
      }
    } catch (err) {
      feedback.textContent = "Erro de rede.";
    }
  });

  // carregar tarefas do backend
  async function carregarTarefas() {
    try {
      const resp = await apiFetch("/tarefas");
      if (!resp.ok) {
        tarefasContainer.textContent = "Erro ao carregar tarefas.";
        return;
      }
      const tarefas = await resp.json();
      tarefasCache = tarefas;
      renderTarefas(tarefas);
    } catch (err) {
      tarefasContainer.textContent = "Erro de rede ao carregar tarefas.";
    }
  }

  // renderiza lista de tarefas (seguro: usa textContent)
  function renderTarefas(tarefas) {
    tarefasContainer.innerHTML = "";
    if (!Array.isArray(tarefas) || tarefas.length === 0) {
      const p = document.createElement("p");
      p.textContent = "Nenhuma tarefa encontrada.";
      tarefasContainer.appendChild(p);
      return;
    }

    tarefas.forEach(tarefa => {
      const card = document.createElement("article");
      card.className = "tarefa";
      card.dataset.id = tarefa.id;

      // header com botões e timer
      const header = document.createElement("div");
      header.className = "task-header";

      const botoes = document.createElement("div");
      botoes.className = "botoes";

      const btnEditar = document.createElement("button");
      btnEditar.className = "editar";
      btnEditar.type = "button";
      btnEditar.textContent = "Editar";

      const btnApagar = document.createElement("button");
      btnApagar.className = "apagar";
      btnApagar.type = "button";
      btnApagar.textContent = "Apagar";

      const btnExpandir = document.createElement("button");
      btnExpandir.className = "expandir";
      btnExpandir.type = "button";
      btnExpandir.textContent = "Expandir";

      const timerSpan = document.createElement("span");
      timerSpan.className = "timer";
      timerSpan.dataset.deadline = tarefa.dataLimite || ""; // ISO ou vazio
      timerSpan.textContent = tarefa.dataLimite ? formatCountdown(new Date(tarefa.dataLimite)) : "";

      botoes.appendChild(btnEditar);
      botoes.appendChild(btnApagar);
      botoes.appendChild(btnExpandir);

      header.appendChild(botoes);
      header.appendChild(timerSpan);

      const h3 = document.createElement("h3");
      h3.textContent = tarefa.titulo;

      const p = document.createElement("p");
      p.textContent = tarefa.descricao;

      const meta = document.createElement("small");
      meta.textContent = `Criada em: ${new Date(tarefa.dataCriacao).toLocaleString()}`;

      // append
      card.appendChild(header);
      card.appendChild(h3);
      card.appendChild(p);
      card.appendChild(meta);

      tarefasContainer.appendChild(card);

      // eventos
      btnExpandir.addEventListener("click", () => abrirModalExpandida(tarefa));
      btnApagar.addEventListener("click", () => apagarTarefa(tarefa.id));
      btnEditar.addEventListener("click", () => abrirModalEdicao(tarefa));
    });
  }

  // formatação do countdown inicial (sem montar interval)
  function formatCountdown(deadlineDate) {
    if (!deadlineDate) return "";
    const now = new Date();
    const diff = deadlineDate - now;
    if (diff <= 0) return "Expirada";
    const dias = Math.floor(diff / (1000 * 60 * 60 * 24));
    const horas = Math.floor((diff / (1000 * 60 * 60)) % 24);
    const minutos = Math.floor((diff / (1000 * 60)) % 60);
    const segundos = Math.floor((diff / 1000) % 60);
    return `${dias}d ${horas}h ${minutos}m ${segundos}s`;
  }

  // atualizar todos os timers (executa com setInterval)
  function atualizarTimers() {
    const timers = document.querySelectorAll(".timer");
    const now = new Date();
    timers.forEach(t => {
      const iso = t.dataset.deadline;
      if (!iso) {
        t.textContent = "";
        return;
      }
      const deadline = new Date(iso);
      const diff = deadline - now;
      if (diff <= 0) {
        t.textContent = "⚠️ Expirada";
        t.classList.add("expired");
      } else {
        const dias = Math.floor(diff / (1000 * 60 * 60 * 24));
        const horas = Math.floor((diff / (1000 * 60 * 60)) % 24);
        const minutos = Math.floor((diff / (1000 * 60)) % 60);
        const segundos = Math.floor((diff / 1000) % 60);
        t.textContent = `${dias}d ${horas}h ${minutos}m ${segundos}s`;
      }
    });
  }

  // iniciar interval único (1s)
  setInterval(atualizarTimers, 1000);

  // abrir modal expandida (visual)
  function abrirModalExpandida(tarefa) {
    const modal = document.createElement("div");
    modal.className = "modal-expandida";
    modal.innerHTML = `
      <div class="modal-conteudo">
        <h2></h2>
        <p></p>
        <small></small>
        <button class="fechar-modal">Fechar</button>
      </div>
    `;
    const content = modal.querySelector(".modal-conteudo");
    content.querySelector("h2").textContent = tarefa.titulo;
    content.querySelector("p").textContent = tarefa.descricao;
    content.querySelector("small").textContent = `Criada em: ${new Date(tarefa.dataCriacao).toLocaleString()}${tarefa.dataLimite ? ' • Vence em: ' + new Date(tarefa.dataLimite).toLocaleString() : ''}`;

    document.body.appendChild(modal);
    modal.querySelector(".fechar-modal").addEventListener("click", () => modal.remove());
  }

  // apagar tarefa
  async function apagarTarefa(id) {
    if (!confirm("Deseja apagar esta tarefa?")) return;
    try {
      const resp = await apiFetch(`/tarefas/${id}`, { method: "DELETE" });
      if (resp.ok) {
        carregarTarefas();
      } else {
        alert("Erro ao apagar tarefa");
      }
    } catch (err) {
      alert("Erro de rede");
    }
  }

  // abrir modal de edição
  function abrirModalEdicao(tarefa) {
    editarTaskId = tarefa.id;
    editTitulo.value = tarefa.titulo || "";
    editDescricao.value = tarefa.descricao || "";

    // preenche data/hora se existir; aceita dataLimite em ISO ou apenas date
    if (tarefa.dataLimite) {
      const d = new Date(tarefa.dataLimite);
      editDataLimite.value = d.toISOString().slice(0,10);
      editHoraLimite.value = d.toTimeString().slice(0,5);
    } else {
      editDataLimite.value = "";
      editHoraLimite.value = "";
    }

    editModal.style.display = "flex";
    // Força reflow para garantir que a animação funcione
    editModal.offsetHeight;
    // Adiciona a classe show para ativar a animação
    setTimeout(() => {
      editModal.classList.add("show");
    }, 10);
  }

  // função para fechar modal com animação
  function fecharModal() {
    editModal.classList.remove("show");
    setTimeout(() => {
      editModal.style.display = "none";
      editarTaskId = null;
    }, 300); // tempo da animação
  }

  // salvar edição
  saveEditBtn.addEventListener("click", async () => {
    if (!editarTaskId) return;
    const novoTitulo = editTitulo.value.trim();
    const novaDescricao = editDescricao.value.trim();
    if (!novoTitulo || !novaDescricao) {
      alert("Preencha título e descrição.");
      return;
    }

    let dataLimiteISO = null;
    if (editDataLimite.value) {
      const hora = editHoraLimite.value || "00:00";
      dataLimiteISO = `${editDataLimite.value}T${hora}:00`;
    }

    const body = { titulo: novoTitulo, descricao: novaDescricao };
    if (dataLimiteISO) body.dataLimite = dataLimiteISO;
    else body.dataLimite = null; // permite limpar

    try {
      const resp = await apiFetch(`/tarefas/${editarTaskId}`, {
        method: "PUT",
        body: JSON.stringify(body)
      });

      if (resp.ok) {
        fecharModal();
        carregarTarefas();
      } else {
        const txt = await resp.text();
        alert(txt || "Erro ao salvar");
      }
    } catch (err) {
      alert("Erro de rede");
    }
  });

  // cancelar edição
  cancelEditBtn.addEventListener("click", () => {
    fecharModal();
  });

  // fechar modal ao clicar no overlay
  editModal.addEventListener("click", (e) => {
    if (e.target === editModal) {
      fecharModal();
    }
  });

  // logout
  document.getElementById("btnLogout").addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "index.html";
  });

  // inicializa
  carregarTarefas();
});








