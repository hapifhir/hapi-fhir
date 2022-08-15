package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.provider.MdmControllerHelper;
import ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseProviderR4Test extends BaseMdmR4Test {
	protected MdmProviderDstu3Plus myMdmProvider;
	@Autowired
	private IMdmControllerSvc myMdmControllerSvc;
	@Autowired
	private IMdmSubmitSvc myMdmSubmitSvc;
	@Autowired
	private MdmSettings myMdmSettings;
	@Autowired
	private MdmControllerHelper myMdmHelper;
	@Autowired
	BatchJobHelper myBatchJobHelper;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	@Autowired
	MessageHelper myMessageHelper;

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private String defaultScript;

	protected void setMdmRuleJson(String theString) throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theString);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		myMdmSettings.setEnabled(true);
		myMdmSettings.setScriptText(json);
		myMdmResourceMatcherSvc.init();
	}

	@BeforeEach
	public void before() throws Exception {
		myMdmProvider = new MdmProviderDstu3Plus(myFhirContext, myMdmControllerSvc, myMdmHelper, myMdmSubmitSvc, myMdmSettings);
// FhirContext theFhirContext, IJobCoordinator theJobCoordinator, IRequestPartitionHelperSvc theRequestPartitionHelperSvc
		defaultScript = myMdmSettings.getScriptText();
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		super.after();
		myMdmSettings.setScriptText(defaultScript);
		myMdmResourceMatcherSvc.init();// This bugger creates new objects from the beans and then ignores them.
	}

	protected void clearMdmLinks() {
		Parameters result = (Parameters) myMdmProvider.clearMdmLinks(null, null, myRequestDetails);
		myBatch2JobHelper.awaitJobCompletion(BatchHelperR4.jobIdFromBatch2Parameters(result));
	}

	protected void clearMdmLinks(String theResourceName) {
		Parameters result = (Parameters) myMdmProvider.clearMdmLinks(getResourceNames(theResourceName), null, myRequestDetails);
		myBatch2JobHelper.awaitJobCompletion(BatchHelperR4.jobIdFromBatch2Parameters(result));
	}

	@Nonnull
	protected List<IPrimitiveType<String>> getResourceNames(String theResourceName) {
		List<IPrimitiveType<String>> resourceNames = new ArrayList<>();
		resourceNames.add(new StringType(theResourceName));
		return resourceNames;
	}
}
