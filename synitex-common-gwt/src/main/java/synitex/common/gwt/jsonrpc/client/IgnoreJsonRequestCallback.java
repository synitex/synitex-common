package synitex.common.gwt.jsonrpc.client;

import com.google.gwt.json.client.JSONObject;

public class IgnoreJsonRequestCallback extends DefaultJsonRequestCallback {

    @Override
    public void onResponse(JSONObject json) {
        //ignore
    }

}
