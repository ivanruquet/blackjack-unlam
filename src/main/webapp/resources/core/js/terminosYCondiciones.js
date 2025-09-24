const checkbox = document.getElementById("acepto");
const jugarBtn = document.getElementById("jugarBtn");

checkbox.addEventListener("change", function() {
    jugarBtn.disabled = !this.checked;
});