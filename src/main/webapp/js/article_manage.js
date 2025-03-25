

    layui.use(['form', 'laypage', 'layer', 'jquery'], function () {
        var form = layui.form,
            laypage = layui.laypage,
            layer = layui.layer,
            $ = layui.$;
        let currentKeywords = "";

        // 动态加载分类下拉框
        function loadCategories() {
            fetch('/portal/findAllTypes')
                .then(res => res.json())
                .then(data => {
                    if (data.code === 200) {
                        const selectElem = document.getElementById("category-select");

                        console.log('Select element:', selectElem); // 检查结果是否为null
                        selectElem.innerHTML = '<option value="">请选择分类</option>';
                        data.data.forEach(cat => {
                            $(selectElem).append(`<option value="${cat.tid}">${cat.tname}</option>`);
                        });

                        // **手动重新渲染 Layui 下拉框**
                        form.render('select');
                    } else {
                        layer.msg("获取分类失败：" + data.message);
                    }
                });
        }

        // 加载文章列表
        function loadArticlePage(pageNum, pageSize, keyWords = "") {
            let categoryValue = $("#category-select").val() || 0;

            fetch('/portal/findArticlePage', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    pageNum: pageNum,
                    pageSize: pageSize,
                    type: categoryValue,
                    keyWords: keyWords
                })
            })
                .then(res => res.json())
                .then(response => {
                    if (response.code === 200) {
                        const pageInfo = response.data.pageInfo;
                        let html = '';
                        pageInfo.pageData.forEach(article => {
                            html += `
                            <tr>
                                <td>${article.title}</td>
                                <td>${article.typeName}</td>
                                <td>${article.publisherName}</td>
                                <td>${article.pageViews}</td>
                                <td>
                                    <button class="layui-btn layui-btn-xs layui-btn-normal edit-btn" data-hid="${article.hid}">编辑</button>
                                    <button class="layui-btn layui-btn-xs layui-btn-danger delete-btn" data-hid="${article.hid}">删除</button>
                                </td>
                            </tr>`;
                        });

                        $("#article-page").html(html);

                        // 调用 laypage 渲染分页组件
                        laypage.render({
                            elem: 'pagination',            // 分页容器的 id
                            count: pageInfo.totalSize,       // 数据总数
                            limit: pageInfo.pageSize,        // 每页显示条数
                            curr: pageInfo.pageNum,          // 当前页码
                            layout: ['limit', 'prev', 'page', 'next', 'skip', 'count'],
                            limits: [2, 4, 6, 8, 10],         // 可选的每页显示条数
                            jump: function(obj, first){
                                // first 为 true 表示首次执行，不需要重新加载数据
                                if (!first) {
                                    loadArticlePage(obj.curr, obj.limit, keyWords);
                                }
                            }
                        });
                    }
                });
        }

        // 删除文章
        $(document).on('click', '.delete-btn', function () {
            var hid = $(this).data('hid');
            layer.confirm('确定要删除这篇文章吗？', {icon: 3}, function (index) {
                fetch(`/headline/removeByHid?hid=${hid}`, {method: 'POST'})
                    .then(res => res.json())
                    .then(response => {
                        if (response.code === 200) {
                            layer.msg('删除成功', {icon: 1});
                            loadArticlePage(1, 4, currentKeywords);
                        } else {
                            layer.msg('删除失败: ' + response.message, {icon: 2});
                        }
                    });
                layer.close(index);
            });
        });

        // 编辑文章
        $(document).on('click', '.edit-btn', function () {
            var hid = $(this).data('hid');

            fetch(`/headline/findHeadlineByHid?hid=${hid}`)
                .then(res => res.json())
                .then(response => {
                    if (response.code === 200) {
                        const article = response.data.headline;
                        layer.open({
                            type: 1,
                            title: '编辑文章',
                            area: ['800px', '600px'],
                            content: `
<form class="layui-form" style="padding:20px">
                            <input type="hidden" id="edit-hid" value="${article.hid}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">标题</label>
                                <div class="layui-input-block">
                                    <input type="text" id="edit-title" value="${article.title}" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">分类</label>
                                <div class="layui-input-block">
                                    <select id="edit-type">
                                        <option value="">请选择分类</option>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">内容</label>
                                <div class="layui-input-block">
                                    <!-- CKEditor 5 编辑器容器 -->
                                    <textarea id="editor">${article.article}</textarea>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">封面图片</label>
                                <div class="layui-input-block">
                                    ${article.imageUrl ? `<img id="current-image" src="${article.imageUrl}" style="max-width:100px; display:block; margin-bottom:10px;">` : ''}
                                    <input type="file" id="edit-image" accept="image/*" class="layui-input">
                                </div>
                            </div>
                        </form>
                    `,
                            btn: ['保存', '取消'],
                            success: function (layero, index) {
                                // 动态加载分类下拉框，设置选中项（article.type 为 tid）
                                fetch('/portal/findAllTypes')
                                    .then(res => res.json())
                                    .then(data => {
                                        if (data.code === 200) {
                                            const select = layero.find('#edit-type');
                                            select.html(''); // 清空选项
                                            select.append('<option value="">请选择分类</option>');
                                            data.data.forEach(cat => {
                                                select.append(`<option value="${cat.tid}" ${cat.tid == article.type ? 'selected' : ''}>${cat.tname}</option>`);
                                            });
                                            layui.form.render('select');
                                        } else {
                                            layer.msg("加载分类失败：" + data.message);
                                        }
                                    })
                                    .catch(err => {
                                        layer.msg("加载分类异常：" + err.message);
                                    });
                            },
                            yes: function (index) {
                                // 收集编辑表单数据，并创建 FormData 用于上传
                                const updatedArticle = {
                                    hid: $('#edit-hid').val(),
                                    title: $('#edit-title').val(),
                                    article: $('#edit-content').val(),
                                    type: $('#edit-type').val()
                                };

                                const formData = new FormData();
                                formData.append("hid", updatedArticle.hid);
                                formData.append("title", updatedArticle.title);
                                formData.append("article", updatedArticle.article);
                                formData.append("type", updatedArticle.type);
                                const fileInput = $('#edit-image')[0];
                                if (fileInput && fileInput.files && fileInput.files[0]) {
                                    formData.append("image", fileInput.files[0]);
                                }

                                fetch('/headline/update', {
                                    method: 'POST',
                                    body: formData
                                })
                                    .then(res => res.json())
                                    .then(resp => {
                                        if (resp.code === 200) {
                                            layer.close(index);
                                            layer.msg("文章更新成功！", {icon: 1});
                                            loadArticlePage(1, 4, currentKeywords);
                                        } else {
                                            layer.msg("更新失败：" + resp.message, {icon: 2});
                                        }
                                    })
                                    .catch(err => {
                                        layer.msg("请求失败，请稍后重试。");
                                    });
                            }
                        });
                    } else {
                        layer.msg("获取文章详情失败：" + response.message);
                    }
                })
                .catch(err => {
                    layer.msg("请求失败，请稍后重试。");
                });
        });


        // 文章搜索功能
        $("#search-btn").click(() => {
            currentKeywords = $("#search-input").val().trim();
            loadArticlePage(1, 4, currentKeywords);
        });

        loadCategories();
        loadArticlePage(1, 4);
    });

