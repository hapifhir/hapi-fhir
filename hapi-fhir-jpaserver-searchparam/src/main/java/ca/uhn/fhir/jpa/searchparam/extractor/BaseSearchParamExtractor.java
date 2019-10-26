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
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseSearchParamExtractor implements ISearchParamExtractor {

	private static final Pattern ASPLIT = Pattern.compile("\\||( or )");
	private static final Pattern ASPLIT_R4 = Pattern.compile("\\|");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ModelConfig myModelConfig;
	private Set<Class<?>> myIgnoredForSearchDatatypes;

	public BaseSearchParamExtractor() {
		super();
	}

	// Used for testing
	protected BaseSearchParamExtractor(FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theCtx;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	protected Set<Class<?>> getIgnoredForSearchDatatypes() {
		return myIgnoredForSearchDatatypes;
	}


	/**
	 * Override parent because we're using FHIRPath here
	 */
	protected List<IBase> extractValues(String thePaths, IBaseResource theResource) {
		List<IBase> values = new ArrayList<>();
		if (isNotBlank(thePaths)) {
			String[] nextPathsSplit = split(thePaths);
			for (String nextPath : nextPathsSplit) {
				List<? extends IBase> allValues;

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

	protected abstract IValueExtractor getPathValueExtractor(IBaseResource theResource, String thePaths);

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
		addIgnoredType(getContext(), "Age", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Annotation", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Attachment", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Count", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Distance", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Ratio", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "SampledData", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Signature", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "LocationPositionComponent", myIgnoredForSearchDatatypes);
	}

	private void addQuantity_Quantity(ResourceTable theEntity, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Quantity");
		BaseRuntimeChildDefinition quantityValueChild = quantityDefinition.getChildByName("value");
		BaseRuntimeChildDefinition quantitySystemChild = quantityDefinition.getChildByName("system");
		BaseRuntimeChildDefinition quantityCodeChild = quantityDefinition.getChildByName("code");

		Optional<IPrimitiveType<BigDecimal>> valueField = quantityValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();
			String system = extractValueAsString(quantitySystemChild, theValue);
			String code = extractValueAsString(quantityCodeChild, theValue);

			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(theSearchParam.getName(), nextValueValue, system, code);
			nextEntity.setResource(theEntity);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Money(ResourceTable theEntity, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> moneyDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Money");
		BaseRuntimeChildDefinition moneyValueChild = moneyDefinition.getChildByName("value");
		BaseRuntimeChildDefinition moneyCurrencyChild = moneyDefinition.getChildByName("currency");

		Optional<IPrimitiveType<BigDecimal>> valueField = moneyValueChild.getAccessor().getFirstValueOrNull(theValue);
		if (valueField.isPresent() && valueField.get().getValue() != null) {
			BigDecimal nextValueValue = valueField.get().getValue();

			String nextValueString = "urn:iso:std:iso:4217";
			String nextValueCode = extractValueAsString(moneyCurrencyChild, theValue);
			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(theSearchParam.getName(), nextValueValue, nextValueString, nextValueCode);
			nextEntity.setResource(theEntity);
			theParams.add(nextEntity);
		}

	}

	private void addQuantity_Range(ResourceTable theEntity, Set<ResourceIndexedSearchParamQuantity> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> rangeDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Range");
		BaseRuntimeChildDefinition rangeLowValueChild = rangeDefinition.getChildByName("low");
		BaseRuntimeChildDefinition rangeHighValueChild = rangeDefinition.getChildByName("high");

		Optional<IBase> low = rangeLowValueChild.getAccessor().getFirstValueOrNull(theValue);
		low.ifPresent(theIBase -> addQuantity_Quantity(theEntity, theParams, theSearchParam, theIBase));

		Optional<IBase> high = rangeHighValueChild.getAccessor().getFirstValueOrNull(theValue);
		high.ifPresent(theIBase -> addQuantity_Quantity(theEntity, theParams, theSearchParam, theIBase));
	}

	private void addToken_Identifier(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> identifierDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Identifier");
		BaseRuntimeChildDefinition identifierSystemValueChild = identifierDefinition.getChildByName("system");
		BaseRuntimeChildDefinition identifierValueValueChild = identifierDefinition.getChildByName("value");
		BaseRuntimeChildDefinition identifierTextValueChild = identifierDefinition.getChildByName("text");

		String system = extractValueAsString(identifierSystemValueChild, theValue);
		String value = extractValueAsString(identifierValueValueChild, theValue);
		if (isNotBlank(value)) {
			addTokenIfNotBlank(theParams, theSearchParam, system, value);
		}

		String text = extractValueAsString(identifierTextValueChild, theValue);
		if (isNotBlank(text)) {
			addSearchTermIfNotBlank(theParams, theSearchParam, text);
		}

	}

	private void addToken_CodeableConcept(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> codeableConceptDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("CodeableConcept");
		BaseRuntimeChildDefinition codeableConceptCodingValueChild = codeableConceptDefinition.getChildByName("coding");
		BaseRuntimeChildDefinition codeableConceptTextValueChild = codeableConceptDefinition.getChildByName("text");

		List<IBase> codings = codeableConceptCodingValueChild.getAccessor().getValues(theValue);
		for (IBase nextCoding : codings) {
			addToken_Coding(theParams, theSearchParam, nextCoding);
		}

		String text = extractValueAsString(codeableConceptTextValueChild, theValue);
		if (isNotBlank(text)) {
			addSearchTermIfNotBlank(theParams, theSearchParam, text);
		}
	}

	private void addToken_Coding(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> codingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Coding");
		BaseRuntimeChildDefinition codingSystemValueChild = codingDefinition.getChildByName("system");
		BaseRuntimeChildDefinition codingCodeValueChild = codingDefinition.getChildByName("code");
		BaseRuntimeChildDefinition codingDisplayValueChild = codingDefinition.getChildByName("display");

		String system = extractValueAsString(codingSystemValueChild, theValue);
		String code = extractValueAsString(codingCodeValueChild, theValue);
		addTokenIfNotBlank(theParams, theSearchParam, system, code);

		String text = extractValueAsString(codingDisplayValueChild, theValue);
		addSearchTermIfNotBlank(theParams, theSearchParam, text);
	}

	private void addToken_ContactPoint(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> contactPointDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("ContactPoint");
		BaseRuntimeChildDefinition contactPointSystemValueChild = contactPointDefinition.getChildByName("system");
		BaseRuntimeChildDefinition contactPointValueValueChild = contactPointDefinition.getChildByName("value");

		String system = extractValueAsString(contactPointSystemValueChild, theValue);
		String value = extractValueAsString(contactPointValueValueChild, theValue);
		addTokenIfNotBlank(theParams, theSearchParam, system, value);
	}

	private void addToken_PatientCommunication(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> patientDefinition = getContext().getResourceDefinition("Patient");
		BaseRuntimeChildDefinition patientCommunicationValueChild = patientDefinition.getChildByName("communication");
		BaseRuntimeElementCompositeDefinition<?> patientCommunicationDefinition = (BaseRuntimeElementCompositeDefinition<?>) patientCommunicationValueChild.getChildByName("communication");
		BaseRuntimeChildDefinition patientCommunicationLanguageValueChild = patientCommunicationDefinition.getChildByName("language");

		List<IBase> values = patientCommunicationLanguageValueChild.getAccessor().getValues(theValue);
		for (IBase next : values) {
			addToken_CodeableConcept(theParams, theSearchParam, next);
		}
	}

	private void addToken_CapabilityStatementRestSecurity(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementDefinition = getContext().getResourceDefinition("CapabilityStatement");
		BaseRuntimeChildDefinition capabilityStatementRestChild = capabilityStatementDefinition.getChildByName("rest");
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestChild.getChildByName("rest");
		BaseRuntimeChildDefinition capabilityStatementRestSecurityValueChild = capabilityStatementRestDefinition.getChildByName("security");
		BaseRuntimeElementCompositeDefinition<?> capabilityStatementRestSecurityDefinition = (BaseRuntimeElementCompositeDefinition<?>) capabilityStatementRestSecurityValueChild.getChildByName("security");
		BaseRuntimeChildDefinition capabilityStatementRestSecurityServiceValueChild = capabilityStatementRestSecurityDefinition.getChildByName("service");

		List<IBase> values = capabilityStatementRestSecurityServiceValueChild.getAccessor().getValues(theValue);
		for (IBase nextValue : values) {
			addToken_CodeableConcept(theParams, theSearchParam, nextValue);
		}

	}

	private void addDate_Period(Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> periodDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Period");
		BaseRuntimeChildDefinition periodStartValueChild = periodDefinition.getChildByName("start");
		BaseRuntimeChildDefinition periodEndValueChild = periodDefinition.getChildByName("end");

		Date start = extractValueAsDate(periodStartValueChild, theValue);
		String startAsString = extractValueAsString(periodStartValueChild, theValue);
		Date end = extractValueAsDate(periodEndValueChild, theValue);

		if (start != null || end != null) {
			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(theSearchParam.getName(), start, end, startAsString);
			theParams.add(nextEntity);
		}
	}

	private void addDate_Timing(Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> timingDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Timing");
		BaseRuntimeChildDefinition timingEventValueChild = timingDefinition.getChildByName("event");
		BaseRuntimeChildDefinition timingRepeatValueChild = timingDefinition.getChildByName("repeat");
		BaseRuntimeElementCompositeDefinition<?> timingRepeatDefinition = (BaseRuntimeElementCompositeDefinition<?>) timingRepeatValueChild.getChildByName("repeat");
		BaseRuntimeChildDefinition timingRepeatBoundsValueChild = timingRepeatDefinition.getChildByName("bounds");

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
				String boundsType = toTypeName(bounds.get());
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
			ResourceIndexedSearchParamDate nextEntity = new ResourceIndexedSearchParamDate(theSearchParam.getName(), dates.first(), dates.last(), firstValue);
			theParams.add(nextEntity);
		}
	}

	private void addNumber_Duration(Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> durationDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Duration");
		BaseRuntimeChildDefinition durationSystemValueChild = durationDefinition.getChildByName("system");
		BaseRuntimeChildDefinition durationCodeValueChild = durationDefinition.getChildByName("code");
		BaseRuntimeChildDefinition durationValueValueChild = durationDefinition.getChildByName("value");

		String system = extractValueAsString(durationSystemValueChild, theValue);
		String code = extractValueAsString(durationCodeValueChild, theValue);
		BigDecimal value = extractValueAsBigDecimal(durationValueValueChild, theValue);
		if (value != null) {

			if (SearchParamConstants.UCUM_NS.equals(system)) {
				if (isNotBlank(code)) {
					Unit<? extends Quantity> unit = Unit.valueOf(code);
					javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
					double dayValue = dayConverter.convert(value.doubleValue());
					value = new BigDecimal(dayValue);
				}
			}

			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	private void addString_HumanName(Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> humanNameDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("HumanName");
		BaseRuntimeChildDefinition humanNameFamilyValueChild = humanNameDefinition.getChildByName("family");
		BaseRuntimeChildDefinition humanNameGivenValueChild = humanNameDefinition.getChildByName("given");

		List<String> families = extractValuesAsStrings(humanNameFamilyValueChild, theValue);
		for (String next : families) {
			addSearchTermIfNotBlank(theParams, theSearchParam, next);
		}

		List<String> givens = extractValuesAsStrings(humanNameGivenValueChild, theValue);
		for (String next : givens) {
			addSearchTermIfNotBlank(theParams, theSearchParam, next);
		}

	}

	private void addString_Quantity(Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Quantity");
		BaseRuntimeChildDefinition quantityValueChild = quantityDefinition.getChildByName("value");

		BigDecimal value = extractValueAsBigDecimal(quantityValueChild, theValue);
		if (value != null) {
			addSearchTermIfNotBlank(theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_Range(Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> rangeDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Range");
		BaseRuntimeChildDefinition rangeLowValueChild = rangeDefinition.getChildByName("low");

		BigDecimal value = extractValueAsBigDecimal(rangeLowValueChild, theValue);
		if (value != null) {
			addSearchTermIfNotBlank(theParams, theSearchParam, value.toPlainString());
		}
	}

	private void addString_ContactPoint(Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> contactPointDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("ContactPoint");
		BaseRuntimeChildDefinition contactPointValueValueChild = contactPointDefinition.getChildByName("value");

		String value = extractValueAsString(contactPointValueValueChild, theValue);
		if (isNotBlank(value)) {
			addSearchTermIfNotBlank(theParams, theSearchParam, value);
		}
	}

	private void addString_Address(Set<ResourceIndexedSearchParamString> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
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
			addSearchTermIfNotBlank(theParams, theSearchParam, nextName);
		}

	}

	private void addNumber_Quantity(Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> quantityDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("Duration");
		BaseRuntimeChildDefinition quantityValueValueChild = quantityDefinition.getChildByName("value");

		BigDecimal value = extractValueAsBigDecimal(quantityValueValueChild, theValue);
		if (value != null) {
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theSearchParam.getName(), value);
			theParams.add(nextEntity);
		}
	}

	@SuppressWarnings("unchecked")
	private void addNumber_Integer(Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<Integer> value = (IPrimitiveType<Integer>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = new BigDecimal(value.getValue());
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	@SuppressWarnings("unchecked")
	private void addNumber_Decimal(Set<ResourceIndexedSearchParamNumber> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<BigDecimal> value = (IPrimitiveType<BigDecimal>) theValue;
		if (value.getValue() != null) {
			BigDecimal valueDecimal = value.getValue();
			ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(theSearchParam.getName(), valueDecimal);
			theParams.add(nextEntity);
		}

	}

	@Override
	public Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IBaseResource theResource) {
		// TODO: implement
		return Collections.emptySet();
	}

	@Override
	public Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamDate> extractor = (params, searchParam, value, path) -> {
			String nextType = toTypeName(value);
			switch (nextType) {
				case "date":
				case "dateTime":
				case "instant":
					addDateTimeTypes(params, searchParam, value);
					break;
				case "Period":
					addDate_Period(params, searchParam, value);
					break;
				case "Timing":
					addDate_Timing(params, searchParam, value);
					break;
				case "string":
					// CarePlan.activitydate can be a string - ignored for now
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.DATE);
	}

	private <T extends BaseResourceIndexedSearchParam> Set<T> extractSearchParams(IBaseResource theResource, IExtractor<T> theExtractor, RestSearchParameterTypeEnum theSearchParamType) {
		Set<T> retVal = new HashSet<>();

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
					theExtractor.extract(retVal, nextSpDef, nextObject, nextPath);
				}
			}
		}
		return retVal;
	}

	private String toTypeName(IBase nextObject) {
		return getContext().getElementDefinition(nextObject.getClass()).getName();
	}

	@SuppressWarnings("unchecked")
	private void addDateTimeTypes(Set<ResourceIndexedSearchParamDate> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<Date> nextBaseDateTime = (IPrimitiveType<Date>) theValue;
		if (nextBaseDateTime.getValue() != null) {
			ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate(theSearchParam.getName(), nextBaseDateTime.getValue(), nextBaseDateTime.getValue(), nextBaseDateTime.getValueAsString());
			theParams.add(param);
		}
	}

	@Override
	public Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamNumber> extractor = (params, searchParam, value, path) -> {
			String nextType = toTypeName(value);
			switch (nextType) {
				case "Duration":
					addNumber_Duration(params, searchParam, value);
					break;
				case "Quantity":
					addNumber_Quantity(params, searchParam, value);
					break;
				case "integer":
					addNumber_Integer(params, searchParam, value);
					break;
				case "decimal":
					addNumber_Decimal(params, searchParam, value);
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.NUMBER);
	}

	@Override
	public Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamQuantity> extractor = (params, searchParam, value, path) -> {
			String nextType = toTypeName(value);
			switch (nextType) {
				case "Quantity":
					addQuantity_Quantity(theEntity, params, searchParam, value);
					break;
				case "Money":
					addQuantity_Money(theEntity, params, searchParam, value);
					break;
				case "Range":
					addQuantity_Range(theEntity, params, searchParam, value);
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.QUANTITY);
	}

	@Override
	public Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamString> extractor = (params, searchParam, value, path) -> {
			if (value instanceof IPrimitiveType) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) value;
				String valueAsString = nextValue.getValueAsString();
				addSearchTermIfNotBlank(params, searchParam, valueAsString);
				return;
			}

			String nextType = toTypeName(value);
			switch (nextType) {
				case "HumanName":
					addString_HumanName(params, searchParam, value);
					break;
				case "Address":
					addString_Address(params, searchParam, value);
					break;
				case "ContactPoint":
					addString_ContactPoint(params, searchParam, value);
					break;
				case "Quantity":
					addString_Quantity(params, searchParam, value);
					break;
				case "Range":
					addString_Range(params, searchParam, value);
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.STRING);
	}

	private void addUri_Uri(Set<ResourceIndexedSearchParamUri> theParams, RuntimeSearchParam theSearchParam, IBase theValue) {
		IPrimitiveType<?> value = (IPrimitiveType<?>) theValue;
		String valueAsString = value.getValueAsString();
		if (isNotBlank(valueAsString)) {
			ResourceIndexedSearchParamUri nextEntity = new ResourceIndexedSearchParamUri(theSearchParam.getName(), valueAsString);
			theParams.add(nextEntity);
		}
	}

	@Override
	public Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IBaseResource theResource) {
		BaseRuntimeElementCompositeDefinition<?> codeSystemDefinition = (BaseRuntimeElementCompositeDefinition<?>) getContext().getElementDefinition("CodeSystem");
		assert codeSystemDefinition != null;
		BaseRuntimeChildDefinition codeSystemUrlValueChild = codeSystemDefinition.getChildByName("url");

		String resourceTypeName = toTypeName(theResource);
		String useSystem;
		if (resourceTypeName.equals("CodeSystem")) {
			useSystem = extractValueAsString(codeSystemUrlValueChild, theResource);
		} else {
			useSystem = null;
		}

		IExtractor<BaseResourceIndexedSearchParam> extractor = (params, searchParam, value, path) -> {

			if (value instanceof IBaseEnumeration<?>) {
				IBaseEnumeration<?> obj = (IBaseEnumeration<?>) value;
				String system = extractSystem(obj);
				String code = obj.getValueAsString();
				addTokenIfNotBlank(params, searchParam, system, code);
				return;
			}

			if (value instanceof IPrimitiveType) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) value;
				String systemAsString = null;
				String valueAsString = nextValue.getValueAsString();
				if ("CodeSystem.concept.code".equals(path)) {
					systemAsString = useSystem;
				}

				addTokenIfNotBlank(params, searchParam, systemAsString, valueAsString);
				return;
			}

			switch (path) {
				case "Patient.communication":
					addToken_PatientCommunication(params, searchParam, value);
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
					addToken_CapabilityStatementRestSecurity(params, searchParam, value);
					return;
			}

			String nextType = toTypeName(value);
			switch (nextType) {
				case "Identifier":
					addToken_Identifier(params, searchParam, value);
					break;
				case "CodeableConcept":
					addToken_CodeableConcept(params, searchParam, value);
					break;
				case "Coding":
					addToken_Coding(params, searchParam, value);
					break;
				case "ContactPoint":
					addToken_ContactPoint(params, searchParam, value);
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.TOKEN);
	}

	private void addTokenIfNotBlank(Set<BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theSystem, String theValue) {
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
			nextEntity = new ResourceIndexedSearchParamToken(theSearchParam.getName(), system, value);
			theParams.add(nextEntity);
		}
	}

	@Override
	public Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IBaseResource theResource) {
		IExtractor<ResourceIndexedSearchParamUri> extractor = (params, searchParam, value, path) -> {
			String nextType = toTypeName(value);
			switch (nextType) {
				case "uri":
				case "url":
					addUri_Uri(params, searchParam, value);
					break;
				default:
					throw new ConfigurationException("Search param " + searchParam.getName() + " is of unexpected datatype: " + value.getClass());
			}
		};

		return extractSearchParams(theResource, extractor, RestSearchParameterTypeEnum.URI);
	}

	@SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
	private void addSearchTermIfNotBlank(Set<? extends BaseResourceIndexedSearchParam> theParams, RuntimeSearchParam theSearchParam, String theValue) {
		if (isNotBlank(theValue)) {
			if (theValue.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
				theValue = theValue.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
			}

			String searchParamName = theSearchParam.getName();
			ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getModelConfig(), searchParamName, StringNormalizer.normalizeString(theValue), theValue);

			Set params = theParams;
			params.add(nextEntity);
		}
	}

	private void addStringParam(ResourceTable theEntity, Set<BaseResourceIndexedSearchParam> retVal, RuntimeSearchParam nextSpDef, String value) {
		if (value.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			value = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}
		ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getModelConfig(), nextSpDef.getName(), StringNormalizer.normalizeString(value), value);
		nextEntity.setResource(theEntity);
		retVal.add(nextEntity);
	}

	protected String[] split(String thePaths) {
		if (getContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
			if (!thePaths.contains("|")) {
				return new String[]{thePaths};
			}
			return ASPLIT_R4.split(thePaths);
		} else {
			if (!thePaths.contains("|") && !thePaths.contains(" or ")) {
				return new String[]{thePaths};
			}
			return ASPLIT.split(thePaths);
		}
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


	@FunctionalInterface
	public interface IValueExtractor {

		List<? extends IBase> get() throws FHIRException;

	}

	@FunctionalInterface
	private interface IExtractor<T> {


		void extract(Set<T> theParams, RuntimeSearchParam theSearchParam, IBase theValue, String thePath);

	}

	private static void addIgnoredType(FhirContext theCtx, String theType, Set<Class<?>> theIgnoredTypes) {
		BaseRuntimeElementDefinition<?> elementDefinition = theCtx.getElementDefinition(theType);
		if (elementDefinition != null) {
			theIgnoredTypes.add(elementDefinition.getImplementingClass());
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

	private static Integer extractValueAsInteger(BaseRuntimeChildDefinition theChildDefinition, IBase theElement) {
		return theChildDefinition
			.getAccessor()
			.<IPrimitiveType<Integer>>getFirstValueOrNull(theElement)
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
