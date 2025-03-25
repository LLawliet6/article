layui.use(['layer', 'jquery', 'laypage'], function(){
    var layer = layui.layer,
        $ = layui.$,
        laypage = layui.laypage;

    var currentKeywords = ""; // 如有搜索需求，可扩展此参数
    var currentType = 0;      // 如有分类筛选，可扩展此参数

    // 加载文章列表（调用 /portal/findArticlePage）
    function loadArticlePage(pageNum, pageSize, keyWords = "") {
        fetch('/portal/findArticlePage', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                pageNum: pageNum,
                pageSize: pageSize,
                type: currentType,
                keyWords: keyWords
            })
        })
            .then(res => res.json())
            .then(data => {
                if(data.code === 200){
                    var pageInfo = data.data.pageInfo;
                    var articles = pageInfo.pageData;
                    var $tbody = $('#articleTable tbody');
                    $tbody.empty();
                    articles.forEach(function(article){
                        // 每篇文章占一行，后面添加一行隐藏的评论区域
                        var articleRow = `
            <tr data-id="${article.hid}">
              <td>${article.hid}</td>
              <td>${article.title}</td>
              <td>${article.typeName}</td>
              <td>${article.publisherName}</td>
              <td>
                <button class="layui-btn layui-btn-sm show-comments">查看评论</button>
              </td>
            </tr>
            <tr class="comment-row" data-id="${article.hid}" style="display:none;">
              <td colspan="5">
                <table class="layui-table" style="margin: 0;">
                  <thead>
                    <tr>
                      <th>评论内容</th>
                      <th>评论者</th>
                      <th>评论时间</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody class="comment-body">
                    <!-- 该文章的评论数据 -->
                  </tbody>
                </table>
              </td>
            </tr>
          `;
                        $tbody.append(articleRow);
                    });

                    // 使用 Layui 分页组件
                    laypage.render({
                        elem: 'pagination',
                        count: pageInfo.totalSize,
                        limit: pageInfo.pageSize,
                        curr: pageInfo.pageNum,
                        layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                        limits: [2, 4, 6, 8, 10],
                        jump: function(obj, first){
                            if(!first){
                                loadArticlePage(obj.curr, obj.limit, keyWords);
                            }
                        }
                    });
                } else {
                    layer.msg("加载文章失败：" + data.message, {icon:2});
                }
            })
            .catch(error => {
                console.error("请求失败：", error);
                layer.msg("请求失败，请稍后重试", {icon:2});
            });
    }

    // 加载指定文章的评论（调用 /comment/findCommentsByArticleId?articleId=...）
    function loadComments(articleId) {
        fetch('/comment/findCommentsByArticleId?articleId=' + encodeURIComponent(articleId))
            .then(res => res.json())
            .then(data => {
                if(data.code === 200){
                    var comments = data.data;
                    var $commentBody = $('tr.comment-row[data-id="' + articleId + '"] .comment-body');
                    $commentBody.empty();
                    if(comments.length === 0){
                        $commentBody.append('<tr><td colspan="4" style="text-align:center;">暂无评论</td></tr>');
                    } else {
                        comments.forEach(function(comment){
                            var row = `
                <tr data-cid="${comment.cid}">
                  <td>${comment.content}</td>
                  <td>${comment.username}</td>
                  <td>${comment.createTime}</td>
                  <td>
                    <button class="layui-btn layui-btn-xs layui-btn-danger delete-comment" data-cid="${comment.cid}">删除</button>
                  </td>
                </tr>
              `;
                            $commentBody.append(row);
                        });
                    }
                } else {
                    layer.msg("加载评论失败：" + data.message, {icon:2});
                }
            })
            .catch(error => {
                console.error("请求评论失败：", error);
                layer.msg("请求评论失败，请稍后重试", {icon:2});
            });
    }

    // 点击“查看评论”按钮，展开或收起评论区域
    $('#articleTable').on('click', '.show-comments', function(){
        var $tr = $(this).closest('tr');
        var articleId = $tr.data('id');
        var $commentRow = $('tr.comment-row[data-id="' + articleId + '"]');
        if($commentRow.is(':visible')){
            $commentRow.hide();
        } else {
            loadComments(articleId);
            $commentRow.show();
        }
    });

    // 删除评论操作，点击删除按钮时触发
    $('#articleTable').on('click', '.delete-comment', function(){
        var cid = $(this).data('cid');
        // 获取当前文章ID，便于删除后刷新评论
        var articleId = $(this).closest('tr.comment-row').data('id') ||
            $(this).closest('.comment-body').closest('tr.comment-row').data('id');
        layer.confirm('确定删除该评论吗？', {icon: 3, title:'提示'}, function(confirmIndex){
            fetch('/comment/deleteComment?cid=' + encodeURIComponent(cid), {
                method: 'GET'
            })
                .then(response => response.json())
                .then(data => {
                    if(data.code === 200){
                        layer.msg("删除成功", {icon: 1});
                        loadComments(articleId);
                    } else {
                        layer.msg("删除失败：" + data.message, {icon:2});
                    }
                })
                .catch(error => {
                    console.error("删除请求失败：", error);
                    layer.msg("请求失败，请稍后重试", {icon:2});
                });
            layer.close(confirmIndex);
        });
    });

    // 初始加载文章列表，第一页，每页显示4条数据
    loadArticlePage(1, 4, currentKeywords);
});
