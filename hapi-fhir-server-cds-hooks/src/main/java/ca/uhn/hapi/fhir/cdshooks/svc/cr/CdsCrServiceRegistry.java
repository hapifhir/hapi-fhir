package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

public class CdsCrServiceRegistry implements ICdsCrServiceRegistry {
	private final Map<FhirVersionEnum, Class<? extends ICdsCrService>> myCdsCrServices;

	public CdsCrServiceRegistry() {
		myCdsCrServices = new HashMap<>();
		myCdsCrServices.put(FhirVersionEnum.DSTU3, CdsCrServiceDstu3.class);
		myCdsCrServices.put(FhirVersionEnum.R4, CdsCrServiceR4.class);
		myCdsCrServices.put(FhirVersionEnum.R5, CdsCrServiceR5.class);
	}

	public void register(
			@Nonnull FhirVersionEnum theFhirVersion, @Nonnull Class<? extends ICdsCrService> theCdsCrService) {
		myCdsCrServices.put(theFhirVersion, theCdsCrService);
	}

	public void unregister(@Nonnull FhirVersionEnum theFhirVersion) {
		myCdsCrServices.remove(theFhirVersion);
	}

	public Optional<Class<? extends ICdsCrService>> find(@Nonnull FhirVersionEnum theFhirVersion) {
		return Optional.ofNullable(myCdsCrServices.get(theFhirVersion));
	}
}
