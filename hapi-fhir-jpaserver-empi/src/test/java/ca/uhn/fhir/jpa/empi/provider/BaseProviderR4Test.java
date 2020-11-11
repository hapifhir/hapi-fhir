package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiExpungeSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiSubmitSvc;
import ca.uhn.fhir.empi.provider.EmpiProviderR4;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public abstract class BaseProviderR4Test extends BaseEmpiR4Test {
	EmpiProviderR4 myEmpiProviderR4;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private IEmpiControllerSvc myEmpiControllerSvc;
	@Autowired
	private IEmpiExpungeSvc myEmpiResetSvc;
	@Autowired
	private IEmpiSubmitSvc myEmpiBatchSvc;
	@Autowired
	private EmpiSettings myEmpiSettings;

	private String defaultScript;

	protected void setEmpiRuleJson(String theString) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theString);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		myEmpiSettings.setScriptText(json);

	}

	@BeforeEach
	public void before() {
		myEmpiProviderR4 = new EmpiProviderR4(myFhirContext, myEmpiControllerSvc, myEmpiMatchFinderSvc, myEmpiResetSvc, myEmpiBatchSvc);
		defaultScript = myEmpiSettings.getScriptText();
	}
	@AfterEach
	public void after() throws IOException {
		super.after();
		myEmpiSettings.setScriptText(defaultScript);
	}
}
