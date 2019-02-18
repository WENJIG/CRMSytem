package cn.wenjig.crm.data.domain;

import cn.wenjig.crm.util.JsonUtil;

public class Page {

    // 当前数据对应的页码
    private int page;
    // 总的数据数目
    private int recTotal;
    // 每页数据数目
    private int recPerPage;

    public Page() {
        page = 1;
        recTotal = 100;
        recPerPage = 10;
    }

    public Page(int a, int b, int c) {
        page = a;
        recTotal = b;
        recPerPage = c;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecTotal() {
        return recTotal;
    }

    public void setRecTotal(int recTotal) {
        this.recTotal = recTotal;
    }

    public int getRecPerPage() {
        return recPerPage;
    }

    public void setRecPerPage(int recPerPage) {
        this.recPerPage = recPerPage;
    }
}
