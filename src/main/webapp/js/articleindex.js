document.addEventListener('DOMContentLoaded', function () {
    // 发送AJAX请求获取文章列表
    fetchArticles();
});

// 获取文章列表函数
function fetchArticles() {
    const url = '/portal/findArticlePage';
    // 请求的参数页码
    const requestData = {
        pageNum: 1,
        pageSize: 8
    };

    // 发送AJAX请求
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => response.json())
        .then(data => {
            const pageInfo = data.data.pageInfo;
            const articlesContainer = document.getElementById('articles-container');
            // 清空容器
            articlesContainer.innerHTML = '';
            // 遍历返回的文章数据并将其动态添加到页面
            pageInfo.pageData.forEach(article => {
                const articleElement = document.createElement('div');
                articleElement.classList.add('col-lg-12', 'col-md-12', 'article-item');
                articleElement.innerHTML = `
                <div class="article-title">
                     <h5><a href="articledetail.html?hid=${article.hid}">${article.title}</a></h5>
                    <p class="meta">浏览量: ${article.pageViews} | 发布: ${article.pastHours}小时前</p>
                </div>
            `;
                // 将文章元素添加到容器中
                articlesContainer.appendChild(articleElement);
            });
        })
        .catch(error => {
            console.error('Error fetching articles:', error);
        });
}
