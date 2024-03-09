let problemId = null;
document.addEventListener('DOMContentLoaded', function () {
    problemId = document.getElementById('problemId').value;
});

function openOthersReply() {
    window.open('/reply/others?problemId=' + problemId);
}