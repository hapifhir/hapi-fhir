package ca.uhn.fhir.jpa.graphql;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.utils.GraphQLEngine.IGraphQLStorageServices;
import org.hl7.fhir.r4.utils.GraphQLEngine.IGraphQLStorageServices.ReferenceResolution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;

public class JpaStorageServicesTest extends BaseResourceProviderR4Test {
	private IIdType myPatientId;

	@Override
	@Before
	public void before() throws Exception {
		super.before();

		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.addName().setFamily("FAM");
		myPatientId = myPatientDao.update(p).getId().toVersionless();
	}
	
	public IGraphQLStorageServices storageServices() {
		return this.myAppCtx.getBean(IGraphQLStorageServices.class);
	}
	
	@Test
	public void testLookupById() {
		Patient result = (Patient) storageServices().lookup(null, myPatientId.getResourceType(), myPatientId.getIdPart());
		Assert.assertNotNull(result);
		Assert.assertEquals("FAM", result.getName().get(0).getFamily());
	}

	@Test
	public void testLookupByReference() {
		ReferenceResolution result = storageServices().lookup(null, null, new Reference(myPatientId));
		Assert.assertNotNull(result);
	}
}
