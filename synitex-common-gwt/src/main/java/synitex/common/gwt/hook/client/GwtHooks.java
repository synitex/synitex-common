package synitex.common.gwt.hook.client;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import synitex.common.gwt.hook.shared.HookConstants;
import synitex.common.gwt.util.client.CheckedScheduledCommand;
import synitex.common.gwt.util.client.GwtHelper;

public class GwtHooks {

    private static GwtHooks INSTANCE;

    private final Function<String, GwtHook> hooksProvider;

    public static void setup(Function<String, GwtHook> hooksProvider) {
        INSTANCE = new GwtHooks(hooksProvider);
    }

    public static GwtHooks get() {
        return INSTANCE;
    }

    private GwtHooks(Function<String, GwtHook> hooksProvider) {
        this.hooksProvider = hooksProvider;
    }

    public void activate() {
        activate(null);
    }

    public <DATA> void activate(final String hookId, final GwtHookInjector injector) {
        Scheduler.get().scheduleDeferred(new CheckedScheduledCommand() {
            @Override
            public void executeChecked() {
                activateImpl(hookId, injector);
            }
        });
    }

    public <DATA> void activate(final Supplier<DATA> dataSupplier) {
        Scheduler.get().scheduleDeferred(new CheckedScheduledCommand() {
            @Override
            public void executeChecked() {
                activate(null, dataSupplier);
            }
        });
    }

    public <DATA> void activate(final String hookId, final Supplier<DATA> dataSupplier) {
        Scheduler.get().scheduleDeferred(new CheckedScheduledCommand() {
            @Override
            public void executeChecked() {
                activateImpl(hookId, new GwtHookInjector() {
                    @Override
                    public void inject(GwtHook hook) {
                        if(dataSupplier != null && hook instanceof DataGwtHook) {
                            ((DataGwtHook)hook).setDataSupplier(dataSupplier);
                        }
                    }
                });
            }
        });
    }

    protected <DATA> void activateImpl(final String hookIdToActivate, final GwtHookInjector injector) {
        BodyElement body = Document.get().getBody();
        NodeList<Element> hookElements = body.getElementsByTagName(HookConstants.HOOK_TAG);
        for (int i = 0; i < hookElements.getLength(); i++) {
            Element hookElem = hookElements.getItem(i);
            String hookId = hookElem.getAttribute("id");

            if(GwtHelper.isEmpty(hookIdToActivate) || hookIdToActivate.equals(hookId)) {

                String hookData = hookElem.getInnerHTML();

                GwtHook hook = hooksProvider.apply(hookId);

                if (hook != null) {

                    if(injector != null) {
                        injector.inject(hook);
                    }

                    hook.activateHook(hookData, (com.google.gwt.user.client.Element) hookElem.getParentElement().cast());
                }

            }
        }
    }

    public static interface GwtHookInjector {

        void inject(GwtHook hook);

    }

}
