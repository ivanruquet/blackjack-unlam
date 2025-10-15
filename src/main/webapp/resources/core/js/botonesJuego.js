document.addEventListener("DOMContentLoaded", () => {
    const fichas = document.querySelectorAll(".boton-ficha-apuesta");
    const valorApuestaEl = document.getElementById("valor-apuesta");
    const saldoDisponibleEl = document.getElementById("saldoDisponible");
    const botonEmpezar = document.getElementById("boton");
    const inputMonto = document.getElementById("inputMonto");
    const botonesDecision = document.querySelectorAll(".boton-desicion");

    let saldo = parseFloat(saldoDisponibleEl.textContent.replace("$", ""));
    let apuesta = 0;
    let juegoIniciado = false;

    botonesDecision.forEach(btn => {
        btn.disabled = true;
        btn.classList.add("boton-deshabilitado");
    });

    fichas.forEach(ficha => {
        ficha.addEventListener("click", (e) => {
            e.preventDefault();
            if (juegoIniciado) return;

            const valorFicha = parseInt(ficha.dataset.ficha);
            if (saldo >= apuesta + valorFicha) {
                apuesta += valorFicha;
                valorApuestaEl.textContent = apuesta;
            } else {
                alert("Saldo insuficiente para apostar esa cantidad.");
            }
        });
    });

    botonEmpezar.addEventListener("click", (e) => {
        e.preventDefault();

        if (apuesta <= 0) {
            alert("Seleccioná al menos una ficha para apostar.");
            return;
        }

        if (saldo < apuesta) {
            alert("Saldo insuficiente.");
            return;
        }

        saldo -= apuesta;
        saldoDisponibleEl.textContent = `$${saldo.toFixed(2)}`;
        inputMonto.value = apuesta;
        juegoIniciado = true;

        botonesDecision.forEach(btn => {
            btn.disabled = false;
            btn.classList.remove("boton-deshabilitado");
        });

        fichas.forEach(ficha => ficha.disabled = true);
        botonEmpezar.disabled = true;

    });
});


