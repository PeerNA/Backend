let problemId = null;
document.addEventListener('DOMContentLoaded', function () {
    problemId = document.getElementById('problemId').value;
});

function updateReply() {
    axios.patch('/api/reply', {
        problemId: problemId,
        answer: document.getElementById('answer').value
    }).then(function (response) {
        console.log(response);
        alert('답안이 수정되었습니다.');
    }).catch(function (error) {
        console.log(error);
        alert('답안 수정에 실패했습니다.');
    });
}

function openOthersReply() {
    window.open('/reply/others?problemId=' + problemId);
}