package synitex.common.gwt.validate.shared;

import com.google.common.collect.Lists;
import synitex.common.gwt.validate.shared.validators.EmailValidator;
import synitex.common.gwt.validate.shared.validators.EmptyValidator;
import synitex.common.gwt.validate.shared.validators.MaxLengthValidator;
import synitex.common.gwt.validate.shared.validators.MinLengthValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Validators factory.
 * 
 * @author sergey.sinica
 * 
 */
public class ValidatorFactory {

	private ValidatorFactory() {

	}

    public static List<? extends Validator<String>> prohibitEmpty() {
        return Lists.newArrayList(new EmptyValidator<String>());
    }

	public static List<Validator<String>> getPasswordValidators() {
		ArrayList<Validator<String>> validators = new ArrayList<Validator<String>>();
		validators.add(new EmptyValidator<String>());
		validators.add(new MinLengthValidator(6));
		validators.add(new MaxLengthValidator(100));
		return validators;
	}

	public static List<Validator<String>> getEmailValidators() {
		ArrayList<Validator<String>> validators = new ArrayList<Validator<String>>();
		validators.add(new EmptyValidator<String>());
		validators.add(new MaxLengthValidator(100));
		validators.add(new EmailValidator());
		return validators;
	}

}
