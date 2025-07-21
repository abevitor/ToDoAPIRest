document.getElementById("cadastroForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const nome = document.getElementById("nome").value;
  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  const resposta = await fetch("http://localhost:8080/usuarios", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nome, email, senha })
  });

  const toggleButton = document.getElementById("toggleTheme");

 
  if (toggleButton) {
    toggleButton.addEventListener("click", () => {
      document.body.classList.toggle("light-theme");
      const isLight = document.body.classList.contains("light-theme");
      localStorage.setItem("tema", isLight ? "light" : "dark");
    });
  }

  if (resposta.ok) {
    document.getElementById("mensagemCadastro").innerText = "Cadastro realizado com sucesso!";
    setTimeout(() => {
      window.location.href = "index.html";
    }, 1000);
  } else {
    const erro = await resposta.text();
    document.getElementById("mensagemCadastro").innerText = "Erro ao cadastrar: " + erro;
  }
});

