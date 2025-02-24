//auth.js
// 显示用户名或登录/注册
window.onload = function() {
    const username = localStorage.getItem("username");
console.log("After loading, localStorage:", localStorage);
    if (username) {
        document.getElementById("username-display").style.display = "block";
        document.getElementById("login-register").style.display = "none";
        document.getElementById("logout").style.display = "block";
        document.getElementById("logged-username").textContent = `您好， ${username}`;
    }
};

function logout() {
    localStorage.removeItem("username");
    localStorage.removeItem("token"); // 确保移除token
    console.log("After logout, localStorage:", localStorage);
    location.reload();
}

