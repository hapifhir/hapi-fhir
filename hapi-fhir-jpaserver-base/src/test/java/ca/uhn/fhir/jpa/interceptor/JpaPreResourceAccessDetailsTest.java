package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.util.ICallable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;

// Created by claude-opus-4-8
@ExtendWith(MockitoExtension.class)
class JpaPreResourceAccessDetailsTest {

	@Mock
	private ISearchBuilder<JpaPid> mySearchBuilder;

	private ICallable<ISearchBuilder> mySearchBuilderSupplier;

	@BeforeEach
	void before() {
		mySearchBuilderSupplier = () -> mySearchBuilder;
	}

	private void stubLoadResourcesByPid(List<IBaseResource> theResourcesToLoad) {
		doAnswer(theInvocation -> {
					List<IBaseResource> listToPopulate = theInvocation.getArgument(2);
					listToPopulate.clear();
					listToPopulate.addAll(theResourcesToLoad);
					return null;
				})
				.when(mySearchBuilder)
				.loadResourcesByPid(any(), any(), any(), anyBoolean(), any());
	}

	private static Observation newObservation(long theId) {
		Observation observation = new Observation();
		observation.setId("Observation/" + theId);
		return observation;
	}

	@Test
	void getResource_whenNoResourcesLoadedForOnePid_returnsNullWithoutIndexOverrun() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L));
		stubLoadResourcesByPid(List.of());

		JpaPreResourceAccessDetails details =
				new JpaPreResourceAccessDetails(resourcePids, mySearchBuilderSupplier);

		assertThat(details.size()).isEqualTo(1);
		assertThat(details.getResource(0)).isNull();
	}

	@Test
	void getResource_whenTrailingPidNotLoaded_returnsNullWithoutIndexOverrun() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L), JpaPid.fromId(2L));
		IBaseResource loadedFirst = newObservation(1L);
		stubLoadResourcesByPid(List.of(loadedFirst));

		JpaPreResourceAccessDetails details =
				new JpaPreResourceAccessDetails(resourcePids, mySearchBuilderSupplier);

		assertThat(details.size()).isEqualTo(2);
		assertThat(details.getResource(0)).isSameAs(loadedFirst);
		assertThat(details.getResource(1)).isNull();
	}

	@Test
	void getResource_whenAllPidsLoad_returnsEachResourcePositionally() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L), JpaPid.fromId(2L), JpaPid.fromId(3L));
		IBaseResource r0 = newObservation(1L);
		IBaseResource r1 = newObservation(2L);
		IBaseResource r2 = newObservation(3L);
		stubLoadResourcesByPid(List.of(r0, r1, r2));

		JpaPreResourceAccessDetails details =
				new JpaPreResourceAccessDetails(resourcePids, mySearchBuilderSupplier);

		assertThat(details.size()).isEqualTo(3);
		assertThat(details.getResource(0)).isSameAs(r0);
		assertThat(details.getResource(1)).isSameAs(r1);
		assertThat(details.getResource(2)).isSameAs(r2);
	}

	@Test
	void getResource_whenMiddlePidNotLoaded_returnsNullForThatIndexWithoutThrowing() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L), JpaPid.fromId(2L), JpaPid.fromId(3L));
		IBaseResource r0 = newObservation(1L);
		IBaseResource r2 = newObservation(3L);
		List<IBaseResource> populated = new ArrayList<>();
		populated.add(r0);
		populated.add(null);
		populated.add(r2);
		stubLoadResourcesByPid(populated);

		JpaPreResourceAccessDetails details =
				new JpaPreResourceAccessDetails(resourcePids, mySearchBuilderSupplier);

		assertThat(details.size()).isEqualTo(3);
		assertThat(details.getResource(0)).isSameAs(r0);
		assertThat(details.getResource(1)).isNull();
		assertThat(details.getResource(2)).isSameAs(r2);
	}
}
