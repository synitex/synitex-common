package synitex.common.gwt.util.client;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionsHandler implements UncaughtExceptionHandler {

    private static final ExceptionsHandler INSTANCE = new ExceptionsHandler();
    private static final String NEW_LINE = "\n\r";
    private static final Logger log = Logger.getLogger(ExceptionsHandler.class.getName());

    private ExceptionsHandler() {

    }

    public static ExceptionsHandler get() {
        return INSTANCE;
    }

    public void handleExceptionFromScheduller(Exception ex) {
        handleExceptionImpl(ex, true);
    }

    @Override
    public void onUncaughtException(Throwable e) {
        handleExceptionImpl(e, true);
    }

    public void handleExceptionSilently(Throwable ex) {
        handleExceptionImpl(ex, false);
    }

    private void handleExceptionImpl(Throwable e, boolean notifyUser) {
        String msg = exceptionMessage2String(e);
        String stacktrace = stacktrace2String(e);
        String msgForDeveloper = formatMessage(msg, stacktrace);

        log.log(Level.FINE, msgForDeveloper);
        if(GwtHelper.isDevelopmentMode()) {
            // show full exception to developer
            Window.alert(msgForDeveloper);
        } else {
            if(notifyUser) {
                // show small, pretty error message to user
                Window.alert(
                        "An unexpected error detected!" +
                                NEW_LINE + NEW_LINE +
                                "Sorry for inconvenience!" +
                                NEW_LINE +
                                "The problem is reported to development team and will be fixed as soon as possible.");
            }
        }

        sendErrorToServer(msg, stacktrace);
    }

    private String formatMessage(String message, String stacktrace) {

        StringBuilder s = new StringBuilder();
        s.append("Exception: ");
        if(GwtHelper.isNotEmpty(message)) {
            s.append(message);
        }
        if(GwtHelper.isNotEmpty(stacktrace)) {
            s.append(NEW_LINE);
            s.append(NEW_LINE);
            s.append("Stacktrace: ");
            s.append(stacktrace);
        }
        return s.toString();
    }

    private String stacktrace2String(Throwable ex) {
        if(ex == null) {
            return "undefined stacktrace";
        }
        StringBuilder s = new StringBuilder();
        StringOutputStream out = new StringOutputStream();
        ex.printStackTrace(new PrintStream(out));
        String stackTrace = out.toString();
        if(GwtHelper.isNotEmpty(stackTrace)) {
            s.append(stackTrace);
        }
        return s.toString();
    }

    private String exceptionMessage2String(Throwable ex) {
        if(ex == null) {
            return "undefined";
        }
        StringBuilder s = new StringBuilder();
        String className = ex.getClass().getName();
        s.append(className);
        String msg = ex.getMessage();
        if(GwtHelper.isNotEmpty(msg)) {
            s.append(": ");
            s.append(msg);
        }
        return s.toString();
    }

    private void sendErrorToServer(String msg, String stacktrace) {
        /*GwtClientExceptionDto dto = new GwtClientExceptionDto(msg, stacktrace);
        String url = GwtUrlBuilder.get().url4rest(RestUrls.EXCEPTIONS);
        new RequestBuilder(url)
                .hidden()
                .callWithDto(dto, new SilentCallback());*/
    }

    // --------------------------------------------------------------------

    private static class StringOutputStream extends OutputStream {
        private StringBuilder buf = new StringBuilder();

        public void write( byte[] b ) throws IOException {
            buf.append( new String( b ) );
        }

        public void write( byte[] b, int off, int len ) throws IOException {
            buf.append( new String( b, off, len ) );
        }

        public void write( int b ) throws IOException {
            byte[] bytes = new byte[1];
            bytes[0] = (byte)b;
            buf.append( new String( bytes ) );
        }

        public String toString() {
            return buf.toString();
        }

    }

}
