package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.TerserUtil;
import ca.uhn.fhir.util.TerserUtilHelper;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Interceptor
public class ResourceFlatteningInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceFlatteningInterceptor.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	public ResourceFlatteningInterceptor(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}


	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdatedPreCommit(RequestDetails theRequest, IBaseResource theResource) {
		ourLog.debug("Validating address on for create {}, {}", theResource, theRequest);
		handleRequest(theRequest, theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreatedPreCommit(RequestDetails theRequest, IBaseResource theResource) {
		ourLog.debug("Validating address on for create {}, {}", theResource, theRequest);
		handleRequest(theRequest, theResource);
	}

	private void handleRequest(RequestDetails theRequest, IBaseResource theResource) {
		switch (myFhirContext.getResourceType(theResource)) {
			case "Location":
				flattenLocation(theResource);
				return;
			case "Account":
				flattenAccount(theResource);
				return;
			case "Role":
				flattenRole(theResource);
				return;
			default:
				return;
		}
	}

	private void flattenLocation(IBaseResource theResource) {
		TerserUtilHelper helper = TerserUtilHelper.newHelper(myFhirContext, theResource);
		IBase managingOrganizationsRef = helper.getFieldValue("managingOrganization");
		if (managingOrganizationsRef == null) {
			ourLog.info("No organization is associated with {}", theResource);
			return;
		}

		IBaseResource referencedOrg = ((IBaseReference) managingOrganizationsRef).getResource();
		if (referencedOrg == null) {
			ourLog.warn("Missing value for reference {}", referencedOrg);
			return;
		}
		TerserUtil.getValues(myFhirContext, referencedOrg, "address").clear();

		IBase newAddress = helper.getFieldValue("address");
		TerserUtil.setFieldByFhirPath(myFhirContext, "address", referencedOrg, newAddress);

		IFhirResourceDao dao = getDao(referencedOrg);
		dao.update(referencedOrg);
	}

	private void flattenAccount(IBaseResource theAccount) {
		TerserUtilHelper account = TerserUtilHelper.newHelper(myFhirContext, theAccount);
		List<IBase> owner = account.getFieldValues("owner");
		if (owner.isEmpty()) {
			ourLog.info("No organization is associated with {}", theAccount);
			return;
		}

		for (IBase o : owner) {
			IBaseResource org = ((IBaseReference) o).getResource();
			if (org == null) {
				ourLog.warn("Missing value for reference {}", o);
				continue;
			}

			IBaseExtension ext = ExtensionUtil.getOrCreateExtension(org, "http://hapifhir.org/Flattener/Account");
			IPrimitiveType status = (IPrimitiveType) TerserUtil.getFirstFieldByFhirPath(myFhirContext, "status", theAccount);
			ExtensionUtil.setExtension(myFhirContext, ext, status.getValueAsString());

			IFhirResourceDao dao = getDao(org);
			dao.update(org);
		}
	}

	// PractitionerRole.specialty becomes Practitioner.Extension.valueCode
	private void flattenRole(IBaseResource theRole) {
		TerserUtilHelper role = TerserUtilHelper.newHelper(myFhirContext, theRole);

		List<IBase> specialties = role.getFieldValues("specialty");
		if (specialties.isEmpty()) {
			ourLog.info("No specialties are associated with {}", theRole);
			return;
		}

		for (IBase o : specialties) {
			IBaseResource practitioner = (IBaseResource) TerserUtil.getValue(myFhirContext, theRole, "practitioner");
			if (practitioner == null) {
				ourLog.warn("Practitioner is not set on {}", theRole);
				continue;
			}

			String roleUrl = "http://hapifhir.org/Flattener/Role/" + theRole.toString();

			IBaseExtension ext = ExtensionUtil.getOrCreateExtension(practitioner, roleUrl);
			ext.setValue(new CodeDt(String.valueOf(role.getFieldValue("status"))));

			IFhirResourceDao dao = getDao(practitioner);
			dao.update(practitioner);
		}
	}

	private IFhirResourceDao getDao(IBaseResource theReferencedOrg) {
		String resourceType = myFhirContext.getResourceType(theReferencedOrg);
		return myDaoRegistry.getResourceDao(resourceType);
	}
}
