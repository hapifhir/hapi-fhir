package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.search.spatial.impl.Point;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public abstract class BaseSearchParamExtractor implements ISearchParamExtractor {

	private static final Pattern SPLIT = Pattern.compile("\\||( or )");
	private static final Pattern SPLIT_R4 = Pattern.compile("\\|");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	@Autowired
	protected ApplicationContext myApplicationContext;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private PartitionSettings myPartitionSettings;

	private Set<String> myIgnoredForSearchDatatypes;
	private BaseRuntimeChildDefinition myQuantityValueValueChild;
	private BaseRuntimeChildDefinition myQuantitySystemValueChild;
	private BaseRuntimeChildDefinition myQuantityCodeValueChild;
	private BaseRuntimeChildDefinition myMoneyValueChild;
	private BaseRuntimeChildDefinition myMoneyCurrencyChild;
	private BaseRuntimeElementCompositeDefinition<?> myLocationPositionDefinition;
	private BaseRuntimeChildDefinition myCodeSystemUrlValueChild;
	private BaseRuntimeChildDefinition myRangeLowValueChild;
	private BaseRuntimeChildDefinition myRangeHighValueChild;
	private BaseRuntimeChildDefinition myAddressLineValueChild;
	private BaseRuntimeChildDefinition myAddressCityValueChild;
	private BaseRuntimeChildDefinition myAddressStateValueChild;
	private BaseRuntimeChildDefinition myAddressCountryValueChild;
	private BaseRuntimeChildDefinition myAddressPostalCodeValueChild;
	private BaseRuntimeChildDefinition myCapabilityStatementRestSecurityServiceValueChild;
	private BaseRuntimeChildDefinition myPeriodStartValueChild;
	private BaseRuntimeChildDefinition myPeriodEndValueChild;
	private BaseRuntimeChildDefinition myTimingEventValueChild;
	private BaseRuntimeChildDefinition myTimingRepeatValueChild;
	private BaseRuntimeChildDefinition myTimingRepeatBoundsValueChild;
	private BaseRuntimeChildDefinition myDurationSystemValueChild;
	private BaseRuntimeChildDefinition myDurationCodeValueChild;
	private BaseRuntimeChildDefinition myDurationValueValueChild;
	private BaseRuntimeChildDefinition myHumanNameFamilyValueChild;
	private BaseRuntimeChildDefinition myHumanNameGivenValueChild;
	private BaseRuntimeChildDefinition myContactPointValueValueChild;
	private BaseRuntimeChildDefinition myIdentifierSystemValueChild;
	private BaseRuntimeChildDefinition myIdentifierValueValueChild;
	private BaseRuntimeChildDefinition myIdentifierTypeValueChild;
	private BaseRuntimeChildDefinition myIdentifierTypeTextValueChild;
	private BaseRuntimeChildDefinition myCodeableConceptCodingValueChild;
	private BaseRuntimeChildDefinition myCodeableConceptTextValueChild;
	private BaseRuntimeChildDefinition myCodingSystemValueChild;
	private BaseRuntimeChildDefinition myCodingCodeValueChild;
	private BaseRuntimeChildDefinition myCodingDisplayValueChild;
	private BaseRuntimeChildDefinition myContactPointSystemValueChild;
	private BaseRuntimeChildDefinition myPatientCommunicationLanguageValueChild;

	/**
	 * Constructor
	 */
	BaseSearchParamExtractor() {
		super();
	}

	/**
	 * UNIT TEST constructor
	 */
	BaseSearchParamExtractor(ModelConfig theModelConfig, PartitionSettings thePartitionSettings, FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		Validate.notNull(theModelConfig);
		Validate.notNull(theCtx);
		Validate.notNull(theSearchParamRegistry);

		myModelConfig = theModelConfig;
		myContext = theCtx;
		mySearchParamRegistry = theSearchParamRegistry;
		myPartitionSettings = thePartitionSettings;
	}

	@Override
	public SearchParamSet<PathAndRef> extractResourceLinks(IBaseResource theResource) {
		IExtractor<PathAndRef> extractor = createReferenceExtractor();
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.REFERENCE);
	}

	private IExtractor<PathAndRef> createReferenceExtractor() {
		return new ResourceLinkExtractor();
	}

	private class ResourceLinkExtractor implements IExtractor<PathAndRef> {

		private PathAndRef myPathAndRef = null;

		@Override
		public void extract(SearchParamSet<PathAndRef> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath) {
			if (theValue instanceof IBaseResource) {
				return;
			}

			String nextType = toRootTypeName(theValue);
			switch (nextType) {
				case "uri":
				case "canonical":
					String typeName = toTypeName(theValue);
					IPrimitiveType<?> valuePrimitive = (IPrimitiveType<?>) theValue;
					IBaseReference fakeReference = (IBaseReference) myContext.getElementDefinition("Reference").newInstance();
					fakeReference.setReference(valuePrimitive.getValueAsString());

					// Canonical has a root type of "uri"
					if ("canonical".equals(typeName)) {

						/*
						 * See #1583
						 * Technically canonical fields should not allow local references (e.g.
						 * Questionnaire/123) but it seems reasonable for us to interpret a canonical
						 * containing a local reference for what it is, and allow people to search
						 * based on that.
						 */
						IIdType parsed = fakeReference.getReferenceElement();
						if (parsed.hasIdPart() && parsed.hasResourceType() && !parsed.isAbsolute()) {
							myPathAndRef = new PathAndRef(theSearchParam.getName(), thePath, fakeReference, false);
							theParams.add(myPathAndRef);
							break;
						}

						if (parsed.isAbsolute()) {
							myPathAndRef = new PathAndRef(theSearchParam.getName(), thePath, fakeReference, true);
							theParams.add(myPathAndRef);
							break;
						}
					}

					theParams.addWarning("Ignoring canonical reference (indexing canonical is not yet supported)");
					break;
				case "reference":
				case "Reference":
					IBaseReference valueRef = (IBaseReference) theValue;

					IIdType nextId = valueRef.getReferenceElement();
					if (nextId.isEmpty() && valueRef.getResource() != null) {
						nextId = valueRef.getResource().getIdElement();
					}

					if (nextId == null ||
						nextId.isEmpty() ||
						nextId.getValue().startsWith("#") ||
						nextId.getValue().startsWith("urn:")) {
						return;
					}

					myPathAndRef = new PathAndRef(theSearchParam.getName(), thePath, valueRef, false);
					theParams.add(myPathAndRef);
					break;
				default:
					addUnexpectedDatatypeWarning(theParams, theSearchParam, theValue);
					break;
			}
		}

		public PathAndRef get(IBase theValue, String thePath) {
			extract(new SearchParamSet<>(),
				new RuntimeSearchParam(null, null, "Reference", null, null, null, null, null, null, null),
				theValue, thePath);
			return myPathAndRef;
		}
	}

	@Override
	public PathAndRef extractReferenceLinkFromResource(IBase theValue, String thePath) {
		ResourceLinkExtractor extractor = new ResourceLinkExtractor();
		return extractor.get(theValue, thePath);
	}

	@Override
	public List<String> extractParamValuesAsStrings(RuntimeSearchParam theSearchParam, IBaseResource theResource) {
		IExtractor extractor;
		switch (theSearchParam.getParamType()) {
			case DATE:
				extractor = createDateExtractor(theResource);
				break;
			case STRING:
				extractor = createStringExtractor(theResource);
				break;
			case TOKEN:
				extractor = createTokenExtractor(theResource);
				break;
			case NUMBER:
				extractor = createNumberExtractor(theResource);
				break;
			case REFERENCE:
				extractor = createReferenceExtractor();
				return extractReferenceParamsAsQueryTokens(theSearchParam, theResource, extractor);
			case QUANTITY:
				extractor = createQuantityExtractor(theResource);
				break;
			case URI:
				extractor = createUriExtractor(theResource);
				break;
			case SPECIAL:
				extractor = createSpecialExtractor(theResource.getIdElement().getResourceType());
				break;
			case COMPOSITE:
			default:
				throw new UnsupportedOperationException("Type " + theSearchParam.getParamType() + " not supported for extraction");
		}
		return extractParamsAsQueryTokens(theSearchParam, theResource, extractor);
	}

	private List<String> extractReferenceParamsAsQueryTokens(RuntimeSearchParam theSearchParam, IBaseResource theResource, IExtractor<PathAndRef> theExtractor) {
		SearchParamSet<PathAndRef> params = new SearchParamSet<>();
		extractSearchParam(theSearchParam, theResource, theExtractor, params);
		return refsToStringList(params);
	}

	private List<String> refsToStringList(SearchParamSet<PathAndRef> theParams) {
		return theParams.stream()
			.map(PathAndRef::getRef)
			.map(ref -> ref.getReferenceElement().toUnqualifiedVersionless().getValue())
			.collect(Collectors.toList());
	}

	private <T extends BaseResourceIndexedSearchParam> List<String> extractParamsAsQueryTokens(RuntimeSearchParam theSearchParam, IBaseResource theResource, IExtractor<T> theExtractor) {
		SearchParamSet<T> params = new SearchParamSet<>();
		extractSearchParam(theSearchParam, theResource, theExtractor, params);
		return toStringList(params);
	}

	private <T extends BaseResourceIndexedSearchParam> List<String> toStringList(SearchParamSet<T> theParams) {
		return theParams.stream()
			.map(param -> param.toQueryParameterType().getValueAsQueryToken(myContext))
			.collect(Collectors.toList());
	}

	@Override
	public SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		IExtractor<BaseResourceIndexedSearchParam> extractor = createTokenExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.TOKEN);
	}

	@Override
	public SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, RuntimeSearchParam theSearchParam) {
		IExtractor<BaseResourceIndexedSearchParam> extractor = createTokenExtractor(theResource);
		SearchParamSet<BaseResourceIndexedSearchParam> setToPopulate = new SearchParamSet<>();
		extractSearchParam(theSearchParam, theResource, extractor, setToPopulate);
		return setToPopulate;
	}

	private IExtractor<BaseResourceIndexedSearchParam> createTokenExtractor(IBaseResource theResource) {
		String resourceTypeName = toRootTypeName(theResource);
		String useSystem;
		if (getContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			if (resourceTypeName.equals("ValueSet")) {
				ca.uhn.fhir.model.dstu2.resource.ValueSet dstu2ValueSet = (ca.uhn.fhir.model.dstu2.resource.ValueSet) theResource;
				useSystem = dstu2ValueSet.getCodeSystem().getSystem();
			} else {
				useSystem = null;
			}
		} else {
			if (resourceTypeName.equals("CodeSystem")) {
				useSystem = extractValueAsString(myCodeSystemUrlValueChild, theResource);
			} else {
				useSystem = null;
			}
		}

		return new TokenExtractor(resourceTypeName, useSystem);
	}

	@Override
	public SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource) {
		String resourceTypeName = toRootTypeName(theResource);
		IExtractor<BaseResourceIndexedSearchParam> extractor = createSpecialExtractor(resourceTypeName);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.SPECIAL);
	}

	private IExtractor<BaseResourceIndexedSearchParam> createSpecialExtractor(String theResourceTypeName) {
		return (params, searchParam, value, path) -> {
			if ("Location.position".equals(path)) {
				addCoords_Position(theResourceTypeName, params, searchParam, value);
			}
		};
	}


	private void addUnexpectedDatatypeWarning(SearchParamSet<?> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		theParams.addWarning("Search param " + theSearchParam.getName() + " is of unexpected datatype: " + theValue.getClass());
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamUri> extractor = createUriExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.URI);
	}

	private IExtractor<ResourceIndexedSearchParamUri> createUriExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path) -> {
			String nextType = toRootTypeName(value);
			String resourceType = toRootTypeName(theResource);
			switch (nextType) {
				case "uri":
				case "url":
				case "oid":
				case "sid":
				case "uuid":
					addUri_Uri(resourceType, params, searchParam, value);
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamDate> extractor = createDateExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.DATE);
	}

	private IExtractor<ResourceIndexedSearchParamDate> createDateExtractor(IBaseResource theResource) {
		return new DateExtractor(theResource);
	}

	private class DateExtractor implements IExtractor<ResourceIndexedSearchParamDate> {

		String myResourceType;
		ResourceIndexedSearchParamDate myIndexedSearchParamDate = null;

		public DateExtractor(IBaseResource theResource) {
			myResourceType = toRootTypeName(theResource);
		}

		public DateExtractor(String theResourceType) {
			myResourceType = theResourceType;
		}

		@Override
		public void extract(SearchParamSet theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath) {
			String nextType = toRootTypeName(theValue);
			switch (nextType) {
				case "date":
				case "dateTime":
				case "instant":
					addDateTimeTypes(myResourceType, theParams, theSearchParam, theValue);
					break;
				case "Period":
					addDate_Period(myResourceType, theParams, theSearchParam, theValue);
					break;
				case "Timing":
					addDate_Timing(myResourceType, theParams, theSearchParam, theValue);
					break;
				case "string":
					// CarePlan.activitydate can be a string - ignored for now
					break;
				default:
					addUnexpectedDatatypeWarning(theParams, theSearchParam, theValue);
					break;

			}
		}

		private void addDate_Period(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
			Date start = extractValueAsDate(myPeriodStartValueChild, theValue);
			String startAsString = extractValueAsString(myPeriodStartValueChild, theValue);
			Date end = extractValueAsDate(myPeriodEndValueChild, theValue);
			String endAsString = extractValueAsString(myPeriodEndValueChild, theValue);

			if (start != null || end != null) {

				if (start == null) {
					start = myModelConfig.getPeriodIndexStartOfTime().getValue();
					startAsString = myModelConfig.getPeriodIndexStartOfTime().getValueAsString();
				}
				if (end == null) {
					end = myModelConfig.getPeriodIndexEndOfTime().getValue();
					endAsString = myModelConfig.getPeriodIndexEndOfTime().getValueAsString();
				}

				myIndexedSearchParamDate = new ResourceIndexedSearchParamDate(myPartitionSettings, theResourceType, theSearchParam.getName(), start, startAsString, end, endAsString, startAsString);
				theParams.add(myIndexedSearchParamDate);
			}
		}

		private void addDate_Timing(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
			List<IPrimitiveType<Date>> values = extractValuesAsFhirDates(myTimingEventValueChild, theValue);

			TreeSet<Date> dates = new TreeSet<>();
			String firstValue = null;
			String finalValue = null;
			for (IPrimitiveType<Date> nextEvent : values) {
				if (nextEvent.getValue() != null) {
					dates.add(nextEvent.getValue());
					if (firstValue == null) {
						firstValue = nextEvent.getValueAsString();
					}
					finalValue = nextEvent.getValueAsString();
				}
			}

			Optional<IBase> repeat = myTimingRepeatValueChild.getAccessor().getFirstValueOrNull(theValue);
			if (repeat.isPresent()) {
				Optional<IBase> bounds = myTimingRepeatBoundsValueChild.getAccessor().getFirstValueOrNull(repeat.get());
				if (bounds.isPresent()) {
					String boundsType = toRootTypeName(bounds.get());
					if ("Period".equals(boundsType)) {
						Date start = extractValueAsDate(myPeriodStartValueChild, bounds.get());
						Date end = extractValueAsDate(myPeriodEndValueChild, bounds.get());
						dates.add(start);
						dates.add(end);
					}
				}
			}

			if (!dates.isEmpty()) {
				myIndexedSearchParamDate = new ResourceIndexedSearchParamDate(myPartitionSettings, theResourceType, theSearchParam.getName(), dates.first(), firstValue, dates.last(), finalValue, firstValue);
				theParams.add(myIndexedSearchParamDate);
			}
		}

		@SuppressWarnings("unchecked")
		private void addDateTimeTypes(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
			IPrimitiveType<Date> nextBaseDateTime = (IPrimitiveType<Date>) theValue;
			if (nextBaseDateTime.getValue() != null) {
				myIndexedSearchParamDate = new ResourceIndexedSearchParamDate(myPartitionSettings, theResourceType, theSearchParam.getName(), nextBaseDateTime.getValue(), nextBaseDateTime.getValueAsString(), nextBaseDateTime.getValue(), nextBaseDateTime.getValueAsString(), nextBaseDateTime.getValueAsString());
				theParams.add(myIndexedSearchParamDate);
			}
		}

		public ResourceIndexedSearchParamDate get(IBase theValue, String thePath) {
			extract(new SearchParamSet<>(),
				new RuntimeSearchParam(null, null, "date", null, null, null, null, null, null, null),
				theValue, thePath);
			return myIndexedSearchParamDate;
		}
	}

	@Override
	public Date extractDateFromResource(IBase theValue, String thePath) {
		DateExtractor extractor = new DateExtractor("DateType");
		return extractor.get(theValue, thePath).getValueHigh();
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamNumber> extractor = createNumberExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.NUMBER);
	}

	private IExtractor<ResourceIndexedSearchParamNumber> createNumberExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path) -> {
			String nextType = toRootTypeName(value);
			String resourceType = toRootTypeName(theResource);
			switch (nextType) {
				case "Duration":
					addNumber_Duration(resourceType, params, searchParam, value);
					break;
				case "Quantity":
					addNumber_Quantity(resourceType, params, searchParam, value);
					break;
				case "integer":
				case "positiveInt":
				case "unsignedInt":
					addNumber_Integer(resourceType, params, searchParam, value);
					break;
				case "decimal":
					addNumber_Decimal(resourceType, params, searchParam, value);
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamQuantity> extractor = createQuantityExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.QUANTITY);
	}

	private IExtractor<ResourceIndexedSearchParamQuantity> createQuantityExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path) -> {
			if (value.getClass().equals(myLocationPositionDefinition.getImplementingClass())) {
				return;
			}

			String nextType = toRootTypeName(value);
			String resourceType = toRootTypeName(theResource);
			switch (nextType) {
				case "Quantity":
					addQuantity_Quantity(resourceType, params, searchParam, value);
					break;
				case "Money":
					addQuantity_Money(resourceType, params, searchParam, value);
					break;
				case "Range":
					addQuantity_Range(resourceType, params, searchParam, value);
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamString> extractor = createStringExtractor(theResource);

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.STRING);
	}

	private IExtractor<ResourceIndexedSearchParamString> createStringExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path) -> {
			String resourceType = toRootTypeName(theResource);

			if (value instanceof IPrimitiveType) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) value;
				String valueAsString = nextValue.getValueAsString();
				createStringIndexIfNotBlank(resourceType, params, searchParam, valueAsString);
				return;
			}

			String nextType = toRootTypeName(value);
			switch (nextType) {
				case "HumanName":
					addString_HumanName(resourceType, params, searchParam, value);
					break;
				case "Address":
					addString_Address(resourceType, params, searchParam, value);
					break;
				case "ContactPoint":
					addString_ContactPoint(resourceType, params, searchParam, value);
					break;
				case "Quantity":
					addString_Quantity(resourceType, params, searchParam, value);
					break;
				case "Range":
					addString_Range(resourceType, params, searchParam, value);
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};
	}

	/**
	 * Override parent because we're using FHIRPath here
	 */
	@Override
	public List<IBase> extractValues(String thePaths, IBaseResource theResource) {
		List<IBase> values = new ArrayList<>();
		if (isNotBlank(thePaths)) {
			String[] nextPathsSplit = split(thePaths);
			for (String nextPath : nextPathsSplit) {
				List<? extends IBase> allValues;

				// This path is hard to parse and isn't likely to produce anything useful anyway
				if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
					if (nextPath.equals("Bundle.entry.resource(0)")) {
						continue;
					}
				}

				nextPath = trim(nextPath);
				IValueExtractor allValuesFunc = getPathValueExtractor(theResource, nextPath);
				try {
					allValues = allValuesFunc.get();
				} catch (Exception e) {
					String msg = getContext().getLocalizer().getMessage(BaseSearchParamExtractor.class, "failedToExtractPaths", nextPath, e.toString());
					throw new InternalErrorException(msg, e);
				}

				values.addAll(allValues);
			}

			for (int i = 0; i < values.size(); i++) {
				IBase nextObject = values.get(i);
				if (nextObject instanceof IBaseExtension) {
					IBaseExtension nextExtension = (IBaseExtension) nextObject;
					nextObject = nextExtension.getValue();
					values.set(i, nextObject);
				}
			}
		}

		return values;
	}

	protected abstract IValueExtractor getPathValueExtractor(IBaseResource theResource, String theSinglePath);

	protected FhirContext getContext() {
		return myContext;
	}

	protected ModelConfig getModelConfig() {
		return myModelConfig;
	}

	private Collection<RuntimeSearchParam> getSearchParams(IBaseResource theResource) {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		Collection<RuntimeSearchParam> retVal = mySearchParamRegistry.getActiveSearchParams(def.getName()).values();
		List<RuntimeSearchParam> defaultList = Collections.emptyList();
		retVal = ObjectUtils.defaultIfNull(retVal, defaultList);
		return retVal;
	}


	private void addQuantity_Quantity(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IPrimitiveType<BigDecimal>> valueField = myQuantityValueValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();
			String system = extractValueAsString(myQuantitySystemValueChild, theValue);
			String code = extractValueAsString(myQuantityCodeValueChild, theValue);

			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(myPartitionSettings, theResourceType, theSearchParam.getName(), nextValueValue, system, code);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Money(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IPrimitiveType<BigDecimal>> valueField = myMoneyValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();

			String nextValueString = "urn:iso:std:iso:4217";
			String nextValueCode = extractValueAsString(myMoneyCurrencyChild, theValue);
			String searchParamName = theSearchParam.getName();
			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(myPartitionSettings, theResourceType, searchParamName, nextValueValue, nextValueString, nextValueCode);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Range(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IBase> low = myRangeLowValueChild.getAccessor().getFirstValueOrNull(theValue);
		low.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));

		Optional<IBase> high = myRangeHighValueChild.getAccessor().getFirstValueOrNull(theValue);
		high.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));
	}

	private void addToken_Identifier(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		String system = extractValueAsString(myIdentifierSystemValueChild, theValue);
		String value = extractValueAsString(myIdentifierValueValueChild, theValue);
		if (isNotBlank(value)) {
			createTokenIndexIfNotBlank(theResourceType, theParams, theSearchParam, system, value);
		}

		if (shouldIndexTextComponentOfToken(theSearchParam)) {
			Optional<IBase> type = myIdentifierTypeValueChild.getAccessor().getFirstValueOrNull(theValue);
			if (type.isPresent()) {
				String text = extractValueAsString(myIdentifierTypeTextValueChild, type.get());
				createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
			}
		}

	}

	protected boolean shouldIndexTextComponentOfToken(RuntimeSearchParam theSearchParam) {
		return tokenTextIndexingEnabledForSearchParam(myModelConfig, theSearchParam);
	}

	private void addToken_CodeableConcept(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<IBase> codings = getCodingsFromCodeableConcept(theValue);
		for (IBase nextCoding : codings) {
			addToken_Coding(theResourceType, theParams, theSearchParam, nextCoding);
		}

		if (shouldIndexTextComponentOfToken(theSearchParam)) {
			String text = getDisplayTextFromCodeableConcept(theValue);
			if (isNotBlank(text)) {
				createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
			}
		}
	}

	@Override
	public List<IBase> getCodingsFromCodeableConcept(IBase theValue) {
		String nextType = BaseSearchParamExtractor.this.toRootTypeName(theValue);
		if ("CodeableConcept".equals(nextType)) {
			return myCodeableConceptCodingValueChild.getAccessor().getValues(theValue);
		} else {
			return null;
		}
	}

	@Override
	public String getDisplayTextFromCodeableConcept(IBase theValue) {
		String nextType = BaseSearchParamExtractor.this.toRootTypeName(theValue);
		if ("CodeableConcept".equals(nextType)) {
			return extractValueAsString(myCodeableConceptTextValueChild, theValue);
		} else {
			return null;
		}
	}

	private void addToken_Coding(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		ResourceIndexedSearchParamToken resourceIndexedSearchParamToken = createSearchParamForCoding(theResourceType, theSearchParam, theValue);
		if (resourceIndexedSearchParamToken != null) {
			theParams.add(resourceIndexedSearchParamToken);
		}

		if (shouldIndexTextComponentOfToken(theSearchParam)) {
			String text = getDisplayTextForCoding(theValue);
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
		}
	}

	@Override
	public ResourceIndexedSearchParamToken createSearchParamForCoding(String theResourceType, RuntimeSearchParam theSearchParam, IBase theValue) {
		String nextType = BaseSearchParamExtractor.this.toRootTypeName(theValue);
		if ("Coding".equals(nextType)) {
			String system = extractValueAsString(myCodingSystemValueChild, theValue);
			String code = extractValueAsString(myCodingCodeValueChild, theValue);
			return createTokenIndexIfNotBlank(theResourceType, theSearchParam, system, code);
		} else {
			return null;
		}
	}

	@Override
	public String getDisplayTextForCoding(IBase theValue) {
		String nextType = BaseSearchParamExtractor.this.toRootTypeName(theValue);
		if ("Coding".equals(nextType)) {
			return extractValueAsString(myCodingDisplayValueChild, theValue);
		} else {
			return null;
		}
	}

	private void addToken_ContactPoint(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		String system = extractValueAsString(myContactPointSystemValueChild, theValue);
		String value = extractValueAsString(myContactPointValueValueChild, theValue);
		createTokenIndexIfNotBlank(theResourceType, theParams, theSearchParam, system, value);
	}

	private void addToken_PatientCommunication(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<IBase> values = myPatientCommunicationLanguageValueChild.getAccessor().getValues(theValue);
		for (IBase next : values) {
			addToken_CodeableConcept(theResourceType, theParams, theSearchParam, next);
		}
	}

	private void addToken_CapabilityStatementRestSecurity(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<IBase> values = myCapabilityStatementRestSecurityServiceValueChild.getAccessor().getValues(theValue);
		for (IBase nextValue : values) {
			addToken_CodeableConcept(theResourceType, theParams, theSearchParam, nextValue);
		}
	}

	private void addDate_Period(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		Date start = extractValueAsDate(myPeriodStartValueChild, theValue);
		String startAsString = extractValueAsString(myPeriodStartValueChild, theValue);
		Date end = extractValueAsDate(myPeriodEndValueChild, theValue);
		String endAsString = extractValueAsString(myPeriodEndValueChild, theValue);

		if (start != null || end != null) {

			if (start == null) {
				start = myModelConfig.getPeriodIndexStartOfTime().getValue();
				startAsString = myModelConfig.getPeriodIndexStartOfTime().getValueAsString();
			}
			if (end == null) {
				end = myModelConfig.getPeriodIndexEndOfTime().getValue();
				endAsString = myModelConfig.getPeriodIndexEndOfTime().getValueAsString();
			}

			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(myPartitionSettings, theResourceType, theSearchParam.getName(), start, startAsString, end, endAsString, startAsString);
			theParams.add(nextEntity);
		}
	}

	private void addDate_Timing(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<IPrimitiveType<Date>> values = extractValuesAsFhirDates(myTimingEventValueChild, theValue);

		TreeSet<Date> dates = new TreeSet<>();
		TreeSet<String> dateStrings = new TreeSet<>();
		String firstValue = null;
		String finalValue = null;
		for (IPrimitiveType<Date> nextEvent : values) {
			if (nextEvent.getValue() != null) {
				dates.add(nextEvent.getValue());
				if (firstValue == null) {
					firstValue = nextEvent.getValueAsString();
				}
				finalValue = nextEvent.getValueAsString();
			}
		}

		Optional<IBase> repeat = myTimingRepeatValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (repeat.isPresent()) {
			Optional<IBase> bounds = myTimingRepeatBoundsValueChild.getAccessor().getFirstValueOrNull(repeat.get());
			if (bounds.isPresent()) {
				String boundsType = toRootTypeName(bounds.get());
				if ("Period".equals(boundsType)) {
					Date start = extractValueAsDate(myPeriodStartValueChild, bounds.get());
					Date end = extractValueAsDate(myPeriodEndValueChild, bounds.get());
					String endString = extractValueAsString(myPeriodEndValueChild, bounds.get());
					dates.add(start);
					dates.add(end);
					//TODO Check if this logic is valid. Does the start of the first period indicate a lower bound??
					if (firstValue == null) {
						firstValue = extractValueAsString(myPeriodStartValueChild, bounds.get());
					}
					finalValue = endString;
				}
			}
		}

		if (!dates.isEmpty()) {
			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(myPartitionSettings, theResourceType, theSearchParam.getName(), dates.first(), firstValue, dates.last(), finalValue, firstValue);
			theParams.add(nextEntity);
		}
	}

	private void addNumber_Duration(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		String system = extractValueAsString(myDurationSystemValueChild, theValue);
		String code = extractValueAsString(myDurationCodeValueChild, theValue);
		BigDecimal value = extractValueAsBigDecimal(myDurationValueValueChild, theValue);
		if (value != null) {
			value = normalizeQuantityContainingTimeUnitsIntoDaysForNumberParam(system, code, value);
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(myPartitionSettings, theResourceType, theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	private void addNumber_Quantity(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BigDecimal value = extractValueAsBigDecimal(myQuantityValueValueChild, theValue);
		if (value != null) {
			String system = extractValueAsString(myQuantitySystemValueChild, theValue);
			String code = extractValueAsString(myQuantityCodeValueChild, theValue);
			value = normalizeQuantityContainingTimeUnitsIntoDaysForNumberParam(system, code, value);
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(myPartitionSettings, theResourceType, theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	@SuppressWarnings("unchecked")
	private void addNumber_Integer(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<Integer> value = (IPrimitiveType<Integer>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = new BigDecimal(value.getValue());
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(myPartitionSettings, theResourceType, theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	@SuppressWarnings("unchecked")
	private void addNumber_Decimal(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<BigDecimal> value = (IPrimitiveType<BigDecimal>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = value.getValue();
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(myPartitionSettings, theResourceType, theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	private void addCoords_Position(String theResourceType, SearchParamSet<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BigDecimal latitude = null;
		BigDecimal longitude = null;

		if (theValue instanceof org.hl7.fhir.dstu3.model.Location.LocationPositionComponent) {
			org.hl7.fhir.dstu3.model.Location.LocationPositionComponent value = (org.hl7.fhir.dstu3.model.Location.LocationPositionComponent) theValue;
			latitude = value.getLatitude();
			longitude = value.getLongitude();
		} else if (theValue instanceof org.hl7.fhir.r4.model.Location.LocationPositionComponent) {
			org.hl7.fhir.r4.model.Location.LocationPositionComponent value = (org.hl7.fhir.r4.model.Location.LocationPositionComponent) theValue;
			latitude = value.getLatitude();
			longitude = value.getLongitude();
		} else if (theValue instanceof org.hl7.fhir.r5.model.Location.LocationPositionComponent) {
			org.hl7.fhir.r5.model.Location.LocationPositionComponent value = (org.hl7.fhir.r5.model.Location.LocationPositionComponent) theValue;
			latitude = value.getLatitude();
			longitude = value.getLongitude();
		}
		// We only accept coordinates when both are present
		if (latitude != null && longitude != null) {
			double normalizedLatitude = Point.normalizeLatitude(latitude.doubleValue());
			double normalizedLongitude = Point.normalizeLongitude(longitude.doubleValue());
			ResourceIndexedSearchParamCoords nextEntity = new ResourceIndexedSearchParamCoords(myPartitionSettings, theResourceType, theSearchParam.getName(), normalizedLatitude, normalizedLongitude);
			theParams.add(nextEntity);
		}
	}

	private void addString_HumanName(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<String> families = extractValuesAsStrings(myHumanNameFamilyValueChild, theValue);
		for (String next : families) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, next);
		}
		List<String> givens = extractValuesAsStrings(myHumanNameGivenValueChild, theValue);
		for (String next : givens) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, next);
		}
	}

	private void addString_Quantity(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BigDecimal value = extractValueAsBigDecimal(myQuantityValueValueChild, theValue);
		if (value != null) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_Range(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		BigDecimal value = extractValueAsBigDecimal(myRangeLowValueChild, theValue);
		if (value != null) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_ContactPoint(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		String value = extractValueAsString(myContactPointValueValueChild, theValue);
		if (isNotBlank(value)) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value);
		}
	}

	private void addString_Address(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		List<String> allNames = new ArrayList<>(extractValuesAsStrings(myAddressLineValueChild, theValue));

		String city = extractValueAsString(myAddressCityValueChild, theValue);
		if (isNotBlank(city)) {
			allNames.add(city);
		}

		String state = extractValueAsString(myAddressStateValueChild, theValue);
		if (isNotBlank(state)) {
			allNames.add(state);
		}

		String country = extractValueAsString(myAddressCountryValueChild, theValue);
		if (isNotBlank(country)) {
			allNames.add(country);
		}

		String postalCode = extractValueAsString(myAddressPostalCodeValueChild, theValue);
		if (isNotBlank(postalCode)) {
			allNames.add(postalCode);
		}

		for (String nextName : allNames) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, nextName);
		}

	}

	private <T> SearchParamSet<T> extractSearchParams(IBaseResource theResource, IExtractor<T> theExtractor, RestSearchParameterTypeEnum theSearchParamType) {
		SearchParamSet<T> retVal = new SearchParamSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != theSearchParamType) {
				continue;
			}

			extractSearchParam(nextSpDef, theResource, theExtractor, retVal);
		}
		return retVal;
	}

	private <T> void extractSearchParam(RuntimeSearchParam theSearchParameterDef, IBaseResource theResource, IExtractor<T> theExtractor, SearchParamSet<T> theSetToPopulate) {
		String nextPathUnsplit = theSearchParameterDef.getPath();
		if (isBlank(nextPathUnsplit)) {
			return;
		}

		String[] splitPaths = split(nextPathUnsplit);
		for (String nextPath : splitPaths) {
			nextPath = trim(nextPath);
			for (IBase nextObject : extractValues(nextPath, theResource)) {
				if (nextObject != null) {
					String typeName = toRootTypeName(nextObject);
					if (!myIgnoredForSearchDatatypes.contains(typeName)) {
						theExtractor.extract(theSetToPopulate, theSearchParameterDef, nextObject, nextPath);
					}
				}
			}
		}
	}

	@Override
	public String toRootTypeName(IBase nextObject) {
		BaseRuntimeElementDefinition<?> elementDefinition = getContext().getElementDefinition(nextObject.getClass());
		BaseRuntimeElementDefinition<?> rootParentDefinition = elementDefinition.getRootParentDefinition();
		return rootParentDefinition.getName();
	}

	@Override
	public String toTypeName(IBase nextObject) {
		BaseRuntimeElementDefinition<?> elementDefinition = getContext().getElementDefinition(nextObject.getClass());
		return elementDefinition.getName();
	}

	private void addUri_Uri(String theResourceType, Set<ResourceIndexedSearchParamUri> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<?> value = (IPrimitiveType<?>) theValue;
		String valueAsString = value.getValueAsString();
		if (isNotBlank(valueAsString)) {
			ResourceIndexedSearchParamUri nextEntity = new ResourceIndexedSearchParamUri(myPartitionSettings, theResourceType, theSearchParam.getName(), valueAsString);
			theParams.add(nextEntity);
		}
	}

	@SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
	private void createStringIndexIfNotBlank(String theResourceType, Set<? extends BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theValue) {
		String value = theValue;
		if (isNotBlank(value)) {
			if (value.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
				value = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
			}

			String searchParamName = theSearchParam.getName();
			String valueNormalized = StringUtil.normalizeStringForSearchIndexing(value);
			String valueEncoded = theSearchParam.encode(valueNormalized);

			if (valueEncoded.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
				valueEncoded = valueEncoded.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
			}

			ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(myPartitionSettings, getModelConfig(), theResourceType, searchParamName, valueEncoded, value);

			Set params = theParams;
			params.add(nextEntity);
		}
	}

	private void createTokenIndexIfNotBlank(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theSystem, String theValue) {
		ResourceIndexedSearchParamToken nextEntity = createTokenIndexIfNotBlank(theResourceType, theSearchParam, theSystem, theValue);
		if (nextEntity != null) {
			theParams.add(nextEntity);
		}
	}

	private ResourceIndexedSearchParamToken createTokenIndexIfNotBlank(String theResourceType, RuntimeSearchParam theSearchParam, String theSystem, String theValue) {
		String system = theSystem;
		String value = theValue;
		ResourceIndexedSearchParamToken nextEntity = null;
		if (isNotBlank(system) || isNotBlank(value)) {
			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				system = system.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
			}
			if (value != null && value.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				value = value.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
			}

			nextEntity = new ResourceIndexedSearchParamToken(myPartitionSettings, theResourceType, theSearchParam.getName(), system, value);
		}

		return nextEntity;
	}

	@Override
	public String[] split(String thePaths) {
		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			if (!thePaths.contains("|")) {
				return new String[]{thePaths};
			}
			return SPLIT_R4.split(thePaths);
		} else {
			if (!thePaths.contains("|") && !thePaths.contains(" or ")) {
				return new String[]{thePaths};
			}
			return SPLIT.split(thePaths);
		}
	}

	private BigDecimal normalizeQuantityContainingTimeUnitsIntoDaysForNumberParam(String theSystem, String theCode, BigDecimal theValue) {
		if (SearchParamConstants.UCUM_NS.equals(theSystem)) {
			if (isNotBlank(theCode)) {
				Unit<? extends Quantity> unit = Unit.valueOf(theCode);
				javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
				double dayValue = dayConverter.convert(theValue.doubleValue());
				theValue = new BigDecimal(dayValue);
			}
		}
		return theValue;
	}

	@PostConstruct
	public void start() {
		myIgnoredForSearchDatatypes = new HashSet<>();
		addIgnoredType(getContext(), "Annotation", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Attachment", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Count", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Distance", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Ratio", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "SampledData", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Signature", myIgnoredForSearchDatatypes);

		/*
		 * This is building up an internal map of all the various field accessors we'll need in order to work
		 * with the model. This is kind of ugly, but we want to be as efficient as possible since
		 * search param extraction happens a whole heck of a lot at runtime..
		 */

		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Quantity");
		myQuantityValueValueChild = quantityDefinition.getChildByName("value");
		myQuantitySystemValueChild = quantityDefinition.getChildByName("system");
		myQuantityCodeValueChild = quantityDefinition.getChildByName("code");

		BaseRuntimeElementCompositeDefinition<?> moneyDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Money");
		myMoneyValueChild = moneyDefinition.getChildByName("value");
		myMoneyCurrencyChild = moneyDefinition.getChildByName("currency");

		BaseRuntimeElementCompositeDefinition<?> locationDefinition = getContext().getResourceDefinition("Location");
		BaseRuntimeChildDefinition locationPositionValueChild = locationDefinition.getChildByName("position");
		myLocationPositionDefinition = (BaseRuntimeElementCompositeDefinition<?>) locationPositionValueChild.getChildByName("position");

		BaseRuntimeElementCompositeDefinition<?> codeSystemDefinition;
		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			codeSystemDefinition = getContext().getResourceDefinition("CodeSystem");
			assert codeSystemDefinition != null;
			myCodeSystemUrlValueChild = codeSystemDefinition.getChildByName("url");
		}

		BaseRuntimeElementCompositeDefinition<?> rangeDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Range");
		myRangeLowValueChild = rangeDefinition.getChildByName("low");
		myRangeHighValueChild = rangeDefinition.getChildByName("high");

		BaseRuntimeElementCompositeDefinition<?> addressDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Address");
		myAddressLineValueChild = addressDefinition.getChildByName("line");
		myAddressCityValueChild = addressDefinition.getChildByName("city");
		myAddressStateValueChild = addressDefinition.getChildByName("state");
		myAddressCountryValueChild = addressDefinition.getChildByName("country");
		myAddressPostalCodeValueChild = addressDefinition.getChildByName("postalCode");

		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			BaseRuntimeElementCompositeDefinition<?> capabilityStatementDefinition = getContext().getResourceDefinition("CapabilityStatement");
			BaseRuntimeChildDefinition capabilityStatementRestChild = capabilityStatementDefinition.getChildByName("rest");
			BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestChild.getChildByName("rest");
			BaseRuntimeChildDefinition capabilityStatementRestSecurityValueChild = capabilityStatementRestDefinition.getChildByName("security");
			BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestSecurityDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestSecurityValueChild.getChildByName("security");
			myCapabilityStatementRestSecurityServiceValueChild = capabilityStatementRestSecurityDefinition.getChildByName("service");
		}

		BaseRuntimeElementCompositeDefinition<?> periodDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Period");
		myPeriodStartValueChild = periodDefinition.getChildByName("start");
		myPeriodEndValueChild = periodDefinition.getChildByName("end");

		BaseRuntimeElementCompositeDefinition<?> timingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Timing");
		myTimingEventValueChild = timingDefinition.getChildByName("event");
		myTimingRepeatValueChild = timingDefinition.getChildByName("repeat");
		BaseRuntimeElementCompositeDefinition<?> timingRepeatDefinition = (BaseRuntimeElementCompositeDefinition<?>) myTimingRepeatValueChild.getChildByName("repeat");
		myTimingRepeatBoundsValueChild = timingRepeatDefinition.getChildByName("bounds[x]");

		BaseRuntimeElementCompositeDefinition<?> durationDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Duration");
		myDurationSystemValueChild = durationDefinition.getChildByName("system");
		myDurationCodeValueChild = durationDefinition.getChildByName("code");
		myDurationValueValueChild = durationDefinition.getChildByName("value");

		BaseRuntimeElementCompositeDefinition<?> humanNameDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("HumanName");
		myHumanNameFamilyValueChild = humanNameDefinition.getChildByName("family");
		myHumanNameGivenValueChild = humanNameDefinition.getChildByName("given");

		BaseRuntimeElementCompositeDefinition<?> contactPointDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("ContactPoint");
		myContactPointValueValueChild = contactPointDefinition.getChildByName("value");
		myContactPointSystemValueChild = contactPointDefinition.getChildByName("system");

		BaseRuntimeElementCompositeDefinition<?> identifierDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Identifier");
		myIdentifierSystemValueChild = identifierDefinition.getChildByName("system");
		myIdentifierValueValueChild = identifierDefinition.getChildByName("value");
		myIdentifierTypeValueChild = identifierDefinition.getChildByName("type");
		BaseRuntimeElementCompositeDefinition<?> identifierTypeDefinition = (BaseRuntimeElementCompositeDefinition<?>) myIdentifierTypeValueChild.getChildByName("type");
		myIdentifierTypeTextValueChild = identifierTypeDefinition.getChildByName("text");

		BaseRuntimeElementCompositeDefinition<?> codeableConceptDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("CodeableConcept");
		myCodeableConceptCodingValueChild = codeableConceptDefinition.getChildByName("coding");
		myCodeableConceptTextValueChild = codeableConceptDefinition.getChildByName("text");

		BaseRuntimeElementCompositeDefinition<?> codingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Coding");
		myCodingSystemValueChild = codingDefinition.getChildByName("system");
		myCodingCodeValueChild = codingDefinition.getChildByName("code");
		myCodingDisplayValueChild = codingDefinition.getChildByName("display");

		BaseRuntimeElementCompositeDefinition<?> patientDefinition = getContext().getResourceDefinition("Patient");
		BaseRuntimeChildDefinition patientCommunicationValueChild = patientDefinition.getChildByName("communication");
		BaseRuntimeElementCompositeDefinition<?> patientCommunicationDefinition = (BaseRuntimeElementCompositeDefinition<?>) patientCommunicationValueChild.getChildByName("communication");
		myPatientCommunicationLanguageValueChild = patientCommunicationDefinition.getChildByName("language");

	}

	@FunctionalInterface
	public interface IValueExtractor {

		List<? extends IBase> get() throws FHIRException;

	}

	@FunctionalInterface
	private interface IExtractor<T> {


		void extract(SearchParamSet<T> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath);

	}

	private class TokenExtractor implements IExtractor<BaseResourceIndexedSearchParam> {
		private final String myResourceTypeName;
		private final String myUseSystem;

		public TokenExtractor(String theResourceTypeName, String theUseSystem) {
			myResourceTypeName = theResourceTypeName;
			myUseSystem = theUseSystem;
		}

		@Override
		public void extract(SearchParamSet<BaseResourceIndexedSearchParam> params, RuntimeSearchParam searchParam, IBase value, String path) {

			// DSTU3+
			if (value instanceof IBaseEnumeration<?>) {
				IBaseEnumeration<?> obj = (IBaseEnumeration<?>) value;
				String system = extractSystem(obj);
				String code = obj.getValueAsString();
				BaseSearchParamExtractor.this.createTokenIndexIfNotBlank(myResourceTypeName, params, searchParam, system, code);
				return;
			}

			// DSTU2 only
			if (value instanceof BoundCodeDt) {
				BoundCodeDt boundCode = (BoundCodeDt) value;
				Enum valueAsEnum = boundCode.getValueAsEnum();
				String system = null;
				if (valueAsEnum != null) {
					//noinspection unchecked
					system = boundCode.getBinder().toSystemString(valueAsEnum);
				}
				String code = boundCode.getValueAsString();
				BaseSearchParamExtractor.this.createTokenIndexIfNotBlank(myResourceTypeName, params, searchParam, system, code);
				return;
			}

			if (value instanceof IPrimitiveType) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) value;
				String systemAsString = null;
				String valueAsString = nextValue.getValueAsString();
				if ("CodeSystem.concept.code".equals(path)) {
					systemAsString = myUseSystem;
				} else if ("ValueSet.codeSystem.concept.code".equals(path)) {
					systemAsString = myUseSystem;
				}

				if (value instanceof IIdType) {
					valueAsString = ((IIdType) value).getIdPart();
				}

				BaseSearchParamExtractor.this.createTokenIndexIfNotBlank(myResourceTypeName, params, searchParam, systemAsString, valueAsString);
				return;
			}

			switch (path) {
				case "Patient.communication":
					BaseSearchParamExtractor.this.addToken_PatientCommunication(myResourceTypeName, params, searchParam, value);
					return;
				case "Consent.source":
					// Consent#source-identifier has a path that isn't typed - This is a one-off to deal with that
					return;
				case "Location.position":
					BaseSearchParamExtractor.this.addCoords_Position(myResourceTypeName, params, searchParam, value);
					return;
				case "StructureDefinition.context":
					// TODO: implement this
					ourLog.warn("StructureDefinition context indexing not currently supported");
					return;
				case "CapabilityStatement.rest.security":
					BaseSearchParamExtractor.this.addToken_CapabilityStatementRestSecurity(myResourceTypeName, params, searchParam, value);
					return;
			}

			String nextType = BaseSearchParamExtractor.this.toRootTypeName(value);
			switch (nextType) {
				case "Identifier":
					BaseSearchParamExtractor.this.addToken_Identifier(myResourceTypeName, params, searchParam, value);
					break;
				case "CodeableConcept":
					BaseSearchParamExtractor.this.addToken_CodeableConcept(myResourceTypeName, params, searchParam, value);
					break;
				case "Coding":
					BaseSearchParamExtractor.this.addToken_Coding(myResourceTypeName, params, searchParam, value);
					break;
				case "ContactPoint":
					BaseSearchParamExtractor.this.addToken_ContactPoint(myResourceTypeName, params, searchParam, value);
					break;
				default:
					BaseSearchParamExtractor.this.addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		}
	}

	public static boolean tokenTextIndexingEnabledForSearchParam(ModelConfig theModelConfig, RuntimeSearchParam theSearchParam) {
		Optional<Boolean> noSuppressForSearchParam = theSearchParam.getExtensions(HapiExtensions.EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING).stream()
			.map(IBaseExtension::getValue)
			.map(val -> (IPrimitiveType<?>) val)
			.map(IPrimitiveType::getValueAsString)
			.map(Boolean::parseBoolean)
			.findFirst();

		//if the SP doesn't care, use the system default.
		if (!noSuppressForSearchParam.isPresent()) {
			return !theModelConfig.isSuppressStringIndexingInTokens();
			//If the SP does care, use its value.
		} else {
			boolean suppressForSearchParam = noSuppressForSearchParam.get();
			ourLog.trace("Text indexing for SearchParameter {}: {}", theSearchParam.getName(), suppressForSearchParam);
			return !suppressForSearchParam;
		}
	}

	private static void addIgnoredType(FhirContext theCtx, String theType, Set<String> theIgnoredTypes) {
		BaseRuntimeElementDefinition<?> elementDefinition = theCtx.getElementDefinition(theType);
		if (elementDefinition != null) {
			theIgnoredTypes.add(elementDefinition.getName());
		}
	}

	private static String extractValueAsString(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<?>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValueAsString)
			.orElse(null);
	}

	private static Date extractValueAsDate(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<Date>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValue)
			.orElse(null);
	}

	private static BigDecimal extractValueAsBigDecimal(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<BigDecimal>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValue)
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	private static List<IPrimitiveType<Date>> extractValuesAsFhirDates(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return (List) theChildDefinition
			.getAccessor()
			.getValues(theElement);
	}

	private static List<String> extractValuesAsStrings(BaseRuntimeChildDefinition theChildDefinition, IBase theValue) {
		return theChildDefinition
			.getAccessor()
			.getValues(theValue)
			.stream()
			.map(t -> (IPrimitiveType) t)
			.map(IPrimitiveType::getValueAsString)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());
	}

	private static <T extends Enum<?>> String extractSystem(IBaseEnumeration<T> theBoundCode) {
		if (theBoundCode.getValue() != null) {
			return theBoundCode.getEnumFactory().toSystem(theBoundCode.getValue());
		}
		return null;
	}

}
