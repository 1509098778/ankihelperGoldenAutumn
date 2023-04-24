package com.mmjang.ankihelper.ui.plan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.data.Settings;
import com.mmjang.ankihelper.data.database.ExternalDatabase;
import com.mmjang.ankihelper.data.plan.OutputPlanPOJO;
import com.mmjang.ankihelper.util.ColorThemeUtils;
import com.mmjang.ankihelper.util.Constant;
import com.mmjang.ankihelper.data.dict.DictionaryRegister;
import com.mmjang.ankihelper.data.dict.IDictionary;
import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.util.SystemUtils;
import com.mmjang.ankihelper.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlanEditorActivity extends AppCompatActivity {

    private String planNameToEdit;
    private AnkiDroidHelper mAnkiDroid;
    private OutputPlanPOJO planForEdit;
    private Map<Long, String> deckList;
    private Map<Long, String> modelList;
    private List<IDictionary> dictionaryList;
    private List<FieldsMapItem> fieldsMapItemList;
    private IDictionary currentDictionary;
    private long currentDeckId;
    private long currentModelId;
    //views
    private EditText planNameEditText;
    private Spinner dictionarySpinner;
    private TextView dictionaryIntroductionTextView;
    private Spinner deckSpinner;
    private Spinner modelSpinner;
    private RecyclerView fieldsSpinnersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Settings.getInstance(this).getPinkThemeQ()){
//            setTheme(R.style.AppThemePink);
//        }
        ColorThemeUtils.setColorTheme(PlanEditorActivity.this);
        super.onCreate(savedInstanceState);
        if(!MyApplication.getAnkiDroid().isAnkiDroidRunning()) {
            startAnkiDroid();
            return;
        }
        setContentView(R.layout.activity_plan_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            initAnkiApi();
            setViewMember();
            handleIntent();
            loadDecksAndModels();
            populateDictionary();
            populateDecksAndModels();
        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_plan_editor_menu_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_plan_edit:
                if (savePlan()) {
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
        planNameEditText = (EditText) findViewById(R.id.text_edit_plan_name);
        dictionarySpinner = (Spinner) findViewById(R.id.dictionary_spinner);
        dictionaryIntroductionTextView = (TextView) findViewById(R.id.text_view_dictionary_introduction);
        deckSpinner = (Spinner) findViewById(R.id.deck_spinner);
        modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        fieldsSpinnersContainer = (RecyclerView) findViewById(R.id.recycler_view_fields_map);
    }

    private void initAnkiApi() {
        if (mAnkiDroid == null) {
            mAnkiDroid = new AnkiDroidHelper(this);
        }
        if (!AnkiDroidHelper.isApiAvailable(MyApplication.getContext())) {
            Toast.makeText(this, R.string.api_not_available_message, Toast.LENGTH_LONG).show();
        }

        if (mAnkiDroid.shouldRequestPermission()) {
            mAnkiDroid.requestPermission(this, 0);
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (action != null && action.equals(Intent.ACTION_SEND)) {
                planNameToEdit = intent.getStringExtra(Intent.EXTRA_TEXT);
                OutputPlanPOJO re = ExternalDatabase.getInstance().getPlanByName(planNameToEdit);
                if (re != null) {
                    planForEdit = re;
                    //set plan name unable to edit
                    planNameEditText.setText(planNameToEdit);
                    //planNameEditText.setEnabled(false);
                }
            }
        }
    }

    private void loadDecksAndModels() {
        deckList = Utils.hashMap2LinkedHashMap(mAnkiDroid.getApi().getDeckList());
        modelList = Utils.hashMap2LinkedHashMap(mAnkiDroid.getApi().getModelList());
    }

    private void populateDictionary() {
        if (dictionaryList == null) {
            dictionaryList = DictionaryRegister.getDictionaryObjectList();
        }

        String[] dictionaryNameList = new String[dictionaryList.size()];
        for (int i = 0; i < dictionaryList.size(); i++) {
            dictionaryNameList[i] = dictionaryList.get(i).getDictionaryName();
        }
        ArrayAdapter<String> dictionarySpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, dictionaryNameList);
        dictionarySpinner.setAdapter(dictionarySpinnerAdapter);

        if (planForEdit != null) {
            String key1 = planForEdit.getDictionaryKey();
            boolean find = false;
            for (int i = 0; i < dictionaryList.size(); i++) {
                IDictionary dict = dictionaryList.get(i);
                String key2 = dict.getDictionaryName();
                if (key1.equals(key2)) {
                    currentDictionary = dictionaryList.get(i);
                    dictionaryIntroductionTextView.setText(currentDictionary.getIntroduction());
                    dictionarySpinner.setSelection(i);
                    find = true;
                    break;
                }
            }
            if(!find){
            String message = String.format("词典\"%s\"不存在，请检查是否需要重新导入自定义词典", key1);
            Utils.showMessage(PlanEditorActivity.this, message);
            }
        } else {

            int pos = dictionarySpinner.getSelectedItemPosition();
            currentDictionary = dictionaryList.get(pos);
            dictionaryIntroductionTextView.setText(currentDictionary.getIntroduction());

        }

        dictionarySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentDictionary = dictionaryList.get(position);
                        dictionaryIntroductionTextView.setText(currentDictionary.getIntroduction());
                        refreshFieldSpinners();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void populateDecksAndModels() {
        ArrayAdapter<String> deckSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, Utils.getMapValueArray(deckList));
        deckSpinner.setAdapter(deckSpinnerAdapter);
        ArrayAdapter<String> modelSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, Utils.getMapValueArray(modelList));
        modelSpinner.setAdapter(modelSpinnerAdapter);

        if (planForEdit != null) {
            long savedDeckId = planForEdit.getOutputDeckId();
            long savedModelId = planForEdit.getOutputModelId();
            //int i = 0;
            long[] deckIdList = Utils.getMapKeyArray(deckList);
            //int deckPos = Arrays.asList(deckIdList).indexOf(savedDeckId);
            int deckPos = Utils.getArrayIndex(deckIdList, savedDeckId);
            if (deckPos == -1) {
                deckPos = 0;
            }
            currentDeckId = deckIdList[deckPos];
            deckSpinner.setSelection(deckPos);

            long[] modelIdList = Utils.getMapKeyArray(modelList);
            //int modelPos = Arrays.asList(modelIdList).indexOf(savedModelId);
            int modelPos = Utils.getArrayIndex(modelIdList, savedModelId);
            if (modelPos == -1) {
                modelPos = 0;
            }
            currentModelId = modelIdList[modelPos];
            modelSpinner.setSelection(modelPos);

            refreshFieldSpinners();
        } else {
            currentDeckId = Utils.getMapKeyArray(deckList)[0];
            currentModelId = Utils.getMapKeyArray(modelList)[0];
            refreshFieldSpinners();
        }

        modelSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentModelId = Utils.getMapKeyArray(modelList)[position];
                        refreshFieldSpinners();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        deckSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentDeckId = Utils.getMapKeyArray(deckList)[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }

    private void refreshFieldSpinners() {
        String[] fields = mAnkiDroid.getApi().getFieldList(currentModelId);
        String[] dictionaryElements = currentDictionary.getExportElementsList();
        String[] sharedElements = Constant.getSharedExportElements();
        String[] allElements = Utils.concatenate(sharedElements, dictionaryElements);
        fieldsMapItemList = new ArrayList<>();
        //if edit, than set spinner initial position
        if (planForEdit != null && currentModelId == planForEdit.getOutputModelId()) {
            for (String fld : fields) {
                Map<String, String> fldMap = planForEdit.getFieldsMap();
                if (fldMap.containsKey(fld)) {
                    ComplexElement ce = new ComplexElement(fldMap.get(fld));

                    int pos = Arrays.asList(allElements).indexOf(ce.getElementNormal());
                    if (pos == -1) {
                        pos = 0;
                    }

                    int appendingPos = Arrays.asList(allElements).indexOf(ce.getElementAppending());
                    if (appendingPos == -1) {
                        appendingPos = 0;
                    }
                    fieldsMapItemList.add(new FieldsMapItem(fld, allElements, pos, appendingPos));
                } else {
                    fieldsMapItemList.add(new FieldsMapItem(fld, allElements));
                }
                //Arrays.asList(allElements).indexOf();
            }
        } else {
            for (String fld : fields) {
                fieldsMapItemList.add(new FieldsMapItem(fld, allElements));
            }
        }

        fieldsSpinnersContainer.setLayoutManager(new LinearLayoutManager(this));
        fieldsSpinnersContainer.setAdapter(new FieldMapSpinnerListAdapter(PlanEditorActivity.this, fieldsMapItemList));
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

    private boolean savePlan() {
        String planName = planNameEditText.getText().toString().trim();
        if (planName.isEmpty()) {
            Toast.makeText(this, R.string.str_plan_name_should_not_be_blank, Toast.LENGTH_SHORT).show();
            return false;
        }
        //DataSupport.findAll()
        OutputPlanPOJO plan;
        if (planForEdit != null) {
            //if when edit an exiting plan, and the user chang the plan name to another existing plan name
            if (!planName.equals(planNameToEdit)) {
                //if name conflicts, toast.
                OutputPlanPOJO rel = ExternalDatabase.getInstance().getPlanByName(planName);
                if (rel != null) {
                    Toast.makeText(this, R.string.plan_already_exists, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            plan = planForEdit;
        } else {
            //if name conflicts, toast.
            OutputPlanPOJO rel = ExternalDatabase.getInstance().getPlanByName(planName);
            if (rel != null) {
                Toast.makeText(this, R.string.plan_already_exists, Toast.LENGTH_SHORT).show();
                return false;
            }
            plan = new OutputPlanPOJO();

        }
        //new OutputPlan();
        plan.setPlanName(planName);
        plan.setDictionaryKey(currentDictionary.getDictionaryName());
        plan.setOutputDeckId(currentDeckId);
        plan.setOutputModelId(currentModelId);

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        boolean allFieldsAreEmpty = true;
        boolean allFieldsAddingAreEmpty = true;
        for (FieldsMapItem item : fieldsMapItemList) {
            String k = item.getField();
            String nv = item.getExportedElementNames()[item.getSelectedFieldPos()];
            String av = item.getExportedElementNames()[item.getSelectedFieldAppendingPos()];
            if (!nv.equals(Constant.getSharedExportElements()[0])) {
                allFieldsAreEmpty = false;
            }
            if (!av.equals(Constant.getSharedExportElements()[0])) {
                allFieldsAddingAreEmpty = false;
            }
            map.put(k, new ComplexElement(nv, av).getElement());
        }

        if (allFieldsAreEmpty || allFieldsAddingAreEmpty) {
            Toast.makeText(this, R.string.save_plan_error_all_blank, Toast.LENGTH_SHORT).show();
            return false;
        }
        plan.setFieldsMap(map);
        if(planNameToEdit != null){
            Settings settings = Settings.getInstance(this);
            if(!settings.getTangoDictChckerMap(planForEdit.getPlanName()).isEmpty()) {
                HashMap mdictCheckerMap = settings.getTangoDictChckerMap(planName);
                settings.removeMdictCheckerMap(planForEdit.getPlanName());
                settings.setTangoDictCheckerMap(planName, mdictCheckerMap);
            }
            ExternalDatabase.getInstance().updatePlan(plan, planNameToEdit);
        }else{
            ExternalDatabase.getInstance().insertPlan(plan);
        }
        return true;
    }

    private void startAnkiDroid() {
        if (AnkiDroidHelper.isApiAvailable(MyApplication.getContext()) && !MyApplication.getAnkiDroid().isAnkiDroidRunning()) {
            MyApplication.getAnkiDroid().startAnkiDroid();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(SystemUtils.isBackActivity(PlanEditorActivity.this, "com.mmjang.ankihelper")) {
                                            Intent intent = getIntent();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            handler.removeCallbacks(this);
                                        }
                                        else {
                                            handler.postDelayed(this, 200);
                                        }
                                    }
                                },
                    200);
        }
    }
}
