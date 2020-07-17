package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageInstallerSvcImplTest {
	private final FhirContext myCtx = FhirContext.forCached(FhirVersionEnum.R4);
	private PackageInstallerSvcImpl mySvc;


	@BeforeEach
	public void before() {
		mySvc = new PackageInstallerSvcImpl();
		mySvc.setFhirContextForUnitTest(myCtx);
	}

	@Test
	public void testValidForUpload_SearchParameterWithMetaParam() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("_id");
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoBase() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoExpression() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}


	@Test
	public void testValidForUpload_GoodSearchParameter() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertTrue(mySvc.validForUpload(sp));
	}

}
