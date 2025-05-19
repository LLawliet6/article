layui.use(['layer','jquery'], function(){
    const layer = layui.layer;
    const $ = layui.jquery;
    const token = localStorage.getItem('token');
    // let currentUser = getCurrentUser(); // 使用 getCurrentUser 方法


    // 打字机效果
    function typeWriter(text, $el, speed = 25, cb){
        $el.text('');
        let i=0, timer;
        timer = setInterval(()=>{
            $el.text($el.text() + text.charAt(i));
            i++;
            $el.parent()[0].scrollTop = $el.parent()[0].scrollHeight;
            if(i>=text.length){ clearInterval(timer); cb&&cb(); }
        }, speed);
    }

    // 显示/隐藏面板
    $('#aiChatBtn').on('click', ()=>{
        const pane = $('#aiChatPane');
        if (pane.is(':visible')) {
            pane.hide();
        } else {
            pane.css('display', 'flex');  // 👈 动态设置为 flex
            $('#chatInput').focus();
        }
    });
    $('#aiChatClose').on('click', ()=> $('#aiChatPane').hide());

    // 发送消息
    $('#chatSend').on('click', ()=>{
        const msg = $('#chatInput').val().trim();
        if(!msg) return;
        // 用户消息
        $('#chatMessages').append(`
      <div style="text-align:right; margin:6px 0;">
        <span style="background:#d0f0fd;padding:6px 10px;border-radius:8px;display:inline-block;">
          ${msg}
        </span>
      </div>
    `);
        $('#chatInput').val('');
        $('#chatMessages')[0].scrollTop = $('#chatMessages')[0].scrollHeight;
        // AI 占位
        $('#chatMessages').append(`
      <div style="margin:6px 0;position:relative;">
        <pre class="aiTyping" 
             style="background:#f0f0f0;padding:6px 10px;border-radius:8px;white-space:pre-wrap;">
        </pre>
        <button class="copyBtn layui-btn layui-btn-xs"
                style="position:absolute;top:4px;right:4px;display:none;">
          复制
        </button>
      </div>
    `);
        const $typing = $('.aiTyping').last();

        // 请求后端
        fetch('/api/ai/chat', {
            method:'POST',
            headers:{'Content-Type':'application/json', 'token': token },
            body: JSON.stringify({message: msg})
        })
            .then(r=>r.json())
            .then(json=>{
                if(json.code!==200) return layer.msg('AI 错误：'+json.msg);
                const answer = json.data;
                typeWriter(answer, $typing, 25, ()=>{
                    // 渲染 Markdown
                    const html = marked.parse(answer);
                    $typing.replaceWith(
                        `<div class="aiContent" style="background:#f0f0f0;padding:6px 10px;border-radius:8px;">${html}</div>`
                    );
                    // 复制功能
                    const $btn = $('.copyBtn').last().show();
                    $btn.on('click', ()=>{
                        navigator.clipboard.writeText(answer)
                            .then(()=> layer.msg('已复制'))
                            .catch(()=> layer.msg('复制失败'));
                    });
                });
            })
            .catch(e=>{
                console.error(e);
                layer.msg('调用 AI 失败');
            });
    });

    // 回车发送
    $('#chatInput').on('keypress', e=>{ if(e.key==='Enter'){ $('#chatSend').click(); e.preventDefault(); } });
});