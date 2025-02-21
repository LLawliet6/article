// 全局事件绑定

// 搜索按钮事件
$('#search-btn').click(function () {
    currentKeywords = $('#search-input').val();
    loadArticlePage(1, 4, currentKeywords);
});

// 搜索输入框按回车事件
$('#search-input').keypress(function (event) {
    if (event.which === 13) {
        currentKeywords = $('#search-input').val();
        loadArticlePage(1, 4, currentKeywords);
    }
});

// 评论提交按钮事件
$('#submit-comment-btn').click(function () {
    submitComment();
});

// 返回文章列表按钮事件
$('#back-btn').click(function () {
    $('#article-detail').hide();
    $('.article-page').show();
    $(window).scrollTop(0);
});

// 回复按钮点击事件
$(document).on('click', '.reply-btn', function () {
    const pid = $(this).data('cid'); // 获取被回复评论的ID
    const replyContent = prompt("请输入回复内容:");
    if (replyContent) {
        $('#comment-content').val(replyContent); // 把回复内容放入输入框
        submitComment(pid); // 提交回复，带上父评论ID
    }
});

// 初始化加载文章列表
loadArticlePage(1, 4);

// typelist.html里的阅读全文
const articleHid = getQueryParameter('hid');
if (articleHid) {
    // 如果 URL 中包含 hid，直接加载对应文章详情
    loadArticleDetail(articleHid);
} else {
    // 如果没有 hid，加载文章列表
    loadArticlePage(1, 10);
}

// 获取 URL 查询参数
function getQueryParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}
