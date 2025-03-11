layui.use(['layer'], function(){
    var layer = layui.layer;

    // 定义加载分类数据的函数
    function loadCategories() {
        fetch('portal/findAllTypes')
            .then(function(response){
                return response.json();
            })
            .then(function(data){
                if(data.code === 200){
                    var categories = data.data;
                    var $tbody = $('#categoryTable tbody');
                    $tbody.empty();
                    categories.forEach(function(category, index) {
                        var row = `
                            <tr data-id="${category.tid}">
                                <td>${index + 1}</td>
                                <td>${category.tname}</td>
                                <td>
                                    <button class="layui-btn layui-btn-sm edit-btn">编辑</button>
                                    <button class="layui-btn layui-btn-sm layui-btn-danger delete-btn">删除</button>
                                </td>
                            </tr>
                        `;
                        $tbody.append(row);
                    });
                } else {
                    console.error('获取分类数据失败：', data.message);
                }
            })
            .catch(function(error){
                console.error('请求失败：', error);
            });
    }

    // 初次加载分类数据
    loadCategories();

    // 添加分类：点击“添加分类”按钮时弹出输入框
    $('.layui-btn-normal').click(function(){
        layer.prompt({
            formType: 0,
            title: '请输入新的分类名称'
        }, function(value, index, elem){
            // 使用 GET 方式传参，确保后端可以用 req.getParameter("tname") 获取到参数
            fetch('portal/addType?tname=' + encodeURIComponent(value), {
                method: 'GET'
            })
                .then(function(response){
                    return response.json();
                })
                .then(function(data){
                    if(data.code === 200){
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                        loadCategories();
                    } else {
                        layer.msg("添加失败：" + data.message, {icon: 2});
                    }
                })
                .catch(function(error){
                    console.error('添加请求失败：', error);
                    layer.msg("请求失败", {icon: 2});
                });
        });
    });

    // 删除分类：点击删除按钮时弹出确认框
    $('#categoryTable').on('click', '.delete-btn', function(){
        var tid = $(this).closest('tr').data('id');
        layer.confirm('确定删除此分类吗？', {icon: 3, title:'提示'}, function(confirmIndex){
            fetch('portal/deleteType', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                // 采用 URL 编码方式传参，确保后端能通过 req.getParameter("tid") 获取到参数
                body: 'tid=' + encodeURIComponent(tid)
            })
                .then(function(response){
                    return response.json();
                })
                .then(function(data){
                    if(data.code === 200){
                        layer.msg("删除成功", {icon: 1});
                        loadCategories();
                    } else {
                        layer.msg("删除失败：" + data.message, {icon: 2});
                    }
                })
                .catch(function(error){
                    console.error('删除请求失败：', error);
                    layer.msg("请求失败", {icon: 2});
                });
            layer.close(confirmIndex);
        });
    });

    // 编辑分类：点击编辑按钮时弹出回显输入框
    $('#categoryTable').on('click', '.edit-btn', function(){
        var $tr = $(this).closest('tr');
        var tid = $tr.data('id');
        var currentName = $tr.find('td:nth-child(2)').text();

        layer.prompt({
            formType: 0,
            value: currentName,
            title: '修改分类名称'
        }, function(value, index, elem){
            if(value && value !== currentName) {
                fetch('portal/updateType', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'tid=' + encodeURIComponent(tid) + '&tname=' + encodeURIComponent(value)
                })
                    .then(function(response){
                        return response.json();
                    })
                    .then(function(data){
                        if(data.code === 200){
                            layer.msg("更新成功", {icon: 1});
                            loadCategories();
                        } else {
                            layer.msg("更新失败：" + data.message, {icon: 2});
                        }
                    })
                    .catch(function(error){
                        console.error('更新请求失败：', error);
                        layer.msg("请求失败", {icon: 2});
                    });
            }
            layer.close(index);
        });
    });
});
