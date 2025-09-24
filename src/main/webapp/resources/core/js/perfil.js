    function guardarPerfil() {
    localStorage.setItem("nombreDeUsuario", document.getElementById("nombreDeUsuario").innerText);
    localStorage.setItem("nombreYApellido", document.getElementById("nombreYApellido").innerText);
    localStorage.setItem("email", document.getElementById("email").innerText);
}

    function cargarPerfil() {
    if (localStorage.getItem("nombreDeUsuario")) {
    document.getElementById("nombreDeUsuario").innerText = localStorage.getItem("nombreDeUsuario");
    document.getElementById("nombreYApellido").innerText = localStorage.getItem("nombreYApellido");
    document.getElementById("email").innerText = localStorage.getItem("email");
}
}

    document.querySelectorAll("[contenteditable]").forEach(el => {
    el.addEventListener("input", guardarPerfil);
});
    cargarPerfil();

    const fileInput = document.getElementById("file");
    const profileImage = document.getElementById("idUs");

    fileInput.addEventListener("change", function(event) {
    const file = event.target.files[0];
    if (file) {
    const reader = new FileReader();

    reader.onload = function(e) {
    profileImage.src = e.target.result;
};

    reader.readAsDataURL(file);
}
});
