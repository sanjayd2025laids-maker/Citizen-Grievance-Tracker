const api = {
  async get(url) {
    const response = await fetch(url);
    if (!response.ok) {
      const body = await response.json().catch(() => null);
      throw new Error(body?.message || `GET ${url} failed: ${response.status}`);
    }
    return response.json();
  },

  async post(url, payload) {
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    const text = await response.text();
    if (!response.ok) {
      const body = text ? JSON.parse(text) : null;
      throw new Error(body?.message || text || `POST ${url} failed: ${response.status}`);
    }
    return text ? JSON.parse(text) : null;
  },

  async put(url, payload) {
    const response = await fetch(url, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    const text = await response.text();
    if (!response.ok) {
      const body = text ? JSON.parse(text) : null;
      throw new Error(body?.message || text || `PUT ${url} failed: ${response.status}`);
    }
    return text ? JSON.parse(text) : null;
  }
};

const setOutput = (id, value) => {
  document.getElementById(id).textContent = JSON.stringify(value, null, 2);
};

const setStat = (id, value) => {
  const el = document.getElementById(id);
  if (el) el.textContent = value;
};

const updateSummaryCounts = (type, data) => {
  const value = Array.isArray(data) ? data.length : 0;
  if (type === 'departments') setStat('deptStat', value);
  if (type === 'categories') setStat('categoryStat', value);
  if (type === 'grievances') setStat('grievanceStat', value);
  if (type === 'escalations') setStat('escalationStat', value);
};

const showError = (message) => {
  alert(message);
};

document.getElementById('departmentForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const name = document.getElementById('departmentName').value.trim();
  try {
    const created = await api.post('/api/departments', { name });
    setOutput('departmentsOutput', created);
    e.target.reset();
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('categoryForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const payload = {
    name: document.getElementById('categoryName').value.trim(),
    slaDays: Number(document.getElementById('slaDays').value),
    departmentId: Number(document.getElementById('departmentId').value),
  };

  try {
    const created = await api.post('/api/categories', payload);
    setOutput('categoriesOutput', created);
    e.target.reset();
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('grievanceForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const payload = {
    categoryId: Number(document.getElementById('grievanceCategoryId').value),
    description: document.getElementById('grievanceDescription').value.trim(),
    location: document.getElementById('grievanceLocation').value.trim(),
  };

  try {
    const created = await api.post('/api/grievances', payload);
    setOutput('grievanceOutput', created);
    e.target.reset();
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('statusForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const id = document.getElementById('statusId').value;
  const status = document.getElementById('statusValue').value;

  try {
    const updated = await api.put(`/api/grievances/${id}/status`, { status });
    setOutput('grievanceOutput', updated);
    e.target.reset();
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('ratingForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const id = document.getElementById('ratingId').value;
  const payload = {
    rating: Number(document.getElementById('ratingValue').value),
    ratingComment: document.getElementById('ratingComment').value.trim(),
  };

  try {
    const updated = await api.put(`/api/grievances/${id}/rating`, payload);
    setOutput('grievanceOutput', updated);
    e.target.reset();
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('loadDepartments').addEventListener('click', async () => {
  try {
    const data = await api.get('/api/departments');
    setOutput('departmentsOutput', data);
    updateSummaryCounts('departments', data);
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('loadCategories').addEventListener('click', async () => {
  try {
    const data = await api.get('/api/categories');
    setOutput('categoriesOutput', data);
    updateSummaryCounts('categories', data);
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('loadGrievances').addEventListener('click', async () => {
  try {
    const data = await api.get('/api/grievances');
    setOutput('grievanceOutput', data);
    updateSummaryCounts('grievances', data);
  } catch (error) {
    showError(error.message);
  }
});

document.getElementById('loadEscalations').addEventListener('click', async () => {
  try {
    const data = await api.get('/api/escalations');
    setOutput('grievanceOutput', data);
    updateSummaryCounts('escalations', data);
  } catch (error) {
    showError(error.message);
  }
});

window.addEventListener('DOMContentLoaded', () => {
  setOutput('departmentsOutput', 'Click “Load Departments” to view records.');
  setOutput('categoriesOutput', 'Click “Load Categories” to view records.');
  setOutput('grievanceOutput', 'Create or load grievances to view results.');
  setStat('deptStat', '0');
  setStat('categoryStat', '0');
  setStat('grievanceStat', '0');
  setStat('escalationStat', '0');
});
