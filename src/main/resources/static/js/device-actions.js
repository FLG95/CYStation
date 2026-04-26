

function toggleDeviceAjax(deviceId) {
    const btn = document.getElementById('btn-' + deviceId);
    const statusText = document.getElementById('status-text-' + deviceId);
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/toggle/' + deviceId, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => response.json())
        .then(status => {
            statusText.textContent = status.displayValue;

            if (status.name === 'ONLINE') {
                btn.textContent = (btn.classList.contains('btn-action')) ? 'OFF' : 'Éteindre';
                btn.className = btn.classList.contains('btn-action') ? 'btn-action btn-off' : 'btn-toggle off';
            } else {
                btn.textContent = (btn.classList.contains('btn-action')) ? 'ON' : 'Allumer';
                btn.className = btn.classList.contains('btn-action') ? 'btn-action btn-on' : 'btn-toggle on';
            }
            btn.disabled = (status.name === 'MAINTENANCE');
        });
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
                // TENTATIVE 1 : On cherche la carte (Vue Index)
                const card = document.getElementById('device-card-' + deviceId);
                // TENTATIVE 2 : On cherche la ligne (Vue Admin)
                const row = document.getElementById('device-row-' + deviceId);

                const elementToDelete = card || row;

                if (elementToDelete) {
                    elementToDelete.style.transition = "all 0.3s ease";
                    elementToDelete.style.opacity = '0';
                    elementToDelete.style.transform = "translateX(20px)";
                    setTimeout(() => elementToDelete.remove(), 300);
                }
            }
        });
}

function deleteZoneAjax(zoneId) {
    if (!confirm("Voulez-vous vraiment supprimer cette zone ?")) return;

    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch(`/mission/zone/delete/${zoneId}`, {
        method: 'POST',
        headers: {
            [header]: token
        }
    })
        .then(response => {
            if (response.ok) {

                const zoneRow = document.getElementById('zone-row-' + zoneId);
                if (zoneRow) {

                    let next = zoneRow.nextElementSibling;
                    while (next && next.classList.contains('device-row')) {
                        let toRemove = next;
                        next = next.nextElementSibling;
                        toRemove.remove();
                    }
                    zoneRow.remove();
                }
            } else {
                alert("Erreur lors de la suppression (Code: " + response.status + ")");
            }
        })
        .catch(error => console.error('Erreur:', error));
}


function repairDeviceAjax(deviceId, deviceTypeCode) {

    const modal = document.getElementById('gameModal');
    const iframe = document.getElementById('gameFrame');

    if (!modal || !iframe) {
        console.error("Éléments de jeu introuvables dans le DOM");
        return;
    }

    const games = {
        'CO2_SENSOR' : '/mission/game/co2',
        'GENERATOR': '/mission/game/reactor',
        'RADAR': '/mission/game/radar',
        'RADIO': '/mission/game/radio',
        'OXYGEN_SENSOR': '/mission/game/o2',
        'RADIATION_SENSOR': '/mission/game/radiation',
        'WATER_RECLAIMER': '/mission/game/water'
    };

    const gameUrl = games[deviceTypeCode] || '/mission/game/radio';

    modal.style.display = 'flex';
    iframe.src = gameUrl + "?deviceId=" + deviceId;
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

