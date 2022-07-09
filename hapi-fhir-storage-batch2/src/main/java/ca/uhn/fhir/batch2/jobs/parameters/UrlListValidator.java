package ca.uhn.fhir.batch2.jobs.parameters;

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

import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UrlListValidator {
	private final String myOperationName;
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public UrlListValidator(String theOperationName, IBatch2DaoSvc theBatch2DaoSvc) {
		myOperationName = theOperationName;
		myBatch2DaoSvc = theBatch2DaoSvc;
	}


	@Nullable
	public List<String> validateUrls(@Nonnull List<String> theUrls) {
			if (theUrls.isEmpty()) {
				if (!myBatch2DaoSvc.isAllResourceTypeSupported()) {
					return Collections.singletonList("At least one type-specific search URL must be provided for " + myOperationName + " on this server");
				}
			}
		return Collections.emptyList();
	}

	@Nullable
	public List<String> validatePartitionedUrls(@Nonnull List<PartitionedUrl> thePartitionedUrls) {
		List<String> urls = thePartitionedUrls.stream().map(PartitionedUrl::getUrl).collect(Collectors.toList());
		return validateUrls(urls);
	}
}
