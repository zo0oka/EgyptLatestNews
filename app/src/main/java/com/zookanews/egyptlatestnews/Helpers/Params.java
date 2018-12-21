package com.zookanews.egyptlatestnews.Helpers;

public class Params {
    private int id;
    private Boolean isRead;

    public Params(int id, Boolean isRead) {
        this.id = id;
        this.isRead = isRead;
    }

    public Params(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getRead() {
        return isRead;
    }
}
