document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signupForm');
    const messageElement = document.getElementById('message');
    const API_BASE_URL = 'http://localhost:8080';

    signupForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        messageElement.textContent = '';
        messageElement.className = 'error'; // Reset class

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch(`${API_BASE_URL}/auth/signup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, password }),
            });

            const responseText = await response.text();

            if (!response.ok) {
                // If response is not ok, throw error with message from backend
                throw new Error(responseText);
            }

            // Success
            messageElement.className = 'success'; // You might need to add a .success class in your CSS
            messageElement.textContent = 'Registration successful! Redirecting to login...';

            // Redirect to login page after a short delay
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 2000);

        } catch (error) {
            console.error('Signup Error:', error);
            messageElement.textContent = error.message || 'An error occurred. Please try again.';
        }
    });
});