package synitex.common.gwt.validate.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate.shared.Validator;
import synitex.common.gwt.validate.shared.ValidatorValueProvider;

import java.util.Arrays;
import java.util.List;

public class GwtValidator {

	private synitex.common.gwt.validate.client.GwtValidatorMessagesProvider messagesProvider;
	private synitex.common.gwt.validate.client.GwtValidatorForm form;

	public GwtValidator(synitex.common.gwt.validate.client.GwtValidatorMessagesProvider messagesProvider) {
		if (messagesProvider == null) {
			throw new IllegalArgumentException(synitex.common.gwt.validate.client.GwtValidatorMessagesProvider.class.getName() + " is required parameter");
		}
		this.messagesProvider = messagesProvider;
	}

    public GwtValidator field(InputElement el, Validator<String> ... validators) {
        List<Validator<String>> validatorsList = Arrays.asList(validators);
        return field(el, validatorsList);
    }

    public GwtValidator integerField(final InputElement el, Validator<Integer> ... validators) {
        List<Validator<Integer>> validatorsList = Arrays.asList(validators);
        return field(el, new ValidatorValueProvider<Integer>() {
            @Override
            public Integer getValue() {
                return GwtHelper.getIntegerValue(el);
            }
        }, validatorsList);
    }

	public GwtValidator field(final TextBox el, List<Validator<String>> validators) {
		return field(el, new ValidatorValueProvider<String>() {
			@Override
			public String getValue() {
				return el.getValue();
			}
		}, validators);
	}

	public GwtValidator field(final TextArea el, List<Validator<String>> validators) {
		return field(el, new ValidatorValueProvider<String>() {
			@Override
			public String getValue() {
				return el.getValue();
			}
		}, validators);
	}

	public GwtValidator field(final TextAreaElement el, List<Validator<String>> validators) {
		return field(el, new ValidatorValueProvider<String>() {
			@Override
			public String getValue() {
				return el.getValue();
			}
		}, validators);
	}

	public GwtValidator field(final InputElement el, List<Validator<String>> validators) {
		return field(el, new ValidatorValueProvider<String>() {
			@Override
			public String getValue() {
				return el.getValue();
			}
		}, validators);
	}

	public <T> GwtValidator field(Widget widget, ValidatorValueProvider<T> value, List<Validator<T>> validators) {
		return field(widget.getElement(), value, validators);
	}

	public <T> GwtValidator field(Widget widget, ValidatorValueProvider<T> value, Validator<T>... validators) {
		return field(widget.getElement(), value, validators);
	}

	public <T> GwtValidator field(Element el, ValidatorValueProvider<T> value, Validator<T>... validators) {
		List<Validator<T>> v = validators == null ? null : Arrays.asList(validators);
		return field(el, value, v);
	}

	public <T> GwtValidator field(Element el, ValidatorValueProvider<T> value, List<Validator<T>> validators) {
		synitex.common.gwt.validate.client.GwtValidatorField<T> fld = new synitex.common.gwt.validate.client.GwtValidatorField<T>(el, value);
		if (validators != null && validators.size() > 0) {
			fld.addValidators(validators);
		}
		fld.setMessagesProvider(messagesProvider);

		if (form == null) {
			form = new synitex.common.gwt.validate.client.GwtValidatorForm();
		}

		form.addValidateField(fld);

		return this;
	}

	public boolean clearAndValidate() {
		return clear().valid();
	}

	public GwtValidator clear() {
		if (form != null) {
			form.clearErrors();
		}
		return this;
	}

	public boolean valid() {
		if (form != null) {
			return form.allValid();
		} else {
			return true;
		}
	}

}
