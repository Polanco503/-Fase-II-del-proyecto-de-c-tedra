const token = localStorage.getItem("restaurantToken");
const email = localStorage.getItem("restaurantEmail");
const role = localStorage.getItem("restaurantRole");

const sessionStatus = document.querySelector("#sessionStatus");
const result = document.querySelector("#result");
const logout = document.querySelector("#logout");
const showToken = document.querySelector("#showToken");
const refreshProducts = document.querySelector("#refreshProducts");
const productForm = document.querySelector("#productForm");
const productId = document.querySelector("#productId");
const productName = document.querySelector("#productName");
const productPrice = document.querySelector("#productPrice");
const productStock = document.querySelector("#productStock");
const cancelEdit = document.querySelector("#cancelEdit");
const adminNotice = document.querySelector("#adminNotice");
const productsContainer = document.querySelector("#products");

function authHeaders() {
    return token ? { Authorization: `Bearer ${token}` } : {};
}

function jsonHeaders() {
    return {
        "Content-Type": "application/json",
        ...authHeaders()
    };
}

function setResult(value) {
    result.textContent = JSON.stringify(value, null, 2);
}

function clearSession() {
    localStorage.removeItem("restaurantToken");
    localStorage.removeItem("restaurantEmail");
    localStorage.removeItem("restaurantRole");
    window.location.href = "/login.html";
}

async function readResponse(response) {
    const text = await response.text();

    try {
        return {
            status: response.status,
            body: text ? JSON.parse(text) : null
        };
    } catch {
        return {
            status: response.status,
            body: text
        };
    }
}

function requireSession() {
    if (!token) {
        window.location.href = "/login.html";
        return false;
    }

    sessionStatus.textContent = `${email} (${role})`;
    return true;
}

function applyRoleState() {
    const isAdmin = role === "ADMINISTRADOR";
    const canManageProducts = role === "ADMINISTRADOR" || role === "MESERO";

    productForm.hidden = !canManageProducts;

    if (isAdmin) {
        adminNotice.textContent = "Sesion administrador: puedes crear, editar y borrar productos.";
        return;
    }

    if (canManageProducts) {
        adminNotice.textContent = "Sesion mesero: puedes crear y editar productos. Solo administrador puede borrar.";
        return;
    }

    adminNotice.textContent = "Tu rol puede ver productos existentes.";
}

function resetProductForm() {
    productId.value = "";
    productName.value = "";
    productPrice.value = "";
    productStock.value = "";
}

async function loadProducts() {
    const response = await fetch("/api/products");
    const data = await readResponse(response);
    setResult(data);

    if (!response.ok || !Array.isArray(data.body)) {
        productsContainer.innerHTML = "<p>No se pudieron cargar productos.</p>";
        return;
    }

    if (data.body.length === 0) {
        productsContainer.innerHTML = "<p>No hay productos registrados.</p>";
        return;
    }

    productsContainer.innerHTML = "";
    data.body.forEach((product) => {
        const row = document.createElement("article");
        row.className = "product-row";

        const details = document.createElement("div");

        const name = document.createElement("div");
        name.className = "product-name";
        name.textContent = product.name;

        const meta = document.createElement("div");
        meta.className = "product-meta";
        meta.textContent = `ID ${product.id} | $${product.price} | Stock ${product.stock}`;

        details.append(name, meta);

        const actions = document.createElement("div");
        actions.className = "row-actions";

        if (role === "ADMINISTRADOR" || role === "MESERO") {
            const edit = document.createElement("button");
            edit.type = "button";
            edit.textContent = "Editar";
            edit.addEventListener("click", () => {
                productId.value = product.id;
                productName.value = product.name;
                productPrice.value = product.price;
                productStock.value = product.stock;
                productName.focus();
            });

            actions.append(edit);
        }

        if (role === "ADMINISTRADOR") {
            const remove = document.createElement("button");
            remove.type = "button";
            remove.textContent = "Borrar";
            remove.className = "danger";
            remove.addEventListener("click", () => deleteProduct(product.id));

            actions.append(remove);
        }

        row.append(details, actions);
        productsContainer.append(row);
    });
}

async function saveProduct(event) {
    event.preventDefault();

    const id = productId.value;
    const payload = {
        name: productName.value,
        price: Number(productPrice.value),
        stock: Number(productStock.value)
    };

    const response = await fetch(id ? `/api/products/${id}` : "/api/products", {
        method: id ? "PUT" : "POST",
        headers: jsonHeaders(),
        body: JSON.stringify(payload)
    });
    const data = await readResponse(response);
    setResult(data);

    if (response.ok) {
        resetProductForm();
        await loadProducts();
    }
}

async function deleteProduct(id) {
    const response = await fetch(`/api/products/${id}`, {
        method: "DELETE",
        headers: authHeaders()
    });
    const data = await readResponse(response);
    setResult(data);

    if (response.ok) {
        await loadProducts();
    }
}

document.querySelectorAll(".actions button").forEach((button) => {
    button.addEventListener("click", async () => {
        const response = await fetch(button.dataset.url, { headers: authHeaders() });
        const data = await readResponse(response);
        setResult(data);
    });
});

showToken.addEventListener("click", () => {
    setResult({
        token,
        email,
        role
    });
});

refreshProducts.addEventListener("click", loadProducts);
productForm.addEventListener("submit", saveProduct);
cancelEdit.addEventListener("click", resetProductForm);
logout.addEventListener("click", clearSession);

if (requireSession()) {
    applyRoleState();
    loadProducts();
}
