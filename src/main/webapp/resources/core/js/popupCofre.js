document.addEventListener("DOMContentLoaded", () => {
  const overlay = document.getElementById("overlay");
  const btnCofre = document.getElementById("btnCofre");
  const btnCerrar = document.getElementById("btnCerrar");

  btnCofre.addEventListener("click", () => {
    overlay.classList.add("active");
  });

  btnCerrar.addEventListener("click", () => {
    overlay.classList.remove("active");
  });

  overlay.addEventListener("click", (e) => {
    if (e.target === overlay) {
      overlay.classList.remove("active");
    }
  });
});

