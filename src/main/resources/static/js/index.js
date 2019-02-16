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
