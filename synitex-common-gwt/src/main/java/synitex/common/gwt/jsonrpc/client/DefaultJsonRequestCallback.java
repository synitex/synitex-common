package synitex.common.gwt.jsonrpc.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import synitex.common.gwt.util.client.ExceptionsHandler;
import synitex.common.gwt.util.client.GwtHelper;

public abstract class DefaultJsonRequestCallback implements synitex.common.gwt.jsonrpc.client.JsonRequestCallback {

    @Override
    public void onApplicationError(String errorCode, String errorMessage, String errorDetails, JSONObject result) {
        String message = GwtHelper.isNotEmpty(errorMessage)
                ? errorMessage
                : "Sorry, we expirienced an error. Please try later.";
        Window.alert(message);
    }

    @Override
    public void onRequestError(RequestException ex) {
        ExceptionsHandler.get().onUncaughtException(ex);
    }

    @Override
    public void onGenericRequestError(Request request, Throwable exception) {
        ExceptionsHandler.get().onUncaughtException(exception);
    }

    @Override
    public void onResponseParseError(Throwable ex) {
        ExceptionsHandler.get().onUncaughtException(ex);
    }

}
