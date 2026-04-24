let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/ws-station');
    stompClient = Stomp.over(socket);



    stompClient.connect({}, function (frame) {
        console.log('Connecté au WebSocket : ' + frame);

        stompClient.subscribe('/topic/device-status', function (response) {
            const device = JSON.parse(response.body);
            console.log("Update reçu pour le device :", device);
            updateUI(device);
        });
    }, function(error) {
        console.log("Erreur WebSocket, tentative de reconnexion dans 5s...");
        setTimeout(connectWebSocket, 5000);
    });
}

function updateDeviceUI(device) {

    const statusText = document.getElementById('status-text-' + device.id);
    const btn = document.getElementById('btn-' + device.id);
    const repairBtn = document.getElementById('btn-repair-' + device.id);

    if (statusText) {

        statusText.textContent = device.status.displayValue;

        if (device.status.name === 'ONLINE') {
            btn.disabled = false;
            btn.textContent = 'Éteindre';
            btn.className = 'btn-toggle off';
            if (repairBtn) repairBtn.style.display = 'none';
        } else if (device.status.name === 'OFFLINE') {
            btn.disabled = false;
            btn.textContent = 'Allumer';
            btn.className = 'btn-toggle on';
            if (repairBtn) repairBtn.style.display = 'none';
        }
    } else {
        console.warn("Élément HTML introuvable pour le device " + device.id);
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