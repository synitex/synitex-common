package synitex.common.gwt.util.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import java.util.logging.Logger;


public class GwtJsonBuilder {

    private static final Logger log = Logger.getLogger(GwtJsonBuilder.class.getName());
    
	private JSONObject json;

	public GwtJsonBuilder() {
		json = new JSONObject();
	}

	public GwtJsonBuilder append(String param, Object value) {
		if (value == null) {
			return this;
		}
		JSONValue jsonVal = GwtJsonHelper.toJsonValue(value);
		if(jsonVal == null) {
		    log.fine("Failed to inject " + value.getClass() + " with param " + param + ", because object can not be transformed to json object.");
			return this;
		}
		json.put(param, jsonVal);
		return this;
	}

	public String toString() {
		return json.toString();
	}

	public JSONObject toJson() {
		return json;
	}

}
