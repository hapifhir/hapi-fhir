package ca.uhn.fhir.jpa.dao.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestSecurityComponent;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Location.LocationPositionComponent;
import org.hl7.fhir.r4.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.springframework.beans.factory.annotation.Autowired;

import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class SearchParamExtractorR4 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorR4.class);
	@Autowired
	private org.hl7.fhir.r4.hapi.ctx.IValidationSupport myValidationSupport;

	/**
	 * Constructor
	 */
	public SearchParamExtractorR4() {
		super();
	}

	public SearchParamExtractorR4(DaoConfig theDaoConfig, FhirContext theCtx, IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry) {
		super(theDaoConfig, theCtx, theSearchParamRegistry);
		myValidationSupport = theValidationSupport;
	}

	private void addQuantity(ResourceTable theEntity, HashSet<ResourceIndexedSearchParamQuantity> retVal, String resourceName, Quantity nextValue) {
		if (!nextValue.getValueElement().isEmpty()) {
			BigDecimal nextValueValue = nextValue.getValueElement().getValue();
			String nextValueString = nextValue.getSystemElement().getValueAsString();
			String nextValueCode = nextValue.getCode();
			ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(resourceName, nextValueValue, nextValueString, nextValueCode);
			nextEntity.setResource(theEntity);
			retVal.add(nextEntity);
		}
	}

	private void addSearchTerm(ResourceTable theEntity, Set<ResourceIndexedSearchParamString> retVal, String resourceName, String searchTerm) {
		if (isBlank(searchTerm)) {
			return;
		}
		if (searchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			searchTerm = searchTerm.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}

		ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getDaoConfig(), resourceName, BaseHapiFhirDao.normalizeString(searchTerm), searchTerm);
		nextEntity.setResource(theEntity);
		retVal.add(nextEntity);
	}

	private void addStringParam(ResourceTable theEntity, Set<BaseResourceIndexedSearchParam> retVal, RuntimeSearchParam nextSpDef, String value) {
		if (value.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			value = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}
		ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getDaoConfig(), nextSpDef.getName(), BaseHapiFhirDao.normalizeString(value), value);
		nextEntity.setResource(theEntity);
		retVal.add(nextEntity);
	}

	@Override
	public List<PathAndRef> extractResourceLinks(IBaseResource theResource, RuntimeSearchParam theNextSpDef) {
		ArrayList<PathAndRef> retVal = new ArrayList<>();

		String[] nextPathsSplit = SPLIT_R4.split(theNextSpDef.getPath());
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

	@Override
	public Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IBaseResource theResource) {
		// TODO: implement
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamDates(ca.uhn.fhir.jpa.entity.ResourceTable, ca.uhn.fhir.model.api.IBaseResource)
	 */
	@Override
	public Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<ResourceIndexedSearchParamDate> retVal = new HashSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.DATE) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null) {
					continue;
				}

				ResourceIndexedSearchParamDate nextEntity;
				if (nextObject instanceof BaseDateTimeType) {
					BaseDateTimeType nextValue = (BaseDateTimeType) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getValue(), nextValue.getValue(), nextValue.getValueAsString());
				} else if (nextObject instanceof Period) {
					Period nextValue = (Period) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getStart(), nextValue.getEnd(), nextValue.getStartElement().getValueAsString());
				} else if (nextObject instanceof Timing) {
					Timing nextValue = (Timing) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					TreeSet<Date> dates = new TreeSet<>();
					String firstValue = null;
					for (DateTimeType nextEvent : nextValue.getEvent()) {
						if (nextEvent.getValue() != null) {
							dates.add(nextEvent.getValue());
							if (firstValue == null) {
								firstValue = nextEvent.getValueAsString();
							}
						}
					}
					if (dates.isEmpty()) {
						continue;
					}

					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), dates.first(), dates.last(), firstValue);
				} else if (nextObject instanceof StringType) {
					// CarePlan.activitydate can be a string
					continue;
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
				if (nextEntity != null) {
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				}
			}
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamNumber(ca.uhn.fhir.jpa.entity.ResourceTable, ca.uhn.fhir.model.api.IBaseResource)
	 */
	@Override
	public HashSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<ResourceIndexedSearchParamNumber> retVal = new HashSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.NUMBER) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IBase) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof Duration) {
					Duration nextValue = (Duration) nextObject;
					if (nextValue.getValueElement().isEmpty()) {
						continue;
					}

					if (BaseHapiFhirDao.UCUM_NS.equals(nextValue.getSystem())) {
						if (isNotBlank(nextValue.getCode())) {

							Unit<? extends javax.measure.quantity.Quantity> unit = Unit.valueOf(nextValue.getCode());
							javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
							double dayValue = dayConverter.convert(nextValue.getValue().doubleValue());
							Duration newValue = new Duration();
							newValue.setSystem(BaseHapiFhirDao.UCUM_NS);
							newValue.setCode(NonSI.DAY.toString());
							newValue.setValue(dayValue);
							nextValue = newValue;

							/*
							 * @SuppressWarnings("unchecked") PhysicsUnit<? extends org.unitsofmeasurement.quantity.Quantity<?>> unit = (PhysicsUnit<? extends org.unitsofmeasurement.quantity.Quantity<?>>)
							 * UCUMFormat.getCaseInsensitiveInstance().parse(nextValue.getCode().getValue(), null); if (unit.isCompatible(UCUM.DAY)) {
							 *
							 * @SuppressWarnings("unchecked") PhysicsUnit<org.unitsofmeasurement.quantity.Time> timeUnit = (PhysicsUnit<Time>) unit; UnitConverter conv = timeUnit.getConverterTo(UCUM.DAY);
							 * double dayValue = conv.convert(nextValue.getValue().getValue().doubleValue()); Duration newValue = new Duration(); newValue.setSystem(UCUM_NS);
							 * newValue.setCode(UCUM.DAY.getSymbol()); newValue.setValue(dayValue); nextValue=newValue; }
							 */
						}
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof Quantity) {
					Quantity nextValue = (Quantity) nextObject;
					if (nextValue.getValueElement().isEmpty()) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof IntegerType) {
					IntegerType nextValue = (IntegerType) nextObject;
					if (nextValue.getValue() == null) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, new BigDecimal(nextValue.getValue()));
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof DecimalType) {
					DecimalType nextValue = (DecimalType) nextObject;
					if (nextValue.getValue() == null) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamQuantity(ca.uhn.fhir.jpa.entity.ResourceTable, ca.uhn.fhir.model.api.IBaseResource)
	 */
	@Override
	public Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<ResourceIndexedSearchParamQuantity> retVal = new HashSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.QUANTITY) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IBase) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof Quantity) {
					Quantity nextValue = (Quantity) nextObject;
					addQuantity(theEntity, retVal, resourceName, nextValue);
				} else if (nextObject instanceof Range) {
					Range nextValue = (Range) nextObject;
					addQuantity(theEntity, retVal, resourceName, nextValue.getLow());
					addQuantity(theEntity, retVal, resourceName, nextValue.getHigh());
				} else if (nextObject instanceof LocationPositionComponent) {
					continue;
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamStrings(ca.uhn.fhir.jpa.entity.ResourceTable, ca.uhn.fhir.model.api.IBaseResource)
	 */
	@Override
	public Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<ResourceIndexedSearchParamString> retVal = new HashSet<>();

		String resourceName = getContext().getResourceDefinition(theResource).getName();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.STRING) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			String nextSpName = nextSpDef.getName();

			if (isBlank(nextPath)) {

				// // TODO: implement phonetic, and any others that have no path
				//
				// // TODO: do we still need this check?
				// if ("Questionnaire".equals(nextSpName) && nextSpDef.getName().equals("title")) {
				// Questionnaire q = (Questionnaire) theResource;
				// String title = "";// q.getGroup().getTitle();
				// addSearchTerm(theEntity, retVal, nextSpName, title);
				// }

				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IBase) nextObject).isEmpty()) {
					continue;
				}

				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof IPrimitiveType<?>) {
					IPrimitiveType<?> nextValue = (IPrimitiveType<?>) nextObject;
					String searchTerm = nextValue.getValueAsString();
					addSearchTerm(theEntity, retVal, nextSpName, searchTerm);
				} else {
					if (nextObject instanceof HumanName) {
						ArrayList<StringType> allNames = new ArrayList<>();
						HumanName nextHumanName = (HumanName) nextObject;
						if (isNotBlank(nextHumanName.getFamily())) {
							allNames.add(nextHumanName.getFamilyElement());
						}
						allNames.addAll(nextHumanName.getGiven());
						for (StringType nextName : allNames) {
							addSearchTerm(theEntity, retVal, nextSpName, nextName.getValue());
						}
					} else if (nextObject instanceof Address) {
						ArrayList<StringType> allNames = new ArrayList<>();
						Address nextAddress = (Address) nextObject;
						allNames.addAll(nextAddress.getLine());
						allNames.add(nextAddress.getCityElement());
						allNames.add(nextAddress.getStateElement());
						allNames.add(nextAddress.getCountryElement());
						allNames.add(nextAddress.getPostalCodeElement());
						for (StringType nextName : allNames) {
							addSearchTerm(theEntity, retVal, nextSpName, nextName.getValue());
						}
					} else if (nextObject instanceof ContactPoint) {
						ContactPoint nextContact = (ContactPoint) nextObject;
						if (nextContact.getValueElement().isEmpty() == false) {
							addSearchTerm(theEntity, retVal, nextSpName, nextContact.getValue());
						}
					} else if (nextObject instanceof Quantity) {
						BigDecimal value = ((Quantity) nextObject).getValue();
						if (value != null) {
							addSearchTerm(theEntity, retVal, nextSpName, value.toPlainString());
						}
					} else if (nextObject instanceof Range) {
						Quantity low = ((Range) nextObject).getLow();
						if (low != null) {
							BigDecimal value = low.getValue();
							if (value != null) {
								addSearchTerm(theEntity, retVal, nextSpName, value.toPlainString());
							}
						}
					} else {
						if (!multiType) {
							throw new ConfigurationException("Search param " + nextSpName + " is of unexpected datatype: " + nextObject.getClass());
						}
					}
				}
			}
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamTokens(ca.uhn.fhir.jpa.entity.ResourceTable, ca.uhn.fhir.model.api.IBaseResource)
	 */
	@Override
	public Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<BaseResourceIndexedSearchParam> retVal = new HashSet<>();

		String useSystem = null;
		if (theResource instanceof CodeSystem) {
			CodeSystem cs = (CodeSystem) theResource;
			useSystem = cs.getUrl();
		}

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.TOKEN) {
				continue;
			}

			String resourceType = theEntity.getResourceType();
			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<String> systems = new ArrayList<>();
			List<String> codes = new ArrayList<>();

			for (Object nextObject : extractValues(nextPath, theResource)) {

				if (nextObject == null) {
					continue;
				}

				// Patient:language
				if (nextObject instanceof PatientCommunicationComponent) {
					PatientCommunicationComponent nextValue = (PatientCommunicationComponent) nextObject;
					nextObject = nextValue.getLanguage();
				}

				if (nextObject instanceof Identifier) {
					Identifier nextValue = (Identifier) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					String system = StringUtils.defaultIfBlank(nextValue.getSystemElement().getValueAsString(), null);
					String value = nextValue.getValueElement().getValue();
					if (isNotBlank(value)) {
						systems.add(system);
						codes.add(value);
					}

					if (isNotBlank(nextValue.getType().getText())) {
						addStringParam(theEntity, retVal, nextSpDef, nextValue.getType().getText());
					}

				} else if (nextObject instanceof ContactPoint) {
					ContactPoint nextValue = (ContactPoint) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(nextValue.getSystemElement().getValueAsString());
					codes.add(nextValue.getValueElement().getValue());
				} else if (nextObject instanceof Enumeration<?>) {
					Enumeration<?> obj = (Enumeration<?>) nextObject;
					String system = extractSystem(obj);
					String code = obj.getValueAsString();
					if (isNotBlank(code)) {
						systems.add(system);
						codes.add(code);
					}
				} else if (nextObject instanceof IPrimitiveType<?>) {
					IPrimitiveType<?> nextValue = (IPrimitiveType<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					if ("CodeSystem.concept.code".equals(nextPath)) {
						systems.add(useSystem);
					} else {
						systems.add(null);
					}
					codes.add(nextValue.getValueAsString());
				} else if (nextObject instanceof Coding) {
					Coding nextValue = (Coding) nextObject;
					extractTokensFromCoding(systems, codes, theEntity, retVal, nextSpDef, nextValue);
				} else if (nextObject instanceof CodeableConcept) {
					CodeableConcept nextCC = (CodeableConcept) nextObject;
					if (!nextCC.getTextElement().isEmpty()) {
						addStringParam(theEntity, retVal, nextSpDef, nextCC.getTextElement().getValue());
					}

					extractTokensFromCodeableConcept(systems, codes, nextCC, theEntity, retVal, nextSpDef);
				} else if (nextObject instanceof CapabilityStatementRestSecurityComponent) {
					// Conformance.security search param points to something kind of useless right now - This should probably
					// be fixed.
					CapabilityStatementRestSecurityComponent sec = (CapabilityStatementRestSecurityComponent) nextObject;
					for (CodeableConcept nextCC : sec.getService()) {
						extractTokensFromCodeableConcept(systems, codes, nextCC, theEntity, retVal, nextSpDef);
					}
				} else if (nextObject instanceof LocationPositionComponent) {
					ourLog.warn("Position search not currently supported, not indexing location");
					continue;
				} else if (nextObject instanceof StructureDefinition.StructureDefinitionContextComponent) {
					ourLog.warn("StructureDefinition context indexing not currently supported"); // TODO: implement this
					continue;
				} else if (resourceType.equals("Consent") && nextPath.equals("Consent.source")) {
					// Consent#source-identifier has a path that isn't typed - This is a one-off to deal with that
					continue;
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " with path " + nextPath + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}

			assert systems.size() == codes.size() : "Systems contains " + systems + ", codes contains: " + codes;

			Set<Pair<String, String>> haveValues = new HashSet<>();
			for (int i = 0; i < systems.size(); i++) {
				String system = systems.get(i);
				String code = codes.get(i);
				if (isBlank(system) && isBlank(code)) {
					continue;
				}

				if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
					system = system.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
				}
				if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
					code = code.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
				}

				Pair<String, String> nextPair = Pair.of(system, code);
				if (haveValues.contains(nextPair)) {
					continue;
				}
				haveValues.add(nextPair);

				ResourceIndexedSearchParamToken nextEntity;
				nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), system, code);
				nextEntity.setResource(theEntity);
				retVal.add(nextEntity);

			}

		}

		return retVal;
	}

	@Override
	public Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IBaseResource theResource) {
		HashSet<ResourceIndexedSearchParamUri> retVal = new HashSet<>();

		Collection<RuntimeSearchParam> searchParams = getSearchParams(theResource);
		for (RuntimeSearchParam nextSpDef : searchParams) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.URI) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IBase) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof UriType) {
					UriType nextValue = (UriType) nextObject;
					if (isBlank(nextValue.getValue())) {
						continue;
					}

					ourLog.trace("Adding param: {}, {}", resourceName, nextValue.getValue());

					ResourceIndexedSearchParamUri nextEntity = new ResourceIndexedSearchParamUri(resourceName, nextValue.getValue());

					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}
		}

		return retVal;
	}

	private void extractTokensFromCodeableConcept(List<String> theSystems, List<String> theCodes, CodeableConcept theCodeableConcept, ResourceTable theEntity,
																 Set<BaseResourceIndexedSearchParam> theListToPopulate, RuntimeSearchParam theParameterDef) {
		for (Coding nextCoding : theCodeableConcept.getCoding()) {
			extractTokensFromCoding(theSystems, theCodes, theEntity, theListToPopulate, theParameterDef, nextCoding);
		}
	}

	private void extractTokensFromCoding(List<String> theSystems, List<String> theCodes, ResourceTable theEntity, Set<BaseResourceIndexedSearchParam> theListToPopulate,
													 RuntimeSearchParam theParameterDef, Coding nextCoding) {
		if (nextCoding != null && !nextCoding.isEmpty()) {

			String nextSystem = nextCoding.getSystemElement().getValueAsString();
			String nextCode = nextCoding.getCodeElement().getValue();
			if (isNotBlank(nextSystem) || isNotBlank(nextCode)) {
				theSystems.add(nextSystem);
				theCodes.add(nextCode);
			}

			if (!nextCoding.getDisplayElement().isEmpty()) {
				addStringParam(theEntity, theListToPopulate, theParameterDef, nextCoding.getDisplayElement().getValue());
			}

		}
	}

	/**
	 * Override parent because we're using FHIRPath here
	 */
	@Override
	protected List<Object> extractValues(String thePaths, IBaseResource theResource) {
		IWorkerContext worker = new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(getContext(), myValidationSupport);
		FHIRPathEngine fp = new FHIRPathEngine(worker);
		fp.setHostServices(new SearchParamExtractorR4HostServices());

		List<Object> values = new ArrayList<>();
		String[] nextPathsSplit = SPLIT_R4.split(thePaths);
		for (String nextPath : nextPathsSplit) {
			List<Base> allValues;
			try {
				allValues = fp.evaluate((Base) theResource, nextPath);
			} catch (FHIRException e) {
				String msg = getContext().getLocalizer().getMessage(BaseSearchParamExtractor.class, "failedToExtractPaths", nextPath, e.toString());
				throw new InternalErrorException(msg, e);
			}
			if (allValues.isEmpty() == false) {
				values.addAll(allValues);
			}
		}

		for (int i = 0; i < values.size(); i++) {
			Object nextObject = values.get(i);
			if (nextObject instanceof Extension) {
				Extension nextExtension = (Extension) nextObject;
				nextObject = nextExtension.getValue();
				values.set(i, nextObject);
			}
		}

		return values;
	}

	@VisibleForTesting
	void setValidationSupportForTesting(org.hl7.fhir.r4.hapi.ctx.IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
	}

	private static <T extends Enum<?>> String extractSystem(Enumeration<T> theBoundCode) {
		if (theBoundCode.getValue() != null) {
			return theBoundCode.getEnumFactory().toSystem(theBoundCode.getValue());
		}
		return null;
	}


	private class SearchParamExtractorR4HostServices implements FHIRPathEngine.IEvaluationContext {
		@Override
		public Base resolveConstant(Object appContext, String name) throws PathEngineException {
			return null;
		}

		@Override
		public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
			return null;
		}

		@Override
		public boolean log(String argument, List<Base> focus) {
			return false;
		}

		@Override
		public FunctionDetails resolveFunction(String functionName) {
			return null;
		}

		@Override
		public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
			return null;
		}

		private Map<String, Base> myResourceTypeToStub = Collections.synchronizedMap(new HashMap<>());

		@Override
		public Base resolveReference(Object theAppContext, String theUrl) throws FHIRException {

			/*
			 * When we're doing resolution within the SearchParamExtractor, if we want
			 * to do a resolve() it's just to check the type, so there is no point
			 * going through the heavyweight test. We can just return a stub and
			 * that's good enough since we're just doing something like
			 *    Encounter.patient.where(resolve() is Patient)
			 */
			IdType url = new IdType(theUrl);
			Base retVal = null;
			if (isNotBlank(url.getResourceType())) {

				retVal = myResourceTypeToStub.get(url.getResourceType());
				if (retVal != null) {
					return retVal;
				}

				ResourceType resourceType = ResourceType.fromCode(url.getResourceType());
				if (resourceType != null) {
					retVal = new Resource(){
						@Override
						public Resource copy() {
							return this;
						}

						@Override
						public ResourceType getResourceType() {
							return resourceType;
						}

						@Override
						public String fhirType() {
							return url.getResourceType();
						}

					};
					myResourceTypeToStub.put(url.getResourceType(), retVal);
				}
			}
			return retVal;
		}

		@Override
		public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
			return false;
		}
	}

}
