package synitex.common.gwt.hook.client;

import com.google.gwt.user.client.Element;

public interface GwtHook {

    String getHookId();

    void activateHook(String hookData, Element hookWrapperEl);

}
