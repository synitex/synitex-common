package synitex.common.gwt.validate2.client.validators;

import com.google.common.base.Supplier;

public class MinLongValidator extends AbstractMinValidator<Long> {

    public MinLongValidator(final Long minValue) {
        super(new Supplier<Long>() {
            @Override
            public Long get() {
                return minValue;
            }
        });
    }

    public MinLongValidator(Supplier<Long> minValueSupplier) {
        super(minValueSupplier);
    }

    @Override
    public boolean isValid(Long value, Long minValue) {
        return value >=minValue;
    }
}
