package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.StringUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringTokenizer;
import org.fhir.ucum.Pair;
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

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public abstract class BaseSearchParamExtractor implements ISearchParamExtractor {

	public static final Set<String> COORDS_INDEX_PATHS;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);

	static {
		Set<String> coordsIndexPaths = Sets.newHashSet("Location.position");
		COORDS_INDEX_PATHS = Collections.unmodifiableSet(coordsIndexPaths);
	}

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
	private BaseRuntimeChildDefinition myHumanNameTextValueChild;
	private BaseRuntimeChildDefinition myHumanNamePrefixValueChild;
	private BaseRuntimeChildDefinition myHumanNameSuffixValueChild;
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
	public SearchParamSet<PathAndRef> extractResourceLinks(IBaseResource theResource, boolean theWantLocalReferences) {
		IExtractor<PathAndRef> extractor = createReferenceExtractor();
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.REFERENCE, theWantLocalReferences);
	}

	private IExtractor<PathAndRef> createReferenceExtractor() {
		return new ResourceLinkExtractor();
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
				if (myModelConfig.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED)) {
					extractor = new CompositeExtractor(
						createQuantityExtractor(theResource),
						createQuantityNormalizedExtractor(theResource)
					);
				} else {
					extractor = createQuantityExtractor(theResource);
				}
				break;
			case URI:
				extractor = createUriExtractor(theResource);
				break;
			case SPECIAL:
				extractor = createSpecialExtractor(theResource.getIdElement().getResourceType());
				break;
			case COMPOSITE:
			default:
				throw new UnsupportedOperationException(Msg.code(503) + "Type " + theSearchParam.getParamType() + " not supported for extraction");
		}

		return extractParamsAsQueryTokens(theSearchParam, theResource, extractor);
	}

	private List<String> extractReferenceParamsAsQueryTokens(RuntimeSearchParam theSearchParam, IBaseResource theResource, IExtractor<PathAndRef> theExtractor) {
		SearchParamSet<PathAndRef> params = new SearchParamSet<>();
		extractSearchParam(theSearchParam, theResource, theExtractor, params, false);
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
		extractSearchParam(theSearchParam, theResource, theExtractor, params, false);
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
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.TOKEN, false);
	}

	@Override
	public SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, RuntimeSearchParam theSearchParam) {
		IExtractor<BaseResourceIndexedSearchParam> extractor = createTokenExtractor(theResource);
		SearchParamSet<BaseResourceIndexedSearchParam> setToPopulate = new SearchParamSet<>();
		extractSearchParam(theSearchParam, theResource, extractor, setToPopulate, false);
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
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.SPECIAL, false);
	}

	private IExtractor<BaseResourceIndexedSearchParam> createSpecialExtractor(String theResourceTypeName) {
		return (params, searchParam, value, path, theWantLocalReferences) -> {
			if (COORDS_INDEX_PATHS.contains(path)) {
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
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.URI, false);
	}

	private IExtractor<ResourceIndexedSearchParamUri> createUriExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path, theWantLocalReferences) -> {
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
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.DATE, false);
	}

	private IExtractor<ResourceIndexedSearchParamDate> createDateExtractor(IBaseResource theResource) {
		return new DateExtractor(theResource);
	}

	@Override
	public Date extractDateFromResource(IBase theValue, String thePath) {
		DateExtractor extractor = new DateExtractor("DateType");
		return extractor.get(theValue, thePath, false).getValueHigh();
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamNumber> extractor = createNumberExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.NUMBER, false);
	}

	private IExtractor<ResourceIndexedSearchParamNumber> createNumberExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path, theWantLocalReferences) -> {
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
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.QUANTITY, false);
	}


	@Override
	public SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamQuantityNormalized> extractor = createQuantityNormalizedExtractor(theResource);
		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.QUANTITY, false);
	}

	private IExtractor<ResourceIndexedSearchParamQuantity> createQuantityExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path, theWantLocalReferences) -> {
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

	private IExtractor<ResourceIndexedSearchParamQuantityNormalized> createQuantityNormalizedExtractor(IBaseResource theResource) {

		return (params, searchParam, value, path, theWantLocalReferences) -> {
			if (value.getClass().equals(myLocationPositionDefinition.getImplementingClass())) {
				return;
			}

			String nextType = toRootTypeName(value);
			String resourceType = toRootTypeName(theResource);
			switch (nextType) {
				case "Quantity":
					addQuantity_QuantityNormalized(resourceType, params, searchParam, value);
					break;
				case "Money":
					addQuantity_MoneyNormalized(resourceType, params, searchParam, value);
					break;
				case "Range":
					addQuantity_RangeNormalized(resourceType, params, searchParam, value);
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

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.STRING, false);
	}

	private IExtractor<ResourceIndexedSearchParamString> createStringExtractor(IBaseResource theResource) {
		return (params, searchParam, value, path, theWantLocalReferences) -> {
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
					throw new InternalErrorException(Msg.code(504) + msg, e);
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

	protected FhirContext getContext() {
		return myContext;
	}

	@VisibleForTesting
	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	protected ModelConfig getModelConfig() {
		return myModelConfig;
	}

	@VisibleForTesting
	public void setModelConfig(ModelConfig theModelConfig) {
		myModelConfig = theModelConfig;
	}

	@VisibleForTesting
	public void setSearchParamRegistry(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
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

	private void addQuantity_QuantityNormalized(String theResourceType, Set<ResourceIndexedSearchParamQuantityNormalized> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		Optional<IPrimitiveType<BigDecimal>> valueField = myQuantityValueValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();
			String system = extractValueAsString(myQuantitySystemValueChild, theValue);
			String code = extractValueAsString(myQuantityCodeValueChild, theValue);

			//-- convert the value/unit to the canonical form if any
			Pair canonicalForm = UcumServiceUtil.getCanonicalForm(system, nextValueValue, code);
			if (canonicalForm != null) {
				double canonicalValue = Double.parseDouble(canonicalForm.getValue().asDecimal());
				String canonicalUnits = canonicalForm.getCode();
				ResourceIndexedSearchParamQuantityNormalized nextEntity = new ResourceIndexedSearchParamQuantityNormalized(myPartitionSettings, theResourceType, theSearchParam.getName(), canonicalValue, system, canonicalUnits);
				theParams.add(nextEntity);
			}

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

	private void addQuantity_MoneyNormalized(String theResourceType, Set<ResourceIndexedSearchParamQuantityNormalized> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		Optional<IPrimitiveType<BigDecimal>> valueField = myMoneyValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();

			String nextValueString = "urn:iso:std:iso:4217";
			String nextValueCode = extractValueAsString(myMoneyCurrencyChild, theValue);
			String searchParamName = theSearchParam.getName();

			ResourceIndexedSearchParamQuantityNormalized nextEntityNormalized = new ResourceIndexedSearchParamQuantityNormalized(myPartitionSettings, theResourceType, searchParamName, nextValueValue.doubleValue(), nextValueString, nextValueCode);
			theParams.add(nextEntityNormalized);
		}
	}

	private void addQuantity_Range(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		Optional<IBase> low = myRangeLowValueChild.getAccessor().getFirstValueOrNull(theValue);
		low.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));

		Optional<IBase> high = myRangeHighValueChild.getAccessor().getFirstValueOrNull(theValue);
		high.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));
	}

	private void addQuantity_RangeNormalized(String theResourceType, Set<ResourceIndexedSearchParamQuantityNormalized> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		Optional<IBase> low = myRangeLowValueChild.getAccessor().getFirstValueOrNull(theValue);
		low.ifPresent(theIBase -> addQuantity_QuantityNormalized(theResourceType, theParams, theSearchParam, theIBase));

		Optional<IBase> high = myRangeHighValueChild.getAccessor().getFirstValueOrNull(theValue);
		high.ifPresent(theIBase -> addQuantity_QuantityNormalized(theResourceType, theParams, theSearchParam, theIBase));
	}

	@SuppressWarnings("unchecked")
	private void addToken_Identifier(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		String system = extractValueAsString(myIdentifierSystemValueChild, theValue);
		String value = extractValueAsString(myIdentifierValueValueChild, theValue);
		if (isNotBlank(value)) {
			createTokenIndexIfNotBlankAndAdd(theResourceType, theParams, theSearchParam, system, value);

			boolean indexIdentifierType = myModelConfig.isIndexIdentifierOfType();
			if (indexIdentifierType) {
				Optional<IBase> type = myIdentifierTypeValueChild.getAccessor().getFirstValueOrNull(theValue);
				if (type.isPresent()) {
					List<IBase> codings = myCodeableConceptCodingValueChild.getAccessor().getValues(type.get());
					for (IBase nextCoding : codings) {

						String typeSystem = myCodingSystemValueChild.getAccessor().getFirstValueOrNull(nextCoding).map(t -> ((IPrimitiveType<String>) t).getValue()).orElse(null);
						String typeValue = myCodingCodeValueChild.getAccessor().getFirstValueOrNull(nextCoding).map(t -> ((IPrimitiveType<String>) t).getValue()).orElse(null);
						if (isNotBlank(typeSystem) && isNotBlank(typeValue)) {
							String paramName = theSearchParam.getName() + Constants.PARAMQUALIFIER_TOKEN_OF_TYPE;
							ResourceIndexedSearchParamToken token = createTokenIndexIfNotBlank(theResourceType, typeSystem, typeValue + "|" + value, paramName);
							if (token != null) {
								theParams.add(token);
							}
						}

					}
				}
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
			return createTokenIndexIfNotBlank(theResourceType, system, code, theSearchParam.getName());
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
		createTokenIndexIfNotBlankAndAdd(theResourceType, theParams, theSearchParam, system, value);
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
			double normalizedLatitude = GeopointNormalizer.normalizeLatitude(latitude.doubleValue());
			double normalizedLongitude = GeopointNormalizer.normalizeLongitude(longitude.doubleValue());
			ResourceIndexedSearchParamCoords nextEntity = new ResourceIndexedSearchParamCoords(myPartitionSettings, theResourceType, theSearchParam.getName(), normalizedLatitude, normalizedLongitude);
			theParams.add(nextEntity);
		}
	}

	private void addString_HumanName(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		List<BaseRuntimeChildDefinition> myHumanNameChildren = Arrays.asList(myHumanNameFamilyValueChild, myHumanNameGivenValueChild, myHumanNameTextValueChild, myHumanNamePrefixValueChild, myHumanNameSuffixValueChild);
		for (BaseRuntimeChildDefinition theChild : myHumanNameChildren) {
			List<String> indices = extractValuesAsStrings(theChild, theValue);
			for (String next : indices) {
				createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, next);
			}
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

	private <T> SearchParamSet<T> extractSearchParams(IBaseResource theResource, IExtractor<T> theExtractor, RestSearchParameterTypeEnum theSearchParamType, boolean theWantLocalReferences) {
		SearchParamSet<T> retVal = new SearchParamSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);

		cleanUpContainedResourceReferences(theResource, theSearchParamType, searchParams);

		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != theSearchParamType) {
				continue;
			}

			extractSearchParam(nextSpDef, theResource, theExtractor, retVal, theWantLocalReferences);
		}
		return retVal;
	}


	/**
	 * Helper function to determine if a set of SPs for a resource uses a resolve as part of its fhir path.
	 */
	private boolean anySearchParameterUsesResolve(Collection<RuntimeSearchParam> searchParams, RestSearchParameterTypeEnum theSearchParamType) {
		return searchParams.stream()
			.filter(param -> param.getParamType() != theSearchParamType)
			.map(RuntimeSearchParam::getPath)
			.filter(Objects::nonNull)
			.anyMatch(path -> path.contains("resolve"));
	}

	/**
	 * HAPI FHIR Reference objects (e.g. {@link org.hl7.fhir.r4.model.Reference}) can hold references either by text
	 * (e.g. "#3") or by resource (e.g. "new Reference(patientInstance)"). The FHIRPath evaluator only understands the
	 * first way, so if there is any chance of the FHIRPath evaluator needing to descend across references, we
	 * have to assign values to those references before indexing.
	 * <p>
	 * Doing this cleanup isn't hugely expensive, but it's not completely free either so we only do it
	 * if we think there's actually a chance
	 */
	private void cleanUpContainedResourceReferences(IBaseResource theResource, RestSearchParameterTypeEnum theSearchParamType, Collection<RuntimeSearchParam> searchParams) {
		boolean havePathWithResolveExpression =
			myModelConfig.isIndexOnContainedResources()
				|| anySearchParameterUsesResolve(searchParams, theSearchParamType);

		if (havePathWithResolveExpression && myContext.getParserOptions().isAutoContainReferenceTargetsWithNoId()) {
			//TODO GGG/JA: At this point, if the Task.basedOn.reference.resource does _not_ have an ID, we will attempt to contain it internally. Wild
			myContext.newTerser().containResources(theResource, FhirTerser.OptionsEnum.MODIFY_RESOURCE, FhirTerser.OptionsEnum.STORE_AND_REUSE_RESULTS);
		}
	}

	private <T> void extractSearchParam(RuntimeSearchParam theSearchParameterDef, IBaseResource theResource, IExtractor<T> theExtractor, SearchParamSet<T> theSetToPopulate, boolean theWantLocalReferences) {
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
						theExtractor.extract(theSetToPopulate, theSearchParameterDef, nextObject, nextPath, theWantLocalReferences);
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

	private void createTokenIndexIfNotBlankAndAdd(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theSystem, String theValue) {
		ResourceIndexedSearchParamToken nextEntity = createTokenIndexIfNotBlank(theResourceType, theSystem, theValue, theSearchParam.getName());
		if (nextEntity != null) {
			theParams.add(nextEntity);
		}
	}

	@VisibleForTesting
	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	private ResourceIndexedSearchParamToken createTokenIndexIfNotBlank(String theResourceType, String theSystem, String theValue, String searchParamName) {
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

			nextEntity = new ResourceIndexedSearchParamToken(myPartitionSettings, theResourceType, searchParamName, system, value);
		}

		return nextEntity;
	}

	@Override
	public String[] split(String thePaths) {
		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			if (!thePaths.contains("|")) {
				return new String[]{thePaths};
			}
			return splitPathsR4(thePaths);
		} else {
			if (!thePaths.contains("|") && !thePaths.contains(" or ")) {
				return new String[]{thePaths};
			}
			return splitOutOfParensOrs(thePaths);
		}
	}

	/**
	 * Iteratively splits a string on any ` or ` or | that is ** not** contained inside a set of parentheses. e.g.
	 *
	 * "Patient.select(a or b)" -->  ["Patient.select(a or b)"]
	 * "Patient.select(a or b) or Patient.select(c or d )" --> ["Patient.select(a or b)", "Patient.select(c or d)"]
	 * "Patient.select(a|b) or Patient.select(c or d )" --> ["Patient.select(a|b)", "Patient.select(c or d)"]
	 * "Patient.select(b) | Patient.select(c)" -->  ["Patient.select(b)", "Patient.select(c)"]
	 *
	 * @param thePaths The string to split
	 * @return The split string

	 */
	private String[] splitOutOfParensOrs(String thePaths) {
		List<String> topLevelOrExpressions = splitOutOfParensToken(thePaths, " or ");
		List<String> retVal = topLevelOrExpressions.stream()
			.flatMap(s -> splitOutOfParensToken(s, "|").stream())
			.collect(Collectors.toList());
		return retVal.toArray(new String[retVal.size()]);
	}

	private List<String> splitOutOfParensToken(String thePath, String theToken) {
		int tokenLength = theToken.length();
		int index = thePath.indexOf(theToken);
		int rightIndex = 0;
		List<String> retVal = new ArrayList<>();
		while (index > -1 ) {
			String left = thePath.substring(rightIndex, index);
			if (allParensHaveBeenClosed(left)) {
				retVal.add(left);
				rightIndex = index + tokenLength;
			}
			index = thePath.indexOf(theToken, index + tokenLength);
		}
		retVal.add(thePath.substring(rightIndex));
		return retVal;
	}

	private boolean allParensHaveBeenClosed(String thePaths) {
		int open = StringUtils.countMatches(thePaths, "(");
		int close = StringUtils.countMatches(thePaths, ")");
		return open == close;
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
		myHumanNameTextValueChild = humanNameDefinition.getChildByName("text");
		myHumanNamePrefixValueChild = humanNameDefinition.getChildByName("prefix");
		myHumanNameSuffixValueChild = humanNameDefinition.getChildByName("suffix");

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

		void extract(SearchParamSet<T> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath, boolean theWantLocalReferences);

	}

	/**
	 * Note that this should only be called for R4+ servers. Prior to
	 * R4 the paths could be separated by the word "or" or by a "|"
	 * character, so we used a slower splitting mechanism.
	 */
	@Nonnull
	public static String[] splitPathsR4(@Nonnull String thePaths) {
		StringTokenizer tok = new StringTokenizer(thePaths, " |");
		tok.setTrimmerMatcher(new StringTrimmingTrimmerMatcher());
		return tok.getTokenArray();
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

	protected static String extractValueAsString(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<?>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValueAsString)
			.orElse(null);
	}

	protected static Date extractValueAsDate(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<Date>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValue)
			.orElse(null);
	}

	protected static BigDecimal extractValueAsBigDecimal(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<BigDecimal>>getFirstValueOrNull(theElement)
			.map(IPrimitiveType::getValue)
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	protected static List<IPrimitiveType<Date>> extractValuesAsFhirDates(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return (List) theChildDefinition
			.getAccessor()
			.getValues(theElement);
	}

	protected static List<String> extractValuesAsStrings(BaseRuntimeChildDefinition theChildDefinition, IBase theValue) {
		return theChildDefinition
			.getAccessor()
			.getValues(theValue)
			.stream()
			.map(t -> (IPrimitiveType) t)
			.map(IPrimitiveType::getValueAsString)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());
	}

	protected static <T extends Enum<?>> String extractSystem(IBaseEnumeration<T> theBoundCode) {
		if (theBoundCode.getValue() != null) {
			return theBoundCode.getEnumFactory().toSystem(theBoundCode.getValue());
		}
		return null;
	}

	private class ResourceLinkExtractor implements IExtractor<PathAndRef> {

		private PathAndRef myPathAndRef = null;

		@Override
		public void extract(SearchParamSet<PathAndRef> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath, boolean theWantLocalReferences) {
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
						nextId.getValue().startsWith("urn:")) {
						return;
					}
					if (!theWantLocalReferences) {
						if (nextId.getValue().startsWith("#"))
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
				theValue, thePath, false);
			return myPathAndRef;
		}
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
		public void extract(SearchParamSet theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath, boolean theWantLocalReferences) {
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
						if (start != null) {
							dates.add(start);
						}
						if (end != null) {
							dates.add(end);
						}
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

		public ResourceIndexedSearchParamDate get(IBase theValue, String thePath, boolean theWantLocalReferences) {
			extract(new SearchParamSet<>(),
				new RuntimeSearchParam(null, null, "date", null, null, null, null, null, null, null),
				theValue, thePath, theWantLocalReferences);
			return myIndexedSearchParamDate;
		}
	}

	private class TokenExtractor implements IExtractor<BaseResourceIndexedSearchParam> {
		private final String myResourceTypeName;
		private final String myUseSystem;

		public TokenExtractor(String theResourceTypeName, String theUseSystem) {
			myResourceTypeName = theResourceTypeName;
			myUseSystem = theUseSystem;
		}

		@Override
		public void extract(SearchParamSet<BaseResourceIndexedSearchParam> params, RuntimeSearchParam searchParam, IBase value, String path, boolean theWantLocalReferences) {

			// DSTU3+
			if (value instanceof IBaseEnumeration<?>) {
				IBaseEnumeration<?> obj = (IBaseEnumeration<?>) value;
				String system = extractSystem(obj);
				String code = obj.getValueAsString();
				BaseSearchParamExtractor.this.createTokenIndexIfNotBlankAndAdd(myResourceTypeName, params, searchParam, system, code);
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
				BaseSearchParamExtractor.this.createTokenIndexIfNotBlankAndAdd(myResourceTypeName, params, searchParam, system, code);
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

				BaseSearchParamExtractor.this.createTokenIndexIfNotBlankAndAdd(myResourceTypeName, params, searchParam, systemAsString, valueAsString);
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

	private static class CompositeExtractor<T> implements IExtractor<T> {

		private final IExtractor<T> myExtractor0;
		private final IExtractor<T> myExtractor1;

		private CompositeExtractor(IExtractor<T> theExtractor0, IExtractor<T> theExtractor1) {
			myExtractor0 = theExtractor0;
			myExtractor1 = theExtractor1;
		}

		@Override
		public void extract(SearchParamSet<T> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath, boolean theWantLocalReferences) {
			myExtractor0.extract(theParams, theSearchParam, theValue, thePath, theWantLocalReferences);
			myExtractor1.extract(theParams, theSearchParam, theValue, thePath, theWantLocalReferences);
		}
	}

}
