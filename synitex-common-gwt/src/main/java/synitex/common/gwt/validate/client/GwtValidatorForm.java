package synitex.common.gwt.validate.client;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link GwtValidatorField} container.
 *
 * @author sergey.sinica
 */
public class GwtValidatorForm {

    private List<GwtValidatorField<?>> validators = new ArrayList<GwtValidatorField<?>>();

    GwtValidatorForm() {

    }

    public void addValidateField(GwtValidatorField<?> validate) {
        validators.add(validate);
    }

    /**
     * True if all is valid.
     *
     * @return
     */
    public boolean allValid() {
        boolean allValid = true;
        for (GwtValidatorField<?> v : validators) {
            if (!v.isValid()) {
                allValid = false;
            }
        }
        return allValid;
    }

    public void clearErrors() {
        for (GwtValidatorField<?> v : validators) {
            v.clearErrors();
        }
    }

}
