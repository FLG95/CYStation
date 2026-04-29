const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

// ─── POPUP ───────────────────────────────────────────────
function showPopup(message, type = 'loading') {
    let popup = document.getElementById('admin-popup');
    if (!popup) {
        popup = document.createElement('div');
        popup.id = 'admin-popup';
        popup.style.cssText = `
            position: fixed;
            top: 30px;
            right: 30px;
            background: #111827;
            border: 1px solid #1e3a5f;
            border-radius: 10px;
            padding: 18px 28px;
            color: #e0e0e0;
            font-family: 'Segoe UI', sans-serif;
            font-size: 0.95rem;
            box-shadow: 0 0 30px rgba(0,150,255,0.15);
            z-index: 9999;
            display: flex;
            align-items: center;
            gap: 12px;
            transition: opacity 0.4s;
        `;
        document.body.appendChild(popup);
    }

    const icons = {
        loading: `<span style="display:inline-block;width:18px;height:18px;border:3px solid #00bfff;border-top-color:transparent;border-radius:50%;animation:spin 0.8s linear infinite"></span>`,
        success: `<span style="color:#00bfff;font-size:1.2rem">✅</span>`,
        error:   `<span style="color:#ff4444;font-size:1.2rem">❌</span>`
    };

    // Ajoute l'animation spin si pas déjà présente
    if (!document.getElementById('popup-style')) {
        const style = document.createElement('style');
        style.id = 'popup-style';
        style.textContent = `@keyframes spin { to { transform: rotate(360deg); } }`;
        document.head.appendChild(style);
    }

    popup.style.opacity = '1';
    popup.style.borderColor = type === 'error' ? '#3f1e1e' : '#1e3a5f';
    popup.innerHTML = `${icons[type]} <span>${message}</span>`;
}

function hidePopup(delay = 2500) {
    setTimeout(() => {
        const popup = document.getElementById('admin-popup');
        if (popup) {
            popup.style.opacity = '0';
            setTimeout(() => popup.remove(), 400);
        }
    }, delay);
}

// ─── FONCTIONS ADMIN ─────────────────────────────────────
function modifyExp(userId, amount) {
    const formData = new FormData();
    formData.append('id', userId);
    formData.append('amount', amount);

    fetch('/admin/users/add-exp', {
        method: 'POST',
        headers: {[header]: token},
        body: formData
    })
        .then(res => res.json())
        .then(newExp => {
            document.getElementById('exp-' + userId).innerText = newExp;

            let newRole = "ROLE_PASSENGER";
            if (newExp >= 100) {
                newRole = "ROLE_ADMIN";
            } else if (newExp >= 50) {
                newRole = "ROLE_SCIENTIST";
            }

            const roleBadge = document.getElementById('role-' + userId);
            if (roleBadge) roleBadge.innerText = newRole;
        });
}

function deleteUser(userId) {
    if (!confirm("Révoquer l'accès de cet utilisateur ?")) return;

    fetch('/admin/users/delete/' + userId, {
        method: 'POST',
        headers: {[header]: token}
    })
        .then(res => {
            if (res.ok) document.getElementById('user-row-' + userId).remove();
        });
}

function toggleUserStatus(userId) {
    showPopup("Envoi du mail d'activation en cours...", 'loading');

    fetch(`/admin/user/${userId}/toggle-status`, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => response.json())
        .then(data => {
            if (data.action === 'MAIL_SENT') {
                showPopup("Mail d'activation envoyé ✓", 'success');
            } else {
                showPopup("Compte désactivé", 'success');
            }
            hidePopup(2000);
            setTimeout(() => location.reload(), 2400);
        })
        .catch(error => {
            console.error('Erreur:', error);
            showPopup("Une erreur est survenue", 'error');
            hidePopup(3000);
        });
}
