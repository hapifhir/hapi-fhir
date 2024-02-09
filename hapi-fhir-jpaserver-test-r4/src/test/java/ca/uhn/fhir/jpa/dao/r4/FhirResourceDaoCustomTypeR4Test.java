package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ })
public class FhirResourceDaoCustomTypeR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCustomTypeR4Test.class);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setDefaultTypeForProfile(CustomObservationR4.PROFILE, CustomObservationR4.class);
	}
	
	@Test
	public void testSaveAndRestore() {
		CustomObservationR4 obs = new CustomObservationR4();
		obs.setEyeColour(new StringType("blue"));
		
		IIdType id = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
		
		CustomObservationR4 read = (CustomObservationR4) myObservationDao.read(id);
		assertThat(read.getEyeColour().getValue()).isEqualTo("blue");
		
		IBundleProvider found = myObservationDao.search(new SearchParameterMap());
		assertThat(found.size().intValue()).isEqualTo(1);
		CustomObservationR4 search = (CustomObservationR4) found.getResources(0, 1).get(0);
		assertThat(search.getEyeColour().getValue()).isEqualTo("blue");
		
	}
	
	@AfterEach
	public void after() {
		myFhirContext.setDefaultTypeForProfile(CustomObservationR4.PROFILE, null);
	}
}
