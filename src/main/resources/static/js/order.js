const productsGrid =
    document.querySelector("#productsGrid");

const productsStatus =
    document.querySelector("#productsStatus");

const cartItems =
    document.querySelector("#cartItems");

const cartTotal =
    document.querySelector("#cartTotal");

const submitOrder =
    document.querySelector("#submitOrder");

const orderStatus =
    document.querySelector("#orderStatus");

const receiptPanel =
    document.querySelector("#receiptPanel");

const receiptOrderNumber =
    document.querySelector("#receiptOrderNumber");

const receiptInvoiceId =
    document.querySelector("#receiptInvoiceId");

const receiptTotal =
    document.querySelector("#receiptTotal");

let products = [];
const cart = new Map();

function money(value) {
    return `$${Number(value).toFixed(2)}`;
}

async function loadProducts() {

    try {

        const response =
            await fetch("/api/products");

        if (!response.ok) {
            throw new Error("No se pudieron cargar los productos");
        }

        products =
            await response.json();

        renderProducts();
        productsStatus.textContent =
            products.length
                ? "Selecciona cantidades para tu pedido."
                : "No hay productos disponibles.";

    } catch (error) {

        console.error("Error cargando productos", error);
        productsStatus.textContent =
            "No se pudieron cargar los productos.";
    }
}

function renderProducts() {

    productsGrid.innerHTML =
        "";

    products.forEach(product => {

        const card =
            document.createElement("article");

        const disabled =
            product.stock <= 0
                ? "disabled"
                : "";

        card.className =
            "product-card";

        card.innerHTML =
            `
            <div>
                <h3>${escapeHtml(product.name)}</h3>
                <p class="stock">Stock: ${product.stock}</p>
            </div>
            <strong>${money(product.price)}</strong>
            <button data-id="${product.id}" ${disabled}>
                Agregar
            </button>
        `;

        productsGrid.appendChild(card);
    });
}

function addToCart(productId) {

    const product =
        products.find(item =>
            item.id === productId);

    if (!product || product.stock <= 0) {
        return;
    }

    const current =
        cart.get(productId) || 0;

    if (current >= product.stock) {
        return;
    }

    cart.set(productId, current + 1);
    renderCart();
}

function updateQuantity(productId, quantity) {

    const product =
        products.find(item =>
            item.id === productId);

    if (!product) {
        return;
    }

    if (quantity <= 0) {
        cart.delete(productId);
    } else {
        cart.set(
            productId,
            Math.min(quantity, product.stock));
    }

    renderCart();
}

function renderCart() {

    cartItems.innerHTML =
        "";

    if (cart.size === 0) {

        cartItems.innerHTML =
            '<p class="empty-cart">Selecciona productos para iniciar.</p>';
        cartTotal.textContent =
            "$0.00";
        submitOrder.disabled =
            true;
        return;
    }

    let total = 0;

    cart.forEach((quantity, productId) => {

        const product =
            products.find(item =>
                item.id === productId);

        if (!product) {
            return;
        }

        const subtotal =
            Number(product.price) * quantity;

        total += subtotal;

        const item =
            document.createElement("div");

        item.className =
            "cart-item";

        item.innerHTML =
            `
            <div>
                <strong>${escapeHtml(product.name)}</strong>
                <span>${money(product.price)} c/u</span>
            </div>
            <input type="number"
                   min="0"
                   max="${product.stock}"
                   value="${quantity}"
                   data-id="${product.id}">
            <strong>${money(subtotal)}</strong>
        `;

        cartItems.appendChild(item);
    });

    cartTotal.textContent =
        money(total);
    submitOrder.disabled =
        false;
}

async function createOrder() {

    if (cart.size === 0) {
        return;
    }

    submitOrder.disabled =
        true;
    orderStatus.textContent =
        "Creando pedido...";

    const items =
        Array.from(cart.entries())
            .map(([productId, quantity]) => ({
                productId,
                quantity
            }));

    try {

        const response =
            await fetch(
                "/api/public/orders",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ items })
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            orderStatus.textContent =
                data.message ||
                "No se pudo crear el pedido.";
            submitOrder.disabled =
                false;
            return;
        }

        showReceipt(data);

    } catch (error) {

        console.error("Error creando pedido", error);
        orderStatus.textContent =
            "Error creando pedido.";
        submitOrder.disabled =
            false;
    }
}

function showReceipt(order) {

    receiptOrderNumber.textContent =
        order.orderNumber;
    receiptInvoiceId.textContent =
        order.invoiceId;
    receiptTotal.textContent =
        money(order.total);
    receiptPanel.hidden =
        false;
    receiptPanel.scrollIntoView({
        behavior: "smooth"
    });
}

function escapeHtml(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#039;");
}

productsGrid.addEventListener(
    "click",
    event => {

        const button =
            event.target.closest("button[data-id]");

        if (!button) {
            return;
        }

        addToCart(
            Number(button.dataset.id));
    }
);

cartItems.addEventListener(
    "input",
    event => {

        if (!event.target.matches("input[data-id]")) {
            return;
        }

        updateQuantity(
            Number(event.target.dataset.id),
            Number(event.target.value));
    }
);

submitOrder.addEventListener(
    "click",
    createOrder
);

loadProducts();
