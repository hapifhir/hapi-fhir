package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class DependencyManager {

	public static final String RESOURCE_TYPE = "Basic";

	// package visible for use in tests
	static final String EXTENSION_URL =
			"http://hapifhir.io/fhir/StructureDefinition/package-loader-dependency-management";
	static final String SUBEXTENSION_NAME_URL = "name";
	static final String SUBEXTENSION_VERSION_URL = "version";

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final PartitionSettings myPartitionSettings;

	DependencyManager(FhirContext theFhirContext, DaoRegistry theDaoRegistry, PartitionSettings thePartitionSettings) {
		this.myDaoRegistry = theDaoRegistry;
		this.myFhirContext = theFhirContext;
		this.myPartitionSettings = thePartitionSettings;
	}

	public String createDependencyResource() {
		IBaseResource basic = TerserUtil.newResource(myFhirContext, RESOURCE_TYPE);

		DaoMethodOutcome outcome = myDaoRegistry.getResourceDao(basic).create(basic, createRequestDetails());
		return outcome.getId().toUnqualifiedVersionless().getValueAsString();
	}

	@SuppressWarnings("unchecked")
	public <T extends IBaseResource> boolean shouldProcessDependency(
			String theId, String thePackageName, String thePackageVersion) {
		IIdType idType = TerserUtil.newElement(myFhirContext, "id", theId);
		Class<T> implementingClass =
				(Class<T>) myFhirContext.getResourceDefinition(RESOURCE_TYPE).getImplementingClass();
		IFhirResourceDao<T> basicDao = myDaoRegistry.getResourceDao(implementingClass);

		T resource = basicDao.read(idType, createRequestDetails());

		if (resourceContainsDependency(resource, thePackageName, thePackageVersion)) {
			return false;
		}

		addDependencyToResource(resource, thePackageName, thePackageVersion);
		basicDao.update(resource, createRequestDetails());

		return true;
	}

	public void deleteDependencyResource(String theId) {
		IIdType idType = TerserUtil.newElement(myFhirContext, "id", theId);
		Class<? extends IBaseResource> implementingClass =
				myFhirContext.getResourceDefinition(RESOURCE_TYPE).getImplementingClass();
		myDaoRegistry.getResourceDao(implementingClass).delete(idType, createRequestDetails());
	}

	private RequestDetails createRequestDetails() {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		if (myPartitionSettings.isPartitioningEnabled()) {
			requestDetails.setRequestPartitionId(myPartitionSettings.getDefaultRequestPartitionId());
		}
		return requestDetails;
	}

	private boolean resourceContainsDependency(
			IBaseResource theResource, String thePackageName, String thePackageVersion) {
		List<IBase> extensions = TerserUtil.getValues(myFhirContext, theResource, "extension");

		if (extensions == null) {
			return false;
		}

		return extensions.stream().anyMatch(t -> isExtensionMatches(t, thePackageName, thePackageVersion));
	}

	@SuppressWarnings("unchecked")
	private boolean isExtensionMatches(IBase theExtension, String thePackageName, String thePackageVersion) {
		IPrimitiveType<String> url =
				(IPrimitiveType<String>) TerserUtil.getFirstFieldByFhirPath(myFhirContext, "url", theExtension);
		if (url == null || !Strings.CS.equals(EXTENSION_URL, url.getValue())) {
			return false;
		}

		List<IBase> subExtensions = TerserUtil.getFieldByFhirPath(myFhirContext, "extension", theExtension);
		if (subExtensions == null || subExtensions.size() != 2) {
			return false;
		}

		boolean match = true;
		for (IBase subExtension : subExtensions) {
			IPrimitiveType<String> subUrl =
					(IPrimitiveType<String>) TerserUtil.getFirstFieldByFhirPath(myFhirContext, "url", subExtension);
			IPrimitiveType<String> subValue = (IPrimitiveType<String>)
					TerserUtil.getFirstFieldByFhirPath(myFhirContext, "valueString", subExtension);
			match &= (subUrl != null && subValue != null)
					&& ((Strings.CS.equals(subUrl.getValue(), SUBEXTENSION_NAME_URL)
									&& Strings.CS.equals(subValue.getValue(), thePackageName))
							|| (Strings.CS.equals(subUrl.getValue(), SUBEXTENSION_VERSION_URL)
									&& Strings.CS.equals(subValue.getValue(), thePackageVersion)));
		}

		return match;
	}

	private void addDependencyToResource(IBaseResource theResource, String thePackageName, String thePackageVersion) {
		FhirTerser terser = myFhirContext.newTerser();

		IBase newExtension = terser.addElement(theResource, "extension");
		terser.addElement(newExtension, "url", EXTENSION_URL);

		IBase packageNameExtension = terser.addElement(newExtension, "extension");
		terser.addElement(packageNameExtension, "url", SUBEXTENSION_NAME_URL);
		terser.addElement(packageNameExtension, "valueString", thePackageName);

		IBase packageVersionExtension = terser.addElement(newExtension, "extension");
		terser.addElement(packageVersionExtension, "url", SUBEXTENSION_VERSION_URL);
		terser.addElement(packageVersionExtension, "valueString", thePackageVersion);
	}
}
