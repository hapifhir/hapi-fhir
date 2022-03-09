package ca.uhn.fhir.batch2.api;

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

import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * This interface can be used to validate the parameters
 * object supplied to start a job instance.
 * <p>
 * Batch2 automatically uses
 * <a href="https://www.baeldung.com/javax-validation">JSR 380</a>
 * to validate the parameters object supplied to job start requests.
 * <p>
 * However not all validation is possible using that API. For example
 * environment-specific rules, or rules about relationships between
 * multiple parameters.
 *
 * @see ca.uhn.fhir.batch2.model.JobDefinition.Builder#setParametersValidator(IJobParametersValidator)
 */
public interface IJobParametersValidator<T extends IModelJson> {

	/**
	 * Validate the given job parameters.
	 *
	 * @param theParameters The parameters object to validate
	 * @return Any strings returned by this method are treated as validation failures and returned to the client initiating the job. Return <code>null</code> or an empty list to indicate that no validation failures occurred.
	 */
	@Nullable
	List<String> validate(@Nonnull T theParameters);

}
