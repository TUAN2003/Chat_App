package com.example.chat_app.models;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GroupChat implements Serializable {
    private String idGroup;
    private String enCodeImage;
    private String mNameGroup;
    private List<String> mIdMember;
    private String mLastMessage;
    private String lastSender;
    private Date dateTime;

    public GroupChat(String enCodeImage, String mNameGroup,Date dateTime, List<String> mIdMember) {
        this.enCodeImage = enCodeImage;
        this.mNameGroup = mNameGroup;
        this.mIdMember = mIdMember;
        this.dateTime=dateTime;
    }
    public GroupChat(){}

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

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }
}
