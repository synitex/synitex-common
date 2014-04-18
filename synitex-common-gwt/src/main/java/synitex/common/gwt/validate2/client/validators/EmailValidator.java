package synitex.common.gwt.validate2.client.validators;


import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IValidateResult;
import synitex.common.gwt.validate2.client.IValidator;
import synitex.common.gwt.validate2.client.ValidateResult;

public class EmailValidator implements IValidator<String> {

    @Override
    public IValidateResult isValid(String s) {
        if(GwtHelper.isEmpty(s) || s.indexOf("@") > 0) {
            return ValidateResult.OK;
        } else {
            return new ValidateResult("Invalid email format");
        }
    }

}
