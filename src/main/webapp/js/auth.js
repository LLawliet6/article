// window.onload = function() {
//     const username = localStorage.getItem("username");
//     console.log("After loading, localStorage:", localStorage);
//
//     // 如果有用户名，显示下拉菜单及用户名
//     if (username) {
//         document.getElementById("username-display").style.display = "block";  // 显示用户名
//         document.getElementById("login-register").style.display = "none";  // 隐藏登录/注册
//         document.getElementById("logged-username").textContent = `您好， ${username}`;  // 设置用户名
//     } else {
//         // 如果没有用户名，显示登录/注册按钮
//         document.getElementById("username-display").style.display = "none";  // 隐藏用户名
//         document.getElementById("login-register").style.display = "block";  // 显示登录/注册
//     }
// };
//
// function logout() {
//     localStorage.removeItem("username");
//     localStorage.removeItem("token");  // 确保移除token
//     console.log("After logout, localStorage:", localStorage);
//
//     // 退出后更新显示状态
//     document.getElementById("username-display").style.display = "none";  // 隐藏用户名
//     document.getElementById("login-register").style.display = "block";  // 显示登录/注册按钮
//
//     location.reload();  // 重新加载页面，确保状态更新
// }
window.onload = function() {
    // 优先检查 sessionStorage
    let token = sessionStorage.getItem("token");
    let username = sessionStorage.getItem("username");
    // 如果有用户名，显示下拉菜单及用户名
    if (username) {
        document.getElementById("username-display").style.display = "block";  // 显示用户名
        document.getElementById("login-register").style.display = "none";  // 隐藏登录/注册
        document.getElementById("logged-username").textContent = `您好， ${username}`;  // 设置用户名
    } else {
        // 如果没有用户名，显示登录/注册按钮
        document.getElementById("username-display").style.display = "none";  // 隐藏用户名
        document.getElementById("login-register").style.display = "block";  // 显示登录/注册
    }

    // 如果 session 中没有，再检查 localStorage
    if (!token) {
        token = localStorage.getItem("token");
        username = localStorage.getItem("username");
    }

    console.log("登录状态检查结果:", { token, username });

    if (token) {
        document.getElementById("username-display").style.display = "block";
        document.getElementById("login-register").style.display = "none";
        document.getElementById("logged-username").textContent = `您好，${username}`;
    } else {
        document.getElementById("username-display").style.display = "none";
        document.getElementById("login-register").style.display = "block";
    }
};

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

    // 更新界面状态
    document.getElementById("username-display").style.display = "none";
    document.getElementById("login-register").style.display = "block";
    location.reload();
}