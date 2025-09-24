const botonesDesicion = document.querySelectorAll('.boton-desicion');

const pozoElemento = document.getElementById('valor-apuesta');
const botonesFicha = document.querySelectorAll('.boton-ficha-apuesta');

let pozoTotal = 0;

botonesFicha.forEach(boton => {
    boton.addEventListener('click', () => {
        const valorFicha = parseInt(boton.dataset.ficha);
        pozoTotal += valorFicha;
        pozoElemento.textContent = pozoTotal;

        if(pozoTotal>0){
            botonesDesicion.forEach(desicion => {
                desicion.classList.remove('boton-deshabilitado');
                desicion.disabled=false;
            })
        }

    });
});

botonesDesicion.forEach(desicion => {
    desicion.addEventListener('click', () => {
        botonesFicha.forEach(ficha => {
            ficha.disabled = true;
            ficha.classList.add('boton-deshabilitado');
        });

    });
});
