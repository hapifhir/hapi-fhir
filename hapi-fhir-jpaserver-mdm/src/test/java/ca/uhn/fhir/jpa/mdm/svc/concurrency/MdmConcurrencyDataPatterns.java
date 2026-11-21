package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The data patterns R1-R22 of the concurrency matrix, with their hand-written expected outcomes.
 * <p>
 * The rules in {@code mdm/mdm-rules-concurrency-matrix.json} give these meanings to the data:
 * <ul>
 *     <li>A source's candidate pool holds the sources whose given name starts with its own given name.</li>
 *     <li>The same family name and birth date is a MATCH; the same family name alone is a POSSIBLE_MATCH; anything
 *     else is NO_MATCH.</li>
 *     <li>NPI and MRN are EID systems.</li>
 *     <li>A source with gender {@code other} is blocked from matching.</li>
 * </ul>
 * Expected outcomes are keyed by the order in which sources committed. Every source is stored before any MDM
 * processing, so a pooled source that hasn't been processed yet has no MATCH link and is dropped as a candidate.
 */
// Created by claude-opus-5-5
public final class MdmConcurrencyDataPatterns {

	public static final String NPI_SYSTEM = "http://company.io/fhir/NamingSystem/npi";
	public static final String MRN_SYSTEM = "http://company.io/fhir/NamingSystem/mrn";
	private static final String DOB_1 = "1980-01-01";
	private static final String DOB_2 = "1975-05-05";
	private static final String DOB_3 = "1990-09-09";

	private MdmConcurrencyDataPatterns() {}

	/**
	 * What a pattern needs from the test to create and prepare its data.
	 */
	public interface PatternContext {
		Patient create(Patient thePatient);

		/** Processes the source through MDM on the calling thread, before the race. */
		void processSerially(Patient theSource);

		Patient update(Patient thePatient);
	}

	/**
	 * Checks the actual outcome of a run.
	 */
	@FunctionalInterface
	public interface Expectation {
		void verify(MdmOutcome theActual);

		static Expectation exactly(MdmOutcome theExpected) {
			return theActual -> assertThat(theActual).isEqualTo(theExpected);
		}
	}

	/**
	 * @param id               the pattern ID, for example {@code R3}
	 * @param threadSources    the source each thread processes before reassignment, by label
	 * @param roles            the role of each label; labels with the same role are interchangeable
	 * @param resources        how to build each source
	 * @param updatedSources   labels whose processing is an UPDATE rather than a CREATE
	 * @param setup            work done before the race, given the created sources by label
	 * @param expectation      the expected outcome, given the labels in the order they committed
	 * @param collisionFree    true if no claims should collide: no conflicts, restarts or blocked steps
	 */
	public record DataPattern(
			String id,
			List<String> threadSources,
			Map<String, String> roles,
			Map<String, Supplier<Patient>> resources,
			Set<String> updatedSources,
			Setup setup,
			Function<List<String>, Expectation> expectation,
			boolean collisionFree) {

		/**
		 * Every distinct assignment of sources to threads: assignments that only swap sources with the same role are
		 * the same.
		 */
		public List<List<String>> assignments() {
			Map<List<String>, List<String>> byRoles = new LinkedHashMap<>();
			for (List<String> permutation : permutations(threadSources)) {
				List<String> roleSequence = permutation.stream().map(roles::get).toList();
				byRoles.putIfAbsent(roleSequence, permutation);
			}
			return List.copyOf(byRoles.values());
		}

		@Override
		public String toString() {
			return id;
		}
	}

	@FunctionalInterface
	public interface Setup {
		void prepare(PatternContext theContext, Map<String, Patient> theSources);
	}

	public static List<DataPattern> threeSourcePatterns() {
		return List.of(r1(), r2(), r3(), r4(), r6(), r7(), r8(), r10(), r12(), r15(), r18(), r19(), r22());
	}

	public static List<DataPattern> twoSourcePatterns() {
		return List.of(r20(), r21());
	}

	/** R1: one resource submitted to all three threads. */
	static DataPattern r1() {
		return pattern("R1", List.of("a", "a", "a"))
				.resource("a", "a", () -> patient("Ann", "Smith", DOB_1, null, null))
				.expect(order -> Expectation.exactly(MdmOutcome.builder().golden("a").build()))
				.build();
	}

	/** R2: three representations of one patient, all carrying the same NPI. */
	static DataPattern r2() {
		return pattern("R2", List.of("a", "b", "c"))
				.resource("a", "R2", () -> patient("Bea", "Adams", DOB_1, "npi-2", null))
				.resource("b", "R2", () -> patient("Bianca", "Baker", DOB_2, "npi-2", null))
				.resource("c", "R2", () -> patient("Bella", "Clark", DOB_3, "npi-2", null))
				.expect(order -> Expectation.exactly(oneGolden(order)))
				.build();
	}

	/** R3: a carries the NPI, b the MRN, and c bridges them by carrying both. */
	static DataPattern r3() {
		MdmOutcome split = MdmOutcome.builder()
				.golden("a")
				.golden("b")
				.possible("c", "a")
				.possible("c", "b")
				.duplicate("a", "b")
				.build();
		MdmOutcome joined = MdmOutcome.builder().golden("a", "b", "c").build();
		Map<String, MdmOutcome> table = new LinkedHashMap<>();
		table.put("abc", split);
		table.put("bac", split);
		table.put("acb", joined);
		table.put("bca", joined);
		table.put("cab", joined);
		table.put("cba", joined);
		table.put("ab", MdmOutcome.builder().golden("a").golden("b").build());
		table.put("ba", MdmOutcome.builder().golden("a").golden("b").build());
		for (String pair : List.of("ac", "ca", "bc", "cb")) {
			table.put(pair, oneGolden(List.of(pair.substring(0, 1), pair.substring(1))));
		}
		return pattern("R3", List.of("a", "b", "c"))
				.resource("a", "a", () -> patient("Cleo", "Dunn", DOB_1, "npi-3", null))
				.resource("b", "b", () -> patient("Dora", "Evans", DOB_2, null, "mrn-3"))
				.resource("c", "c", () -> patient("Edna", "Fox", DOB_3, "npi-3", "mrn-3"))
				.expect(byOrder(table))
				.build();
	}

	/** R4/R5: a and b share an NPI; c (R5) is a different patient with an MRN. */
	static DataPattern r4() {
		return pattern("R4", List.of("a", "b", "c"))
				.resource("a", "R4", () -> patient("Faye", "Gray", DOB_1, "npi-4", null))
				.resource("b", "R4", () -> patient("Gina", "Hart", DOB_2, "npi-4", null))
				.resource("c", "R5", () -> patient("Hope", "Irwin", DOB_3, null, "mrn-5"))
				.expect(order -> Expectation.exactly(groups(order, Set.of("a", "b"), Set.of("c"))))
				.build();
	}

	/** R6: three representations of one patient without EIDs, all in each other's pools, all matching. */
	static DataPattern r6() {
		return pattern("R6", List.of("a", "b", "c"))
				.resource("a", "R6", () -> patient("Ivy", "Jones", DOB_1, null, null))
				.resource("b", "R6", () -> patient("Ivy", "Jones", DOB_1, null, null))
				.resource("c", "R6", () -> patient("Ivy", "Jones", DOB_1, null, null))
				.expect(order -> Expectation.exactly(oneGolden(order)))
				.build();
	}

	/** R7: a is in the pools of b and c, b is in the pool of c, not the reverse; all match. */
	static DataPattern r7() {
		Map<String, MdmOutcome> table = new LinkedHashMap<>();
		table.put("abc", MdmOutcome.builder().golden("a", "b", "c").build());
		table.put("acb", MdmOutcome.builder().golden("a", "b", "c").build());
		table.put(
				"bac",
				MdmOutcome.builder()
						.golden("a")
						.golden("b")
						.possible("c", "a")
						.possible("c", "b")
						.duplicate("a", "b")
						.build());
		table.put("bca", MdmOutcome.builder().golden("b", "c").golden("a").build());
		table.put("cab", MdmOutcome.builder().golden("c").golden("a", "b").build());
		table.put("cba", MdmOutcome.builder().golden("c").golden("b").golden("a").build());
		table.put("ab", MdmOutcome.builder().golden("a", "b").build());
		table.put("ba", MdmOutcome.builder().golden("a").golden("b").build());
		table.put("ac", MdmOutcome.builder().golden("a", "c").build());
		table.put("ca", MdmOutcome.builder().golden("a").golden("c").build());
		table.put("bc", MdmOutcome.builder().golden("b", "c").build());
		table.put("cb", MdmOutcome.builder().golden("b").golden("c").build());
		return pattern("R7", List.of("a", "b", "c"))
				.resource("a", "a", () -> patient("Johnathan", "Lee", DOB_1, null, null))
				.resource("b", "b", () -> patient("John", "Lee", DOB_1, null, null))
				.resource("c", "c", () -> patient("Jo", "Lee", DOB_1, null, null))
				.expect(byOrder(table))
				.build();
	}

	/** R8/R9: all in each other's pools; a and b match; c (R9) is a different patient. */
	static DataPattern r8() {
		return pattern("R8", List.of("a", "b", "c"))
				.resource("a", "R8", () -> patient("Kim", "Moore", DOB_1, null, null))
				.resource("b", "R8", () -> patient("Kim", "Moore", DOB_1, null, null))
				.resource("c", "R9", () -> patient("Kim", "Wright", DOB_2, null, null))
				.expect(order -> Expectation.exactly(groups(order, Set.of("a", "b"), Set.of("c"))))
				.build();
	}

	/**
	 * R10/R11: a ("Elias") is in b's ("Eli") pool but not the reverse, and they match; c (R11) is in both pools and
	 * has both in its pool, but matches neither.
	 */
	static DataPattern r10() {
		return pattern("R10", List.of("a", "b", "c"))
				.resource("a", "a", () -> patient("Elias", "Nash", DOB_1, null, null))
				.resource("b", "b", () -> patient("Eli", "Nash", DOB_1, null, null))
				.resource("c", "c", () -> patient(new String[] {"Eli", "Elias"}, "Owen", DOB_2, null, null, null))
				.expect(order -> {
					MdmOutcome.Builder outcome = MdmOutcome.builder();
					List<String> rTen = order.stream().filter(l -> !l.equals("c")).toList();
					if (rTen.equals(List.of("a", "b"))) {
						outcome.golden("a", "b");
					} else {
						rTen.forEach(outcome::golden);
					}
					if (order.contains("c")) {
						outcome.golden("c");
					}
					return Expectation.exactly(outcome.build());
				})
				.build();
	}

	/** R12-R14: different patients, all in each other's pools, none matching. */
	static DataPattern r12() {
		return pattern("R12", List.of("a", "b", "c"))
				.resource("a", "R12", () -> patient("Lyn", "Price", DOB_1, null, null))
				.resource("b", "R12", () -> patient("Lyn", "Quinn", DOB_2, null, null))
				.resource("c", "R12", () -> patient("Lyn", "Reed", DOB_3, null, null))
				.expect(order -> Expectation.exactly(separate(order)))
				.collisionFree()
				.build();
	}

	/** R15-R17: unrelated patients. */
	static DataPattern r15() {
		return pattern("R15", List.of("a", "b", "c"))
				.resource("a", "R15", () -> patient("Mia", "Stone", DOB_1, null, null))
				.resource("b", "R15", () -> patient("Nora", "Tate", DOB_2, null, null))
				.resource("c", "R15", () -> patient("Opal", "Upton", DOB_3, null, null))
				.expect(order -> Expectation.exactly(separate(order)))
				.collisionFree()
				.build();
	}

	/**
	 * R18: all in each other's pools, each pair a POSSIBLE_MATCH. The first to commit gets the only golden resource;
	 * each later source is a POSSIBLE_MATCH to it.
	 */
	static DataPattern r18() {
		return pattern("R18", List.of("a", "b", "c"))
				.resource("a", "R18", () -> patient("Pia", "Vance", DOB_1, null, null))
				.resource("b", "R18", () -> patient("Pia", "Vance", DOB_2, null, null))
				.resource("c", "R18", () -> patient("Pia", "Vance", DOB_3, null, null))
				.expect(order -> {
					MdmOutcome.Builder outcome = MdmOutcome.builder();
					String first = order.get(0);
					outcome.golden(first);
					order.stream().skip(1).forEach(later -> outcome.possible(later, first));
					return Expectation.exactly(outcome.build());
				})
				.build();
	}

	/** R19: a is blocked from matching but matches b and c by score; b and c match each other. */
	static DataPattern r19() {
		MdmOutcome all = MdmOutcome.builder().golden("a", "b", "c").build();
		Map<String, MdmOutcome> table = new LinkedHashMap<>();
		table.put("abc", all);
		table.put("acb", all);
		table.put(
				"bac",
				MdmOutcome.builder()
						.golden("a")
						.golden("b")
						.possible("c", "a")
						.possible("c", "b")
						.duplicate("a", "b")
						.build());
		table.put("bca", MdmOutcome.builder().golden("b", "c").golden("a").build());
		table.put(
				"cab",
				MdmOutcome.builder()
						.golden("a")
						.golden("c")
						.possible("b", "a")
						.possible("b", "c")
						.duplicate("a", "c")
						.build());
		table.put("cba", MdmOutcome.builder().golden("b", "c").golden("a").build());
		table.put("ab", MdmOutcome.builder().golden("a", "b").build());
		table.put("ba", MdmOutcome.builder().golden("a").golden("b").build());
		table.put("ac", MdmOutcome.builder().golden("a", "c").build());
		table.put("ca", MdmOutcome.builder().golden("a").golden("c").build());
		table.put("bc", MdmOutcome.builder().golden("b", "c").build());
		table.put("cb", MdmOutcome.builder().golden("b", "c").build());
		return pattern("R19", List.of("a", "b", "c"))
				.resource(
						"a",
						"blocked",
						() -> patient(new String[] {"Rae"}, "West", DOB_1, Enumerations.AdministrativeGender.OTHER, null, null))
				.resource("b", "R19", () -> patient("Rae", "West", DOB_1, null, null))
				.resource("c", "R19", () -> patient("Rae", "West", DOB_1, null, null))
				.expect(byOrder(table))
				.build();
	}

	/** R20: a carries an NPI; b has no EID but matches a by score. */
	static DataPattern r20() {
		return pattern("R20", List.of("a", "b"))
				.resource("a", "a", () -> patient("Sue", "Young", DOB_1, "npi-20", null))
				.resource("b", "b", () -> patient("Sue", "Young", DOB_1, null, null))
				.expect(order -> Expectation.exactly(oneGolden(order)))
				.build();
	}

	/**
	 * R21: a was already linked to GR_1 (by its NPI); an UPDATE of a that adds an MRN races with b, which carries
	 * only that MRN.
	 */
	static DataPattern r21() {
		Function<List<String>, Expectation> expectation = order -> {
			String key = String.join("", order);
			return switch (key) {
				case "ab" -> Expectation.exactly(MdmOutcome.builder().golden("a", "b").build());
				// Only the update of a: GR_1 gains the MRN
				case "a" -> Expectation.exactly(MdmOutcome.builder().golden("a").build());
				// Only b: its source-EID search finds a (whose update committed before the race) on GR_1, but b's MRN
				// and GR_1's NPI differ, so b gets its own golden resource, flagged as a possible duplicate of GR_1
				case "b" -> Expectation.exactly(MdmOutcome.builder()
						.golden("a")
						.golden("b")
						.duplicate("a", "b")
						.build());
				case "ba" -> theActual -> {
					// b got its own golden resource first; the update of a then finds GR_1 by NPI and b's by MRN
					assertThat(theActual.goldenResources()).contains("b");
					assertThat(theActual.possibleDuplicates()).hasSize(1);
				};
				default -> throw new IllegalStateException("No expectation for order " + key);
			};
		};
		return pattern("R21", List.of("a", "b"))
				.resource("a", "a", () -> patient("Tia", "Zane", DOB_1, "npi-21", null))
				.resource("b", "b", () -> patient("Uma", "Abbot", DOB_2, null, "mrn-21"))
				.updated("a")
				.setup((theContext, theSources) -> {
					Patient a = theSources.get("a");
					theContext.processSerially(a);
					a.addIdentifier().setSystem(MRN_SYSTEM).setValue("mrn-21");
					theSources.put("a", theContext.update(a));
				})
				.expect(expectation)
				.build();
	}

	/** R22: x was processed before the race; three new sources matching it race. */
	static DataPattern r22() {
		return pattern("R22", List.of("a", "b", "c"))
				.resource("x", "x", () -> patient("Vera", "Bloom", DOB_1, null, null))
				.resource("a", "R22", () -> patient("Vera", "Bloom", DOB_1, null, null))
				.resource("b", "R22", () -> patient("Vera", "Bloom", DOB_1, null, null))
				.resource("c", "R22", () -> patient("Vera", "Bloom", DOB_1, null, null))
				.setup((theContext, theSources) -> theContext.processSerially(theSources.get("x")))
				.expect(order -> {
					List<String> members = new ArrayList<>(order);
					members.add("x");
					return Expectation.exactly(oneGolden(members));
				})
				.build();
	}

	private static MdmOutcome oneGolden(List<String> theCommitted) {
		return MdmOutcome.builder()
				.golden(theCommitted.stream().distinct().toArray(String[]::new))
				.build();
	}

	private static MdmOutcome separate(List<String> theCommitted) {
		MdmOutcome.Builder retVal = MdmOutcome.builder();
		theCommitted.forEach(retVal::golden);
		return retVal.build();
	}

	/**
	 * One golden resource per group, holding the group's sources that committed.
	 */
	@SafeVarargs
	private static MdmOutcome groups(List<String> theCommitted, Set<String>... theGroups) {
		MdmOutcome.Builder retVal = MdmOutcome.builder();
		for (Set<String> group : theGroups) {
			String[] members = theCommitted.stream().filter(group::contains).toArray(String[]::new);
			if (members.length > 0) {
				retVal.golden(members);
			}
		}
		return retVal.build();
	}

	private static Function<List<String>, Expectation> byOrder(Map<String, MdmOutcome> theTable) {
		return order -> {
			String key = String.join("", order);
			MdmOutcome expected = theTable.get(key);
			if (expected == null) {
				throw new IllegalStateException("No expectation for commit order " + key);
			}
			return Expectation.exactly(expected);
		};
	}

	static Patient patient(String theGiven, String theFamily, String theBirthDate, String theNpi, String theMrn) {
		return patient(new String[] {theGiven}, theFamily, theBirthDate, null, theNpi, theMrn);
	}

	static Patient patient(
			String[] theGivens,
			String theFamily,
			String theBirthDate,
			Enumerations.AdministrativeGender theGender,
			String theNpi,
			String theMrn) {
		Patient retVal = new Patient();
		HumanName name = retVal.addName().setFamily(theFamily);
		for (String given : theGivens) {
			name.addGiven(given);
		}
		retVal.setBirthDateElement(new DateType(theBirthDate));
		retVal.setGender(theGender == null ? Enumerations.AdministrativeGender.FEMALE : theGender);
		if (theNpi != null) {
			retVal.addIdentifier().setSystem(NPI_SYSTEM).setValue(theNpi);
		}
		if (theMrn != null) {
			retVal.addIdentifier().setSystem(MRN_SYSTEM).setValue(theMrn);
		}
		return retVal;
	}

	private static List<List<String>> permutations(List<String> theItems) {
		if (theItems.size() <= 1) {
			return List.of(theItems);
		}
		List<List<String>> retVal = new ArrayList<>();
		for (int i = 0; i < theItems.size(); i++) {
			List<String> rest = new ArrayList<>(theItems);
			String first = rest.remove(i);
			for (List<String> tail : permutations(rest)) {
				List<String> next = new ArrayList<>();
				next.add(first);
				next.addAll(tail);
				retVal.add(next);
			}
		}
		return retVal;
	}

	private static PatternBuilder pattern(String theId, List<String> theThreadSources) {
		return new PatternBuilder(theId, theThreadSources);
	}

	private static class PatternBuilder {
		private final String myId;
		private final List<String> myThreadSources;
		private final Map<String, String> myRoles = new LinkedHashMap<>();
		private final Map<String, Supplier<Patient>> myResources = new LinkedHashMap<>();
		private final Set<String> myUpdated = new HashSet<>();
		private Setup mySetup = (theContext, theSources) -> {};
		private Function<List<String>, Expectation> myExpectation;
		private boolean myCollisionFree;

		PatternBuilder(String theId, List<String> theThreadSources) {
			myId = theId;
			myThreadSources = theThreadSources;
		}

		PatternBuilder resource(String theLabel, String theRole, Supplier<Patient> theBuilder) {
			myRoles.put(theLabel, theRole);
			myResources.put(theLabel, theBuilder);
			return this;
		}

		PatternBuilder updated(String theLabel) {
			myUpdated.add(theLabel);
			return this;
		}

		PatternBuilder setup(Setup theSetup) {
			mySetup = theSetup;
			return this;
		}

		PatternBuilder expect(Function<List<String>, Expectation> theExpectation) {
			myExpectation = theExpectation;
			return this;
		}

		PatternBuilder collisionFree() {
			myCollisionFree = true;
			return this;
		}

		DataPattern build() {
			return new DataPattern(
					myId,
					myThreadSources,
					myRoles,
					myResources,
					Set.copyOf(myUpdated),
					mySetup,
					myExpectation,
					myCollisionFree);
		}
	}
}
