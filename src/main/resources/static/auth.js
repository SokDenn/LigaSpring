        async function login(event) {
            event.preventDefault(); // Предотвращаем отправку формы

            const username = document.querySelector('input[name="username"]').value;
            const password = document.querySelector('input[name="password"]').value;

            const response = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                // Сохраняем refresh token в локальном хранилище
                localStorage.setItem('refreshToken', data.refreshToken);
                // Перенаправляем пользователя на защищенную страницу
                window.location.href = '/tasks';
            } else {
                alert('Неверный логин или пароль');
            }
        }

async function fetchWithAuth(url, options = {}) {
    let response = await fetch(url, {
        ...options,
        credentials: 'include' // Обеспечивает отправку куки с запросом
    });

    if (response.status === 401) {
        // Попытка обновления access token
        const refreshSuccess = await refreshAccessToken();
        if (refreshSuccess) {
            // Повторяем оригинальный запрос после обновления токена
            response = await fetch(url, {
                ...options,
                credentials: 'include'
            });
        } else {
            alert('Не удалось обновить токен, пожалуйста, войдите снова.');
            // Перенаправляем пользователя на страницу входа
            window.location.href = '/login';
        }
    }

    return response;
}

async function refreshAccessToken() {
    const refreshToken = localStorage.getItem('refreshToken');

    const response = await fetch('/api/v1/auth/refresh', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token: refreshToken })
    });

    if (response.ok) {
        const data = await response.json();
        // Обновляем access token в куках
        document.cookie = `accessToken=${data.accessToken}; path=/; secure; HttpOnly`;
        return true;
    } else {
        alert('Не удалось обновить access token');
        // Перенаправляем пользователя на страницу входа
        window.location.href = '/login';
        return false;
    }
}