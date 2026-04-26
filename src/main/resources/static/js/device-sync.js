let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/ws-station');
    stompClient = Stomp.over(socket);



    stompClient.connect({}, function (frame) {
        console.log('Connecté au WebSocket : ' + frame);

        stompClient.subscribe('/topic/device-status', function (response) {
            const device = JSON.parse(response.body);
            console.log("Update reçu :", device);
            updateDeviceUI(device);
        });
        stompClient.subscribe('/topic/device-telemetry', function (response) {
            const device = JSON.parse(response.body);
            updateTelemetryUI(device);
        });
    }, function(error) {
        console.log("Erreur WebSocket, tentative de reconnexion dans 5s...");
        setTimeout(connectWebSocket, 5000);
    });
}

function updateDeviceUI(device) {
    const statusText = document.getElementById('status-text-' + device.id);
    const btn = document.getElementById('btn-' + device.id); // Bouton Allumer/Éteindre
    const repairBtn = document.getElementById('btn-repair-' + device.id);

    if (statusText) {

        statusText.textContent = device.status.displayValue || device.status;

        const statusName = device.status.name || device.status;

        if (statusName === 'MAINTENANCE') {

            if (btn) btn.disabled = true;
            if (repairBtn) {
                repairBtn.style.display = 'block';
                repairBtn.setAttribute('onclick', `repairDeviceAjax(${device.id}, ${device.deviceTypeId})`);
            }
        }
        else if (statusName === 'ONLINE') {
            if (btn) {
                btn.disabled = false;
                btn.textContent = 'Éteindre';
                btn.className = 'btn-toggle off';
            }
            if (repairBtn) repairBtn.style.display = 'none';
        }
        else if (statusName === 'OFFLINE') {
            if (btn) {
                btn.disabled = false;
                btn.textContent = 'Allumer';
                btn.className = 'btn-toggle on';
            }
            if (repairBtn) repairBtn.style.display = 'none';
        }
    }
}

function updateTelemetryUI(device) {
    const telemetrySpan = document.getElementById('telemetry-text-' + device.id);
    if (telemetrySpan && device.telemetryDisplay) {
        telemetrySpan.textContent = device.telemetryDisplay;
    }

    const deviceCard = document.getElementById('device-card-' + device.id);
    if (deviceCard && device.deviceImage) {
        const img = deviceCard.querySelector('img');
        if (img && img.src !== device.deviceImage) {
            img.src = device.deviceImage;
        }
    }
}

connectWebSocket();


function closeModal() {
    document.getElementById('gameModal').style.display = 'none';
    document.getElementById('gameFrame').src = '';
}


function confirmRepairOnServer(deviceId) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/mission/device/repair/' + deviceId, {
        method: 'POST',
        headers: { [header]: token }
    })
        .then(response => {
            if (!response.ok) throw new Error("Erreur serveur lors de la réparation");
            return response.json();
        })
        .then(device => {
            console.log("Réparation confirmée pour :", device.name);

            updateDeviceUI(device);
        })
        .catch(err => console.error("Erreur finale :", err));
}



window.addEventListener('message', function(event) {
    if (event.data && event.data.type === 'MINIGAME_WIN') {
        const deviceId = event.data.deviceId;

        closeModal();

        confirmRepairOnServer(deviceId);
    }
});