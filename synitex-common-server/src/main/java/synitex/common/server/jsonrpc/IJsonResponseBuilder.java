package synitex.common.server.jsonrpc;

import synitex.common.gwt.jsonrpc.shared.ReqConstants;

public interface IJsonResponseBuilder {

    Builder get();

    Builder get(String param, Object data);

    public static class Builder {

        private JsonResponseWrapper wrapper;

        Builder() {
            wrapper = new JsonResponseWrapper();
        }

        public JsonResponseWrapper build() {
            return wrapper;
        }

        public Builder add(String param, Object data) {
            wrapper.getData().put(param, data);
            return this;
        }

        public Builder ok() {
            wrapper.setStatus(ReqConstants.PARAM_RESPONSE_STATUS__OK);
            return this;
        }

        public Builder error() {
            return error(null);
        }

        public Builder error(String code) {
            return error(code, null, null);
        }

        public Builder error(String code, String message, String details) {
            wrapper.setStatus(ReqConstants.PARAM_RESPONSE_STATUS__ERROR);
            wrapper.setErrorCode(code);
            wrapper.setErrorMessage(message);
            wrapper.setErrorDetails(details);
            return this;
        }

    }

}
