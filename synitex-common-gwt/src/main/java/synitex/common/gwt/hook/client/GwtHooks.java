package synitex.common.gwt.hook.client;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import synitex.common.gwt.hook.shared.HookConstants;
import synitex.common.gwt.util.client.CheckedScheduledCommand;
import synitex.common.gwt.util.client.GwtHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GwtHooks {

    private static GwtHooks INSTANCE;

    private Map<String, GwtHook> hooks = Maps.newHashMap();
    private Predicate<String> anyHookPredicate = new Predicate<String>() {
        @Override
        public boolean apply(String input) {
            return true;
        }
    };

    public static GwtHooks get() {
        return INSTANCE;
    }

    public void register(GwtHook hook) {
        hooks.put(hook.getHookId(), hook);
    }

    private GwtHooks() {}

    public void activate() {
        activate(null);
    }

    public <DATA> void activate(String hookId, GwtHookInjector injector) {
        BodyElement body = Document.get().getBody();
        Predicate<String> hookPredicate = genHookIdPredicate(hookId);
        activateChecked(body, injector, hookPredicate);
    }

    public <DATA> void activate(Supplier<DATA> dataSupplier) {
        BodyElement body = Document.get().getBody();
        GwtHookInjector injector = genHookInjector(dataSupplier);
        activateChecked(body, injector, anyHookPredicate);
    }

    public <DATA> void activate(String hookId, Supplier<DATA> dataSupplier) {
        BodyElement body = Document.get().getBody();
        GwtHookInjector injector = genHookInjector(dataSupplier);
        Predicate<String> hookPredicate = genHookIdPredicate(hookId);
        activateChecked(body, injector, hookPredicate);
    }

    public <DATA> void activate(String hookId, Element parentEl) {
        Predicate<String> hookPredicate = genHookIdPredicate(hookId);
        activateChecked(parentEl, null, hookPredicate);
    }

    public <DATA> void activate(String hookId, Element parentEl, Supplier<DATA> dataSupplier) {
        Predicate<String> hookPredicate = genHookIdPredicate(hookId);
        GwtHookInjector injector = genHookInjector(dataSupplier);
        activateChecked(parentEl, injector, hookPredicate);
    }

    public <DATA> void activateChecked(final Element el, final GwtHookInjector injector, final Predicate<String> activateHookPredicate) {
        Scheduler.get().scheduleDeferred(new CheckedScheduledCommand() {
            @Override
            public void executeChecked() {
                activateImpl(el, injector, activateHookPredicate);
            }
        });
    }

    private <DATA> void activateImpl(final Element el, final GwtHookInjector injector, Predicate<String> activateHookPredicate) {
        NodeList<Element> hookElements = el.getElementsByTagName(HookConstants.HOOK_TAG);
        for (int i = 0; i < hookElements.getLength(); i++) {
            Element hookElem = hookElements.getItem(i);
            String hookId = hookElem.getAttribute("id");
            boolean needToActivate = activateHookPredicate.apply(hookId);
            if(needToActivate) {
                String hookData = hookElem.getInnerHTML();
                GwtHook hook = hooks.get(hookId);
                if (hook != null) {
                    if(injector != null) {
                        injector.inject(hook);
                    }
                    hook.activateHook(hookData, (com.google.gwt.user.client.Element) hookElem.getParentElement().cast());
                }
            }
        }
    }

    private <DATA> GwtHookInjector genHookInjector(final Supplier<DATA> dataSupplier) {
        return new GwtHookInjector() {
            @Override
            public void inject(GwtHook hook) {
                if (dataSupplier != null && hook instanceof DataGwtHook) {
                    ((DataGwtHook) hook).setDataSupplier(dataSupplier);
                }
            }
        };
    }

    private Predicate<String> genHookIdPredicate(final String hookId) {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return GwtHelper.isEmpty(hookId) || hookId.equals(input);
            }
        };
    }

    // ----------------------------------------------------

    public static interface GwtHookInjector {

        void inject(GwtHook hook);

    }

}
