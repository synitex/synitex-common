package synitex.common.gwt.validate2.client.validators;

import com.google.common.base.Supplier;

public class MinIntegerValidator extends AbstractMinValidator<Integer> {

    public MinIntegerValidator(final Integer minValue) {
        super(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return minValue;
            }
        });
    }

    public MinIntegerValidator(Supplier<Integer> minValueSupplier) {
        super(minValueSupplier);
    }

    @Override
    public boolean isValid(Integer value, Integer minValue) {
        return value >= minValue;
    }
}
