package ca.uhn.fhir.jpa.provider.r4;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.function.Consumer;

/**
 * Pre-save hook for Consent saved during $member-match.
 */
public interface IMemberMatchConsentHook extends Consumer<IBaseResource> {
}
