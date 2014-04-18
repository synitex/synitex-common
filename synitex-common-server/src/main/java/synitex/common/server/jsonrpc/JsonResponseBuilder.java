package synitex.common.server.jsonrpc;


import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class JsonResponseBuilder implements IJsonResponseBuilder {

    private static final Logger log = LoggerFactory.getLogger(JsonResponseBuilder.class);

    public JsonResponseBuilder() {

    }

    @Override
    public Builder get() {
        return new Builder();
    }

    @Override
    public Builder get(String param, Object data) {
        return new Builder().add(param, data);
    }
}
