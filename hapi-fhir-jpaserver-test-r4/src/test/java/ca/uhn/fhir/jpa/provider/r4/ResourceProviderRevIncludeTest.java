package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.DetectedIssue;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderRevIncludeTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderRevIncludeTest.class);



	class SqlCapturingInterceptor {
		SqlQueryList queryList = null;
		@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
		public void trackRequest(RequestDetails theRequestDetails, SqlQueryList theQueryList) {
			if (queryList == null) {
				queryList = theQueryList;
			} else {
				queryList.addAll(theQueryList);
			}
		}

		public SqlQueryList getQueryList() {
			return queryList;
		}
	}

	@Test
	public void testRevincludeIterate() {
		// /Patient?_id=123&_revinclude:iterate=CareTeam:subject:Group&_revinclude=Group:member:Patient&_revinclude=DetectedIssue:patient

		Patient p = new Patient();
		String methodName = "testRevincludeIterate";
		p.addName().setFamily(methodName);
		IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created patient: {}", pid.getValue()); // 1

		Group g = new Group();
		g.addMember().setEntity(new Reference(pid));
		IIdType gid = myClient.create().resource(g).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created group: {}", gid.getValue()); // 2

		CareTeam ct = new CareTeam();
		ct.getSubject().setReferenceElement(gid);
		IIdType ctid = myClient.create().resource(ct).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created care team: {}", ctid.getValue()); // 3

		List<IIdType> dids = new ArrayList<>();
		for (int i = 0; i < 100; ++i){
			DetectedIssue di = new DetectedIssue();
			di.getPatient().setReferenceElement(pid);
			IIdType diid = myClient.create().resource(di).execute().getId().toUnqualifiedVersionless();
			ourLog.info("Created detected issue: {}", diid.getValue()); // 4...103
			dids.add(diid);
		}
		SqlCapturingInterceptor sqlCapturingInterceptor = new SqlCapturingInterceptor();
		myCaptureQueriesListener.clear();
		myInterceptorRegistry.registerInterceptor(sqlCapturingInterceptor);
		Bundle bundle = myClient.search()
			.forResource(Patient.class)
			.count(200)
			.where(Patient.RES_ID.exactly().codes(pid.getIdPart()))
			.revInclude(new Include("CareTeam:subject:Group").setRecurse(true))
//			.revInclude(new Include("CareTeam:subject:Group"))
			.revInclude(new Include("Group:member:Patient"))
			.revInclude(DetectedIssue.INCLUDE_PATIENT)
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Patient patient = null;
		boolean patientFound = false;
		boolean groupFound = false;
		boolean careTeamFound = false;
		boolean detectedIssueFound = false;
		for (IBaseResource foundResource : foundResources) {
			String type = foundResource.getIdElement().getResourceType();
			switch (type) {
				case "Patient":
					if (foundResource.getIdElement().getIdPart().equals(pid.getIdPart())) {
						patientFound = true;
						patient = (Patient) foundResource;
					}
					break;
				case "Group":
					if (foundResource.getIdElement().getIdPart().equals(gid.getIdPart())) {
						groupFound = true;
					}
					break;
				case "CareTeam":
					if (foundResource.getIdElement().getIdPart().equals(ctid.getIdPart())) {
						careTeamFound = true;
					}
					break;
				case "DetectedIssue":
					if (dids.contains(foundResource.getIdElement())) {
						detectedIssueFound = true;
					}
					break;
				default:
					ourLog.warn(type + " found but not expected");
			}

			if (patientFound && groupFound && careTeamFound && detectedIssueFound) {
				break;
			}
		}

		assertTrue(patientFound);
		assertTrue(groupFound);
		assertTrue(careTeamFound);
		assertNotNull(patient);
		assertEquals(pid.getIdPart(), patient.getIdElement().getIdPart());
		assertEquals(methodName, patient.getName().get(0).getFamily());

		//Ensure that the revincludes are included in the query list of the sql trace.
		//TODO GGG/KHS reduce this to something less than 6 by smarter iterating and getting the resource types earlier when needed.
		assertThat(sqlCapturingInterceptor.getQueryList()).hasSize(5);
		myInterceptorRegistry.unregisterInterceptor(sqlCapturingInterceptor);
	}

	@Test
	public void includeRevInclude() {
		Practitioner practitioner = new Practitioner();
		practitioner.addName().setFamily("testIncludeRevInclude");
		IIdType practitionerId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless();

		DetectedIssue detectedIssue = new DetectedIssue();
		detectedIssue.getAuthor().setReferenceElement(practitionerId);
		IIdType detectedIssueId = myClient.create().resource(detectedIssue).execute().getId().toUnqualifiedVersionless();

		PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.getPractitioner().setReferenceElement(practitionerId);
		IIdType practitionerRoleId = myClient.create().resource(practitionerRole).execute().getId().toUnqualifiedVersionless();

		// DetectedIssue?_id=123&_include=DetectedIssue:author&_revinclude=PractitionerRole:practitioner
		Bundle bundle = myClient.search()
			.forResource(DetectedIssue.class)
			.where(DetectedIssue.RES_ID.exactly().codes(detectedIssueId.getIdPart()))
			.include(new Include("DetectedIssue:author"))
			.revInclude(new Include("PractitionerRole:practitioner").setRecurse(true))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		assertThat(foundResources).hasSize(3);
		assertEquals(detectedIssueId.getIdPart(), foundResources.get(0).getIdElement().getIdPart());
		assertEquals(practitionerId.getIdPart(), foundResources.get(1).getIdElement().getIdPart());
		assertEquals(practitionerRoleId.getIdPart(), foundResources.get(2).getIdElement().getIdPart());
	}

	@Test
	public void includeRevIncludeIterate() {
		Patient p = new Patient();
		String methodName = "includeRevIncludeIterate";
		p.addName().setFamily(methodName);
		IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		EpisodeOfCare episodeOfCare = new EpisodeOfCare();
		episodeOfCare.addIdentifier(new Identifier().setSystem("system1").setValue("value1"));
		IIdType episodeOfCareId = myClient.create().resource(episodeOfCare).execute().getId().toUnqualifiedVersionless();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference(pid));
		encounter.addEpisodeOfCare(new Reference(episodeOfCareId));
		IIdType encounterId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless();

		Task task = new Task();
		task.setEncounter(new Reference(encounterId));
		IIdType taskId = myClient.create().resource(task).execute().getId().toUnqualifiedVersionless();

		// EpisodeOfCare?identifier=system1|value1&_revinclude=Encounter:episode-of-care&_include:iterate=Encounter:patient&_revinclude:iterate=Task:encounter
		Bundle bundle = myClient.search()
			.forResource(EpisodeOfCare.class)
			.where(EpisodeOfCare.IDENTIFIER.exactly().systemAndIdentifier("system1", "value1"))
			.revInclude(new Include("Encounter:episode-of-care"))
			.include(new Include("Encounter:patient").setRecurse(true))
			.revInclude(new Include("Task:encounter").setRecurse(true))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		assertThat(foundResources).hasSize(4);
		assertEquals(episodeOfCareId.getIdPart(), foundResources.get(0).getIdElement().getIdPart());
		assertEquals(encounterId.getIdPart(), foundResources.get(1).getIdElement().getIdPart());
		assertEquals(taskId.getIdPart(), foundResources.get(2).getIdElement().getIdPart());
		assertEquals(pid.getIdPart(), foundResources.get(3).getIdElement().getIdPart());
	}

}
