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
import org.hl7.fhir.instance.model.api.IAnyResource;
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
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderRevIncludeTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderRevIncludeTest.class);



	static class SqlCapturingInterceptor {
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
			.where(IAnyResource.RES_ID.exactly().codes(pid.getIdPart()))
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
					ourLog.warn("{} found but not expected", type);
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
		//TODO GGG/KHS reduce this to something less than 5 by smarter iterating and getting the resource types earlier when needed.
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
			.where(IAnyResource.RES_ID.exactly().codes(detectedIssueId.getIdPart()))
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

	/**
	 * Reproduces SMILE-9135: When _include and _revinclude are used together WITHOUT _iterate,
	 * _include should only apply to the initial search result set, not to revincluded resources.
	 * <p>
	 * Setup:
	 * - SR/A: no replaces (initial search target)
	 * - SR/B: no replaces (should NOT appear without _iterate)
	 * - SR/C: replaces = [SR/A, SR/B] (found via _revinclude of SR/A)
	 * <p>
	 * Query: ServiceRequest?_id=A&_include=ServiceRequest:replaces&_revinclude=ServiceRequest:replaces
	 * <p>
	 * Expected: SR/A + SR/C only (2 resources)
	 * Bug: SR/B is also returned because _include is applied to revincluded SR/C
	 */
	@Test
	void testRevIncludeDoesNotIncludeFromIncludedResources() {
		// SR/A - initial search target, no replaces
		ServiceRequest srA = new ServiceRequest();
		srA.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srA.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srAId = myClient.create().resource(srA).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created ServiceRequest A: {}", srAId.getValue());

		// SR/B - should NOT appear in results (no replaces, not referenced by SR/A)
		ServiceRequest srB = new ServiceRequest();
		srB.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srB.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srBId = myClient.create().resource(srB).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created ServiceRequest B: {}", srBId.getValue());

		// SR/C - replaces both SR/A and SR/B; should appear via _revinclude (references SR/A)
		ServiceRequest srC = new ServiceRequest();
		srC.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srC.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		srC.addReplaces(new Reference(srAId));
		srC.addReplaces(new Reference(srBId));
		IIdType srCId = myClient.create().resource(srC).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created ServiceRequest C: {}", srCId.getValue());

		// ServiceRequest?_id=A&_include=ServiceRequest:replaces&_revinclude=ServiceRequest:replaces
		// Without _iterate, _include should only apply to the initial result set (SR/A),
		// NOT to the revincluded SR/C
		Bundle bundle = myClient.search()
			.forResource(ServiceRequest.class)
			.where(IAnyResource.RES_ID.exactly().codes(srAId.getIdPart()))
			.include(new Include("ServiceRequest:replaces"))
			.revInclude(new Include("ServiceRequest:replaces"))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Set<String> foundIds = foundResources.stream()
			.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toSet());

		ourLog.info("Found {} resources: {}", foundResources.size(), foundIds);

		// SR/A should be present (initial search result)
		assertThat(foundIds).contains(srAId.getValue());
		// SR/C should be present (found via _revinclude: SR/C.replaces references SR/A)
		assertThat(foundIds).contains(srCId.getValue());
		// SR/B should NOT be present (it would only appear if _include was applied to SR/C,
		// which should not happen without _iterate)
		assertThat(foundIds).doesNotContain(srBId.getValue());
		// Exactly 2 resources: SR/A + SR/C
		assertThat(foundResources).hasSize(2);
	}

	/**
	 * Adjacent test for SMILE-9135: _revinclude alone should work correctly.
	 * When only _revinclude is used (no _include), there is no opportunity for
	 * the transitive bug to manifest.
	 */
	@Test
	void testRevIncludeAloneDoesNotProduceTransitiveResults() {
		ServiceRequest srA = new ServiceRequest();
		srA.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srA.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srAId = myClient.create().resource(srA).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srB = new ServiceRequest();
		srB.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srB.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srBId = myClient.create().resource(srB).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srC = new ServiceRequest();
		srC.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srC.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		srC.addReplaces(new Reference(srAId));
		srC.addReplaces(new Reference(srBId));
		IIdType srCId = myClient.create().resource(srC).execute().getId().toUnqualifiedVersionless();

		// ServiceRequest?_id=A&_revinclude=ServiceRequest:replaces (no _include)
		Bundle bundle = myClient.search()
			.forResource(ServiceRequest.class)
			.where(IAnyResource.RES_ID.exactly().codes(srAId.getIdPart()))
			.revInclude(new Include("ServiceRequest:replaces"))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Set<String> foundIds = foundResources.stream()
			.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toSet());

		ourLog.info("RevInclude alone - Found {} resources: {}", foundResources.size(), foundIds);

		assertThat(foundIds).contains(srAId.getValue());
		assertThat(foundIds).contains(srCId.getValue());
		assertThat(foundIds).doesNotContain(srBId.getValue());
		assertThat(foundResources).hasSize(2);
	}

	/**
	 * Adjacent test for SMILE-9135: _include alone should work correctly.
	 * Since SR/A has no replaces, _include should not add anything.
	 */
	@Test
	void testIncludeAloneDoesNotProduceTransitiveResults() {
		ServiceRequest srA = new ServiceRequest();
		srA.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srA.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srAId = myClient.create().resource(srA).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srB = new ServiceRequest();
		srB.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srB.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srBId = myClient.create().resource(srB).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srC = new ServiceRequest();
		srC.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srC.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		srC.addReplaces(new Reference(srAId));
		srC.addReplaces(new Reference(srBId));
		myClient.create().resource(srC).execute().getId().toUnqualifiedVersionless();

		// ServiceRequest?_id=A&_include=ServiceRequest:replaces (no _revinclude)
		Bundle bundle = myClient.search()
			.forResource(ServiceRequest.class)
			.where(IAnyResource.RES_ID.exactly().codes(srAId.getIdPart()))
			.include(new Include("ServiceRequest:replaces"))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Set<String> foundIds = foundResources.stream()
			.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toSet());

		ourLog.info("Include alone - Found {} resources: {}", foundResources.size(), foundIds);

		// SR/A has no replaces, so _include adds nothing
		assertThat(foundIds).contains(srAId.getValue());
		assertThat(foundIds).doesNotContain(srBId.getValue());
		assertThat(foundResources).hasSize(1);
	}

	/**
	 * Adjacent test for SMILE-9135: With _iterate, _include SHOULD apply to revincluded resources.
	 * This is the correct behavior when iterate is specified - SR/B should appear.
	 */
	@Test
	void testRevIncludeWithIncludeIterateProducesTransitiveResults() {
		ServiceRequest srA = new ServiceRequest();
		srA.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srA.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srAId = myClient.create().resource(srA).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srB = new ServiceRequest();
		srB.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srB.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		IIdType srBId = myClient.create().resource(srB).execute().getId().toUnqualifiedVersionless();

		ServiceRequest srC = new ServiceRequest();
		srC.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		srC.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		srC.addReplaces(new Reference(srAId));
		srC.addReplaces(new Reference(srBId));
		IIdType srCId = myClient.create().resource(srC).execute().getId().toUnqualifiedVersionless();

		// ServiceRequest?_id=A&_include:iterate=ServiceRequest:replaces&_revinclude:iterate=ServiceRequest:replaces
		// With _iterate, the transitive cascade IS correct
		Bundle bundle = myClient.search()
			.forResource(ServiceRequest.class)
			.where(IAnyResource.RES_ID.exactly().codes(srAId.getIdPart()))
			.include(new Include("ServiceRequest:replaces").setRecurse(true))
			.revInclude(new Include("ServiceRequest:replaces").setRecurse(true))
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Set<String> foundIds = foundResources.stream()
			.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toSet());

		ourLog.info("Iterate - Found {} resources: {}", foundResources.size(), foundIds);

		// With _iterate, SR/B SHOULD be included (SR/C.replaces -> SR/B is followed iteratively)
		assertThat(foundIds).contains(srAId.getValue());
		assertThat(foundIds).contains(srBId.getValue());
		assertThat(foundIds).contains(srCId.getValue());
		assertThat(foundResources).hasSize(3);
	}

}
