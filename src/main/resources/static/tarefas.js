document.addEventListener("DOMContentLoaded", async function () {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Você precisa estar logado.");
    window.location.href = "index.html";
    return;
  }

  try {
    const resposta = await fetch("http://localhost:8080/tarefas", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token
      }
    });

    if (resposta.ok) {
      const tarefas = await resposta.json();
      const container = document.getElementById("tarefasContainer");
      container.innerHTML = "";

      tarefas.forEach(tarefa => {
        const card = document.createElement("div");
        card.classList.add("tarefa-card");

        card.innerHTML = `
          <h3>${tarefa.nome}</h3>
          <p><strong>Descrição:</strong> ${tarefa.descricao}</p>
          <p class="status"><strong>Status:</strong> ${tarefa.concluida ? "Concluída ✅" : "Pendente ⏳"}</p>
        `;

        container.appendChild(card);
      });
    } else {
      alert("Erro ao carregar tarefas. Faça login novamente.");
      window.location.href = "index.html";
    }
  } catch (erro) {
    console.error("Erro ao buscar tarefas:", erro);
    alert("Erro inesperado. Tente novamente.");
  }
});

