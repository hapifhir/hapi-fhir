package ca.uhn.fhir.parser;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.RDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.system.StreamRDF;
import org.hl7.fhir.instance.model.api.*;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is the FHIR RDF parser/encoder. Users should not interact with this class directly, but should use
 * {@link FhirContext#newRDFParser()} to get an instance.
 */
public class RDFParser extends BaseParser {

	private static final String FHIR_NS = "http://hl7.org/fhir";
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RDFParser.class);

	private FhirContext context;
	private Lang lang;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the RDF parser is to invoke
	 * {@link FhirContext#newRDFParser()}.
	 *
	 * @param parserErrorHandler the Parser Error Handler
	 */
	public RDFParser(final FhirContext context, final IParserErrorHandler parserErrorHandler, final Lang lang) {
		super(context, parserErrorHandler);
		this.context = context;
		this.lang = lang;
	}

	@Override
	protected void doEncodeResourceToWriter(final IBaseResource resource,
														 final Writer writer,
														 final EncodeContext encodeContext) {

		StreamRDF eventWriter = RDFUtil.createRDFWriter(writer, this.lang);
		encodeResourceToRDFStreamWriter(resource, eventWriter, encodeContext);
	}

	@Override
	protected <T extends IBaseResource> T doParseResource(final Class<T> resourceType,
																			final Reader reader) throws DataFormatException {

		StreamRDF streamReader = createStreamReader(reader);
		return parseResource(resourceType, streamReader);
	}

	@Override
	public EncodingEnum getEncoding() {
		return EncodingEnum.RDF;
	}

	@Override
	public IParser setPrettyPrint(final boolean prettyPrint) {
		return this;
	}


	private StreamRDF createStreamReader(Reader reader) {
		return RDFUtil.createRDFReader(reader, this.lang);
	}

	private void encodeResourceToRDFStreamWriter(final IBaseResource resource,
																final StreamRDF streamWriter,
																final boolean containedResource,
																final IIdType resourceId,
																final EncodeContext encodeContext) {
		RuntimeResourceDefinition resDef = this.context.getResourceDefinition(resource);
		if (resDef == null) {
			throw new ConfigurationException("Unknown resource type: " + resource.getClass());
		}

		if (!containedResource) {
			super.containResourcesForEncoding(resource);
		}

		streamWriter.start();
		streamWriter.base(FHIR_NS);

		Model model = ModelFactory.createDefaultModel();

		if (resource instanceof IAnyResource) {
			// HL7.org Structures
			if (resourceId != null) {
				writeCommentsPre(streamWriter, resourceId);

				Resource element = model.createResource(resourceId.getBaseUrl());
				Property property = model.createProperty("value", resourceId.getIdPart());
				element.addProperty(property, resourceId.getIdPart());

				streamWriter.start();
				streamWriter.prefix("value", resourceId.getIdPart());
				streamWriter.finish();
				writeCommentsPost(streamWriter, resourceId);
			}

			encodeCompositeElementToStreamWriter(resource, resource, streamWriter, containedResource, new CompositeChildElement(resDef, encodeContext), encodeContext);

		} else {

			// DSTU2+
			if (resourceId != null) {
				streamWriter.start();
				streamWriter.prefix("value", resourceId.getIdPart());
				encodeExtensionsIfPresent(resource, streamWriter, resourceId, false, encodeContext);
				streamWriter.finish();
				writeCommentsPost(streamWriter, resourceId);
			}
			/*
			InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			IdDt idDt = resource.getId();
			String versionIdPart = idDt.getVersionIdPart();
			if (isBlank(versionIdPart)) {
				versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
			}
			List<BaseCodingDt> securityLabels = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.SECURITY_LABELS);
			List<? extends IIdType> profiles = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.PROFILES);
			profiles = super.getProfileTagsForEncoding(resource, profiles);

			TagList tags = getMetaTagsForEncoding((resource), encodeContext);

			if (!ElementUtil.isEmpty(versionIdPart, updated, securityLabels, tags, profiles)) {
				streamWriter.start();

				for (IIdType profile : profiles) {
					streamWriter.start();
					streamWriter.prefix("value", profile.getValue());
					streamWriter.finish();
				}
				for (BaseCodingDt securityLabel : securityLabels) {
					streamWriter.start();
					encodeCompositeElementToStreamWriter(resource, securityLabel, streamWriter, containedResource, null, encodeContext);
					streamWriter.finish();
				}
				if (tags != null) {
					for (Tag tag : tags) {
						if (tag.isEmpty()) {
							continue;
						}
						streamWriter.start();
						streamWriter.prefix("system", tag.getScheme());
						streamWriter.prefix("code", tag.getTerm());
						streamWriter.prefix("display", tag.getLabel());
						streamWriter.finish();
					}
				}
				streamWriter.finish();
			}
			*/
			if (resource instanceof IBaseBinary) {
				IBaseBinary bin = (IBaseBinary) resource;
				streamWriter.prefix("contentType", bin.getContentType());
				streamWriter.prefix("content", bin.getContentAsBase64());
			} else {
				encodeCompositeElementToStreamWriter(resource, resource, streamWriter, containedResource, new CompositeChildElement(resDef, encodeContext), encodeContext);
			}

		}

		streamWriter.finish();
	}

	private void writeCommentsPre(final StreamRDF eventWriter, final IBase element) {
		if (element != null && element.hasFormatComment()) {
			for (String next : element.getFormatCommentsPre()) {
				if (isNotBlank(next)) {
					eventWriter.base(next);
				}
			}
		}
	}

	private void writeCommentsPost(final StreamRDF eventWriter, final IBase element) {
		if (element != null && element.hasFormatComment()) {
			for (String next : element.getFormatCommentsPost()) {
				if (isNotBlank(next)) {
					eventWriter.base(next);
				}
			}
		}
	}

	private void encodeChildElementToStreamWriter(final IBaseResource resource,
																 final StreamRDF eventWriter,
																 final BaseRuntimeChildDefinition childDefinition,
																 final IBase element,
																 final String childName,
																 final BaseRuntimeElementDefinition<?> childDef,
																 final String extensionUrl,
																 final boolean includedResource,
																 final CompositeChildElement parent,
																 final EncodeContext encodeContext) {

		String childGenericName = childDefinition.getElementName();

		encodeContext.pushPath(childGenericName, false);
		try {

			if (element == null || element.isEmpty()) {
				if (!isChildContained(childDef, includedResource)) {
					return;
				}
			}

			writeCommentsPre(eventWriter, element);

			switch (childDef.getChildType()) {
				case ID_DATATYPE: {
					IIdType value = (IIdType) element;
					assert value != null;
					String encodedValue = "id".equals(childName) ? value.getIdPart() : value.getValue();
					if (StringUtils.isNotBlank(encodedValue) || !hasNoExtensions(value)) {
						eventWriter.start();
						if (StringUtils.isNotBlank(encodedValue)) {
							eventWriter.prefix("value", encodedValue);
						}
						encodeExtensionsIfPresent(resource, eventWriter, element, includedResource, encodeContext);
						eventWriter.finish();
					}
					break;
				}
				case PRIMITIVE_DATATYPE: {
					IPrimitiveType<?> pd = (IPrimitiveType) element;
					assert pd != null;
					String value = pd.getValueAsString();
					if (value != null || !hasNoExtensions(pd)) {
						eventWriter.start();
						String elementId = getCompositeElementId(element);
						if (isNotBlank(elementId)) {
							eventWriter.prefix("id", elementId);
						}
						if (value != null) {
							eventWriter.prefix("value", value);
						}
						encodeExtensionsIfPresent(resource, eventWriter, element, includedResource, encodeContext);
						eventWriter.finish();
					}
					break;
				}
				case RESOURCE_BLOCK:
				case COMPOSITE_DATATYPE: {
					eventWriter.start();
					String elementId = getCompositeElementId(element);
					if (isNotBlank(elementId)) {
						eventWriter.prefix("id", elementId);
					}
					if (isNotBlank(extensionUrl)) {
						eventWriter.prefix("url", extensionUrl);
					}
					encodeCompositeElementToStreamWriter(resource, element, eventWriter, includedResource, parent, encodeContext);
					eventWriter.finish();
					break;
				}
				case CONTAINED_RESOURCE_LIST:
				case CONTAINED_RESOURCES: {
					/*
					 * Disable per #103 for (IResource next : value.getContainedResources()) { if (getContainedResources().getResourceId(next) != null) { continue; }
					 * theEventWriter.writeStartElement("contained"); encodeResourceToRDFStreamWriter(next, theEventWriter, true, fixContainedResourceId(next.getId().getValue()));
					 * theEventWriter.writeEndElement(); }
					 */
					for (IBaseResource next : getContainedResources().getContainedResources()) {
						IIdType resourceId = getContainedResources().getResourceId(next);
						eventWriter.start();
						encodeResourceToRDFStreamWriter(next, eventWriter, true, fixContainedResourceId(resourceId.getValue()), encodeContext);
						eventWriter.finish();
					}
					break;
				}
				case RESOURCE: {
					IBaseResource baseResource = (IBaseResource) element;
					String resourceName = this.context.getResourceDefinition(baseResource).getName();
					if (!super.shouldEncodeResource(resourceName)) {
						break;
					}
					eventWriter.start();
					encodeContext.pushPath(resourceName, true);
					encodeResourceToRDFStreamWriter(resource, eventWriter, encodeContext);
					encodeContext.popPath();
					eventWriter.finish();
					break;
				}
				case EXTENSION_DECLARED:
				case UNDECL_EXT: {
					throw new IllegalStateException("state should not happen: " + childDef.getName());
				}
			}

			writeCommentsPost(eventWriter, element);

		} finally {
			encodeContext.popPath();
		}

	}

	private void encodeResourceToRDFStreamWriter(final IBaseResource resource,
																final StreamRDF eventWriter,
																final EncodeContext encodeContext) {
		IIdType resourceId = null;

		if (StringUtils.isNotBlank(resource.getIdElement().getIdPart())) {
			resourceId = resource.getIdElement();
			if (resource.getIdElement().getValue().startsWith("urn:")) {
				resourceId = null;
			}
		}

		if (!super.shouldEncodeResourceId(resource, encodeContext)) {
			resourceId = null;
		} else if (encodeContext.getResourcePath().size() == 1 && getEncodeForceResourceId() != null) {
			resourceId = getEncodeForceResourceId();
		}

		encodeResourceToRDFStreamWriter(resource, eventWriter, false, resourceId, encodeContext);
	}

	private void encodeUndeclaredExtensions(final IBaseResource resource,
														 final StreamRDF eventWriter,
														 final List<? extends IBaseExtension<?, ?>> extensions,
														 final boolean includedResource,
														 final EncodeContext encodeContext) {
		for (IBaseExtension<?, ?> next : extensions) {
			if (next == null || (ElementUtil.isEmpty(next.getValue()) && next.getExtension().isEmpty())) {
				continue;
			}

			writeCommentsPre(eventWriter, next);

			eventWriter.start();

			String elementId = getCompositeElementId(next);
			if (isNotBlank(elementId)) {
				eventWriter.prefix("id", elementId);
			}

			String url = getExtensionUrl(next.getUrl());
			eventWriter.prefix("url", url);

			if (next.getValue() != null) {
				IBaseDatatype value = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = this.context.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				BaseRuntimeElementDefinition<?> childDef;
				if (childName == null) {
					childDef = this.context.getElementDefinition(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException("Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
					childName = RuntimeChildUndeclaredExtensionDefinition.createExtensionChildName(childDef);
				} else {
					childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException("Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
				}
				encodeChildElementToStreamWriter(resource, eventWriter, extDef, value, childName,
					childDef, null, includedResource, null, encodeContext);
			}

			// child extensions
			encodeExtensionsIfPresent(resource, eventWriter, next, includedResource, encodeContext);

			eventWriter.finish();

			writeCommentsPost(eventWriter, next);

		}
	}

	private void encodeExtensionsIfPresent(final IBaseResource resource,
														final StreamRDF writer,
														final IBase element,
														final boolean includedResource,
														final EncodeContext encodeContext) {
		if (element instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) element;
			encodeUndeclaredExtensions(resource, writer, toBaseExtensionList(res.getUndeclaredExtensions()), includedResource, encodeContext);
			encodeUndeclaredExtensions(resource, writer, toBaseExtensionList(res.getUndeclaredModifierExtensions()), includedResource, encodeContext);
		}
		if (element instanceof IBaseHasExtensions) {
			IBaseHasExtensions res = (IBaseHasExtensions) element;
			encodeUndeclaredExtensions(resource, writer, res.getExtension(), includedResource, encodeContext);
		}
		if (element instanceof IBaseHasModifierExtensions) {
			IBaseHasModifierExtensions res = (IBaseHasModifierExtensions) element;
			encodeUndeclaredExtensions(resource, writer, res.getModifierExtension(), includedResource, encodeContext);
		}
	}

	private void encodeExtension(final IBaseResource theResource,
										  final StreamRDF theEventWriter,
										  final boolean theContainedResource,
										  final CompositeChildElement nextChildElem,
										  final BaseRuntimeChildDefinition nextChild,
										  final IBase nextValue,
										  final String childName,
										  final String extensionUrl,
										  final BaseRuntimeElementDefinition<?> childDef,
										  final EncodeContext theEncodeContext) {
		BaseRuntimeDeclaredChildDefinition extDef = (BaseRuntimeDeclaredChildDefinition) nextChild;
		if (extDef.isModifier()) {
			theEventWriter.start();
		} else {
			theEventWriter.start();
		}

		String elementId = getCompositeElementId(nextValue);
		if (isNotBlank(elementId)) {
			theEventWriter.prefix("id", elementId);
		}

		theEventWriter.prefix("url", extensionUrl);
		encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, nextValue, childName,
			childDef, null, theContainedResource, nextChildElem, theEncodeContext);
		theEventWriter.finish();
	}

	private void encodeCompositeElementToStreamWriter(final IBaseResource resource,
																	  final IBase element,
																	  final StreamRDF streamRDF,
																	  final boolean containedResource,
																	  final CompositeChildElement parent,
																	  final EncodeContext encodeContext) {

		for (CompositeChildElement nextChildElem : super.compositeChildIterator(element, containedResource, parent, encodeContext)) {

			BaseRuntimeChildDefinition nextChild = nextChildElem.getDef();

			if (nextChild.getElementName().equals("url") && element instanceof IBaseExtension) {
				/*
				 * RDF encoding is a one-off for extensions. The URL element goes in an attribute
				 * instead of being encoded as a normal element, only for RDF encoding
				 */
				continue;
			}

			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				INarrativeGenerator gen = this.context.getNarrativeGenerator();
				INarrative narr;
				if (resource instanceof IResource) {
					narr = ((IResource) resource).getText();
				} else if (resource instanceof IDomainResource) {
					narr = ((IDomainResource) resource).getText();
				} else {
					narr = null;
				}
				assert narr != null;
				if (gen != null && narr.isEmpty()) {
					gen.populateResourceNarrative(this.context, resource);
				}
				if (!narr.isEmpty()) {
					RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
					String childName = nextChild.getChildNameByDatatype(child.getDatatype());
					BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
					encodeChildElementToStreamWriter(resource,
						streamRDF, nextChild, narr, childName, type, null,
						containedResource, nextChildElem, encodeContext);
					continue;
				}
			}

			if (nextChild instanceof RuntimeChildContainedResources) {
				encodeChildElementToStreamWriter(resource, streamRDF, nextChild, null,
					nextChild.getChildNameByDatatype(null),
					nextChild.getChildElementDefinitionByDatatype(null), null,
					containedResource, nextChildElem, encodeContext);
			} else {

				List<? extends IBase> values = nextChild.getAccessor().getValues(element);
				values = super.preProcessValues(nextChild, resource, values, nextChildElem, encodeContext);

				if (values == null || values.isEmpty()) {
					continue;
				}
				for (IBase nextValue : values) {
					if ((nextValue == null || nextValue.isEmpty())) {
						continue;
					}

					ChildNameAndDef childNameAndDef = super.getChildNameAndDef(nextChild, nextValue);
					if (childNameAndDef == null) {
						continue;
					}

					String childName = childNameAndDef.getChildName();
					BaseRuntimeElementDefinition<?> childDef = childNameAndDef.getChildDef();
					String extensionUrl = getExtensionUrl(nextChild.getExtensionUrl());

					if (extensionUrl != null && !childName.equals("extension")) {
						encodeExtension(resource, streamRDF, containedResource, nextChildElem, nextChild,
							nextValue, childName, extensionUrl, childDef, encodeContext);
					} else if (nextChild instanceof RuntimeChildExtension) {
						IBaseExtension<?, ?> extension = (IBaseExtension<?, ?>) nextValue;
						if ((extension.getValue() == null || extension.getValue().isEmpty())) {
							if (extension.getExtension().isEmpty()) {
								continue;
							}
						}
						encodeChildElementToStreamWriter(resource, streamRDF, nextChild, nextValue,
							childName, childDef, getExtensionUrl(extension.getUrl()),
							containedResource, nextChildElem, encodeContext);
					} else if (!(nextChild instanceof RuntimeChildNarrativeDefinition) || !containedResource) {
						encodeChildElementToStreamWriter(resource, streamRDF, nextChild, nextValue,
							childName, childDef, extensionUrl, containedResource, nextChildElem, encodeContext);
					}
				}
			}
		}
	}

	private <Q extends IBaseExtension<?, ?>> List<IBaseExtension<?, ?>> toBaseExtensionList(final List<Q> theList) {
		List<IBaseExtension<?, ?>> retVal = new ArrayList<>(theList.size());
		retVal.addAll(theList);
		return retVal;
	}

	private <T extends IBaseResource> T parseResource(Class<T> resourceType, StreamRDF streamReader) {
		ParserState<T> parserState = ParserState.getPreResourceInstance(this, resourceType, context, false, getErrorHandler());
		return doRDFLoop(streamReader, parserState);
	}


	private <T> T doRDFLoop(StreamRDF streamReader, ParserState<T> parserState) {
		logger.trace("Entering RDF parsing loop with state: {}", parserState);
		return parserState.getObject();
	}
}
