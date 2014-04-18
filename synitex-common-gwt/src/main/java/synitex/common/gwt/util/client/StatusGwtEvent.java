package synitex.common.gwt.util.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import synitex.common.gwt.util.client.StatusGwtEvent.Handler;

public class StatusGwtEvent extends GwtEvent<Handler> {

    public static final Type<Handler> TYPE = new Type<Handler>();

    private final String text;

    public StatusGwtEvent(String text) {
        this.text = text;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onShowStatus(text);
    }

    public static interface Handler extends EventHandler {
        void onShowStatus(String text);
    }

}
