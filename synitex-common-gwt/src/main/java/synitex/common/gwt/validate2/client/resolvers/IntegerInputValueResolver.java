package synitex.common.gwt.validate2.client.resolvers;

import com.google.gwt.dom.client.InputElement;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IFieldValueResolver;

public class IntegerInputValueResolver implements IFieldValueResolver<Integer, InputElement> {

    @Override
    public Integer resolveValue(InputElement element) {
        return GwtHelper.getIntegerValue(element);
    }

}
