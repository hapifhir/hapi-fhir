/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.merge;

import ca.uhn.fhir.i18n.Msg;

/**
 * The type of change a merge made to a set of resources, recorded on each member merge Provenance and used to
 * order undo restores (undeletes, then updates, then deletes).
 */
// Created by Claude Opus 4.8
public enum MergeChangeType {
	CREATE("create", 3),
	UPDATE("update", 2),
	DELETE("delete", 1);

	private final String myCode;
	private final int myUndoOrder;

	MergeChangeType(String theCode, int theUndoOrder) {
		myCode = theCode;
		myUndoOrder = theUndoOrder;
	}

	public String getCode() {
		return myCode;
	}

	public int getUndoOrder() {
		return myUndoOrder;
	}

	public static MergeChangeType fromCode(String theCode) {
		for (MergeChangeType changeType : values()) {
			if (changeType.myCode.equals(theCode)) {
				return changeType;
			}
		}
		throw new IllegalArgumentException(Msg.code(3012) + "Invalid change type code '" + theCode + "'");
	}
}
