package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.FhirHttpRequest;
import ca.uhn.fhir.test.utilities.FhirHttpResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies that a write-only caller cannot use a write operation's
 * response body to read content it has no read authorization for.
 */
class AuthorizationInterceptorWriteResponseJpaR4Test extends BaseResourceProviderR4Test {
	private static final String OBSERVATION_SECRET_CODE = "SECRET-DIAGNOSIS";
	private static final String OBSERVATION_NEW_CODE = "NEW-DIAGNOSIS";
	private static final String BUNDLE_OBSERVATION_SECRET_CODE = "SECRET-DIAGNOSIS-OTHER";
	private static final String MRN_IDENTIFIER = "SECRET-MRN-001";

	private IIdType myObservationId;
	private IIdType myPatientId;

	@BeforeEach
	void beforeEach() {
		Patient patient = new Patient();
		patient.setActive(true);
		myPatientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.setSubject(new Reference(myPatientId));
		observation.getCode().addCoding().setCode(OBSERVATION_SECRET_CODE);
		observation.addIdentifier().setValue(MRN_IDENTIFIER);
		myObservationId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
	}

	private FhirHttpRequest request(String thePath) {
		return FhirHttpRequest.to(ourHttpClient.getClient(), myFhirContext, myServerBase + thePath);
	}

	private FhirHttpResponse patchObservation(String thePreferReturn) {
		return patchObservation(request("/Observation/" + myObservationId.getIdPart()).withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + thePreferReturn));
	}

	private FhirHttpResponse patchObservation() {
		return patchObservation(request("/Observation/" + myObservationId.getIdPart()));
	}

	private FhirHttpResponse patchObservation(FhirHttpRequest theRequest) {
		return theRequest.patch("""
				[ { "op": "replace", "path": "/status", "value": "amended" } ]
			""");
	}

	@Nested
	class PatchReadAuthorization {

		/**
		 * The caller can patch and write any resource but holds no read rule at all.
		 */
		@Nested
		class WriteOnlyPermissions {

			@BeforeEach
			void beforeEach() {
				setupWriteOnlyPermissions();
			}

			@Test
			void patch_writeOnly_returnsForbidden() {
				String body = patchObservation().assertStatus(403).getBody();

				assertThat(body)
					.as("the stored resource body must not be disclosed to a caller without read scope")
					.doesNotContain("SECRET-DIAGNOSIS")
					.doesNotContain(myPatientId.getIdPart());
			}

			@Test
			void patch_writeOnlyWithPreferReturnMinimal_returnsSuccessAndNoBody() {
				String body = patchObservation(Constants.HEADER_PREFER_RETURN_MINIMAL).assertStatus(200).getBody();

				assertThat(body)
					.as("the returned body should be empty")
					.isEmpty();
			}

			@Test
			void patch_writeOnlyOnStoredBundleResource_returnsForbidden() {
				IIdType bundleId = setupBundle();

				String body  = request("/Bundle/" + bundleId.getIdPart()).patch("""
						[ { "op": "replace", "path": "/entry/0/resource/status", "value": "amended" } ]
					""").assertStatus(403).getBody();

				assertThat(body)
					.as("the stored resource body must not be disclosed to a caller without read scope")
					.doesNotContain(BUNDLE_OBSERVATION_SECRET_CODE);
			}
		}

		@Nested
		class ReadAndWritePermissions {

			@BeforeEach
			void beforeEach() {
				setupReadAndWritePermissions();
			}

			@Test
			void patch_readAndWrite_returnsMergedResource() {
				String body = patchObservation().assertStatus(200).getBody();

				assertThat(body)
					.as("the returned body should be the full Observation resource")
					.startsWith("<Observation")
					.contains("SECRET-DIAGNOSIS")
					.contains(myPatientId.getIdPart());
			}

			@Test
			void patch_readAndWriteOnStoredBundleResource_returnsSuccess() {
				IIdType bundleId = setupBundle();

				String body  = request("/Bundle/" + bundleId.getIdPart()).patch("""
						[ { "op": "replace", "path": "/entry/0/resource/status", "value": "amended" } ]
					""").assertStatus(200).getBody();

				assertThat(body)
					.as("The returned body should be the full Bundle resource")
					.contains(BUNDLE_OBSERVATION_SECRET_CODE);
			}
		}

		private IIdType setupBundle() {
			Bundle storedBundle = new Bundle();
			storedBundle.setType(Bundle.BundleType.COLLECTION);
			Observation embdeddedObservation = new Observation();
			embdeddedObservation.setStatus(Observation.ObservationStatus.FINAL);
			embdeddedObservation.setSubject(new Reference(myPatientId));
			embdeddedObservation.getCode().addCoding().setCode(BUNDLE_OBSERVATION_SECRET_CODE);
			storedBundle.addEntry().setResource(embdeddedObservation);
			return myClient.create().resource(storedBundle).execute().getId();
		}
	}

	@Nested class ConditionalCreateBody {

		@BeforeEach
		void beforeEach() {
			setupWriteOnlyPermissions();
		}

		@Test
		void create_writeOnlyConditionalUrlMatchingExistingResource_returnsNoResourceBody() {
			FhirHttpResponse response = request("/Observation")
				.withHeader(Constants.HEADER_IF_NONE_EXIST, "Observation?identifier=" + MRN_IDENTIFIER)
				.post(createObservation())
				.assertStatus(200);

			assertThat(response.getBody())
				.as("a conditional create that matched an existing resource must not disclose that resource's content")
				.isEmpty();
			assertThat(response.getHeader(Constants.HEADER_LOCATION))
				.as("the id of the matched resource is still communicated via Location")
				.contains(myObservationId.getIdPart());
		}

		@Test
		void create_writeOnlyConditionalUrlMatchingExistingResourceWithPreferRepresentation_returnsNoResourceBody() {
			String body = request("/Observation")
				.withHeader(Constants.HEADER_IF_NONE_EXIST, "Observation?identifier=" + MRN_IDENTIFIER)
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
				.post(createObservation()).assertStatus(200).getBody();

			assertThat(body)
				.as("suppression must hold even when the caller explicitly asks for the representation")
				.isEmpty();
		}

		@Test
		void create_writeOnlyConditionalUrlMatchingNothing_createsAndReturnsSubmittedBody() {
			Observation submitted = createObservation();
			submitted.getIdentifierFirstRep().setValue("NO-MATCH-MRN");

			String body = request("/Observation")
				.withHeader(Constants.HEADER_IF_NONE_EXIST, "Observation?identifier=NO-MATCH-MRN")
				.post(submitted).assertStatus(201).getBody();

			assertThat(body)
				.as("a conditional create with no match must still return the submitted resource")
				.contains("\"resourceType\":\"Observation\"")
				.doesNotContain(OBSERVATION_SECRET_CODE)
				.contains(OBSERVATION_NEW_CODE);
		}

		@Test
		void create_writeOnlyPlainCreate_returnsSubmittedBody() {
			String body = request("/Observation")
				.post(createObservation()).assertStatus(201).getBody();

			assertThat(body)
				.as("a plain create must still return the submitted body")
				.contains("\"resourceType\":\"Observation\"")
				.contains(OBSERVATION_NEW_CODE);
		}

		@Test
		void update_writeOnlyPlainPut_returnsSubmittedBody() {
			Observation updated = createObservation();
			updated.setId(myObservationId);
			updated.setStatus(Observation.ObservationStatus.AMENDED);

			String body = request("/Observation/" + myObservationId.getIdPart())
				.put(updated).assertStatus(200).getBody();

			assertThat(body)
				.as("update should still return the submitted body")
				.contains("\"resourceType\":\"Observation\"")
				.contains(OBSERVATION_NEW_CODE);
		}

		@Test
		void update_writeOnlyConditionalPutMatchingExistingResource_returnsSubmittedBody() {
			Observation updated = createObservation();
			updated.setStatus(Observation.ObservationStatus.AMENDED);

			String body = request("/Observation?identifier=" + MRN_IDENTIFIER)
				.put(updated).assertStatus(200).getBody();

			assertThat(body)
				.as("update-as-create should return the submitted body")
				.contains("\"resourceType\":\"Observation\"")
				.contains(OBSERVATION_NEW_CODE);
		}
	}

	@Nested class OperationOutcomeReadAuthorization {

		/**
		 * The caller can patch and write any resource but holds no read rule at all.
		 */
		@Nested
		class WriteOnlyPermissions {

			@BeforeEach
			void beforeEach() {
				setupWriteOnlyPermissions();
			}

			@Test
			void patch_writeOnlyWithPreferReturnOperationOutcome_returnsSuccess() {
				String body = patchObservation(Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME).assertStatus(200).getBody();

				assertThat(body)
					.as("the returned body should be the full Observation resource")
					.startsWith("<OperationOutcome")
					.doesNotContain("SECRET-DIAGNOSIS");
			}

			@Test
			void read_writeOnlyStoredOperationOutcome_returnsForbidden() {
				OperationOutcome outcome = new OperationOutcome();
				IIdType ooId = myClient.create().resource(outcome).execute().getId().toUnqualifiedVersionless();

				assertThatThrownBy(() -> myClient.read().resource("OperationOutcome").withId(ooId).execute())
					.as("should not be able to read OperationOutcome without read permissions")
					.isInstanceOf(ForbiddenOperationException.class)
					.hasMessageContaining("HTTP 403 Forbidden");
			}
		}

		/**
		 * Positive control: with a read rule granted, a directly-addressed stored {@code OperationOutcome}
		 * must still be readable - proves the exemption in {@link WriteOnlyPermissions} is narrow rather
		 * than a blanket bypass of read authorization for the resource type.
		 */
		@Nested
		class ReadAndWritePermissions {

			@BeforeEach
			void beforeEach() {
				setupReadAndWritePermissions();
			}

			@Test
			void read_readAndWriteStoredOperationOutcome_returnsSuccess() {
				OperationOutcome outcome = new OperationOutcome();
				IIdType ooId = myClient.create().resource(outcome).execute().getId().toUnqualifiedVersionless();

				IBaseResource result = myClient.read().resource("OperationOutcome").withId(ooId).execute();

				assertThat(result.getIdElement().toUnqualifiedVersionless().getValue())
					.as("a caller with read permission must still be able to read a stored OperationOutcome directly")
					.isEqualTo(ooId.getValue());
			}
		}
	}

	private void setupReadAndWritePermissions() {
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(
			new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return new RuleBuilder()
						.allow().patch().allRequests().andThen()
						.allow().write().allResources().withAnyId().andThen()
						.allow().read().allResources().withAnyId().andThen()
						.build();
				}
			});
	}

	private void setupWriteOnlyPermissions() {
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(
			new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return new RuleBuilder()
						.allow().patch().allRequests().andThen()
						.allow().write().allResources().withAnyId().andThen()
						.build();
				}
			});
	}

	private Observation createObservation() {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.addIdentifier().setValue(MRN_IDENTIFIER);
		observation.getCode().addCoding().setCode(OBSERVATION_NEW_CODE);
		return observation;
	}
}
