// layui.use(['layer', 'jquery'], function () {
//     const layer = layui.layer;
//     const $ = layui.jquery;
//     const token = localStorage.getItem('token');
//     // 打字机效果
//     function typeWriter(text, $el, speed = 25, cb) {
//         $el.text('');
//         let i = 0, timer;
//         timer = setInterval(() => {
//             $el.text($el.text() + text.charAt(i));
//             i++;
//             $el.parent()[0].scrollTop = $el.parent()[0].scrollHeight;
//             if (i >= text.length) {
//                 clearInterval(timer);
//                 cb && cb();
//             }
//         }, speed);
//     }
//
//     // 显示/隐藏面板
//     $('#aiChatBtn').on('click', () => {
//         const pane = $('#aiChatPane');
//         if (pane.is(':visible')) {
//             pane.hide();
//         } else {
//             pane.css('display', 'flex');  // 👈 动态设置为 flex
//             $('#chatInput').focus();
//         }
//     });
//     $('#aiChatClose').on('click', () => $('#aiChatPane').hide());
//
//     $('#chatSend').on('click', () => {
//         const msg = $('#chatInput').val().trim();
//         if (!msg) return;
//         // 用户消息
//         $('#chatMessages').append(`<div style="text-align:right; margin:6px 0;">
//         <span style="background:#d0f0fd;padding:6px 10px;border-radius:8px;display:inline-block;">${msg}</span>
//       </div>`);
//         $('#chatInput').val('');
//         // AI 占位
//         $('#chatMessages').append(`<div style="margin:6px 0;position:relative;">
//         <pre class="aiTyping" style="background:#f0f0f0;padding:6px 10px;border-radius:8px;white-space:pre-wrap;color:#999;"><em>正在思考...</em></pre>
//         <button class="copyBtn layui-btn layui-btn-xs" style="position:absolute;top:4px;right:4px;display:none;">复制</button>
//       </div>`);
//         const $typing = $('.aiTyping').last();
//         const $responseBlock = $typing.parent(); // 获取整个响应区块
//         // 请求后端
//         fetch('/api/ai/chat', {
//             method: 'POST',
//             headers: {'Content-Type': 'application/json', 'token': token},
//             body: JSON.stringify({message: msg})
//         })
//             .then(r => r.json())
//             .then(json => {
//                 if (json.code !== 200) return layer.msg('AI 错误：' + json.msg);
//                 const answer = json.data;
//                 // 填入 Quill 编辑器
//                 quill.setText(answer);
//                 typeWriter(answer, $typing, 25, () => {
//                     // 渲染 Markdown
//                     const html = marked.parse(answer);
//                     $typing.replaceWith(
//                         `<div class="aiContent" style="background:#f0f0f0;padding:6px 10px;border-radius:8px;">${html}</div>`
//                     );
//                     // // 复制功能
//                     // const $btn = $('.copyBtn').last().show();
//                     // 显示复制按钮
//                     $responseBlock.find('.copyBtn').show().on('click', () => {
//                         navigator.clipboard.writeText(answer)
//                             .then(() => layer.msg('已复制'))
//                             .catch(() => layer.msg('复制失败'));
//                     });
//                 });
//             })
//             .catch(e => {
//                 console.error(e);
//                 layer.msg('调用 AI 失败');
//             });
//     });
//     // 发送图片请求
//     $('#chatImageSend').on('click', () => {
//         const prompt = $('#chatInput').val().trim();
//         if (!prompt) return layer.msg('请输入图片描述');
//         $('#chatInput').val('');
//         // 占位
//         $('#chatMessages').append(`
//       <div style="margin:6px 0; position:relative;">
//         <div class="aiImageTyping" style="background:#f0f0f0;padding:10px;border-radius:8px;color:#999;">
//           <em>图片生成中...</em>
//         </div>
//       </div>
//     `);
//         const $placeholder = $('.aiImageTyping').last().parent();
//
//         fetch('/api/ai/image', {
//             method: 'POST',
//             headers: {'Content-Type': 'application/json', 'token': token},
//             body: JSON.stringify({prompt})
//         })
//             .then(r => r.json()).then(json => {
//             if (json.code !== 200) return layer.msg('图片生成失败：' + json.msg);
//             const urls = json.data; // 数组
//             let imgs = urls.map((u, idx) => `
//   <div style="margin-top:5px;">
//     <img src="${u}"
//          style="max-width:100%;border-radius:6px; display:block;" />
//     <div style="margin-top:4px;">
//       <a href="${u}" download
//          class="layui-btn layui-btn-xs">下载图片</a>
//       <button class="layui-btn layui-btn-xs fillBtn"
//               data-url="${u}"
//               style="margin-left:4px;">填充</button>
//     </div>
//   </div>
// `).join('');
//             $placeholder.html(imgs);
//             $placeholder[0].scrollTop = $placeholder[0].scrollHeight;
//         })
//             .catch(e => {
//                 console.error(e);
//                 layer.msg('调用图片接口失败');
//                 $placeholder.find('.aiImageTyping').text('[生成失败]');
//             });
//     });
//     // 现在可以正确绑定图片按钮事件
//     $('#chatImageSend').on('click', () => {
//         console.log('图片按钮被点击了'); // 调试用
//         // …你的图片生成逻辑…
//     });
//     // 回车发送
//     $('#chatInput').on('keypress', e => {
//         if (e.key === 'Enter') {
//             $('#chatSend').click();
//             e.preventDefault();
//         }
//     });
// });
// 点击“填充”按钮时，把图片下载成 File 并填入 #image


layui.use(['layer', 'jquery'], function () {
    const layer = layui.layer;
    const $ = layui.jquery;
    const token = localStorage.getItem('token');


    // 打字机效果
    function typeWriter(text, $el, speed = 25, cb) {
        $el.text('');
        let i = 0, timer = setInterval(() => {
            $el.text($el.text() + text.charAt(i++));
            $el.parent()[0].scrollTop = $el.parent()[0].scrollHeight;
            if (i >= text.length) {
                clearInterval(timer);
                cb && cb();
            }
        }, speed);
    }

    // 显示/隐藏聊天面板
    $('#aiChatBtn').on('click', () => {
        const pane = $('#aiChatPane');
        if (pane.is(':visible')) {
            pane.hide();
        } else {
            pane.css('display', 'flex');
            $('#chatInput').focus();
        }
    });
    $('#aiChatClose').on('click', () => $('#aiChatPane').hide());

    // 发送纯文本对话
    $('#chatSend').on('click', () => {
        const msg = $('#chatInput').val().trim();
        if (!msg) return;
        appendUserMessage(msg);
        appendTypingPlaceholder();
        $('#chatInput').val('');

        fetch('/api/ai/chat', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'token': token},
            body: JSON.stringify({message: msg})
        })
            .then(r => r.json())
            .then(json => {
                if (json.code !== 200) return layer.msg('AI 错误：' + json.msg);
                const answer = json.data;
                const $typing = $('.aiTyping').last();
                const $block = $typing.parent();
                typeWriter(answer, $typing, 25, () => {
                    const html = marked.parse(answer);
                    $typing.replaceWith(`<div class="aiContent" style="background:#f0f0f0;padding:6px 10px;border-radius:8px;">${html}</div>`);
                    $block.find('.copyBtn').show().off('click').on('click', () => {
                        navigator.clipboard.writeText(answer).then(() => layer.msg('已复制')).catch(() => layer.msg('复制失败'));
                    });
                });
            })
            .catch(e => {
                console.error(e);
                layer.msg('调用 AI 失败');
            });
    });

    // 发送图片生成请求
    $('#chatImageSend').on('click', () => {
        const prompt = $('#chatInput').val().trim();
        if (!prompt) return layer.msg('请输入图片描述');
        appendUserMessage(prompt);
        $('#chatInput').val('');
        appendImagePlaceholder();

        const $block = $('.aiImageTyping').last().parent();
        fetch('/api/ai/image', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'token': token},
            body: JSON.stringify({prompt})
        })
            .then(r => r.json())
            .then(json => {
                if (json.code !== 200) return layer.msg('图片生成失败：' + json.msg);
                const urls = json.data;
                let html = urls.map(u => `
        <div style="margin-top:5px;">
          <img src="${u}" style="max-width:100%;border-radius:6px;display:block;" />
          <div style="margin-top:4px;">
            <a href="${u}" download class="layui-btn layui-btn-xs">下载图片</a>
            <button class="layui-btn layui-btn-xs fillBtn" data-url="${u}" style="margin-left:4px;">填充</button>
          </div>
        </div>
      `).join('');
                $block.html(html);
                $block[0].scrollTop = $block[0].scrollHeight;
            })
            .catch(e => {
                console.error(e);
                layer.msg('调用图片接口失败');
                $block.find('.aiImageTyping').text('[生成失败]');
            });
    });

    // “填充”按钮：把图片自动填入上传控件
    $(document).on('click', '.fillBtn', async function () {
        const url = $(this).data('url');
        try {
            const resp = await fetch(url);
            const blob = await resp.blob();
            const ext = blob.type.split('/')[1] || 'png';
            const file = new File([blob], `ai-image.${ext}`, {type: blob.type});
            const dt = new DataTransfer();
            dt.items.add(file);
            document.getElementById('image').files = dt.files;
            layer.msg('已填充图片到上传控件');
        } catch {
            layer.msg('填充失败');
        }
    });

    // “AI 生成内容”按钮：根据标题生成文章正文
    $('#aiFillContent').on('click', () => {
        const title = $('#title').val().trim();
        if (!title) return layer.msg('请先填写标题');
        const btn = $('#aiFillContent').prop('disabled', true).text('生成中…');
        fetch('/api/ai/chat', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'token': token},
            body: JSON.stringify({message: '请根据以下标题生成一段文章正文：' + title})
        })
            .then(r => r.json())
            .then(json => {
                if (json.code !== 200) throw new Error(json.msg || 'AI失败');
                quill.setText(json.data);
            })
            .catch(err => {
                layer.msg('生成失败：' + err.message)
            })
            .finally(() => btn.prop('disabled', false).text('AI 生成内容'));
    });

    // 按回车发送文本
    $('#chatInput').on('keypress', e => {
        if (e.key === 'Enter') {
            $('#chatSend').click();
            e.preventDefault();
        }
    });

    // 辅助函数
    function appendUserMessage(msg) {
        $('#chatMessages').append(`
      <div style="text-align:right;margin:6px 0;">
        <span style="background:#d0f0fd;padding:6px 10px;border-radius:8px;display:inline-block;">
          ${msg}
        </span>
      </div>`);
        $('#chatMessages')[0].scrollTop = $('#chatMessages')[0].scrollHeight;
    }

    function appendTypingPlaceholder() {
        $('#chatMessages').append(`
      <div style="margin:6px 0;position:relative;">
        <pre class="aiTyping" style="background:#f0f0f0;padding:6px 10px;border-radius:8px;white-space:pre-wrap;color:#999;">
          <em>正在思考...</em>
        </pre>
        <button class="copyBtn layui-btn layui-btn-xs" style="position:absolute;top:4px;right:4px;display:none;">复制</button>
      </div>`);
    }

    function appendImagePlaceholder() {
        $('#chatMessages').append(`
      <div style="margin:6px 0;position:relative;">
        <div class="aiImageTyping" style="background:#f0f0f0;padding:10px;border-radius:8px;color:#999;">
          <em>图片生成中...</em>
        </div>
      </div>`);
    }
});


$(document).on('click', '.fillBtn', async function () {
    const url = $(this).data('url');
    try {
        // 1. 下载图片为 Blob
        const resp = await fetch(url);
        const blob = await resp.blob();
        // 2. 构造一个 File 对象（这里用当前时间和后缀）
        const ext = blob.type.split('/')[1] || 'png';
        const file = new File([blob], `ai-image.${ext}`, {type: blob.type});
        // 3. 用 DataTransfer 填充到 input[type=file]
        const dt = new DataTransfer();
        dt.items.add(file);
        const input = document.getElementById('image'); // 你的上传控件 ID
        input.files = dt.files;
        layer.msg('已填充生成图片到上传图片控件');
    } catch (e) {
        console.error(e);
        layer.msg('填充失败');
    }
});
