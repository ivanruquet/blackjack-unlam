document.addEventListener("DOMContentLoaded", () => {
    const nombreSpan = document.getElementById("nombre");
    const apellidoSpan = document.getElementById("apellido");
    const nombreInput = document.getElementById("nombreInput");
    const apellidoInput = document.getElementById("apellidoInput");
    const form = document.querySelector('form');

    if (!nombreSpan || !apellidoSpan || !nombreInput || !apellidoInput || !form) {
        console.error("Error: No se encontraron todos los elementos del formulario");
        return;
    }

    form.addEventListener('submit', (e) => {
        nombreInput.value = nombreSpan.textContent.trim();
        apellidoInput.value = apellidoSpan.textContent.trim();
    });

    nombreSpan.addEventListener("blur", () => {
        nombreInput.value = nombreSpan.textContent.trim();
    });

    apellidoSpan.addEventListener("blur", () => {
        apellidoInput.value = apellidoSpan.textContent.trim();
    });

    console.log("Perfil JS cargado correctamente");
});
