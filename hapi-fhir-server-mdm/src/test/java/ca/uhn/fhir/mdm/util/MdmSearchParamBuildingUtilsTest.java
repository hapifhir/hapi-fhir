package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hl7.fhir.dstu2016may.model.Basic.SP_IDENTIFIER;

/**
 * An EID carrying a system but no value is legal FHIR and identifies nobody. A token search built from
 * one matches on the system alone, which would return every resource in that EID system, so it must
 * never reach a query.
 */
// Created by claude-opus-5
class MdmSearchParamBuildingUtilsTest {

	private static final String MRN_SYSTEM = "http://example.com/mrn";
	private static final String NPI_SYSTEM = "http://example.com/npi";

	@Test
	void buildEidTokenParam_eidsWithValues_matchesEachEidAgainstItsOwnSystem() {
		Optional<TokenOrListParam> tokenParam = MdmSearchParamBuildingUtils.buildEidTokenParam(
				List.of(new CanonicalEID(MRN_SYSTEM, "mrn-1", null), new CanonicalEID(NPI_SYSTEM, "npi-9", null)));

		assertThat(tokenParam).isPresent();
		assertThat(tokenParam.get().getValuesAsQueryTokens())
				.extracting(TokenParam::getSystem, TokenParam::getValue)
				.containsExactly(tuple(MRN_SYSTEM, "mrn-1"), tuple(NPI_SYSTEM, "npi-9"));
	}

	@Test
	void buildEidTokenParam_eidWithNoValue_isDropped() {
		Optional<TokenOrListParam> tokenParam = MdmSearchParamBuildingUtils.buildEidTokenParam(
				List.of(new CanonicalEID(MRN_SYSTEM, "mrn-1", null), new CanonicalEID(NPI_SYSTEM, null, null)));

		assertThat(tokenParam).isPresent();
		assertThat(tokenParam.get().getValuesAsQueryTokens())
				.extracting(TokenParam::getSystem, TokenParam::getValue)
				.containsExactly(tuple(MRN_SYSTEM, "mrn-1"));
	}

	@Test
	void buildEidTokenParam_everyEidValuelessOrBlank_isEmpty() {
		Optional<TokenOrListParam> tokenParam = MdmSearchParamBuildingUtils.buildEidTokenParam(
				Arrays.asList(new CanonicalEID(MRN_SYSTEM, null, null), new CanonicalEID(NPI_SYSTEM, "  ", null)));

		assertThat(tokenParam).isEmpty();
	}

	@Test
	void buildEidTokenParam_noEids_isEmpty() {
		assertThat(MdmSearchParamBuildingUtils.buildEidTokenParam(List.of())).isEmpty();
	}

	@Test
	void buildEidSearchParameterMap_searchableEids_restrictsToGoldenRecordsAndToThoseEids() {
		Optional<SearchParameterMap> map = MdmSearchParamBuildingUtils.buildEidSearchParameterMap(
				List.of(new CanonicalEID(MRN_SYSTEM, "mrn-1", null)));

		assertThat(map).isPresent();
		assertThat(map.get().get(PARAM_TAG)).isNotNull();
		assertThat(map.get().get(SP_IDENTIFIER)).isNotNull();
	}

	/**
	 * The map must not fall back to "every golden resource" when nothing is searchable, so no map is
	 * offered at all.
	 */
	@Test
	void buildEidSearchParameterMap_noSearchableEid_isEmpty() {
		assertThat(MdmSearchParamBuildingUtils.buildEidSearchParameterMap(
						List.of(new CanonicalEID(MRN_SYSTEM, null, null))))
				.isEmpty();
	}
}
