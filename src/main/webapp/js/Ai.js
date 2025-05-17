layui.use(['layer','jquery'], function(){
    const layer = layui.layer;
    const $ = layui.jquery;
    let chatIndex = null;

    // 打字机效果（写入纯文本）
    function typeWriter(text, $textEl, speed = 25, callback) {
        $textEl.text('');
        let i = 0;
        const timer = setInterval(() => {
            $textEl.text($textEl.text() + text.charAt(i));
            i++;
            const cm = $('#chatMessages')[0];
            cm.scrollTop = cm.scrollHeight;
            if (i >= text.length) {
                clearInterval(timer);
                callback && callback();
            }
        }, speed);
    }

    // 浮动按钮点击：开/关聊天窗口
    $('#aiChatBtn').on('click', function(){
        if (chatIndex !== null) {
            layer.close(chatIndex); chatIndex = null;
        } else {
            chatIndex = layer.open({
                type: 1,
                title: 'AI 助手',
                area: ['650px','850px'],
                offset: 'rb',
                shade: 0.3,
                content: `
          <div style="display:flex; flex-direction:column; height:100%;">
            <div id="chatMessages"
                 style="flex:1; overflow-y:auto; padding:10px; background:#fafafa; border-bottom:1px solid #eee;">
            </div>
            <div style="padding:10px; display:flex; border-top:1px solid #eee;">
              <input type="text" id="chatInput"
                     placeholder="说点什么..."
                     style="flex:1; padding:8px; border:1px solid #ccc; border-radius:4px;" />
              <button id="chatSend"
                      class="layui-btn layui-btn-sm layui-btn-normal"
                      style="margin-left:8px; height:50px;">发送</button>
            </div>
          </div>
        `,
                success(layero){
                    layero.find('#chatInput').focus();
                },
                end(){ chatIndex = null; }
            });
        }
    });

    // 发送消息
    $(document).on('click','#chatSend', function(){
        const msg = $('#chatInput').val().trim();
        if (!msg) return;

        // 用户消息
        $('#chatMessages').append(`
      <div style="text-align:right; margin:8px 0;">
        <span style="display:inline-block; background:#d0f0fd; padding:8px 12px; border-radius:10px;">
          ${msg}
        </span>
      </div>
    `);
        $('#chatInput').val('');
        $('#chatMessages')[0].scrollTop = $('#chatMessages')[0].scrollHeight;

        // AI 占位：容器里先放一个 <pre> 显示纯文本，一键复制按钮默认隐藏
        $('#chatMessages').append(`
      <div style="position:relative; text-align:left; margin:8px 0;">
        <pre id="aiTyping" 
             style="display:inline-block; background:#f0f0f0; padding:8px 12px; border-radius:10px; min-width:50px; white-space:pre-wrap;">
        </pre>
        <button class="copyBtn layui-btn layui-btn-xs"
                style="position:absolute; top:4px; right:4px; display:none;">
          复制
        </button>
      </div>
    `);

        // 调用后端 AI
        fetch('/api/ai/chat', {
            method:'POST',
            headers:{ 'Content-Type':'application/json' },
            body: JSON.stringify({ message: msg })
        })
            .then(r => r.json())
            .then(json => {
                if (json.code !== 200) {
                    return layer.msg('AI 错误：'+json.msg);
                }
                const answer = json.data;

                // 打字机效果输出到 <pre#aiTyping>
                const $typing = $('#aiTyping');
                typeWriter(answer, $typing, 25, () => {
                    // 渲染 Markdown
                    const html = marked.parse(answer);
                    $typing.replaceWith(`<div class="ai-content" style="padding:8px 12px; background:#f0f0f0; border-radius:10px;">${html}</div>`);
                    // 显示复制按钮，并绑定复制完整 Markdown 文本
                    const $copy = $('.copyBtn').last().show();
                    $copy.on('click', () => {
                        navigator.clipboard.writeText(answer)
                            .then(() => layer.msg('已复制到剪贴板'))
                            .catch(() => layer.msg('复制失败'));
                    });
                    // 滚到底部
                    $('#chatMessages')[0].scrollTop = $('#chatMessages')[0].scrollHeight;
                });
            })
            .catch(err => {
                console.error(err);
                layer.msg('调用 AI 失败');
                $('#aiTyping').text('[调用失败]');
            });
    });

    // 回车发送
    $(document).on('keydown','#chatInput', function(e){
        if (e.key === 'Enter') {
            $('#chatSend').click();
            e.preventDefault();
        }
    });
});