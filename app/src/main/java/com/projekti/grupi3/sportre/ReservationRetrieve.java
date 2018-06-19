package com.projekti.grupi3.sportre;


public class ReservationRetrieve {

    String FieldName,Date,Time,UserId;

    public ReservationRetrieve()
    {
            //konstruktor pa parametra
    }

    public ReservationRetrieve(String fieldName,String date,String time,String usernameId)
    {
        FieldName=fieldName;
        Date=date;
        Time=time;
        UserId=usernameId;

    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String usernameId) {
        UserId = usernameId;
    }
}
