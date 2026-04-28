

function toggleDeviceAjax(deviceId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    fetch(`/mission/device/toggle/${deviceId}`, {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(async response => {
            if (!response.ok) {

                const errorText = await response.text();
                throw new Error(errorText);
            }
            return response.text();
        })
        .then(status => {
            console.log("Changement réussi : " + status);
        })
        .catch(error => {
            alert(error.message);
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

                const card = document.getElementById('device-card-' + deviceId);
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
        'WATER_RECLAIMER': '/mission/game/water',
        'COFFEE': '/mission/game/coffee'

    };

    const gameUrl = games[deviceTypeCode] || '/mission/game/radio';

    modal.style.display = 'flex';
    iframe.src = gameUrl + "?deviceId=" + deviceId;
}

function closeModal() {
    const modal = document.getElementById('gameModal');
    const iframe = document.getElementById('gameFrame');
    if (modal) {
        modal.style.display = 'none';
    }
    if (iframe) iframe.src = '';
    document.body.style.overflow = '';
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


function maintenanceDeviceAjax(deviceId) {
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;

    fetch(`/admin/device/maintenance/${deviceId}`, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => {
            if (response.ok) {
                const btnMaint = document.getElementById('btn-maintenance-' + deviceId);
                const spanMaint = document.getElementById('text-maintenance-' + deviceId);

                if (btnMaint) {
                    btnMaint.disabled = true;
                    btnMaint.style.opacity = '0.5';
                    btnMaint.style.cursor = 'not-allowed';
                    if (spanMaint) spanMaint.textContent = 'Déjà en maintenance';
                }
                location.reload();

            } else {
                alert("Erreur lors de la mise en maintenance");
            }
        });
}

document.addEventListener('keydown', function(e) {
    if (e.target.classList.contains('editable-zone-name') && e.key === 'Enter') {
        e.preventDefault();
        e.target.blur();
    }
});

