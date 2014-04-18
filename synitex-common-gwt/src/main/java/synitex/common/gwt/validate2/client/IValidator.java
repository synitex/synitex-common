package synitex.common.gwt.validate2.client;

public interface IValidator<VALUE> {

    IValidateResult isValid(VALUE value);

}
