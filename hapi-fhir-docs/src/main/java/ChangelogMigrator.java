/*-
 * #%L
 * HAPI FHIR - Docs
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

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.input.DOMBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is just here to force a javadoc to be built in order to keep
 * Maven Central happy
 */
public class ChangelogMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(ChangelogMigrator.class);
	private static final Namespace NS = Namespace.getNamespace( "http://maven.apache.org/changes/1.0.0");

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

		org.jdom2.Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//If want to make namespace aware.
		//factory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		org.w3c.dom.Document w3cDocument = documentBuilder.parse(new File("src/changes/changes.xml"));
		document = new DOMBuilder().build(w3cDocument);

		int actionCount = 0;
		int releaseCount = 0;


		Element docElement = document.getRootElement();
		Element bodyElement = docElement.getChild("body", NS);
		List<Element> releases = bodyElement.getChildren("release", NS);
		for (Element nextRelease : releases) {
			String version = nextRelease.getAttributeValue("version");
			String date = nextRelease.getAttributeValue("date");
			String description = nextRelease.getAttributeValue("description");
			ourLog.info("Found release {} - {} - {}", version, date, description);
			releaseCount++;

			for (Element nextAction : nextRelease.getChildren("action", NS)) {
				StringBuilder contentBuilder = new StringBuilder();
				for (Content nextContents : nextAction.getContent()) {
					if (nextContents instanceof Text) {
						String text = ((Text) nextContents).getTextNormalize();
						contentBuilder.append(text);
					} else {
						throw new IllegalStateException("Unknown type: " + nextContents.getClass());
					}
				}

				actionCount++;
			}

		}

		ourLog.info("Found {} releases and {} actions", releaseCount, actionCount);

	}

}


