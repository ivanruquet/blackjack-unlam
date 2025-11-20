document.addEventListener("DOMContentLoaded", () => {
    const botonPerfil = document.querySelector(".IconoPerfil");
    const menuPerfil = document.getElementById("menuPerfil");

    botonPerfil.addEventListener("click", (e) => {
        e.stopPropagation();
        menuPerfil.classList.toggle("show");
    });

    document.addEventListener("click", () => {
        menuPerfil.classList.remove("show");
    });
});