package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
import ca.uhn.fhir.util.Logs;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Status of a Batch2 Job Instance.
 */
public enum StatusEnum {

	/**
	 * Task is waiting to execute and should begin with no intervention required.
	 */
	QUEUED(true, false, true),

	/**
	 * Task is current executing
	 */
	IN_PROGRESS(true, false, true),

	/**
	 * For reduction steps
	 */
	FINALIZE(true, false, true),

	/**
	 * Task completed successfully
	 */
	COMPLETED(false, true, false),

	/**
	 * Task execution resulted in an error but the error may be transient (or transient status is unknown).
	 * Retrying may result in success.
	 */
	ERRORED(true, false, true),

	/**
	 * Task has failed and is known to be unrecoverable. There is no reason to believe that retrying will
	 * result in a different outcome.
	 */
	FAILED(true, true, false),

	/**
	 * Task has been cancelled.
	 */
	CANCELLED(true, true, false);

	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	/** Map from state to Set of legal inbound states */
	static final Map<StatusEnum, Set<StatusEnum>> ourFromStates;
	/** Map from state to Set of legal outbound states */
	static final Map<StatusEnum, Set<StatusEnum>> ourToStates;

	static {
		EnumMap<StatusEnum, Set<StatusEnum>> fromStates = new EnumMap<>(StatusEnum.class);
		EnumMap<StatusEnum, Set<StatusEnum>> toStates = new EnumMap<>(StatusEnum.class);

		for (StatusEnum nextEnum: StatusEnum.values()) {
			fromStates.put(nextEnum, EnumSet.noneOf(StatusEnum.class));
			toStates.put(nextEnum, EnumSet.noneOf(StatusEnum.class));
		}
		for (StatusEnum nextPriorEnum: StatusEnum.values()) {
			for (StatusEnum nextNextEnum: StatusEnum.values()) {
				if (isLegalStateTransition(nextPriorEnum, nextNextEnum)) {
					fromStates.get(nextNextEnum).add(nextPriorEnum);
					toStates.get(nextPriorEnum).add(nextNextEnum);
				}
			}
		}

		ourFromStates = Maps.immutableEnumMap(fromStates);
		ourToStates = Maps.immutableEnumMap(toStates);
	}

	private final boolean myIncomplete;
	private final boolean myEnded;
	private final boolean myIsCancellable;
	private static StatusEnum[] ourIncompleteStatuses;
	private static Set<StatusEnum> ourEndedStatuses;
	private static Set<StatusEnum> ourNotEndedStatuses;

	StatusEnum(boolean theIncomplete, boolean theEnded, boolean theIsCancellable) {
		myIncomplete = theIncomplete;
		myEnded = theEnded;
		myIsCancellable = theIsCancellable;
	}

	/**
	 * Statuses that represent a job that has not yet completed. I.e.
	 * all statuses except {@link #COMPLETED}
	 */
	public static StatusEnum[] getIncompleteStatuses() {
		StatusEnum[] retVal = ourIncompleteStatuses;
		if (retVal == null) {
			EnumSet<StatusEnum> incompleteSet = EnumSet.noneOf(StatusEnum.class);
			for (StatusEnum next : values()) {
				if (next.myIncomplete) {
					incompleteSet.add(next);
				}
			}
			ourIncompleteStatuses = incompleteSet.toArray(new StatusEnum[0]);
			retVal = ourIncompleteStatuses;
		}
		return retVal;
	}

	/**
	 * Statuses that represent a job that has ended. I.e.
	 * all statuses except {@link #QUEUED and #COMPLETED}
	 */
	@Nonnull
	public static Set<StatusEnum> getEndedStatuses() {
		Set<StatusEnum> retVal = ourEndedStatuses;
		if (retVal == null) {
			initializeStaticEndedStatuses();
		}
		retVal = ourEndedStatuses;
		return retVal;
	}

	/**
	 * Statuses that represent a job that has not ended. I.e.
	 * {@link #QUEUED and #COMPLETED}
	 */
	@Nonnull
	public static Set<StatusEnum> getNotEndedStatuses() {
		Set<StatusEnum> retVal = ourNotEndedStatuses;
		if (retVal == null) {
			initializeStaticEndedStatuses();
		}
		retVal = ourNotEndedStatuses;
		return retVal;
	}

	private static void initializeStaticEndedStatuses() {
		EnumSet<StatusEnum> endedSet = EnumSet.noneOf(StatusEnum.class);
		EnumSet<StatusEnum> notEndedSet = EnumSet.noneOf(StatusEnum.class);
		for (StatusEnum next : values()) {
			if (next.myEnded) {
				endedSet.add(next);
			} else {
				notEndedSet.add(next);
			}
		}
		ourEndedStatuses = Collections.unmodifiableSet(endedSet);
		ourNotEndedStatuses = Collections.unmodifiableSet(notEndedSet);
	}

	public static boolean isLegalStateTransition(StatusEnum theOrigStatus, StatusEnum theNewStatus) {
		boolean canTransition;
		switch (theOrigStatus) {
			case QUEUED:
				// initial state can transition to anything
				canTransition = true;
				break;
			case IN_PROGRESS:
				canTransition = theNewStatus != QUEUED;
				break;
			case ERRORED:
				canTransition = theNewStatus == FAILED || theNewStatus == COMPLETED || theNewStatus == CANCELLED || theNewStatus == ERRORED;
				break;
			case CANCELLED:
				// terminal state cannot transition
				canTransition =  false;
				break;
			case COMPLETED:
				canTransition =  false;
				break;
			case FAILED:
				canTransition = theNewStatus == FAILED;
				break;
			case FINALIZE:
				canTransition = theNewStatus != QUEUED && theNewStatus != IN_PROGRESS;
				break;
			default:
				throw new IllegalStateException(Msg.code(2131) + "Unknown batch state " + theOrigStatus);
		}

		if (!canTransition) {
			ourLog.trace("Tried to execute an illegal state transition. [origStatus={}, newStatus={}]", theOrigStatus, theNewStatus);
		}
		return canTransition;
	}

	public boolean isIncomplete() {
		return myIncomplete;
	}

	public boolean isEnded() {
		return myEnded;
	}

	public boolean isCancellable() {
		return myIsCancellable;
	}

	/**
	 * States that may transition to this state.
	 */
	public Set<StatusEnum> getPriorStates() {
		return ourFromStates.get(this);
	}

	/**
	 * States this state may transotion to.
	 */
	public Set<StatusEnum> getNextStates() {
		return ourToStates.get(this);
	}
}
