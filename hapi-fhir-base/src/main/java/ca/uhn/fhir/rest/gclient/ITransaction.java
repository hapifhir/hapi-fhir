package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ITransaction {

	/**
	 * Use a list of resources as the transaction input
	 */
	ITransactionTyped<List<IBaseResource>> withResources(List<? extends IBaseResource> theResources);
	
	/**
	 * Use the given Bundle resource as the transaction input
	 */
	<T extends IBaseBundle> ITransactionTyped<T> withBundle(T theBundleResource);

	/**
	 * Use the given raw text (should be a Bundle resource) as the transaction input
	 */
	ITransactionTyped<String> withBundle(String theBundle);

}
