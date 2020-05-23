package ca.uhn.fhir.jpa.api.dao;

/*
 * #%L
 * HAPI FHIR JPA API
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Map;

/**
 * Note that this interface is not considered a stable interface. While it is possible to build applications
 * that use it directly, please be aware that we may modify methods, add methods, or even remove methods from
 * time to time, even within minor point releases.
 *
 * @param <T>  The bundle type
 * @param <MT> The Meta datatype type
 */
public interface IFhirSystemDao<T, MT> extends IDao {

	ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails);

	Map<String, Long> getResourceCounts();

	/**
	 * Returns a cached count of resources using a cache that regularly
	 * refreshes in the background. This method will never
	 */
	@Nullable
	Map<String, Long> getResourceCountsFromCache();


	IBundleProvider history(Date theDate, Date theUntil, RequestDetails theRequestDetails);

	/**
	 * Not supported for DSTU1
	 *
	 * @param theRequestDetails TODO
	 */
	MT metaGetOperation(RequestDetails theRequestDetails);

	/**
	 * Implementations may implement this method to implement the $process-message
	 * operation
	 */
	IBaseBundle processMessage(RequestDetails theRequestDetails, IBaseBundle theMessage);

	T transaction(RequestDetails theRequestDetails, T theResources);

}
