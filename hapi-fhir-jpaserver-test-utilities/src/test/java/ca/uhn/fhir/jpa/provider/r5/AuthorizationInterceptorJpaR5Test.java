package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class AuthorizationInterceptorJpaR5Test extends BaseResourceProviderR5Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
	}

	/**
	 * See #503
	 */
	@Test
	public void testDeleteIsAllowedForCompartment() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final IIdType id = myClient.create().resource(patient).execute().getId();

		Observation obsInCompartment = new Observation();
		obsInCompartment.setStatus(Enumerations.ObservationStatus.FINAL);
		obsInCompartment.getSubject().setReferenceElement(id.toUnqualifiedVersionless());
		IIdType obsInCompartmentId = myClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		Observation obsNotInCompartment = new Observation();
		obsNotInCompartment.setStatus(Enumerations.ObservationStatus.FINAL);
		IIdType obsNotInCompartmentId = myClient.create().resource(obsNotInCompartment).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().resourcesOfType(Observation.class).inCompartment("Patient", id).andThen()
					.deny().delete().allResources().withAnyId().andThen()
					.allowAll()
					.build();
			}
		});

		myClient.delete().resourceById(obsInCompartmentId.toUnqualifiedVersionless()).execute();

		try {
			myClient.delete().resourceById(obsNotInCompartmentId.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testDeleteIsAllowedForCompartmentUsingTransaction() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final IIdType id = myClient.create().resource(patient).execute().getId();

		Observation obsInCompartment = new Observation();
		obsInCompartment.setStatus(Enumerations.ObservationStatus.FINAL);
		obsInCompartment.getSubject().setReferenceElement(id.toUnqualifiedVersionless());
		IIdType obsInCompartmentId = myClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		Observation obsNotInCompartment = new Observation();
		obsNotInCompartment.setStatus(Enumerations.ObservationStatus.FINAL);
		IIdType obsNotInCompartmentId = myClient.create().resource(obsNotInCompartment).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().resourcesOfType(Observation.class).inCompartment("Patient", id).andThen()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.denyAll()
					.build();
			}
		});

		Bundle bundle;

		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obsInCompartmentId.toUnqualifiedVersionless().getValue());
		myClient.transaction().withBundle(bundle).execute();

		try {
			bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obsNotInCompartmentId.toUnqualifiedVersionless().getValue());
			myClient.transaction().withBundle(bundle).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}


}
