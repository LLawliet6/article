$(function () {
    // 加载菜单
    $.ajax({
        url: "/menu/getMenuByRole",
        headers: {
            token: localStorage.getItem("token") || ""
        },
        success: function (res) {
            if (res.code === 200) {
                renderMenu(res.data);
            } else {
                console.error("菜单加载失败", res.message);
            }
        }
    });

    // 渲染菜单
    function renderMenu(menuList) {
        let $container = $("#menu-container");
        $container.empty(); // 清空原有

        menuList.forEach(parent => {
            let $li = $(`
        <li class="layui-nav-item layui-nav-itemed">
          <a href="javascript:;">
            <i class="layui-icon ${parent.icon}"></i> ${parent.title}
          </a>
        </li>
      `);

            if (parent.children && parent.children.length > 0) {
                let $dl = $('<dl class="layui-nav-child"></dl>');

                parent.children.forEach(child => {
                    let $dd = $(`
            <dd>
              <a href="javascript:;" data-url="${child.url}">
                <i class="layui-icon ${child.icon}"></i> ${child.title}
              </a>
            </dd>
          `);

                    $dd.find("a").on("click", function () {
                        const url = $(this).data("url");
                        $("#main-content").html(`<iframe src="${url}" style="width:100%;height:100%;border:none;"></iframe>`);
                    });

                    $dl.append($dd);
                });

                $li.append($dl);
            }

            $container.append($li);
        });

        layui.use('element', function () {
            layui.element.init(); // 重新渲染
        });
    }
});
