package synitex.common.gwt.validate.shared.validators;

import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorResult;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;

public class IntegerMinValueValidator implements Validator<Integer> {

    private final int minValue;

    public IntegerMinValueValidator(int minAcceptableValue) {
        this.minValue = minAcceptableValue;
    }

    @Override
    public ValidatorResult validate(ValidatorValueProvider<Integer> valueProvider) {
        Integer value = valueProvider.getValue();
        if(value == null) {
            return null; // all ok
        }
        if(value <minValue) {
            return new ValidatorResult("min.value");
        } else {
            return null;
        }
    }
}
