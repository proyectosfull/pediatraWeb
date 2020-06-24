package com.mibebe.bean.util;

import java.io.Serializable;

public class FileBase64 implements Serializable {
    
    private String type;
    private String name;
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data != null ? data.replace(" ", "+") : data;
    }
    
    public String getExtension() {
        return type.split("/")[1];
    }
}
