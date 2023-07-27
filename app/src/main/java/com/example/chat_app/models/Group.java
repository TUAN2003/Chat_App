package com.example.chat_app.models;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Group implements Serializable {
    private String idGroup;
    private String enCodeImage;
    private String mNameGroup;
    private List<String> mIdMember;
    private String mLastMessage;
    private List<String> watcheds;
    private Date dateTime;

    public Group(){}

    public String getEnCodeImage() {
        return enCodeImage;
    }

    public void setEnCodeImage(String enCodeImage) {
        this.enCodeImage = enCodeImage;
    }

    public String getNameGroup() {
        return mNameGroup;
    }

    public void setNameGroup(String mNameGroup) {
        this.mNameGroup = mNameGroup;
    }

    public List<String> getIdMember() {
        return mIdMember;
    }

    public void setIdMember(List<String> mIdMember) {
        this.mIdMember = mIdMember;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String mLastMessage) {
        this.mLastMessage = mLastMessage;
    }

    public Date getDate() {
        return dateTime;
    }

    public void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public List<String> getWatcheds() {
        return watcheds;
    }

    public void setWatcheds(List<String> watcheds) {
        this.watcheds = watcheds;
    }
}
