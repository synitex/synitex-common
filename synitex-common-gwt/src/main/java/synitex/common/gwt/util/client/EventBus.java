package synitex.common.gwt.util.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;

import java.util.logging.Logger;

public class EventBus {

    private static final Logger log = Logger.getLogger(EventBus.class.getName());

    private static final EventBus INSTANCE = new EventBus();

    public static EventBus get() {
        return INSTANCE;
    }

    private final SimpleEventBus bus;

    private EventBus() {
        bus = new SimpleEventBus();
    }

    public void fire(GwtEvent event) {
        log.info("Event fired: " + event.getClass().getName());
        bus.fireEvent(event);
    }

    public <H extends EventHandler> void register(GwtEvent.Type<H> type, H handler) {
        log.info("Event handler registered: " + handler.getClass().getName());
        bus.addHandler(type, handler);
    }

}
