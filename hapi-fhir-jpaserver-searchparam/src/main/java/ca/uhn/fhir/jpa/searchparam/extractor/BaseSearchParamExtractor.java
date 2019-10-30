package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

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
	private Set<String> myIgnoredForSearchDatatypes;
	private BaseRuntimeChildDefinition quantityValueChild;
	private BaseRuntimeChildDefinition quantitySystemChild;
	private BaseRuntimeChildDefinition quantityCodeChild;
	private BaseRuntimeChildDefinition moneyValueChild;
	private BaseRuntimeChildDefinition moneyCurrencyChild;
	private BaseRuntimeElementCompositeDefinition<?> locationPositionDefinition;
	private BaseRuntimeChildDefinition codeSystemUrlValueChild;
	private BaseRuntimeChildDefinition rangeLowValueChild;
	private BaseRuntimeChildDefinition rangeHighValueChild;

	/**
	 * Constructor
	 */
	BaseSearchParamExtractor() {
		super();
	}

	/**
	 * UNIT TEST constructor
	 */
	BaseSearchParamExtractor(FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theCtx;
		mySearchParamRegistry = theSearchParamRegistry;
		start();
	}

	@Override
	public SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {

		String resourceTypeName = toRootTypeName(theResource);
		String useSystem;
		if (getContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			if (resourceTypeName.equals("ValueSet")) {
				ca.uhn.fhir.model.dstu2.resource.ValueSet dstu2ValueSet = (ca.uhn.fhir.model.dstu2.resource.ValueSet)theResource;
				useSystem = dstu2ValueSet.getCodeSystem().getSystem();
			} else {
				useSystem = null;
			}
		} else {
			if (resourceTypeName.equals("CodeSystem")) {
				useSystem = extractValueAsString(codeSystemUrlValueChild, theResource);
			} else {
				useSystem = null;
			}
		}

		IExtractor<BaseResourceIndexedSearchParam> extractor = (params, searchParam, value, path) -> {

			// DSTU3+
			if (value instanceof IBaseEnumeration<?>) {
				IBaseEnumeration<?> obj = (IBaseEnumeration<?>) value;
				String system = extractSystem(obj);
				String code = obj.getValueAsString();
				createTokenIndexIfNotBlank(resourceTypeName, params, searchParam, system, code);
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
				createTokenIndexIfNotBlank(resourceTypeName, params, searchParam, system, code);
				return;
			}

			if (value instanceof IPrimitiveType) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) value;
				String systemAsString = null;
				String valueAsString = nextValue.getValueAsString();
				if ("CodeSystem.concept.code".equals(path)) {
					systemAsString = useSystem;
				} else if ("ValueSet.codeSystem.concept.code".equals(path)) {
					systemAsString = useSystem;
				}

				createTokenIndexIfNotBlank(resourceTypeName, params, searchParam, systemAsString, valueAsString);
				return;
			}

			switch (path) {
				case "Patient.communication":
					addToken_PatientCommunication(resourceTypeName, params, searchParam, value);
					return;
				case "Consent.source":
					// Consent#source-identifier has a path that isn't typed - This is a one-off to deal with that
					return;
				case "Location.position":
					ourLog.warn("Position search not currently supported, not indexing location");
					return;
				case "StructureDefinition.context":
					// TODO: implement this
					ourLog.warn("StructureDefinition context indexing not currently supported");
					return;
				case "CapabilityStatement.rest.security":
					addToken_CapabilityStatementRestSecurity(resourceTypeName, params, searchParam, value);
					return;
			}

			String nextType = toRootTypeName(value);
			switch (nextType) {
				case "Identifier":
					addToken_Identifier(resourceTypeName, params, searchParam, value);
					break;
				case "CodeableConcept":
					addToken_CodeableConcept(resourceTypeName, params, searchParam, value);
					break;
				case "Coding":
					addToken_Coding(resourceTypeName, params, searchParam, value);
					break;
				case "ContactPoint":
					addToken_ContactPoint(resourceTypeName, params, searchParam, value);
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.TOKEN);
	}

	public void addUnexpectedDatatypeWarning(SearchParamSet<? extends BaseResourceIndexedSearchParam> params, RuntimeSearchParam searchParam, IBase value) {
		params.addWarning("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamUri> extractor = (params, searchParam, value, path) -> {
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

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.URI);
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamCoords> extractSearchParamCoords(IBaseResource theResource) {
		return new SearchParamSet<>();
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamDate> extractor = (params, searchParam, value, path) -> {
			String nextType = toRootTypeName(value);
			String resourceType = toRootTypeName(theResource);
			switch (nextType) {
				case "date":
				case "dateTime":
				case "instant":
					addDateTimeTypes(resourceType, params, searchParam, value);
					break;
				case "Period":
					addDate_Period(resourceType, params, searchParam, value);
					break;
				case "Timing":
					addDate_Timing(resourceType, params, searchParam, value);
					break;
				case "string":
					// CarePlan.activitydate can be a string - ignored for now
					break;
				default:
					addUnexpectedDatatypeWarning(params, searchParam, value);
					break;
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.DATE);
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {

		IExtractor<ResourceIndexedSearchParamNumber> extractor = (params, searchParam, value, path) -> {
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

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.NUMBER);
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {

		IExtractor<ResourceIndexedSearchParamQuantity> extractor = (params, searchParam, value, path) -> {
			if (value.getClass().equals(locationPositionDefinition.getImplementingClass())) {
				ourLog.warn("Position search not currently supported, not indexing location");
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

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.QUANTITY);
	}

	@Override
	public SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamString> extractor = (params, searchParam, value, path) -> {
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

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.STRING);
	}

	@Override
	public List<PathAndRef> extractResourceLinks(IBaseResource theResource, RuntimeSearchParam theNextSpDef) {
		ArrayList<PathAndRef> retVal = new ArrayList<>();

		String[] nextPathsSplit = split(theNextSpDef.getPath());
		for (String path : nextPathsSplit) {
			path = path.trim();
			if (isNotBlank(path)) {

				for (Object next : extractValues(path, theResource)) {
					retVal.add(new PathAndRef(path, next));
				}
			}
		}

		return retVal;
	}

	/**
	 * Override parent because we're using FHIRPath here
	 */
	private List<IBase> extractValues(String thePaths, IBaseResource theResource) {
		List<IBase> values = new ArrayList<>();
		if (isNotBlank(thePaths)) {
			String[] nextPathsSplit = split(thePaths);
			for (String nextPath : nextPathsSplit) {
				List<? extends IBase> allValues;

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

	public Collection<RuntimeSearchParam> getSearchParams(IBaseResource theResource) {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		Collection<RuntimeSearchParam> retVal = mySearchParamRegistry.getActiveSearchParams(def.getName()).values();
		List<RuntimeSearchParam> defaultList = Collections.emptyList();
		retVal = ObjectUtils.defaultIfNull(retVal, defaultList);
		return retVal;
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

		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Quantity");
		quantityValueChild = quantityDefinition.getChildByName("value");
		quantitySystemChild = quantityDefinition.getChildByName("system");
		 quantityCodeChild = quantityDefinition.getChildByName("code");

		BaseRuntimeElementCompositeDefinition<?> moneyDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Money");
		moneyValueChild = moneyDefinition.getChildByName("value");
		 moneyCurrencyChild = moneyDefinition.getChildByName("currency");

		BaseRuntimeElementCompositeDefinition<?> locationDefinition = getContext().getResourceDefinition("Location");
		BaseRuntimeChildDefinition locationPositionValueChild = locationDefinition.getChildByName("position");
		locationPositionDefinition = (BaseRuntimeElementCompositeDefinition<?>) locationPositionValueChild.getChildByName("position");

		BaseRuntimeElementCompositeDefinition<?> codeSystemDefinition;
		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			codeSystemDefinition = getContext().getResourceDefinition("CodeSystem");
			assert codeSystemDefinition != null;
			codeSystemUrlValueChild = codeSystemDefinition.getChildByName("url");
		}

		BaseRuntimeElementCompositeDefinition<?> rangeDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Range");
		 rangeLowValueChild = rangeDefinition.getChildByName("low");
		 rangeHighValueChild = rangeDefinition.getChildByName("high");

	}

	private void addQuantity_Quantity(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IPrimitiveType<BigDecimal>> valueField = quantityValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();
			String system = extractValueAsString(quantitySystemChild, theValue);
			String code = extractValueAsString(quantityCodeChild, theValue);

			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(theResourceType, theSearchParam.getName(), nextValueValue, system, code);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Money(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IPrimitiveType<BigDecimal>> valueField = moneyValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();

			String nextValueString = "urn:iso:std:iso:4217";
			String nextValueCode = extractValueAsString(moneyCurrencyChild, theValue);
			String searchParamName = theSearchParam.getName();
			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(theResourceType, searchParamName, nextValueValue, nextValueString, nextValueCode);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Range(String theResourceType, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {

		Optional<IBase> low = rangeLowValueChild.getAccessor().getFirstValueOrNull(theValue);
		low.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));

		Optional<IBase> high = rangeHighValueChild.getAccessor().getFirstValueOrNull(theValue);
		high.ifPresent(theIBase -> addQuantity_Quantity(theResourceType, theParams, theSearchParam, theIBase));
	}

	private void addToken_Identifier(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> identifierDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Identifier");
		BaseRuntimeChildDefinition identifierSystemValueChild = identifierDefinition.getChildByName("system");
		BaseRuntimeChildDefinition identifierValueValueChild = identifierDefinition.getChildByName("value");

		BaseRuntimeChildDefinition identifierTypeValueChild = identifierDefinition.getChildByName("type");
		BaseRuntimeElementCompositeDefinition<?> identifierTypeDefinition = (BaseRuntimeElementCompositeDefinition<?>) identifierTypeValueChild.getChildByName("type");

		BaseRuntimeChildDefinition identifierTypeTextValueChild = identifierTypeDefinition.getChildByName("text");

		String system = extractValueAsString(identifierSystemValueChild, theValue);
		String value = extractValueAsString(identifierValueValueChild, theValue);
		if (isNotBlank(value)) {
			createTokenIndexIfNotBlank(theResourceType, theParams, theSearchParam, system, value);
		}

		Optional<IBase> type = identifierTypeValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (type.isPresent()) {
			String text = extractValueAsString(identifierTypeTextValueChild, type.get());
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
		}

	}

	private void addToken_CodeableConcept(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> codeableConceptDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("CodeableConcept");
		BaseRuntimeChildDefinition codeableConceptCodingValueChild = codeableConceptDefinition.getChildByName("coding");
		BaseRuntimeChildDefinition codeableConceptTextValueChild = codeableConceptDefinition.getChildByName("text");

		List<IBase> codings = codeableConceptCodingValueChild.getAccessor().getValues(theValue);
		for (IBase nextCoding : codings) {
			addToken_Coding(theResourceType, theParams, theSearchParam, nextCoding);
		}

		String text = extractValueAsString(codeableConceptTextValueChild, theValue);
		if (isNotBlank(text)) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
		}
	}

	private void addToken_Coding(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> codingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Coding");
		BaseRuntimeChildDefinition codingSystemValueChild = codingDefinition.getChildByName("system");
		BaseRuntimeChildDefinition codingCodeValueChild = codingDefinition.getChildByName("code");
		BaseRuntimeChildDefinition codingDisplayValueChild = codingDefinition.getChildByName("display");

		String system = extractValueAsString(codingSystemValueChild, theValue);
		String code = extractValueAsString(codingCodeValueChild, theValue);
		createTokenIndexIfNotBlank(theResourceType, theParams, theSearchParam, system, code);

		String text = extractValueAsString(codingDisplayValueChild, theValue);
		createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, text);
	}

	private void addToken_ContactPoint(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> contactPointDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("ContactPoint");
		BaseRuntimeChildDefinition contactPointSystemValueChild = contactPointDefinition.getChildByName("system");
		BaseRuntimeChildDefinition contactPointValueValueChild = contactPointDefinition.getChildByName("value");

		String system = extractValueAsString(contactPointSystemValueChild, theValue);
		String value = extractValueAsString(contactPointValueValueChild, theValue);
		createTokenIndexIfNotBlank(theResourceType, theParams, theSearchParam, system, value);
	}

	private void addToken_PatientCommunication(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> patientDefinition = getContext().getResourceDefinition("Patient");
		BaseRuntimeChildDefinition patientCommunicationValueChild = patientDefinition.getChildByName("communication");
		BaseRuntimeElementCompositeDefinition<?> patientCommunicationDefinition = (BaseRuntimeElementCompositeDefinition<?>) patientCommunicationValueChild.getChildByName("communication");
		BaseRuntimeChildDefinition patientCommunicationLanguageValueChild = patientCommunicationDefinition.getChildByName("language");

		List<IBase> values = patientCommunicationLanguageValueChild.getAccessor().getValues(theValue);
		for (IBase next : values) {
			addToken_CodeableConcept(theResourceType, theParams, theSearchParam, next);
		}
	}

	private void addToken_CapabilityStatementRestSecurity(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementDefinition = getContext().getResourceDefinition("CapabilityStatement");
		BaseRuntimeChildDefinition capabilityStatementRestChild = capabilityStatementDefinition.getChildByName("rest");
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestChild.getChildByName("rest");
		BaseRuntimeChildDefinition capabilityStatementRestSecurityValueChild = capabilityStatementRestDefinition.getChildByName("security");
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestSecurityDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestSecurityValueChild.getChildByName("security");
		BaseRuntimeChildDefinition capabilityStatementRestSecurityServiceValueChild = capabilityStatementRestSecurityDefinition.getChildByName("service");

		List<IBase> values = capabilityStatementRestSecurityServiceValueChild.getAccessor().getValues(theValue);
		for (IBase nextValue : values) {
			addToken_CodeableConcept(theResourceType, theParams, theSearchParam, nextValue);
		}

	}

	private void addDate_Period(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> periodDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Period");
		BaseRuntimeChildDefinition periodStartValueChild = periodDefinition.getChildByName("start");
		BaseRuntimeChildDefinition periodEndValueChild = periodDefinition.getChildByName("end");

		Date start = extractValueAsDate(periodStartValueChild, theValue);
		String startAsString = extractValueAsString(periodStartValueChild, theValue);
		Date end = extractValueAsDate(periodEndValueChild, theValue);

		if (start != null || end != null) {
			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(theResourceType, theSearchParam.getName(), start, end, startAsString);
			theParams.add(nextEntity);
		}
	}

	private void addDate_Timing(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> timingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Timing");
		BaseRuntimeChildDefinition timingEventValueChild = timingDefinition.getChildByName("event");
		BaseRuntimeChildDefinition timingRepeatValueChild = timingDefinition.getChildByName("repeat");
		BaseRuntimeElementCompositeDefinition<?> timingRepeatDefinition = (BaseRuntimeElementCompositeDefinition<?>) timingRepeatValueChild.getChildByName("repeat");
		BaseRuntimeChildDefinition timingRepeatBoundsValueChild = timingRepeatDefinition.getChildByName("bounds[x]");

		List<IPrimitiveType<Date>> values = extractValuesAsFhirDates(timingEventValueChild, theValue);

		TreeSet<Date> dates = new TreeSet<>();
		String firstValue = null;
		for (IPrimitiveType<Date> nextEvent : values) {
			if (nextEvent.getValue() != null) {
				dates.add(nextEvent.getValue());
				if (firstValue == null) {
					firstValue = nextEvent.getValueAsString();
				}
			}
		}

		Optional<IBase> repeat = timingRepeatValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (repeat.isPresent()) {
			Optional<IBase> bounds = timingRepeatBoundsValueChild.getAccessor().getFirstValueOrNull(repeat.get());
			if (bounds.isPresent()) {
				String boundsType = toRootTypeName(bounds.get());
				switch (boundsType) {
					case "Period":
						BaseRuntimeElementCompositeDefinition<?> periodDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Period");
						BaseRuntimeChildDefinition periodStartValueChild = periodDefinition.getChildByName("start");
						BaseRuntimeChildDefinition periodEndValueChild = periodDefinition.getChildByName("end");
						Date start = extractValueAsDate(periodStartValueChild, bounds.get());
						Date end = extractValueAsDate(periodEndValueChild, bounds.get());
						dates.add(start);
						dates.add(end);
						break;
				}
			}
		}

		if (!dates.isEmpty()) {
			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(theResourceType, theSearchParam.getName(), dates.first(), dates.last(), firstValue);
			theParams.add(nextEntity);
		}
	}

	private void addNumber_Duration(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> durationDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Duration");
		BaseRuntimeChildDefinition durationSystemValueChild = durationDefinition.getChildByName("system");
		BaseRuntimeChildDefinition durationCodeValueChild = durationDefinition.getChildByName("code");
		BaseRuntimeChildDefinition durationValueValueChild = durationDefinition.getChildByName("value");

		String system = extractValueAsString(durationSystemValueChild, theValue);
		String code = extractValueAsString(durationCodeValueChild, theValue);
		BigDecimal value = extractValueAsBigDecimal(durationValueValueChild, theValue);
		if (value != null) {
			value = normalizeQuantityContainingTimeUnitsIntoDaysForNumberParam(system, code, value);
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theResourceType, theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	private void addNumber_Quantity(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Duration");
		BaseRuntimeChildDefinition quantityValueValueChild = quantityDefinition.getChildByName("value");
		BaseRuntimeChildDefinition quantitySystemChild = quantityDefinition.getChildByName("system");
		BaseRuntimeChildDefinition quantityCodeChild = quantityDefinition.getChildByName("code");

		BigDecimal value = extractValueAsBigDecimal(quantityValueValueChild, theValue);
		if (value != null) {
			String system = extractValueAsString(quantitySystemChild, theValue);
			String code = extractValueAsString(quantityCodeChild, theValue);
			value = normalizeQuantityContainingTimeUnitsIntoDaysForNumberParam(system, code, value);
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theResourceType, theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	@SuppressWarnings("unchecked")
	private void addNumber_Integer(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<Integer> value = (IPrimitiveType<Integer>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = new BigDecimal(value.getValue());
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theResourceType, theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	@SuppressWarnings("unchecked")
	private void addNumber_Decimal(String theResourceType, Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<BigDecimal> value = (IPrimitiveType<BigDecimal>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = value.getValue();
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theResourceType, theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	private void addString_HumanName(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> humanNameDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("HumanName");
		BaseRuntimeChildDefinition humanNameFamilyValueChild = humanNameDefinition.getChildByName("family");
		BaseRuntimeChildDefinition humanNameGivenValueChild = humanNameDefinition.getChildByName("given");

		List<String> families = extractValuesAsStrings(humanNameFamilyValueChild, theValue);
		for (String next : families) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, next);
		}

		List<String> givens = extractValuesAsStrings(humanNameGivenValueChild, theValue);
		for (String next : givens) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, next);
		}

	}

	private void addString_Quantity(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Quantity");
		BaseRuntimeChildDefinition quantityValueChild = quantityDefinition.getChildByName("value");

		BigDecimal value = extractValueAsBigDecimal(quantityValueChild, theValue);
		if (value != null) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_Range(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> rangeDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Range");
		BaseRuntimeChildDefinition rangeLowValueChild = rangeDefinition.getChildByName("low");

		BigDecimal value = extractValueAsBigDecimal(rangeLowValueChild, theValue);
		if (value != null) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_ContactPoint(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> contactPointDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("ContactPoint");
		BaseRuntimeChildDefinition contactPointValueValueChild = contactPointDefinition.getChildByName("value");

		String value = extractValueAsString(contactPointValueValueChild, theValue);
		if (isNotBlank(value)) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, value);
		}
	}

	private void addString_Address(String theResourceType, Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> addressDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Address");
		BaseRuntimeChildDefinition addressLineValueChild = addressDefinition.getChildByName("line");
		BaseRuntimeChildDefinition addressCityValueChild = addressDefinition.getChildByName("city");
		BaseRuntimeChildDefinition addressStateValueChild = addressDefinition.getChildByName("state");
		BaseRuntimeChildDefinition addressCountryValueChild = addressDefinition.getChildByName("country");
		BaseRuntimeChildDefinition addressPostalCodeValueChild = addressDefinition.getChildByName("postalCode");

		List<String> allNames = new ArrayList<>(extractValuesAsStrings(addressLineValueChild, theValue));

		String city = extractValueAsString(addressCityValueChild, theValue);
		if (isNotBlank(city)) {
			allNames.add(city);
		}

		String state = extractValueAsString(addressStateValueChild, theValue);
		if (isNotBlank(state)) {
			allNames.add(state);
		}

		String country = extractValueAsString(addressCountryValueChild, theValue);
		if (isNotBlank(country)) {
			allNames.add(country);
		}

		String postalCode = extractValueAsString(addressPostalCodeValueChild, theValue);
		if (isNotBlank(postalCode)) {
			allNames.add(postalCode);
		}

		for (String nextName : allNames) {
			createStringIndexIfNotBlank(theResourceType, theParams, theSearchParam, nextName);
		}

	}


	private <T extends BaseResourceIndexedSearchParam> SearchParamSet<T> extractSearchParams(IBaseResource theResource, IExtractor<T> theExtractor, RestSearchParameterTypeEnum theSearchParamType) {
		SearchParamSet<T> retVal = new SearchParamSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != theSearchParamType) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (IBase nextObject : extractValues(nextPath, theResource)) {
				if (nextObject != null) {
					String typeName = toRootTypeName(nextObject);
					if (!myIgnoredForSearchDatatypes.contains(typeName)) {
						theExtractor.extract(retVal, nextSpDef, nextObject, nextPath);
					}
				}
			}
		}
		return retVal;
	}

	private String toRootTypeName(IBase nextObject) {
		BaseRuntimeElementDefinition<?> elementDefinition = getContext().getElementDefinition(nextObject.getClass());
		BaseRuntimeElementDefinition<?> rootParentDefinition = elementDefinition.getRootParentDefinition();
		return rootParentDefinition.getName();
	}

	@SuppressWarnings("unchecked")
	private void addDateTimeTypes(String theResourceType, Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<Date> nextBaseDateTime = (IPrimitiveType<Date>) theValue;
		if (nextBaseDateTime.getValue() != null) {
			ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(theResourceType, theSearchParam.getName(), nextBaseDateTime.getValue(), nextBaseDateTime.getValue(), nextBaseDateTime.getValueAsString());
			theParams.add(param);
		}
	}


	private void addUri_Uri(String theResourceType, Set<ResourceIndexedSearchParamUri> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<?> value = (IPrimitiveType<?>) theValue;
		String valueAsString = value.getValueAsString();
		if (isNotBlank(valueAsString)) {
			ResourceIndexedSearchParamUri nextEntity = new ResourceIndexedSearchParamUri(theResourceType, theSearchParam.getName(), valueAsString);
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
			String valueNormalized = StringNormalizer.normalizeString(value);
			if (valueNormalized.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
				valueNormalized = valueNormalized.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
			}

			ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getModelConfig(), theResourceType, searchParamName, valueNormalized, value);

			Set params = theParams;
			params.add(nextEntity);
		}
	}

	private void createTokenIndexIfNotBlank(String theResourceType, Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theSystem, String theValue) {
		String system = theSystem;
		String value = theValue;
		if (isNotBlank(system) || isNotBlank(value)) {
			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				system = system.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
			}
			if (value != null && value.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				value = value.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
			}

			ResourceIndexedSearchParamToken nextEntity;
			nextEntity = new ResourceIndexedSearchParamToken(theResourceType, theSearchParam.getName(), system, value);
			theParams.add(nextEntity);
		}
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

	@FunctionalInterface
	public interface IValueExtractor {

		List<? extends IBase> get() throws FHIRException;

	}

	@FunctionalInterface
	private interface IExtractor<T> {


		void extract(SearchParamSet<T> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath);

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
			.map(t -> t.getValueAsString())
			.orElse(null);
	}

	private static Date extractValueAsDate(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<Date>>getFirstValueOrNull(theElement)
			.map(t -> t.getValue())
			.orElse(null);
	}

	private static BigDecimal extractValueAsBigDecimal(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<BigDecimal>>getFirstValueOrNull(theElement)
			.map(t -> t.getValue())
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
			.map(t -> t.getValueAsString())
			.filter(t -> isNotBlank(t))
			.collect(Collectors.toList());
	}

	private static <T extends Enum<?>> String extractSystem(IBaseEnumeration<T> theBoundCode) {
		if (theBoundCode.getValue() != null) {
			return theBoundCode.getEnumFactory().toSystem(theBoundCode.getValue());
		}
		return null;
	}


}
