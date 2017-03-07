package embedded.example;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import jersey.repackaged.com.google.common.collect.Maps;

@Singleton
@Path("Patient")
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsPatientProvider extends AbstractJaxRsResourceProvider<Patient> {

	private final Map<String, Patient> patients = Maps.newConcurrentMap();
	private final SseBroadcaster broadcaster = new SseBroadcaster();

	@Inject
	public JaxRsPatientProvider() {
		super(FhirContext.forDstu3(), JaxRsPatientProvider.class);
	}

	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
		final List<Patient> result = new LinkedList<Patient>();
		for (final Patient patient : patients.values()) {
			Patient single = null;
			if (name == null
					|| patient.getName().get(0).getFamilyElement().getValueNotNull().equals(name.getValueNotNull())) {
				single = patient;
			}
			if (single != null) {
				result.add(single);
			}
		}
		return result;
	}

	@Create
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam final String theConditional)
			throws Exception {

		storePatient(patient);

		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(patient);
		result.setId(new IdType(patient.getId()));
		return result;
	}

	// Conceptual wrapper for storing in a db
	private void storePatient(final Patient patient) {

		try {
			patients.put(patient.getIdentifierFirstRep().getValue(), patient);
			// if storing is successful the notify the listeners that listens on
			// any patient => patient/*

			final String bundleToString = currentPatientsAsJsonString();

			broadcaster
					.broadcast(new OutboundEvent.Builder().name("patients").data(String.class, bundleToString).build());

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private String currentPatientsAsJsonString() {
		final IParser jsonParser = this.getFhirContext().newJsonParser().setPrettyPrint(true);
		final org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		for (final Patient p : patients.values())
			bundle.addEntry(new BundleEntryComponent().setResource(p));
		final String bundleToString = jsonParser.encodeResourceToString(bundle);
		return bundleToString;
	}

	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@GET
	@Path("listen")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput listenToBroadcast() throws IOException {
		final EventOutput eventOutput = new EventOutput();
		
		final String bundleToString = currentPatientsAsJsonString();
		
		eventOutput.write(
				new OutboundEvent.Builder().name("patients").data(String.class, bundleToString).build());
		this.broadcaster.add(eventOutput);
		return eventOutput;
	}
}
