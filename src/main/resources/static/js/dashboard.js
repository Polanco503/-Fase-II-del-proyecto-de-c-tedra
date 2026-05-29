const token = localStorage.getItem("restaurantToken");
const email = localStorage.getItem("restaurantEmail");
const role = localStorage.getItem("restaurantRole");

const sessionStatus =
    document.querySelector("#sessionStatus");

const logout =
    document.querySelector("#logout");

const menuItems =
    document.querySelectorAll(".menu-item");

const productFormPanel =
    document.querySelector("#productFormPanel");

const productForm =
    document.querySelector("#productForm");

const productFormTitle =
    document.querySelector("#productFormTitle");

const productIdInput =
    document.querySelector("#productId");

const productNameInput =
    document.querySelector("#productName");

const productPriceInput =
    document.querySelector("#productPrice");

const productStockInput =
    document.querySelector("#productStock");

const productStatus =
    document.querySelector("#productStatus");

const cancelProductEdit =
    document.querySelector("#cancelProductEdit");

const refreshProducts =
    document.querySelector("#refreshProducts");

const productsContainer =
    document.querySelector("#productsContainer");

const tableFormPanel =
    document.querySelector("#tableFormPanel");

const tableForm =
    document.querySelector("#tableForm");

const tableFormTitle =
    document.querySelector("#tableFormTitle");

const tableIdInput =
    document.querySelector("#tableId");

const tableNumberInput =
    document.querySelector("#tableNumber");

const tableCapacityInput =
    document.querySelector("#tableCapacity");

const tableStatus =
    document.querySelector("#tableStatus");

const cancelTableEdit =
    document.querySelector("#cancelTableEdit");

const refreshTables =
    document.querySelector("#refreshTables");

const tablesContainer =
    document.querySelector("#tablesContainer");

const userForm =
    document.querySelector("#userForm");

const userEmailInput =
    document.querySelector("#userEmail");

const userPasswordInput =
    document.querySelector("#userPassword");

const userRoleInput =
    document.querySelector("#userRole");

const userStatus =
    document.querySelector("#userStatus");

const refreshUsers =
    document.querySelector("#refreshUsers");

const usersTableBody =
    document.querySelector("#usersTableBody");

const usersStatus =
    document.querySelector("#usersStatus");

const refreshOrders =
    document.querySelector("#refreshOrders");

const ordersTableBody =
    document.querySelector("#ordersTableBody");

const ordersStatus =
    document.querySelector("#ordersStatus");

const refreshInvoices =
    document.querySelector("#refreshInvoices");

const invoicesTableBody =
    document.querySelector("#invoicesTableBody");

const invoicesStatus =
    document.querySelector("#invoicesStatus");

const invoiceReceiptPanel =
    document.querySelector("#invoiceReceiptPanel");

const invoiceReceiptOrder =
    document.querySelector("#invoiceReceiptOrder");

const invoiceReceiptId =
    document.querySelector("#invoiceReceiptId");

const invoiceReceiptTotal =
    document.querySelector("#invoiceReceiptTotal");

const invoiceReceiptItems =
    document.querySelector("#invoiceReceiptItems");

const sections = [
    "homeSection",
    "productsSection",
    "tablesSection",
    "ordersSection",
    "invoicesSection",
    "usersSection"
];

function requireSession() {

    if (!token) {

        window.location.href =
            "/login.html";

        return false;
    }

    sessionStatus.textContent =
        `${email} (${role})`;

    return true;
}

function logoutSession() {

    localStorage.removeItem("restaurantToken");
    localStorage.removeItem("restaurantEmail");
    localStorage.removeItem("restaurantRole");

    window.location.href =
        "/login.html";
}

function showSection(sectionId) {

    sections.forEach(id => {

        const section =
            document.getElementById(id);

        if (section) {

            section.hidden =
                id !== sectionId;
        }
    });

    menuItems.forEach(item => {

        item.classList.remove("active");

        if (
            item.dataset.section ===
            sectionId
        ) {

            item.classList.add("active");
        }
    });
}

async function loadStatistics() {

    try {

        const products =
            await fetch("/api/products")
                .then(r => r.json());

        document.getElementById(
            "totalProducts"
        ).textContent =
            products.length;

    } catch {

        document.getElementById(
            "totalProducts"
        ).textContent = "0";
    }

    try {

        const tables =
            await fetch(
                "/api/tables",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            ).then(r => r.json());

        document.getElementById(
            "totalTables"
        ).textContent =
            tables.length;

    } catch {

        document.getElementById(
            "totalTables"
        ).textContent = "0";
    }

    try {

        const orders =
            await fetch(
                "/api/orders",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            ).then(r => r.json());

        document.getElementById(
            "totalOrders"
        ).textContent =
            orders.length;

    } catch {

        document.getElementById(
            "totalOrders"
        ).textContent = "0";
    }

    try {

        const invoices =
            await fetch(
                "/api/facturas",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            ).then(r => r.json());

        document.getElementById(
            "totalInvoices"
        ).textContent =
            invoices.length;

    } catch {

        document.getElementById(
            "totalInvoices"
        ).textContent = "0";
    }
}

function applyRoleVisibility() {

    const usersButton =
        document.querySelector(
            '[data-section="usersSection"]'
        );

    const tablesButton =
        document.querySelector(
            '[data-section="tablesSection"]'
        );

    const tablesSummaryCard =
        document.querySelector(
            "#tablesSummaryCard"
        );

    const invoicesButton =
        document.querySelector(
            '[data-section="invoicesSection"]'
        );

    const invoicesSummaryCard =
        document.querySelector(
            "#invoicesSummaryCard"
        );

    if (
        usersButton &&
        role !== "ADMINISTRADOR"
    ) {

        usersButton.style.display =
            "none";
    }

    if (
        role !== "ADMINISTRADOR" &&
        tablesButton
    ) {

        tablesButton.style.display =
            "none";
    }

    if (
        role !== "ADMINISTRADOR" &&
        tablesSummaryCard
    ) {

        tablesSummaryCard.style.display =
            "none";
    }

    if (
        role !== "ADMINISTRADOR" &&
        invoicesButton
    ) {

        invoicesButton.style.display =
            "none";
    }

    if (
        role !== "ADMINISTRADOR" &&
        invoicesSummaryCard
    ) {

        invoicesSummaryCard.style.display =
            "none";
    }

    if (
        productFormPanel &&
        role === "ADMINISTRADOR"
    ) {

        productFormPanel.hidden =
            false;
    }

    if (
        tableFormPanel &&
        role === "ADMINISTRADOR"
    ) {

        tableFormPanel.hidden =
            false;
    }
}

async function loadProducts() {

    if (!productsContainer) {
        return;
    }

    try {

        const response =
            await fetch("/api/products");

        if (!response.ok) {

            throw new Error(
                "No se pudieron cargar los productos"
            );
        }

        const products =
            await response.json();

        productsContainer.innerHTML =
            "";

        products.forEach(product => {

            const card =
                document.createElement("div");

            card.className =
                "product-card";

            card.innerHTML =
                `
                <h3>${escapeHtml(product.name)}</h3>

                <div class="product-price">
                    $${product.price}
                </div>

                <div class="product-stock">
                    Stock: ${product.stock}
                </div>

                <div class="product-actions"></div>
            `;

            if (role === "ADMINISTRADOR") {

                const actions =
                    card.querySelector(
                        ".product-actions"
                    );

                actions.innerHTML =
                    `
                    <button class="btn-edit"
                            data-action="edit"
                            data-id="${product.id}">
                        Editar
                    </button>

                    <button class="btn-delete"
                            data-action="delete"
                            data-id="${product.id}">
                        Eliminar
                    </button>
                `;
            }

            productsContainer.appendChild(card);
        });

    } catch (error) {

        console.error(
            "Error cargando productos",
            error
        );
    }
}

function escapeHtml(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#039;");
}

function resetProductForm() {

    productForm.reset();
    productIdInput.value =
        "";
    productFormTitle.textContent =
        "Agregar producto";
    cancelProductEdit.hidden =
        true;
    productStatus.textContent =
        "";
}

async function readJsonResponse(response) {

    const text =
        await response.text();

    if (!text) {
        return {};
    }

    return JSON.parse(text);
}

async function saveProduct(event) {

    event.preventDefault();

    const editingId =
        productIdInput.value;

    if (editingId && role !== "ADMINISTRADOR") {

        productStatus.textContent =
            "Solo el administrador puede editar productos.";
        return;
    }

    const product =
        {
            name: productNameInput.value.trim(),
            price: Number(productPriceInput.value),
            stock: Number(productStockInput.value)
        };

    const url =
        editingId
            ? `/api/products/${editingId}`
            : "/api/products";

    const method =
        editingId
            ? "PUT"
            : "POST";

    try {

        const response =
            await fetch(
                url,
                {
                    method,
                    headers: {
                        "Content-Type": "application/json",
                        Authorization:
                            `Bearer ${token}`
                    },
                    body: JSON.stringify(product)
                }
            );

        const data =
            await readJsonResponse(response);

        if (!response.ok) {

            productStatus.textContent =
                data.message ||
                "No se pudo guardar el producto.";
            return;
        }

        resetProductForm();
        productStatus.textContent =
            editingId
                ? "Producto actualizado."
                : "Producto agregado.";
        await loadProducts();
        await loadStatistics();

    } catch (error) {

        console.error(
            "Error guardando producto",
            error
        );

        productStatus.textContent =
            "Error guardando producto.";
    }
}

async function handleProductAction(event) {

    const button =
        event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id =
        button.dataset.id;

    if (button.dataset.action === "edit") {

        await startProductEdit(id);
        return;
    }

    if (button.dataset.action === "delete") {

        await deleteProduct(id);
    }
}

async function startProductEdit(id) {

    if (role !== "ADMINISTRADOR") {
        return;
    }

    try {

        const response =
            await fetch(`/api/products/${id}`);

        const product =
            await response.json();

        if (!response.ok) {

            productStatus.textContent =
                product.message ||
                "No se pudo cargar el producto.";
            return;
        }

        productIdInput.value =
            product.id;
        productNameInput.value =
            product.name;
        productPriceInput.value =
            product.price;
        productStockInput.value =
            product.stock;
        productFormTitle.textContent =
            "Editar producto";
        cancelProductEdit.hidden =
            false;
        productNameInput.focus();

    } catch (error) {

        console.error(
            "Error cargando producto",
            error
        );

        productStatus.textContent =
            "Error cargando producto.";
    }
}

async function deleteProduct(id) {

    if (role !== "ADMINISTRADOR") {
        return;
    }

    const confirmed =
        window.confirm(
            "Deseas eliminar este producto?"
        );

    if (!confirmed) {
        return;
    }

    try {

        const response =
            await fetch(
                `/api/products/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {

            const data =
                await readJsonResponse(response);

            productStatus.textContent =
                data.message ||
                "No se pudo eliminar el producto.";
            return;
        }

        resetProductForm();
        productStatus.textContent =
            "Producto eliminado.";
        await loadProducts();
        await loadStatistics();

    } catch (error) {

        console.error(
            "Error eliminando producto",
            error
        );

        productStatus.textContent =
            "Error eliminando producto.";
    }
}

async function loadTables() {

    if (
        !tablesContainer ||
        role !== "ADMINISTRADOR"
    ) {
        return;
    }

    try {

        const response =
            await fetch(
                "/api/tables",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {

            throw new Error(
                "No se pudieron cargar las mesas"
            );
        }

        const tables =
            await response.json();

        tablesContainer.innerHTML =
            "";

        tables.forEach(table => {

            const card =
                document.createElement("div");

            card.className =
                "table-card";

            card.innerHTML =
                `
                <h3>Mesa ${table.tableNumber}</h3>

                <div class="table-capacity">
                    Capacidad: ${table.capacity}
                </div>

                <div class="table-actions"></div>
            `;

            if (role === "ADMINISTRADOR") {

                const actions =
                    card.querySelector(
                        ".table-actions"
                    );

                actions.innerHTML =
                    `
                    <button class="btn-edit"
                            data-action="edit"
                            data-id="${table.id}">
                        Editar
                    </button>

                    <button class="btn-delete"
                            data-action="delete"
                            data-id="${table.id}">
                        Eliminar
                    </button>
                `;
            }

            tablesContainer.appendChild(card);
        });

    } catch (error) {

        console.error(
            "Error cargando mesas",
            error
        );
    }
}

function resetTableForm() {

    tableForm.reset();
    tableIdInput.value =
        "";
    tableFormTitle.textContent =
        "Agregar mesa";
    cancelTableEdit.hidden =
        true;
    tableStatus.textContent =
        "";
}

async function saveTable(event) {

    event.preventDefault();

    if (role !== "ADMINISTRADOR") {

        tableStatus.textContent =
            "Solo el administrador puede guardar mesas.";
        return;
    }

    const editingId =
        tableIdInput.value;

    const table =
        {
            tableNumber: Number(tableNumberInput.value),
            capacity: Number(tableCapacityInput.value)
        };

    const url =
        editingId
            ? `/api/tables/${editingId}`
            : "/api/tables";

    const method =
        editingId
            ? "PUT"
            : "POST";

    try {

        const response =
            await fetch(
                url,
                {
                    method,
                    headers: {
                        "Content-Type": "application/json",
                        Authorization:
                            `Bearer ${token}`
                    },
                    body: JSON.stringify(table)
                }
            );

        const data =
            await readJsonResponse(response);

        if (!response.ok) {

            tableStatus.textContent =
                data.message ||
                "No se pudo guardar la mesa.";
            return;
        }

        resetTableForm();
        tableStatus.textContent =
            editingId
                ? "Mesa actualizada."
                : "Mesa agregada.";
        await loadTables();
        await loadStatistics();

    } catch (error) {

        console.error(
            "Error guardando mesa",
            error
        );

        tableStatus.textContent =
            "Error guardando mesa.";
    }
}

async function handleTableAction(event) {

    const button =
        event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id =
        button.dataset.id;

    if (button.dataset.action === "edit") {

        await startTableEdit(id);
        return;
    }

    if (button.dataset.action === "delete") {

        await deleteTable(id);
    }
}

async function startTableEdit(id) {

    if (role !== "ADMINISTRADOR") {
        return;
    }

    try {

        const response =
            await fetch(
                `/api/tables/${id}`,
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        const table =
            await response.json();

        if (!response.ok) {

            tableStatus.textContent =
                table.message ||
                "No se pudo cargar la mesa.";
            return;
        }

        tableIdInput.value =
            table.id;
        tableNumberInput.value =
            table.tableNumber;
        tableCapacityInput.value =
            table.capacity;
        tableFormTitle.textContent =
            "Editar mesa";
        cancelTableEdit.hidden =
            false;
        tableNumberInput.focus();

    } catch (error) {

        console.error(
            "Error cargando mesa",
            error
        );

        tableStatus.textContent =
            "Error cargando mesa.";
    }
}

async function deleteTable(id) {

    if (role !== "ADMINISTRADOR") {
        return;
    }

    const confirmed =
        window.confirm(
            "Deseas eliminar esta mesa?"
        );

    if (!confirmed) {
        return;
    }

    try {

        const response =
            await fetch(
                `/api/tables/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {

            const data =
                await readJsonResponse(response);

            tableStatus.textContent =
                data.message ||
                "No se pudo eliminar la mesa.";
            return;
        }

        resetTableForm();
        tableStatus.textContent =
            "Mesa eliminada.";
        await loadTables();
        await loadStatistics();

    } catch (error) {

        console.error(
            "Error eliminando mesa",
            error
        );

        tableStatus.textContent =
            "Error eliminando mesa.";
    }
}

async function createInternalUser(event) {

    event.preventDefault();

    if (role !== "ADMINISTRADOR") {
        return;
    }

    try {

        const response =
            await fetch(
                "/api/auth/register",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization:
                            `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        email: userEmailInput.value.trim(),
                        password: userPasswordInput.value,
                        role: userRoleInput.value
                    })
                }
            );

        const data =
            await readJsonResponse(response);

        if (!response.ok) {

            userStatus.textContent =
                data.message ||
                "No se pudo crear el usuario.";
            return;
        }

        userForm.reset();
        userStatus.textContent =
            data.message ||
            "Usuario creado correctamente.";
        await loadUsers();

    } catch (error) {

        console.error(
            "Error creando usuario",
            error
        );

        userStatus.textContent =
            "Error creando usuario.";
    }
}

async function loadUsers() {

    if (
        !usersTableBody ||
        role !== "ADMINISTRADOR"
    ) {
        return;
    }

    try {

        const response =
            await fetch(
                "/api/users",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {
            throw new Error("No se pudieron cargar los usuarios");
        }

        const users =
            await response.json();

        usersTableBody.innerHTML =
            "";

        if (users.length === 0) {

            usersTableBody.innerHTML =
                `
                <tr>
                    <td colspan="2">No hay usuarios registrados.</td>
                </tr>
                `;
            return;
        }

        users.forEach(user => {

            const row =
                document.createElement("tr");

            row.innerHTML =
                `
                <td>${escapeHtml(user.email)}</td>
                <td>
                    <span class="status-pill">
                        ${escapeHtml(user.role)}
                    </span>
                </td>
                `;

            usersTableBody.appendChild(row);
        });

        if (usersStatus) {
            usersStatus.textContent =
                "";
        }

    } catch (error) {

        console.error(
            "Error cargando usuarios",
            error
        );

        if (usersStatus) {
            usersStatus.textContent =
                "No se pudieron cargar los usuarios.";
        }
    }
}

function money(value) {

    return `$${Number(value || 0).toFixed(2)}`;
}

function formatDate(value) {

    if (!value) {
        return "Sin fecha";
    }

    return new Date(value)
        .toLocaleString(
            "es-SV",
            {
                dateStyle: "short",
                timeStyle: "short"
            }
        );
}

function formatOrderItems(items) {

    if (!items || items.length === 0) {
        return "Sin productos";
    }

    return items.map(item =>
        `${item.quantity} x ${item.productName}`)
        .join(", ");
}

async function loadOrders() {

    if (!ordersTableBody) {
        return;
    }

    try {

        const response =
            await fetch(
                "/api/orders",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {
            throw new Error("No se pudieron cargar los pedidos");
        }

        const orders =
            await response.json();

        ordersTableBody.innerHTML =
            "";

        if (orders.length === 0) {

            ordersTableBody.innerHTML =
                `
                <tr>
                    <td colspan="6">No hay pedidos registrados.</td>
                </tr>
                `;
            return;
        }

        orders.forEach(order => {

            const row =
                document.createElement("tr");

            row.innerHTML =
                `
                <td>${escapeHtml(order.orderNumber || `#${order.id}`)}</td>
                <td>
                    <span class="status-pill">
                        ${escapeHtml(order.status)}
                    </span>
                </td>
                <td>${escapeHtml(formatOrderItems(order.items))}</td>
                <td>${money(order.total)}</td>
                <td>${formatDate(order.createdAt)}</td>
                <td>
                    <select data-order-id="${order.id}">
                        ${renderStatusOptions(order.status)}
                    </select>
                </td>
                `;

            ordersTableBody.appendChild(row);
        });

        if (ordersStatus) {
            ordersStatus.textContent =
                "";
        }

    } catch (error) {

        console.error(
            "Error cargando pedidos",
            error
        );

        if (ordersStatus) {
            ordersStatus.textContent =
                "No se pudieron cargar los pedidos.";
        }
    }
}

function renderStatusOptions(currentStatus) {

    const statuses = [
        "PENDIENTE",
        "EN_PREPARACION",
        "LISTO",
        "ENTREGADO",
        "CANCELADO"
    ];

    return statuses.map(status => {

        const selected =
            status === currentStatus
                ? "selected"
                : "";

        return `<option value="${status}" ${selected}>${status}</option>`;
    }).join("");
}

async function updateOrderStatus(event) {

    if (!event.target.matches("select[data-order-id]")) {
        return;
    }

    const select =
        event.target;

    try {

        const response =
            await fetch(
                `/api/orders/${select.dataset.orderId}/status`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization:
                            `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        status: select.value
                    })
                }
            );

        const data =
            await readJsonResponse(response);

        if (!response.ok) {

            if (ordersStatus) {
                ordersStatus.textContent =
                    data.message ||
                    "No se pudo actualizar el pedido.";
            }
            await loadOrders();
            return;
        }

        if (ordersStatus) {
            ordersStatus.textContent =
                "Estado actualizado.";
        }
        await loadOrders();

    } catch (error) {

        console.error(
            "Error actualizando pedido",
            error
        );

        if (ordersStatus) {
            ordersStatus.textContent =
                "Error actualizando pedido.";
        }
        await loadOrders();
    }
}

async function loadInvoices() {

    if (
        !invoicesTableBody ||
        role !== "ADMINISTRADOR"
    ) {
        return;
    }

    try {

        const response =
            await fetch(
                "/api/facturas",
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {
            throw new Error("No se pudieron cargar las facturas");
        }

        const invoices =
            await response.json();

        invoicesTableBody.innerHTML =
            "";

        if (invoices.length === 0) {

            invoicesTableBody.innerHTML =
                `
                <tr>
                    <td colspan="5">No hay facturas registradas.</td>
                </tr>
                `;
            return;
        }

        invoices.forEach(invoice => {

            const row =
                document.createElement("tr");

            row.innerHTML =
                `
                <td>#${invoice.id}</td>
                <td>${escapeHtml(invoice.orderNumber || `#${invoice.orderId}`)}</td>
                <td>${money(invoice.total)}</td>
                <td>${formatDate(invoice.fechaFactura)}</td>
                <td>
                    <button class="secondary-btn"
                            data-invoice-id="${invoice.id}"
                            data-order-id="${invoice.orderId}"
                            data-order-number="${escapeHtml(invoice.orderNumber || `#${invoice.orderId}`)}"
                            data-total="${invoice.total}">
                        Ver
                    </button>
                </td>
                `;

            invoicesTableBody.appendChild(row);
        });

        if (invoicesStatus) {
            invoicesStatus.textContent =
                "";
        }

    } catch (error) {

        console.error(
            "Error cargando facturas",
            error
        );

        if (invoicesStatus) {
            invoicesStatus.textContent =
                "No se pudieron cargar las facturas.";
        }
    }
}

async function showInvoiceReceipt(event) {

    const button =
        event.target.closest("button[data-invoice-id]");

    if (!button || !invoiceReceiptPanel) {
        return;
    }

    try {

        const response =
            await fetch(
                `/api/orders/${button.dataset.orderId}`,
                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        const order =
            await response.json();

        if (!response.ok) {
            throw new Error("No se pudo cargar el comprobante");
        }

        invoiceReceiptOrder.textContent =
            order.orderNumber || button.dataset.orderNumber;
        invoiceReceiptId.textContent =
            button.dataset.invoiceId;
        invoiceReceiptTotal.textContent =
            money(button.dataset.total);
        invoiceReceiptItems.textContent =
            formatOrderItems(order.items);
        invoiceReceiptPanel.hidden =
            false;
        invoiceReceiptPanel.scrollIntoView({
            behavior: "smooth",
            block: "nearest"
        });

    } catch (error) {

        console.error(
            "Error cargando comprobante",
            error
        );

        if (invoicesStatus) {
            invoicesStatus.textContent =
                "No se pudo cargar el comprobante.";
        }
    }
}

menuItems.forEach(item => {

    item.addEventListener(
        "click",
        () => {

            showSection(
                item.dataset.section
            );
        }
    );
});

logout.addEventListener(
    "click",
    logoutSession
);

if (productForm) {

    productForm.addEventListener(
        "submit",
        saveProduct
    );
}

if (cancelProductEdit) {

    cancelProductEdit.addEventListener(
        "click",
        resetProductForm
    );
}

if (refreshProducts) {

    refreshProducts.addEventListener(
        "click",
        async () => {

            await loadProducts();
            await loadStatistics();
        }
    );
}

if (productsContainer) {

    productsContainer.addEventListener(
        "click",
        handleProductAction
    );
}

if (tableForm) {

    tableForm.addEventListener(
        "submit",
        saveTable
    );
}

if (cancelTableEdit) {

    cancelTableEdit.addEventListener(
        "click",
        resetTableForm
    );
}

if (refreshTables) {

    refreshTables.addEventListener(
        "click",
        async () => {

            await loadTables();
            await loadStatistics();
        }
    );
}

if (tablesContainer) {

    tablesContainer.addEventListener(
        "click",
        handleTableAction
    );
}

if (userForm) {

    userForm.addEventListener(
        "submit",
        createInternalUser
    );
}

if (refreshOrders) {

    refreshOrders.addEventListener(
        "click",
        async () => {

            await loadOrders();
            await loadStatistics();
        }
    );
}

if (ordersTableBody) {

    ordersTableBody.addEventListener(
        "change",
        updateOrderStatus
    );
}

if (refreshInvoices) {

    refreshInvoices.addEventListener(
        "click",
        async () => {

            await loadInvoices();
            await loadStatistics();
        }
    );
}

if (invoicesTableBody) {

    invoicesTableBody.addEventListener(
        "click",
        showInvoiceReceipt
    );
}

if (refreshUsers) {

    refreshUsers.addEventListener(
        "click",
        loadUsers
    );
}

if (requireSession()) {

    applyRoleVisibility();

    loadStatistics();

    loadProducts();

    loadTables();

    loadOrders();

    loadInvoices();

    loadUsers();

    showSection(
        "homeSection"
    );
}
