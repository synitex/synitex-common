package synitex.common.gwt.validate.shared;

public class ValidatorResult {

	private String errorKey;

	public ValidatorResult(String errorKey) {
		this.errorKey = errorKey;
    }

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}
}
