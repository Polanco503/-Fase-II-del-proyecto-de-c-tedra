const form = document.querySelector("#loginForm");
const statusText = document.querySelector("#status");
const emailInput = document.querySelector("#email");
const passwordInput = document.querySelector("#password");

function setSession(data) {
    localStorage.setItem("restaurantToken", data.token);
    localStorage.setItem("restaurantEmail", data.email);
    localStorage.setItem("restaurantRole", data.role);
    statusText.textContent = `Sesion activa: ${data.email} (${data.role})`;
    window.location.href = "/dashboard.html";
}

function loadSession() {
    const token = localStorage.getItem("restaurantToken");
    const email = localStorage.getItem("restaurantEmail");
    const role = localStorage.getItem("restaurantRole");

    if (!token) {
        statusText.textContent = "Sin sesion activa.";
        return;
    }

    statusText.textContent = `Sesion activa: ${email} (${role})`;
    window.location.href = "/dashboard.html";
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: emailInput.value,
            password: passwordInput.value
        })
    });

    const data = await response.json();

    if (!response.ok) {
        statusText.textContent = data.message || "Login rechazado.";
        return;
    }

    setSession(data);
});

document.querySelectorAll(".quick-users button").forEach((button) => {
    button.addEventListener("click", () => {
        emailInput.value = button.dataset.email;
        passwordInput.value = button.dataset.password;
    });
});

loadSession();
