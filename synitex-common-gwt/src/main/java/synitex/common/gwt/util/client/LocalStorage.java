package synitex.common.gwt.util.client;

import com.google.common.base.Strings;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LocalStorage {

    private static final Logger log = Logger.getLogger(LocalStorage.class.getName());

    private static final long MONTH_IN_MILIS = 2592000000L;

    private static Map<String, LocalStorage> storagesByNamespaces = new HashMap<String, LocalStorage>();

    private String namespace;
    private Storage storage;

    public static LocalStorage forNamespace(String namespace) {
        LocalStorage storage = storagesByNamespaces.get(namespace);
        if(storage == null) {
            storage = new LocalStorage(namespace);
            storagesByNamespaces.put(namespace, storage);
        }
        return storage;
    }

    private LocalStorage(String namespace) {
        this.namespace = namespace;
        storage = Storage.getLocalStorageIfSupported();
        if(storage == null) {
            log.finer("LocalStorage is not supported. Will try to use session storage...");
            storage = Storage.getSessionStorageIfSupported();
            if(storage == null) {
                log.finer("SystemStorage is not supported. Will use coockies...");
                if(!Cookies.isCookieEnabled()) {
                    log.finer("Cookies are disabled. The items saved in storage will not be persisted.");
                }
            }
        }
    }

    public void set(String key, String value) {
        String safeValue = Strings.nullToEmpty(value);
        String storageKey = genStorageKey(key);
        if(storage != null) {
            storage.setItem(storageKey, safeValue);
        } else {
            Cookies.setCookie(storageKey, safeValue, getNextCookieExpirationDate());
        }
    }

    public String get(String key) {
        String storageKey = genStorageKey(key);
        String value = null;
        if(storage != null) {
            value = storage.getItem(storageKey);
        } else {
             value = Cookies.getCookie(storageKey);
        }
        return Strings.nullToEmpty(value);
    }

    public void remove(String key) {
        String storageKey = genStorageKey(key);
        if(storage != null) {
            storage.removeItem(storageKey);
        } else {
            Cookies.removeCookie(storageKey);
        }
    }

    private String genStorageKey(String key) {
        return namespace + "-" + key;
    }

    private Date getNextCookieExpirationDate() {
        long currentTime = System.currentTimeMillis();
        return new Date(currentTime + MONTH_IN_MILIS);
    }

}
