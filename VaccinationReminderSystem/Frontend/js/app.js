document.addEventListener('DOMContentLoaded', () => {
    const API_BASE_URL = 'http://localhost:8080';

    // Views
    const loginView = document.getElementById('loginView');
    const dashboardView = document.getElementById('dashboardView');
    
    // Login Form Elements
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const errorMessage = document.getElementById('errorMessage');
    
    // Dashboard Elements
    const logoutButton = document.getElementById('logoutButton');
    const dashboardContent = document.getElementById('dashboardContent');

    // =================== ADDED SECTION START ===================
    // Add Child Form Elements
    const addChildForm = document.getElementById('addChildForm');
    const childNameInput = document.getElementById('childName');
    const childDobInput = document.getElementById('childDob');
    const addChildError = document.getElementById('addChildError');
    // =================== ADDED SECTION END ===================

    const showLoginView = () => {
        dashboardView.style.display = 'none';
        loginView.style.display = 'block';
    };

    const showDashboardView = () => {
        loginView.style.display = 'none';
        dashboardView.style.display = 'block';
        fetchDashboardData();
    };

    const fetchDashboardData = async () => {
        dashboardContent.innerHTML = '<p>Loading...</p>';
        const token = localStorage.getItem('token');
        if (!token) return showLoginView();

        try {
            const response = await fetch(`${API_BASE_URL}/child`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (response.status === 403) { // Handle expired tokens
                localStorage.removeItem('token');
                return showLoginView();
            }
            if (!response.ok) throw new Error(`Status: ${response.status}`);

            const children = await response.json();
            dashboardContent.innerHTML = '';

            if (children.length === 0) {
                dashboardContent.innerHTML = '<p>No children found. Add one using the form above.</p>';
                return;
            }

            children.forEach(child => renderChildCard(child));

        } catch (err) {
            console.error(err);
            dashboardContent.innerHTML = `<p class="error">${err.message}</p>`;
        }
    };

    const renderChildCard = (child) => {
        const card = document.createElement('div');
        card.className = 'child-card';

        let scheduleHtml = '<ul>';
        (child.schedules || []).forEach(s => {
            const statusClass = s.completed ? 'status-completed' : 'status-pending';
            const statusText = s.completed ? 'Completed' : 'Pending';

            scheduleHtml += `
                <li>
                    <strong>${s.vaccineName || 'Unknown Vaccine'}</strong> - 
                    Due: ${s.scheduledDate} - 
                    Status: <span class="${statusClass}">${statusText}</span>
                    ${!s.completed ? `<button class="mark-completed" data-id="${s.id}">Mark Completed</button>` : ''}
                </li>
            `;
        });
        scheduleHtml += '</ul>';

        const formHtml = `
            <form class="schedule-form" data-child-id="${child.id}">
                <label>Vaccine ID</label>
                <input type="number" name="vaccineId" required>
                <label>Scheduled Date</label>
                <input type="date" name="scheduledDate" required>
                <button type="submit">Add Schedule</button>
            </form>
        `;

        card.innerHTML = `
            <h3>${child.name}</h3>
            <p>DOB: ${child.dob}</p>
            <h4>Schedules:</h4>
            ${scheduleHtml}
            <h4>Add Schedule:</h4>
            ${formHtml}
        `;

        dashboardContent.appendChild(card);

        // Handle Add Schedule
        card.querySelector('.schedule-form')?.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const vaccineId = formData.get('vaccineId');
            const scheduledDate = formData.get('scheduledDate');

            try {
                const token = localStorage.getItem('token');
                const response = await fetch(`${API_BASE_URL}/schedule`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        child: { id: child.id },
                        vaccine: { id: parseInt(vaccineId) },
                        scheduledDate,
                        completed: false
                    })
                });
                if (!response.ok) throw new Error(await response.text());
                fetchDashboardData();
            } catch (err) {
                console.error(err);
                alert(`Error adding schedule: ${err.message}`);
            }
        });

        // Handle Mark Completed
        card.querySelectorAll('.mark-completed').forEach(btn => {
            btn.addEventListener('click', async () => {
                const scheduleId = btn.getAttribute('data-id');
                const token = localStorage.getItem('token');

                try {
                    const response = await fetch(`${API_BASE_URL}/schedule/${scheduleId}`, {
                        method: 'PUT',
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                    if (!response.ok) throw new Error(await response.text());
                    fetchDashboardData();
                } catch (err) {
                    console.error(err);
                    alert(`Error updating schedule: ${err.message}`);
                }
            });
        });
    };

    // Login
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorMessage.textContent = '';
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: emailInput.value, password: passwordInput.value })
            });
            if (!response.ok) throw new Error(await response.text() || 'Invalid credentials.');
            const data = await response.json();
            if (data.token) {
                localStorage.setItem('token', data.token);
                showDashboardView();
            } else throw new Error('No token received');
        } catch (err) {
            console.error(err);
            errorMessage.textContent = 'Invalid credentials.';
        }
    });

    // Logout
    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('token');
        showLoginView();
    });

    // =================== ADDED SECTION START ===================
    // Handle Add Child Form Submission
    addChildForm.addEventListener('submit', async (e) => {
        e.preventDefault(); // Prevents the page from reloading
        addChildError.textContent = '';
        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`${API_BASE_URL}/child`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    name: childNameInput.value,
                    dob: childDobInput.value
                })
            });
            if (!response.ok) throw new Error(await response.text());
            
            // If successful, clear the form and refresh the dashboard
            childNameInput.value = '';
            childDobInput.value = '';
            fetchDashboardData(); 
        } catch (err) {
            console.error('Error adding child:', err);
            addChildError.textContent = `Error: ${err.message}`;
        }
    });
    // =================== ADDED SECTION END ===================

    // Initial check on page load (No change here)
    const isTokenExpired = (token) => {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return Date.now() >= payload.exp * 1000;
        } catch { return true; }
    };

    const initializeWithTokenValidation = () => {
        const token = localStorage.getItem('token');
        if (token && !isTokenExpired(token)) showDashboardView();
        else showLoginView();
    };

    initializeWithTokenValidation();
});