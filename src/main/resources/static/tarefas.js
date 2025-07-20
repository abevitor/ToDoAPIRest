document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");
  const tituloInput = document.getElementById("titulo");
  const descricaoInput = document.getElementById("descricao");
  const tarefasContainer = document.querySelector(".tarefas-container");
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "index.html";
    return;
  }

  async function carregarTarefas() {
    const resposta = await fetch("http://localhost:8080/tarefas", {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (resposta.ok) {
      const tarefas = await resposta.json();
      tarefasContainer.innerHTML = "";

      tarefas.forEach(tarefa => {
        const div = document.createElement("div");
        div.classList.add("tarefa");

        div.innerHTML = `
          <div class="botoes">
            <button class="editar">Editar</button>
            <button class="apagar">Apagar</button>
            <button class="expandir">Expandir</button>
          </div>
          <h3>${tarefa.titulo}</h3>
          <p>${tarefa.descricao}</p>
          <small>Criada em: ${new Date(tarefa.dataCriacao).toLocaleDateString()}</small>
        `;

        tarefasContainer.appendChild(div);

        const btnEditar = div.querySelector(".editar");
        const btnApagar = div.querySelector(".apagar");
        const btnExpandir = div.querySelector(".expandir");
        const tituloEl = div.querySelector("h3");
        const descricaoEl = div.querySelector("p");
        const botoesDiv = div.querySelector(".botoes");

        btnExpandir.addEventListener("click", () => abrirModal(tarefa));

        btnApagar.addEventListener("click", async () => {
          if (confirm("Deseja apagar esta tarefa?")) {
            const resp = await fetch(`http://localhost:8080/tarefas/${tarefa.id}`, {
              method: "DELETE",
              headers: { "Authorization": `Bearer ${token}` }
            });
            if (resp.ok) carregarTarefas();
            else alert("Erro ao apagar tarefa");
          }
        });

        btnEditar.addEventListener("click", () => {
          const inputTitulo = document.createElement("input");
          inputTitulo.value = tituloEl.textContent;
          inputTitulo.classList.add("input-edicao");

          const inputDescricao = document.createElement("textarea");
          inputDescricao.value = descricaoEl.textContent;
          inputDescricao.classList.add("input-edicao");

          const btnSalvar = document.createElement("button");
          btnSalvar.textContent = "Salvar";
          btnSalvar.classList.add("editar");

          const btnCancelar = document.createElement("button");
          btnCancelar.textContent = "Cancelar";
          btnCancelar.classList.add("apagar");

          tituloEl.style.display = "none";
          descricaoEl.style.display = "none";
          botoesDiv.style.display = "none";

          div.appendChild(inputTitulo);
          div.appendChild(inputDescricao);
          div.appendChild(btnSalvar);
          div.appendChild(btnCancelar);

          btnCancelar.addEventListener("click", () => {
            inputTitulo.remove();
            inputDescricao.remove();
            btnSalvar.remove();
            btnCancelar.remove();
            tituloEl.style.display = "block";
            descricaoEl.style.display = "block";
            botoesDiv.style.display = "flex";
          });

          btnSalvar.addEventListener("click", async () => {
            const novoTitulo = inputTitulo.value.trim();
            const novaDescricao = inputDescricao.value.trim();
            if (!novoTitulo || !novaDescricao) {
              alert("Preencha todos os campos.");
              return;
            }

            const resp = await fetch(`http://localhost:8080/tarefas/${tarefa.id}`, {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
              },
              body: JSON.stringify({ titulo: novoTitulo, descricao: novaDescricao })
            });

            if (resp.ok) carregarTarefas();
            else alert("Erro ao salvar alterações.");
          });
        });
      });
    } else {
      alert("Erro ao carregar tarefas");
    }
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const titulo = tituloInput.value.trim();
    const descricao = descricaoInput.value.trim();
    if (!titulo || !descricao) return;

    const resp = await fetch("http://localhost:8080/tarefas", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({ titulo, descricao })
    });

    if (resp.ok) {
      tituloInput.value = "";
      descricaoInput.value = "";
      carregarTarefas();
    } else {
      alert("Erro ao adicionar tarefa");
    }
  });

  document.getElementById("btnLogout").addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "index.html";
  });

  carregarTarefas();

  // --- Modal Expandir ---
  function abrirModal(tarefa) {
    const modal = document.createElement("div");
    modal.className = "modal-expandida";
    modal.innerHTML = `
      <div class="modal-conteudo">
        <h2>${tarefa.titulo}</h2>
        <p>${tarefa.descricao}</p>
        <small>Criada em: ${new Date(tarefa.dataCriacao).toLocaleDateString()}</small>
        <button class="fechar-modal">Fechar</button>
      </div>
    `;
    document.body.appendChild(modal);

    modal.querySelector(".fechar-modal").addEventListener("click", () => {
      modal.remove();
    });
  }
});









