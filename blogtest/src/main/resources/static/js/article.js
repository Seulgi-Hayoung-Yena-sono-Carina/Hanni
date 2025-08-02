const createButton = document.getElementById('create-btn');

if (createButton) {
  createButton.addEventListener('click', event => {
    const formData = new FormData();
    formData.append("title", document.getElementById("title").value);
    formData.append("content", document.getElementById("content").value);

    const files = document.getElementById("imageFiles").files;
    for (let i = 0; i < files.length; i++) {
      formData.append("imageFiles", files[i]);
    }

    fetch("/api/articles", {
      method: "POST",
      headers: {
        Authorization: "Bearer " + localStorage.getItem("access_token")
      },
      body: formData
    }).then(response => {
      if (response.ok) {
        alert("글 등록이 완료되었습니다.");
        location.replace("/articles");
      } else {
        alert("글 등록에 실패했습니다.");
      }
    });
  });
}


// 수정 기능 (여러 장 이미지 업로드 지원)
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
  modifyButton.addEventListener('click', event => {
    let params = new URLSearchParams(location.search);
    let id = params.get('id');

    const formData = new FormData();
    formData.append("title", document.getElementById("title").value);
    formData.append("content", document.getElementById("content").value);

    const files = document.getElementById("imageFiles").files;
    for (let i = 0; i < files.length; i++) {
      formData.append("imageFiles", files[i]);
    }

    fetch(`/api/articles/${id}`, {
      method: "PUT",
      headers: {
        Authorization: "Bearer " + localStorage.getItem("access_token")
      },
      body: formData
    }).then(response => {
      if (response.ok) {
        alert("글이 수정 완료되었습니다.");
        location.replace(`/articles/${id}`);
      } else {
        alert("글을 수정할 권한이 없습니다.");
      }
    });
  });
}



// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        function success() {
            alert('글의 삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('글을 삭제할 권한이 없습니다.');
            location.replace('/articles');
        }

        httpRequest('DELETE',`/api/articles/${id}`, null, success, fail);
    });
}



// 로그아웃 기능
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {

            localStorage.removeItem('access_token');
            deleteCookie('refresh_token');
            alert('로그아웃 되었습니다!');
            location.replace('/login');
        }

        function fail() {
            alert('로그아웃 실패했습니다.');
        }
        httpRequest('DELETE', '/api/refresh-token', null, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// 쿠키를 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => {
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}