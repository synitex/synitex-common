package synitex.common.gwt.validate2.client.validators;

import com.google.common.base.Supplier;
import synitex.common.gwt.validate2.client.IValidateResult;
import synitex.common.gwt.validate2.client.IValidator;
import synitex.common.gwt.validate2.client.ValidateResult;

public abstract class AbstractMinValidator<T extends Number> implements IValidator<T> {

    private Supplier<T> minValueSupplier;

    public abstract boolean isValid(T value, T minValue);

    public AbstractMinValidator(Supplier<T> minValueSupplier) {
        this.minValueSupplier = minValueSupplier;
    }

    @Override
    public IValidateResult isValid(T t) {
        T min = minValueSupplier.get();
         if(t == null || min == null || isValid(t, min)) {
             return ValidateResult.OK;
         } else {
             return new ValidateResult("Field value should be greater than " + min);
         }
    }


}
