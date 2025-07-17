document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formNovaTarefa");
  const tituloInput = document.getElementById("titulo");
  const descricaoInput = document.getElementById("descricao");
  const tarefasContainer = document.getElementById("tarefasContainer");

  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "index.html";
    return;
  }


  document.getElementById("btnLogout").addEventListener("click", () => {
  localStorage.removeItem("token");
  window.location.href = "index.html"; 
});



  carregarTarefas();

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const titulo = tituloInput.value.trim();
    const descricao = descricaoInput.value.trim();

    if (!titulo || !descricao) return;

    const resposta = await fetch("http://localhost:8080/tarefas", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({ titulo, descricao })
    });

    if (resposta.ok) {
      tituloInput.value = "";
      descricaoInput.value = "";
      await carregarTarefas();
    } else {
      alert("Erro ao adicionar tarefa");
    }
  });

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
        div.className = "tarefa";
        div.innerHTML = `
          <h3>${tarefa.titulo}</h3>
          <p>${tarefa.descricao}</p>
          <small>Criada em: ${new Date(tarefa.dataCriacao).toLocaleDateString()}</small>
          <hr />
        `;
        tarefasContainer.appendChild(div);
      });
    } else {
      alert("Erro ao carregar tarefas");
    }
  }
});







