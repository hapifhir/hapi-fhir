/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
package ca.uhn.fhir.jpa.migrate.taskdef;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;

// LUKETODO: javadoc
public class ExecuteTaskPrecondition {
	private final Supplier<Boolean> myPreconditionRunner;
	private final String myPreconditionReason;

	public ExecuteTaskPrecondition(Supplier<Boolean> thePreconditionRunner, String thePreconditionReason) {
		myPreconditionRunner = thePreconditionRunner;
		myPreconditionReason = thePreconditionReason;
	}

	public Supplier<Boolean> getPreconditionRunner() {
		return myPreconditionRunner;
	}

	public String getPreconditionReason() {
		return myPreconditionReason;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		ExecuteTaskPrecondition that = (ExecuteTaskPrecondition) theO;
		return Objects.equals(myPreconditionRunner, that.myPreconditionRunner)
				&& Objects.equals(myPreconditionReason, that.myPreconditionReason);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myPreconditionRunner, myPreconditionReason);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ExecuteTaskPrecondition.class.getSimpleName() + "[", "]")
				.add("myPreconditionRunner=" + myPreconditionRunner)
				.add("myPreconditionReason='" + myPreconditionReason + "'")
				.toString();
	}
}
