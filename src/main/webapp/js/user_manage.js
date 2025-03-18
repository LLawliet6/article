layui.use(['layer'], function(){
    var layer = layui.layer;

    // 加载用户数据
    function loadUsers() {
        fetch('/user/findAll')
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    var users = data.data;
                    var $tbody = $('#userTable tbody');
                    $tbody.empty();
                    users.forEach((user, index) => {
                        var row = `
                            <tr data-id="${user.uid}">
                                <td>${index + 1}</td>
                                <td>${user.username}</td>
                                <td>
                                    <button class="layui-btn layui-btn-sm edit-btn">编辑</button>
                                    <button class="layui-btn layui-btn-sm layui-btn-danger delete-btn">删除</button>
                                </td>
                            </tr>
                        `;
                        $tbody.append(row);
                    });
                } else {
                    console.error('获取用户数据失败：', data.message);
                }
            })
            .catch(error => {
                console.error('请求失败：', error);
            });
    }

    // 初次加载用户数据
    loadUsers();

    // 删除用户
    $('#userTable').on('click', '.delete-btn', function(){
        var uid = $(this).closest('tr').data('id');
        layer.confirm('确定删除该用户吗？', {icon: 3, title:'提示'}, function(confirmIndex){
            fetch(`/user/removeUser?uid=${encodeURIComponent(uid)}`, {
                method: 'GET'
            })
                .then(response => response.json())
                .then(data => {
                    if (data.code === 200) {
                        layer.msg("删除成功", {icon: 1});
                        loadUsers(); // 重新加载用户列表
                    } else {
                        layer.msg("删除失败：" + data.message, {icon: 2});
                    }
                })
                .catch(error => {
                    console.error('删除请求失败：', error);
                    layer.msg("请求失败", {icon: 2});
                });

            layer.close(confirmIndex);
        });
    });

    // 编辑用户
    $('#userTable').on('click', '.edit-btn', function(){
        var $tr = $(this).closest('tr');
        var uid = $tr.data('id');
        var currentUsername = $tr.find('td:nth-child(2)').text();

        layer.prompt({
            formType: 0,
            value: currentUsername,
            title: '修改用户名'
        }, function(value, index){
            if (value && value !== currentUsername) {
                fetch('/user/updateUser', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `uid=${encodeURIComponent(uid)}&username=${encodeURIComponent(value)}`
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.code === 200) {
                            layer.msg("更新成功", {icon: 1});
                            loadUsers();
                        } else {
                            layer.msg("更新失败：" + data.message, {icon: 2});
                        }
                    })
                    .catch(error => {
                        console.error('更新请求失败：', error);
                        layer.msg("请求失败", {icon: 2});
                    });
            }
            layer.close(index);
        });
    });
});
