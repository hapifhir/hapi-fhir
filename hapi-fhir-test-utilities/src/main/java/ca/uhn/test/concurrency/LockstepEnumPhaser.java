package ca.uhn.test.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test helper to force a particular sequence on 2 or more threads.
 * Wraps Phaser with an Enum for better messages, and some test support.
 *
 * @param <E> an enum used to name the phases.
 */
public class LockstepEnumPhaser<E extends Enum<E>> {
	private static final Logger ourLog = LoggerFactory.getLogger(LockstepEnumPhaser.class);
	final Phaser myPhaser;
	final Class<E> myEnumClass;
	final E[] myEnumConstants;

	public LockstepEnumPhaser(int theParticipantCount, Class<E> theEnumClass) {
		myPhaser = new Phaser(theParticipantCount);
		myEnumClass = theEnumClass;
		myEnumConstants = myEnumClass.getEnumConstants();
	}

	public E arrive() {
		E result = phaseToEnum(myPhaser.arrive());
		ourLog.info("Arrive in phase {}", result);
		return result;
	}

	public void assertInPhase(E theStageEnum) {
		assertEquals(theStageEnum, getPhase(), "In stage " + theStageEnum);
	}

	public E getPhase() {
		return phaseToEnum(myPhaser.getPhase());
	}

	public E awaitAdvance(E thePhase) {
		checkAwait(thePhase);
		return doAwait(thePhase);
	}

	/**
	 * Like arrive(), but verify stage first
	 */
	public E arriveAtMyEndOf(E thePhase) {
		assertInPhase(thePhase);
		return arrive();
	}

	public E arriveAndAwaitSharedEndOf(E thePhase) {
		checkAwait(thePhase);
		arrive();
		return doAwait(thePhase);
	}

	public E arriveAndDeregister() {
		return phaseToEnum(myPhaser.arriveAndDeregister());
	}

	public E register() {
		return phaseToEnum(myPhaser.register());
	}

	private E doAwait(E thePhase) {
		ourLog.debug("Start doAwait - {}", thePhase);
		E phase = phaseToEnum(myPhaser.awaitAdvance(thePhase.ordinal()));
		ourLog.info("Finish doAwait - {}", thePhase);
		return phase;
	}

	private void checkAwait(E thePhase) {
		E currentPhase = getPhase();
		if (currentPhase.ordinal() < thePhase.ordinal()) {
			fail("Can't wait for end of phase " + thePhase + ", still in phase " + currentPhase);
		} else if (currentPhase.ordinal() > thePhase.ordinal()) {
			ourLog.warn("Skip waiting for phase {}, already in phase {}", thePhase, currentPhase);
		}
	}


	private E phaseToEnum(int resultOrdinal) {
		if (resultOrdinal >= myEnumConstants.length) {
			throw new IllegalStateException("Enum " + myEnumClass.getName() + " should declare one more stage for post-completion reporting in phase " + resultOrdinal);
		}
		return myEnumConstants[resultOrdinal];
	}


}
