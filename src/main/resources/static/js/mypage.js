function linkGithubRepo() {
    const repoName = prompt("Github repository 명을 입력해주세요. (예: CS_Study)");
    if (repoName === undefined)
        return;
    axios.patch('/api/users/github-repo', {
        githubRepo: repoName
    }).then(function (response) {
        console.log(response);
        alert('Github repository 연동이 완료되었습니다.');
    }).catch(function (error) {
        console.log(error);
        alert('Github repository 연동에 실패하였습니다.');
    });
}
