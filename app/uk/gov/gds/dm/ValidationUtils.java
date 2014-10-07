package uk.gov.gds.dm;

public class ValidationUtils {
    public static Boolean isWordCountLessThan(String s, int maxWords){
        return ((s == null) || (s.split("\\s").length <= maxWords));
    }

    public static Boolean stringArrayValuesAreNotTooLong(String[] sArr, int maxLength){
        if(sArr == null){
            return true;
        }

        for(String s : sArr){
            if(s.length() > maxLength){
                return false;
            }
        }
        return true;
    }
}
