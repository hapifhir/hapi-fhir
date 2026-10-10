package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.mdm.config.BlockListConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Regression test for https://github.com/hapifhir/hapi-fhir/issues/8439
 * <p>
 * Merging the golden resource of a blocked patient into another golden resource
 * must succeed: the blocked patient's golden resource becomes a redirect to the
 * surviving golden resource. Before the fix, the merge failed with a
 * duplicate-key error while saving the blocked golden resource's tags.
 */
// Created by Muse Spark (AI assistance was used in preparing this change)
@ContextConfiguration(classes = {BlockListConfig.class})
public class MdmProviderMergeBlockedGoldenResourceR4Test extends BaseMdmProviderR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IBlockListRuleProvider myBlockListRuleProvider;

	@Test
	void mergeGoldenResourceOfBlockedPatient_succeedsAndRedirectsToSurvivor() {
		// setup: Jane Doe is on the block list, so MDM creates a lone golden resource for her
		blockJaneDoe();
		Patient jane =
				(Patient) myMdmHelper.createWithLatch(buildJanePatient()).getDaoMethodOutcome().getResource();
		Patient janeGolden = getGoldenResourceFromTargetResource(jane);
		assertThat(MdmResourceUtil.isBlockedGoldenResource(janeGolden)).isTrue();

		// Frank Doe is not blocked and matches nobody, so he gets his own golden resource
		Patient frank =
				(Patient) myMdmHelper.createWithLatch(buildFrankPatient()).getDaoMethodOutcome().getResource();
		Patient frankGolden = getGoldenResourceFromTargetResource(frank);
		assertThat(MdmResourceUtil.isBlockedGoldenResource(frankGolden)).isFalse();

		// merge G-Jane into G-Frank — this used to fail with a duplicate-key error on G-Jane's tags
		Patient merged = (Patient) myMdmProvider.mergeGoldenResources(
				new StringType(janeGolden.getIdElement().getValue()),
				new StringType(frankGolden.getIdElement().getValue()),
				null,
				myRequestDetails);

		// validate: the surviving golden resource is returned, and G-Jane now redirects to it
		assertThat(merged.getIdElement().getValue()).isEqualTo(frankGolden.getIdElement().getValue());
		Patient fromGoldenAfter = myPatientDao.read(
				janeGolden.getIdElement().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertThat(MdmResourceUtil.isGoldenRecord(fromGoldenAfter)).isFalse();
		assertThat(MdmResourceUtil.isGoldenRecordRedirected(fromGoldenAfter)).isTrue();
	}

	private void blockJaneDoe() {
		BlockListJson blockListJson = new BlockListJson();
		BlockListRuleJson rule = new BlockListRuleJson();
		rule.setResourceType("Patient");
		rule.addBlockListField().setFhirPath("name.single().family").setBlockedValue("Doe");
		rule.addBlockListField().setFhirPath("name.single().given.first()").setBlockedValue("Jane");
		blockListJson.addBlockListRule(rule);
		when(myBlockListRuleProvider.getBlocklistRules()).thenReturn(blockListJson);
	}
}
