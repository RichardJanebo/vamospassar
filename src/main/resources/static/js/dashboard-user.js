const btnOpenMenu = document.querySelector(".menu-toggle");
const btnCloseMenu = document.querySelector(".close_btn");
const btnToggleTheme = document.querySelector("#btn_tema");
const menu = document.getElementById("menu");
const body = document.body;

// Elementos para exibir o tempo e as provas
const timeSpan = document.querySelector("#time span");
const trialDiv = document.querySelector("#trial");



// Abertura do menu
btnOpenMenu.addEventListener("click", () => {
    menu.classList.toggle("active");
});



// Fechamento do menu
btnCloseMenu.addEventListener("click", () => {
    menu.classList.remove("active");
});


// Alternar tema
btnToggleTheme.addEventListener("click", () => {
    const root = document.documentElement;
    root.classList.toggle("theme-light");
    root.classList.toggle("theme-dark");
});



// ✅ Fetch e atualização dos dados
async function fetchUserProgress() {
    try {
        // Simulação de endpoint — troque pela sua URL real depois
        const response = await fetch("http://localhost:8080/api/findFreeDay");
        if (!response.ok) throw new Error("Erro ao buscar dados");

        const data = await response.json();

        const dias = data.days ?? 0;
        const provas = data.trial ?? 0;

        // Atualiza os elementos no HTML
        timeSpan.textContent = `${dias}/7`;
        trialDiv.innerHTML = `Provas resolvidas: <span>${provas}/4</span>`;

    } catch (error) {
        console.error("Erro ao carregar progresso:", error);
        timeSpan.textContent = "Erro";
        trialDiv.textContent = "Erro ao carregar";
    }
}

document.addEventListener("DOMContentLoaded", fetchUserProgress);
