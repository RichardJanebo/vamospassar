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

document.getElementById("my_form").addEventListener('submit', (event) => {
    event.preventDefault();

    const name = document.getElementById("ip_name").value.trim();
    const email = document.getElementById("ip_email").value.trim();
    const password = document.getElementById("ip_password").value.trim();
    const number_cell = document.getElementById("ip_tel").value.trim();

    const isEmailValid = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);


    if (!name) return alert("O campo Nome não pode estar vazio.");
    if (!email) return alert("O campo Email não pode estar vazio.");
    if (!number_cell) return alert("O campo Telefone não pode estar vazio.");
    if (!password) return alert("O campo Senha não pode estar vazio.");

    if (!isEmailValid(email)) return alert("Formato de email inválido.");



    fetch("http://localhost:8080/authentication/register", {
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
    })
        .then(response => {
            if (response.ok) {
                console.log(response.json()
                )
            } else {
                alert("Erro ao registrar usuário.");
            }
        })
        .catch(() => alert("Erro de conexão com o servidor."));
});
