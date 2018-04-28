package com.cui.mdc.mdcMode.entity;

import android.databinding.BaseObservable;

/**
 * Created by cuiyang on 16/6/3.
 */
public class BaseEntity extends BaseObservable {

    protected int code;
    protected String message;
    protected int itemType;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public BaseEntity() {
    }

    public BaseEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}
