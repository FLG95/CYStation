
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


function repairDeviceAjax(deviceId, typeId) {
    const modal = document.getElementById('gameModal');
    const iframe = document.getElementById('gameFrame');

    const games = {
        0: '/mission/game/co2',
        1: '/mission/game/reactor',
        2: '/mission/game/radar',
        3: '/mission/game/radio'
    };

    const gameUrl = games[typeId];

    if (gameUrl) {
        modal.style.display = 'flex';
        iframe.src = gameUrl + "?deviceId=" + deviceId;
        console.log("Lancement du jeu type " + typeId + " pour le device " + deviceId);
    } else {
        console.error("Aucun mini-jeu configuré pour le type ID : " + typeId);
    }
}

function confirmRepairOnServer(deviceId) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    console.log("Envoi de la confirmation de réparation pour l'ID :", deviceId);

    fetch('/mission/device/repair/' + deviceId, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => {
            if (!response.ok) throw new Error("Erreur serveur");
            return response.json();
        })
        .then(deviceData => {
            console.log("Données reçues du serveur :", deviceData);

            if (deviceData && deviceData.id) {
                updateDeviceUI(deviceData);
            } else {
                console.error("Le serveur a répondu, mais l'objet device est invalide :", deviceData);
            }
        })
        .catch(err => console.error("Erreur lors du fetch de réparation :", err));
}


function updateZoneAjax(zoneId, element, fieldName) {
    const newValue = element.innerText.trim();
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    const params = new URLSearchParams();
    params.append('id', zoneId);
    params.append(fieldName, newValue);

    fetch('/mission/zone/update', {
        method: 'POST',
        headers: {
            [header]: token,
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params
    })
        .then(response => {
            if (response.ok) {
                console.log("Mise à jour réussie");

                const mainCard = document.getElementById('zone-card-' + zoneId);
                if (mainCard) {
                    if (fieldName === 'name') {
                        mainCard.querySelector('h3').innerText = newValue;
                    }
                }
            } else {
                alert("Erreur lors de la sauvegarde");
            }
        })
        .catch(error => console.error('Erreur:', error));
}

document.addEventListener('keydown', function(e) {
    if (e.target.classList.contains('editable-zone-name') && e.key === 'Enter') {
        e.preventDefault();
        e.target.blur();
    }
});



window.addEventListener('message', function(event) {
    if (event.data.action === 'REPAIR_SUCCESS') {
        const deviceId = event.data.deviceId;

        closeModal();

        confirmRepairOnServer(deviceId);
    }
});

