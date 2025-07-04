const btn_google = document.getElementById("btn_login_google");

btn_google.addEventListener('click',()=>{
    window.location.href="http://localhost:8080/oauth2/authorization/google"
})
document.getElementById("my_form").addEventListener('submit', async (event) => {
    event.preventDefault();
    const email = document.getElementById("ip_email").value;
    const password = document.getElementById("ip_password").value;

    try {
        const response = await fetch("http://localhost:8080/authentication/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();

            localStorage.setItem("token", data.token);
            alert(data)

            if (data.redirectURL) {
                window.location.href = data.redirectURL;
            } else {
                window.location.href = "/dashboard";
            }
        } else {
            console.error("Login inválido");
        }

    } catch (err) {
        alert("Falha na requisição");
        console.error(err);
    }
});
