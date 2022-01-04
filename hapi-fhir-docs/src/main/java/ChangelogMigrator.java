/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.i18n.Msg;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.apache.commons.io.FileUtils;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This is just here to force a javadoc to be built in order to keep
 * Maven Central happy
 */
public class ChangelogMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(ChangelogMigrator.class);
	private static final Namespace NS = Namespace.getNamespace("http://maven.apache.org/changes/1.0.0");

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

			ArrayList<Object> items = new ArrayList<>();

			for (Element nextAction : nextRelease.getChildren("action", NS)) {

				HashMap<String, Object> itemRootMap = new HashMap<>();
				items.add(itemRootMap);
				HashMap<Object, Object> itemMap = new HashMap<>();
				itemRootMap.put("item", itemMap);

				String type = nextAction.getAttribute("type").getValue();
				switch (type) {
					case "change":
						itemMap.put("type", "change");
						break;
					case "fix":
						itemMap.put("type", "fix");
						break;
					case "remove":
						itemMap.put("type", "remove");
						break;
					case "add":
						itemMap.put("type", "add");
						break;
					default:
						throw new Error(Msg.code(630) + "Unknown type: " + type);
				}

				String issue = nextAction.getAttribute("issue") != null ? nextAction.getAttribute("issue").getValue() : null;
				if (isNotBlank(issue)) {
					itemMap.put("issue", issue);
				}

				StringBuilder contentBuilder = new StringBuilder();
				for (Content nextContents : nextAction.getContent()) {
					if (nextContents instanceof Text) {
						String text = ((Text) nextContents).getTextNormalize();
						contentBuilder.append(" ").append(text);
					} else {
						throw new IllegalStateException(Msg.code(631) + "Unknown type: " + nextContents.getClass());
					}
				}

				String value = contentBuilder.toString().trim().replaceAll(" {2}", " ");
				itemMap.put("title", value);

				actionCount++;
			}

			String releaseDir = "hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/" + version.replace(".", "_");
			File releaseDirFile = new File(releaseDir);
			FileUtils.forceMkdir(releaseDirFile);
			File file = new File(releaseDirFile, "changes.yaml");
			ourLog.info("Writing file: {}", file.getAbsolutePath());
			try (FileWriter writer = new FileWriter(file, false)) {

				YAMLFactory yf = new YAMLFactory().disable(YAMLGenerator.Feature.SPLIT_LINES);

				ObjectMapper mapper = new ObjectMapper(yf);
				mapper.writeValue(writer, items);

			}

			file = new File(releaseDirFile, "version.yaml");
			ourLog.info("Writing file: {}", file.getAbsolutePath());
			try (FileWriter writer = new FileWriter(file, false)) {

				YAMLFactory yf = new YAMLFactory();
				ObjectMapper mapper = new ObjectMapper(yf);
				HashMap<Object, Object> versionMap = new HashMap<>();
				versionMap.put("release-date", date);
				if (isNotBlank(description)) {
					versionMap.put("codename", description);
				}
				mapper.writeValue(writer, versionMap);

			}

		}

		ourLog.info("Found {} releases and {} actions", releaseCount, actionCount);

	}

}


