package synitex.common.gwt.validate2.client;

import com.google.common.base.Supplier;
import synitex.common.gwt.validate2.client.validators.EmailValidator;
import synitex.common.gwt.validate2.client.validators.IntegerMandatoryValidator;
import synitex.common.gwt.validate2.client.validators.MinIntegerValidator;
import synitex.common.gwt.validate2.client.validators.MinLongValidator;
import synitex.common.gwt.validate2.client.validators.StringLengthValidator;
import synitex.common.gwt.validate2.client.validators.StringMandatoryValidator;

public class Validators {

    private static EmailValidator email;
    private static StringMandatoryValidator mandatoryString;
    private static IntegerMandatoryValidator mandatoryInteger;

    private Validators() {

    }

    public static EmailValidator email() {
        if(email == null) {
            email = new EmailValidator();
        }
        return email;
    }

    public static StringMandatoryValidator mandatoryString() {
        if(mandatoryString == null) {
            mandatoryString = new StringMandatoryValidator();
        }
        return mandatoryString;
    }

    public static IntegerMandatoryValidator mandatoryInteger() {
        if(mandatoryInteger == null) {
            mandatoryInteger = new IntegerMandatoryValidator();
        }
        return mandatoryInteger;
    }

    public static MinIntegerValidator minInteger(int min) {
        return new MinIntegerValidator(min);
    }

    public static MinIntegerValidator minInteger(Supplier<Integer> minValueSupplier) {
        return new MinIntegerValidator(minValueSupplier);
    }

    public static MinLongValidator minLong(long min) {
        return new MinLongValidator(min);
    }

    public static StringLengthValidator stringLengthValidator(int min, int max) {
        return new StringLengthValidator(min, max);
    }
}
