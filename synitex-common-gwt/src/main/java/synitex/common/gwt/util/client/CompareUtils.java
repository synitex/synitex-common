package synitex.common.gwt.util.client;

import java.math.BigDecimal;
import java.util.Date;

public class CompareUtils {

    private CompareUtils() {

    }

    public static int compareIgnoreCase(String s1, String s2) {
        if(s1 != null && s2 != null) {
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        } else if(s2 == null) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int compare(String s1, String s2) {
        if(s1 != null && s2 != null) {
            return s1.compareTo(s2);
        } else if(s2 == null) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int compare(Date s1, Date s2) {
        if(s1 != null && s2 != null) {
            return s1.compareTo(s2);
        } else if(s2 == null) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int compare(BigDecimal s1, BigDecimal s2) {
        if(s1 != null && s2 != null) {
            return s1.compareTo(s2);
        } else if(s2 == null) {
            return 1;
        } else {
            return -1;
        }
    }
}
