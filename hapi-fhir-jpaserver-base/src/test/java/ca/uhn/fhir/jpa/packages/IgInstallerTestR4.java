package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class IgInstallerTestR4 extends BaseJpaR4Test {

	@Autowired
	public DaoConfig daoConfig;
	@Autowired
	public IgInstaller igInstaller;

	@Before
	public void setup() {
		daoConfig.setMyImplementationGuideURL("https://build.fhir.org/ig/hl7dk/dk-medcom/package.tgz"); // for R4
	}

	@Test
	public void installIgForR4() throws IOException {
		igInstaller.run();
	}
}
