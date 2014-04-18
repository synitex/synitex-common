package synitex.common.gwt.validate2.client;

import java.util.List;

public interface IValidateResultPainter {

    void clear();

    void paint(List<IValidateResult> validateResults);

    void paint(IValidateResult... validateResults);

}
