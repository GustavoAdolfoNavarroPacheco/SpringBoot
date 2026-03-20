/* ========================
   LOGITRACK — APP.JS
   Conexion via fetch al API REST (Spring Boot)
   ======================== */

const API = 'http://localhost:8080/api';
// const API = 'http://10.117.47.37:8080/api';

let token = null;
let currentUser = null;
let bodegas = [];
let productos = [];

// IDs de edicion activos
let editBodegaId = null;
let editProductoId = null;

/* ======================== UTILIDADES ======================== */

function showPage(id) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

function switchTab(tabId, el) {
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  document.getElementById(tabId).classList.add('active');
  if (el) el.classList.add('active');
  if (tabId === 'tab-dashboard') loadDashboard();
  if (tabId === 'tab-bodegas') loadBodegas();
  if (tabId === 'tab-productos') loadProductos();
  if (tabId === 'tab-movimientos') loadMovimientos();
  if (tabId === 'tab-auditorias') loadAuditorias();
}

function openModal(id) {
  if (id === 'modal-bodega') {
    editBodegaId = null;
    document.getElementById('modal-bodega-titulo').textContent = 'Nueva Bodega';
    document.getElementById('bod-nombre').value = '';
    document.getElementById('bod-ubicacion').value = '';
    document.getElementById('bod-capacidad').value = '';
    loadPersonasSelect();
  }
  if (id === 'modal-producto') {
    editProductoId = null;
    document.getElementById('modal-producto-titulo').textContent = 'Nuevo Producto';
    document.getElementById('prod-nombre').value = '';
    document.getElementById('prod-categoria').value = '';
    document.getElementById('prod-stock').value = '0';
    document.getElementById('prod-precio').value = '';
    loadBodegasSelect('prod-bodega');
  }
  if (id === 'modal-movimiento') {
    loadBodegasSelect('mov-origen');
    loadBodegasSelect('mov-destino');
    loadProductosSelect();
    setDefaultDateTime();
    toggleBodegas();
    if (document.getElementById('productos-rows').children.length === 0) addProductoRow();
  }
  document.getElementById(id).classList.remove('hidden');
}

function closeModal(id) {
  document.getElementById(id).classList.add('hidden');
  document.querySelectorAll(`#${id} .error-msg`).forEach(e => e.classList.add('hidden'));
  document.querySelectorAll(`#${id} input`).forEach(i => i.value = '');
}

function closeModalOutside(event, id) {
  if (event.target.id === id) closeModal(id);
}

function showToast(msg, type = 'ok') {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.className = `toast ${type}`;
  t.classList.remove('hidden');
  setTimeout(() => t.classList.add('hidden'), 3500);
}

function showError(id, msg) {
  const el = document.getElementById(id);
  el.textContent = msg;
  el.classList.remove('hidden');
}

function formatDate(str) {
  if (!str) return '—';
  const d = new Date(str);
  return d.toLocaleDateString('es-CO') + ' ' + d.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
}

function badgeHTML(tipo) {
  const cls = { ENTRADA: 'badge-entrada', SALIDA: 'badge-salida', TRANSFERENCIA: 'badge-transferencia' };
  return `<span class="mini-badge ${cls[tipo] || ''}">${tipo}</span>`;
}

function setDefaultDateTime() {
  const now = new Date();
  now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
  document.getElementById('mov-fecha').value = now.toISOString().slice(0, 16);
}

/* ======================== API HELPER ======================== */

async function apiFetch(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API}${path}`, { ...options, headers });

  if (res.status === 401) {
    doLogout();
    throw new Error('Sesion expirada. Por favor inicia sesion de nuevo.');
  }

  const text = await res.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }

  if (!res.ok) {
    const msg = (data && (data.message || data.error || data.mensaje)) || `Error ${res.status}`;
    throw new Error(msg);
  }
  return data;
}

/* ======================== AUTH ======================== */

async function doLogin() {
  const email = document.getElementById('login-email').value.trim();
  const password = document.getElementById('login-password').value;
  const errEl = document.getElementById('login-error');
  errEl.classList.add('hidden');

  if (!email || !password) { showError('login-error', 'Completa todos los campos.'); return; }

  try {
    const data = await apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });

    token = data.token;
    currentUser = data;

    document.getElementById('user-name').textContent = currentUser.nombre || email.split('@')[0];
    document.getElementById('user-role').textContent = currentUser.rol || 'EMPLEADO';
    document.getElementById('user-avatar').textContent = (currentUser.nombre || email)[0].toUpperCase();
    document.getElementById('mobile-avatar').textContent = (currentUser.nombre || email)[0].toUpperCase();

    showPage('page-dashboard');
    loadDashboard();

    // Ocultar auditorias si es EMPLEADO
    const navAuditoria = document.getElementById('nav-auditorias');
    if (currentUser.rol === 'EMPLEADO') {
      navAuditoria.style.display = 'none';
    } else {
      navAuditoria.style.display = '';
    }

  } catch (e) {
    showError('login-error', e.message || 'Credenciales incorrectas.');
  }
}

async function doRegister() {
  const body = {
    nombre: document.getElementById('reg-nombre').value.trim(),
    apellido: document.getElementById('reg-apellido').value.trim(),
    documento: document.getElementById('reg-documento').value.trim(),
    email: document.getElementById('reg-email').value.trim(),
    password: document.getElementById('reg-password').value,
    rol: document.getElementById('reg-rol').value,
  };
  document.getElementById('reg-error').classList.add('hidden');
  document.getElementById('reg-success').classList.add('hidden');

  if (!body.nombre || !body.apellido || !body.email || !body.password || !body.documento) {
    showError('reg-error', 'Completa todos los campos obligatorios.'); return;
  }

  try {
    await apiFetch('/auth/registro', { method: 'POST', body: JSON.stringify(body) });
    const s = document.getElementById('reg-success');
    s.textContent = '¡Cuenta creada! Ahora puedes iniciar sesion.';
    s.classList.remove('hidden');
  } catch (e) {
    showError('reg-error', e.message);
  }
}

function doLogout() {
  token = null;
  currentUser = null;
  showPage('page-login');
}

/* ======================== DASHBOARD ======================== */

async function loadDashboard() {
  try {
    const [bodegasData, productosData, movimientosData, stockBajoData] = await Promise.all([
      apiFetch('/bodegas').catch(() => []),
      apiFetch('/productos').catch(() => []),
      apiFetch('/movimientos').catch(() => []),
      apiFetch('/productos/stock-bajo?limite=10').catch(() => []),
    ]);

    document.getElementById('stat-bodegas').textContent = Array.isArray(bodegasData) ? bodegasData.length : '?';
    document.getElementById('stat-productos').textContent = Array.isArray(productosData) ? productosData.length : '?';
    document.getElementById('stat-movimientos').textContent = Array.isArray(movimientosData) ? movimientosData.length : '?';
    document.getElementById('stat-stock-bajo').textContent = Array.isArray(stockBajoData) ? stockBajoData.length : '?';

    // Stock bajo tabla
    const sbEl = document.getElementById('stock-bajo-list');
    if (!Array.isArray(stockBajoData) || stockBajoData.length === 0) {
      sbEl.innerHTML = '<p class="loading-text">✓ Todos los productos tienen stock suficiente</p>';
    } else {
      sbEl.innerHTML = `
        <table class="data-table">
          <thead><tr><th>PRODUCTO</th><th>CATEGORIA</th><th>STOCK</th><th>BODEGA</th></tr></thead>
          <tbody>
            ${stockBajoData.map(p => `
              <tr>
                <td>${p.nombre}</td>
                <td>${p.categoria}</td>
                <td class="stock-low">${p.stock}</td>
                <td>${p.bodegaNombre || p.bodegaId || '—'}</td>
              </tr>`).join('')}
          </tbody>
        </table>`;
    }

    // Ultimos movimientos (ultimos 5)
    const ulEl = document.getElementById('ultimos-movimientos');
    const ultimos = Array.isArray(movimientosData) ? movimientosData.slice(-5).reverse() : [];
    if (ultimos.length === 0) {
      ulEl.innerHTML = '<p class="loading-text">Sin movimientos registrados</p>';
    } else {
      ulEl.innerHTML = ultimos.map(m => `
        <div class="mini-item">
          ${badgeHTML(m.tipoMovimiento)}
          <span class="mini-text">${formatDate(m.fecha)}</span>
        </div>`).join('');
    }

  } catch (e) {
    showToast('Error cargando el dashboard: ' + e.message, 'err');
  }
}

/* ======================== BODEGAS ======================== */

async function loadBodegas() {
  const tbody = document.getElementById('tbody-bodegas');
  try {
    bodegas = await apiFetch('/bodegas');
    const todosProductos = await apiFetch('/productos');

    if (!bodegas.length) {
      tbody.innerHTML = '<tr><td colspan="6" class="loading-text">No hay bodegas registradas</td></tr>';
      return;
    }

    tbody.innerHTML = bodegas.map(b => {
      const productosEnBodega = todosProductos.filter(p => p.bodegaId === b.id);
      const totalStock = productosEnBodega.reduce((sum, p) => sum + p.stock, 0);
      const capacidad = b.capacidad || 0;
      const porcentaje = capacidad > 0 ? Math.min((totalStock / capacidad) * 100, 100) : 0;
      const colorBarra = porcentaje >= 80 ? 'var(--red)' : porcentaje >= 50 ? 'var(--yellow)' : 'var(--green)';

      return `
        <tr>
          <td><span style="font-family:var(--font-mono);color:var(--text-muted)">#${b.id}</span></td>
          <td><strong>${b.nombre}</strong></td>
          <td>${b.ubicacion}</td>
          <td>
            <div style="display:flex;flex-direction:column;gap:4px">
              <span style="font-size:12px;font-weight:700">${totalStock} / ${capacidad}</span>
              <div style="background:var(--bg4);border-radius:99px;height:5px;width:100px">
                <div style="background:${colorBarra};width:${porcentaje}%;height:100%;border-radius:99px;transition:width 0.3s"></div>
              </div>
            </div>
          </td>
          <td>${b.encargadoNombre || '—'}</td>
          <td style="display:flex;gap:6px">
            <button class="btn-icon" style="color:var(--blue);border-color:rgba(59,130,246,0.3)" onclick="editBodega(${b.id})">✎</button>
            <button class="btn-icon" onclick="deleteBodega(${b.id})">✕</button>
          </td>
        </tr>`;
    }).join('');
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="6" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

async function loadPersonasSelect(selectedId = null) {
  try {
    const personas = await apiFetch('/personas');
    const sel = document.getElementById('bod-encargado');
    sel.innerHTML = personas.map(p =>
      `<option value="${p.id}" ${selectedId == p.id ? 'selected' : ''}>${p.nombre} ${p.apellido}</option>`
    ).join('');
  } catch { }
}

async function editBodega(id) {
  const bodega = bodegas.find(b => b.id === id);
  if (!bodega) return;
  editBodegaId = id;
  document.getElementById('modal-bodega-titulo').textContent = 'Editar Bodega';
  document.getElementById('bod-nombre').value = bodega.nombre;
  document.getElementById('bod-ubicacion').value = bodega.ubicacion;
  document.getElementById('bod-capacidad').value = bodega.capacidad;
  await loadPersonasSelect(bodega.encargadoId);
  document.getElementById('modal-bodega').classList.remove('hidden');
}

async function saveBodega() {
  const body = {
    nombre: document.getElementById('bod-nombre').value.trim(),
    ubicacion: document.getElementById('bod-ubicacion').value.trim(),
    capacidad: parseInt(document.getElementById('bod-capacidad').value),
    encargadoId: parseInt(document.getElementById('bod-encargado').value),
  };
  if (!body.nombre || !body.ubicacion || !body.capacidad || !body.encargadoId) {
    showError('bod-error', 'Completa todos los campos.'); return;
  }
  try {
    if (editBodegaId) {
      await apiFetch(`/bodegas/${editBodegaId}`, { method: 'PUT', body: JSON.stringify(body) });
      showToast('Bodega actualizada correctamente ✓');
    } else {
      await apiFetch('/bodegas', { method: 'POST', body: JSON.stringify(body) });
      showToast('Bodega registrada correctamente ✓');
    }
    editBodegaId = null;
    closeModal('modal-bodega');
    loadBodegas();
  } catch (e) {
    showError('bod-error', e.message);
  }
}

async function deleteBodega(id) {
  if (!confirm('¿Eliminar esta bodega?')) return;
  try {
    await apiFetch(`/bodegas/${id}`, { method: 'DELETE' });
    showToast('Bodega eliminada');
    loadBodegas();
  } catch (e) {
    showToast('Error: ' + e.message, 'err');
  }
}

async function loadBodegasSelect(selectId) {
  try {
    const data = bodegas.length ? bodegas : await apiFetch('/bodegas');
    const sel = document.getElementById(selectId);
    const hasNone = sel.querySelector('option[value=""]');
    sel.innerHTML = (hasNone ? '<option value="">-- Ninguna --</option>' : '') +
      data.map(b => `<option value="${b.id}">${b.nombre}</option>`).join('');
  } catch { }
}

/* ======================== PRODUCTOS ======================== */

async function loadProductos() {
  const tbody = document.getElementById('tbody-productos');
  try {
    productos = await apiFetch('/productos');
    if (!productos.length) {
      tbody.innerHTML = '<tr><td colspan="7" class="loading-text">No hay productos registrados</td></tr>';
      return;
    }
    tbody.innerHTML = productos.map(p => `
      <tr>
        <td><span style="font-family:var(--font-mono);color:var(--text-muted)">#${p.id}</span></td>
        <td><strong>${p.nombre}</strong></td>
        <td>${p.categoria}</td>
        <td class="${p.stock < 10 ? 'stock-low' : 'stock-ok'}">${p.stock}</td>
        <td style="font-family:var(--font-mono)">$${parseFloat(p.precio).toLocaleString('es-CO')}</td>
        <td>${p.bodegaNombre || p.bodegaId || '—'}</td>
        <td style="display:flex;gap:6px">
          <button class="btn-icon" style="color:var(--blue);border-color:rgba(59,130,246,0.3)" onclick="editProducto(${p.id})">✎</button>
          <button class="btn-icon" onclick="deleteProducto(${p.id})">✕</button>
        </td>
      </tr>`).join('');
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

async function editProducto(id) {
  const producto = productos.find(p => p.id === id);
  if (!producto) return;
  editProductoId = id;
  document.getElementById('modal-producto-titulo').textContent = 'Editar Producto';
  document.getElementById('prod-nombre').value = producto.nombre;
  document.getElementById('prod-categoria').value = producto.categoria;
  document.getElementById('prod-stock').value = producto.stock;
  document.getElementById('prod-precio').value = producto.precio;
  await loadBodegasSelect('prod-bodega');
  document.getElementById('prod-bodega').value = producto.bodegaId;
  document.getElementById('modal-producto').classList.remove('hidden');
}

async function loadProductosSelect() {
  try {
    const data = productos.length ? productos : await apiFetch('/productos');
    productos = data;
    document.querySelectorAll('.prod-select').forEach(sel => {
      sel.innerHTML = data.map(p => `<option value="${p.id}">${p.nombre} (stock: ${p.stock})</option>`).join('');
    });
  } catch { }
}

async function saveProducto() {
  const body = {
    nombre: document.getElementById('prod-nombre').value.trim(),
    categoria: document.getElementById('prod-categoria').value.trim(),
    stock: parseInt(document.getElementById('prod-stock').value) || 0,
    precio: parseFloat(document.getElementById('prod-precio').value),
    bodegaId: parseInt(document.getElementById('prod-bodega').value),
  };
  if (!body.nombre || !body.categoria || !body.precio || !body.bodegaId) {
    showError('prod-error', 'Completa todos los campos.'); return;
  }
  try {
    if (editProductoId) {
      await apiFetch(`/productos/${editProductoId}`, { method: 'PUT', body: JSON.stringify(body) });
      showToast('Producto actualizado correctamente ✓');
    } else {
      await apiFetch('/productos', { method: 'POST', body: JSON.stringify(body) });
      showToast('Producto registrado correctamente ✓');
    }
    editProductoId = null;
    closeModal('modal-producto');
    loadProductos();
  } catch (e) {
    showError('prod-error', e.message);
  }
}

async function deleteProducto(id) {
  if (!confirm('¿Eliminar este producto?')) return;
  try {
    await apiFetch(`/productos/${id}`, { method: 'DELETE' });
    showToast('Producto eliminado');
    loadProductos();
  } catch (e) {
    showToast('Error: ' + e.message, 'err');
  }
}

/* ======================== MOVIMIENTOS ======================== */

async function loadMovimientos() {
  const tbody = document.getElementById('tbody-movimientos');
  try {
    const data = await apiFetch('/movimientos');
    renderMovimientos(data, tbody);
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

function renderMovimientos(data, tbody) {
  if (!data || !data.length) {
    tbody.innerHTML = '<tr><td colspan="7" class="loading-text">No hay movimientos registrados</td></tr>';
    return;
  }
  tbody.innerHTML = data.map(m => `
    <tr>
      <td><span style="font-family:var(--font-mono);color:var(--text-muted)">#${m.id}</span></td>
      <td style="font-size:12px">${formatDate(m.fecha)}</td>
      <td>${badgeHTML(m.tipoMovimiento)}</td>
      <td>${m.bodegaOrigenNombre || '—'}</td>
      <td>${m.bodegaDestinoNombre || '—'}</td>
      <td>${m.usuarioNombre || '—'}</td>
      <td style="color:var(--text-muted);font-size:12px">${m.descripcion || '—'}</td>
    </tr>`).join('');
}

async function filtrarMovimientos() {
  const desde = document.getElementById('filtro-desde').value;
  const hasta = document.getElementById('filtro-hasta').value;
  const tbody = document.getElementById('tbody-movimientos');
  if (!desde || !hasta) { showToast('Selecciona rango de fechas', 'err'); return; }
  try {
    const data = await apiFetch(`/movimientos/por-fechas?inicio=${desde}T00:00:00&fin=${hasta}T23:59:59`);
    renderMovimientos(data, tbody);
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

function toggleBodegas() {
  const tipo = document.getElementById('mov-tipo').value;
  const origen = document.getElementById('wrap-origen');
  const destino = document.getElementById('wrap-destino');
  if (tipo === 'ENTRADA') {
    origen.style.opacity = '0.4'; origen.style.pointerEvents = 'none';
    destino.style.opacity = '1'; destino.style.pointerEvents = 'auto';
  } else if (tipo === 'SALIDA') {
    origen.style.opacity = '1'; origen.style.pointerEvents = 'auto';
    destino.style.opacity = '0.4'; destino.style.pointerEvents = 'none';
  } else {
    origen.style.opacity = '1'; origen.style.pointerEvents = 'auto';
    destino.style.opacity = '1'; destino.style.pointerEvents = 'auto';
  }
}

function addProductoRow() {
  const container = document.getElementById('productos-rows');
  const div = document.createElement('div');
  div.className = 'producto-row';
  div.innerHTML = `
    <select class="prod-select"></select>
    <input type="number" placeholder="Cant." min="1" class="prod-cantidad" />
    <button onclick="this.closest('.producto-row').remove()">✕</button>
  `;
  container.appendChild(div);
  filtrarProductosPorBodega(div.querySelector('.prod-select'));
}

function filtrarProductosPorBodega(sel) {
  const tipo = document.getElementById('mov-tipo').value;
  const origenId = parseInt(document.getElementById('mov-origen').value);

  let productosFiltrados = productos;

  if ((tipo === 'SALIDA' || tipo === 'TRANSFERENCIA') && origenId) {
    productosFiltrados = productos.filter(p => p.bodegaId === origenId);
  }

  sel.innerHTML = productosFiltrados.length
    ? productosFiltrados.map(p => `<option value="${p.id}">${p.nombre} (stock: ${p.stock})</option>`).join('')
    : '<option value="">— Sin productos en esta bodega —</option>';
}

function actualizarProductosMovimiento() {
  document.querySelectorAll('.prod-select').forEach(sel => {
    filtrarProductosPorBodega(sel);
  });
}

async function saveMovimiento() {
  const tipo = document.getElementById('mov-tipo').value;
  const origenId = document.getElementById('mov-origen').value || null;
  const destinoId = document.getElementById('mov-destino').value || null;
  const descripcion = document.getElementById('mov-descripcion').value.trim();

  const rows = document.querySelectorAll('.producto-row');
  const detalles = [];
  rows.forEach(row => {
    const pid = row.querySelector('.prod-select')?.value;
    const cant = row.querySelector('.prod-cantidad')?.value;
    if (pid && cant) detalles.push({ productoId: parseInt(pid), cantidad: parseInt(cant) });
  });

  if (detalles.length === 0) {
    showError('mov-error', 'Agrega al menos un producto.'); return;
  }

  const body = {
    tipoMovimiento: tipo,
    bodegaOrigenId: origenId ? parseInt(origenId) : null,
    bodegaDestinoId: destinoId ? parseInt(destinoId) : null,
    descripcion: descripcion || null,
    detalles,
  };

  try {
    await apiFetch('/movimientos', { method: 'POST', body: JSON.stringify(body) });
    showToast('Movimiento registrado correctamente ✓');
    closeModal('modal-movimiento');
    document.getElementById('productos-rows').innerHTML = '';
    loadMovimientos();
    loadProductos();
  } catch (e) {
    showError('mov-error', e.message);
  }
}

/* ======================== AUDITORIAS ======================== */

async function loadAuditorias() {
  const tbody = document.getElementById('tbody-auditorias');
  try {
    const data = await apiFetch('/auditoria');
    renderAuditorias(data, tbody);
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

function renderAuditorias(data, tbody) {
  if (!data || !data.length) {
    tbody.innerHTML = '<tr><td colspan="7" class="loading-text">No hay auditorias registradas</td></tr>';
    return;
  }
  const colors = { CREAR: 'badge-entrada', ACTUALIZAR: 'badge-transferencia', ELIMINAR: 'badge-salida' };
  tbody.innerHTML = data.map(a => `
    <tr>
      <td><span style="font-family:var(--font-mono);color:var(--text-muted)">#${a.id}</span></td>
      <td><strong>${a.entidad || '—'}</strong></td>
      <td><span class="mini-badge ${colors[a.operacion] || ''}">${a.operacion}</span></td>
      <td style="font-size:12px">${formatDate(a.fecha)}</td>
      <td>${a.usuarioNombre || '—'}</td>
      <td style="font-size:11px;color:var(--text-muted);max-width:140px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap" title="${a.valorAnterior || ''}">${a.valorAnterior || '—'}</td>
      <td style="font-size:11px;color:var(--text-muted);max-width:140px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap" title="${a.valorNuevo || ''}">${a.valorNuevo || '—'}</td>
    </tr>`).join('');
}

async function filtrarAuditorias() {
  const op = document.getElementById('filtro-operacion').value;
  const tbody = document.getElementById('tbody-auditorias');
  try {
    const url = op ? `/auditoria/por-operacion?operacion=${op}` : '/auditoria';
    const data = await apiFetch(url);
    renderAuditorias(data, tbody);
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" class="loading-text">Error: ${e.message}</td></tr>`;
  }
}

/* ======================== INIT ======================== */
document.getElementById('login-password').addEventListener('keydown', e => {
  if (e.key === 'Enter') doLogin();
});
document.getElementById('login-email').addEventListener('keydown', e => {
  if (e.key === 'Enter') doLogin();
});