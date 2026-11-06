package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.svc.concurrency.ConcurrencyScheduler.EventTypeEnum;
import ca.uhn.fhir.jpa.mdm.svc.concurrency.ConcurrencyScheduler.StepOutcomeEnum;
import ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyDataPatterns.DataPattern;
import ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyDataPatterns.PatternContext;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The concurrency test matrix: every thread sequence (P1-P24) against every data pattern (R1-R22) and every distinct
 * assignment of the pattern's sources to threads. See {@link ThreadSequence} and {@link MdmConcurrencyDataPatterns}.
 * <p>
 * Each case stores its sources, runs any setup, then has {@link ConcurrencyScheduler} drive one worker thread per
 * source through the sequence. It then checks the resulting golden resources and links against the outcome expected
 * for the order in which the sources actually committed.
 */
@ContextConfiguration(classes = MdmConcurrencyMatrixTestConfig.class)
@TestPropertySource(
		properties = {
			"module.mdm.config.script.file=classpath:mdm/mdm-rules-concurrency-matrix.json",
			"mdm.prevent_eid_updates=false"
		})
// Created by claude-opus-5-5
class MdmConcurrencyMatrixIT extends BaseMdmR4Test {

	@Autowired
	private ConcurrencyScheduler myScheduler;

	@Autowired
	private IMdmMatchClaimJpaRepository myMdmMatchClaimJpaRepository;

	private int myOriginalMaxRetries;

	@BeforeEach
	void beforeAllowManyRetries() {
		// Blocked threads hit the database lock timeout while the scheduler holds their competitor, and retry
		myOriginalMaxRetries = myMdmSettings.getMatchConflictMaxRetries();
		myMdmSettings.setMatchConflictMaxRetries(1000);
	}

	@AfterEach
	void afterCleanUp() {
		myMdmSettings.setMatchConflictMaxRetries(myOriginalMaxRetries);
		runInTransaction(() -> myMdmMatchClaimJpaRepository.deleteAll());
	}

	/**
	 * Set the {@code mdm.matrix.filter} system property to a regular expression to run only the matching cases, for
	 * example {@code -Dmdm.matrix.filter="P7 x R3 .*"}.
	 */
	static Stream<Arguments> cases() {
		List<Arguments> retVal = new ArrayList<>();
		addCases(retVal, ThreadSequence.forThreadCount(3), MdmConcurrencyDataPatterns.threeSourcePatterns());
		addCases(retVal, ThreadSequence.forThreadCount(2), MdmConcurrencyDataPatterns.twoSourcePatterns());
		String filter = System.getProperty("mdm.matrix.filter");
		return retVal.stream().filter(theCase -> filter == null || ((String) theCase.get()[0]).matches(filter));
	}

	private static void addCases(List<Arguments> theCases, List<ThreadSequence> theSequences, List<DataPattern> thePatterns) {
		for (DataPattern pattern : thePatterns) {
			for (List<String> assignment : pattern.assignments()) {
				for (ThreadSequence sequence : theSequences) {
					String name = sequence + " x " + pattern.id() + " " + assignment;
					theCases.add(Arguments.of(name, sequence, pattern, assignment));
				}
			}
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("cases")
	void matrix(String theName, ThreadSequence theSequence, DataPattern thePattern, List<String> theAssignment) {
		// Setup
		Map<String, Patient> sources = createSources(thePattern);
		List<Runnable> tasks = new ArrayList<>();
		for (String label : theAssignment) {
			// Each thread gets its own copy, since MDM can change the source it is given
			Patient copy = myPatientDao.read(sources.get(label).getIdElement().toUnqualifiedVersionless(), mySrd);
			boolean update = thePattern.updatedSources().contains(label);
			tasks.add(() -> myMdmMatchLinkSvc.updateMdmLinksForMdmSource(
					copy, update ? createContextForUpdate("Patient") : createContextForCreate("Patient")));
		}

		// Execute
		ConcurrencyScheduler.Run run = myScheduler.run(theSequence, tasks, theAssignment);

		// Verify
		int abortedThread = theSequence.abortedThread();
		for (int i = 0; i < run.workers().size(); i++) {
			ConcurrencyScheduler.Worker worker = run.workers().get(i);
			if (i + 1 == abortedThread) {
				assertThat(worker.isAborted()).as("%s aborted; %s", worker.getName(), run).isTrue();
			} else {
				assertThat(worker.getFailure()).as("%s failed; %s", worker.getName(), run).isNull();
			}
		}

		List<String> commitOrder = run.commitOrder();
		MdmOutcome actual = readOutcome(sources);
		try {
			thePattern.expectation().apply(commitOrder).verify(actual);
		} catch (AssertionError e) {
			throw new AssertionError("Commit order " + commitOrder + ", " + run + ": " + e.getMessage(), e);
		}

		if (thePattern.collisionFree()) {
			assertThat(run.count(EventTypeEnum.CONFLICT)).as("conflicts; %s", run).isZero();
			assertThat(run.count(EventTypeEnum.ROLLBACK)).as("restarts; %s", run).isZero();
			assertThat(run.anyStep(StepOutcomeEnum.BLOCKED)).as("blocked; %s", run).isFalse();
		}
	}

	private Map<String, Patient> createSources(DataPattern thePattern) {
		Map<String, Patient> sources = new LinkedHashMap<>();
		thePattern.resources().forEach((label, builder) -> sources.put(label, createPatient(builder.get())));
		PatternContext context = new PatternContext() {
			@Override
			public Patient create(Patient thePatient) {
				return createPatient(thePatient);
			}

			@Override
			public void processSerially(Patient theSource) {
				myMdmMatchLinkSvc.updateMdmLinksForMdmSource(theSource, createContextForCreate("Patient"));
			}

			@Override
			public Patient update(Patient thePatient) {
				return (Patient) myPatientDao.update(thePatient, mySrd).getResource();
			}
		};
		thePattern.setup().prepare(context, sources);
		return sources;
	}

	/**
	 * Describes the MDM links by source label.
	 */
	private MdmOutcome readOutcome(Map<String, Patient> theSources) {
		return runInTransaction(() -> {
			Map<Long, String> labelsByPid = new HashMap<>();
			theSources.forEach((label, patient) -> labelsByPid.put(pidOf(patient), label));

			Set<Long> goldenPids = new TreeSet<>();
			for (IBaseResource golden : getAllGoldenPatients()) {
				goldenPids.add(pidOf(golden));
			}

			List<MdmLink> links = myMdmLinkDao.findAll();
			Map<Long, Set<String>> membersByGolden = new HashMap<>();
			goldenPids.forEach(pid -> membersByGolden.put(pid, new TreeSet<>()));
			for (MdmLink link : links) {
				if (link.getMatchResult() == MdmMatchResultEnum.MATCH) {
					String label = labelsByPid.get(link.getSourcePersistenceId().getId());
					membersByGolden
							.computeIfAbsent(link.getGoldenResourcePersistenceId().getId(), pid -> new TreeSet<>())
							.add(label);
				}
			}
			Map<Long, String> goldenNames = new HashMap<>();
			membersByGolden.forEach((pid, members) -> goldenNames.put(pid, String.join("+", members)));

			Set<String> possibleMatches = new TreeSet<>();
			Set<String> possibleDuplicates = new TreeSet<>();
			for (MdmLink link : links) {
				String golden = goldenNames.get(link.getGoldenResourcePersistenceId().getId());
				if (link.getMatchResult() == MdmMatchResultEnum.POSSIBLE_MATCH) {
					possibleMatches.add(labelsByPid.get(link.getSourcePersistenceId().getId()) + "~" + golden);
				} else if (link.getMatchResult() == MdmMatchResultEnum.POSSIBLE_DUPLICATE) {
					String other = goldenNames.get(link.getSourcePersistenceId().getId());
					possibleDuplicates.add(MdmOutcome.duplicateName(golden, other));
				}
			}
			return new MdmOutcome(new TreeSet<>(goldenNames.values()), possibleMatches, possibleDuplicates);
		});
	}

	private Long pidOf(IBaseResource theResource) {
		JpaPid pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theResource);
		return pid.getId();
	}
}
