// 评论相关操作

// 递归渲染评论
function renderComments(comments) {
    comments.sort(function (a, b) {
        return new Date(a.createTime) - new Date(b.createTime); // 按时间升序排序
    });

    let html = '<ul>';
    comments.forEach(function (comment) {
        let replyInfo = '';
        if (comment.replyUsername) {
            replyInfo = `回复 <strong>${comment.replyUsername}</strong>: `;
        }

        // 父评论，开启 <li>
        html += `
        <li>
            <div class="comment-item">
                <p><strong>${comment.username}</strong></p>
                <p>${replyInfo}${comment.content}</p>
                <small>${new Date(comment.createTime).toLocaleString()}</small>
                <button class="reply-btn" data-cid="${comment.cid}">回复</button>
        `;

        // 递归渲染子评论
        if (comment.children && comment.children.length > 0) {
            html += '<ul>'; // 子评论的容器
            html += renderComments(comment.children); // 渲染子评论
            html += '</ul>'; // 关闭子评论的容器
        }

        // 关闭父评论的 <div> 和 <li>
        html += '</div></li>';
    });
    html += '</ul>';
    return html;
}

// 加载文章评论
function loadComments(articleId) {
    $.ajax({
        url: `/comment/findCommentsByArticleId?articleId=${articleId}`,
        method: 'GET',
        success: function (response) {
            if (response.code === 200) {
                const comments = response.data;
                const $commentList = $('#comment-list');
                $commentList.empty();

                // 渲染评论列表
                const commentsHtml = renderComments(comments);
                $commentList.append(commentsHtml);
            } else {
                $('#comment-list').html('<p>没有评论。</p>');
            }
        },
        error: function () {
            $('#comment-list').html('<p>评论加载失败，请稍后重试。</p>');
        }
    });
}

// 提交评论
function submitComment(pid = null) {
    const token = localStorage.getItem('token');
    const currentUser = getCurrentUser();
    if (!token || !currentUser) {
        alert("请先登录后再发表评论。");
        return;
    }
    const commentContent = $('#comment-content').val();
    if (!commentContent.trim()) {
        alert("评论内容不能为空！");
        return;
    }
    const commentData = {
        articleId: currentArticleId,
        userId: currentUser.uid,
        content: commentContent,
        pid: pid // 父评论的ID，顶级评论时为null
    };

    $.ajax({
        url: '/comment/saveComment',
        method: 'POST',
        data: commentData,
        success: function (response) {
            if (response.code === 200) {
                alert("评论成功！");
                $('#comment-content').val('');
                loadComments(currentArticleId); // 重新加载评论
            } else {
                alert("评论失败：" + response.message);
            }
        },
        error: function () {
            alert("请求失败，请稍后重试。");
        }
    });
}
