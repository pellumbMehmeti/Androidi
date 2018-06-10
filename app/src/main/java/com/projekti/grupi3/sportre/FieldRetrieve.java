package com.projekti.grupi3.sportre;

public class FieldRetrieve { {

    private String FieldName,Price,Image;

    public FieldRetrieve()
    {
        //konstruktori pa parametra
    }

    public FieldRetrieve(String fieldName, String price, String image) {
        FieldName = fieldName;
        Price = price;
        Image = image;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
