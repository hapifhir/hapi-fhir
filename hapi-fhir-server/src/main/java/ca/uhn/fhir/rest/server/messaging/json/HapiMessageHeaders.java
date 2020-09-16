package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.model.api.IModelJson;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is for holding headers for BaseJsonMessages. Any serializable data can be thrown into
 * the header map. There are also three special headers, defined by the constants in this class, which are for use
 * in message handling retrying. There are also matching helper functions for fetching those special variables; however
 * they can also be accessed in standard map fashion with a `get` on the map.
 */
public class HapiMessageHeaders implements Map<String, Object>, IModelJson {
    public static String RETRY_COUNT_KEY = "retryCount";
    public static String FIRST_FAILURE_KEY = "firstFailure";
    public static String LAST_FAILURE_KEY = "lastFailure";

    private final Map<String, Object> headers;

    public HapiMessageHeaders(Map<String, Object> theHeaders) {
        headers = theHeaders;
    }

    public HapiMessageHeaders() {
        headers = new HashMap<>();
    }


    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public Object get(Object key) {
         return this.headers.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return this.headers.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        this.headers.putAll(m);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.headers.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.headers.entrySet();
    }

    public Integer getRetryCount() {
        return (Integer)this.getHeaders().get(RETRY_COUNT_KEY);
    }

    public Long getFirstFailureDate() {
    	return (Long)this.getHeaders().get(FIRST_FAILURE_KEY);
    }

    public Long getLastFailureDate() {
    	return (Long)this.getHeaders().get(LAST_FAILURE_KEY);
    }

    public void setRetryCount(Integer theRetryCount) {
    	this.getHeaders().put(RETRY_COUNT_KEY, theRetryCount);
	 }

    public void setLastFailureDate(Long theLastFailureDate) {
    	this.getHeaders().put(LAST_FAILURE_KEY, theLastFailureDate);
	 }
	public void setFirstFailureDate(Long theFirstFailureDate) {
    	this.getHeaders().put(FIRST_FAILURE_KEY, theFirstFailureDate);
	 }
    public Map<String, Object> getHeaders() {
        return this.headers;
    }

	/**
	 * Sets deffault values for the special headers that HAPI cares about during retry.
	 */
	public void initializeDefaultRetryValues() {
        headers.put(RETRY_COUNT_KEY, 0);
        headers.put(FIRST_FAILURE_KEY, null);
        headers.put(LAST_FAILURE_KEY, null);
    }
}
