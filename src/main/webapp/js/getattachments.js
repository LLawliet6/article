document.addEventListener("DOMContentLoaded", function () {
    // 获取文章ID
    const urlParams = new URLSearchParams(window.location.search);
    const hid = urlParams.get("hid");

    // 检查hid是否存在
    if (!hid) {
        console.error("未找到文章 ID");
        return;
    }

    // 通过hid查询附件信息
    fetch(`/portal/getAttachments?hid=${hid}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const attachments = data.data;
                const attachmentListDiv = document.getElementById("attachment-list");

                // 清空附件列表
                attachmentListDiv.innerHTML = "<ul style='list-style-type: none; padding: 0;' id='attachment-ul'></ul>";

                const ul = document.getElementById("attachment-ul");

                // 遍历附件数据
                attachments.forEach(attachment => {
                    console.log(attachment);
                    const listItem = document.createElement("li");
                    listItem.style.marginBottom = "10px";
                    listItem.setAttribute("id", `attachment-${attachment.aid}`);

                    // 创建文件名下载链接
                    const link = document.createElement("a");
                    link.href = `/${attachment.fileUrl}`;
                    link.download = attachment.fileName;
                    link.textContent = attachment.fileName;
                    link.style.textDecoration = "none";
                    link.style.color = "#007BFF";
                    link.style.cursor = "pointer";
                    link.target = "_blank";

                    // 创建 "预览" 按钮
                    const previewBtn = document.createElement("button");
                    previewBtn.textContent = "预览";
                    previewBtn.style.marginLeft = "10px";
                    previewBtn.style.cursor = "pointer";
                    previewBtn.onclick = function () {
                        previewFile(attachment.fileUrl, attachment.fileName);
                    };

                    // 创建 "删除" 按钮
                    const deleteBtn = document.createElement("button");
                    deleteBtn.textContent = "删除";
                    deleteBtn.style.marginLeft = "10px";
                    deleteBtn.style.color = "red";
                    deleteBtn.style.cursor = "pointer";
                    deleteBtn.onclick = function () {
                        deleteAttachment(attachment.aid);
                    };

                    // 追加元素
                    listItem.appendChild(link);
                    listItem.appendChild(previewBtn);
                    listItem.appendChild(deleteBtn);
                    ul.appendChild(listItem);
                });
            } else {
                console.error("加载附件失败：", data.message);
            }
        })
        .catch(err => console.error("请求附件数据出错：", err));

});
// 文件预览
function previewFile(fileUrl, fileName) {
    const fileExt = fileName.split('.').pop().toLowerCase();
    const imageFormats = ['jpg', 'jpeg', 'png', 'gif'];
    const pdfFormats = ['pdf'];
    const textFormats = ['txt'];
    if (imageFormats.includes(fileExt)) {
        // 预览图片
        const imgPreview = `<div style="text-align:center;"><img src="/${fileUrl}" style="max-width:100%; max-height:500px;"></div>`;
        openPreviewWindow(imgPreview, fileName);
    } else if (pdfFormats.includes(fileExt)) {
        // 预览 PDF
        window.open(`/${fileUrl}`, '_blank');
    } else if (textFormats.includes(fileExt)) {
        // 预览 txt 文件
        fetch(`/${fileUrl}`)
            .then(response => response.text())
            .then(textContent => {
                const textPreview = `<div style="text-align:left; white-space: pre-wrap; word-wrap: break-word; max-width: 100%; max-height: 500px;">${textContent}</div>`;
                openPreviewWindow(textPreview, fileName);
            })
            .catch(err => alert("无法加载文件内容：" + err));
    } else {
        alert("该文件类型不支持在线预览，请下载后查看。");
    }
}

// 打开预览窗口
function openPreviewWindow(content, title) {
    const previewWin = window.open("", "_blank", "width=800,height=600");
    previewWin.document.write(`
        <html>
            <head><title>${title}</title><link rel="stylesheet" href="css/layui.css"></head>
            <body><div class="layui-container" style="margin-top: 20px;">${content}</div></body>
        </html>
    `);
    previewWin.document.close();
}

// 删除附件
function deleteAttachment(attachmentId) {
    layui.layer.confirm('确定要删除这个附件吗？', {
        btn: ['确认', '取消']
    }, function () {
        fetch(`/portal/deleteAttachment?aid=${attachmentId}`, {
            method: "DELETE",
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    // 删除成功，从页面移除
                    const item = document.getElementById(`attachment-${attachmentId}`);
                    if (item) item.remove();
                    layui.layer.msg("附件已成功删除！");
                } else {
                    layui.layer.msg("删除失败：" + data.message);
                }
            })
            .catch(err => console.error("删除附件出错：", err));
    });
}