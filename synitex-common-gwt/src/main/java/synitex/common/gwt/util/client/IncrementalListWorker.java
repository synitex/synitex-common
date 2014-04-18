package synitex.common.gwt.util.client;

import com.google.gwt.core.client.Scheduler;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class IncrementalListWorker<T> {

    private static final Logger log = Logger.getLogger(IncrementalListWorker.class.getName());

    private static final int defaultStep = 10;

    private int step = defaultStep;
    private final Iterator<T> iterator;
    private Callback callback;
    private Worker<T> worker;
    private String statusMessage;

    public IncrementalListWorker(List<T> input) {
        this.iterator = input.iterator();
    }

    public IncrementalListWorker<T> setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public IncrementalListWorker<T> setStep(int step) {
        this.step = step;
        return this;
    }

    public void work(Worker<T> worker, Callback callback) {
        if(GwtHelper.isNotEmpty(statusMessage)) {
            EventBus.get().fire(new synitex.common.gwt.util.client.StatusGwtEvent(statusMessage));
        }
        this.callback = callback;
        this.worker = worker;
        Scheduler.get().scheduleIncremental(new WorkCommand());
    }

    private void finish() {
        if(GwtHelper.isNotEmpty(statusMessage)) {
            EventBus.get().fire(new synitex.common.gwt.util.client.StatusHideGwtEvent());
        }
        callback.done();
    }

    private class WorkCommand extends CheckedRepeatingCommand {

        @Override
        public boolean executeChecked() {
            int i = 0;
            while(iterator.hasNext())  {
                T value = iterator.next();
                worker.handle(value);
                i++;
                if(i > step) {
                    return true;
                }
            }
            finish();
            return false;
        }
    }

    public static interface Worker<T> {
        void handle(T object);
    }

    public static interface Callback {
        void done();
    }

}
