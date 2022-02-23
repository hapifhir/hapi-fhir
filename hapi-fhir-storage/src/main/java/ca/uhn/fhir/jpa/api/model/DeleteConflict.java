package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.primitive.IdDt;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DeleteConflict {

	private final IdDt mySourceId;
	private final String mySourcePath;
	private final IdDt myTargetId;

	public DeleteConflict(IdDt theSourceId, String theSourcePath, IdDt theTargetId) {
		mySourceId = theSourceId;
		mySourcePath = theSourcePath;
		myTargetId = theTargetId;
	}

	public IdDt getSourceId() {
		return mySourceId;
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public IdDt getTargetId() {
		return myTargetId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("sourceId", mySourceId)
			.append("sourcePath", mySourcePath)
			.append("targetId", myTargetId)
			.toString();
	}

}
