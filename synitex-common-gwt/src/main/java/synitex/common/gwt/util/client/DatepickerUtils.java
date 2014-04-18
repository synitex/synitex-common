package synitex.common.gwt.util.client;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Utilities to work with http://eternicode.github.img/bootstrap-datepicker/ from GWT.
 */
public class DatepickerUtils {

    private static final Logger log = Logger.getLogger(DatepickerUtils.class.getName());

    private static DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");

    public static native void setupDatepicker(String elemId) /*-{
        $wnd.jQuery("#" + elemId).datepicker({
            todayBtn: true,
            format: "mm/dd/yyyy",
            language: "no"
        });
    }-*/;

    public static Date getDate(InputElement el) {
        String value = el.getValue();
        if(GwtHelper.isEmpty(value)) {
            return null;
        }
        try {
            return dtf.parse(value);
        } catch (IllegalArgumentException ex) {
            synitex.common.gwt.util.client.ExceptionsHandler.get().handleExceptionSilently(ex);
            return null;
        }
    }

    public static void setDate(Date date, String inputElId) {
        JsDate d = JsDate.create(date.getTime());
        setDateImpl(d, inputElId);
    }

    private static native void setDateImpl(JsDate d, String inputElId) /*-{
        $wnd.jQuery("#" + inputElId).datepicker("setDate", d);
    }-*/;

}
