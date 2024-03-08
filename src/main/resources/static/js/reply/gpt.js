let sockJs = null;
let stompClient = null;
let userId = null;
let lastChatId = null;
let chatList = document.getElementById("chat-list");
let loading = false;

document.addEventListener("DOMContentLoaded", function () {
    chatList = document.getElementById("chat-list");

    sockJs = new SockJS("/stomp");
    stompClient = Stomp.over(sockJs);
    userId = document.getElementById("userId").value;
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/user/gpt', function (frame) {
            if (!loading) {
                createChatCard();
                loading = true;
            }
            updateLastMessage(frame.body);
            console.log('/user/gpt: ', frame.body);
        });
    });
});

function createChatCard() {
    const time = new Date();
    if (lastChatId == null) {
        lastChatId = 0;
    }

    const li = document.createElement("li");
    li.className = "flex items-end justify-between bg-[#1E1E2D] text-white p-3 my-2 rounded-lg";

    const p = document.createElement("p");
    p.id = 'chat' + ++lastChatId;
    li.appendChild(p);

    const span = document.createElement("span");
    span.className = "ml-2 text-xs";
    span.innerText = time.getHours() + ":" + time.getMinutes();
    li.appendChild(span);

    chatList.appendChild(li);
}
function updateLastMessage(message) {
    if (message === '\n-----END MESSAGE-----\n') {
        loading = false;
        return;
    }

    const lastChatContent = document.getElementById('chat' + lastChatId);
    lastChatContent.innerText += message;
}