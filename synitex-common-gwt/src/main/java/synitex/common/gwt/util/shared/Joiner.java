package synitex.common.gwt.util.shared;

import com.google.common.base.Function;

import java.util.Collection;
import java.util.Iterator;

public class Joiner {

    private final String delim;

    private Joiner(String  delim) {
        this.delim = delim;
    }

    public static Joiner on(String delim) {
        return new Joiner(delim);
    }

    public <T> String join(Collection<T> collection, Function<T, String> function) {
        if(collection == null || collection.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            T part = it.next();
            String stringPart = function.apply(part);
            if(stringPart != null && !"".equals(stringPart)) {
                sb.append(stringPart);
                if(it.hasNext()) {
                    sb.append(delim);
                }
            }
        }
        return sb.toString();
    }

}
