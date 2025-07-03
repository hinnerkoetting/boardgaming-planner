package de.oetting.wwp.infrastructure;

public class RestProblem {
    private String type;
    private String title;
    private String detail;
    private String instance;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
