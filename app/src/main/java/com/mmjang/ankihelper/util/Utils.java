package com.mmjang.ankihelper.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.ichi2.anki.FlashCardsContract;
import com.mmjang.ankihelper.MyApplication;
import com.mmjang.ankihelper.R;
import com.mmjang.ankihelper.anki.AnkiDroidHelper;
import com.mmjang.ankihelper.data.dict.Definition;
import com.mmjang.ankihelper.data.plan.OutputPlan;
import com.mmjang.ankihelper.data.plan.OutputPlanPOJO;
import com.mmjang.ankihelper.ui.LauncherActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liao on 2017/4/27.
 */

public class Utils {
    private static final String FIELDS_SEPERATOR = "@@@@";

    public static String fieldsMap2Str(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(value.equals("") || value.isEmpty())
                value = " ";
            sb.append(key);
            sb.append(FIELDS_SEPERATOR);
            sb.append(value);
            sb.append(FIELDS_SEPERATOR);
        }
        return sb.toString();
    }

    public static Map<String, String> fieldsStr2Map(String str) {
        LinkedHashMap<String, String> results = new LinkedHashMap<>();
        String[] fields = str.split(FIELDS_SEPERATOR);
        int pairs = fields.length / 2;
        for (int i = 0; i < pairs; i++) {
            results.put(fields[i * 2], fields[i * 2 + 1].trim());
        }
        return results;
    }

    public static LinkedHashMap<Long, String> hashMap2LinkedHashMap(Map<Long, String> hashMap) {
//        LinkedHashMap<Long, String> linkedHashMap = new LinkedHashMap<Long, String>();
//        Long[] keyArray = new Long[hashMap.size()];
//        int i = 0;
//        for (Long id : hashMap.keySet()) {
//            keyArray[i] = id;
//            i++;
//        }
//        Arrays.sort(keyArray);
//        for (Long k : hashMap.keySet()) {
//            linkedHashMap.put(k, hashMap.get(k));
//        }
//        return linkedHashMap;

        LinkedHashMap<Long, String> linkedHashMap = new LinkedHashMap<>();
        List<Map.Entry<Long, String>> entrys = new ArrayList<>(hashMap.entrySet());
        Collections.sort(entrys, new Comparator<Map.Entry<Long, String>>() {
            @Override
            public int compare(Map.Entry<Long, String> o1, Map.Entry<Long, String> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for(Map.Entry<Long, String> entry: entrys) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    public static long[] getMapKeyArray(Map<Long, String> map) {
        long[] keyArr = new long[map.size()];
        int i = 0;
        for (long id : map.keySet()) {
            keyArr[i] = id;
            i++;
        }
        return keyArr;
    }

    public static String[] getMapValueArray(Map<Long, String> map) {
        String[] valArr = new String[map.size()];
        int i = 0;
        for (String val : map.values()) {
            valArr[i] = val;
            i++;
        }
        return valArr;
    }

    public static long findMapKeyByVal(Map<Long, String> map, String val) {
        for (long key : map.keySet()) {
            if (map.get(key).equals(val))
                return key;
        }
        return -1;
    }

    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static int getPX(Context context, int dp) {
        // margin in dips
        //int dpValue = 5; // margin in dips
        float d = context.getResources().getDisplayMetrics().density;
        int margin = (int) (dp * d);
        return margin;
    }

    public static void hideSoftKeyboard(Activity context) {
        //Hides the SoftKeyboard
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = context.getCurrentFocus();
        if(currentFocus != null){
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static int getArrayIndex(long[] arr, long value) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == value) return i;
        return -1;
    }

    public static String getAllHtmlFromDefinitionList(List<Definition> defList){
        if(defList.size() <= 1){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for(Definition def : defList){
            sb.append("<div>");
            sb.append(def.getDisplayHtml());
            sb.append("</div>");
            sb.append("<br/>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    public static boolean containsTranslationField(OutputPlanPOJO outputPlan){
        if(outputPlan == null){
            return false;
        }
        Map<String, String> map = outputPlan.getFieldsMap();
        for(String key : map.keySet()){
            if(map.get(key).equals("句子翻译")){
                return true;
            }
        }
        return false;
    }

    public static int getResIdFromAttribute(final Activity activity,final int attr)
    {
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                activity.getApplicationInfo().theme, new int[] {attr});
        int attributeResourceId = a.getResourceId(0, 0);
        a.recycle();
        return attributeResourceId;
    }

    public static int getResIdFromAttribute(final Context context,final int attr)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                context.getApplicationInfo().theme, new int[] {attr});
        int attributeResourceId = a.getResourceId(0, 0);
        a.recycle();
        return attributeResourceId;
    }

    public static boolean deleteNote(Context context, long noteid){
        ContentResolver cr = context.getContentResolver();
        if(cr == null) return false;
        Uri noteUri = Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteid));
        int i = cr.delete(noteUri, null, null);
        if(i == 1) {
            return true;
        }
        else{
            return false;
        }
    }

    public static String renderTmpl(String tmpl, Map<String, String> dataMap) {
        tmpl = tmpl.trim();
        for (String key : dataMap.keySet()) {
            tmpl = tmpl.replace("{{" + key + "}}", dataMap.get(key));
        }
        return tmpl;
    }

    public static String keyCleanup(String key) {
        return key.trim().replaceAll("[,.!?()\"'“”’？]", "").toLowerCase();
    }

    public static void showMessage(Context context, String message){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                }).show();
    }

    public static String fromTagSetToString(Set<String> tagSet){
        String tags = "";
        int size = tagSet.size();
        int i = 0;
        for(String tag : tagSet){
            if(i < size - 1){
                tags = tags + tag.trim() + ",";
            }else{
                tags = tags + tag.trim();
            }
            i ++;
        }
        return tags;
    }

    public static Set<String> fromStringToTagSet(String s){
        Set<String> set = new HashSet<>();
        for(String item : s.split(",")){
            set.add(item);
        }
        return set;
    }

    public static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static String getSpecificFileName(String key) {
        try {
            String text = key.trim().replaceAll("[^\\w\\d]+", "_").replaceFirst("^_", "");
            int start = 0;
            int end = text.length()>18? 18 : text.length();
            return text.substring(start, end) +
                    "_" + Utils.getRandomHexString(8);

        } catch (Exception e) {
            Log.e("Utils class", e.getMessage());
            return "error_is_in_a_method_called_getSpecificFileName";
        }
    }

    //update some funs.
    // Regex pattern used in removing tags from text before checksum
    private static final Pattern stylePattern = Pattern.compile("(?s)<style.*?>.*?</style>");
    private static final Pattern scriptPattern = Pattern.compile("(?s)<script.*?>.*?</script>");
    private static final Pattern tagPattern = Pattern.compile("<.*?>");
    private static final Pattern imgPattern = Pattern.compile("<img src=[\\\"']?([^\\\"'>]+)[\\\"']? ?/?>");
    private static final Pattern htmlEntitiesPattern = Pattern.compile("&#?\\w+;");

    static String joinFields(String[] list) {
        return list != null ? TextUtils.join("\u001f", list): null;
    }


    static String[] splitFields(String fields) {
        return fields != null? fields.split("\\x1f", -1): null;
    }

    static String joinTags(Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        for (String t : tags) {
            t.replaceAll(" ", "_");
        }
        return TextUtils.join(" ", tags);
    }

    static String[] splitTags(String tags) {
        if (tags == null) {
            return null;
        }
        return tags.trim().split("\\s+");
    }

    static Long fieldChecksum(String data) {
        data = stripHTMLMedia(data);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(data.getBytes("UTF-8"));
            BigInteger biginteger = new BigInteger(1, digest);
            String result = biginteger.toString(16);

            // pad checksum to 40 bytes, as is done in the main AnkiDroid code
            if (result.length() < 40) {
                String zeroes = "0000000000000000000000000000000000000000";
                result = zeroes.substring(0, zeroes.length() - result.length()) + result;
            }

            return Long.valueOf(result.substring(0, 8), 16);
        } catch (Exception e) {
            // This is guaranteed to never happen
            throw new IllegalStateException("Error making field checksum with SHA1 algorithm and UTF-8 encoding", e);
        }
    }

    /**
     * Strip HTML but keep media filenames
     */
    private static String stripHTMLMedia(String s) {
        Matcher imgMatcher = imgPattern.matcher(s);
        return stripHTML(imgMatcher.replaceAll(" $1 "));
    }

    private static String stripHTML(String s) {
        Matcher htmlMatcher = stylePattern.matcher(s);
        s = htmlMatcher.replaceAll("");
        htmlMatcher = scriptPattern.matcher(s);
        s = htmlMatcher.replaceAll("");
        htmlMatcher = tagPattern.matcher(s);
        s = htmlMatcher.replaceAll("");
        return entsToTxt(s);
    }

    public static String stripJSAndStyle(String s) {
        Matcher htmlMatcher = stylePattern.matcher(s);
        s = htmlMatcher.replaceAll("");
        htmlMatcher = scriptPattern.matcher(s);
        s = htmlMatcher.replaceAll("");
        return entsToTxt(s);
    }

    /**
     * Takes a string and replaces all the HTML symbols in it with their unescaped representation.
     * This should only affect substrings of the form &something; and not tags.
     * Internet rumour says that Html.fromHtml() doesn't cover all cases, but it doesn't get less
     * vague than that.
     * @param html The HTML escaped text
     * @return The text with its HTML entities unescaped.
     */
    @SuppressWarnings("deprecation")
    private static String entsToTxt(String html) {
        // entitydefs defines nbsp as \xa0 instead of a standard space, so we
        // replace it first
        html = html.replace("&nbsp;", " ");
        Matcher htmlEntities = htmlEntitiesPattern.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (htmlEntities.find()) {
            // Html.fromHtml(String) is deprecated but it's replacement isn't available till API24
            htmlEntities.appendReplacement(sb, Html.fromHtml(htmlEntities.group()).toString());
        }
        htmlEntities.appendTail(sb);
        return sb.toString();
    }

    /**
     * Some File methods
     * @param context
     * @return
     */
    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }

    public static void cleanVideoCacheDir(Context context) throws IOException {
        File videoCacheDir = getVideoCacheDir(context);
        cleanDirectory(videoCacheDir);
    }

    private static void cleanDirectory(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        File[] contentFiles = file.listFiles();
        if (contentFiles != null) {
            for (File contentFile : contentFiles) {
                delete(contentFile);
            }
        }
    }

    private static void delete(File file) throws IOException {
        if (file.isFile() && file.exists()) {
            deleteOrThrow(file);
        } else {
            cleanDirectory(file);
            deleteOrThrow(file);
        }
    }

    private static void deleteOrThrow(File file) throws IOException {
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new IOException(String.format("File %s can't be deleted", file.getAbsolutePath()));
            }
        }
    }
}
