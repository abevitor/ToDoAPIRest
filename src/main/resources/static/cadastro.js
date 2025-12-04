document.getElementById("cadastroForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const nome = document.getElementById("nome").value.trim();
  const email = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value;

  if (!nome || !email || !senha) {
    document.getElementById("mensagemCadastro").innerText = "Preencha todos os campos.";
    return;
  }

  try {
    const resposta = await fetch("http://localhost:8080/usuarios", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nome, email, senha })
    });

    if (resposta.ok) {
      document.getElementById("mensagemCadastro").innerText = "Cadastro realizado com sucesso!";
      setTimeout(() => {
        window.location.href = "index.html";
      }, 800);
    } else {
      const erro = await resposta.text();
      document.getElementById("mensagemCadastro").innerText = "Erro ao cadastrar: " + erro;
    }
  } catch (err) {
    document.getElementById("mensagemCadastro").innerText = "Erro de rede: " + err.message;
  }
});
