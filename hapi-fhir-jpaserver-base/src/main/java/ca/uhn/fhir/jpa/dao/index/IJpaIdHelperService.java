package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * This class is an analog to {@link IIdHelperService} but with additional JPA server methods
 * added.
 *
 * JA 2022-02-17 - I moved these methods out of IdHelperService because I want to reuse
 * IdHelperService in storage-engine-neutral batch tasks such as bulk import. These methods
 * are all just being used by MDM, so they're JPA specific. I believe it should be possible
 * though to just replace all of these calls with equivalents from IdHelperService,
 * at which point this interface and its implementation could just go away.
 *
 * All of the methods here aren't partition aware, so it's not great to use them
 * anyhow. The equivalents in {@link IIdHelperService} are probably a bit more
 * clunky because you have to convert between {@link Long} and
 * {@link ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId} to use them,
 * but they also have caching and partition awareness so the tradeoff for that
 * extra effort is that they are better.
 */
public interface IJpaIdHelperService extends IIdHelperService {

	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nonnull
	List<Long> getPidsOrThrowException(List<IIdType> theIds);


	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nullable
	Long getPidOrNull(IBaseResource theResource);


	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nonnull
	Long getPidOrThrowException(IIdType theId);

	@Nonnull
	Long getPidOrThrowException(@Nonnull IAnyResource theResource);

	IIdType resourceIdFromPidOrThrowException(Long thePid);

	/**
	 * Given a set of PIDs, return a set of public FHIR Resource IDs.
	 * This function will resolve a forced ID if it resolves, and if it fails to resolve to a forced it, will just return the pid
	 * Example:
	 * Let's say we have Patient/1(pid == 1), Patient/pat1 (pid == 2), Patient/3 (pid == 3), their pids would resolve as follows:
	 * <p>
	 * [1,2,3] -> ["1","pat1","3"]
	 *
	 * @param thePids The Set of pids you would like to resolve to external FHIR Resource IDs.
	 * @return A Set of strings representing the FHIR IDs of the pids.
	 */
	Set<String> translatePidsToFhirResourceIds(Set<Long> thePids);

}
