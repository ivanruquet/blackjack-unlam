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
