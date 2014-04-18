package synitex.common.gwt.validate.shared.validators;

import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorResult;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;


public class EmailValidator implements Validator<String> {

    @Override
	public ValidatorResult validate(ValidatorValueProvider<String> valueProvider) {
		String value = valueProvider.getValue();
		if (value == null || "".equals(value.trim())) {
            return null;
        }

		if (!value.contains("@")) {
			return new ValidatorResult("not.an.email");
		} else {
			return null;
		}

    }

}
