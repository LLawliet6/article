$(document).ready(function() {
    // 获取当前页面的文章ID（hid）
    const urlParams = new URLSearchParams(window.location.search);
    const hid = urlParams.get("hid");  // 从 URL 中获取文章ID

    if (!hid) {
        alert("没有找到文章ID");
        return;
    }

    // 获取 token
    const token = localStorage.getItem('token');
    // if (!token) {
    //     alert("请先登录");
    //     return;
    // }

    // 确保 token 获取后再执行 checkIfFavorited
    checkIfFavorited(hid, token);

    // 点击收藏按钮
    $("#favorite-button").click(function() {
        // 使用 axios 发起请求
        axios.post(`/headline/favorite?hid=${hid}`, {}, {
            headers: {
                "token": token  // 添加 token 到请求头
            }
        })
            .then(function(response) {
                if (response.data.code === 200) {
                    // 根据返回的信息更新按钮和图标
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

    // 页面加载时检查是否收藏
    function checkIfFavorited(hid, token) {
        // 确保 token 已经完全准备好，再发送请求
        axios.post(`/headline/favoriteStatus?hid=${hid}`, {}, {
            headers: {
                "token": token  // 添加 token 到请求头
            }
        })
            .then(function(response) {
                if (response.data.code === 200) {
                    // 根据返回的数据更新按钮状态
                    updateFavoriteButton(response.data.data);
                } else {
                    console.error('Error:', error);  // 如果请求失败，提示错误
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }

    // 更新收藏按钮和图片
    function updateFavoriteButton(message) {
        const img = $("#favorite-icon");

        if (message === "已收藏") {
            img.attr("src", "images/取消收藏.png");  // 更新图片为 取消收藏
        } else {
            img.attr("src", "images/收藏.png");  // 更新图片为 收藏
        }
    }
});
