package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.Optional;
import javax.annotation.Nonnull;

public interface ICdsCrServiceRegistry {
	void register(@Nonnull FhirVersionEnum theFhirVersion, @Nonnull Class<? extends ICdsCrService> theCdsCrService);

	void unregister(@Nonnull FhirVersionEnum theFhirVersion);

	Optional<Class<? extends ICdsCrService>> find(@Nonnull FhirVersionEnum theFhirVersion);
}
