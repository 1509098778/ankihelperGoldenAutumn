package com.mmjang.ankihelper.data.dict;

import com.mmjang.ankihelper.domain.PronounceManager;
import com.mmjang.ankihelper.util.RegexUtil;
import com.mmjang.ankihelper.util.StringUtil;
import com.mmjang.ankihelper.util.Trace;

import java.util.HashMap;

//Dictionary language type
public class DictLanguageType {
    public static final int NAN = -1;
    public static final int ZHO = 1;
    public static final int RUS = 2;
    public static final int ENG = 4;
    public static final int FRA = 8;
    public static final int DEU = 16;
    public static final int SPA = 32;
    public static final int JPN = 64;
    public static final int KOR = 128;
    public static final int THA = 256;
    public static final int ALL = 512;
    

    public static boolean isExistLT(int type) {
        switch(type) {
            case ZHO:
            case RUS:
            case ENG:
            case FRA:
            case DEU:
            case SPA:
            case JPN:
            case KOR:
            case THA:
            case ALL:
                return true;
            default:
                return false;
        }
    }

//    public static int getLangIndex(int type) {
//        switch(type) {
//            case ZHO:
//                return 0;
//            case RUS:
//                return 1;
//            case ENG:
//                return 2;
//            case FRA:
//                return 3;
//            case DEU:
//                return 4;
//            case SPA:
//                return 5;
//            case JPN:
//                return 6;
//            case KOR:
//                return 7;
//            case THA:
//                return 8;
//            default:
//                return 9;
//        }
//    }

    public static String[] getLanguageNameList() {

        String[] dictSoundNames = new String[]{
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("CHN"), "汉"),//0
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("RUS"), "俄"),//1
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("GBR"), "英"),//2
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("FRA"), "法"),//3
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("DEU"), "德"),//4
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("ESP"), "西"),//5
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("PRK"), "朝鲜"),//6
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("JPN"), "日"),//7
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("THA"), "泰"),//8
            String.format("%s %s", DictLanguageType.getFlagByCountryISO3("ALL"), "未定")//9
        };
        return dictSoundNames;
    }

//    public static String[] getLanguageNameList() {
//
//        String[] dictSoundNames = new String[]{
//                DictLanguageType.getFlagByCountryISO3("CHN"),//0
//                DictLanguageType.getFlagByCountryISO3("RUS"),//1
//                DictLanguageType.getFlagByCountryISO3("GBR"),//2
//                DictLanguageType.getFlagByCountryISO3("FRA"),//3
//                DictLanguageType.getFlagByCountryISO3("DEU"),//4
//                DictLanguageType.getFlagByCountryISO3("ESP"),//5
//                DictLanguageType.getFlagByCountryISO3("PRK"),//6
//                DictLanguageType.getFlagByCountryISO3("JPN"),//7
//                DictLanguageType.getFlagByCountryISO3("THA"),//8
//                DictLanguageType.getFlagByCountryISO3("ALL")//9
//        };
//        return dictSoundNames;
//    }


    public static String getLanguageISO3ByLTId(int languageType) {
        HashMap<Integer, String> ltMap = new HashMap<>();
        ltMap.put(DictLanguageType.ZHO, "zho");
        ltMap.put(DictLanguageType.RUS, "rus");
        ltMap.put(DictLanguageType.ENG, "eng");
        ltMap.put(DictLanguageType.FRA, "fra");
        ltMap.put(DictLanguageType.DEU, "deu");
        ltMap.put(DictLanguageType.SPA, "spa");
        ltMap.put(DictLanguageType.JPN, "jpn");
        ltMap.put(DictLanguageType.KOR, "kor");
        ltMap.put(DictLanguageType.THA, "tha");
        //extend languauage iso3 by diy
        ltMap.put(DictLanguageType.ALL, "ALL");
        ltMap.put(DictLanguageType.ALL|ZHO, "ALL-zho");
        ltMap.put(DictLanguageType.ALL|RUS, "ALL-rus");
        ltMap.put(DictLanguageType.ALL|ENG, "ALL-eng");
        ltMap.put(DictLanguageType.ALL|FRA, "ALL-fra");
        ltMap.put(DictLanguageType.ALL|DEU, "ALL-deu");
        ltMap.put(DictLanguageType.ALL|SPA, "ALL-spa");
        ltMap.put(DictLanguageType.ALL|JPN, "ALL-jpn");
        ltMap.put(DictLanguageType.ALL|KOR, "ALL-kor");
        ltMap.put(DictLanguageType.ALL|THA, "ALL-tha");

        return ltMap.get(languageType);
    }

    public static int getLTIdByCountryISO2(String lang) {
        HashMap<String, Integer> ltMap = new HashMap<String, Integer>();
        ltMap.put("zh", ZHO);
        ltMap.put("ru", RUS);
        ltMap.put("en", ENG);
        ltMap.put("fr", FRA);
        ltMap.put("de", DEU);
        ltMap.put("es", SPA);
        ltMap.put("ja", JPN);
        ltMap.put("ko", KOR);
        ltMap.put("th", THA);
        ltMap.put("all", ALL);
        return ltMap.containsKey((String) lang) ? ltMap.get(lang) : ltMap.get("all");
    }

    public static int getLTIdByWord(String str) {
        str = str.trim();
        int type = ALL;
        if(str.equals(""))
            return type;

//        String[]  words = str.split(" ");
//        String word = "";
//        if(words.length>0)
//            word = words[0];
//        else
//            word = str;

        Trace.e("getLTIdByWord", str);
        //中文
        if (RegexUtil.isChinese(str.charAt(0))) {
            type = DictLanguageType.ZHO;
        }
        //俄语
        else if (RegexUtil.isRussian(str)) {
            type = DictLanguageType.RUS;
        }
        //英文
        else if (RegexUtil.isEnglish(str)) {
            type = DictLanguageType.ENG;
        }
        // 韩
        else if (RegexUtil.isKorean(str.charAt(0))) {
            type = DictLanguageType.KOR;
        }
        // 日
        else if (StringUtil.isJapanese(str)) {
            type = DictLanguageType.JPN;
        }
        // 泰
        else if (RegexUtil.isThai(str)) {
            type = DictLanguageType.THA;
        }

        return type;
    }

    public static String getFlagByCountryISO3(String country) {
        switch (country.toUpperCase()) {
//            case "ABW": return "🇦🇼";
//            case "AFG": return "🇦🇫";
//            case "AGO": return "🇦🇴";
//            case "AIA": return "🇦🇮";
//            case "ALA": return "🇦🇽";
//            case "ALB": return "🇦🇱";
//            case "AND": return "🇦🇩";
//            case "ARE": return "🇦🇪";
            case "ARG": return "🇦🇷";
//            case "ARM": return "🇦🇲";
//            case "ASM": return "🇦🇸";
//            case "ATA": return "🇦🇶";
//            case "ATF": return "🇹🇫";
//            case "ATG": return "🇦🇬";
            case "AUS": return "🇦🇺";
            case "AUT": return "🇦🇹";
//            case "AZE": return "🇦🇿";
//            case "BDI": return "🇧🇮";
            case "BEL": return "🇧🇪";
//            case "BEN": return "🇧🇯";
//            case "BFA": return "🇧🇫";
//            case "BGD": return "🇧🇩";
//            case "BGR": return "🇧🇬";
//            case "BHR": return "🇧🇭";
//            case "BHS": return "🇧🇸";
//            case "BIH": return "🇧🇦";
//            case "BLM": return "🇧🇱";
//            case "BLR": return "🇧🇾";
//            case "BLZ": return "🇧🇿";
//            case "BMU": return "🇧🇲";
//            case "BOL": return "🇧🇴";
//            case "BRA": return "🇧🇷";
//            case "BRB": return "🇧🇧";
//            case "BRN": return "🇧🇳";
//            case "BTN": return "🇧🇹";
//            case "BVT": return "🇧🇻";
//            case "BWA": return "🇧🇼";
//            case "CAF": return "🇨🇫";
            case "CAN": return "🇨🇦";
//            case "CCK": return "🇨🇨";
            case "CHE": return "🇨🇭";
//            case "CHL": return "🇨🇱";
            case "CHN": return "🇨🇳";
//            case "CIV": return "🇨🇮";
//            case "CMR": return "🇨🇲";
//            case "COD": return "🇨🇩";
//            case "COG": return "🇨🇬";
//            case "COK": return "🇨🇰";
            case "COL": return "🇨🇴";
//            case "COM": return "🇰🇲";
//            case "CPV": return "🇨🇻";
//            case "CRI": return "🇨🇷";
//            case "CUB": return "🇨🇺";
//            case "CXR": return "🇨🇽";
//            case "CYM": return "🇰🇾";
//            case "CYP": return "🇨🇾";
//            case "CZE": return "🇨🇿";
            case "DEU": return "🇩🇪";
//            case "DJI": return "🇩🇯";
//            case "DMA": return "🇩🇲";
//            case "DNK": return "🇩🇰";
//            case "DOM": return "🇩🇴";
//            case "DZA": return "🇩🇿";
//            case "ECU": return "🇪🇨";
//            case "EGY": return "🇪🇬";
//            case "ERI": return "🇪🇷";
//            case "ESH": return "🇪🇭";
            case "ESP": return "🇪🇸";
//            case "EST": return "🇪🇪";
//            case "ETH": return "🇪🇹";
//            case "FIN": return "🇫🇮";
//            case "FJI": return "🇫🇯";
//            case "FLK": return "🇫🇰";
            case "FRA": return "🇫🇷";
//            case "FRO": return "🇫🇴";
//            case "FSM": return "🇫🇲";
//            case "GAB": return "🇬🇦";
            case "GBR": return "🇬🇧";
//            case "GEO": return "🇬🇪";
//            case "GGY": return "🇬🇬";
//            case "GHA": return "🇬🇭";
//            case "GIB": return "🇬🇮";
//            case "GIN": return "🇬🇳";
//            case "GLP": return "🇬🇵";
//            case "GMB": return "🇬🇲";
//            case "GNB": return "🇬🇼";
//            case "GNQ": return "🇬🇶";
//            case "GRC": return "🇬🇷";
//            case "GRD": return "🇬🇩";
//            case "GRL": return "🇬🇱";
//            case "GTM": return "🇬🇹";
//            case "GUF": return "🇬🇫";
//            case "GUM": return "🇬🇺";
//            case "GUY": return "🇬🇾";
            case "HKG": return "🇭🇰";
//            case "HMD": return "🇭🇲";
//            case "HND": return "🇭🇳";
//            case "HRV": return "🇭🇷";
//            case "HTI": return "🇭🇹";
//            case "HUN": return "🇭🇺";
//            case "IDN": return "🇮🇩";
//            case "IMN": return "🇮🇲";
            case "IND": return "🇮🇳";
//            case "IOT": return "🇮🇴";
            case "IRL": return "🇮🇪";
//            case "IRN": return "🇮🇷";
//            case "IRQ": return "🇮🇶";
//            case "ISL": return "🇮🇸";
//            case "ISR": return "🇮🇱";
//            case "ITA": return "🇮🇹";
//            case "JAM": return "🇯🇲";
//            case "JEY": return "🇯🇪";
//            case "JOR": return "🇯🇴";
            case "JPN": return "🇯🇵";
//            case "KAZ": return "🇰🇿";
//            case "KEN": return "🇰🇪";
//            case "KGZ": return "🇰🇬";
//            case "KHM": return "🇰🇭";
//            case "KIR": return "🇰🇮";
//            case "KNA": return "🇰🇳";
            case "KOR": return "🇰🇷";
//            case "KWT": return "🇰🇼";
//            case "LAO": return "🇱🇦";
//            case "LBN": return "🇱🇧";
//            case "LBR": return "🇱🇷";
//            case "LBY": return "🇱🇾";
//            case "LCA": return "🇱🇨";
//            case "LIE": return "🇱🇮";
//            case "LKA": return "🇱🇰";
//            case "LSO": return "🇱🇸";
//            case "LTU": return "🇱🇹";
//            case "LUX": return "🇱🇺";
//            case "LVA": return "🇱🇻";
//            case "MAC": return "🇲🇴";
//            case "MAF": return "🇲🇫";
//            case "MAR": return "🇲🇦";
//            case "MCO": return "🇲🇨";
//            case "MDA": return "🇲🇩";
//            case "MDG": return "🇲🇬";
//            case "MDV": return "🇲🇻";
            case "MEX": return "🇲🇽";
//            case "MHL": return "🇲🇭";
//            case "MKD": return "🇲🇰";
//            case "MLI": return "🇲🇱";
//            case "MLT": return "🇲🇹";
//            case "MMR": return "🇲🇲";
//            case "MNE": return "🇲🇪";
//            case "MNG": return "🇲🇳";
//            case "MNP": return "🇲🇵";
//            case "MOZ": return "🇲🇿";
//            case "MRT": return "🇲🇷";
//            case "MSR": return "🇲🇸";
//            case "MTQ": return "🇲🇶";
//            case "MUS": return "🇲🇺";
//            case "MWI": return "🇲🇼";
//            case "MYS": return "🇲🇾";
//            case "MYT": return "🇾🇹";
//            case "NAM": return "🇳🇦";
//            case "NCL": return "🇳🇨";
//            case "NER": return "🇳🇪";
//            case "NFK": return "🇳🇫";
//            case "NGA": return "🇳🇬";
//            case "NIC": return "🇳🇮";
//            case "NIU": return "🇳🇺";
//            case "NLD": return "🇳🇱";
//            case "NOR": return "🇳🇴";
//            case "NPL": return "🇳🇵";
//            case "NRU": return "🇳🇷";
            case "NZL": return "🇳🇿";
//            case "OMN": return "🇴🇲";
//            case "PAK": return "🇵🇰";
//            case "PAN": return "🇵🇦";
//            case "PCN": return "🇵🇳";
//            case "PER": return "🇵🇪";
            case "PHL": return "🇵🇭";
//            case "PLW": return "🇵🇼";
//            case "PNG": return "🇵🇬";
//            case "POL": return "🇵🇱";
//            case "PRI": return "🇵🇷";
            case "PRK": return "🇰🇵";
//            case "PRT": return "🇵🇹";
//            case "PRY": return "🇵🇾";
//            case "PSE": return "🇵🇸";
//            case "PYF": return "🇵🇫";
//            case "QAT": return "🇶🇦";
//            case "REU": return "🇷🇪";
//            case "ROU": return "🇷🇴";
            case "RUS": return "🇷🇺";
//            case "RWA": return "🇷🇼";
//            case "SAU": return "🇸🇦";
//            case "SDN": return "🇸🇩";
//            case "SEN": return "🇸🇳";
            case "SGP": return "🇸🇬";
//            case "SGS": return "🇬🇸";
//            case "SHN": return "🇸🇭";
//            case "SJM": return "🇸🇯";
//            case "SLB": return "🇸🇧";
//            case "SLE": return "🇸🇱";
//            case "SLV": return "🇸🇻";
//            case "SMR": return "🇸🇲";
//            case "SOM": return "🇸🇴";
//            case "SPM": return "🇵🇲";
//            case "SRB": return "🇷🇸";
//            case "SSD": return "🇸🇸";
//            case "STP": return "🇸🇹";
//            case "SUR": return "🇸🇷";
//            case "SVK": return "🇸🇰";
//            case "SVN": return "🇸🇮";
//            case "SWE": return "🇸🇪";
//            case "SWZ": return "🇸🇿";
//            case "SYC": return "🇸🇨";
//            case "SYR": return "🇸🇾";
//            case "TCA": return "🇹🇨";
//            case "TCD": return "🇹🇩";
//            case "TGO": return "🇹🇬";
            case "THA": return "🇹🇭";
//            case "TJK": return "🇹🇯";
//            case "TKL": return "🇹🇰";
//            case "TKM": return "🇹🇲";
//            case "TLS": return "🇹🇱";
//            case "TON": return "🇹🇴";
//            case "TTO": return "🇹🇹";
//            case "TUN": return "🇹🇳";
//            case "TUR": return "🇹🇷";
//            case "TUV": return "🇹🇻";
            case "TWN": return "🇨🇳";
//            case "TZA": return "🇹🇿";
//            case "UGA": return "🇺🇬";
//            case "UKR": return "🇺🇦";
//            case "UMI": return "🇺🇲";
//            case "URY": return "🇺🇾";
            case "USA": return "🇺🇸";
//            case "UZB": return "🇺🇿";
//            case "VAT": return "🇻🇦";
//            case "VCT": return "🇻🇨";
//            case "VEN": return "🇻🇪";
//            case "VGB": return "🇻🇬";
//            case "VIR": return "🇻🇮";
//            case "VNM": return "🇻🇳";
//            case "VUT": return "🇻🇺";
//            case "WLF": return "🇼🇫";
//            case "WSM": return "🇼🇸";
//            case "YEM": return "🇾🇪";
            case "ZAF": return "🇿🇦";
//            case "ZMB": return "🇿🇲";
//            case "ZWE": return "🇿🇼";
            default:
                return "❓";
        }
    }

}
