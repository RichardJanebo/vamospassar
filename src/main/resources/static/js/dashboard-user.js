const btnOpenMenu = document.querySelector(".menu-toggle");
const btnCloseMenu = document.querySelector(".close_btn");
const btnToggleTheme = document.querySelector("#btn_tema");
const menu = document.getElementById("menu");
const body = document.body;

btnOpenMenu.addEventListener("click", () => {
  menu.classList.toggle("active");
});

btnCloseMenu.addEventListener("click", () => {
  menu.classList.remove("active");
});

btnToggleTheme.addEventListener("click", () => {
  const root = document.documentElement;

    if (root.classList.contains("theme-light")) {
      root.classList.remove("theme-light");
      root.classList.add("theme-dark");
    } else {
      root.classList.remove("theme-dark");
      root.classList.add("theme-light");
    }
});

document.documentElement.classList.toggle("theme-dark");
