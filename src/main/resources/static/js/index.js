var isInitPager = false;

// 废案 暂留
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

// 关于
function about() {
    new $.zui.Messager('1581900362', {
        icon: 'qq'
    }).show();
}

// 错误信息提示
function errorMessage(status, thrown) {
    switch (status) {
        case 400 : new $.zui.Messager("服务器拒绝了你的访问:参数错误", {
            type: "danger",
            icon: 'frown'
        }).show();
            break;
        case 403 : new $.zui.Messager("服务器拒绝了你的访问:权限不足", {
            type: "danger",
            icon: 'frown'
        }).show();
            break;
        case 404 : new $.zui.Messager("服务器拒绝了你的访问:无此资源", {
            type: "danger",
            icon: 'frown'
        }).show();
            break;
        default : new $.zui.Messager("服务器拒绝了你的访问:" + textStatus, {
            type: "danger",
            icon: 'frown'
        }).show();
            break;
    }
}

/**
 * 从服务器内存中读取日志（还未被添加至数据库时）
 */
function readLogByServerRAM() {
    disNonePaper();
    $.ajax({
        type: 'POST',
        url: "/log/readByRam",
        data: {},
        dataType: "text",
        success: function (data, textStatus) {
            var dataArray = $.parseJSON(data);
            $("#inc-su-admin-context-data").html("" +
                "<div id='inc-su-admin-context-data'>" +
                "<div id='datagridExample' class='datagrid'>" +
                "<header class=\"clearfix\">\n" +
                "      <div class=\"input-control search-box search-box-circle has-icon-left has-icon-right pull-right\" id=\"searchboxExample1\" style=\"width: 600px;\">" +
                "        <input id=\"inputSearchExample1\" type=\"search\" class=\"form-control search-input\" placeholder=\"搜索\">" +
                "        <label for=\"inputSearchExample1\" class=\"input-control-icon-left search-icon\"><i class=\"icon icon-search\"></i></label>" +
                "        <a href=\"#\" class=\"input-control-icon-right search-clear-btn\"><i class=\"icon icon-remove\"></i></a>" +
                "      </div>\n" +
                "      <h3>日志信息</h3>\n" +
                "</header>" +
                "</div>" +
                "</div>");
            $('#datagridExample').datagrid({
                dataSource: {
                    cols:[
                        {name: 'args', label: '参数', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'description', label: '说明', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'employeeId', label: '操作员工ID', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'endTime', label: '结束时间', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'id', label: '日志ID', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'level', label: '日志等级', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'method', label: '调用方法', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'operating_type', label: '类型', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'returnValue', label: '返回值', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'runTime', label: '运行时间/ms', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'startTime', label: '开始时间', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}}
                    ],
                    array: dataArray
                },
                // 禁用勾选
                checkable: false,
                // 开启排序
                sortable: true
        });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}

/**
 * 从服务器数据库中读取日志
 */
function readLogByServerDB(startPage, capacity) {
    var thisStartPage = startPage;
    var thisCapacity = capacity;
    $.ajax({
        type: 'POST',
        url: "/log/readByDB",
        data: {
            start: thisStartPage,
            capacity: thisCapacity
        },
        dataType: "text",
        success: function (data, textStatus) {
            var dataArray = $.parseJSON(data);
            $("#inc-su-admin-context-data").html("" +
                "<div id='inc-su-admin-context-data'>" +
                "<div id='datagridExample' class='datagrid'>" +
                "<header class=\"clearfix\">\n" +
                "      <div class=\"input-control search-box search-box-circle has-icon-left has-icon-right pull-right\" id=\"searchboxExample1\" style=\"width: 600px;\">" +
                "        <input id=\"inputSearchExample1\" type=\"search\" class=\"form-control search-input\" placeholder=\"搜索\">" +
                "        <label for=\"inputSearchExample1\" class=\"input-control-icon-left search-icon\"><i class=\"icon icon-search\"></i></label>" +
                "        <a href=\"#\" class=\"input-control-icon-right search-clear-btn\"><i class=\"icon icon-remove\"></i></a>" +
                "      </div>\n" +
                "      <h3>日志信息</h3>\n" +
                "</header>");
            var count = dataArray.count;
            $('#myPager').css("display","inline-block");
            $('#myPager').data('zui.pager').set({
                page: startPage,
                recTotal: count,
                recPerPage: capacity
            });
            $('#datagridExample').datagrid({
                dataSource: {
                    cols:[
                        {name: 'args', label: '参数', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'description', label: '说明', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'employeeId', label: '操作员工ID', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'endTime', label: '结束时间', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'id', label: '日志ID', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'level', label: '日志等级', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'method', label: '调用方法', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'operating_type', label: '类型', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'returnValue', label: '返回值', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'runTime', label: '运行时间/ms', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'startTime', label: '开始时间', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}}
                    ],
                    array: dataArray.logInfos
                },
                // 禁用勾选
                checkable: false,
                // 开启排序
                sortable: true
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}
/**
 * 初始化分页器
 */

function paperInit() {
    $('#myPager').pager({
        page: 1,
        recTotal: 10,
        recPerPage: 10,
        maxNavCount: 10,
        onPageChange: function (state, oldPage) {
            if (state.page !== oldPage) {
                onPageChange(state.page, state.recPerPage);
            }
        },
        onRender: null,
    });
}

/**
 * 使分页器不可见
 */
function disNonePaper() {
    $('#myPager').css("display","none");
}
/**
 * 当分页器页码变更时
 * @param Statu
 * @param recPerPage
 */
function onPageChange(page, capacity, recTotal) {
    readLogByServerDB(page, capacity, recTotal);
}

/*
 * 查看所有的员工
 */
function findAllEmp() {
    disNonePaper();
    $.ajax({
        type: 'POST',
        url: "/emp/findAll",
        data: {},
        dataType: "text",
        success: function (data, textStatus) {
            var dataArray = $.parseJSON(data);
            $("#inc-su-admin-context-data").html("" +
                "<div id='inc-su-admin-context-data'>" +
                "<div id='datagridExample' class='datagrid'>" +
                "<header class=\"clearfix\">\n" +
                "      <div class=\"input-control search-box search-box-circle has-icon-left has-icon-right pull-right\" id=\"searchboxExample1\" style=\"width: 600px;\">" +
                "        <input id=\"inputSearchExample1\" type=\"search\" class=\"form-control search-input\" placeholder=\"搜索\">" +
                "        <label for=\"inputSearchExample1\" class=\"input-control-icon-left search-icon\"><i class=\"icon icon-search\"></i></label>" +
                "        <a href=\"#\" class=\"input-control-icon-right search-clear-btn\"><i class=\"icon icon-remove\"></i></a>" +
                "      </div>\n" +
                "      <h3>员工信息</h3>\n" +
                "</header>" +
                "</div>" +
                "</div>");
            $('#datagridExample').datagrid({
                dataSource: {
                    cols:[
                        {name: 'email', label: '账号', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'id', label: '员工ID', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'nickname', label: '昵称', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'officeTel', label: '办公电话', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'password', label: '密码', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'phoneNo', label: '电话', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'realname', label: '真实姓名', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}},
                        {name: 'workStatus', label: '是否在职', width: 150, style: {"overflow-x":"scroll", "white-space": "nowrap", "text-overflow": "clip"}}
                    ],
                    array: dataArray
                },
                // 启用勾选
                checkable: true,
                // true,点击行中任意部分将勾选此行, false必须点击勾选框
                checkByClickRow: false,
                // 开启排序
                sortable: true
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}

// 添加一位新员工
function addEmp() {
    var account = $("#inc-su-admin-emp-add-account").val();
    var password = $("#inc-su-admin-emp-add-pwd").val();
    var nickname = $("#inc-su-admin-emp-add-nick").val();
    var realname = $("#inc-su-admin-emp-add-real").val();
    var phoneNo = $("#inc-su-admin-emp-add-phone").val();
    var officeTel = $("#inc-su-admin-emp-add-officeTel").val();
    $.ajax({
        type: 'POST',
        url: "/emp/addOne",
        data: {
            account: account,
            password: password,
            nickname: nickname,
            realname: realname,
            phoneNo: phoneNo,
            officeTel: officeTel
        },
        dataType: "text",
        success: function (data, textStatus) {
            new $.zui.Messager(data, {
                icon: 'check',
                type: "success"
            }).show();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}

// 立即将内存中的日志写入数据库
function writeLogNowToDB() {
    $.ajax({
        type: 'POST',
        url: "/log/writeNowToDB",
        data: {},
        dataType: "text",
        success: function (data, textStatus) {
            new $.zui.Messager(data, {
                icon: 'check',
                type: "success"
            }).show();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}

/**
 * 获取新闻热点
 */
function getNews() {
    $.ajax({
        type: 'POST',
        url: "/news/",
        data: {},
        dataType: "text",
        success: function (data, textStatus) {
            if (data === "获取数据失败！") {
                new $.zui.Messager(data, {
                    icon: 'frown',
                    type: "danger"
                }).show();
                return;
            }
            var dataArray = $.parseJSON(data);
            var text = "<div>";
            for (var i = 0; i < dataArray.length; i++) {
                text += "<a class='btn btn-link' target='_blank' style='font-size: 28px' href='" + dataArray[i].url + "'>" + dataArray[i].title + "</a></br>";
            }
            text += "</div>";
            $("#news-text").html(text);
            $("#modal-news").modal();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            errorMessage(XMLHttpRequest.status, errorThrown);
        }
    });
}