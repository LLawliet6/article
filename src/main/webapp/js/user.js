// 获取当前用户信息
function getCurrentUser() {
    const token = localStorage.getItem('token');
    let currentUser = null;
    if (token) {
        $.ajax({
            url: '/user/getUserInfo',
            method: 'GET',
            headers: {'token': token},
            async: false, // 同步请求，确保返回数据后再执行后续逻辑
            success: function (response) {
                if (response.code === 200) {
                    currentUser = response.data.loginUser;
                }
            },
            error: function () {
                console.error("获取用户信息失败");
            }
        });
    }
    return currentUser;
}
