package com.mmjang.ankihelper.data.dict.mdict;

/**
 * Created by liao on 2017/8/12.
 */

public class MdictInformation {
    private int id;
    private String dictName;
    private String dictIntro;
    private int dictLang;
    private String defTpml;
    private String defRegex;
    private String[] fields;
    private int order;

    public MdictInformation(int id, String dictName, String dictIntro, int dictLang, String defTpml, String defRegex, String[] fields, int order){
        //this.version = version;
        this.id = id;
        this.dictName = dictName;
        this.dictIntro = dictIntro;
        this.dictLang = dictLang;
        this.defTpml = defTpml;
        this.defRegex = defRegex;
        this.fields = fields;
        this.order = order;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDictName() {
        return dictName;
    }

    public String getDictIntro() {
        return dictIntro;
    }

    public int getDictLang() {
        return dictLang;
    }

    public void setDictLang(int dictLang) {
        this.dictLang = dictLang;
    }

    public String getDefTpml() {
        return defTpml;
    }

    public String getDefRegex() {
        return defRegex;
    }

    public void setDefRegex(String defRegex) {
        this.defRegex = defRegex;
    }

    public String[] getFields() {
        return fields;
    }
}
