package synitex.common.gwt.validate2.client.validators;

import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IValidateResult;
import synitex.common.gwt.validate2.client.IValidator;
import synitex.common.gwt.validate2.client.ValidateResult;

public class StringLengthValidator implements IValidator<String> {

    private final int min;
    private final int max;

    public StringLengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public IValidateResult isValid(String s) {
        if(GwtHelper.isNotEmpty(s)) {
            int length = s.length();
            if(min > 0 && length < min) {
                return new ValidateResult("Minimum length should be " + min + " symbols.");
            }
            if(max > 0 && length > max) {
                return new ValidateResult("Maximun length should be " + max + " symbols.");
            }
        }
        return ValidateResult.OK;
    }

}
