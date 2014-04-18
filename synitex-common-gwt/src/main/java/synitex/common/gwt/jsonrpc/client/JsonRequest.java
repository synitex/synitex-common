package synitex.common.gwt.jsonrpc.client;

import com.google.common.base.Supplier;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONValue;


/**
 * 
 * 
 * @author sergey.sinica
 * 
 */
public class JsonRequest extends AbstractRequest {

	JsonRequest(String url, String urlPrefix, final JSONValue data, synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener lifeCycleListener) {
		super(url, new Supplier<String>() {
            @Override
            public String get() {
                return data == null ? "" : data.toString();
            }
        }, RequestBuilder.POST, urlPrefix, lifeCycleListener);
	}

}
