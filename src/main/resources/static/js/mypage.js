function linkGithubRepo() {
    const repoName = prompt("Github repository 명을 입력해주세요. (예: CS_Study)");
    if (repoName === undefined)
        return;
    axios.patch('/api/users/github-repo', {
        githubRepo: repoName
    }).then(function (response) {
        console.log(response);
        alert('GitHub Repository Name 이 등록되었습니다.');
    }).catch(function (error) {
        console.log(error);
        alert('GitHub Repository Name 등록에 실패했습니다.');
    });
}
