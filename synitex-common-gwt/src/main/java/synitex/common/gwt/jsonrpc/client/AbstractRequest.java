package synitex.common.gwt.jsonrpc.client;

import com.google.common.base.Supplier;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import synitex.common.gwt.jsonrpc.shared.ReqConstants;
import synitex.common.gwt.util.client.EventBus;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.util.client.GwtJsonHelper;
import synitex.common.gwt.util.client.StatusHideGwtEvent;


public class AbstractRequest implements RequestCallback {

    private Supplier<String> data;
    private synitex.common.gwt.jsonrpc.client.JsonRequestCallback callback;
	private String url;
	private synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener lifeCycleListener;
    private Method method;
    private String urlPrefix = "/"; // by default relative to current context

	public void setLifeCycleListener(synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener lifeCycleListener) {
		this.lifeCycleListener = lifeCycleListener;
	}

    AbstractRequest(String url, Supplier<String> data, Method method, String urlPrefix, synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener lifeCycleListener) {
        this.url = url;
        this.data = data;
        if(urlPrefix != null) {
            this.urlPrefix = urlPrefix;
        }
        this.lifeCycleListener = lifeCycleListener;
        this.method = method;
    }

	public void send(synitex.common.gwt.jsonrpc.client.JsonRequestCallback callback) {
		if (callback == null) {
			throw new IllegalArgumentException("Callback can not be null!");
		}
		this.callback = callback;

		if (lifeCycleListener != null) {
			lifeCycleListener.beforeCall();
		}

		try {
			sendImpl();
		} finally {
			if (lifeCycleListener != null) {
				lifeCycleListener.afterCall();
			}
		}
	}

	private void sendImpl() {
		RequestBuilder rb = new RequestBuilder(method, urlPrefix + url);

        if(method.equals(RequestBuilder.POST)) {
            rb.setHeader("Content-Type", "application/json");
        }

        if(data != null) {
            String s = data.get();
            if(GwtHelper.isNotEmpty(s)) {
		        rb.setRequestData(s);
            }
        }

		rb.setCallback(this);
		try {
			rb.send();
		} catch (RequestException e) {
            EventBus.get().fire(new StatusHideGwtEvent());
			if (callback != null) {
				callback.onRequestError(e);
			}
		}
	}

	@Override
	public void onResponseReceived(Request request, Response response) {

        EventBus.get().fire(new StatusHideGwtEvent());

		String responseText = response.getText();

		if (GwtHelper.isEmpty(responseText)) {
			callback.onResponse(null);
			return;
		}

		JSONObject json = null;
		try {
			 json = GwtJsonHelper.getJsonFromString(responseText);
		} catch (Exception e) {
			callback.onResponseParseError(e);
		}

		if (json == null) {
			callback.onResponseParseError(new RuntimeException("No response json is parsed"));
			return;
		}

        JSONObject result = GwtJsonHelper.getJsonObject(json, ReqConstants.PARAM_RESPONSE_DATA);

        String responseStatus = GwtJsonHelper.getString(json, ReqConstants.PARAM_RESPONSE_STATUS);
        if(ReqConstants.PARAM_RESPONSE_STATUS__ERROR.equals(responseStatus)) {
            String errorCode = GwtJsonHelper.getString(json, ReqConstants.PARAM_RESPONSE_ERROR_CODE);
            String errorMessage = GwtJsonHelper.getString(json, ReqConstants.PARAM_RESPONSE_ERROR_MESSAGE);
            String errorDetails = GwtJsonHelper.getString(json, ReqConstants.PARAM_RESPONSE_ERROR_DETAILS);
            callback.onApplicationError(errorCode, errorMessage, errorDetails, result);
        } else {
		    callback.onResponse(result);
        }
	}

	@Override
	public void onError(Request request, Throwable exception) {
        EventBus.get().fire(new StatusHideGwtEvent());
		callback.onGenericRequestError(request, exception);
	}

}
