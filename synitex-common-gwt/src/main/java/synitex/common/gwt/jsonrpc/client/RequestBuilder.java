package synitex.common.gwt.jsonrpc.client;

import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONValue;
import synitex.common.gwt.util.client.EventBus;
import synitex.common.gwt.util.client.GwtJsonBuilder;
import synitex.common.gwt.util.client.GwtJsonDto;
import synitex.common.gwt.util.client.StatusGwtEvent;

/**
 * Builder used to create {@link JsonRequest} instances.
 * 
 * @author sergey.sinica
 * 
 */
public class RequestBuilder implements synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener {

    private static final String defaultStatusMessage = "Loading...";

	private String url;
	private synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener lifeCycleListener;
    private String statusMessage = defaultStatusMessage;
    private boolean useRelativeUrl = true;
    private Method method = com.google.gwt.http.client.RequestBuilder.POST;
    private boolean hidden = false;

    public RequestBuilder(String url) {
		this.url = url;
	}

    public RequestBuilder setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public RequestBuilder hidden() {
        this.hidden = true;
        return this;
    }

    public RequestBuilder useAbsoluteUrl() {
        this.useRelativeUrl = false;
        return this;
    }

    public RequestBuilder useGetMethod() {
        this.method = com.google.gwt.http.client.RequestBuilder.GET;
        return this;
    }

    public RequestBuilder setLifeCycleListener(synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener listener) {
        this.lifeCycleListener = listener;
        return this;
    }

    public void call(JsonRequestCallback callback) {
        call(null, callback);
    }

    public void callWithDto(GwtJsonDto jsonDto, JsonRequestCallback callback) {
        if(jsonDto == null) {
            call(callback);
        } else {
            call(jsonDto.toJsonObject(new GwtJsonBuilder()).toJson(), callback);
        }
    }

    public void call(JSONValue json) {
        call(json, new IgnoreJsonRequestCallback());
    }

    public void call(JSONValue json, JsonRequestCallback callback) {
        String urlPrefix = useRelativeUrl ? null : "";
        AbstractRequest req = method.equals(com.google.gwt.http.client.RequestBuilder.GET)
                ? new GetRequest(url, urlPrefix, null, this)
                : new JsonRequest(url, urlPrefix, json, this);
        req.send(callback);
    }

    @Override
    public void beforeCall() {

        if(!hidden) {
            EventBus.get().fire(new StatusGwtEvent(statusMessage));
        }

        if(lifeCycleListener != null) {
            lifeCycleListener.beforeCall();
        }
    }

    @Override
    public void afterCall() {
        if(lifeCycleListener != null) {
            lifeCycleListener.afterCall();
        }
    }

}
