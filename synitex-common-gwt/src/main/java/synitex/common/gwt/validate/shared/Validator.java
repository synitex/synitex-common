package synitex.common.gwt.validate.shared;


/**
 * Validator interface.
 * 
 * @author sergey.sinica
 * 
 * @param <T>
 */
public interface Validator<T> {

	/**
	 * Validates field's value.
	 * 
	 * @param value
	 *            provides value to com.synitex.validate
	 * @return validation result, if value did not pass validation process or
	 *         null if value is valid.
	 */
	ValidatorResult validate(ValidatorValueProvider<T> valueProvider);

}
