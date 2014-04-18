package synitex.common.gwt.util.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

public class JsObject extends JavaScriptObject {

    private static final Logger log = Logger.getLogger(JsObject.class.getName());

    protected JsObject() {

    }

    protected final boolean parseBoolean(String param) {
        return Boolean.TRUE.equals(getBoolean(param));
    }

    protected final String parseString(String param) {
        return getStringAsString(param);
    }

    protected final int parseInt(String param) {
        Integer value = parseInteger(param);
        return value == null ? 0 : value.intValue();
    }

    protected final Integer parseInteger(String param) {
        String s = getNumberAsString(param);
        if("null".equals(s))  {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            log.log(WARNING, "Failed to parse Integer from = " + s, ex);
            return null;
        }
    }

    protected final Long parseLong(String param) {
        String s = getNumberAsString(param);
        if("null".equals(s))  {
            return null;
        }
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException ex) {
            log.log(WARNING, "Failed to parse Long from = " + s, ex);
            return null;
        }
    }

    protected final Date parseDate(String param) {
        String s = getNumberAsString(param);
        if("null".equals(s)) {
            return null;
        } else {
            try {
                Long timestamp = Long.valueOf(s);
                return new Date(timestamp);
            } catch (NumberFormatException ex) {
                log.log(WARNING, "Failed to parse Date from = " + s, ex);
                return null;
            }
        }
    }

    protected final Double parseDouble(String param) {
        String s = getNumberAsString(param);
        if("null".equals(s))  {
            return null;
        }
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException ex) {
            log.log(WARNING, "Failed to parse Double from = " + s, ex);
            return null;
        }
    }

    protected final List<String> parseStrings(String param) {
        JsArrayString strings = getNativeStrings(param);
        if(strings == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < strings.length(); i++) {
            list.add(strings.get(i));
        }
        return list;
    }

    protected final native JsArrayString getNativeStrings(String param) /*-{
        return this[param];
    }-*/;

    protected final native String getNumberAsString(String param) /*-{
        return this[param] == null ? "null" : this[param].toString();
    }-*/;

    protected final native String getStringAsString(String param) /*-{
        return this[param] == null ? "" : this[param].toString();
    }-*/;

    protected final native JsObject getJsObject(String param) /*-{
        return this[param];
    }-*/;

    protected final native boolean getBoolean(String param) /*-{;
        return this[param] == null ? false : this[param];
    }-*/;
}
