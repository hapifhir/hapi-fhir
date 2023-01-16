package ca.uhn.fhir.fhirpath;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFhirPathEvaluationContext {

	/**
	 * Evaluates the <code>resolve()</code> function and returns the target
	 * of the resolution.
	 *
	 * @param theReference The reference
	 * @param theContext The entity containing the reference. Note that this will be <code>null</code> for FHIR versions R4 and below.
	 */
	default IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
		return null;
	}
}
