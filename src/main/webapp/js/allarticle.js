$(document).ready(function () {
    let currentKeywords = ""; // 存储当前的搜索关键词

    // 初始加载文章列表
    loadArticlePage(1, 4);

    // 搜索按钮的点击事件
    $('#search-btn').click(function () {
        currentKeywords = $('#search-input').val(); // 更新当前搜索关键词
        loadArticlePage(1, 4, currentKeywords);
    });

    // 回车搜索
    $('#search-input').keypress(function (event) {
        if (event.which === 13) {
            currentKeywords = $('#search-input').val();
            loadArticlePage(1, 4, currentKeywords);
        }
    });

    // 返回文章列表按钮
    $('#back-btn').click(function () {
        $('#article-detail').hide();
        $('.article-page').show();
    });
});

// 加载文章列表
function loadArticlePage(pageNum, pageSize, keyWords = "") {
    $.ajax({
        url: '/portal/findArticlePage',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            pageNum: pageNum,
            pageSize: pageSize,
            type: 0,
            keyWords: keyWords
        }),
        dataType: 'json',
        success: function (response) {
            if (response.code === 200) {
                const pageInfo = response.data.pageInfo;
                const articles = pageInfo.pageData;

                // 清空 article-page 的内容
                $('.article-page').empty();

                // 动态生成文章列表
                articles.forEach(function (article) {
                    const articleElement = generateArticleElement(article);
                    $('.article-page').append(articleElement);
                });

                // 绑定文章操作按钮的点击事件
                bindArticleButtons();

                // 生成分页器
                generatePagination(pageInfo.pageNum, pageInfo.totalPage);
            } else {
                $('.article-page').html('<p>没有找到相关文章。</p>');
            }
        }
    });
}

// 生成每篇文章的HTML内容
function generateArticleElement(article) {
    return `
        <div class="article-item" data-hid="${article.hid}">
            <h4>${article.title}</h4>
            <p>类别：${article.type}</p>
            <p>浏览量: ${article.pageViews} 次</p>
            <p>发布时间: ${article.pastHours} 小时前</p>
            <p>发布者ID: ${article.publisher}</p>
            <button class="read-more-btn" data-hid="${article.hid}">阅读全文</button>
            <button class="edit-btn" data-hid="${article.hid}" style="color: green;">修改</button>
            <button class="delete-btn" data-hid="${article.hid}" style="color: red;">删除</button>
        </div>
    `;
}

// 绑定“阅读全文”、“编辑”和“删除”按钮事件
function bindArticleButtons() {
    $('.read-more-btn').click(function () {
        var articleHid = $(this).data('hid');
        loadArticleDetail(articleHid);
    });

    $('.edit-btn').click(function () {
        var articleHid = $(this).data('hid');
        editArticle(articleHid);
    });

    $('.delete-btn').click(function () {
        var articleHid = $(this).data('hid');
        deleteArticle(articleHid);
    });
}

// 加载文章详情
function loadArticleDetail(hid) {
    $.ajax({
        url: '/portal/showHeadlineDetail',
        method: 'GET',
        data: { hid: hid },
        success: function (response) {
            if (response.code === 200) {
                var articleData = response.data.headline;
                $('#article-title').text(articleData.title);
                $('#article-type').text('分类: ' + articleData.typeName);
                $('#article-author').text('作者: ' + (articleData.publisherName ? articleData.publisherName : '匿名'));
                $('#article-pastHours').text('发布时间: ' + articleData.pastHours + ' 小时前');
                $('#article-views').text('浏览量: ' + articleData.pageViews);
                $('#article-content').html(articleData.article);

                // 加载评论
                loadComments(hid);

                // 隐藏文章列表，显示文章详情
                $('.article-page').hide();
                $('#article-detail').show();
            } else {
                alert('获取文章详情失败: ' + response.message);
            }
        },
        error: function () {
            alert('请求失败，请稍后重试。');
        }
    });
}

// 删除文章
function deleteArticle(hid) {
    if (confirm("确定要删除这篇文章吗？")) {
        $.ajax({
            url: `/headline/removeByHid?hid=${hid}`,
            method: 'POST',
            success: function (response) {
                if (response.code === 200) {
                    alert("文章删除成功！");
                    loadArticlePage(1, 4, currentKeywords); // 刷新文章列表
                } else {
                    alert("删除失败：" + response.message);
                }
            },
            error: function () {
                alert("请求失败，请稍后重试。");
            }
        });
    }
}

// 编辑文章
function editArticle(hid) {
    $.ajax({
        url: `/headline/findHeadlineByHid?hid=${hid}`,
        method: 'GET',
        success: function (response) {
            if (response.code === 200) {
                const article = response.data.headline;
                $('#edit-title').val(article.title);
                $('#edit-type').val(article.type);
                $('#edit-content').val(article.article);
                $('#edit-hid').val(article.hid);
                $('#edit-modal').show(); // 显示编辑模态框
            } else {
                alert("获取文章详情失败：" + response.message);
            }
        },
        error: function () {
            alert("请求失败，请稍后重试。");
        }
    });
}

$('#save-edit-btn').click(function () {
    const updatedArticle = {
        hid: $('#edit-hid').val(),
        title: $('#edit-title').val(),
        article: $('#edit-content').val(),
        type: $('#edit-type').val()
    };

    $.ajax({
        url: '/headline/update',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedArticle),
        success: function (response) {
            if (response.code === 200) {
                alert("文章更新成功！");
                $('#edit-modal').hide(); // 关闭编辑模态框
                loadArticlePage(1, 4, currentKeywords); // 刷新文章列表
            } else {
                alert("更新失败：" + response.message);
            }
        },
        error: function () {
            alert("请求失败，请稍后重试。");
        }
    });
});

$('#cancel-edit-btn').click(function () {
    $('#edit-modal').hide();
});

// 加载评论
function loadComments(articleId) {
    $.ajax({
        url: '/comment/getComments',
        method: 'GET',
        data: { articleId: articleId },
        success: function (response) {
            if (response.code === 200) {
                const comments = response.data;
                $('#comments-list').empty(); // 清空评论列表

                if (comments.length > 0) {
                    comments.forEach(function (comment) {
                        const commentElement = `
                            <div class="comment-item">
                                <p><strong>${comment.username}</strong> (${comment.createTime})</p>
                                <p>${comment.content}</p>
                            </div>
                        `;
                        $('#comments-list').append(commentElement);
                    });
                } else {
                    $('#comments-list').append('<p>暂时没有评论。</p>');
                }
            } else {
                $('#comments-list').html('<p>加载评论失败。</p>');
            }
        },
        error: function () {
            alert('加载评论请求失败，请稍后重试。');
        }
    });
}

// 提交评论
$('#submit-comment').click(function () {
    const content = $('#comment-content').val();
    if (!content) {
        alert('评论内容不能为空');
        return;
    }

    $.ajax({
        url: '/comment/addComment',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            articleId: currentArticleId,  // 使用当前文章ID
            userId: 1,  // 假设用户ID为1，你需要从实际登录的用户信息中获取
            content: content
        }),
        success: function (response) {
            if (response.code === 200) {
                $('#comment-content').val(''); // 清空评论输入框
                loadComments(currentArticleId); // 重新加载评论
            } else {
                alert('提交评论失败: ' + response.message);
            }
        },
        error: function () {
            alert('提交评论请求失败，请稍后重试。');
        }
    });
});

// 生成分页器
function generatePagination(currentPage, totalPages) {
    $('.pagination').empty();

    for (let i = 1; i <= totalPages; i++) {
        const pageItem = $('<li class="page-item"></li>');
        const pageLink = $('<a class="page-link" href="javascript:void(0);"></a>').text(i);

        if (i === currentPage) {
            pageItem.addClass('active');
        }

        pageItem.append(pageLink);
        $('.pagination').append(pageItem);

        pageLink.click(function () {
            loadArticlePage(i, 4, currentKeywords);
        });
    }
}

// 错误处理
function handleAjaxError(xhr, status, error) {
    alert("请求失败，请稍后重试。");
}
