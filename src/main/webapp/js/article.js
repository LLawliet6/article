// 文章相关操作
let currentKeywords = "";
let currentArticleId = null;

// 获取当前用户信息
function getCurrentUser() {
    const token = localStorage.getItem('token');
    let currentUser = null;
    if (token) {
        $.ajax({
            url: '/user/getUserInfo',
            method: 'GET',
            headers: { 'token': token },
            async: false,
            success: function (response) {
                if (response.code === 200) {
                    currentUser = response.data.loginUser;
                }
            },
            error: function () {
                console.error("获取用户信息失败");
            }
        });
    }
    return currentUser;
}
// 加载文章列表
function loadArticlePage(pageNum, pageSize, keyWords = "") {
    const token = localStorage.getItem('token');
    let currentUser = getCurrentUser(); // 使用 getCurrentUser 方法
    if (token && currentUser) {
        // 用户已登录
        loadArticlesWithUser(pageNum, pageSize, keyWords, currentUser);
    } else {
        // 未登录或获取用户信息失败
        loadArticlesWithUser(pageNum, pageSize, keyWords, null);
    }
}

// 加载文章数据，根据用户是否登录执行不同逻辑
function loadArticlesWithUser(pageNum, pageSize, keyWords, user) {
    $.ajax({
        url: '/article/loadArticles',
        method: 'GET',
        data: { pageNum, pageSize, keyWords },
        success: function (response) {
            if (response.code === 200) {
                const articles = response.data.articles;
                const totalPages = response.data.totalPages;
                renderArticles(articles, user);
                generatePagination(pageNum, totalPages);
            } else {
                console.error('加载文章失败:', response.message);
            }
        },
        error: function () {
            console.error('加载文章请求失败');
        }
    });
}
// 加载文章列表
function loadArticlePage(pageNum, pageSize, keyWords = "") {
    const token = localStorage.getItem('token');
    let currentUser = null;
    if (token) {
        $.ajax({
            url: '/user/getUserInfo',
            method: 'GET',
            headers: { 'token': token },
            async: false,
            success: function (response) {
                if (response.code === 200) {
                    currentUser = response.data.loginUser;
                    loadArticlesWithUser(pageNum, pageSize, keyWords, currentUser);
                }
            },
            error: function () {
                console.error("获取用户信息失败");
            }
        });
    } else {
        loadArticlesWithUser(pageNum, pageSize, keyWords, currentUser);
    }
}

function loadArticlesWithUser(pageNum, pageSize, keyWords, currentUser) {
    $.ajax({
        url: '/portal/findArticlePage',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ pageNum, pageSize, type: 0, keyWords }),
        dataType: 'json',
        success: function (response) {
            if (response.code === 200) {
                const articles = response.data.pageInfo.pageData;
                $('.article-page').empty();
                articles.forEach(function (article) {
                    const isOwner = (currentUser && (currentUser.uid === article.publisher || currentUser.username === 'admin'));
                    let typeImage = '';
                    switch (article.type) {
                        case 1: typeImage = '/images/type1.jpg'; break;
                        case 2: typeImage = '/images/type2.jpg'; break;
                        case 3: typeImage = '/images/type3.jpg'; break;
                        case 4: typeImage = '/images/type5.jpg'; break;
                        default: typeImage = '/images/type4.jpg';
                    }
                    $('.article-page').append(`
                        <div class="article-item" data-hid="${article.hid}" style="display: flex; align-items: flex-start; margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; border-radius: 5px;">
                            <img src="${typeImage}" alt="文章类别图片" style="width: 150px; height: auto; margin-right: 15px; border-radius: 5px;" />
                            <div style="flex: 1;">
                                <h4>${article.title}</h4>
                                <p>类别：${article.type}</p>
                                <p>浏览量: ${article.pageViews} 次</p>
                                <p>发布时间: ${article.pastHours} 小时前</p>
                                <p>发布者ID: ${article.publisher}</p>
                                <button class="read-more-btn" data-hid="${article.hid}">阅读全文</button>
                                ${isOwner ? `<button class="edit-btn" data-hid="${article.hid}" style="color: green;">修改</button><button class="delete-btn" data-hid="${article.hid}" style="color: red;">删除</button>` : ''}
                            </div>
                        </div>
                    `);
                });
                bindArticleButtons();
                generatePagination(response.data.pageInfo.pageNum, response.data.pageInfo.totalPage);
            } else {
                $('.article-page').html('<p>没有找到相关文章。</p>');
            }
        },
        error: function () {
            console.error("加载文章失败");
        }
    });
}

// 加载文章详情
function loadArticleDetail(hid) {
    currentArticleId = hid;
    $.ajax({
        url: '/portal/showHeadlineDetail',
        method: 'GET',
        data: { hid: hid },
        success: function (response) {
            if (response.code === 200) {
                const articleData = response.data.headline;
                $('#article-title').text(articleData.title);
                $('#article-type').text('分类: ' + articleData.typeName);
                $('#article-author').text('作者: ' + (articleData.publisherName || '匿名'));
                $('#article-pastHours').text('发布时间: ' + articleData.pastHours + ' 小时前');
                $('#article-views').text('浏览量: ' + articleData.pageViews);
                $('#article-content').html(articleData.article);
                if (articleData.imageUrl) {
                    $('#article-image').attr('src', articleData.imageUrl).css('display', 'block');
                } else {
                    $('#article-image').hide();
                }
                $('.article-page').hide();
                $('#article-detail').show();
                loadComments(currentArticleId);
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
                    loadArticlePage(1, 4, currentKeywords);
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
                $('#edit-modal').show();
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
                $('#edit-modal').hide();
                loadArticlePage(1, 4, currentKeywords);
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
