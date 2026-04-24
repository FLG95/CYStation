
function toggleDeviceAjax(deviceId) {
    const btn = document.getElementById('btn-' + deviceId);
    const statusText = document.getElementById('status-text-' + deviceId);
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/toggle/' + deviceId, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => {
            if (response.ok) return response.json();
            throw new Error("Erreur serveur");
        })
        .then(status => {
            console.log("Statut reçu :", status);

            statusText.textContent = status.displayValue;

            if (status.name === 'ONLINE') {
                btn.textContent = 'Éteindre';
                btn.className = 'btn-toggle off';
            } else {
                btn.textContent = 'Allumer';
                btn.className = 'btn-toggle on';
            }

            btn.disabled = (status.name === 'MAINTENANCE');
        })
}

function repairDeviceAjax(deviceId) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/repair/' + deviceId, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => {
            if (response.ok) return response.json();
            throw new Error("Erreur lors de la réparation");
        })
        .then(status => {
            const statusText = document.getElementById('status-text-' + deviceId);
            const btn = document.getElementById('btn-' + deviceId);
            const repairBtn = document.getElementById('btn-repair-' + deviceId);

            statusText.textContent = status.displayValue;

            btn.disabled = false;
            btn.textContent = 'Allumer';
            btn.className = 'btn-toggle on';

            repairBtn.style.display = 'none';
        })
        .catch(err => console.error(err));
}

function deleteDeviceAjax(deviceId) {
    if (!confirm("Confirmer la désintégration du module ?")) return;

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/delete/' + deviceId, {
        method: 'POST',
        headers: {[header]: token}
    })
        .then(response => {
            if (response.ok) {
                const card = document.getElementById('device-card-' + deviceId);
                card.style.opacity = '0';
                setTimeout(() => card.remove(), 300);
            }
        });
}

function deleteZoneAjax(zoneId) {
    console.log("Appel de suppression pour la zone ID :", zoneId);
    if (!confirm("Attention : Supprimer cette zone détruira tous les appareils associés. Confirmer ?")) return;

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/zone/delete/' + zoneId, {
        method: 'POST',
        headers: {[header]: token}
    })
        .then(response => {
            if (response.ok) {
                window.location.hash = '#!';

                const zoneCard = document.getElementById('zone-card-' + zoneId);
                if (zoneCard) {
                    zoneCard.style.transition = "all 0.5s ease";
                    zoneCard.style.opacity = "0";
                    zoneCard.style.transform = "scale(0.5)";
                    setTimeout(() => zoneCard.remove(), 500);
                }

                const zoneModal = document.getElementById('zoneDetailsModal-' + zoneId);
                if (zoneModal) zoneModal.remove();

            } else {
                alert("Erreur lors de la suppression de la zone.");
            }
        })
        .catch(error => console.error('Erreur:', error));
}

