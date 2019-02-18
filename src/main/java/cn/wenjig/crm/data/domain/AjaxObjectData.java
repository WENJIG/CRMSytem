package cn.wenjig.crm.data.domain;

import cn.wenjig.crm.util.JsonUtil;

import java.io.Serializable;

public class AjaxObjectData implements Serializable {

    private String result;
    private Object data;
    private String message;
    private Page page;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
