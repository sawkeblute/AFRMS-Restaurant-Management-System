import {
    TAX_RATE,
    USERS,
    ROLE_PERMISSIONS,
    MENU_DATA,
    CATEGORIES,
    INITIAL_INVENTORY,
    RECIPES,
    INITIAL_ORDERS
} from './data.js';

const STORAGE_KEY = 'afrms-state-v6-menu-images-beverages';
const currency = new Intl.NumberFormat('th-TH', { style: 'currency', currency: 'THB' });
const VIEW_LABELS = {
    dashboard: 'Dashboard',
    menu: 'Menu',
    pos: 'Point of Sale',
    orders: 'Kitchen Orders',
    inventory: 'Inventory',
    reports: 'Reports',
    audit: 'Audit Log',
    settings: 'Settings'
};
const PERMISSION_VIEWS = Object.keys(VIEW_LABELS);

class App {
    constructor() {
        this.currentView = 'dashboard';
        this.currentUser = null;
        this.cart = [];
        this.state = this.loadState();
        this.init();
    }

    init() {
        document.getElementById('login-form').addEventListener('submit', (event) => this.login(event));
        document.getElementById('logout-btn').addEventListener('click', () => this.logout());
        this.setupNavigation();
        this.refreshIcons();
    }

    loadState() {
        const saved = localStorage.getItem(STORAGE_KEY);
        if (saved) {
            const parsed = JSON.parse(saved);
            return {
                ...parsed,
                rolePermissions: this.normalizeRolePermissions(parsed.rolePermissions)
            };
        }

        return {
            menu: MENU_DATA.map(item => ({ ...item })),
            inventory: INITIAL_INVENTORY.map(item => ({ ...item })),
            orders: INITIAL_ORDERS.map(order => ({ ...order })),
            inventoryTransactions: [],
            auditLogs: [],
            rolePermissions: this.defaultRolePermissions()
        };
    }

    defaultRolePermissions() {
        return Object.fromEntries(
            Object.entries(ROLE_PERMISSIONS).map(([role, permissions]) => [role, [...permissions]])
        );
    }

    normalizeRolePermissions(savedPermissions = {}) {
        const defaults = this.defaultRolePermissions();
        return Object.fromEntries(Object.entries(defaults).map(([role, fallback]) => {
            const saved = Array.isArray(savedPermissions[role]) ? savedPermissions[role] : fallback;
            const valid = saved.filter(view => PERMISSION_VIEWS.includes(view));
            return [role, valid.length ? [...new Set(valid)] : [...fallback]];
        }));
    }

    persist() {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(this.state));
    }

    refreshIcons() {
        if (window.lucide) lucide.createIcons();
    }

    login(event) {
        event.preventDefault();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const user = USERS.find(item => item.username === username && item.password === password);

        if (!user) {
            document.getElementById('login-error').textContent = 'Invalid username or password.';
            return;
        }

        this.currentUser = user;
        document.getElementById('login-screen').hidden = true;
        document.querySelector('.sidebar').hidden = false;
        document.getElementById('main-content').hidden = false;
        document.getElementById('current-user-name').textContent = user.name;
        document.getElementById('current-user-role').textContent = user.role;
        this.applyRolePermissions();
        this.audit('LOGIN', user.id, 'User session started');
        this.switchView(this.firstAllowedView());
    }

    logout() {
        if (this.currentUser) this.audit('LOGOUT', this.currentUser.id, 'User session ended');
        this.currentUser = null;
        this.cart = [];
        document.getElementById('login-screen').hidden = false;
        document.querySelector('.sidebar').hidden = true;
        document.getElementById('main-content').hidden = true;
        document.getElementById('login-error').textContent = '';
        this.refreshIcons();
    }

    allowedViews() {
        return this.state.rolePermissions[this.currentUser?.role] || [];
    }

    firstAllowedView() {
        return this.allowedViews()[0] || 'dashboard';
    }

    canAccess(view) {
        return this.allowedViews().includes(view);
    }

    applyRolePermissions() {
        document.querySelectorAll('.nav-item').forEach(item => {
            const view = item.getAttribute('data-view');
            item.parentElement.hidden = !this.canAccess(view);
        });
    }

    setupNavigation() {
        document.querySelectorAll('.nav-item').forEach(item => {
            item.setAttribute('role', 'button');
            item.setAttribute('tabindex', '0');
            item.addEventListener('click', (event) => {
                event.preventDefault();
                this.switchView(item.getAttribute('data-view'));
            });
            item.addEventListener('keydown', (event) => {
                if (event.key !== 'Enter' && event.key !== ' ') return;
                event.preventDefault();
                this.switchView(item.getAttribute('data-view'));
            });
        });
    }

    switchView(view) {
        if (!this.canAccess(view)) {
            this.renderDenied(view);
            return;
        }

        document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));
        const active = document.querySelector(`[data-view="${view}"]`);
        if (active) active.classList.add('active');
        this.currentView = view;
        window.scrollTo(0, 0);
        this.renderView(view);
    }

    renderDenied(view) {
        document.getElementById('view-container').innerHTML = `
            <div class="animate-in empty-state">
                <i data-lucide="lock"></i>
                <h1>Access Restricted</h1>
                <p>Your role cannot open the ${view} module.</p>
            </div>`;
        this.refreshIcons();
    }

    renderView(view) {
        const container = document.getElementById('view-container');
        const template = document.getElementById(`${view}-template`);
        container.innerHTML = '';
        container.appendChild(template.content.cloneNode(true));

        const methods = {
            dashboard: () => this.initDashboard(),
            menu: () => this.initMenu(),
            pos: () => this.initPOS(),
            orders: () => this.initOrders(),
            inventory: () => this.initInventory(),
            reports: () => this.initReports(),
            audit: () => this.initAudit(),
            settings: () => this.initSettings()
        };
        methods[view]?.();
        this.refreshIcons();
    }

    audit(action, entityId, justification = '') {
        if (!this.currentUser) return;
        this.state.auditLogs.unshift({
            id: `AUD-${Date.now()}`,
            userId: this.currentUser.id,
            userName: this.currentUser.name,
            action,
            entityId,
            justification,
            timestamp: new Date().toISOString()
        });
        this.persist();
    }

    orderTotals(orders = this.state.orders) {
        const paidOrders = orders.filter(order => order.paymentStatus === 'Paid');
        const revenue = paidOrders.reduce((sum, order) => sum + order.total, 0);
        const cost = paidOrders.reduce((sum, order) => {
            return sum + order.items.reduce((itemSum, item) => itemSum + (item.cost * item.quantity), 0);
        }, 0);
        return {
            orders: paidOrders.length,
            revenue,
            cost,
            profit: revenue - cost,
            average: paidOrders.length ? revenue / paidOrders.length : 0
        };
    }

    lowStockItems() {
        return this.state.inventory.filter(item => item.quantity <= item.reorderLevel);
    }

    initDashboard() {
        const totals = this.orderTotals();
        document.getElementById('dashboard-clock').textContent = new Date().toLocaleString();
        document.getElementById('stat-revenue').textContent = currency.format(totals.revenue);
        document.getElementById('stat-orders').textContent = totals.orders;
        document.getElementById('stat-profit').textContent = currency.format(totals.profit);
        document.getElementById('stat-low-stock').textContent = this.lowStockItems().length;

        const recent = document.getElementById('recent-orders-list');
        recent.innerHTML = this.state.orders.slice(0, 6).map(order => `
            <div class="list-row">
                <div>
                    <strong>${order.id}</strong>
                    <span>${order.items.map(item => `${item.quantity}x ${item.name}`).join(', ')}</span>
                </div>
                <span class="pill">${order.status}</span>
            </div>`).join('') || this.emptyText('No orders recorded yet.');

        const lowStock = document.getElementById('low-stock-list');
        lowStock.innerHTML = this.lowStockItems().map(item => `
            <div class="list-row">
                <div><strong>${item.name}</strong><span>${this.formatQty(item.quantity)} ${item.unit} remaining</span></div>
                <span class="danger-text">Low</span>
            </div>`).join('') || this.emptyText('All ingredients are above reorder level.');
    }

    initMenu() {
        this.renderFilters();
        this.renderMenuItems('All');
        document.getElementById('menu-save-btn').addEventListener('click', () => {
            this.audit('MENU_SNAPSHOT', 'MenuItem', 'Manager saved current menu configuration');
            this.toast('Menu snapshot saved to audit log.');
        });
    }

    renderFilters() {
        const filterContainer = document.getElementById('category-filters');
        filterContainer.innerHTML = '';
        CATEGORIES.forEach(category => {
            const button = document.createElement('button');
            button.className = `filter-btn ${category === 'All' ? 'active' : ''}`;
            button.textContent = category;
            button.addEventListener('click', () => {
                document.querySelectorAll('.filter-btn').forEach(item => item.classList.remove('active'));
                button.classList.add('active');
                this.renderMenuItems(category);
            });
            filterContainer.appendChild(button);
        });
    }

    renderMenuItems(category) {
        const grid = document.getElementById('menu-grid');
        const items = category === 'All' ? this.state.menu : this.state.menu.filter(item => item.category === category);
        grid.innerHTML = items.map(item => `
            <article class="menu-card">
                <img src="${item.image}" alt="${item.name}" class="menu-image">
                <div class="menu-info">
                    <h3 class="menu-name">${item.name}</h3>
                    <p class="thai-name">${item.thaiName || ''}</p>
                    <p class="menu-desc">${item.description}</p>
                    <div class="menu-footer">
                        <span class="menu-price">${currency.format(item.price)}</span>
                        <span class="pill">${item.category}</span>
                    </div>
                </div>
            </article>`).join('');
    }

    initPOS() {
        const grid = document.getElementById('pos-menu-grid');
        grid.innerHTML = this.state.menu.map(item => `
            <button class="menu-card pos-card" data-id="${item.id}">
                <div class="menu-info">
                    <h3 class="menu-name">${item.name}</h3>
                    <p class="thai-name">${item.thaiName || ''}</p>
                    <span class="muted">${item.category}</span>
                    <span class="menu-price">${currency.format(item.price)}</span>
                </div>
            </button>`).join('');
        grid.querySelectorAll('[data-id]').forEach(card => {
            card.addEventListener('click', () => this.addToCart(Number(card.dataset.id)));
        });
        document.getElementById('checkout-btn').addEventListener('click', () => this.checkout());
        this.renderCart();
    }

    addToCart(id) {
        const item = this.state.menu.find(menuItem => menuItem.id === id);
        const existing = this.cart.find(cartItem => cartItem.id === id);
        if (existing) existing.quantity += 1;
        else this.cart.push({ ...item, quantity: 1 });
        this.renderCart();
    }

    removeFromCart(id) {
        const index = this.cart.findIndex(item => item.id === id);
        if (index < 0) return;
        if (this.cart[index].quantity > 1) this.cart[index].quantity -= 1;
        else this.cart.splice(index, 1);
        this.renderCart();
    }

    cartTotals() {
        const subtotal = this.cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
        const tax = subtotal * TAX_RATE;
        return { subtotal, tax, total: subtotal + tax };
    }

    renderCart() {
        const list = document.getElementById('cart-items');
        if (!list) return;
        list.innerHTML = this.cart.map(item => `
            <div class="cart-item">
                <div class="cart-item-info">
                    <h4>${item.name}</h4>
                    <span>${currency.format(item.price)} x ${item.quantity}</span>
                </div>
                <button class="icon-btn small" data-remove="${item.id}" title="Remove item"><i data-lucide="minus"></i></button>
            </div>`).join('') || this.emptyText('No items in this order.');
        list.querySelectorAll('[data-remove]').forEach(button => {
            button.addEventListener('click', () => this.removeFromCart(Number(button.dataset.remove)));
        });

        const totals = this.cartTotals();
        document.getElementById('cart-subtotal').textContent = currency.format(totals.subtotal);
        document.getElementById('cart-tax').textContent = currency.format(totals.tax);
        document.getElementById('cart-total-price').textContent = currency.format(totals.total);
        document.getElementById('payment-amount').value = totals.total ? totals.total.toFixed(2) : '';
        this.refreshIcons();
    }

    checkout() {
        const message = document.getElementById('pos-message');
        const totals = this.cartTotals();
        const paymentAmount = Number(document.getElementById('payment-amount').value);

        if (!this.cart.length) {
            message.textContent = 'Payment failed: the cart is empty.';
            return;
        }
        if (Number.isNaN(paymentAmount) || paymentAmount < totals.total) {
            message.textContent = 'Payment failed: The amount entered is less than the total.';
            return;
        }

        const stockError = this.validateStock();
        if (stockError) {
            message.textContent = stockError;
            return;
        }

        const order = {
            id: `ORD-${Math.floor(1000 + Math.random() * 9000)}`,
            userId: this.currentUser.id,
            items: this.cart.map(item => ({
                menuItemId: item.id,
                name: item.name,
                quantity: item.quantity,
                price: item.price,
                cost: item.cost
            })),
            subtotal: totals.subtotal,
            tax: totals.tax,
            total: totals.total,
            status: 'Preparing',
            paymentStatus: 'Paid',
            createdAt: new Date().toISOString()
        };

        this.deductInventory(order);
        this.state.orders.unshift(order);
        this.audit('ORDER_CREATED', order.id, `Paid order processed for ${currency.format(order.total)}`);
        this.cart = [];
        this.persist();
        this.toast(`Receipt ${order.id} processed for ${currency.format(order.total)}.`);
        this.switchView('dashboard');
    }

    validateStock() {
        for (const cartItem of this.cart) {
            const recipe = RECIPES[cartItem.id] || [];
            for (const step of recipe) {
                const ingredient = this.state.inventory.find(item => item.id === step.ingredientId);
                const required = step.amount * cartItem.quantity;
                if (!ingredient || ingredient.quantity < required) {
                    return `Order blocked: insufficient stock for ${ingredient?.name || step.ingredientId}.`;
                }
            }
        }
        return '';
    }

    deductInventory(order) {
        order.items.forEach(orderItem => {
            const recipe = RECIPES[orderItem.menuItemId] || [];
            recipe.forEach(step => {
                const ingredient = this.state.inventory.find(item => item.id === step.ingredientId);
                const quantity = step.amount * orderItem.quantity;
                ingredient.quantity = Number((ingredient.quantity - quantity).toFixed(2));
                this.state.inventoryTransactions.unshift({
                    ingredientId: ingredient.id,
                    action: 'DEDUCT',
                    quantity,
                    userId: this.currentUser.id,
                    orderId: order.id,
                    timestamp: new Date().toISOString(),
                    justification: `Recipe deduction for ${orderItem.name}`
                });
            });
        });
    }

    initOrders() {
        const grid = document.getElementById('live-orders-grid');
        const statuses = ['Preparing', 'Ready', 'Completed'];
        grid.className = 'kitchen-board';
        grid.innerHTML = statuses.map(status => {
            const orders = this.state.orders.filter(order => order.status === status);
            return `
                <section class="kitchen-column">
                    <div class="kitchen-column-header">
                        <h2>${status}</h2>
                        <span class="pill">${orders.length}</span>
                    </div>
                    <div class="kitchen-column-body">
                        ${orders.map(order => this.renderKitchenOrder(order)).join('') || this.emptyText('No orders in this stage.')}
                    </div>
                </section>`;
        }).join('');

        grid.querySelectorAll('[data-status]').forEach(button => {
            button.addEventListener('click', () => this.updateOrderStatus(button.dataset.status, button.dataset.next));
        });
    }

    renderKitchenOrder(order) {
        const created = new Date(order.createdAt);
        return `
            <article class="kitchen-ticket">
                <div class="order-card-header">
                    <div>
                        <h3>${order.id}</h3>
                        <p class="muted">${created.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} · ${this.minutesSince(created)} min</p>
                    </div>
                    <span class="pill ${order.status === 'Completed' ? '' : 'danger'}">${order.status}</span>
                </div>
                <div class="ticket-items">
                    ${order.items.map(item => `<div class="ticket-line"><strong>${item.quantity}x</strong><span>${item.name}</span></div>`).join('')}
                </div>
                <div class="button-row">
                    ${order.status !== 'Ready' && order.status !== 'Completed' ? `<button class="kitchen-action ready-action" data-status="${order.id}" data-next="Ready"><i data-lucide="bell-ring"></i><span>Mark Ready</span></button>` : ''}
                    ${order.status !== 'Completed' ? `<button class="kitchen-action complete-action" data-status="${order.id}" data-next="Completed"><i data-lucide="check-circle-2"></i><span>Complete</span></button>` : ''}
                </div>
            </article>`;
    }

    minutesSince(date) {
        return Math.max(0, Math.floor((Date.now() - date.getTime()) / 60000));
    }

    updateOrderStatus(orderId, status) {
        const order = this.state.orders.find(item => item.id === orderId);
        if (!order) return;
        order.status = status;
        this.audit('ORDER_STATUS_UPDATED', order.id, `Kitchen status changed to ${status}`);
        this.persist();
        this.toast(`${order.id} marked ${status}.`);
        this.initOrders();
    }

    initInventory() {
        const select = document.getElementById('stock-ingredient');
        select.innerHTML = this.state.inventory.map(item => `<option value="${item.id}">${item.name}</option>`).join('');
        document.getElementById('stock-form').addEventListener('submit', (event) => this.recordStock(event));
        this.renderInventoryTable();
    }

    recordStock(event) {
        event.preventDefault();
        const ingredientId = document.getElementById('stock-ingredient').value;
        const amount = Number(document.getElementById('stock-amount').value);
        const action = document.getElementById('stock-action').value;
        const note = document.getElementById('stock-note').value.trim();
        const ingredient = this.state.inventory.find(item => item.id === ingredientId);

        if (!ingredient || Number.isNaN(amount) || amount <= 0 || !note) {
            this.toast('Please enter a positive quantity and justification.', 'error');
            return;
        }

        ingredient.quantity = action === 'ADD'
            ? Number((ingredient.quantity + amount).toFixed(2))
            : Number(amount.toFixed(2));
        this.state.inventoryTransactions.unshift({
            ingredientId,
            action,
            quantity: amount,
            userId: this.currentUser.id,
            timestamp: new Date().toISOString(),
            justification: note
        });
        this.audit('INVENTORY_UPDATED', ingredientId, note);
        this.persist();
        this.toast(`${ingredient.name} stock updated.`);
        event.target.reset();
        this.initInventory();
    }

    renderInventoryTable() {
        document.getElementById('inventory-table').innerHTML = this.state.inventory.map(item => {
            const low = item.quantity <= item.reorderLevel;
            return `<tr>
                <td>${item.name}</td>
                <td>${this.formatQty(item.quantity)} ${item.unit}</td>
                <td>${this.formatQty(item.reorderLevel)} ${item.unit}</td>
                <td><span class="pill ${low ? 'danger' : ''}">${low ? 'Low Stock' : 'Healthy'}</span></td>
            </tr>`;
        }).join('');
    }

    initReports() {
        const totals = this.orderTotals();
        document.getElementById('report-revenue').textContent = currency.format(totals.revenue);
        document.getElementById('report-cost').textContent = currency.format(totals.cost);
        document.getElementById('report-profit').textContent = currency.format(totals.profit);
        document.getElementById('report-average').textContent = currency.format(totals.average);
        document.getElementById('report-table').innerHTML = this.state.orders.map(order => `
            <tr>
                <td>${order.id}</td>
                <td>${order.userId}</td>
                <td>${order.items.map(item => `${item.quantity}x ${item.name}`).join(', ')}</td>
                <td>${order.status}</td>
                <td>${currency.format(order.total)}</td>
            </tr>`).join('');
    }

    initAudit() {
        document.getElementById('audit-table').innerHTML = this.state.auditLogs.map(log => `
            <tr>
                <td>${new Date(log.timestamp).toLocaleString()}</td>
                <td>${log.userName}</td>
                <td>${log.action}</td>
                <td>${log.entityId}</td>
                <td>${log.justification}</td>
            </tr>`).join('') || `<tr><td colspan="5">No audit events recorded.</td></tr>`;
    }

    initSettings() {
        const container = document.getElementById('role-matrix');
        container.className = 'settings-panel';
        container.innerHTML = `
            <div class="settings-actions">
                <p class="muted">Choose which modules each role can access. Changes apply immediately after saving.</p>
                <div class="button-row">
                    <button id="rbac-save-btn" class="primary-btn" type="button"><i data-lucide="save"></i> Save Permissions</button>
                    <button id="rbac-reset-btn" class="secondary-btn" type="button"><i data-lucide="rotate-ccw"></i> Reset Defaults</button>
                </div>
            </div>
            <div class="role-grid">
                ${Object.keys(this.state.rolePermissions).map(role => this.renderRoleCard(role)).join('')}
            </div>
            <p id="settings-message" class="message-text" aria-live="polite"></p>`;

        document.getElementById('rbac-save-btn').addEventListener('click', () => this.saveRolePermissions());
        document.getElementById('rbac-reset-btn').addEventListener('click', () => this.resetRolePermissions());
        this.refreshIcons();
    }

    renderRoleCard(role) {
        const permissions = this.state.rolePermissions[role] || [];
        return `
            <section class="role-card">
                <div class="role-card-header">
                    <h2>${role}</h2>
                    <span class="pill">${permissions.length} modules</span>
                </div>
                <div class="permission-list">
                    ${PERMISSION_VIEWS.map(view => {
                        const checked = permissions.includes(view);
                        const protectsCurrentAdmin = role === this.currentUser?.role && view === 'settings';
                        return `
                            <label class="permission-toggle">
                                <input
                                    type="checkbox"
                                    data-role="${role}"
                                    data-permission="${view}"
                                    ${checked ? 'checked' : ''}
                                    ${protectsCurrentAdmin ? 'disabled' : ''}>
                                <span>${VIEW_LABELS[view]}</span>
                            </label>`;
                    }).join('')}
                </div>
            </section>`;
    }

    saveRolePermissions() {
        const nextPermissions = {};
        let hasEmptyRole = false;

        Object.keys(this.state.rolePermissions).forEach(role => {
            const checked = [...document.querySelectorAll(`[data-role="${role}"]:checked`)]
                .map(input => input.dataset.permission);
            if (role === this.currentUser?.role && !checked.includes('settings')) checked.push('settings');
            nextPermissions[role] = PERMISSION_VIEWS.filter(view => checked.includes(view));
            if (!nextPermissions[role].length) hasEmptyRole = true;
        });

        if (hasEmptyRole) {
            document.getElementById('settings-message').textContent = 'Every role needs at least one accessible module.';
            return;
        }

        this.state.rolePermissions = nextPermissions;
        this.audit('RBAC_UPDATED', 'RolePermissions', 'Role access matrix updated');
        this.persist();
        this.applyRolePermissions();
        this.toast('RBAC permissions saved.');

        if (!this.canAccess(this.currentView)) {
            this.switchView(this.firstAllowedView());
            return;
        }
        this.initSettings();
    }

    resetRolePermissions() {
        this.state.rolePermissions = this.defaultRolePermissions();
        this.audit('RBAC_RESET', 'RolePermissions', 'Role access matrix reset to defaults');
        this.persist();
        this.applyRolePermissions();
        this.toast('RBAC permissions reset to defaults.');
        this.initSettings();
    }

    formatQty(value) {
        return Number.isInteger(value) ? value : value.toFixed(2);
    }

    emptyText(text) {
        return `<p class="muted">${text}</p>`;
    }

    toast(text, type = 'success') {
        let toast = document.getElementById('app-toast');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'app-toast';
            toast.setAttribute('role', 'status');
            toast.setAttribute('aria-live', 'polite');
            document.body.appendChild(toast);
        }
        toast.className = `app-toast ${type === 'error' ? 'error' : ''}`;
        toast.textContent = text;
        clearTimeout(this.toastTimer);
        this.toastTimer = setTimeout(() => {
            toast.classList.add('hiding');
        }, 2600);
    }
}

window.app = new App();
