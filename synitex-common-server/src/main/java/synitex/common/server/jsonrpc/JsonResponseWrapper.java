package synitex.common.server.jsonrpc;

import synitex.common.gwt.jsonrpc.shared.ReqConstants;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class JsonResponseWrapper {

    private Map<String, Object> data = new HashMap<String, Object>();

    private String status;

    @JsonProperty(ReqConstants.PARAM_RESPONSE_ERROR_CODE)
    private String errorCode;

    @JsonProperty(ReqConstants.PARAM_RESPONSE_ERROR_MESSAGE)
    private String errorMessage;

    @JsonProperty(ReqConstants.PARAM_RESPONSE_ERROR_DETAILS)
    private String errorDetails;

    JsonResponseWrapper() {

    }

    @JsonProperty(ReqConstants.PARAM_RESPONSE_DATA)
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @JsonProperty(ReqConstants.PARAM_RESPONSE_STATUS)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
