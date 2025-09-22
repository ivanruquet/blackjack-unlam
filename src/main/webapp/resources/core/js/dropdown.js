document.addEventListener("DOMContentLoaded", () => {
    const botonPerfil = document.querySelector(".IconoPerfil");
    const menuPerfil = document.getElementById("menuPerfil");

    botonPerfil.addEventListener("click", (e) => {
        e.stopPropagation(); // evita que se cierre al instante
        menuPerfil.classList.toggle("show");
    });

    // Cerrar si se hace clic fuera
    document.addEventListener("click", () => {
        menuPerfil.classList.remove("show");
    });
});