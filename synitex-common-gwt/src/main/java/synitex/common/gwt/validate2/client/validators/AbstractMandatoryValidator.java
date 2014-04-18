package synitex.common.gwt.validate2.client.validators;


import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IValidateResult;
import synitex.common.gwt.validate2.client.IValidator;
import synitex.common.gwt.validate2.client.ValidateResult;

public abstract class AbstractMandatoryValidator<VALUE> implements IValidator<VALUE> {

    @Override
    public IValidateResult isValid(Object s) {
        if(s == null || s instanceof String && GwtHelper.isEmpty(s.toString())) {
            return new ValidateResult("Mandatory field");
        } else {
            return ValidateResult.OK;
        }
    }

}
