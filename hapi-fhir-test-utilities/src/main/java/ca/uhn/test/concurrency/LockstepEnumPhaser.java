/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.test.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test helper to force a particular sequence on multiple threads.
 * Wraps Phaser with an Enum for better messages, and some test support.
 * The only use is to impose a particular execution sequence over multiple threads when reproducing bugs.
 * <p>
 * The simplest usage is to declare the number of collaborators as theParticipantCount
 * in the constructor, and then have each participant thread call {@link #arriveAndAwaitSharedEndOf}
 * as they finish the work of every phase.
 * Every thread needs to confirm, even if they do no work in that phase.
 * <p>
 * Note: this is just a half-baked wrapper around Phaser.
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
		assertThat(theStageEnum).as("In stage" + theStageEnum).isEqualTo(getPhase());
	}

	/**
	 * Get the current phase.
	 */
	public E getPhase() {
		return phaseToEnum(myPhaser.getPhase());
	}

	/**
	 * Declare that this thread-participant has finished the work of thePhase,
	 * and then wait until all other participants have also finished.
	 *
	 * @param thePhase the phase the thread just completed
	 * @return the new phase starting.
	 */
	public E arriveAndAwaitSharedEndOf(E thePhase) {
		checkAwait(thePhase);
		E current = arrive();
		assertEquals(current, thePhase);
		return doAwait(thePhase);
	}

	/**
	 * Finish a phase, and deregister so that later stages can complete
	 * with a reduced participant count.
	 */
	public E arriveAndDeregister() {
		return phaseToEnum(myPhaser.arriveAndDeregister());
	}

	/**
	 * Add a new participant to the pool.
	 * Later await calls will wait for one more arrival before proceeding.
	 */
	public E register() {
		return phaseToEnum(myPhaser.register());
	}

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

	/**
	 * Defensively verify that the phase we are waiting to end is actually the current phase.
	 */
	private void checkAwait(E thePhase) {
		E currentPhase = getPhase();
		if (currentPhase.ordinal() < thePhase.ordinal()) {
			// Explicitly progressing lock-step is safer for most tests.
			// But we could allow waiting multiple phases with a loop here instead of failing. MB
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
