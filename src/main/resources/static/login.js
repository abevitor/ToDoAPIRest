document.getElementById("btnCadastro").addEventListener("click", function () {
  window.location.href = "cadastro.html";
});

document.getElementById("login-form").addEventListener("submit", async function (e) {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  const toggleButton = document.getElementById("toggleTheme");

 
  if (toggleButton) {
    toggleButton.addEventListener("click", () => {
      document.body.classList.toggle("light-theme");
      const isLight = document.body.classList.contains("light-theme");
      localStorage.setItem("tema", isLight ? "light" : "dark");
    });
  }

  const resposta = await fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, senha })
  });

  if (resposta.ok) {
    const token = await resposta.text();
    localStorage.setItem("token", token);
    window.location.href = "tarefas.html";
  } else {
    document.getElementById("erro").innerText = "Credenciais inválidas";
  }
});

