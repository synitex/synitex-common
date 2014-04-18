package synitex.common.gwt.hook.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import synitex.common.gwt.util.client.GwtHelper;

public abstract class AbstractGwtHook implements synitex.common.gwt.hook.client.GwtHook {

    public static final String ATTR_CLICK_ID = "ss-id";
    public static final String ATTR_CLICK_DATA = "ss-data";

    public abstract void onClickHandled(String ssId, String ssData);

    public void registerClickHandler(Element el) {
        GwtHelper.sinkEvents(el, Event.ONCLICK);
        GwtHelper.addEventListener(el, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTypeInt() == Event.ONCLICK) {
                    processClickEvent(event);
                }
            }
        });
    }

    private void processClickEvent(Event event) {
        EventTarget eventTarget = event.getEventTarget();
        if(Element.is(eventTarget)) {
            Element el = Element.as(eventTarget);
            Element target = findFirstParentElementWithSSIdAttr(el);
            if(target != null) {
                onClickHandled(target.getAttribute(ATTR_CLICK_ID), target.getAttribute(ATTR_CLICK_DATA));
            }
        }
    }

    private Element findFirstParentElementWithSSIdAttr(Element el) {
        if(el == null) {
            return null;
        }
        String ssId = el.getAttribute(ATTR_CLICK_ID);
        if(GwtHelper.isNotEmpty(ssId)) {
            return el;
        } else {
            return findFirstParentElementWithSSIdAttr(el.getParentElement());
        }
    }

}
