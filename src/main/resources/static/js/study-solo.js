let cursorId = 0;
let category = new URL(location.href).searchParams.get('category');
let isFetching = false; // 현재 데이터를 로딩 중인지 여부
let problemId = -1;

let submitButton = document.getElementById('submit-button');
let exampleAnswer = document.getElementById('example-answer');
let othersAnswer = document.getElementById('others-answer');

document.addEventListener("DOMContentLoaded", function () {
    submitButton = document.getElementById('submit-button');
    exampleAnswer = document.getElementById('example-answer');
    othersAnswer = document.getElementById('others-answer');
    loadNewProblems();
});

function loadNewProblems() {
    console.log("Loading New Problems...");
    isFetching = true; // 데이터 로딩 중으로 표시

    axios.get('/api/problem/' + category, {
        params: {
            cursorId: cursorId
        }
    }).then(function (response) {
        console.log(response);
        const problems = response.data;
        if (problems.length === 0) {
            alert('해당 카테고리에는 더 이상 문제가 존재하지 않습니다.');
            return;
        }
        const problemList = document.getElementById('problem-list');

        problems.forEach(function (problem) {
            problemList.appendChild(buildProblemItem(problem));
        });
        cursorId = problems[problems.length - 1].problemId;

    }).catch(function (error) {
        console.log(error);
        alert('카테고리가 유효하지 않습니다.')
    });
    isFetching = false; // 데이터 로딩 완료로 표시
}

function submitReply() {
    axios.post('/api/reply', {
        problemId: problemId,
        answer: document.getElementById('answer').value
    }).then(function (response) {
        console.log(response);
        if (response.status === 201) {
            alert('답안이 제출되었습니다.');
            submitButton.innerText = '답안 수정';
            submitButton.setAttribute('onclick', 'updateReply()');
            submitButton
                .className="text-gray-900 bg-gradient-to-r from-red-200 via-red-300 to-yellow-200 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-red-100 dark:focus:ring-red-400 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2";

            exampleAnswer
                .disabled = !exampleAnswer.disabled;
            exampleAnswer
                .classList.remove('cursor-not-allowed')

            othersAnswer
                .disabled = !othersAnswer.disabled;
            othersAnswer
                .classList.remove('cursor-not-allowed')
        }
    }).catch(function (error) {
        console.log(error);
        alert('답안 제출에 실패했습니다.');
    });
}

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

function createSvgElement() {
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('class', 'w-4 h-4 ms-3 rtl:rotate-180 text-gray-500 dark:text-gray-400');
    svg.setAttribute('aria-hidden', 'true');
    svg.setAttribute('fill', 'none');
    svg.setAttribute('viewBox', '0 0 14 10');
    svg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');

    const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    path.setAttribute('stroke', 'currentColor');
    path.setAttribute('stroke-linecap', 'round');
    path.setAttribute('stroke-linejoin', 'round');
    path.setAttribute('stroke-width', '2');
    path.setAttribute('d', 'M1 5h12m0 0L9 1m4 4L9 9');

    svg.appendChild(path);

    return svg;
}


function buildProblemItem(problem) {
    const problemItem = document.createElement('li');
    const input = document.createElement('input');
    input.addEventListener('click', () => selectProblem(problem));
    input.setAttribute('type', 'button');
    input.setAttribute('id', 'problem-' + problem.problemId);
    input.setAttribute('name', 'problem');
    input.setAttribute('value', 'problem-' + problem.problemId);
    input.setAttribute('class', 'hidden peer');
    const label = document.createElement('label');
    label.setAttribute('for', 'problem-' + problem.problemId);
    label.setAttribute('class', 'inline-flex items-center justify-between w-full p-5 text-gray-900 bg-white border border-gray-200 rounded-lg cursor-pointer dark:hover:text-gray-300 dark:border-gray-500 dark:peer-checked:text-blue-500 peer-checked:border-blue-600 peer-checked:text-blue-600 hover:text-gray-900 hover:bg-gray-100 dark:text-white dark:bg-gray-600 dark:hover:bg-gray-500');
    const div = document.createElement('div');
    div.setAttribute('class', 'block');
    const question = document.createElement('div');
    question.setAttribute('class', 'w-full text-lg font-semibold');
    question.innerText = problem.question;
    const category = document.createElement('div');
    category.setAttribute('class', 'w-full text-gray-500 dark:text-gray-400');
    category.innerText = problem.category;
    div.appendChild(question);
    div.appendChild(category);
    label.appendChild(div);
    label.appendChild(createSvgElement());
    problemItem.appendChild(input);
    problemItem.appendChild(label);
    return problemItem;
}

function selectProblem(problem) {
    problemId = problem.problemId;
    document.getElementById('question').innerText = problem.question;

    submitButton
        .innerText = '답안 제출'
    submitButton
        .setAttribute('onclick', 'submitReply()');
    submitButton
        .setAttribute('class', 'text-white bg-gradient-to-r from-red-400 via-red-500 to-red-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2');
}