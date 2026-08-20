/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class TerminologyXmlUtil {

	/**
	 * Non-instantiable
	 */
	private TerminologyXmlUtil() {
		// nothing
	}

	/**
	 * This method is mostly a wrapper around {@link XmlUtil#parseDocument(Reader, boolean, boolean)}
	 * but it catches parse and IO exceptions and wraps them in a {@link JobExecutionFailedException},
	 * which will cause an instant job failure.
	 */
	public static Element parseXmlDocument(AttachmentDetails theAttachment, String theSourceFilename) {
		InputStreamReader reader = new InputStreamReader(theAttachment.getInputStream(), StandardCharsets.UTF_8);
		Document document;
		try {
			document = XmlUtil.parseDocument(reader, false, true);
		} catch (SAXException | IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2967) + "Failed to parse file " + theSourceFilename + ": " + e.getMessage(), e);
		}

		return document.getDocumentElement();
	}
}
