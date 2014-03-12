package ca.uhn.fhir.tinder;

import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Profile.ExtensionDefn;
import ca.uhn.fhir.model.dstu.resource.Profile.Structure;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElement;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElementDefinition;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureElementDefinitionType;
import ca.uhn.fhir.model.dstu.resource.Profile.StructureSearchParam;
import ca.uhn.fhir.model.dstu.valueset.DataTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.Child;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.ResourceBlock;
import ca.uhn.fhir.tinder.model.SearchParameter;
import ca.uhn.fhir.tinder.model.Slicing;

public class ProfileParser extends BaseStructureParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ProfileParser.class);

	public static void main(String[] args) throws Exception {

		// FhirContext fhirContext = new FhirContext(Profile.class);
		// XmlParser parser = fhirContext.newXmlParser();
		//
		// String file = IOUtils.toString(new FileReader("src/test/resources/prof/organization.xml"));
		// Profile text = (Profile) parser.parseResource(file);
		//
		// ValueSetGenerator vsp = new ValueSetGenerator();
		// vsp.setDirectory("src/test/resources/vs/");
		// vsp.parse();
		//
		// ProfileParser p = new ProfileParser();
		// p.parseSingleProfile(text, "http://fhir.connectinggta.ca/static/Profile/organization.xml");
		// p.bindValueSets(vsp);
		// p.writeAll("target/generated/valuesets/ca/uhn/fhir/model/dstu/resource");
		//
		// vsp.writeMarkedValueSets("target/generated/valuesets/ca/uhn/fhir/model/dstu/valueset");
	}

	public void parseSingleProfile(Profile theProfile, String theUrlTOThisProfile) throws Exception {
		for (Structure nextStructure : theProfile.getStructure()) {
			
			int elemIdx = 0;
			Map<String, BaseElement> elements = new HashMap<String, BaseElement>();
			for (StructureElement next : nextStructure.getElement()) {

				BaseElement elem;
				if (elemIdx == 0) {
					Resource resource = new Resource();
					resource.setProfile(theProfile.getIdentifier().getValue());
					if (resource.getProfile() == null) {
						resource.setProfile(theUrlTOThisProfile);
					}
					
					for (StructureSearchParam nextParam : nextStructure.getSearchParam()) {
						SearchParameter param = new SearchParameter();
						param.setName(nextParam.getName().getValue());
						param.setPath(nextParam.getXpath().getValue());
						param.setType(nextParam.getType().getValue());
						param.setDescription(nextParam.getDocumentation().getValue());
						resource.getSearchParameters().add(param);
					}
					
					addResource(resource);
					elem = resource;
					// below StringUtils.isBlank(type) || type.startsWith("=")
				} else if (next.getDefinition().getType().isEmpty()) {
					elem = new ResourceBlock();
					// } else if (type.startsWith("@")) {
					// elem = new ResourceBlockCopy();
					// } else if (type.equals("*")) {
					// elem = new AnyChild();
				} else {
					elem = new Child();
				}

				elem.setName(next.getPath().getValue());
				elem.setElementNameAndDeriveParentElementName(next.getPath().getValue());

				boolean allResourceReferences = next.getDefinition().getType().size() > 0;
				for (StructureElementDefinitionType nextType : next.getDefinition().getType()) {
					if (nextType.getCode().getValueAsEnum() != DataTypeEnum.RESOURCEREFERENCE) {
						allResourceReferences = false;
					}
				}
				elem.setResourceRef(allResourceReferences);

				StructureElementDefinition definition = next.getDefinition();

				BaseElement parentElement = elements.get(elem.getElementParentName());
				Slicing childIsSliced = parentElement != null ? parentElement.getChildElementNameToSlicing().get(elem.getName()) : null;

				if (next.getSlicing().getDiscriminator().getValue() != null) {
					Slicing slicing = new Slicing();
					slicing.setDiscriminator(next.getSlicing().getDiscriminator().getValue());
					if (parentElement.getChildElementNameToSlicing().get(elem.getName()) != null) {
						throw new ConfigurationException("Found multiple slicing definitions for path: " + next.getPath().getValue());
					}
					parentElement.getChildElementNameToSlicing().put(elem.getName(), slicing);
					continue;
				}

				/*
				 * Profiles come with a number of standard elements which are generally ignored because they are boilerplate, unless the definition is somehow changing their behaviour (e.g. through
				 * slices)
				 */
				if (next.getPath().getValue().endsWith(".contained")) {
					continue;
				}
				if (next.getPath().getValue().endsWith(".text")) {
					continue;
				}
				if (next.getPath().getValue().endsWith(".extension")) {
					if (childIsSliced != null) {
						if (!"url".equals(childIsSliced.getDiscriminator())) {
							throw new ConfigurationException("Extensions must be sliced on 'url' discriminator. Found: " + next.getSlicing().getDiscriminator().getValue());
						}
						if (next.getDefinition().getType().size() != 1 || next.getDefinition().getType().get(0).getCode().getValueAsEnum() != DataTypeEnum.EXTENSION) {
							throw new ConfigurationException("Extension slices must have a single type with a code of 'Extension'");
						}
						String name = next.getName().getValue();
						if (StringUtils.isBlank(name)) {
							throw new ConfigurationException("Extension slices must have a 'name' defined, none found at path: " + next.getPath());
						}
						elem.setName(name);
						elem.setElementName(name);
						String profile = next.getDefinition().getType().get(0).getProfile().getValueAsString();
						if (isBlank(profile)) {
							throw new ConfigurationException("Extension slice for " + next.getPath().getValue() + " has no profile specified in its type");
						}
						if (profile.startsWith("#")) {
							Profile.ExtensionDefn extension = findExtension(theProfile, profile.substring(1));
							if (extension == null) {
								throw new ConfigurationException("Unknown local extension reference: " + profile);
							}
							ourLog.info("Element at path {} is using extension {}", next.getPath(), profile);
							definition = extension.getDefinition();
							String extensionUrl = theUrlTOThisProfile + profile;
							elem.setExtensionUrl(extensionUrl);
						} else {
							// TODO: implement this
							throw new ConfigurationException("Extensions specified outside of the given profile are not yet supported");
						}
					} else {
						continue;
					}
				}
				if (next.getPath().getValue().endsWith(".modifierExtension")) {
					continue;
				}

				for (StructureElementDefinitionType nextType : definition.getType()) {
					if (nextType.getCode().getValueAsEnum() == DataTypeEnum.RESOURCEREFERENCE) {
						if (nextType.getProfile().getValueAsString().startsWith("http://hl7.org/fhir/profiles/")) {
							elem.getType().add(capitalize(nextType.getProfile().getValueAsString().substring("http://hl7.org/fhir/profiles/".length())));
						} else {
							// TODO: implement this.. we need to be able to
							// reference other profiles
							throw new ConfigurationException("Profile type not yet supported");
						}
					} else {
						elem.getType().add(capitalize(nextType.getCode().getValue()) + "Dt");
					}

				}

				elem.setBinding(definition.getBinding().getName().getValue());
				elem.setShortName(definition.getShort().getValue());
				elem.setDefinition(definition.getFormal().getValue());
				elem.setRequirement(definition.getRequirements().getValue());
				elem.setCardMin(definition.getMin().getValueAsString());
				elem.setCardMax(definition.getMax().getValue());

				if (elem instanceof Child) {
					Child child = (Child) elem;
					elements.put(elem.getName(), elem);
					if (parentElement == null) {
						throw new Exception("Can't find element " + elem.getElementParentName() + "  -  Valid values are: " + elements.keySet());
					}
					parentElement.addChild(child);

					/*
					 * Find simple setters
					 */
					scanForSimpleSetters(child);
				} else {
					Resource res = (Resource) elem;
					elements.put(res.getName(), res);
				}

				elemIdx++;
			}

		}
	}

	private ExtensionDefn findExtension(Profile theProfile, String theCode) {
		for (ExtensionDefn next : theProfile.getExtensionDefn()) {
			if (theCode.equals(next.getCode().getValue())) {
				return next;
			}
		}
		return null;
	}

	@Override
	protected String getFilenameSuffix() {
		return "";
	}

	@Override
	protected String getTemplate() {
		return "/vm/resource.vm";
	}

	public void parseBaseResources(List<String> theBaseResourceNames) throws MojoFailureException {
		FhirContext fhirContext = new FhirContext(Profile.class);

		for (String nextFileName : theBaseResourceNames) {
			ourLog.info("Parsing file: {}", nextFileName);

			Profile profile;
			try {
				profile = (Profile) fhirContext.newXmlParser().parseResource(IOUtils.toString(new FileReader(nextFileName)));
			} catch (Exception e) {
				throw new MojoFailureException("Failed to load or parse file: " + nextFileName, e);
			}

			try {
				parseSingleProfile(profile, "");
			} catch (Exception e) {
				throw new MojoFailureException("Failed to process file: " + nextFileName, e);
			}
		}

		// for (int i = 0; i < theBaseResourceNames.size(); i++) {
		// theBaseResourceNames.set(i, theBaseResourceNames.get(i).toLowerCase());
		// }
		//
		// try {
		//
		// Bundle bundle = fhirContext.newXmlParser().parseBundle(IOUtils.toString(getClass().getResourceAsStream("/prof/allprofiles.xml")));
		// TreeSet<String> allProfiles = new TreeSet<String>();
		// for (BundleEntry nextResource : bundle.getEntries() ) {
		// Profile nextProfile = (Profile) nextResource.getResource();
		// allProfiles.add(nextProfile.getName().getValue());
		// if (theBaseResourceNames.contains(nextProfile.getName().getValue().toLowerCase())){
		// parseSingleProfile(nextProfile, bundle.getLinkBase().getValueNotNull());
		// }
		// }
		//
		// ourLog.info("Base profiles found: {}", allProfiles);
		//
		// } catch (Exception e) {
		// throw new MojoFailureException("Failed to load base resources", e);
		// }
	}

}
