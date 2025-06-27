document.getElementById("ip_tel").addEventListener("input", (event) => {
    let input = event.target.value.replace(/\D/g, "");
    if (input.length > 11) input = input.slice(0, 11);

    if (input.length <= 10) {
        input = input.replace(/(\d{2})(\d{4})(\d{0,4})/, "($1) $2-$3");
    } else {
        input = input.replace(/(\d{2})(\d{5})(\d{0,4})/, "($1) $2-$3");
    }

    event.target.value = input;
});

document.getElementById("my_form").addEventListener('submit', async (event) => {
    event.preventDefault();

    const submitButton = document.getElementById("submit_button");
    const defaultButtonText = "Registrar";

    // Função para ativar loading
    const setLoading = () => {
        submitButton.disabled = true;
        submitButton.value = "Registrando... ⏳";
    };


    // Função para resetar botão
    const resetButton = (text = defaultButtonText) => {
        submitButton.disabled = false;
        submitButton.value = "Registre-se";
    };


    // Começa loading
    setLoading();

    // Captura valores
    const name = document.getElementById("ip_name").value.trim();
    const email = document.getElementById("ip_email").value.trim();
    const password = document.getElementById("ip_password").value.trim();
    const number_cell = document.getElementById("ip_tel").value.trim();

    const isEmailValid = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

    // Validação simples
    if (!name || !email || !number_cell || !password) {
        alert("Todos os campos devem ser preenchidos.");
        resetButton();
        return;
    }

    if (!isEmailValid(email)) {
        alert("Formato de email inválido.");
        resetButton();
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/authentication/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password,
                name: name,
                number_cell: number_cell
            })
        });

        if (response.ok) {
            submitButton.value = "✅ Registrado!";

            window.location.href = "http://localhost:8080/login"


        } else {
            const error = await response.text();
            alert("Erro ao registrar usuário: " + error);
            resetButton();
        }

    } catch (error) {
        alert("Erro de conexão com o servidor.");
        resetButton();
    }
});
