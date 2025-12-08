document.addEventListener("DOMContentLoaded", () => {
const toggleButton = document.getElementById("toggleTheme");
const temaSalvo = localStorage.getItem("tema");


if (temaSalvo === "light") document.body.classList.add("light-theme");


if (toggleButton) {
toggleButton.onclick = () => {
document.body.classList.toggle("light-theme");
localStorage.setItem("tema", document.body.classList.contains("light-theme") ? "light" : "dark");
};
}
});