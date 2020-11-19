package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmExpungeSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.provider.MdmProviderR4;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public abstract class BaseProviderR4Test extends BaseMdmR4Test {
	MdmProviderR4 myMdmProviderR4;
	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;
	@Autowired
	private IMdmControllerSvc myMdmControllerSvc;
	@Autowired
	private IMdmExpungeSvc myMdmExpungeSvc;
	@Autowired
	private IMdmSubmitSvc myMdmSubmitSvc;
	@Autowired
	private MdmSettings myMdmSettings;

	private String defaultScript;

	protected void setEmpiRuleJson(String theString) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theString);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		myMdmSettings.setScriptText(json);

	}

	@BeforeEach
	public void before() {
		myMdmProviderR4 = new MdmProviderR4(myFhirContext, myMdmControllerSvc, myMdmMatchFinderSvc, myMdmExpungeSvc, myMdmSubmitSvc);
		defaultScript = myMdmSettings.getScriptText();
	}
	@AfterEach
	public void after() throws IOException {
		super.after();
		myMdmSettings.setScriptText(defaultScript);
	}
}
