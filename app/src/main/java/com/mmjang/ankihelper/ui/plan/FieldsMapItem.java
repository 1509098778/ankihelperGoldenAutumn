package com.mmjang.ankihelper.ui.plan;

/**
 * Created by liao on 2017/4/28.
 */

public class FieldsMapItem {
    private String field;
    private String[] exportElements;
    private int selectedFieldPos, selectedFieldAppendingPos;

    public FieldsMapItem(String fld, String[] expEleList) {
        field = fld;
        exportElements = expEleList;
        selectedFieldPos = 0;
        selectedFieldAppendingPos = 0;
    }

    public FieldsMapItem(String fld, String[] expEleList, int selPos, int selAppendingPos) {
        field = fld;
        exportElements = expEleList;
        selectedFieldPos = selPos;
        selectedFieldAppendingPos = selAppendingPos;
    }

    public String[] getExportedElementNames() {
        return exportElements;
    }

    public String getField() {
        return field;
    }

    public int getSelectedFieldPos() {
        return selectedFieldPos;
    }

    public void setSelectedFieldPos(int selectedFieldPos) {
        this.selectedFieldPos = selectedFieldPos;
    }

    public int getSelectedFieldAppendingPos() {
        return selectedFieldAppendingPos;
    }

    public void setSelectedFieldAppendingPos(int selectedFieldAppendingPos) {
        this.selectedFieldAppendingPos = selectedFieldAppendingPos;
    }
}
