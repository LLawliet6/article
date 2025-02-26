window.onload = function() {
    const username = localStorage.getItem("username");
    console.log("After loading, localStorage:", localStorage);

    if (username) {
        // 显示用户名
        document.getElementById("username-display").style.display = "block";
        document.getElementById("login-register").style.display = "none";
        document.getElementById("logged-username").textContent = `您好，${username}`;

        // 显示下拉框
        document.getElementById("user-dropdown").style.display = "block";

        // 为下拉菜单项绑定事件
        document.getElementById("profile-link").addEventListener("click", function() {
            // 这里你可以实现跳转到个人资料页面
            window.location.href = "profile.html";
        });

        document.getElementById("settings-link").addEventListener("click", function() {
            // 这里你可以实现跳转到设置页面
            window.location.href = "settings.html";
        });
    }
};
