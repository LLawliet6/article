// admin.js

// 优先检查 sessionStorage
let token = sessionStorage.getItem("token");
let username = sessionStorage.getItem("username");

// 如果 sessionStorage 中没有，再检查 localStorage
if (!token) {
    token = localStorage.getItem("token");
    username = localStorage.getItem("username");
}

// 如果获取到用户名，则动态更新导航栏中的管理员名字
if (username) {
    // 找到导航栏中的管理员名字元素
    const adminNameElement = document.querySelector(".layui-nav-item .layui-nav-img + span");
    if (adminNameElement) {
        adminNameElement.textContent = username; // 更新用户名
    }
} else {
    // 如果没有获取到用户名，可以跳转到登录页面
    window.location.href = "login.html"; // 假设登录页面是 login.html
}

function logout() {
    // 同时清除两种存储
    localStorage.removeItem("username");
    localStorage.removeItem("token");
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("token");

    console.log("退出登录后存储状态:", {
        localStorage: localStorage,
        sessionStorage: sessionStorage
    });
    // 跳转到 login.html
    // 显示退出登录提示
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.msg('退出登录成功', {
            icon: 1, // 提示图标，1 表示成功
            time: 500 // 提示显示时间，单位毫秒（1 秒后自动关闭）
        }, function () {
            // 提示关闭后的回调函数
            window.location.href = 'login.html'; // 跳转到登录页面
        });
    });
}