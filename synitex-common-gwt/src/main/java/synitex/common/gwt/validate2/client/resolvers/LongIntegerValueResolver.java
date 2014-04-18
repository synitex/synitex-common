package synitex.common.gwt.validate2.client.resolvers;

import com.google.gwt.dom.client.InputElement;
import synitex.common.gwt.util.client.GwtHelper;
import synitex.common.gwt.validate2.client.IFieldValueResolver;

public class LongIntegerValueResolver implements IFieldValueResolver<Long, InputElement> {

    @Override
    public Long resolveValue(InputElement element) {
        return GwtHelper.getLongValue(element);
    }

}
