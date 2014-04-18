package synitex.common.gwt.validate2.client;

import com.google.gwt.dom.client.Element;

public interface IFieldValueResolver<VALUE_TYPE, ELEMENT_TYPE extends Element> {

    VALUE_TYPE resolveValue(ELEMENT_TYPE element);

}
