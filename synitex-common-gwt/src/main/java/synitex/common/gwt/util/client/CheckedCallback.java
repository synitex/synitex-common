package synitex.common.gwt.util.client;

import com.google.gwt.core.client.Callback;

public abstract class CheckedCallback<RESULT> implements Callback<RESULT, Exception> {

    @Override
    public void onFailure(Exception reason) {
        synitex.common.gwt.util.client.ExceptionsHandler.get().handleExceptionSilently(reason);
    }

}
