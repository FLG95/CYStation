const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

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
            if (newExp >= 50) {
                newRole = "ROLE_SCIENTIST";
            } else if (newExp >= 100) {
                newRole = "ROLE_ADMIN";
            } else if (newExp <= 0){
                newRole = "ROLE_PASSENGER";

            }

            const roleBadge = document.getElementById('role-' + userId);
            if (roleBadge) {
                roleBadge.innerText = newRole;

            }
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