package synitex.common.gwt.util.client;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import synitex.common.gwt.util.client.GwtJsonHelper.ValueParser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class IncrementalJsonArrayParser<T> {

    private static final Logger log = Logger.getLogger(IncrementalJsonArrayParser.class.getName());

    private static final int defaultStep = 10;

    private final JSONObject json;
    private final String param;
    private final ValueParser<T> parser;

    private int step = defaultStep;
    private String statusMessage = null;
    private JSONArray array;
    private int size;
    private Callback<T> callback;
    private Duration duration;

    public IncrementalJsonArrayParser(JSONObject json, String param, ValueParser<T> parser) {
        this.json = json;
        this.param = param;
        this.parser = parser;
    }

    public IncrementalJsonArrayParser<T> setStep(int step) {
        this.step = step;
        return this;
    }

    public IncrementalJsonArrayParser<T> setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public void parse(Callback<T> callback) {
        if(GwtHelper.isNotEmpty(statusMessage)) {
            EventBus.get().fire(new synitex.common.gwt.util.client.StatusGwtEvent(statusMessage));
        }

        duration = new Duration();

        JSONValue value = json.get(param);
        this.array = value.isArray();
        this.size = array.size();
        this.callback = callback;

        Scheduler.get().scheduleIncremental(new ParseCommand());
    }

    private int getStepSize() {
        return step;
    }

    private void setResult(List<T> result) {

        int elapsedMilis = duration.elapsedMillis();

        if(GwtHelper.isNotEmpty(statusMessage)) {
            EventBus.get().fire(new synitex.common.gwt.util.client.StatusHideGwtEvent());
        }

        callback.parsed(result, new ParseStatistics(elapsedMilis));

    }

    // -----------------------------------------------------------

    private class ParseCommand extends CheckedRepeatingCommand {

        private int i = 0;
        private List<T> res = new ArrayList<T>();

        public ParseCommand() {

        }

        @Override
        public boolean executeChecked() {
            int step = 0;
            while(i < size) {
                JSONValue item = array.get(i);
                if (item != null) {
                    T r = parser.parse(item);
                    if (r != null) {
                        res.add(r);
                    }
                }

                i++;
                step++;
                if(step > getStepSize()) {
                    return true;
                }
            }

            setResult(res);

            return false;
        }

    }

    public static interface Callback<T> {
        void parsed(List<T> result, ParseStatistics stats);
    }

    public static class ParseStatistics {

        private final int elapsed;

        public ParseStatistics(int elapsedMilis) {
            this.elapsed = elapsedMilis;
        }

        public int getElapsedMilis() {
            return elapsed;
        }
    }

}
