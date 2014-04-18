package synitex.common.gwt.validate2.client.resolvers;

import com.google.gwt.dom.client.InputElement;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IFieldValueResolver;

public class StringInputValueResolver implements IFieldValueResolver<String, InputElement> {

    @Override
    public String resolveValue(InputElement element) {
        return GwtHelper.defaultIfEmpty(element.getValue(), "");
    }
}
