package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CachingValidationSupportTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Mock
	private IValidationSupport myValidationSupport;

	@Test
	public void testAsyncBackgroundLoading() {
		StructureDefinition sd0 = (StructureDefinition) new StructureDefinition().setId("SD0");
		StructureDefinition sd1 = (StructureDefinition) new StructureDefinition().setId("SD1");
		StructureDefinition sd2 = (StructureDefinition) new StructureDefinition().setId("SD2");
		List<StructureDefinition> responses = Collections.synchronizedList(Lists.newArrayList(
			sd0, sd1, sd2
		));

		when(myValidationSupport.getFhirContext()).thenReturn(ourCtx);
		when(myValidationSupport.fetchAllNonBaseStructureDefinitions()).thenAnswer(t -> {
			Thread.sleep(2000);
			return Collections.singletonList(responses.remove(0));
		});

		CachingValidationSupport.CacheTimeouts cacheTimeouts = CachingValidationSupport.CacheTimeouts
			.defaultValues()
			.setMiscMillis(1000);
		CachingValidationSupport support = new CachingValidationSupport(myValidationSupport, cacheTimeouts);

		assertEquals(3, responses.size());
		List<IBaseResource> fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertSame(sd0, fetched.get(0));
		assertEquals(2, responses.size());

		sleepAtLeast(1200);
		fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertSame(sd0, fetched.get(0));
		assertEquals(2, responses.size());

		await().until(() -> responses.size(), equalTo(1));
		assertEquals(1, responses.size());
		fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertSame(sd1, fetched.get(0));
		assertEquals(1, responses.size());
	}


}
