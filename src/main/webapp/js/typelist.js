$(document).ready(function() {
    $.ajax({
        url: '/portal/findAllTypes',
        method: 'GET',
        dataType: 'json',
        success: function(response) {
            if (response.code === 200) {
                const articleTypes = response.data;
                const categoriesContainer = $('.article-categories');
                categoriesContainer.empty();
                articleTypes.forEach(function(type) {
                    let articlesHtml = '<ul>';
                    if (type.articles && type.articles.length > 0) {
                        type.articles.forEach(function(article) {
                            articlesHtml += `
                                <li>
                                    <a href="articledetail.html?hid=${article.hid}" target="_self">${article.title}</a>
                                </li>`;
                        });
                    } else {
                        articlesHtml += '<li>暂无文章</li>';
                    }
                    articlesHtml += '</ul>';
                    const categoryHtml = `
                        <div class="category-item">
                            <h4>${type.tname}</h4>
                            ${articlesHtml}
                        </div>`;
                    categoriesContainer.append(categoryHtml);
                });
            } else {
                alert('获取文章类别失败，请稍后再试。');
            }
        },
        error: function() {
            alert('获取文章类别失败，请检查网络连接或稍后再试。');
        }
    });
});
