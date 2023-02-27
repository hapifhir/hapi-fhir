package ca.uhn.test.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test helper to force a particular sequence on 2 or more threads.
 * Wraps Phaser with an Enum for better messages, and some test support.
 * The only use is to impose a particular execution sequence over multiple threads when reproducing bugs.
 *
 * The simplest usage is to declare the number of collaborators as theParticipantCount
 * in the constructor, and then have each participant thread call {@link #arriveAndAwaitSharedEndOf}
 * as they finish the work of every phase.
 * Every thread needs to confirm, even if they do no work in that phase.
 * <p>
 * Note: this is just a half-baked wrapper around Phaser.
 * The behaviour is not especially precise, or tested. Comments welcome: MB.
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

	public void assertInPhase(E theStageEnum) {
		assertEquals(theStageEnum, getPhase(), "In stage " + theStageEnum);
	}

	public E getPhase() {
		return phaseToEnum(myPhaser.getPhase());
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

	// arriveAndAwaitSharedEndOf is safest.  Make these public if they become useful.

	E arrive() {
		E result = phaseToEnum(myPhaser.arrive());
		ourLog.info("Arrive to my end of phase {}", result);
		return result;
	}


	private E doAwait(E thePhase) {
		ourLog.debug("Start doAwait - {}", thePhase);
		E phase = phaseToEnum(myPhaser.awaitAdvance(thePhase.ordinal()));
		ourLog.info("Done waiting for end of {}.  Now starting {}", thePhase, getPhase());
		return phase;
	}

	private void checkAwait(E thePhase) {
		E currentPhase = getPhase();
		if (currentPhase.ordinal() < thePhase.ordinal()) {
			fail(String.format("Can't wait for end of phase %s, still in phase %s", thePhase, currentPhase));
		} else if (currentPhase.ordinal() > thePhase.ordinal()) {
			fail(String.format("Can't wait for end of phase %s, already in phase %s", thePhase, currentPhase));
		}
	}


	private E phaseToEnum(int resultOrdinal) {
		if (resultOrdinal >= myEnumConstants.length) {
			throw new IllegalStateException("Enum " + myEnumClass.getName() + " should declare one more enum value for post-completion reporting of phase " + resultOrdinal);
		}
		return myEnumConstants[resultOrdinal];
	}


}
