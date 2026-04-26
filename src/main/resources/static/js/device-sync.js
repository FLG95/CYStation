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
    const led = document.getElementById('led-' + device.id);
    const btn = document.getElementById('btn-' + device.id);
    const repairBtn = document.getElementById('btn-repair-' + device.id);

    const statusName = device.status.name || device.status;

    if (statusText) {
        statusText.textContent = device.status.displayValue || statusName;
        statusText.className = 'status-indicator ' + statusName;
    }

    if (led) {
        led.className = 'status-led ' + (statusName === 'ONLINE' ? 'led-on' : (statusName === 'MAINTENANCE' ? 'led-maintenance' : 'led-off'));
    }

    if (statusName === 'MAINTENANCE') {
        if (btn) btn.disabled = true;
        if (repairBtn) {
            repairBtn.style.display = 'inline-block';
            repairBtn.setAttribute('onclick', `repairDeviceAjax(${device.id}, '${device.deviceCategoryCode}')`);
        }
    } else {
        if (btn) {
            btn.disabled = false;
            const isAdmin = btn.classList.contains('btn-action');
            btn.textContent = (statusName === 'ONLINE') ? (isAdmin ? 'OFF' : 'Éteindre') : (isAdmin ? 'ON' : 'Allumer');
            btn.className = isAdmin ?
                (statusName === 'ONLINE' ? 'btn-action btn-off' : 'btn-action btn-on') :
                (statusName === 'ONLINE' ? 'btn-toggle off' : 'btn-toggle on');
        }
        if (repairBtn) repairBtn.style.display = 'none';
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