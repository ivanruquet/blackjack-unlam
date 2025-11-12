document.addEventListener("DOMContentLoaded", () => {
    const nombreSpan = document.getElementById("nombre");
    const apellidoSpan = document.getElementById("apellido");
    const usernameSpan = document.getElementById("username");

    const nombreInput = document.getElementById("nombreInput");
    const apellidoInput = document.getElementById("apellidoInput");
    const usernameInput = document.getElementById("usernameInput");

    const form = document.querySelector('form');

    if (!nombreSpan || !apellidoSpan  || !usernameSpan|| !nombreInput || !usernameInput || !apellidoInput || !form) {
        console.error("Error: No se encontraron todos los elementos del formulario");
        return;
    }

    form.addEventListener('submit', (e) => {
        nombreInput.value = nombreSpan.textContent.trim();
        apellidoInput.value = apellidoSpan.textContent.trim();
        usernameInput.value = usernameSpan.textContent.trim();

    });

    nombreSpan.addEventListener("blur", () => {
        nombreInput.value = nombreSpan.textContent.trim();
    });

    apellidoSpan.addEventListener("blur", () => {
        apellidoInput.value = apellidoSpan.textContent.trim();
    });

    usernameSpan.addEventListener("blur", () => {
        usernameInput.value = usernameSpan.textContent.trim();
    });
    console.log("Perfil JS cargado correctamente");
});


document.addEventListener("DOMContentLoaded", function() {
    const btnRecompensa = document.getElementById("btnRecompensa");

    const logro5Partidas = btnRecompensa.dataset.logro5partidas === "true";
    const logro2Manos = btnRecompensa.dataset.logro2manos === "true";
    const recompensaReclamada = btnRecompensa.dataset.recompensaReclamada === "true";

    if (logro5Partidas && logro2Manos && !recompensaReclamada) {
        btnRecompensa.disabled = false;
        btnRecompensa.style.backgroundColor = "#28a745";
        btnRecompensa.style.cursor = "pointer";
    } else {
        btnRecompensa.disabled = true;
        btnRecompensa.style.backgroundColor = "gray";
        btnRecompensa.style.cursor = "not-allowed";
    }
});




