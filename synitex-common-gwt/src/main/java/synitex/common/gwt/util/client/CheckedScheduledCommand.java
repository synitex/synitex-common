package synitex.common.gwt.util.client;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public abstract class CheckedScheduledCommand implements ScheduledCommand {

    public abstract void executeChecked();

    @Override
    public void execute() {
        try {
            executeChecked();
        } catch (Exception ex) {
            synitex.common.gwt.util.client.ExceptionsHandler.get().handleExceptionFromScheduller(ex);
            throw new RuntimeException(ex);
        }
    }
}
