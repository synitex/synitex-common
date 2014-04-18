package synitex.common.gwt.validate2.client;


import com.google.gwt.dom.client.Element;

public interface IValidateField  {

    boolean validate(boolean stopOnFirstFail);

    void reset();

    Element getElement();
}
