
document.addEventListener("DOMContentLoaded", () => {
  const toggleButton = document.getElementById("toggleTheme");
  const temaSalvo = localStorage.getItem("tema");
  if (temaSalvo === "light") {
    document.body.classList.add("light-theme");
  }

  if (toggleButton) {
    toggleButton.addEventListener("click", () => {
      document.body.classList.toggle("light-theme");
      const isLight = document.body.classList.contains("light-theme");
      localStorage.setItem("tema", isLight ? "light" : "dark");
    });
  }
});
