package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JobInstanceParameters {

	private final ListMultimap<String, String> myParameters;

	public JobInstanceParameters(ListMultimap<String, String> theParameters) {
		myParameters = Multimaps.unmodifiableListMultimap(theParameters);
	}

	public List<String> getValues(@Nonnull String theParamName) {
		Validate.notBlank(theParamName);
		return myParameters
			.entries()
			.stream()
			.filter(t -> t.getKey().equals(theParamName))
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.collect(Collectors.toList());
	}

	public Optional<String> getValue(@Nonnull String theParamName) {
		Validate.notBlank(theParamName);
		return myParameters
			.entries()
			.stream()
			.filter(t -> t.getKey().equals(theParamName))
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.findFirst();
	}

	public Optional<Integer> getValueInteger(@Nonnull String theParamName) {
		return myParameters
			.entries()
			.stream()
			.filter(t -> t.getKey().equals(theParamName))
			.filter(t -> isNotBlank(t.getValue()))
			.map(t -> parseIntegerParam(t.getKey(), t.getValue()))
			.findFirst();
	}

	/**
	 * Return the total number of parameters. Note that if two parameters have the same parameter
	 * name, they are counted as two for the purposes of this method.
	 */
	public int size() {
		return myParameters.size();
	}

	private static Integer parseIntegerParam(String theParameterName, String theParameterValue) {
		try {
			return Integer.parseInt(theParameterValue);
		} catch (NumberFormatException e) {
			throw new JobExecutionFailedException("Invalid parameter value for parameter " + theParameterName + ". Expected integer.");
		}
	}


	public static JobInstanceParameters.Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private final ListMultimap<String, String> myValues = ArrayListMultimap.create();

		public Builder withEntry(String theKey, String theValue) {
			Validate.notBlank(theKey);
			Validate.notBlank(theKey);
			myValues.put(theKey, theValue);
			return this;
		}

		public JobInstanceParameters build() {
			return new JobInstanceParameters(myValues);
		}

	}
}
