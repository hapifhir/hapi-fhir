package ca.uhn.test.concurrency;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
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
			throw new IllegalStateException(Msg.code(2280) + "Enum " + myEnumClass.getName() + " should declare one more enum value for post-completion reporting of phase " + resultOrdinal);
		}
		return myEnumConstants[resultOrdinal];
	}


}
