package synitex.common.gwt.validate.shared.validators;

import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorResult;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;


public class DurationValidator implements Validator<String> {

	@Override
	public ValidatorResult validate(ValidatorValueProvider<String> valueProvider) {
		try {
			String value = valueProvider.getValue();
			int a = Integer.valueOf(value);
			
			if (a < 1) {
				return new ValidatorResult("not.duration");
			}
			
			return null;
		} catch (NumberFormatException e) {
			return new ValidatorResult("not.duration");
		}
	}

}
