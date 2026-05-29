const form = document.querySelector("#registerForm");

const email = document.querySelector("#registerEmail");

const password = document.querySelector("#registerPassword");

const confirmPassword =
    document.querySelector("#confirmPassword");

const status =
    document.querySelector("#registerStatus");

form.addEventListener("submit", async (event) => {

    event.preventDefault();

    if (password.value !== confirmPassword.value) {

        status.textContent =
            "Las contraseñas no coinciden";

        return;
    }

    const response = await fetch(
        "/api/auth/register",
        {
            method: "POST",
            headers: {
                "Content-Type":
                    "application/json"
            },
            body: JSON.stringify({
                email: email.value,
                password: password.value
            })
        }
    );

    const data = await response.json();

    if (!response.ok) {

        status.textContent =
            data.message ||
            "No fue posible registrar";

        return;
    }

    status.textContent =
        "Registro exitoso. Redirigiendo...";

    setTimeout(() => {

        window.location.href =
            "/login.html";

    }, 2000);
});