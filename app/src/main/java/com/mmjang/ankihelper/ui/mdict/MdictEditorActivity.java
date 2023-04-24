package com.mmjang.ankihelper.ui.mdict;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.data.dict.DictLanguageType;
import com.mmjang.ankihelper.data.dict.Mdict;
import com.mmjang.ankihelper.data.dict.mdict.MdictInformation;
import com.mmjang.ankihelper.data.dict.mdict.MdictManager;
import com.mmjang.ankihelper.ui.tango.FieldMapListAdapter;
import com.mmjang.ankihelper.ui.tango.FieldsMapItem;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.util.Trace;

import java.util.Arrays;
import java.util.List;

public class MdictEditorActivity extends AppCompatActivity {

    private int order = -1;
    MdictManager mdictManager;
    //views
    private EditText dictNameEditText;
    private EditText defRegexEditText;
    private Spinner languageNameSpinner;

    /**
     * 适配器的数据源
     */
    private List<Integer> dataList;
    private List<List<FieldsMapItem>> fieldsMapItemLList;
    private List<FieldsMapItem> totalFieldsMapItemList;
    /**
     * 适配器
     */
    private FieldMapListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Settings.getInstance(this).getPinkThemeQ()){
//            setTheme(R.style.AppThemePink);
//        }
        ColorThemeUtils.setColorTheme(MdictEditorActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdict_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mdictManager = new MdictManager(MyApplication.getContext(), "");
        try {
            setViewMember();
            handleIntent();
            populateLanguageName();

            // 初始化数据
            adapter = new FieldMapListAdapter(MdictEditorActivity.this, totalFieldsMapItemList);

        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_mdict_editor_menu_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_mdict_edit:
                if (saveMdictInformation()) {
                    finish();
                }
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    private void setViewMember() {
        // 实例化控件
        dictNameEditText = (EditText) findViewById(R.id.et_mdict_name);
        defRegexEditText = (EditText) findViewById(R.id.et_mdict_definition_regex);
        languageNameSpinner = (Spinner) findViewById(R.id.language_name_spinner);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (action != null && action.equals(Intent.ACTION_SEND)) {
                order = intent.getIntExtra(Constant.INTENT_ANKIHELPER_MDICT_ORDER, -1);
                MdictInformation mdictInfor = mdictManager.getMdictInfoByOrder(order);
                if (order != -1) {
                    Trace.e("dictName", ""+mdictInfor.getDictName());
                    dictNameEditText.setText(mdictInfor.getDictName());
                    defRegexEditText.setText(mdictInfor.getDefRegex());
                    //dictNameEditText.setEnabled(false);
                }
            }
        } else
            finish();
    }

    private void populateLanguageName() {
        Trace.e("languageName", Arrays.toString(DictLanguageType.getLanguageNameList()));
        ArrayAdapter<String> languageNameSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, DictLanguageType.getLanguageNameList());
        languageNameSpinner.setAdapter(languageNameSpinnerAdapter);

        if (order != -1) {
            languageNameSpinner.setSelection(mdictManager.getMdictInfoByOrder(order).getDictLang());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(TestActivity.this, R.string.permission_granted, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private boolean saveMdictInformation() {
        String dictName = dictNameEditText.getText().toString().trim();
        if (dictName.isEmpty()) {
            Toast.makeText(this, R.string.str_locator_dict_name_should_not_be_blank, Toast.LENGTH_SHORT).show();
            return false;
        }
        MdictInformation mdictInformation = mdictManager.getMdictInfoByOrder(order);
        mdictInformation.setDictLang(languageNameSpinner.getSelectedItemPosition());
        mdictInformation.setDefRegex(defRegexEditText.getText().toString());
        mdictManager.updateMdictInformation(mdictInformation);
        return true;
    }
}
