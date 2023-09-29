package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.Optional;
import javax.annotation.Nonnull;

public interface ICdsCrDiscoveryServiceRegistry {
	void register(
			@Nonnull FhirVersionEnum theFhirVersion, @Nonnull Class<? extends ICrDiscoveryService> ICrDiscoveryService);

	void unregister(@Nonnull FhirVersionEnum theFhirVersion);

	Optional<Class<? extends ICrDiscoveryService>> find(@Nonnull FhirVersionEnum theFhirVersion);
}
