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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.DurationDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;

class SearchParamExtractorDstu1 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	public SearchParamExtractorDstu1(FhirContext theContext) {
		super(theContext);
	}

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
					nextEntity = new ResourceIndexedSearchParamDate(nextSpDef.getName(), nextValue.getStart().getValue(), nextValue.getEnd().getValue());
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
					if (nextValue.getValue().isEmpty()) {
						continue;
					}

					if (new UriDt(BaseFhirDao.UCUM_NS).equals(nextValue.getSystem())) {
						if (isNotBlank(nextValue.getCode().getValue())) {

							Unit<? extends Quantity> unit = Unit.valueOf(nextValue.getCode().getValue());
							javax.measure.converter.UnitConverter dayConverter = unit.getConverterTo(NonSI.DAY);
							double dayValue = dayConverter.convert(nextValue.getValue().getValue().doubleValue());
							DurationDt newValue = new DurationDt();
							newValue.setSystem(BaseFhirDao.UCUM_NS);
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

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else if (nextObject instanceof QuantityDt) {
					QuantityDt nextValue = (QuantityDt) nextObject;
					if (nextValue.getValue().isEmpty()) {
						continue;
					}

					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue());
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
					if (nextValue.getValue().isEmpty()) {
						continue;
					}

					ResourceIndexedSearchParamQuantity nextEntity = new ResourceIndexedSearchParamQuantity(resourceName, nextValue.getValue().getValue(), nextValue.getSystem().getValueAsString(), nextValue.getUnits().getValue());
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

	@Override
	public List<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamString> retVal = new ArrayList<ResourceIndexedSearchParamString>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.STRING) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (isBlank(nextPath)) {
				// TODO: implement phoenetic, and any others that have no path
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

				if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					String searchTerm = nextValue.getValueAsString();
					if (searchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
						searchTerm = searchTerm.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
					}

					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, BaseFhirDao.normalizeString(searchTerm), searchTerm);
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else {
					if (nextObject instanceof BaseHumanNameDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						HumanNameDt nextHumanName = (HumanNameDt) nextObject;
						allNames.addAll(nextHumanName.getFamily());
						allNames.addAll(nextHumanName.getGiven());
						for (StringDt nextName : allNames) {
							if (nextName.isEmpty()) {
								continue;
							}
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, BaseFhirDao.normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof AddressDt) {
						ArrayList<StringDt> allNames = new ArrayList<StringDt>();
						AddressDt nextAddress = (AddressDt) nextObject;
						allNames.addAll(nextAddress.getLine());
						allNames.add(nextAddress.getCity());
						allNames.add(nextAddress.getState());
						allNames.add(nextAddress.getCountry());
						allNames.add(nextAddress.getZip());
						for (StringDt nextName : allNames) {
							if (nextName.isEmpty()) {
								continue;
							}
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, BaseFhirDao.normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof ContactDt) {
						ContactDt nextContact = (ContactDt) nextObject;
						if (nextContact.getValue().isEmpty() == false) {
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, BaseFhirDao.normalizeString(nextContact.getValue().getValueAsString()), nextContact.getValue().getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
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

	@Override
	public List<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		ArrayList<BaseResourceIndexedSearchParam> retVal = new ArrayList<BaseResourceIndexedSearchParam>();

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

			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject instanceof IdentifierDt) {
					IdentifierDt nextValue = (IdentifierDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(nextValue.getSystem().getValueAsString());
					codes.add(nextValue.getValue().getValue());
				} else if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					systems.add(null);
					codes.add(nextValue.getValueAsString());
				} else if (nextObject instanceof CodeableConceptDt) {
					CodeableConceptDt nextCC = (CodeableConceptDt) nextObject;
					if (!nextCC.getText().isEmpty()) {
						ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(nextSpDef.getName(), BaseFhirDao.normalizeString(nextCC.getText().getValue()), nextCC.getText().getValue());
						nextEntity.setResource(theEntity);
						retVal.add(nextEntity);
					}

					for (CodingDt nextCoding : nextCC.getCoding()) {
						if (nextCoding.isEmpty()) {
							continue;
						}

						String nextSystem = nextCoding.getSystem().getValueAsString();
						String nextCode = nextCoding.getCode().getValue();
						if (isNotBlank(nextSystem) || isNotBlank(nextCode)) {
							systems.add(nextSystem);
							codes.add(nextCode);
						}

						if (!nextCoding.getDisplay().isEmpty()) {
							systems.add(null);
							codes.add(nextCoding.getDisplay().getValue());
						}

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

}
