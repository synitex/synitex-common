package synitex.common.gwt.validate2.client;

import synitex.common.gwt.util.client.GwtHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidateResult implements IValidateResult {

    public static final ValidateResult OK = new ValidateResult();

    private List<String> errors = new ArrayList<String>();

    private ValidateResult() {

    }

    public ValidateResult(String... errors) {
        if(errors != null) {
            this.errors.addAll(Arrays.asList(errors));
        }
    }

    @Override
    public boolean hasErrors() {
        return GwtHelper.isNotEmpty(errors);
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }
}
