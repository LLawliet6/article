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
                attachmentListDiv.innerHTML += `<ul style="list-style-type: none; padding: 0;">`;

                // 遍历附件数据
                attachments.forEach(attachment => {
                    const listItem = document.createElement("li");
                    listItem.style.marginBottom = "10px";

                    // 创建下载链接
                    const link = document.createElement("a");
                    link.href = `/${attachment.fileUrl}`;
                    link.download = attachment.fileName;
                    link.textContent = attachment.fileName;
                    link.style.textDecoration = "none";
                    link.style.color = "#007BFF";
                    link.style.cursor = "pointer";

                    listItem.appendChild(link);
                    attachmentListDiv.appendChild(listItem);
                });

                attachmentListDiv.innerHTML += `</ul>`;
            } else {
                console.error("加载附件失败：", data.message);
            }
        })
        .catch(err => console.error("请求附件数据出错：", err));
});