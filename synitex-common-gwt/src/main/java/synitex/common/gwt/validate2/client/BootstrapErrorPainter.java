package synitex.common.gwt.validate2.client;

import com.google.gwt.dom.client.Element;
import synitex.common.gwt.util.client.GwtHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BootstrapErrorPainter implements IValidateResultPainter {

    private static final String CSS_ERROR = "error";

    private final Element elControlGroup;
    private final Element elHelp;

    public BootstrapErrorPainter(Element elControlGroup, Element elHelp) {
        this.elControlGroup = elControlGroup;
        this.elHelp = elHelp;
    }

    @Override
    public void clear() {
        elControlGroup.removeClassName(CSS_ERROR);
        elHelp.setInnerHTML("");
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

            elHelp.setInnerHTML(sb.toString());
            elControlGroup.addClassName(CSS_ERROR);
        }
    }

    @Override
    public void paint(IValidateResult... validateResults) {
        if(validateResults != null) {
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
