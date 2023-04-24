package com.mmjang.ankihelper.ui.plan;

public class ComplexElement {
    private static String SEPERATOR = "\t";
    public String getElementNormal() {
        return elementNormal;
    }

    public void setElementNormal(String elementNormal) {
        this.elementNormal = elementNormal;
    }

    public String getElement() {
        return elementNormal + SEPERATOR + elementAppending;
    }
    public String getElementAppending() {
        return elementAppending;
    }

    public void setElementAppending(String elementAppending) {
        this.elementAppending = elementAppending;
    }

    String elementNormal, elementAppending;

    public ComplexElement() {
        elementNormal = "空";
        elementAppending = "空";
    }

    public ComplexElement(String normal, String append) {
        elementNormal = normal;
        elementAppending = append;
    }

    public ComplexElement(String str) {
        String[] eleArr = str.split(SEPERATOR);
        if(eleArr.length == 2) {
            elementNormal = eleArr[0];
            elementAppending = eleArr[1];
        } else {
            elementNormal = str;
            elementAppending = str;
        }
    }




}
