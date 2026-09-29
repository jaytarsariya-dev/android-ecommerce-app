package com.example.androidminorproject;
public class DataClass {
    private String dataname;
    private String dataprice;
    private String datades;
    private String dataimage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataname() {
        return dataname;
    }

    public String getDataprice() {
        return dataprice;
    }

    public String getDatades() {
        return datades;
    }

    public String getDataimage() {
        return dataimage;
    }

    public DataClass(String dataname, String dataprice, String datades, String dataimage) {
        this.dataname = dataname;
        this.dataprice = dataprice;
        this.datades = datades;
        this.dataimage = dataimage;
    }
    public DataClass(){

    }
}

