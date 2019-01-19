package ca.uhn.fhir.jpa.model.interceptor.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.stream.Collectors;

public class HookParams {

	private ListMultimap<Class<?>, Object> myParams = ArrayListMultimap.create();

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
		myParams.put(theType, theParam);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> theParamType, int theIndex) {
		List<T> objects = (List<T>) myParams.get(theParamType);
		T retVal = null;
		if (objects.size() > theIndex) {
			retVal = objects.get(theIndex);
		}
		return retVal;
	}

	/**
	 * Multivalued parameters will be returned twice in this list
	 */
	public List<String> getTypesAsSimpleName() {
		return myParams.values().stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList());
	}

}
