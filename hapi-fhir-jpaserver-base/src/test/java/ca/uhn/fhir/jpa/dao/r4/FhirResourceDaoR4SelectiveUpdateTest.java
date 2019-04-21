package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.AfterClass;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.assertEquals;

public class FhirResourceDaoR4SelectiveUpdateTest extends BaseJpaR4Test {

	public static final String EUID_SYSTEM = "http://euid";

	@Test
	public void testInterceptorPreservesAttribute() throws Exception {
		CentralAttributesPreservationInterceptor interceptor = new CentralAttributesPreservationInterceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);

		// Create the patient with no additional identifier
		Patient p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("http://foo").setValue("bar");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();
		assertEquals("1", id.getVersionIdPart());

		// Update to add a preserved identifier
		p = new Patient();
		p.setId(id.toVersionless());
		p.setActive(true);
		p.addIdentifier(new Identifier().setSystem("http://foo").setValue("bar"));
		p.addIdentifier(new Identifier().setSystem(EUID_SYSTEM).setValue("123"));
		id = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("2", id.getVersionIdPart());

		// Update to change something but include the preserved attribute
		p = new Patient();
		p.setId(id.toVersionless());
		p.setActive(false);
		p.addIdentifier(new Identifier().setSystem("http://foo").setValue("bar"));
		id = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("3", id.getVersionIdPart());

		// Read it back
		p = myPatientDao.read(id);
		assertEquals(false, p.getActive());
		assertEquals(2, p.getIdentifier().size());

	}

	public class CentralAttributesPreservationInterceptor extends ServerOperationInterceptorAdapter {

		@Override
		public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
			Resource oldResource = (Resource) theOldResource;
			Resource newResource = (Resource) theNewResource;
			if (theOldResource instanceof Patient) {
				Patient oldPt = (Patient) oldResource;
				Patient newPt = (Patient) newResource;

				Identifier oldPtEuid = getEuidIdentifier(oldPt, false);
				if (oldPtEuid == null || isBlank(oldPtEuid.getValue())) {
					return;
				}

				Identifier newPtEuid = getEuidIdentifier(newPt, true);
				if (isBlank(newPtEuid.getValue())) {
					newPtEuid.setValue(oldPtEuid.getValue());
				}
			}
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static Identifier getEuidIdentifier(Patient thePt, boolean theCreate) {
		return thePt
			.getIdentifier()
			.stream()
			.filter(t -> t.getSystem().equals("http://euid"))
			.findFirst()
			.orElseGet(() -> {
				if (!theCreate) {
					return null;
				}
				Identifier identifier = new Identifier();
				identifier.setSystem(EUID_SYSTEM);
				identifier.setUse(Identifier.IdentifierUse.SECONDARY);
				thePt.getIdentifier().add(identifier);
				return identifier;
			});
	}


}
