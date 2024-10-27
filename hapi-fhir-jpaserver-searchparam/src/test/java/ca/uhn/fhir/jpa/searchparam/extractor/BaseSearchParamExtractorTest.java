package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseSearchParamExtractorTest {

	@Test
	void testSplitPathsR4() {
		List<String> tokens = Arrays.asList(BaseSearchParamExtractor.splitPathsR4("  aaa | bbb + '|' |   ccc  ddd  "));
		assertThat(tokens).containsExactly("aaa", "bbb + '|'", "ccc  ddd");
	}

	@Nested
	class ExtractResourceLevelParams {

		@Spy
		BaseSearchParamExtractor myExtractor;

		@Mock
		IBaseResource myResource;

		@Mock
		RestSearchParameterTypeEnum mySearchParamType;

		@Mock
		ISearchParamExtractor.ISearchParamFilter mySearchParamFilter;

		@Mock
		RuntimeSearchParam myRuntimeSearchParam;

		@Mock
		StorageSettings myStorageSettings;

		@BeforeEach
		void setUp() {
			myExtractor.setStorageSettings(myStorageSettings);
			doReturn(List.of(myRuntimeSearchParam)).when(myExtractor).getSearchParams(myResource);
			when(mySearchParamFilter.filterSearchParams(any())).thenReturn(List.of(myRuntimeSearchParam));
			when(myRuntimeSearchParam.getParamType()).thenReturn(mySearchParamType);
		}

		@Test
		void testWhenSet_resourceLevelParamsAreExtracted() {
			myExtractor.setExtractResourceLevelParams(true);
			doNothing().when(myExtractor).extractSearchParam(any(), any(), any(), any(), eq(false));

			// execute
			myExtractor.extractSearchParams(myResource, null, mySearchParamType, false, mySearchParamFilter);

			verify(myExtractor).extractSearchParam(any(), any(), any(), any(), eq(false));
		}

		@Test
		void testWhenNotSet_resourceLevelParamsAreNotExtracted() {
			when(myRuntimeSearchParam.getPath()).thenReturn("Resource.something");

			// execute
			myExtractor.extractSearchParams(myResource, null, mySearchParamType, false, mySearchParamFilter);

			verify(myExtractor, never()).extractSearchParam(any(), any(), any(), any(), eq(false));
		}

	}
}
