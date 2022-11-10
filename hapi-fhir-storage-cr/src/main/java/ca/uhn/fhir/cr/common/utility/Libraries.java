package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Libraries {

	private static final Map<FhirVersionEnum, LibraryFunctions> cachedFunctions = new ConcurrentHashMap<>();
	private static final String LIBRARY_RESOURCE_TYPE = "Library";

	private Libraries() {
	}

	static byte[] getContent(IBaseResource library, LibraryFunctions libraryFunctions, String contentType) {
		for (IBase attachment : libraryFunctions.getAttachments().apply(library)) {
			String libraryContentType = libraryFunctions.getContentType().apply(attachment);
			if (libraryContentType != null && libraryContentType.equals(contentType)) {
				byte[] content = libraryFunctions.getContent().apply(attachment);
				if (content != null) {
					return content;
				}
			}
		}

		return null;
	}

	public static byte[] getContent(IBaseResource library, String contentType) {
		checkNotNull(library);
		checkArgument(library.fhirType().equals(LIBRARY_RESOURCE_TYPE));
		checkNotNull(contentType);

		LibraryFunctions libraryFunctions = getFunctions(library);
		return getContent(library, libraryFunctions, contentType);
	}

	static LibraryFunctions getFunctions(IBaseResource library) {
		FhirVersionEnum fhirVersion = library.getStructureFhirVersionEnum();
		return cachedFunctions.computeIfAbsent(fhirVersion, Libraries::getFunctions);
	}

	static LibraryFunctions getFunctions(FhirVersionEnum fhirVersionEnum) {
		FhirContext fhirContext = FhirContext.forCached(fhirVersionEnum);

		Class<? extends IBaseResource> libraryClass = fhirContext.getResourceDefinition(LIBRARY_RESOURCE_TYPE)
				.getImplementingClass();
		Function<IBase, List<IBase>> attachments = Reflections
				.getFunction(libraryClass, "content");
		Function<IBase, String> contentType = Reflections.getPrimitiveFunction(
				fhirContext.getElementDefinition("Attachment").getImplementingClass(), "contentType");
		Function<IBase, byte[]> content = Reflections
				.getPrimitiveFunction(fhirContext.getElementDefinition("Attachment").getImplementingClass(), "data");
		Function<IBase, String> version = Reflections.getVersionFunction(libraryClass);
		return new LibraryFunctions(attachments, contentType, content, version);
	}

	public static String getVersion(IBaseResource library) {
		checkNotNull(library);
		checkArgument(library.fhirType().equals(LIBRARY_RESOURCE_TYPE));

		LibraryFunctions libraryFunctions = getFunctions(library);
		return libraryFunctions.getVersion().apply(library);
	}


	public static final class LibraryFunctions {

		private final Function<IBase, List<IBase>> getAttachments;
		private final Function<IBase, String> getContentType;
		private final Function<IBase, byte[]> getContent;
		private final Function<IBase, String> getVersion;

		LibraryFunctions(Function<IBase, List<IBase>> getAttachments, Function<IBase, String> getContentType, Function<IBase, byte[]> getContent, Function<IBase, String> getVersion) {
			this.getAttachments = getAttachments;
			this.getContentType = getContentType;
			this.getContent = getContent;
			this.getVersion = getVersion;
		}

		public Function<IBase, List<IBase>> getAttachments() {
			return this.getAttachments;
		}

		public Function<IBase, String> getContentType() {
			return this.getContentType;
		}

		public Function<IBase, byte[]> getContent() {
			return this.getContent;
		}

		public Function<IBase, String> getVersion() {
			return this.getVersion;
		}
	}
}
