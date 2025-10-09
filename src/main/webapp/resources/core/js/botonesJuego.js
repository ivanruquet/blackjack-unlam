document.addEventListener('DOMContentLoaded', () => {
  const botonesDesicion = document.querySelectorAll('.boton-desicion');
  const pozoElemento = document.getElementById('valor-apuesta');
  const botonesFicha = document.querySelectorAll('.boton-ficha-apuesta');
  const botonEmpezar = document.getElementById('boton');
  const formEmpezar = document.querySelector('.botones form');
  const saldoElemento = document.getElementById('saldoDisponible');

  let pozoTotal = 0;
  let apuestaConfirmada = false;

  botonesDesicion.forEach(btn => {
    btn.disabled = true;
    btn.classList.add('boton-deshabilitado');
  });

  function actualizarSaldo(montoApostado) {
    const saldoActual = parseFloat(saldoElemento.textContent.replace('$', '')) || 0;
    const nuevoSaldo = saldoActual - montoApostado;
    saldoElemento.textContent = `$${nuevoSaldo.toFixed(2)}`;
  }

  function obtenerSaldoActual() {
    return parseFloat(saldoElemento.textContent.replace('$', '')) || 0;
  }

  botonesFicha.forEach(boton => {
    boton.addEventListener('click', e => {
      e.preventDefault();
      if (apuestaConfirmada) return;

      const valorFicha = parseInt(boton.dataset.ficha, 10) || 0;
      const saldoActual = obtenerSaldoActual();

      if (pozoTotal + valorFicha > saldoActual) {
        alert('No cuenta con suficiente saldo para esta apuesta.');
        return;
      }

      pozoTotal += valorFicha;
      pozoElemento.textContent = pozoTotal;
    });
  });

  const manejarInicio = e => {
    e.preventDefault();
    if (apuestaConfirmada || pozoTotal <= 0) return;
    apuestaConfirmada = true;

    actualizarSaldo(pozoTotal);

    botonesFicha.forEach(ficha => {
      ficha.disabled = true;
      ficha.classList.add('boton-deshabilitado');
    });

    botonesDesicion.forEach(desicion => {
      desicion.disabled = false;
      desicion.classList.remove('boton-deshabilitado');
    });

    botonEmpezar.disabled = true;
    botonEmpezar.classList.add('boton-deshabilitado');
  };

  if (formEmpezar) {
    formEmpezar.addEventListener('submit', manejarInicio);
  } else if (botonEmpezar) {
    botonEmpezar.addEventListener('click', manejarInicio);
  }

  botonesDesicion.forEach(desicion => {
    desicion.addEventListener('click', () => {
      console.log(`Decisión tomada: ${desicion.textContent}`);
    });
  });
});

