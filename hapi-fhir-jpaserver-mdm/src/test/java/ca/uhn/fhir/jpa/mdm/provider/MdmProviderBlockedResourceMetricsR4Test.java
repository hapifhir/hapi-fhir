package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.mdm.config.BlockListConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MdmSearchParamBuildingUtils;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Regression test for https://github.com/hapifhir/hapi-fhir/issues/8440
 * <p>
 * When a patient is blocked from MDM matching, its golden resource carries the
 * BLOCKED tag and MDM metrics count it as an excluded resource. If the blocklist
 * is later cleared and the patient is re-checked (update), the golden resource
 * must lose the BLOCKED tag so metrics no longer count it as excluded.
 */
// Created by Muse Spark (AI assistance was used in preparing this change)
@ContextConfiguration(classes = {BlockListConfig.class})
public class MdmProviderBlockedResourceMetricsR4Test extends BaseMdmProviderR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IBlockListRuleProvider myBlockListRuleProvider;

	@Test
	void unblockedPatientUpdate_removesBlockedTagFromGoldenResourceSoMetricsNoLongerCountItAsExcluded()
			throws Exception {
		// setup: Jane Doe is on the block list
		blockJaneDoe();
		Patient jane = (Patient) myMdmHelper.createWithLatch(buildJanePatient()).getDaoMethodOutcome().getResource();
		Patient janeGolden = getGoldenResourceFromTargetResource(jane);

		// sanity: the golden resource is blocked and metrics count it as excluded
		assertThat(MdmResourceUtil.isBlockedGoldenResource(janeGolden)).isTrue();
		assertThat(countBlockedGoldenResources()).isEqualTo(1);

		// the block list is cleared, and Jane is re-checked via update
		when(myBlockListRuleProvider.getBlocklistRules()).thenReturn(new BlockListJson());
		jane.setGender(Enumerations.AdministrativeGender.FEMALE);
		myMdmHelper.updateWithLatch(jane);

		// validate: the golden resource is no longer blocked, so it is not excluded
		Patient updatedGolden = myPatientDao.read(
				janeGolden.getIdElement().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertThat(MdmResourceUtil.isBlockedGoldenResource(updatedGolden)).isFalse();
		assertThat(countBlockedGoldenResources()).isEqualTo(0);
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

	/**
	 * Runs the exact search the MDM metrics service uses to count excluded resources.
	 */
	private long countBlockedGoldenResources() {
		SearchParameterMap map = MdmSearchParamBuildingUtils.buildSearchParameterForBlockedResourceCount("Patient");
		map.setCount(0);
		map.setLoadSynchronous(true);
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		IBundleProvider outcome = myPatientDao.search(map, new SystemRequestDetails());
		return outcome.size();
	}
}
