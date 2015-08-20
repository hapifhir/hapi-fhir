package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.apache.commons.lang3.tuple.Pair;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseHumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.DurationDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestSecurity;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Patient.Communication;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;

public class SearchParamExtractorDstu2 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	public SearchParamExtractorDstu2(FhirContext theContext) {
		super(theContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamDates(ca.uhn.fhir.jpa.entity.ResourceTable,
	 * ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public List<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamDate> retVal = new ArrayList<ResourceIndexedSearchParamDate>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
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
				if (nextObject instanceof BaseDateTimeDt) {
					BaseDateTimeDt nextValue = (BaseDateTimeDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getValue(), nextValue.getValue());
				} else if (nextObject instanceof PeriodDt) {
					PeriodDt nextValue = (PeriodDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getStart(), nextValue.getEnd());
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

		theEntity.setParamsDatePopulated(retVal.size() > 0);

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamNumber(ca.uhn.fhir.jpa.entity.ResourceTable,
	 * ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public ArrayList<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamNumber> retVal = new ArrayList<ResourceIndexedSearchParamNumber>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.NUMBER) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IDatatype) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof DurationDt) {
					DurationDt nextValue = (DurationDt) nextObject;
					if (nextValue.getValueElement().isEmpty()) {
						continue;
					}

					if (new UriDt(BaseHapiFhirDao.UCUM_NS).equals(nextValue.getSystemElement())) {
						if (isNotBlank(nextValue.getCode())) {

							Unit<? extends Quantity> unit = Unit.valueOf(nextValue.getCode());
							javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
							double dayValue = dayConverter.convert(nextValue.getValue().doubleValue());
							DurationDt newValue = new DurationDt();
							newValue.setSystem(BaseHapiFhirDao.UCUM_NS);
							newValue.setCode(NonSI.DAY.toString());
							newValue.setValue(dayValue);
							nextValue = newValue;

							/*
							 * @SuppressWarnings("unchecked") PhysicsUnit<? extends
							 * org.unitsofmeasurement.quantity.Quantity<?>> unit = (PhysicsUnit<? extends
							 * org.unitsofmeasurement.quantity.Quantity<?>>)
							 * UCUMFormat.getCaseInsensitiveInstance().parse(nextValue.getCode().getValue(), null); if
							 * (unit.isCompatible(UCUM.DAY)) {
							 * 
							 * @SuppressWarnings("unchecked") PhysicsUnit<org.unitsofmeasurement.quantity.Time> timeUnit
							 * = (PhysicsUnit<Time>) unit; UnitConverter conv = timeUnit.getConverterTo(UCUM.DAY);
							 * double dayValue = conv.convert(nextValue.getValue().getValue().doubleValue()); DurationDt
							 * newValue = new DurationDt(); newValue.setSystem(UCUM_NS);
							 * newValue.setCode(UCUM.DAY.getSymbol()); newValue.setValue(dayValue); nextValue=newValue;
							 * }
							 */
						}
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof QuantityDt) {
					QuantityDt nextValue = (QuantityDt) nextObject;
					if (nextValue.getValueElement().isEmpty()) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof IntegerDt) {
					IntegerDt nextValue = (IntegerDt) nextObject;
					if (nextValue.getValue() == null) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, new BigDecimal(nextValue.getValue()));
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

		theEntity.setParamsNumberPopulated(retVal.size() > 0);

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamQuantity(ca.uhn.fhir.jpa.entity.ResourceTable,
	 * ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public List<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamQuantity> retVal = new ArrayList<ResourceIndexedSearchParamQuantity>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.QUANTITY) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IDatatype) nextObject).isEmpty()) {
					continue;
				}

				String resourceName = nextSpDef.getName();
				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof QuantityDt) {
					QuantityDt nextValue = (QuantityDt) nextObject;
					if (nextValue.getValueElement().isEmpty()) {
						continue;
					}

					ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(resourceName, nextValue.getValueElement().getValue(), nextValue.getSystemElement().getValueAsString(), nextValue.getCode());
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

		theEntity.setParamsNumberPopulated(retVal.size() > 0);

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamStrings(ca.uhn.fhir.jpa.entity.ResourceTable,
	 * ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public List<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamString> retVal = new ArrayList<ResourceIndexedSearchParamString>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.STRING) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			String resourceName = nextSpDef.getName();
			
			if (isBlank(nextPath)) {
				
				// TODO: implement phonetic, and any others that have no path
				
				if ("Questionnaire".equals(def.getName()) && nextSpDef.getName().equals("title")) {
					Questionnaire q = (Questionnaire) theResource;
					String title = q.getGroup().getTitle();
					addSearchTerm(theEntity, retVal, resourceName, title);
				}
				continue;
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null || ((IDatatype) nextObject).isEmpty()) {
					continue;
				}

				boolean multiType = false;
				if (nextPath.endsWith("[x]")) {
					multiType = true;
				}

				if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					String searchTerm = nextValue.getValueAsString();
					addSearchTerm(theEntity, retVal, resourceName, searchTerm);
				} else {
					if (nextObject instanceof BaseHumanNameDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						HumanNameDt nextHumanName = (HumanNameDt) nextObject;
						allNames.addAll(nextHumanName.getFamily());
						allNames.addAll(nextHumanName.getGiven());
						for (StringDt nextName : allNames) {
							addSearchTerm(theEntity, retVal, resourceName, nextName.getValue());
						}
					} else if (nextObject instanceof AddressDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						AddressDt nextAddress = (AddressDt) nextObject;
						allNames.addAll(nextAddress.getLine());
						allNames.add(nextAddress.getCityElement());
						allNames.add(nextAddress.getStateElement());
						allNames.add(nextAddress.getCountryElement());
						allNames.add(nextAddress.getPostalCodeElement());
						for (StringDt nextName : allNames) {
							addSearchTerm(theEntity, retVal, resourceName, nextName.getValue());
						}
					} else if (nextObject instanceof ContactPointDt) {
						ContactPointDt nextContact = (ContactPointDt) nextObject;
						if (nextContact.getValueElement().isEmpty() == false) {
							addSearchTerm(theEntity, retVal, resourceName, nextContact.getValue());
						}
					} else {
						if (!multiType) {
							throw new ConfigurationException("Search param " + resourceName + " is of unexpected datatype: " + nextObject.getClass());
						}
					}
				}
			}
		}

		theEntity.setParamsStringPopulated(retVal.size() > 0);

		return retVal;
	}

	private void addSearchTerm(ResourceTable theEntity, ArrayList<ResourceIndexedSearchParamString> retVal, String resourceName, String searchTerm) {
		if (isBlank(searchTerm)) {
			return;
		}
		if (searchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			searchTerm = searchTerm.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}

		ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, BaseHapiFhirDao.normalizeString(searchTerm), searchTerm);
		nextEntity.setResource(theEntity);
		retVal.add(nextEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.jpa.dao.ISearchParamExtractor#extractSearchParamTokens(ca.uhn.fhir.jpa.entity.ResourceTable,
	 * ca.uhn.fhir.model.api.IResource)
	 */
	@Override
	public List<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		ArrayList<BaseResourceIndexedSearchParam> retVal = new ArrayList<BaseResourceIndexedSearchParam>();

		String useSystem = null;
		if (theResource instanceof ValueSet) {
			ValueSet vs = (ValueSet) theResource;
			useSystem = vs.getCodeSystem().getSystem();
		}
		
		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.TOKEN) {
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

			List<String> systems = new ArrayList<String>();
			List<String> codes = new ArrayList<String>();

			String needContactPointSystem = null;
			if (nextPath.endsWith("(system=phone)")) {
				nextPath = nextPath.substring(0, nextPath.length() - "(system=phone)".length());
				needContactPointSystem = "phone";
			}
			if (nextPath.endsWith("(system=email)")) {
				nextPath = nextPath.substring(0, nextPath.length() - "(system=email)".length());
				needContactPointSystem = "email";
			}

			for (Object nextObject : extractValues(nextPath, theResource)) {
				
				// Patient:language 
				if (nextObject instanceof Patient.Communication) {
					Communication nextValue = (Patient.Communication) nextObject;
					nextObject= nextValue.getLanguage();
				}
				
				if (nextObject instanceof IdentifierDt) {
					IdentifierDt nextValue = (IdentifierDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(nextValue.getSystemElement().getValueAsString());
					codes.add(nextValue.getValueElement().getValue());
				} else if (nextObject instanceof ContactPointDt) {
						ContactPointDt nextValue = (ContactPointDt) nextObject;
						if (nextValue.isEmpty()) {
							continue;
						}
						if (isNotBlank(needContactPointSystem)) {
							if (!needContactPointSystem.equals(nextValue.getSystemElement().getValueAsString())) {
								continue;
							}
						}
						systems.add(nextValue.getSystemElement().getValueAsString());
						codes.add(nextValue.getValueElement().getValue());
				} else if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					if ("ValueSet.codeSystem.concept.code".equals(nextPath)) {
						systems.add(useSystem);
					} else {
						systems.add(null);
					}
					codes.add(nextValue.getValueAsString());
				} else if (nextObject instanceof CodingDt) {
					CodingDt nextValue = (CodingDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					String nextSystem = nextValue.getSystemElement().getValueAsString();
					String nextCode = nextValue.getCodeElement().getValue();
					if (isNotBlank(nextSystem) || isNotBlank(nextCode)) {
						systems.add(nextSystem);
						codes.add(nextCode);
					}

					if (!nextValue.getDisplayElement().isEmpty()) {
						systems.add(null);
						codes.add(nextValue.getDisplayElement().getValue());
					}
				} else if (nextObject instanceof CodeableConceptDt) {
					CodeableConceptDt nextCC = (CodeableConceptDt) nextObject;
					if (!nextCC.getTextElement().isEmpty()) {
						String value = nextCC.getTextElement().getValue();
						if (value.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
							value = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
						}
						ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(nextSpDef.getName(), BaseHapiFhirDao.normalizeString(value), value);
						nextEntity.setResource(theEntity);
						retVal.add(nextEntity);
					}

					extractTokensFromCodeableConcept(systems, codes, nextCC);
				} else if (nextObject instanceof RestSecurity) {
					// Conformance.security search param points to something kind of useless right now - This should probably be fixed.
					RestSecurity sec = (RestSecurity)nextObject;
					for (BoundCodeableConceptDt<RestfulSecurityServiceEnum> nextCC : sec.getService()) {
						extractTokensFromCodeableConcept(systems, codes, nextCC);
					}
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
			}

			assert systems.size() == codes.size() : "Systems contains " + systems + ", codes contains: " + codes;

			Set<Pair<String, String>> haveValues = new HashSet<Pair<String, String>>();
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

		theEntity.setParamsTokenPopulated(retVal.size() > 0);

		return retVal;
	}

	private void extractTokensFromCodeableConcept(List<String> systems, List<String> codes, CodeableConceptDt nextCC) {
		for (CodingDt nextCoding : nextCC.getCoding()) {
			if (nextCoding.isEmpty()) {
				continue;
			}

			String nextSystem = nextCoding.getSystemElement().getValueAsString();
			String nextCode = nextCoding.getCodeElement().getValue();
			if (isNotBlank(nextSystem) || isNotBlank(nextCode)) {
				systems.add(nextSystem);
				codes.add(nextCode);
			}

			if (!nextCoding.getDisplayElement().isEmpty()) {
				systems.add(null);
				codes.add(nextCoding.getDisplayElement().getValue());
			}

		}
	}

}
