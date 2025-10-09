const inputFecha = document.getElementById('fechaNacimiento');
    const error = document.getElementById('errorFecha');

    const hoy = new Date().toISOString().split('T')[0];
    inputFecha.max = hoy;

    inputFecha.addEventListener('change', () => {
        if (inputFecha.value > hoy) {
            error.style.display = 'inline';
            inputFecha.value = '';
        } else {
            error.style.display = 'none';
        }
    });