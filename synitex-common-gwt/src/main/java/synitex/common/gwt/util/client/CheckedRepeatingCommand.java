package synitex.common.gwt.util.client;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public abstract class CheckedRepeatingCommand implements RepeatingCommand {

    public abstract boolean executeChecked();

    @Override
    public boolean execute() {
        try {
            return executeChecked();
        } catch (Exception ex) {
            synitex.common.gwt.util.client.ExceptionsHandler.get().handleExceptionFromScheduller(ex);
            throw new RuntimeException(ex);
        }
    }

}
