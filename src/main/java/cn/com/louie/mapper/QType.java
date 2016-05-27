

package cn.com.louie.mapper;

import java.lang.reflect.Type;

public class QType {
    private String key;
    private Type type;

    public QType(String key, Type type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
