package synitex.common.gwt.validate2.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.resolvers.IntegerInputValueResolver;
import synitex.common.gwt.validate2.client.resolvers.LongIntegerValueResolver;
import synitex.common.gwt.validate2.client.resolvers.StringInputValueResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ValidateField<ELEMENT extends Element, VALUE> implements IValidateField {

    private static final Logger log = Logger.getLogger(ValidateField.class.getName());

    private final ELEMENT el;
    private final IFieldValueResolver<VALUE, ELEMENT> valueResolver;
    private List<IValidator<VALUE>> validators;
    private IValidateResultPainter validateResultPainter;

    public static <ELEMENT extends Element, VALUE> Builder<ELEMENT, VALUE> wrap(ELEMENT el, Class<VALUE> valueType) {
        return new Builder<ELEMENT, VALUE>(el, valueType);
    }

    private ValidateField(ELEMENT el, IFieldValueResolver<VALUE, ELEMENT> valueResolver) {
        this.el = el;
        this.valueResolver = valueResolver;
    }

    private void setValidators(List<IValidator<VALUE>> validators) {
        this.validators = validators;
    }

    private void setValidateResultPainter(IValidateResultPainter validateResultPainter) {
        this.validateResultPainter = validateResultPainter;
    }

    @Override
    public boolean validate(boolean stopOnFirstFail) {
        if(GwtHelper.isEmpty(validators)) {
            return true;
        }
        if(valueResolver == null) {
            log.fine("No value provider is set for " + el);
            return true;
        }
        List<IValidateResult> validateResults = new ArrayList<IValidateResult>();
        VALUE value = valueResolver.resolveValue(el);
        for(IValidator<VALUE> validator : validators) {
            IValidateResult validateResult = validator.isValid(value);
            if(validateResult.hasErrors()) {
                validateResults.add(validateResult);
                if(stopOnFirstFail) {
                    break;
                }
            }
        }

        if(validateResultPainter != null && GwtHelper.isNotEmpty(validateResults)) {
            validateResultPainter.paint(validateResults);
        }

        return GwtHelper.isEmpty(validateResults);
    }

    @Override
    public void reset() {
        if(validateResultPainter != null) {
            validateResultPainter.clear();
        }
    }

    @Override
    public Element getElement() {
        return el;
    }

    // -----------------------------------------------

    public static class Builder<ELEMENT extends Element, VALUE> {

        private final ValidateField<ELEMENT, VALUE> field;
        private List<IValidator<VALUE>> validators = new ArrayList<IValidator<VALUE>>();

        private Builder(ELEMENT el, Class<VALUE> valueType) {
            IFieldValueResolver<VALUE, ELEMENT> valueResolver = findResolver(el, valueType);
            field = new ValidateField<ELEMENT, VALUE>(el, valueResolver);
        }

        public Builder<ELEMENT, VALUE> with(IValidator<VALUE> validator) {
            validators.add(validator);
            return this;
        }

        public Builder<ELEMENT, VALUE> errorPainter(IValidateResultPainter painter) {
            field.setValidateResultPainter(painter);
            return this;
        }

        public Builder<ELEMENT, VALUE> bootstrapErrorPainter(Element elControlGroup, Element elHelp) {
            field.setValidateResultPainter(new BootstrapErrorPainter(elControlGroup, elHelp));
            return this;
        }

        public Builder<ELEMENT, VALUE> bootstrap3ErrorPainer(Element elInput) {
            field.setValidateResultPainter(new Bootstrap3ErrorPainter(elInput));
            return this;
        }

        public ValidateField<ELEMENT, VALUE> build() {
            field.setValidators(validators);
            return field;
        }

        private IFieldValueResolver<VALUE, ELEMENT> findResolver(ELEMENT el, Class<VALUE> valueType) {
            if(el instanceof InputElement) {
                if(valueType == Integer.class || valueType == Integer.TYPE) {
                    return (IFieldValueResolver<VALUE, ELEMENT>) new IntegerInputValueResolver();
                } else if(valueType == String.class) {
                    return (IFieldValueResolver<VALUE, ELEMENT>) new StringInputValueResolver();
                } else if(valueType == Long.class || valueType == Long.TYPE) {
                    return (IFieldValueResolver<VALUE, ELEMENT>) new LongIntegerValueResolver();
                }
            }

            return null;
        }

    }

}
