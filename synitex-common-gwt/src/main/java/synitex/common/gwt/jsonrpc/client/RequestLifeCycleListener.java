package synitex.common.gwt.jsonrpc.client;

public interface RequestLifeCycleListener {

	void beforeCall();

	void afterCall();

}
