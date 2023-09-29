package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

public class CdsCrDiscoveryServiceRegistry implements ICdsCrDiscoveryServiceRegistry {
	private final Map<FhirVersionEnum, Class<? extends ICrDiscoveryService>> myCrDiscoveryServices;

	public CdsCrDiscoveryServiceRegistry() {
		myCrDiscoveryServices = new HashMap<>();
		myCrDiscoveryServices.put(FhirVersionEnum.DSTU3, CrDiscoveryServiceDstu3.class);
		myCrDiscoveryServices.put(FhirVersionEnum.R4, CrDiscoveryServiceR4.class);
		myCrDiscoveryServices.put(FhirVersionEnum.R5, CrDiscoveryServiceR5.class);
	}

	public void register(
			@Nonnull FhirVersionEnum theFhirVersion,
			@Nonnull Class<? extends ICrDiscoveryService> theCrDiscoveryService) {
		myCrDiscoveryServices.put(theFhirVersion, theCrDiscoveryService);
	}

	public void unregister(@Nonnull FhirVersionEnum theFhirVersion) {
		myCrDiscoveryServices.remove(theFhirVersion);
	}

	@Override
	public Optional<Class<? extends ICrDiscoveryService>> find(@Nonnull FhirVersionEnum theFhirVersion) {
		return Optional.empty();
	}
}
