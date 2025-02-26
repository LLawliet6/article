$(document).ready(function() {
    // 获取当前页面的文章ID（hid）
    const urlParams = new URLSearchParams(window.location.search);
    const hid = urlParams.get("hid");  // 从 URL 中获取参数 hid

    if (!hid) {
        alert("没有找到文章ID");
        return;
    }

    // 点击收藏按钮
    $("#favorite-button").click(function() {
        // 获取 token
        const token = localStorage.getItem('token');

        if (!token) {
            alert("请先登录");
            return;
        }

        // 使用 axios 发起收藏请求
        axios.post(`/headline/favorite?hid=${hid}`, {}, {  // 第二个参数为空对象，表示没有请求体
            headers: {
                "token": token  // 添加 token 到请求头
            }
        })
            .then(function(response) {
                if (response.data.code === 200) {
                    // 根据返回的信息更新按钮
                    updateFavoriteButton(response.data.data);
                } else {
                    alert(response.data.message);  // 如果请求失败，提示错误
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                alert("请求失败.");
            });
    });

    // 更新收藏按钮文本
    function updateFavoriteButton(message) {
        const button = $("#favorite-button");
        const img = $("#favorite-icon");

        if (message === "收藏成功") {
            img.attr("src", "images/取消收藏.png");  // 更新图片为 取消收藏
            // 更新按钮文本
        } else {
            img.attr("src", "images/收藏.png");  // 更新图片为 收藏
           // 更新按钮文本
        }
    }
});
