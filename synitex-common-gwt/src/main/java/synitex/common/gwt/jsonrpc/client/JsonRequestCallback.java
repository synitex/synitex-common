package synitex.common.gwt.jsonrpc.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;

public interface JsonRequestCallback {

	void onResponse(JSONObject json);

	void onApplicationError(String errorCode, String errorMessage, String errorDetails, JSONObject data);

	void onRequestError(RequestException ex);

	void onGenericRequestError(Request request, Throwable exception);

	void onResponseParseError(Throwable ex);

}
