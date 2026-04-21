let stompClient = null;

function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    // Disable debug logging to keep console clean
    stompClient.debug = null;
    
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        
        // Subscribe to user-specific notifications
        stompClient.subscribe('/user/queue/notifications', function (notification) {
            handleNotification(JSON.parse(notification.body));
        });
    }, function(error) {
        console.error('STOMP error:', error);
        setTimeout(connect, 5000); // Reconnect attempt
    });
}

function handleNotification(payload) {
    // Show toast
    const toastEl = document.getElementById('baseNotificationToast');
    if (toastEl) {
        document.getElementById('toastMessage').innerText = payload.message;
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }
    
    // Dynamically update DOM if we are on complaint detail page
    const statusBadge = document.getElementById('status-badge-' + payload.complaintId);
    if (statusBadge) {
        statusBadge.innerText = payload.newStatus;
        statusBadge.className = 'badge badge-' + payload.newStatus;
    }
    
    // Play sound (optional)
    // new Audio('/sounds/notification.mp3').play();
}

// Connect when page loads
document.addEventListener("DOMContentLoaded", function() {
    connect();
});
