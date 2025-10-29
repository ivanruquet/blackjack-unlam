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


document.addEventListener("DOMContentLoaded", () => {
    const fichas = document.querySelectorAll(".boton-ficha-apuesta");
    const valorApuestaEl = document.getElementById("valor-apuesta");
    const saldoDisponibleEl = document.getElementById("saldoDisponible");
    const botonEmpezar = document.getElementById("boton");
    const inputMonto = document.getElementById("inputMonto");
    const botonesDecision = document.querySelectorAll(".boton-desicion");
    const mensajeEstrategiaDiv = document.getElementById("mensajeEstrategiaJS");
    const botonEstrategia = document.querySelector('form[th\\:action="@{/mostrarEstrategia}"] button');
    const formApuesta = document.getElementById("form-apuesta");
    const botonesDecision = document.querySelectorAll(".boton-deshabilitado, .boton-desicion");

    let saldo = parseFloat((saldoDisponibleEl?.textContent || "0").replace("$", "")) || 0;
    let apuesta = 0;
    let juegoIniciado = false;

    const setEstadoBotones = (botones, habilitar) => {
        botones.forEach(boton => {
            boton.disabled = !habilitar;
            boton.classList.toggle("boton-deshabilitado", !habilitar);
        });
    };

    setEstadoBotones(fichas, true);
    setEstadoBotones(botonesDecision, false);

    fichas.forEach(ficha => {
        ficha.addEventListener("click", (e) => {
            e.preventDefault();
            if (juegoIniciado) return;

            const valorFicha = parseInt(ficha.dataset.ficha, 10);
            if (saldo >= apuesta + valorFicha) {
                apuesta += valorFicha;
                valorApuestaEl.textContent = apuesta;
            } else {
                alert("Saldo insuficiente para apostar esa cantidad.");
            }
        });
    });

    botonEmpezar.addEventListener("click", () => {
        // NO preventDefault: el botón no es submit
        if (apuesta <= 0) { alert("Seleccioná al menos una ficha para apostar."); return; }
        if (saldo < apuesta) { alert("Saldo insuficiente."); return; }

        // seteo y bloqueo UI
        inputMonto.value = apuesta;
        saldo -= apuesta;
        if (saldoDisponibleEl) saldoDisponibleEl.textContent = `$${saldo.toFixed(2)}`;
        juegoIniciado = true;

        setEstadoBotones(fichas, false);
        setEstadoBotones(botonesDecision, true);
        botonesDecision.forEach(btn => { btn.disabled = false; btn.classList.remove("boton-deshabilitado"); });
        fichas.forEach(ficha => ficha.disabled = true);
        formApuesta.submit();
        botonEmpezar.disabled = true;
    });

    if (botonEstrategia) {
        botonEstrategia.addEventListener("click", async (event) => {
            event.preventDefault();

            try {
                const response = await fetch("/mostrarEstrategia", { method: "POST" });
                if (!response.ok) throw new Error("Error al obtener estrategia");

                const html = await response.text();
                mensajeEstrategiaDiv.textContent = html;
                mensajeEstrategiaDiv.style.display = "block";
            } catch (error) {
                console.error(error);
                mensajeEstrategiaDiv.textContent = "Error al obtener estrategia";
                mensajeEstrategiaDiv.style.display = "block";
            }
        });
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const mensajeResultadoDiv = document.getElementById("mensajeResultadoJS");

    const botonRendirse = document.querySelector('form[th\\:action="@{/rendirse}"] button');

    if (botonRendirse) {
        let resultadoVisible = false;

        botonRendirse.addEventListener("click", async (event) => {
            event.preventDefault();

            if (resultadoVisible) {
                mensajeResultadoDiv.style.display = "none";
                resultadoVisible = false;
                return;
            }

            try {
                const response = await fetch("/pararse", { method: "POST" });
                if (!response.ok) throw new Error("Error al obtener resultado");

                const texto = await response.text();

                mensajeResultadoDiv.textContent = texto;
                mensajeResultadoDiv.style.display = "block";
                resultadoVisible = true;

    });
});
            } catch (error) {
                console.error(error);
                mensajeResultadoDiv.textContent = "Error al obtener resultado";
                mensajeResultadoDiv.style.display = "block";
            }
        });
    }
});
