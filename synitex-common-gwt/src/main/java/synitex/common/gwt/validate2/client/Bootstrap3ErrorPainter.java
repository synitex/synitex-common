package synitex.common.gwt.validate2.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import synitex.common.gwt.util.client.GwtHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap3ErrorPainter implements IValidateResultPainter {

    private static final String CSS_ERROR = "has-error";
    private static final String CSS_HELP = "help-block";

    private final Element inputEl;
    private final Element formGroupEl;
    private Element helpEl;

    public Bootstrap3ErrorPainter(Element inputEl) {
        this.inputEl = inputEl;
        this.formGroupEl = inputEl.getParentElement();
    }

    @Override
    public void clear() {
        formGroupEl.removeClassName(CSS_ERROR);
        if(helpEl != null) {
            helpEl.setInnerHTML("");
        }
    }

    @Override
    public void paint(List<IValidateResult> validateResults) {
        if(GwtHelper.isEmpty(validateResults)) {
            clear();
        } else {
            List<String> errors = collectErrors(validateResults);
            StringBuilder sb = new StringBuilder();
            if(errors.size() == 1) {
                sb.append(errors.get(0));
            } else {
                sb.append("<ul>");
                for(String error : errors) {
                    sb.append("<li>");
                    sb.append(error);
                    sb.append("</li>");
                }
                sb.append("</ul>");
            }

            if(helpEl == null) {
                helpEl = DOM.createDiv();
                helpEl.setClassName(CSS_HELP);
                formGroupEl.insertAfter(helpEl, inputEl);
            }
            helpEl.setInnerHTML(sb.toString());
            formGroupEl.addClassName(CSS_ERROR);
        }
    }

    @Override
    public void paint(IValidateResult... validateResults) {
        if(validateResults != null && validateResults.length > 0) {
            paint(Arrays.asList(validateResults));
        } else {
            clear();
        }
    }

    private List<String> collectErrors(List<IValidateResult> validateResults) {
        List<String> errors = new ArrayList<String>();
        if(GwtHelper.isNotEmpty(validateResults)) {
            for(IValidateResult result : validateResults) {
                List<String> errs = result.getErrors();
                if(GwtHelper.isNotEmpty(errs)) {
                    errors.addAll(errs);
                }
            }
        }
        return errors;
    }

}
