package ca.uhn.fhir.jpa.dao.r4.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Set;

import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES;
import static org.assertj.core.api.Assertions.assertThat;

public class ReplaceReferencesTestHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesTestHelper.class);

	static final Identifier pat1IdentifierA = new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB = new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA = new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB = new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC = new Identifier().setSystem("SYSC").setValue("VALC");
	public static final int TOTAL_EXPECTED_PATCHES = 23;
	public static final int SMALL_BATCH_SIZE = 5;
	public static final int EXPECTED_SMALL_BATCHES = (TOTAL_EXPECTED_PATCHES + SMALL_BATCH_SIZE - 1) / SMALL_BATCH_SIZE;
	private final IFhirResourceDao<Patient> myPatientDao;

	IIdType myOrgId;
	IIdType mySourcePatientId;
	IIdType mySourceCarePlanId;
	IIdType mySourceEncId1;
	IIdType mySourceEncId2;
	ArrayList<IIdType> mySourceObsIds;
	IIdType myTargetPatientId;
	IIdType myTargetEnc1;
	Patient myResultPatient;

	private final FhirContext myFhirContext;
	private final IGenericClient myFhirClient;
	private final SystemRequestDetails mySrd = new SystemRequestDetails();

	public ReplaceReferencesTestHelper(FhirContext theFhirContext, IGenericClient theFhirClient, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myFhirClient = theFhirClient;
		myPatientDao = theDaoRegistry.getResourceDao(Patient.class);
	}

	public void beforeEach() throws Exception {

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myFhirClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatientId = myFhirClient.create().resource(patient1).execute().getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatientId = myFhirClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(Encounter.EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatientId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId1 = myFhirClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatientId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId2 = myFhirClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless();

		CarePlan carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setReferenceElement(mySourcePatientId);
		mySourceCarePlanId = myFhirClient.create().resource(carePlan).execute().getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(Encounter.EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatientId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myFhirClient.create().resource(targetEnc1).execute().getId().toUnqualifiedVersionless();

		mySourceObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatientId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			IIdType obsId = myFhirClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
			mySourceObsIds.add(obsId);
		}

		myResultPatient = new Patient();
		myResultPatient.setIdElement((IdType) myTargetPatientId);
		myResultPatient.addIdentifier(pat1IdentifierA);
		Patient.PatientLinkComponent link = myResultPatient.addLink();
		link.setOther(new Reference(mySourcePatientId));
		link.setType(Patient.LinkType.REPLACES);
	}

	public void setSourceAndTarget(PatientMergeInputParameters inParams) {
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatient = new Reference().setReferenceElement(myTargetPatientId);
	}

	public void setResultPatient(PatientMergeInputParameters theInParams) {
		theInParams.resultPatient = myResultPatient;
	}

	public Patient readSourcePatient() {
		return myPatientDao.read(mySourcePatientId, mySrd);
	}

	public Object getTargetPatientId() {
		return myTargetPatientId;
	}

	public Bundle getTargetEverythingBundle() {
		return myFhirClient.operation()
			.onInstance(myTargetPatientId)
			.named("$everything")
			.withParameter(Parameters.class, "_count", new IntegerType(100))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();
	}

	public Parameters callReplaceReferences(boolean theIsAsync) {
		return callReplaceReferencesWithBatchSize(theIsAsync, null);
	}

	public Parameters callReplaceReferencesWithBatchSize(boolean theIsAsync, Integer theBatchSize) {
		IOperationUntypedWithInputAndPartialOutput<Parameters> request = myFhirClient.operation()
			.onServer()
			.named(OPERATION_REPLACE_REFERENCES)
			.withParameter(Parameters.class, ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID, new StringType(mySourcePatientId.getValue()))
			.andParameter(ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID, new StringType(myTargetPatientId.getValue()));
		if (theBatchSize != null) {
			request.andParameter(ProviderConstants.OPERATION_REPLACE_REFERENCES_BATCH_SIZE, new IntegerType(theBatchSize));
		}

		if (theIsAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request
			.returnResourceType(Parameters.class)
			.execute();
	}

	public void assertContainsAllResources(Set<IIdType> theActual, boolean theWithDelete) {
		if (theWithDelete) {
			assertThat(theActual).doesNotContain(mySourcePatientId);
		}
		assertThat(theActual).contains(mySourceEncId1);
		assertThat(theActual).contains(mySourceEncId2);
		assertThat(theActual).contains(myOrgId);
		assertThat(theActual).contains(mySourceCarePlanId);
		assertThat(theActual).containsAll(mySourceObsIds);
		assertThat(theActual).contains(myTargetPatientId);
		assertThat(theActual).contains(myTargetEnc1);
	}

	public void assertNothingChanged(Set<IIdType> theActual) {
		assertThat(theActual).doesNotContain(mySourcePatientId);
		assertThat(theActual).doesNotContain(mySourceEncId1);
		assertThat(theActual).doesNotContain(mySourceEncId2);
		assertThat(theActual).contains(myOrgId);
		assertThat(theActual).doesNotContain(mySourceCarePlanId);
		assertThat(theActual).doesNotContainAnyElementsOf(mySourceObsIds);
		assertThat(theActual).contains(myTargetPatientId);
		assertThat(theActual).contains(myTargetEnc1);
	}

	public PatientMergeInputParameters buildMultipleTargetMatchParameters(boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatientIdentifier = patBothIdentifierC;
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public PatientMergeInputParameters buildMultipleSourceMatchParameters(boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatientIdentifier = patBothIdentifierC;
		inParams.targetPatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public static class PatientMergeInputParameters {
		public Type sourcePatient;
		public Type sourcePatientIdentifier;
		public Type targetPatient;
		public Type targetPatientIdentifier;
		public Patient resultPatient;
		public Boolean preview;
		public Boolean deleteSource;

		public Parameters asParametersResource() {
			Parameters inParams = new Parameters();
			if (sourcePatient != null) {
				inParams.addParameter().setName("source-patient").setValue(sourcePatient);
			}
			if (sourcePatientIdentifier != null) {
				inParams.addParameter().setName("source-patient-identifier").setValue(sourcePatientIdentifier);
			}
			if (targetPatient != null) {
				inParams.addParameter().setName("target-patient").setValue(targetPatient);
			}
			if (targetPatientIdentifier != null) {
				inParams.addParameter().setName("target-patient-identifier").setValue(targetPatientIdentifier);
			}
			if (resultPatient != null) {
				inParams.addParameter().setName("result-patient").setResource(resultPatient);
			}
			if (preview != null) {
				inParams.addParameter().setName("preview").setValue(new BooleanType(preview));
			}
			if (deleteSource != null) {
				inParams.addParameter().setName("delete-source").setValue(new BooleanType(deleteSource));
			}
			return inParams;
		}
	}

}
