// 分页功能
function generatePagination(currentPage, totalPages) {
    const $pagination = $('#pagination');
    $pagination.empty();
    if (totalPages <= 1) return;

    for (let i = 1; i <= totalPages; i++) {
        $pagination.append(`<button class="page-btn" data-page="${i}">${i}</button>`);
    }

    // 高亮当前页
    $pagination.find(`[data-page="${currentPage}"]`).addClass('active');

    // 点击事件
    $pagination.find('.page-btn').click(function () {
        const page = $(this).data('page');
        loadArticlePage(page, 4, currentKeywords);
    });
}
