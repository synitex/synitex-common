package synitex.common.gwt.validate2.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import synitex.common.gwt.util.client.GwtHelper;

import java.util.ArrayList;
import java.util.List;

public class ValidateForm {

    private boolean stopOnFirstFail = false;
    private boolean attachBlurListeners = false;

    private List<IValidateField> fields = new ArrayList<IValidateField>();
    private EventListener blurListener;

    public ValidateForm add(IValidateField fld) {
        fields.add(fld);
        if(attachBlurListeners) {
            attachBlurListener(fld);
        }
        return this;
    }

    public ValidateForm add(IValidateField...flds) {
        if(flds != null) {
            for(IValidateField fld : flds) {
                add(fld);
            }
        }
        return this;
    }

    /**
     * Instructs fields in this form to stop validation process on first fail.
     */
    public ValidateForm stopOnFirstFail() {
        stopOnFirstFail = true;
        return this;
    }

    /**
     * Instructs form to try to attach blur listener to fields and re-validate the
     * form on blur events.
     */
    public ValidateForm validateOnFieldsBlur() {
        attachBlurListeners = true;
        return this;
    }

    public boolean resetAndValidate() {
        reset();
        return validate();
    }

    public boolean validate() {
        boolean allValid = true;
        for(IValidateField fld : fields) {
            boolean valid = fld.validate(stopOnFirstFail);
            if(!valid) {
                allValid = false;
            }
        }
        return allValid;
    }

    public void reset() {
        for(IValidateField fld : fields) {
            fld.reset();
        }
    }

    private void attachBlurListener(IValidateField fld) {
        Element el = fld.getElement();
        GwtHelper.sinkEvents(el, Event.ONBLUR);
        GwtHelper.addEventListener(el, getBlurListener());
    }

    public EventListener getBlurListener() {
        if(blurListener == null) {
            blurListener = new EventListener() {
                @Override
                public void onBrowserEvent(Event event) {
                    resetAndValidate();
                }
            };
        }
        return blurListener;
    }
}
