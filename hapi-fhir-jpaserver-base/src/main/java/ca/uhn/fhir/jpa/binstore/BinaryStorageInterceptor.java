package ca.uhn.fhir.jpa.binstore;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Interceptor
public class BinaryStorageInterceptor {

	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;
	@Autowired
	private FhirContext myCtx;

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeResource(AtomicInteger theCounter, IBaseResource theResource) {

		Class<? extends IBase> binaryType = myCtx.getElementDefinition("base64Binary").getImplementingClass();
		List<? extends IBase> binaryElements = myCtx.newTerser().getAllPopulatedChildElementsOfType(theResource, binaryType);

		List<String> attachmentIds = binaryElements
			.stream()
			.flatMap(t -> ((IBaseHasExtensions) t).getExtension().stream())
			.filter(t -> JpaConstants.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
			.map(t -> ((IPrimitiveType) t.getValue()).getValueAsString())
			.collect(Collectors.toList());

		for (String next : attachmentIds) {
			myBinaryStorageSvc.expungeBlob(theResource.getIdElement(), next);
			theCounter.incrementAndGet();
		}

	}

}
