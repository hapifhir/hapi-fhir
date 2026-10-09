package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
@ExtendWith(MockitoExtension.class)
class IFhirResourceDaoCodeSystemTest {

	private static final String SYSTEM = "http://example.org/cs";

	@Mock
	private IFhirResourceDaoCodeSystem<CodeSystem> myDao;

	private final IValidationSupport.LookupCodeResult myResult = new IValidationSupport.LookupCodeResult();

	@BeforeEach
	void beforeEach() {
		when(myDao.lookupCode(any(), any(), any(), any(), any(), any(), any())).thenCallRealMethod();
	}

	@Test
	void lookupCode_versionWithSystem_delegatesWithVersionPackedIntoSystem() {
		when(myDao.getContext()).thenReturn(FhirContext.forR4Cached());
		UriType system = new UriType(SYSTEM);
		ArgumentCaptor<IPrimitiveType<String>> systemCaptor = ArgumentCaptor.captor();
		when(myDao.lookupCode(any(), systemCaptor.capture(), isNull(), any(), any(), any()))
				.thenReturn(myResult);

		IValidationSupport.LookupCodeResult result =
				myDao.lookupCode(new CodeType("a"), system, new StringType("1.0.0"), null, null, List.of(), null);

		assertThat(result).isSameAs(myResult);
		assertThat(systemCaptor.getValue().getValueAsString()).isEqualTo(SYSTEM + "|1.0.0");
		assertThat(system.getValueAsString()).isEqualTo(SYSTEM);
	}

	@Test
	void lookupCode_versionWithCoding_delegatesWithTheCodingAsGiven() {
		Coding coding = new Coding(SYSTEM, "a", null);
		when(myDao.lookupCode(isNull(), isNull(), eq(coding), any(), any(), any()))
				.thenReturn(myResult);

		IValidationSupport.LookupCodeResult result =
				myDao.lookupCode(null, null, new StringType("1.0.0"), coding, null, List.of(), null);

		assertThat(result).isSameAs(myResult);
		assertThat(coding.getVersion()).isNull();
	}

	@Test
	void lookupCode_noVersion_delegatesWithTheSystemAsGiven() {
		UriType system = new UriType(SYSTEM);
		when(myDao.lookupCode(any(), eq(system), isNull(), any(), any(), any()))
				.thenReturn(myResult);

		IValidationSupport.LookupCodeResult result =
				myDao.lookupCode(new CodeType("a"), system, null, null, null, List.of(), null);

		assertThat(result).isSameAs(myResult);
		verify(myDao).lookupCode(any(), eq(system), isNull(), any(), any(), any());
	}
}
