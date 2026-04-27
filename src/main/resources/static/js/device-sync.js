let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/ws-station');
    stompClient = Stomp.over(socket);


    stompClient.connect({}, function (frame) {
        console.log('Connecté au WebSocket : ' + frame);

        stompClient.subscribe('/topic/device-status', function (response) {
            const device = JSON.parse(response.body);
            updateDeviceUI(device);
        });
        stompClient.subscribe('/topic/device-telemetry', function (response) {
            const device = JSON.parse(response.body);
            updateTelemetryUI(device);
        });
        stompClient.subscribe('/topic/energy-update', function (response) {
            const energyData = JSON.parse(response.body);
            updateEnergyUI(energyData);
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
    const telemetrySpan = document.getElementById('telemetry-text-' + device.id);
    const statusName = device.status.name || device.status;
    const displayLabel = device.status.displayValue || statusName;

    if (statusText) {
        statusText.textContent = 'Statut: ' + displayLabel;
    }

    if (telemetrySpan) {
        if (device.telemetryDisplay) {
            telemetrySpan.textContent = device.telemetryDisplay;
        } else {
            // Fallback si telemetryDisplay est vide : on affiche la conso ou prod
            const val = (device.deviceCategoryCode === 'GEN') ? device.production : device.consumption;
            telemetrySpan.textContent = (statusName === 'ONLINE' ? val : 0) + ' GW';
        }
    }

    if (led) {
        led.className = 'status-led ' + (
            statusName === 'ONLINE' ? 'led-on' :
                (statusName === 'MAINTENANCE' ? 'led-maintenance' : 'led-off')
        );
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

            const isToggleStyle = btn.classList.contains('btn-toggle');
            btn.textContent = (statusName === 'ONLINE') ? 'Éteindre' : 'Allumer';
            btn.className = isToggleStyle ?
                (statusName === 'ONLINE' ? 'btn-toggle off' : 'btn-toggle on') :
                btn.className;
        }
        if (repairBtn) repairBtn.style.display = 'none';
    }
}

function updateEnergyUI(data) {
    const prodElem = document.getElementById('prod-' + data.zoneId);
    const consElem = document.getElementById('cons-' + data.zoneId);
    const barElem = document.getElementById('bar-' + data.zoneId);

    if (prodElem) prodElem.textContent = data.production;
    if (consElem) consElem.textContent = data.consumption;

    if (barElem) {
        const percentage = data.production > 0 ? (data.consumption / data.production) * 100 : 0;
        barElem.style.width = Math.min(percentage, 100) + '%';
        barElem.style.backgroundColor = percentage > 100 ? '#ff1744' : (percentage > 85 ? '#ff9100' : '#00e5ff');
    }
}

function updateTelemetryUI(device) {
    const telemetrySpan = document.getElementById('telemetry-text-' + device.id);
    if (telemetrySpan && device.telemetryDisplay) {
        telemetrySpan.textContent = device.telemetryDisplay;
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