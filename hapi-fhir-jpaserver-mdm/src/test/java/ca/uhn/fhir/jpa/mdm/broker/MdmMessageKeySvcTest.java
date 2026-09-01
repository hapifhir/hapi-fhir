package ca.uhn.fhir.jpa.mdm.broker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The message key decides which consumer a message is routed to, so two records for the same person must
 * produce the same key no matter what order their identifiers happen to appear in.
 */
// Created by claude-opus-5
public class MdmMessageKeySvcTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String MRN_SYSTEM = "http://example.com/mrn";
	private static final String NPI_SYSTEM = "http://example.com/npi";

	private MdmMessageKeySvc myMessageKeySvc;

	@BeforeEach
	public void before() {
		ISearchParamRegistry searchParamRetriever = mock(ISearchParamRegistry.class);
		when(searchParamRetriever.getActiveSearchParam(eq("Patient"), eq("identifier"), any()))
			.thenReturn(new RuntimeSearchParam(
				null, null, "identifier", "Description", "identifier", RestSearchParameterTypeEnum.STRING,
				new HashSet<>(), new HashSet<>(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
				null, null, null));

		MdmRulesJson rules = new MdmRulesJson();
		rules.setVersion("test version");
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystems("Patient", List.of(MRN_SYSTEM, NPI_SYSTEM));

		MdmSettings mdmSettings =
			new MdmSettings(new MdmRuleValidator(ourFhirContext, searchParamRetriever, null, null)).setMdmRules(rules);

		myMessageKeySvc = new MdmMessageKeySvc(new EIDHelper(ourFhirContext, mdmSettings));
	}

	@Test
	public void getMessageKey_identifierOrderReversed_returnsTheSameKey() {
		Patient oneOrder = new Patient();
		oneOrder.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		oneOrder.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		Patient reversed = new Patient();
		reversed.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));
		reversed.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		assertThat(myMessageKeySvc.getMessageKeyOrNull(oneOrder))
			.isEqualTo(myMessageKeySvc.getMessageKeyOrNull(reversed))
			.isEqualTo("mrn-1");
	}

	@Test
	public void getMessageKey_resourceCarriesOnlyTheSecondSystem_fallsBackToIt() {
		Patient patient = new Patient();
		patient.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		assertThat(myMessageKeySvc.getMessageKeyOrNull(patient)).isEqualTo("npi-9");
	}

	@Test
	public void getMessageKey_resourceHasNoEid_returnsNull() {
		assertThat(myMessageKeySvc.getMessageKeyOrNull(new Patient())).isNull();
	}
}
