package ca.uhn.fhir.cql.common.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import org.cqframework.cql.cql2elm.FhirLibrarySourceProvider;
import org.hl7.elm.r1.VersionedIdentifier;
import org.opencds.cqf.cql.evaluator.cql2elm.content.LibraryContentType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Function;

public class LibraryContentProvider<LibraryType, AttachmentType>
	implements org.opencds.cqf.cql.evaluator.cql2elm.content.LibraryContentProvider {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LibraryContentProvider.class);

	private FhirLibrarySourceProvider innerProvider;
	private LibraryResolutionProvider<LibraryType> provider;
	private Function<LibraryType, Iterable<AttachmentType>> getAttachments;
	private Function<AttachmentType, String> getContentType;
	private Function<AttachmentType, byte[]> getContent;

	public LibraryContentProvider(LibraryResolutionProvider<LibraryType> provider,
										  Function<LibraryType, Iterable<AttachmentType>> getAttachments,
										  Function<AttachmentType, String> getContentType, Function<AttachmentType, byte[]> getContent) {

		this.innerProvider = new FhirLibrarySourceProvider();

		this.provider = provider;
		this.getAttachments = getAttachments;
		this.getContentType = getContentType;
		this.getContent = getContent;
	}

	@Override
	public InputStream getLibraryContent(VersionedIdentifier versionedIdentifier, LibraryContentType libraryContentType){

		// TODO: Support loading ELM
		if (libraryContentType != LibraryContentType.CQL) {
			return null;
		}

		try {
			LibraryType lib = this.provider.resolveLibraryByName(versionedIdentifier.getId(),
				versionedIdentifier.getVersion());
			for (AttachmentType attachment : this.getAttachments.apply(lib)) {
				if ("text/cql".equals(this.getContentType.apply(attachment))) {
					return new ByteArrayInputStream(this.getContent.apply(attachment));
				}
			}
		} catch (Exception e) {
			ourLog.warn("Failed to parse Library source for VersionedIdentifier '" + versionedIdentifier + "'!"
				+ System.lineSeparator() + e.getMessage(), e);
		}

		return this.innerProvider.getLibrarySource(versionedIdentifier);
	}
}
