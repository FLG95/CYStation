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
        stompClient.subscribe('/topic/zone-energy', function (message) {
            const energyData = JSON.parse(message.body);
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
    const maintBtn = document.getElementById('btn-maintenance-' + device.id);
    const maintSpan = document.getElementById('text-maintenance-' + device.id);
    const statusName = device.status.name || device.status;
    const displayLabel = device.status.displayValue || statusName;


    if (statusText) {
        statusText.textContent = displayLabel;
        statusText.className = 'status-indicator ' + statusName;
    }

    if (telemetrySpan) {
        telemetrySpan.textContent = device.telemetryDisplay || '0 GW';
    }


    if (led) {
        led.className = 'status-led ' + (
            statusName === 'ONLINE' ? 'led-on' :
                (statusName === 'MAINTENANCE' ? 'led-maintenance' : 'led-off')
        );
    }

    if (statusName === 'MAINTENANCE') {

        if (btn) {
            btn.disabled = true;
            btn.textContent = 'Allumer';
            if (btn.classList.contains('btn-toggle')) {
                btn.className = 'btn-toggle on';
            } else {
                btn.className = 'btn-action';
            }
        }

        if (maintBtn) {
            maintBtn.disabled = true;
            maintBtn.style.opacity = '0.5';
            maintBtn.style.cursor = 'not-allowed';
            if (maintSpan) maintSpan.textContent = 'Déjà en maintenance';
        }

        if (repairBtn) {
            repairBtn.style.display = 'inline-block';
            repairBtn.setAttribute('onclick', `repairDeviceAjax(${device.id}, '${device.deviceCategoryCode}')`);
        }
    } else {
        if (btn) {
            btn.disabled = false;
            const isToggle = btn.classList.contains('btn-toggle');
            if (statusName === 'ONLINE') {
                btn.textContent = 'Éteindre';
                btn.className = isToggle ? 'btn-toggle off' : 'btn-action btn-off';
            } else {
                btn.textContent = 'Allumer';
                btn.className = isToggle ? 'btn-toggle on' : 'btn-action btn-on';
            }
        }

        if (maintBtn) {
            maintBtn.disabled = false;
            maintBtn.style.opacity = '1';
            maintBtn.style.cursor = 'pointer';
            if (maintSpan) maintSpan.textContent = 'Mettre en maintenance';
        }
        if (repairBtn) repairBtn.style.display = 'none';
    }



}

function updateEnergyUI(data) {

    const prodElem = document.getElementById('prod-' + data.zoneId);
    const consElem = document.getElementById('cons-' + data.zoneId);
    const barElem = document.getElementById('bar-' + data.zoneId);

    /*
    if (prodElem) prodElem.textContent = data.production;
    if (consElem) consElem.textContent = data.consumption;
    if (barElem) {
        const percentage = data.production > 0 ? (data.consumption / data.production) * 100 : 0;
        barElem.style.width = Math.min(percentage, 100) + '%';
        barElem.style.backgroundColor = percentage > 100 ? '#ff1744' : (percentage > 85 ? '#ff9100' : '#00e5ff');
    }*/

    if (prodElem) prodElem.textContent = Math.round(data.production);
    if (consElem) consElem.textContent = Math.round(data.consumption);

    if (barElem) {

        const percentage = data.production > 0 ? (data.consumption / data.production) * 100 : 0;

        const displayWidth = Math.min(percentage, 100);
        barElem.style.width = displayWidth + '%';

        if (percentage > 100) {
            barElem.style.backgroundColor = '#ff3e3e';
            barElem.classList.add('pulse-red');
        } else if (percentage > 85) {
            barElem.style.backgroundColor = '#ffaa00';
            barElem.classList.remove('pulse-red');
        } else {
            barElem.style.backgroundColor = '#00ff9d';
            barElem.classList.remove('pulse-red');
        }
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