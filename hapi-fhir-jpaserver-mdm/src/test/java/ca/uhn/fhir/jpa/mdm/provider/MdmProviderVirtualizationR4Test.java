package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.mdm.interceptor.MdmReadVirtualizationInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MdmProviderVirtualizationR4Test extends BaseMdmProviderR4Test {

	@Autowired
	private MdmReadVirtualizationInterceptor myMdmReadVirtualizationInterceptor;

	@Test
	public void testDuplicateResourcesDontCountTowardsTotal() {
		//Given: we enable virtualization
		myInterceptorRegistry.registerInterceptor(myMdmReadVirtualizationInterceptor);

		//Given: we link 2 patients and create some obs for each
		Patient p = createPatientAndUpdateLinks(buildPaulPatient());
		Patient p2 = createPatientAndUpdateLinks(buildPaulPatient());
		Patient goldenPat = getGoldenResourceFromTargetResource(p);
		IIdType patId = p.getIdElement().toUnqualifiedVersionless();
		IIdType pat2Id = p2.getIdElement().toUnqualifiedVersionless();
		IIdType goldenPatId = goldenPat.getIdElement().toUnqualifiedVersionless();
		IIdType obsId1 = createObservation(withSubject(patId), withObservationCode("http://foo", "code0")).toUnqualifiedVersionless();
		IIdType obsId2 = createObservation(withSubject(patId), withObservationCode("http://foo", "code0")).toUnqualifiedVersionless();
		IIdType obsId3 = createObservation(withSubject(pat2Id), withObservationCode("http://foo", "code0")).toUnqualifiedVersionless();

		//When: we query for $everything
		Bundle everything = myClient.operation().onInstanceVersion(p.getIdElement()).named("$everything").withNoParameters(Parameters.class).returnResourceType(Bundle.class).execute();
		List<IBaseResource> collect = everything.getEntry().stream().map(Bundle.BundleEntryComponent::getResource).collect(Collectors.toList());

		//Then: the total should reflect the deduplicated count, and not the original count.
		assertThat(everything.getTotal()).isEqualTo(4);
		assertThat(collect).extracting(e -> e.getIdElement().toUnqualifiedVersionless())
			.containsExactly(patId, obsId1, obsId2, obsId3)
			.doesNotContain(pat2Id)
			.doesNotContain(goldenPatId);

		myInterceptorRegistry.unregisterInterceptor(myMdmReadVirtualizationInterceptor);
	}
}
