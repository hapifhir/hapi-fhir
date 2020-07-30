package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiBatchSvc;
import ca.uhn.fhir.empi.api.IEmpiResetSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.provider.EmpiProviderR4;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.validation.IResourceLoader;
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
	private IEmpiPersonMergerSvc myPersonMergerSvc;
	@Autowired
	private IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;
	@Autowired
	private IEmpiLinkQuerySvc myEmpiLinkQuerySvc;
	@Autowired
	private IResourceLoader myResourceLoader;
	@Autowired
	private IEmpiSettings myEmpiSettings;
	@Autowired
	private IEmpiResetSvc myEmpiExpungeSvc;
	@Autowired
	private IEmpiBatchSvc myEmpiBatchSvc;

	private String defaultScript;

	protected void setEmpiRuleJson(String theString) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theString);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		((EmpiSettings)myEmpiSettings).getScriptText();
		((EmpiSettings)myEmpiSettings).setScriptText(json);
	}

	@BeforeEach
	public void before() {
		myEmpiProviderR4 = new EmpiProviderR4(myFhirContext, myEmpiMatchFinderSvc, myPersonMergerSvc, myEmpiLinkUpdaterSvc, myEmpiLinkQuerySvc, myResourceLoader, myEmpiExpungeSvc, myEmpiBatchSvc);
		defaultScript = ((EmpiSettings)myEmpiSettings).getScriptText();
	}
	@AfterEach
	public void after() throws IOException {
		super.after();
		((EmpiSettings)myEmpiSettings).setScriptText(defaultScript);
	}
}
