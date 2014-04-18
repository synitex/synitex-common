package synitex.common.gwt.validate.shared.validators;

import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorResult;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;


public class WebValidator implements Validator<String> {

    @Override
	public ValidatorResult validate(ValidatorValueProvider<String> valueProvider) {
		String value = valueProvider.getValue();
        if (value == null) {
            return null;
        }
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return null;
        } else {
			return new ValidatorResult("web.ste.invalid.format");
        }
    }

}
