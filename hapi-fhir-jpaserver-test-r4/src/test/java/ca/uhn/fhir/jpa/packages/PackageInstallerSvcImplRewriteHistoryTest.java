package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.ConceptMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PackageInstallerSvcImplRewriteHistoryTest extends BaseJpaR4Test {
	@Autowired
	PackageInstallerSvcImpl mySvc;


	@AfterEach
	void after() {
		myDaoConfig.setUpdateWithHistoryRewriteEnabled(false);
	}

	@Test
	void svc_notnull() {
		assertNotNull(mySvc);
	}

	@Test
	void updateWithHistoryRewriteEnabled() {
		myDaoConfig.setUpdateWithHistoryRewriteEnabled(true);

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setId("ConceptMap/testcm");
		mySvc.updateResource(myConceptMapDao, conceptMap);
	}
}
