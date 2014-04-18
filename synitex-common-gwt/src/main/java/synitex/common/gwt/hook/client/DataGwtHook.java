package synitex.common.gwt.hook.client;

import com.google.common.base.Supplier;

public interface DataGwtHook<DATA> extends synitex.common.gwt.hook.client.GwtHook {

    void setDataSupplier(Supplier<DATA> dataSupplier);

}
