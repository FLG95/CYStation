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

function updateUI(device) {
    const statusText = document.getElementById('status-text-' + device.id);
    const btn = document.getElementById('btn-' + device.id);
    const repairBtn = document.getElementById('btn-repair-' + device.id);

    if (statusText) {

        statusText.textContent = device.status.displayValue;

        if (device.status.name === 'MAINTENANCE') {
            btn.disabled = true;
            btn.textContent = 'Allumer';
            btn.className = 'btn-toggle on';
            repairBtn.style.display = 'block';
        } else if (device.status.name === 'ONLINE') {
            btn.disabled = false;
            btn.textContent = 'Éteindre';
            btn.className = 'btn-toggle off';
            repairBtn.style.display = 'none';
        } else {
            btn.disabled = false;
            btn.textContent = 'Allumer';
            btn.className = 'btn-toggle on';
            repairBtn.style.display = 'none';
        }
    }
}

connectWebSocket();




