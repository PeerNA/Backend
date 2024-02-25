let sockJs = null;
let stompClient = null;
let userId = null;
document.addEventListener("DOMContentLoaded", function () {
    userId = document.getElementById("userId").value;
    sockJs = new SockJS("/stomp");
    stompClient = Stomp.over(sockJs);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        /*
        stompClient.subscribe('/user/' + userId + '/match', function (message) {
            console.log('/user/{userId}/match: ', message);
        });
        destination 은 /user/{userId}/match 이지만, /user/match 로 subscribe 해야한다.
         */

        stompClient.subscribe('/user/match/join', function (message) {
            console.log('/user/match: ', message);
        });

        stompClient.send("/app/info", {}, {}, function (error) {
            console.log('debug', error);
        });
    });
});

function joinQueue() {
    let category = document.getElementById("category").value;
    stompClient.send("/app/match/join", {}, category, function (error) {
        console.log('error', error);
    });
}
