package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DependencyManager {

	private static final Logger ourLog = LoggerFactory.getLogger(DependencyManager.class);

	public static final String RESOURCE_TYPE = "Basic";

	// package visible for use in tests
	static final String EXTENSION_URL =
			"http://hapifhir.io/fhir/StructureDefinition/package-loader-dependency-management";
	static final String SUBEXTENSION_NAME_URL = "name";
	static final String SUBEXTENSION_VERSION_URL = "version";

	public static final String FIELD_ID = "id";
	public static final String FIELD_EXTENSION = "extension";
	public static final String FIELD_URL = "url";
	public static final String FIELD_VALUE = "valueString";

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

		DaoMethodOutcome outcome = getBasicDao().create(basic, createRequestDetails());
		return outcome.getId().toUnqualifiedVersionless().getValueAsString();
	}

	public <T extends IBaseResource> boolean shouldProcessDependency(
			String theId, String thePackageName, String thePackageVersion) {
		IIdType idType = TerserUtil.newElement(myFhirContext, FIELD_ID, theId);
		IFhirResourceDao<T> basicDao = getBasicDao();

		try {
			T resource = basicDao.read(idType, createRequestDetails());

			if (resourceContainsDependency(resource, thePackageName, thePackageVersion)) {
				return false;
			}

			addDependencyToResource(resource, thePackageName, thePackageVersion);
			basicDao.update(resource, createRequestDetails());
		} catch (ResourceNotFoundException | ResourceGoneException e) {
			// This should never happen
			// The lifespan of the resource is controlled by the root job.
			// A dependency job should never find that the resource hasn't been created yet
			//   or has already been destroyed.
			// However, if it does happen, we should carry on to install the dependency anyway.
			ourLog.error("Failed to process dependency resource", e);
		}

		return true;
	}

	public void deleteDependencyResource(String theId) {
		IIdType idType = TerserUtil.newElement(myFhirContext, FIELD_ID, theId);

		getBasicDao().delete(idType, createRequestDetails());
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> getBasicDao() {
		Class<T> implementingClass =
				(Class<T>) myFhirContext.getResourceDefinition(RESOURCE_TYPE).getImplementingClass();
		return myDaoRegistry.getResourceDao(implementingClass);
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
		List<IBase> extensions = TerserUtil.getValues(myFhirContext, theResource, FIELD_EXTENSION);

		if (extensions == null) {
			return false;
		}

		return extensions.stream().anyMatch(t -> isExtensionMatches(t, thePackageName, thePackageVersion));
	}

	@SuppressWarnings("unchecked")
	private boolean isExtensionMatches(IBase theExtension, String thePackageName, String thePackageVersion) {
		IPrimitiveType<String> url =
				(IPrimitiveType<String>) TerserUtil.getFirstFieldByFhirPath(myFhirContext, FIELD_URL, theExtension);
		if (url == null || !Strings.CS.equals(EXTENSION_URL, url.getValue())) {
			return false;
		}

		List<IBase> subExtensions = TerserUtil.getFieldByFhirPath(myFhirContext, FIELD_EXTENSION, theExtension);
		if (subExtensions == null || subExtensions.size() != 2) {
			return false;
		}

		boolean match = true;
		for (IBase subExtension : subExtensions) {
			IPrimitiveType<String> subUrl =
					(IPrimitiveType<String>) TerserUtil.getFirstFieldByFhirPath(myFhirContext, FIELD_URL, subExtension);
			IPrimitiveType<String> subValue = (IPrimitiveType<String>)
					TerserUtil.getFirstFieldByFhirPath(myFhirContext, FIELD_VALUE, subExtension);
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

		IBase newExtension = terser.addElement(theResource, FIELD_EXTENSION);
		terser.addElement(newExtension, FIELD_URL, EXTENSION_URL);

		IBase packageNameExtension = terser.addElement(newExtension, FIELD_EXTENSION);
		terser.addElement(packageNameExtension, FIELD_URL, SUBEXTENSION_NAME_URL);
		terser.addElement(packageNameExtension, FIELD_VALUE, thePackageName);

		IBase packageVersionExtension = terser.addElement(newExtension, FIELD_EXTENSION);
		terser.addElement(packageVersionExtension, FIELD_URL, SUBEXTENSION_VERSION_URL);
		terser.addElement(packageVersionExtension, FIELD_VALUE, thePackageVersion);
	}
}
