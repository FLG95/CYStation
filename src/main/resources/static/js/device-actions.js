function toggleDeviceAjax(deviceId) {
    const btn = document.getElementById('btn-' + deviceId);
    const statusText = document.getElementById('status-text-' + deviceId); // On cible le <p>

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/toggle/' + deviceId, {
        method: 'POST',
        headers: {[header]: token}
    })
        .then(response => {
            if (response.ok) return response.text();
            throw new Error();
        })
        .then(newStatus => {

            if (newStatus === 'ONLINE') {
                statusText.textContent = 'ONLINE';
                btn.textContent = 'Éteindre';
                btn.className = 'btn-toggle off';
            } else {
                statusText.textContent = 'OFFLINE';
                btn.textContent = 'Allumer';
                btn.className = 'btn-toggle on';
            }
        })
        .catch(error => console.error('Erreur:', error));
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

