package ca.uhn.fhir.util.adapters;

import java.util.Optional;

public class AdapterUtils {

	/**
	 * Main entry point for adapter calls.
	 * Implements three conversions: cast to the target type, use IAdaptable if present, or lastly try the AdapterManager.INSTANCE.
	 * @param theObject the object to be adapted
	 * @param theTargetType the type of the adapter requested
	 */
	static <T> Optional<T> adapt(Object theObject, Class<T> theTargetType) {
		if (theTargetType.isInstance(theObject)) {
			//noinspection unchecked
			return Optional.of((T) theObject);
		}

		if (theObject instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) theObject;
			var adapted = adaptable.getAdapter(theTargetType);
			if (adapted.isPresent()) {
				return adapted;
			}
		}

		return AdapterManager.INSTANCE.getAdapter(theObject, theTargetType);
	}
}
