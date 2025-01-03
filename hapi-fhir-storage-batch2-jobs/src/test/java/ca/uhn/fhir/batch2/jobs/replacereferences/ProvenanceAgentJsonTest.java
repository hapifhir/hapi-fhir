package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.api.ProvenanceAgent;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProvenanceAgentJsonTest {

	private final FhirContext fhirContext = FhirContext.forR4();

	@Test
	void testFromAndToIProvenanceAgent_RoundTrip() {
		ProvenanceAgent agent = new ProvenanceAgent();
		IBaseReference who = new Reference();
		who.setReference("Practitioner/123");
		agent.setWho(who);

		IBaseReference onBehalfOf = new Reference();
		onBehalfOf.setReference("Organization/456");
		agent.setOnBehalfOf(onBehalfOf);

		ProvenanceAgentJson json = ProvenanceAgentJson.from(agent, fhirContext);
		assertThat(json).isNotNull();
		assertThat(json.getWho()).isNotNull();
		assertThat(json.getOnBehalfOf()).isNotNull();

		IProvenanceAgent result = ProvenanceAgentJson.toIProvenanceAgent(json, fhirContext);
		assertThat(result).isNotNull();
		assertThat(result.getWho().getReferenceElement().getValueAsString()).isEqualTo("Practitioner/123");
		assertThat(result.getOnBehalfOf().getReferenceElement().getValueAsString()).isEqualTo("Organization/456");
	}

	@Test
	void testToIProvenanceAgent_NullInput() {
		assertThat(ProvenanceAgentJson.toIProvenanceAgent(null, fhirContext)).isNull();
		assertThat(ProvenanceAgentJson.toIProvenanceAgents(null, fhirContext)).isNull();
	}

	@Test
	void testToIProvenanceAgents_NullInput() {
		assertThat(ProvenanceAgentJson.toIProvenanceAgents(null, fhirContext)).isNull();
	}

	@Test
	void testFrom_NullInput() {
		assertThat(ProvenanceAgentJson.from((IProvenanceAgent) null, fhirContext)).isNull();
	}

	@Test
	void testFromList_NullInput() {
		assertThat(ProvenanceAgentJson.from((List<IProvenanceAgent>) null, fhirContext)).isNull();
	}

	@Test
	void testFromAndToIProvenanceAgent_RoundTrip_PartialFields_NullOnBehalfOf() {
		ProvenanceAgent agent = new ProvenanceAgent();
		IBaseReference who = new Reference();
		who.setReference("Patient/789");
		agent.setWho(who);

		ProvenanceAgentJson json = ProvenanceAgentJson.from(agent, fhirContext);
		assertThat(json.getWho()).isNotNull();
		assertThat(json.getOnBehalfOf()).isNull();

		IProvenanceAgent result = ProvenanceAgentJson.toIProvenanceAgent(json, fhirContext);
		assertThat(result.getWho().getReferenceElement().getValueAsString()).isEqualTo("Patient/789");
		assertThat(result.getOnBehalfOf()).isNull();
	}

	@Test
	void testFromAndToIProvenanceAgent_RoundTrip_PartialFields_NullWho() {
		ProvenanceAgent agent = new ProvenanceAgent();
		IBaseReference onBehalfOf = new Reference();
		onBehalfOf.setReference("Organization/456");
		agent.setOnBehalfOf(onBehalfOf);

		ProvenanceAgentJson json = ProvenanceAgentJson.from(agent, fhirContext);
		assertThat(json.getWho()).isNull();
		assertThat(json.getOnBehalfOf()).isNotNull();

		IProvenanceAgent result = ProvenanceAgentJson.toIProvenanceAgent(json, fhirContext);
		assertThat(result.getWho()).isNull();
		assertThat(result.getOnBehalfOf().getReferenceElement().getValueAsString()).isEqualTo("Organization/456");
	}

	@Test
	void testFromAndToIProvenanceAgent_RoundTrip_PartialFields_IdentifierInWhoReference() {
		ProvenanceAgent agent = new ProvenanceAgent();
		Reference whoReference = new Reference();
		whoReference.getIdentifier().setSystem("http://example.org").setValue("who-identifier");
		agent.setWho(whoReference);

		ProvenanceAgentJson json = ProvenanceAgentJson.from(agent, fhirContext);
		assertThat(json.getWho()).isNotNull();
		assertThat(json.getOnBehalfOf()).isNull();

		IProvenanceAgent result = ProvenanceAgentJson.toIProvenanceAgent(json, fhirContext);
		assertThat(result.getOnBehalfOf()).isNull();
		assertThat(result.getWho()).isNotNull();
		Reference who = (Reference) result.getWho();
		assertThat(who.hasIdentifier()).isTrue();
		assertThat(who.getIdentifier().getSystem()).isEqualTo("http://example.org");
		assertThat(who.getIdentifier().getValue()).isEqualTo("who-identifier");
		assertThat(who.hasReference()).isFalse();
	}

	@Test
	void testFromAndToIProvenanceAgent_RoundTrip_PartialFields_IdentifierInOnBehalfOfReference() {
		ProvenanceAgent agent = new ProvenanceAgent();
		Reference onBehalfOfReference = new Reference();
		onBehalfOfReference.getIdentifier().setSystem("http://example.org").setValue("onBehalfOf-identifier");
		agent.setOnBehalfOf(onBehalfOfReference);

		ProvenanceAgentJson json = ProvenanceAgentJson.from(agent, fhirContext);
		assertThat(json.getWho()).isNull();
		assertThat(json.getOnBehalfOf()).isNotNull();

		IProvenanceAgent result = ProvenanceAgentJson.toIProvenanceAgent(json, fhirContext);
		assertThat(result.getWho()).isNull();
		assertThat(result.getOnBehalfOf()).isNotNull();
		Reference onBehalfOf = (Reference) result.getOnBehalfOf();
		assertThat(onBehalfOf.hasIdentifier()).isTrue();
		assertThat(onBehalfOf.getIdentifier().getSystem()).isEqualTo("http://example.org");
		assertThat(onBehalfOf.getIdentifier().getValue()).isEqualTo("onBehalfOf-identifier");
		assertThat(onBehalfOf.hasReference()).isFalse();
	}
}
