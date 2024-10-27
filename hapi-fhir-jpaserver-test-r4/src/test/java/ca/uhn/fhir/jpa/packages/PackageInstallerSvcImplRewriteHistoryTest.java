package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageInstallerSvcImplRewriteHistoryTest extends BaseJpaR4Test {
	public static final IIdType CONCEPT_MAP_TEST_ID = new IdDt("ConceptMap/PackageInstallerSvcImplRewriteHistoryTest");
	@Autowired
	PackageInstallerSvcImpl mySvc;


	@AfterEach
	void after() {
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(false);
	}

	@Test
	void svc_notnull() {
		assertNotNull(mySvc);
	}

	@Test
	void updateWithHistoryRewriteEnabled() {
		// setup
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(true);

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setId(CONCEPT_MAP_TEST_ID);
		conceptMap.setUrl("http://example.com/ConceptMap/testcm");

		// execute
		// red-green this threw a NPE before the fix
		mySvc.createOrUpdateResource(myConceptMapDao, conceptMap, null);

		// verify
		ConceptMap readConceptMap = myConceptMapDao.read(CONCEPT_MAP_TEST_ID);
		assertEquals(CONCEPT_MAP_TEST_ID.toString(), readConceptMap.getIdElement().toVersionless().toString());

	}
}
