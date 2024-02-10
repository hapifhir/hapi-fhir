package ca.uhn.fhir.jpa.ips.jpa;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JpaSectionSearchStrategyCollection {

	private Map<Class<? extends IBaseResource>, Object> mySearchStrategies;

	private JpaSectionSearchStrategyCollection(Map<Class<? extends IBaseResource>, Object> theSearchStrategies) {
		mySearchStrategies = theSearchStrategies;
	}

	@SuppressWarnings("unchecked")
	public <T extends IBaseResource> IJpaSectionSearchStrategy<T> getSearchStrategy(Class<T> theClass) {
		return (IJpaSectionSearchStrategy<T>) mySearchStrategies.get(theClass);
	}

	public Collection<Class<? extends IBaseResource>> getResourceTypes() {
		return mySearchStrategies.keySet();
	}

	public static JpaSectionSearchStrategyCollectionBuilder newBuilder() {
		return new JpaSectionSearchStrategyCollectionBuilder();
	}

	public static class JpaSectionSearchStrategyCollectionBuilder {
		private Map<Class<? extends IBaseResource>, Object> mySearchStrategies = new HashMap<>();

		public <T extends IBaseResource> JpaSectionSearchStrategyCollectionBuilder addStrategy(
				Class<T> theType, IJpaSectionSearchStrategy<T> theSearchStrategy) {
			mySearchStrategies.put(theType, theSearchStrategy);
			return this;
		}

		public JpaSectionSearchStrategyCollection build() {
			return new JpaSectionSearchStrategyCollection(mySearchStrategies);
		}
	}
}
