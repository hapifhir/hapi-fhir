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

	/**
	 * Configures the mocked {@link ISearchBuilder} so that {@code loadResourcesByPid} populates the
	 * caller-supplied list (3rd argument) with exactly {@code theResourcesToLoad}, regardless of how
	 * many PIDs were requested. This mirrors {@code SearchBuilder.doLoadPids}, which only pads the
	 * populated list up to the highest successfully-loaded index and skips deleted/unloadable PIDs.
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * Primary reproduction (TDD RED): the exact customer "length 0" case. A single PID is supplied
	 * but the search builder loads ZERO resources (all PIDs unloadable/deleted). The consent
	 * interceptor still iterates {@code 0 .. size()-1} and calls {@code getResource(0)}. The desired
	 * behavior is that {@code getResource(0)} returns {@code null} (resource gracefully absent) rather
	 * than overrunning the populated list with an IndexOutOfBoundsException.
	 */
	@Test
	void getResource_whenNoResourcesLoadedForOnePid_returnsNullWithoutIndexOverrun() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L));
		stubLoadResourcesByPid(List.of());

		JpaPreResourceAccessDetails details =
				new JpaPreResourceAccessDetails(resourcePids, mySearchBuilderSupplier);

		assertThat(details.size()).isEqualTo(1);
		assertThat(details.getResource(0)).isNull();
	}

	/**
	 * Adjacent (predicted FAIL -> desired PASS): trailing PID fails to load. Two PIDs are supplied but
	 * only index 0 is populated; the populated list is shorter than {@code size()}. Asking for the
	 * trailing index must not overrun.
	 */
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

	/**
	 * Adjacent (predicted PASS, stays PASS): every PID loads positionally. {@code getResource(i)}
	 * returns each loaded resource.
	 */
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

	/**
	 * Adjacent (predicted PASS but returns null): a middle PID fails to load. The populated list is
	 * null-padded at the missing index (length == size()), so {@code getResource(1)} returns
	 * {@code null} rather than throwing — no index overrun, but a null resource is exposed.
	 */
	@Test
	void getResource_whenMiddlePidNotLoaded_returnsNullForThatIndexWithoutThrowing() {
		List<JpaPid> resourcePids = List.of(JpaPid.fromId(1L), JpaPid.fromId(2L), JpaPid.fromId(3L));
		IBaseResource r0 = newObservation(1L);
		IBaseResource r2 = newObservation(3L);
		// Positional null-padding: index 1 left null, index 2 populated.
		List<IBaseResource> populated = new java.util.ArrayList<>();
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
