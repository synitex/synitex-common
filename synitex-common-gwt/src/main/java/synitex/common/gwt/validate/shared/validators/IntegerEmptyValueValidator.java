package synitex.common.gwt.validate.shared.validators;

import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorResult;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;

public class IntegerEmptyValueValidator implements Validator<Integer> {

    @Override
    public ValidatorResult validate(ValidatorValueProvider<Integer> valueProvider) {
        if(valueProvider.getValue() == null) {
            return new ValidatorResult("validator.empty");
        } else {
            return null;
        }
    }
}
