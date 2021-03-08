package ca.uhn.fhir.util;

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.lang.reflect.Field;
import java.util.function.BiPredicate;

/**
 * Boolean-value function for comparing two FHIR primitives via <code>.equals()</code> method on the instance
 * internal values.
 */
public class PrimitiveTypeEqualsPredicate implements BiPredicate {

	/**
	 * Returns true if both bases are of the same type and hold the same values.
	 */
	@Override
	public boolean test(Object theBase1, Object theBase2) {
		if (theBase1 == null) {
			return theBase2 == null;
		}
		if (theBase2 == null) {
			return false;
		}
		if (!theBase1.getClass().equals(theBase2.getClass())) {
			return false;
		}

		for (Field f : theBase1.getClass().getDeclaredFields()) {
			Class<?> fieldClass = f.getType();

			if (!IPrimitiveType.class.isAssignableFrom(fieldClass)) {
				continue;
			}

			IPrimitiveType<?> val1, val2;

			f.setAccessible(true);
			try {
				val1 = (IPrimitiveType<?>) f.get(theBase1);
				val2 = (IPrimitiveType<?>) f.get(theBase2);
			} catch (Exception e) {
				// swallow
				continue;
			}

			if (val1 == null && val2 == null) {
				continue;
			}

			if (val1 == null || val2 == null) {
				return false;
			}

			Object actualVal1 = val1.getValue();
			Object actualVal2 = val2.getValue();

			if (actualVal1 == null && actualVal2 == null) {
				continue;
			}
			if (actualVal1 == null) {
				return false;
			}
			if (!actualVal1.equals(actualVal2)) {
				return false;
			}
		}

		return true;
	}
}
