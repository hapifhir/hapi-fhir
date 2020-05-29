package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

public class NpmJpaValidationSupport implements IValidationSupport {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiPackageCacheManager myHapiPackageCacheManager;

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return fetchResource("ValueSet", theUri);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theUri) {
		return fetchResource("CodeSystem", theUri);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUri) {
		return fetchResource("StructureDefinition", theUri);
	}

	@Nullable
	public IBaseResource fetchResource(String theResourceType, String theUri) {
		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
		IBaseResource asset = myHapiPackageCacheManager.loadPackageAssetByUrl(fhirVersion, theUri);
		if (asset != null) {
			Class<? extends IBaseResource> type = myFhirContext.getResourceDefinition(theResourceType).getImplementingClass();
			if (type.isAssignableFrom(asset.getClass())) {
				return asset;
			}
		}
		return null;
	}
}
