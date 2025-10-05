const checkbox = document.getElementById('acepto');
const boton = document.getElementById('jugarBtn');

boton.disabled = !checkbox.checked;

checkbox.addEventListener('change', () => {
    boton.disabled = !checkbox.checked;
});