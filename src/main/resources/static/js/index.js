function show(idn) {
    var id = "workContent" + idn;
    var cls = document.getElementsByClassName("includeWork");

    for (var i = 0; i < cls.length; i++) {
        var divs = cls[i].getElementsByTagName("div");
        for (var j = 0; j < divs.length; j++) {
            divs[j].style.display = "none";
            if (id == divs[j].id) {
                document.getElementById(id).style.display = "block";
            }
        }
    }

}

function about() {
    new $.zui.Messager('1581900362', {
        icon: 'qq'
    }).show();
}

/**
 * 从服务器内存中读取日志（还未被添加至数据库时）
 */
function readLogByServerRAM() {
    $.ajax({
        type: 'POST',
        url: "/log/readByRam",
        data: null,
        dataType: "text",
        success: function (data, textStatus) {
            $("#inc-su-admin-context-data").html("<div id='inc-su-admin-context-data'><div id='datagridExample' class='datagrid'></div></div>");
            $('#datagridExample').datagrid({
                dataSource: {
                    cols:[
                        {name: 'time', label: '时间', width: 132},
                        {name: 'hera', label: '英雄', width: 134},
                        {name: 'action', label: '动作', width: 109},
                        {name: 'target', label: '目标', width: 109},
                        {name: 'desc', label: '描述', width: 287}
                    ],
                    array:[
                        {time: '00:11:12', hero:'幻影刺客', action: '击杀', target: '斧王', desc: '幻影刺客击杀了斧王。'},
                        {time: '00:13:22', hero:'幻影刺客', action: '购买了', target: '隐刀', desc: '幻影刺客购买了隐刀。'},
                        {time: '00:19:36', hero:'斧王', action: '购买了', target: '黑皇杖', desc: '斧王购买了黑皇杖。'},
                        {time: '00:21:43', hero:'力丸', action: '购买了', target: '隐刀', desc: '力丸购买了隐刀。'}
                    ]
                },
        });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            switch (textStatus) {
                case 403 : new $.zui.Messager("服务器拒绝了你的访问:权限不足", {
                    icon: 'frown'
                }).show();
                break;
                default : new $.zui.Messager("服务器拒绝了你的访问:" + textStatus, {
                    icon: 'frown'
                }).show();
                break;
            }
            $("#exp-text").text(errorThrown);
        }
    });
}
