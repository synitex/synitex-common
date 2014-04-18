package synitex.common.gwt.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import synitex.common.gwt.util.shared.Locale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Can be used only on client side! Do not even try to use in server side!
 * @author sergey.sinica
 *
 */
public class GwtHelper {

    private static final Logger log = Logger.getLogger(GwtHelper.class.getName());

    private static long idCounter = 10;
    private static Storage localStorage;
    private static boolean isLocalStorageLoaded = false;
    private static boolean isDev = false;
    private static String currentLang;

    private GwtHelper() {
    }

    public static String safeString(String html) {
        if (html == null || "".equals(html)) {
            return "";
        }
        SafeHtml safe = SimpleHtmlSanitizer.sanitizeHtml(html);
        return safe.asString();
    }

    public static String nextId() {
        return "id-" + nextIdLong();
    }

    private static long nextIdLong() {
        idCounter += 1;
        return idCounter;
    }

    public static boolean isDevelopmentMode() {
        return isDev || !GWT.isScript();
    }

    public static EventListener addEventListener(com.google.gwt.dom.client.Element element, final EventListener listener) {
        return addEventListener((Element) element.cast(), listener);
    }

    public static EventListener addEventListener(Element element, final EventListener listener) {
        final EventListener currentListener = DOM.getEventListener(element);
        if (currentListener == null) {
            DOM.setEventListener(element, new CompositeEventListener(listener));
        } else if (currentListener instanceof CompositeEventListener) {
            ((CompositeEventListener) currentListener).addEventListener(listener);
        } else {
            DOM.setEventListener(element, new CompositeEventListener(currentListener, listener));
        }
        return listener;
    }

    public static void removeEventListener(Element element, final EventListener listener) {
        if (listener == null) {
            return;
        }
        final EventListener currentListener = DOM.getEventListener(element);
        if (currentListener == null) {
        } else if (currentListener instanceof CompositeEventListener) {
            CompositeEventListener compositeEventListener = (CompositeEventListener) currentListener;
            compositeEventListener.removeEventListener(listener);
            if (compositeEventListener.getSize() == 0) {
                DOM.setEventListener(element, null);
            }
        } else {
            DOM.setEventListener(element, null);
        }
    }

    public static String defaultIfEmpty(String value, String defaultValue) {
        return GwtHelper.isEmpty(value) ? defaultValue : value;
    }


    static class CompositeEventListener implements EventListener {
        private List<EventListener> listeners;
        private List<EventListener> listenersToRemove;

        CompositeEventListener(EventListener... eventListeners) {
            listeners = new ArrayList<EventListener>(2);
            Collections.addAll(listeners, eventListeners);
        }

        void addEventListener(EventListener listener) {
            listeners.add(listener);
        }

        void removeEventListener(EventListener listener) {
            if (listenersToRemove == null) {
                listenersToRemove = new ArrayList<EventListener>(2);
            }
            listenersToRemove.add(listener);
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (listenersToRemove != null) {
                listeners.removeAll(listenersToRemove);
                listenersToRemove = null;
            }
            for (EventListener l : listeners) {
                l.onBrowserEvent(event);
            }
        }

        public int getSize() {
            return listeners.size() - (listenersToRemove != null ? listenersToRemove.size() : 0);
        }
    }

    public static void sinkEvents(Element element, int eventBits) {
        int value = eventBits | DOM.getEventsSunk(element);
        DOM.sinkEvents(element, value);
    }

    public static void sinkEvents(com.google.gwt.dom.client.Element domEl, int eventBits) {
        Element el = domEl.cast();
        sinkEvents(el, eventBits);
    }

    public static String assignId(Element el) {
        String id = nextId();
        el.setId(id);
        return id;
    }

    public static String assignId(com.google.gwt.dom.client.Element el) {
        String id = nextId();
        el.setId(id);
        return id;
    }

    public static String assignId(Widget el) {
        String id = nextId();
        el.getElement().setId(id);
        return id;
    }

    public static String getIdFrom(Event event) {
        Element el = getElFrom(event);
        if (el == null) {
            return null;
        }
        return el.getId();
    }

    public static Element getElFrom(Event event) {
        EventTarget target = event.getEventTarget();
        if (target == null) {
            return null;
        }
        return target.cast();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Converts string to minutes.
     * @param str (00:24, 1:01, 13:05)
     * @return minutes count
     */
    public static int stringToTime(String str) {
        if (str == null || "".equals(str)) {
            return -1;
        }
        int l = str.length();
        if (l < 4 || l > 5) {
            return -1;
        }

        int idx = str.indexOf(":");
        if (idx < 1) {
            return -1;
        }

        String hs = str.substring(0, idx);
        String ms = str.substring(idx + 1);
        if(ms.length() != 2) {
            return -1;
        }
        if (ms.startsWith("0")) {
            ms = ms.substring(1);
        }

        try {
            int hour = Integer.valueOf(hs);
            int min = Integer.valueOf(ms);

            if (hour < 1 || hour > 24) {
                return -1;
            }

            if (min < 0 || min > 59) {
                return -1;
            }

            int time = hour * 60 + min;
            return time;

        } catch (Exception e) {
            return -1;
        }

    }

    public static String timeToString(int time) {
        int min = time % 60;
        int hour = time / 60;
        return hour + ":" + to2symbolString(min);
    }

    public static String timeZoneToString(int offset) {
        String s = append0prefix(offset);
        return offset > 0 ? "UTC: +" + s : "UTC: " + s;
    }

    public static String append0prefix(int a) {
        if (a < 10) {
            return "0" + a;
        } else {
            return String.valueOf(a);
        }
    }

    public static String to2symbolString(int a) {
        if (a < 10) {
            return "0" + a;
        } else {
            return String.valueOf(a);
        }
    }

    public static void clearElement(Element el) {
        NodeList<Node> nodes = el.getChildNodes();
        if (nodes == null || nodes.getLength() == 0) {
            return;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            el.removeChild(nodes.getItem(i));
        }
    }

    public static void clearElement(com.google.gwt.dom.client.Element el) {
        clearElement((Element) el.cast());
    }

    /**
     * Changes current locale and refreshes browsers page.
     * @param locale
     */
    public static void changeCurrentLocaleAndRefreshPage(String locale) {
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        // remove any previous set locale
        urlBuilder.removeParameter("locale");
        // set new locale
        urlBuilder.setParameter("locale", locale);
        String url = urlBuilder.buildString();
        // reload the page with new locale
        Window.Location.replace(url);
    }

    public static String getCurrentLanguage() {
        String cookieslang = Cookies.getCookie(Locale.CURRENT_LANG_COOKIE);
        if (cookieslang != null) {
            return cookieslang;
        } else if (currentLang != null) {
            // if cookies are not supported by browser
            return currentLang;
        } else {
            return "en";
        }
        //LocaleInfo currentLocale = LocaleInfo.getCurrentLocale();
        //String locale = currentLocale.getLocaleName();
        //return locale;
    }

    public static void saveLanguageToCookies(String lang) {
        currentLang = lang; // in case if cookies are not supported by browser
        Date currentTime = new Date();
        long time = currentTime.getTime();
        time += 5184000000L; // 60 days
        Cookies.setCookie(Locale.CURRENT_LANG_COOKIE, lang, new Date(time));
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T, B> boolean isEmpty(Map<T, B> map) {
        return map == null || map.size() == 0;
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }

    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.size() == 0;
    }

    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.size() == 0;
    }

    public static Storage getLocalStorage() {
        if (!isLocalStorageLoaded) {
            isLocalStorageLoaded = true;
            localStorage = Storage.getLocalStorageIfSupported();
        }
        return localStorage;
    }

    public static String formatTimeNoYear(long time) {
        DateTimeFormat format = DateTimeFormat.getFormat("dd/MM");
        return format.format(new Date(time));
    }

    public static String formatTimeWithYear(long time) {
        DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy");
        return format.format(new Date(time));
    }

    public static String formatTimeHourMin(long time) {
        DateTimeFormat format = DateTimeFormat.getFormat("hh:mm");
        return format.format(new Date(time));
    }

    public static String formatTimeDayHourMin(long time) {
        DateTimeFormat format = DateTimeFormat.getFormat("dd/MM hh:mm");
        return format.format(new Date(time));
    }

    public static void setDev(boolean value) {
        isDev = value;
    }

    public static String getStringValue(InputElement el) {
        String value = el.getValue();
        return GwtHelper.isEmpty(value) ? "" : value.trim();
    }

    public static String getStringValue(TextAreaElement el) {
        String value = el.getValue();
        return GwtHelper.isEmpty(value) ? "" : value.trim();
    }

    public static Integer getIntegerValue(InputElement el) {
        String value = el.getValue();
        if(GwtHelper.isEmpty(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Double getDoubleValue(InputElement el) {
        String value = el.getValue();
        if(GwtHelper.isEmpty(value)) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Long getLongValue(InputElement el) {
        String value = el.getValue();
        if(GwtHelper.isEmpty(value)) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static void setStringValue(String value, InputElement el) {
        if(GwtHelper.isEmpty(value)) {
            el.setValue("");
        } else {
            el.setValue(value);
        }
    }

    public static void setStringValue(String value, TextAreaElement el) {
        if(GwtHelper.isEmpty(value)) {
            el.setValue("");
        } else {
            el.setValue(value);
        }
    }

    public static void setIntValue(int value, InputElement el) {
        if(value == 0) {
            el.setValue("");
        } else {
            el.setValue(String.valueOf(value));
        }
    }

    public static int getIntValue(InputElement el) {
        Integer value = getIntegerValue(el);
        return value == null ? 0: value.intValue();
    }

    public static int getIntValue(InputElement el, int defaultValue) {
        Integer value = getIntegerValue(el);
        return value == null ? defaultValue: value.intValue();
    }

    public static void logPosition(com.google.gwt.dom.client.Element element) {
        Element el = element.cast();
        logPosition(el);
    }

    public static void logPosition(Element el) {
        log.fine("------ Position of " + el.getId() + ", " + el.getStyle() + " -----------");
        log.fine("absolute: "
                + el.getAbsoluteLeft()
                + ", "
                + el.getAbsoluteTop()
                + ", "
                + el.getAbsoluteRight()
                + ", "
                + el.getAbsoluteBottom()
        );
        log.fine("client width / client height: " + el.getClientWidth() + " / " + el.getClientHeight());
        log.fine("offset left / offset top: " + el.getOffsetLeft() + " / " + el.getOffsetTop());
        log.fine("offset height / offset width: " + el.getOffsetHeight() + " / " + el.getOffsetWidth());
        log.fine("scroll left / scroll top: " + el.getScrollLeft() + " / " + el.getScrollTop());
        log.fine("scroll height / scroll width: " + el.getScrollHeight() + " / " + el.getScrollWidth());
    }

    public static String buildUrl(String url) {
        if(isDevelopmentMode()) {
            String delim = url.indexOf("?") > 0 ? "&" : "?";
            url += delim + "gwt.codesvr=127.0.0.1:9997";
            return url;
        } else {
            return url;
        }
    }

    public static int indexOf(String value, ListBox list) {
        int idx = -1;
        for(int i = 0; i < list.getItemCount(); i++) {
            if(list.getValue(i).equals(value)) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    public static boolean setSelected(String value, ListBox list) {
        int idx = indexOf(value, list);
        if(idx > -1) {
            list.setSelectedIndex(idx);
            return true;
        } else {
            return false;
        }
    }
}