package synitex.common.gwt.jsonrpc.client;

import com.google.common.base.Supplier;
import com.google.gwt.http.client.RequestBuilder;
import synitex.common.gwt.util.client.GwtHelper;

import java.util.Map;
import java.util.Map.Entry;


public class GetRequest extends AbstractRequest {

	GetRequest(String url, String urlPrefix, final Map<String, String> data, synitex.common.gwt.jsonrpc.client.RequestLifeCycleListener listener) {
        super(url, new Supplier<String>() {
            @Override
            public String get() {
                if(GwtHelper.isEmpty(data)) {
                    return "";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(Entry<String, String> en : data.entrySet()) {
                        if(sb.length() != 0) {
                            sb.append("&");
                        }
                        sb.append(en.getKey());
                        sb.append("=");
                        sb.append(en.getValue());
                    }
                    return sb.toString();
                }
            }
        }, RequestBuilder.GET, urlPrefix, listener);
	}

}
