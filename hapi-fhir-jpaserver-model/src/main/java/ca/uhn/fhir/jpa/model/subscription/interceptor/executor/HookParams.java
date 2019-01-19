package ca.uhn.fhir.jpa.model.subscription.interceptor.executor;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HookParams {

	private Map<Class<?>, Object> myParams = new HashMap<>();

	/**
	 * Constructor
	 */
	public HookParams() {
	}

	/**
	 * Constructor
	 */
	public HookParams(Object... theParams) {
		for (Object next : theParams) {
			add(next);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void add(T theNext) {
		Class<T> nextClass = (Class<T>) theNext.getClass();
		add(nextClass, theNext);
	}

	public <T> HookParams add(Class<T> theType, T theParam) {
		Validate.isTrue(myParams.containsKey(theType) == false, "Already have param of type %s", theType);
		myParams.put(theType, theParam);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> theParamType) {
		return (T) myParams.get(theParamType);
	}

	Set<Class<?>> getTypes() {
		return myParams.keySet();
	}
}
