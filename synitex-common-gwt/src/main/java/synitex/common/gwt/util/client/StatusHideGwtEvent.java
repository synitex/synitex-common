package synitex.common.gwt.util.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import synitex.common.gwt.util.client.StatusHideGwtEvent.Handler;

public class StatusHideGwtEvent extends GwtEvent<Handler> {

    public static final Type<Handler> TYPE = new Type<Handler>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onHideStatus();
    }

    public static interface Handler extends EventHandler {
        void onHideStatus();
    }

}
