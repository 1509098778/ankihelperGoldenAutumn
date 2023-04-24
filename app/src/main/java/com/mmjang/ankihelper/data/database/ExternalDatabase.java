package com.mmjang.ankihelper.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.data.book.Book;
import com.mmjang.ankihelper.data.dict.customdict.CustomDictionaryInformation;
import com.mmjang.ankihelper.data.dict.mdict.MdictInformation;
import com.mmjang.ankihelper.data.history.HistoryPOJO;
import com.mmjang.ankihelper.data.plan.OutputPlanPOJO;

import java.util.ArrayList;
import java.util.List;

public class ExternalDatabase {
    private static final String TAG = "ExternalDatabase";
    private static final String TB_DICT = "dict";
    private static final String TB_ENTRY = "entry";
    private static final String CL_ID = "id";
    private static final String CL_ORDER = "ord";
    private static final String CL_NAME = "name";
    private static final String CL_LANG = "lang";
    private static final String CL_ELEMENTS = "elements";
    private static final String CL_TMPL = "tmpl";
    private static final String CL_DESCRIPTION = "description";
    private static final String CL_DICT_ID = "dict_id";
    private static final String CL_HEADWORD = "headword";
    private static final String CL_ENTRY_TEXTS = "entry_texts";

    private static final String TB_MDICT = "mdict";
    private static final String MDL_ID = "id";
    private static final String MDL_NAME = "name";
    private static final String MDL_DESCRIPTION = "description";
    private static final String MDL_LANG = "lang";
    private static final String MDL_TMPL = "tmpl";
    private static final String MDL_REGEX = "definition_regex";
    private static final String MDL_ELEMENTS = "elements";
    private static final String MDL_ORDER = "ord";

    private static final String SPLITTER = "\t"; //original file is splitted by \t, so it's safe.
    private static final String SQL_CREATE_INDEX = "CREATE INDEX IF NOT EXISTS headword_index ON entry (headword)";
    private static final String SQL_DROP_INDEX = "DROP INDEX IF EXISTS headword_index";
    Context mContext;
    SQLiteDatabase mDatabase;
    private static ExternalDatabase instance;
    private ExternalDatabase(Context context){
        mContext = context;
        ExternalDatabaseHelper dbHelper = new ExternalDatabaseHelper(mContext);
        mDatabase = dbHelper.getWritableDatabase();
    }

    public static ExternalDatabase getInstance() {
        if(instance == null){
            instance = new ExternalDatabase(MyApplication.getContext());
        }
        return instance;
    }

    public void clearDB(){
        mDatabase.delete(TB_DICT, null, null);
        mDatabase.delete(TB_ENTRY, null, null);
    }

    public void deleteCustomDictById(int id){
        mDatabase.delete(TB_DICT,
                "" + CL_ID + "='" + id +"'",
                null
        );
        mDatabase.delete(TB_ENTRY,
                "" + CL_DICT_ID + "='" + id +"'",
                null
        );
    }

    public void updateOrderCustomDict(int id, int newPos) {
        ContentValues cv = new ContentValues();
        cv.put(CL_ORDER, newPos);
        mDatabase.update(TB_DICT, cv, CL_ID + "=" + id, null);
    }

    public static String getHeadwordColumnName(){
        return CL_HEADWORD;
    }

    public void addDictionaryInformation(int id, String name, String lang, String[] elements, String description, String tmpl, int order){
        SQLiteDatabase db = mDatabase;
        ContentValues values = new ContentValues();
        values.put(CL_ID, id);
        values.put(CL_NAME, name);
        values.put(CL_LANG, lang);
        values.put(CL_ELEMENTS, joinFields(elements));
        values.put(CL_DESCRIPTION, description);
        values.put(CL_TMPL, tmpl);
        values.put(CL_ORDER, order);
        db.insert(TB_DICT, null, values);
    }

    public void addEntries(int dictId, List<String[]> entries){ //assume first column is headword
        SQLiteDatabase db = mDatabase;
        db.beginTransaction();
        for(String[] entry : entries){
            if(entry.length < 2){
                continue;
            }
            ContentValues values = new ContentValues();
            values.put(CL_DICT_ID, dictId);
            values.put(CL_HEADWORD, entry[0].toLowerCase());   //must have at least 2 columns, enforced in manager
            String[] elements = entry;//Arrays.copyOfRange(entry, 1, entry.length - 1);
            values.put(CL_ENTRY_TEXTS, joinFields(elements));
            db.insert(TB_ENTRY, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Cursor getWordLookupCursor(int dictId, String word){
        Cursor cursor = mDatabase.query(
                TB_ENTRY,
                new String[] {CL_HEADWORD, CL_ENTRY_TEXTS},
                CL_DICT_ID + "= ? AND " + CL_HEADWORD + "= ? COLLATE NOCASE",
                new String[]{String.valueOf(dictId), word},
                null,null,null, "50");
        return cursor;
    }

    public Cursor getFilterCursor(int dictId, String query){
        Cursor cursor = mDatabase.query(
                TB_ENTRY,
                new String[]{"rowid _id", CL_HEADWORD},
                CL_DICT_ID + "=? AND " + CL_HEADWORD + " LIKE ?",
                new String[]{String.valueOf(dictId), query + "%"},
                CL_HEADWORD,
                null,
                null
        );
        return cursor;
    }

    public List<Integer> getDictIdList(){
        Cursor cursor = mDatabase.query(
                TB_DICT,
                new String[] {CL_ID},
                "",
                null,
                null,
                null,
                null
        );
        List<Integer> re = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            re.add(cursor.getInt(0));
        }
        return re;
    }

    @Nullable  //if null, not found
    public CustomDictionaryInformation getDictInfo(int dictId){
        Cursor cursor = mDatabase.query(
                TB_DICT,
                new String[] {CL_ID, CL_NAME, CL_DESCRIPTION, CL_LANG, CL_TMPL, CL_ELEMENTS, CL_ORDER},
                CL_ID + "=" + dictId,
                null,
                null,
                null,
                null
        );
        CustomDictionaryInformation customDictionaryInformation = null;
        while(cursor.moveToNext()){
            customDictionaryInformation = new CustomDictionaryInformation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    fromFieldsString(cursor.getString(5)),
                    cursor.getInt(6)
            );
        }
        return customDictionaryInformation;
    }

    public void dropHwdIndex(){
        mDatabase.execSQL(SQL_DROP_INDEX);
    }

    public void createHwdIndex(){
        mDatabase.execSQL(SQL_CREATE_INDEX);
    }

    public List<String[]> queryHeadword(int dictId, String q){
        //dont use collate nocase
        List<String[]> result = new ArrayList<>();
        if(q.isEmpty()){
            return result;
        }
        q = q.toLowerCase();
        Cursor cursor = mDatabase.query(
                TB_ENTRY,
                new String[] {CL_ENTRY_TEXTS},
                CL_DICT_ID + "=? AND " + CL_HEADWORD + "=?",
                new String[]{String.valueOf(dictId), q}
                ,null,null,null);
//        Cursor cursor1 = mDatabase.rawQuery(
//                "select entry_texts from entry where rowid in (select rowid from entry where headword=?) and dict_id=?", new String[]{
//                        q,
//                        Integer.toString(dictId)
//                }
//        );
        while(cursor.moveToNext()){
            result.add(fromFieldsString(cursor.getString(0)));
        }
        return result;
    }

    public boolean insertHistory(HistoryPOJO history){
        ContentValues values = new ContentValues();
        values.put(DBContract.History.COLUMN_TIME_STAMP, history.getTimeStamp());
        values.put(DBContract.History.COLUMN_DEFINITION, history.getDefinition());
        values.put(DBContract.History.COLUMN_DICTIONARY, history.getDictionary());
        values.put(DBContract.History.COLUMN_NOTE, history.getNote());
        values.put(DBContract.History.COLUMN_TAG, history.getTag());
        values.put(DBContract.History.COLUMN_SENTENCE, history.getSentence());
        values.put(DBContract.History.COLUMN_TYPE, history.getType());
        values.put(DBContract.History.COLUMN_WORD, history.getWord());
        long result = mDatabase.insert(DBContract.History.TABLE_NAME, null, values);
        return result >= 0;
    }

    public void insertManyHistory(List<HistoryPOJO> historyPOJOS){
        mDatabase.beginTransaction();
        for(HistoryPOJO history : historyPOJOS){
            insertHistory(history);
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public List<HistoryPOJO> getHistoryAfter(long timeStamp){
        Cursor cursor = mDatabase.rawQuery(
                String.format("select %s, %s, %s, %s, %s, %s, %s, %s from %s where %s > %s",
                        DBContract.History.COLUMN_TIME_STAMP,
                        DBContract.History.COLUMN_DEFINITION,
                        DBContract.History.COLUMN_DICTIONARY,
                        DBContract.History.COLUMN_NOTE,
                        DBContract.History.COLUMN_TAG,
                        DBContract.History.COLUMN_SENTENCE,
                        DBContract.History.COLUMN_TYPE,
                        DBContract.History.COLUMN_WORD,
                        DBContract.History.TABLE_NAME,
                        DBContract.History.COLUMN_TIME_STAMP,
                        timeStamp
                        ),
                null
        );
        List<HistoryPOJO> result = new ArrayList<>();
        while (cursor.moveToNext()){
            HistoryPOJO historyPOJO = new HistoryPOJO();
            historyPOJO.setTimeStamp(cursor.getLong(0));
            historyPOJO.setDefinition(cursor.getString(1));
            historyPOJO.setDictionary(cursor.getString(2));
            historyPOJO.setNote(cursor.getString(3));
            historyPOJO.setTag(cursor.getString(4));
            historyPOJO.setSentence(cursor.getString(5));
            historyPOJO.setType(cursor.getInt(6));
            historyPOJO.setWord(cursor.getString(7));
            result.add(historyPOJO);
        }
        return result;
    }

    public List<OutputPlanPOJO> getAllPlan(){
        Cursor cursor = mDatabase.rawQuery(
                String.format("select %s, %s, %s, %s, %s from %s",
                        DBContract.Plan.COLUMN_PLAN_NAME,
                        DBContract.Plan.COLUMN_DICTIONARY_KEY,
                        DBContract.Plan.COLUMN_OUTPUT_DECK_ID,
                        DBContract.Plan.COLUMN_OUTPUT_MODEL_ID,
                        DBContract.Plan.COLUMN_FIELDS_MAP,
                        DBContract.Plan.TABLE_NAME
                        ),
                null
        );
        List<OutputPlanPOJO> result = new ArrayList<>();
        while(cursor.moveToNext()){
            OutputPlanPOJO outputPlanPOJO = new OutputPlanPOJO();
            outputPlanPOJO.setPlanName(cursor.getString(0));
            outputPlanPOJO.setDictionaryKey(cursor.getString(1));
            outputPlanPOJO.setOutputDeckId(cursor.getLong(2));
            outputPlanPOJO.setOutputModelId(cursor.getLong(3));
            outputPlanPOJO.setFieldsMapString(cursor.getString(4));
            result.add(outputPlanPOJO);
        }
        return result;
    }

    public OutputPlanPOJO getPlanByName(String planName){
        Cursor cursor = mDatabase.rawQuery(
                String.format("select %s, %s, %s, %s, %s from %s where %s='%s'",
                        DBContract.Plan.COLUMN_PLAN_NAME,
                        DBContract.Plan.COLUMN_DICTIONARY_KEY,
                        DBContract.Plan.COLUMN_OUTPUT_DECK_ID,
                        DBContract.Plan.COLUMN_OUTPUT_MODEL_ID,
                        DBContract.Plan.COLUMN_FIELDS_MAP,
                        DBContract.Plan.TABLE_NAME,
                        DBContract.Plan.COLUMN_PLAN_NAME,
                        planName
                ),
                null
        );
        OutputPlanPOJO result = null;
        while(cursor.moveToNext()){
            OutputPlanPOJO outputPlanPOJO = new OutputPlanPOJO();
            outputPlanPOJO.setPlanName(cursor.getString(0));
            outputPlanPOJO.setDictionaryKey(cursor.getString(1));
            outputPlanPOJO.setOutputDeckId(cursor.getLong(2));
            outputPlanPOJO.setOutputModelId(cursor.getLong(3));
            outputPlanPOJO.setFieldsMapString(cursor.getString(4));
            result = outputPlanPOJO;
        }
        return result;
    }

    public void refreshPlanWith(List<OutputPlanPOJO> outputPlanPOJOS){
        mDatabase.beginTransaction();
        mDatabase.delete(DBContract.Plan.TABLE_NAME, null, null);
        for(OutputPlanPOJO outputPlanPOJO : outputPlanPOJOS){
            insertPlan(outputPlanPOJO);
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void deleteAllPlansDB(){
        mDatabase.beginTransaction();
        mDatabase.delete(DBContract.Plan.TABLE_NAME, null, null);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public int deletePlanByName(String planName){
        return mDatabase.delete(DBContract.Plan.TABLE_NAME,
                "" + DBContract.Plan.COLUMN_PLAN_NAME + "='" + planName +"'",
                null
                );
    }

    //the plan name don't change
    public int updatePlan(OutputPlanPOJO outputPlanPOJO){
        return updatePlan(outputPlanPOJO, outputPlanPOJO.getPlanName());
    }

    //if we want to change the plan's name
    public int updatePlan(OutputPlanPOJO outputPlanPOJO, String oldPlanName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Plan.COLUMN_PLAN_NAME, outputPlanPOJO.getPlanName());
        contentValues.put(DBContract.Plan.COLUMN_DICTIONARY_KEY, outputPlanPOJO.getDictionaryKey());
        contentValues.put(DBContract.Plan.COLUMN_OUTPUT_DECK_ID, outputPlanPOJO.getOutputDeckId());
        contentValues.put(DBContract.Plan.COLUMN_OUTPUT_MODEL_ID, outputPlanPOJO.getOutputModelId());
        contentValues.put(DBContract.Plan.COLUMN_FIELDS_MAP, outputPlanPOJO.getFieldsMapString());
        return mDatabase.update(DBContract.Plan.TABLE_NAME, contentValues,
                "" + DBContract.Plan.COLUMN_PLAN_NAME + "='" + oldPlanName +"'", null);
    }

    public long insertPlan(OutputPlanPOJO outputPlanPOJO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Plan.COLUMN_PLAN_NAME, outputPlanPOJO.getPlanName());
        contentValues.put(DBContract.Plan.COLUMN_DICTIONARY_KEY, outputPlanPOJO.getDictionaryKey());
        contentValues.put(DBContract.Plan.COLUMN_OUTPUT_DECK_ID, outputPlanPOJO.getOutputDeckId());
        contentValues.put(DBContract.Plan.COLUMN_OUTPUT_MODEL_ID, outputPlanPOJO.getOutputModelId());
        contentValues.put(DBContract.Plan.COLUMN_FIELDS_MAP, outputPlanPOJO.getFieldsMapString());
        return mDatabase.insert(DBContract.Plan.TABLE_NAME, null, contentValues);
    }

    private static String joinFields(String[] fields){
        StringBuilder sb = new StringBuilder();
        for(String s : fields){
            sb.append(s);
            sb.append(SPLITTER);
        }
        return sb.toString().trim();
    }

    private static String[] fromFieldsString(String fieldsString){
        return fieldsString.split(SPLITTER);
    }

        //book operations
    public long insertBook(Book book){
        return mDatabase.insert(DBContract.Book.TABLE_NAME, null, book.getContentValues());
    }

    public long updateBook(Book book){
        return mDatabase.update(DBContract.Book.TABLE_NAME, book.getContentValues(), "id=" + book.getId(), null);
    }

    public long deleteBook(Book book){
        return mDatabase.delete(DBContract.Book.TABLE_NAME, "id=" + book.getId(), null);
    }

    public List<Book> getLastBooks(){
        List<Book> bookList = new ArrayList<>();
        Cursor cursor = mDatabase.query(
            DBContract.Book.TABLE_NAME,
            new String[]{DBContract.Book.COLUMN_ID, DBContract.Book.COLUMN_LAST_OPEN_TIME,
                    DBContract.Book.COLUMN_BOOK_NAME, DBContract.Book.COLUMN_AUTHOR, DBContract.Book.COLUMN_BOOK_PATH, DBContract.Book.COLUMN_READ_POSITION},
            null, null, null, null, DBContract.Book.COLUMN_LAST_OPEN_TIME + " desc");
        while(cursor.moveToNext()){
            bookList.add(
                    new Book(
                            cursor.getLong(0),
                            cursor.getLong(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5)
                    )
            );
        }
        return bookList;
    }

    public Book refreshBook(Book oldbook){
        List<Book> bookList = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                DBContract.Book.TABLE_NAME,
                new String[]{DBContract.Book.COLUMN_ID, DBContract.Book.COLUMN_LAST_OPEN_TIME,
                        DBContract.Book.COLUMN_BOOK_NAME, DBContract.Book.COLUMN_AUTHOR, DBContract.Book.COLUMN_BOOK_PATH, DBContract.Book.COLUMN_READ_POSITION},
                "id=" + oldbook.getId(), null, null, null, null);
        if(cursor.moveToFirst()) {
            return new Book(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }else{
            return null;
        }
    }

    //mdict
    public void clearMdictDB(){
        mDatabase.delete(TB_MDICT, null, null);
    }

    public void deleteMdictById(int id){
        mDatabase.delete(TB_MDICT,
                "" + MDL_ID + "='" + id +"'",
                null
        );
    }

    public void updateOrderMdict(int id, int newPos) {
        ContentValues cv = new ContentValues();
        cv.put(MDL_ORDER, newPos);
        mDatabase.update(TB_MDICT, cv, MDL_ID + "=" + id, null);
    }

    public void addMdictInformation(int id, String name, String lang, String[] elements, String description, String tmpl, int order){
        SQLiteDatabase db = mDatabase;
        ContentValues values = new ContentValues();
        values.put(MDL_ID, id);
        values.put(MDL_NAME, name);
        values.put(MDL_LANG, lang);
        values.put(MDL_ELEMENTS, joinFields(elements));
        values.put(MDL_DESCRIPTION, description);
        values.put(MDL_TMPL, tmpl);
        values.put(MDL_ORDER, order);
        db.insert(TB_MDICT, null, values);
    }

    public void addMdictInformation(MdictInformation mdictInformation){
        SQLiteDatabase db = mDatabase;
        ContentValues values = new ContentValues();
        values.put(MDL_ID, mdictInformation.getId());
        values.put(MDL_NAME, mdictInformation.getDictName());
        values.put(MDL_LANG, mdictInformation.getDictLang());
        values.put(MDL_ELEMENTS, joinFields(mdictInformation.getFields()));
        values.put(MDL_DESCRIPTION, mdictInformation.getDictIntro());
        values.put(MDL_REGEX, mdictInformation.getDefRegex());
        values.put(MDL_TMPL, mdictInformation.getDefTpml());
        values.put(MDL_ORDER, mdictInformation.getOrder());
        db.insert(TB_MDICT, null, values);
    }

    public int updateMdictInformation(MdictInformation mdictInformation){
        try {
            SQLiteDatabase db = mDatabase;
            ContentValues values = new ContentValues();
//        values.put(MDL_ID, mdictInformation.getId());
            values.put(MDL_NAME, mdictInformation.getDictName());
            values.put(MDL_LANG, mdictInformation.getDictLang());
            values.put(MDL_ELEMENTS, joinFields(mdictInformation.getFields()));
            values.put(MDL_DESCRIPTION, mdictInformation.getDictIntro());
            values.put(MDL_TMPL, mdictInformation.getDefTpml());
            values.put(MDL_REGEX, mdictInformation.getDefRegex());
//        values.put(MDL_ORDER, mdictInformation.getOrder());
            return db.update(TB_MDICT, values, MDL_ID + "=" + mdictInformation.getId(), null);
        } catch(Exception e) {
            return -1;
        }
    }

    public List<Integer> getMdictIdList(){
        Cursor cursor = mDatabase.query(
                TB_MDICT,
                new String[] {MDL_ID},
                "",
                null,
                null,
                null,
                null
        );
        List<Integer> re = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            re.add(cursor.getInt(0));
        }
        return re;
    }

    @Nullable  //if null, not found
    public MdictInformation getMdictInfoById(int dictId){
        Cursor cursor = mDatabase.query(
                TB_MDICT,
                new String[] {MDL_ID, MDL_NAME, MDL_DESCRIPTION, MDL_LANG, MDL_TMPL, MDL_REGEX, MDL_ELEMENTS, MDL_ORDER},
                MDL_ID + "=" + dictId,
                null,
                null,
                null,
                null
        );
        MdictInformation mdictInformation = null;
        while(cursor.moveToNext()){
            mdictInformation = new MdictInformation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    fromFieldsString(cursor.getString(6)),
                    cursor.getInt(7)
            );
        }
        return mdictInformation;
    }

    @Nullable  //if null, not found
    public MdictInformation getMdictInfoByOrder(int order){
        Cursor cursor = mDatabase.query(
                TB_MDICT,
                new String[] {MDL_ID, MDL_NAME, MDL_DESCRIPTION, MDL_LANG, MDL_TMPL, MDL_REGEX, MDL_ELEMENTS, MDL_ORDER},
                MDL_ORDER + "=" + order,
                null,
                null,
                null,
                null
        );
        MdictInformation mdictInformation = null;
        while(cursor.moveToNext()){
            mdictInformation = new MdictInformation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    fromFieldsString(cursor.getString(6)),
                    cursor.getInt(7)
            );
        }
        return mdictInformation;
    }
}
