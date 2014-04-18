package synitex.common.gwt.validate2.client;

import java.util.List;

public interface IValidateResult {

    boolean hasErrors();

    List<String> getErrors();

}
