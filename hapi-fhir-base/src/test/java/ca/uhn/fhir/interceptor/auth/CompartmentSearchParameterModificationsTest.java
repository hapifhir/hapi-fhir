package ca.uhn.fhir.interceptor.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-5
class CompartmentSearchParameterModificationsTest {

	private static final int THREAD_COUNT = 16;
	private static final int ITERATIONS_PER_THREAD = 5_000;
	private static final int RESOURCE_TYPE_COUNT = 500;
	private static final Set<String> SEEDED_OMITTED_SPS = Set.of("subject", "source");
	private static final Set<String> SEEDED_ADDITIONAL_SPS = Set.of("performer", "patient");

	private final CompartmentSearchParameterModifications myModifications =
			new CompartmentSearchParameterModifications();

	@Test
	void getOmittedSPNamesForResourceType_whenNothingRegistered_returnsEmptyImmutableSet() {
		Set<String> actual = myModifications.getOmittedSPNamesForResourceType("Group");

		assertThat(actual).isEmpty();
		assertThatThrownBy(() -> actual.add("member")).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void getAdditionalSearchParamNamesForResourceType_whenNothingRegistered_returnsEmptyImmutableSet() {
		Set<String> actual = myModifications.getAdditionalSearchParamNamesForResourceType("Device");

		assertThat(actual).isEmpty();
		assertThatThrownBy(() -> actual.add("patient")).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void getOmittedSPNamesForResourceType_whenRegistered_returnsImmutableSetWithValues() {
		myModifications.addSPToOmitFromCompartment("Group", "member");

		Set<String> actual = myModifications.getOmittedSPNamesForResourceType("Group");

		assertThat(actual).containsExactly("member");
		assertThatThrownBy(() -> actual.add("managingEntity")).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void getAdditionalSearchParamNamesForResourceType_whenRegistered_returnsImmutableSetWithValues() {
		myModifications.addSPToIncludeInCompartment("Device", "patient");
		myModifications.addSPToIncludeInCompartment("Device", "subject");

		Set<String> actual = myModifications.getAdditionalSearchParamNamesForResourceType("Device");

		assertThat(actual).containsExactlyInAnyOrder("patient", "subject");
		assertThatThrownBy(() -> actual.add("owner")).isInstanceOf(UnsupportedOperationException.class);
	}

	/**
	 * The root cause of the reported ConcurrentModificationException(gitlab-8555): the getters used to call
	 * computeIfAbsent, so a logical read structurally modified the backing map. This asserts the
	 * getters are side effect free, deterministically and without relying on thread interleaving.
	 */
	@Test
	void getSPNames_doNotStructurallyModifyInternalState() throws Exception {
		for (int i = 0; i < 1_000; i++) {
			myModifications.getOmittedSPNamesForResourceType("UnregisteredType" + i);
			myModifications.getAdditionalSearchParamNamesForResourceType("UnregisteredType" + i);
		}

		assertThat(readBackingMap("myOmittedResourceTypeToParameterCodeMap")).isEmpty();
		assertThat(readBackingMap("myAdditionalResourceTypeToParameterCodeMap")).isEmpty();

		myModifications.addSPToOmitFromCompartment("Group", "member");

		assertThat(readBackingMap("myOmittedResourceTypeToParameterCodeMap")).containsOnlyKeys("group");
		assertThat(readBackingMap("myAdditionalResourceTypeToParameterCodeMap")).isEmpty();
	}

	@Test
	void getSPNames_areMatchedCaseInsensitivelyOnResourceType() {
		myModifications.addSPToOmitFromCompartment("Group", "member");
		myModifications.addSPToIncludeInCompartment("DEVICE", "patient");

		assertThat(myModifications.getOmittedSPNamesForResourceType("group")).containsExactly("member");
		assertThat(myModifications.getOmittedSPNamesForResourceType("GROUP")).containsExactly("member");
		assertThat(myModifications.getOmittedSPNamesForResourceType("GrOuP")).containsExactly("member");
		assertThat(myModifications.getAdditionalSearchParamNamesForResourceType("device"))
				.containsExactly("patient");
	}

	@Test
	void addSPToOmitFromCompartment_doesNotAffectAdditionalSPNames() {
		myModifications.addSPToOmitFromCompartment("Group", "member");

		assertThat(myModifications.getAdditionalSearchParamNamesForResourceType("Group")).isEmpty();
	}

	@Test
	void fromAdditionalAndOmittedSPNames_populatesBothMaps() {
		CompartmentSearchParameterModifications actual =
				CompartmentSearchParameterModifications.fromAdditionalAndOmittedSPNames(
						"List", Set.of("subject"), Set.of("source", "patient"));

		assertThat(actual.getAdditionalSearchParamNamesForResourceType("List")).containsExactly("subject");
		assertThat(actual.getOmittedSPNamesForResourceType("List"))
				.containsExactlyInAnyOrder("source", "patient");
	}

	@Test
	void fromAdditionalCompartmentParamNames_populatesAdditionalOnly() {
		CompartmentSearchParameterModifications actual =
				CompartmentSearchParameterModifications.fromAdditionalCompartmentParamNames(
						"Device", Set.of("patient", "subject"));

		assertThat(actual.getAdditionalSearchParamNamesForResourceType("Device"))
				.containsExactlyInAnyOrder("patient", "subject");
		assertThat(actual.getOmittedSPNamesForResourceType("Device")).isEmpty();
	}

	@Test
	@Timeout(value = 120, unit = TimeUnit.SECONDS)
	void getSPNames_underConcurrentReads_neverThrowAndPreserveState() throws Exception {
		seedPatientCompartmentOmissions();
		seedEverySecondResourceType();

		List<Throwable> failures = runConcurrently(theThreadIndex -> {
			for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
				String resourceType = "ResourceType" + ((theThreadIndex + i) % RESOURCE_TYPE_COUNT);
				// forEach rather than contains() or count(), neither of which iterates the Set, and
				// iteration is where a concurrently mutated Set throws
				myModifications.getOmittedSPNamesForResourceType(resourceType).forEach(spName -> {});
				myModifications.getAdditionalSearchParamNamesForResourceType(resourceType).forEach(spName -> {});
			}
		});

		assertThat(failures).as("Concurrent reads must not modify shared state").isEmpty();
		assertSeededStateIntact();
	}

	@Test
	@Timeout(value = 120, unit = TimeUnit.SECONDS)
	void addAndGetSPNames_underConcurrentReadsAndWrites_neverThrowAndPreserveState() throws Exception {
		seedPatientCompartmentOmissions();

		List<Throwable> failures = runConcurrently(theThreadIndex -> {
			boolean writer = theThreadIndex < 2;
			for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
				String resourceType = "ResourceType" + ((theThreadIndex + i) % RESOURCE_TYPE_COUNT);
				if (writer) {
					myModifications.addSPToOmitFromCompartment(resourceType, "sp" + theThreadIndex);
					myModifications.addSPToIncludeInCompartment(resourceType, "sp" + theThreadIndex);
				} else {
					myModifications.getOmittedSPNamesForResourceType(resourceType).forEach(spName -> {});
					myModifications
							.getAdditionalSearchParamNamesForResourceType(resourceType)
							.forEach(spName -> {});
				}
			}
		});

		assertThat(failures)
				.as("Concurrent reads and writes must not corrupt shared state")
				.isEmpty();
		assertSeededStateIntact();
		for (int i = 0; i < RESOURCE_TYPE_COUNT; i++) {
			assertThat(myModifications.getOmittedSPNamesForResourceType("ResourceType" + i))
					.containsExactlyInAnyOrder("sp0", "sp1");
			assertThat(myModifications.getAdditionalSearchParamNamesForResourceType("ResourceType" + i))
					.containsExactlyInAnyOrder("sp0", "sp1");
		}
	}

	private void seedPatientCompartmentOmissions() {
		myModifications.addSPToOmitFromCompartment("Group", "member");
		myModifications.addSPToOmitFromCompartment("List", "subject");
		myModifications.addSPToOmitFromCompartment("List", "source");
		myModifications.addSPToOmitFromCompartment("List", "patient");
	}

	/**
	 * Registers SPs on every second resource type, so concurrent reads hit a mix of populated keys - where
	 * traversal does real work - and absent keys.
	 */
	private void seedEverySecondResourceType() {
		for (int i = 0; i < RESOURCE_TYPE_COUNT; i += 2) {
			String resourceType = "ResourceType" + i;
			SEEDED_OMITTED_SPS.forEach(spName -> myModifications.addSPToOmitFromCompartment(resourceType, spName));
			SEEDED_ADDITIONAL_SPS.forEach(
					spName -> myModifications.addSPToIncludeInCompartment(resourceType, spName));
		}
	}

	private void assertSeededStateIntact() {
		assertThat(myModifications.getOmittedSPNamesForResourceType("Group")).contains("member");
		assertThat(myModifications.getOmittedSPNamesForResourceType("List"))
				.contains("subject", "source", "patient");
	}

	/**
	 * Runs the given task on {@link #THREAD_COUNT} threads that all start hammering at the same moment,
	 * and returns everything that was thrown. The tasks deliberately do not assert - assertions belong
	 * on the calling thread.
	 */
	private List<Throwable> runConcurrently(ThreadTask theTask) throws Exception {
		List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());
		CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		try {
			List<Future<?>> futures = new ArrayList<>();
			for (int threadIndex = 0; threadIndex < THREAD_COUNT; threadIndex++) {
				int index = threadIndex;
				futures.add(executor.submit(() -> {
					try {
						barrier.await(30, TimeUnit.SECONDS);
						theTask.run(index);
					} catch (Throwable t) {
						failures.add(t);
					}
				}));
			}
			for (Future<?> future : futures) {
				// A corrupted HashMap can hang a reader rather than throw, so bound the wait
				future.get(60, TimeUnit.SECONDS);
			}
		} finally {
			executor.shutdownNow();
		}
		return failures;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Set<String>> readBackingMap(String theFieldName) throws Exception {
		// Reflection because there is no API to observe whether a read inserted an entry
		Field field = CompartmentSearchParameterModifications.class.getDeclaredField(theFieldName);
		field.setAccessible(true);
		return (Map<String, Set<String>>) field.get(myModifications);
	}

	@FunctionalInterface
	private interface ThreadTask {
		void run(int theThreadIndex);
	}
}
