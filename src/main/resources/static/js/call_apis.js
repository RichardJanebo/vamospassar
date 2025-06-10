const find_questions = document.getElementById("find_questions");

find_questions.addEventListener('click', async () => {

    const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });

    const results = await chrome.scripting.executeScript({
        target: { tabId: tab.id },
        function: read_question,
    });

    const questions = results[0].result;

    push_question(questions);

});

const push_question = (questions) => {
    fetch("http://localhost:8080/api/get_response",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ "questions": questions })
        }
    )
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro na resposta do servidor");
            }

            return response.json();
        })
        .then(data => {
            console.log("Resposta do servidor:", data);
            createResponse(data.Resposta);
        })
        .catch(error => {
            console.error("Erro ao enviar as perguntas:", error);
        });
}

// Esta função será executada dentro da aba, precisa ser independente
const read_question = () => {
    const elementos = document.querySelectorAll('.panel.panel-default');
    let questions = "";

    elementos.forEach((elemento) => {
        const texto = elemento.textContent.trim();
        if (texto.startsWith('1)') || texto.startsWith('2)') || texto.startsWith('3)') || texto.startsWith('4)') || texto.startsWith('5)')) {
            questions += texto + "\n";
        }
    });

    return questions;
}

const click_on_questions = (resposta) => {
    console.log(resposta);
}

const createResponse = (response) => {
    const responseQuestion = document.getElementById("responseQuestion");


    for (i = 0; i < response.length; i++) {
        const li = document.createElement("li");
        li.textContent = response[i];

        responseQuestion.appendChild(li);

    }

    responseQuestion.style.display = "block";







}


document.getElementById("testeWIndo").addEventListener('click', () => {
    alert("Hello")
    window.location.href = "http://localhost:8080/teste";

})
