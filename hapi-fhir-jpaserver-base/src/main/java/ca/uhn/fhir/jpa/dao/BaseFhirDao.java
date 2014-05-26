package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;

public abstract class BaseFhirDao {
	private FhirContext myContext = new FhirContext();
	@PersistenceContext(name = "FHIR_UT", type = PersistenceContextType.TRANSACTION, unitName = "FHIR_UT")
	private EntityManager myEntityManager;
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;

	private Map<Class<? extends IResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	public FhirContext getContext() {
		return myContext;
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	protected ResourceTable updateEntity(final IResource theResource, ResourceTable entity, boolean theUpdateHistory) {
		if (entity.getPublished() == null) {
			entity.setPublished(new Date());
		}

		if (theUpdateHistory) {
			final ResourceHistoryTable historyEntry = entity.toHistory(getContext());
			myEntityManager.persist(historyEntry);
		}

		entity.setVersion(entity.getVersion() + 1);
		theResource.setId(new IdDt(entity.getResourceType(), entity.getId().toString(), Long.toString(entity.getVersion())));

		final List<ResourceIndexedSearchParamString> stringParams = extractSearchParamStrings(entity, theResource);
		final List<ResourceIndexedSearchParamToken> tokenParams = extractSearchParamTokens(entity, theResource);
		final List<ResourceIndexedSearchParamNumber> numberParams = extractSearchParamNumber(entity, theResource);
		final List<ResourceIndexedSearchParamDate> dateParams = extractSearchParamDates(entity, theResource);
		final List<ResourceLink> links = extractResourceLinks(entity, theResource);

		populateResourceIntoEntity(theResource, entity);

		entity.setUpdated(new Date());

		if (entity.getId() == null) {
			myEntityManager.persist(entity);
		} else {
			entity = myEntityManager.merge(entity);
		}

		if (entity.isParamsStringPopulated()) {
			for (ResourceIndexedSearchParamString next : entity.getParamsString()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamString next : stringParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsTokenPopulated()) {
			for (ResourceIndexedSearchParamToken next : entity.getParamsToken()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamToken next : tokenParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsNumberPopulated()) {
			for (ResourceIndexedSearchParamNumber next : entity.getParamsNumber()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamNumber next : numberParams) {
			myEntityManager.persist(next);
		}

		if (entity.isParamsDatePopulated()) {
			for (ResourceIndexedSearchParamDate next : entity.getParamsDate()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceIndexedSearchParamDate next : dateParams) {
			myEntityManager.persist(next);
		}

		if (entity.isHasLinks()) {
			for (ResourceLink next : entity.getResourceLinks()) {
				myEntityManager.remove(next);
			}
		}
		for (ResourceLink next : links) {
			myEntityManager.persist(next);
		}

		return entity;
	}

	protected List<ResourceLink> extractResourceLinks(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceLink> retVal = new ArrayList<ResourceLink>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.REFERENCE) {
				continue;
			}

			String nextPath = nextSpDef.getPath();

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				if (nextObject == null) {
					continue;
				}

				ResourceLink nextEntity;
				if (nextObject instanceof ResourceReferenceDt) {
					ResourceReferenceDt nextValue = (ResourceReferenceDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}

					String typeString = nextValue.getResourceId().getResourceType();
					if (isBlank(typeString)) {
						continue;
					}
					Class<? extends IResource> type = getContext().getResourceDefinition(typeString).getImplementingClass();
					String id = nextValue.getResourceId().getUnqualifiedId();
					if (StringUtils.isBlank(id)) {
						continue;
					}

					IFhirResourceDao<?> dao = getDao(type);
					if (dao == null) {
						throw new InvalidRequestException("This server is not able to handle resources of type: " + nextValue.getResourceId().getResourceType());
					}
					Long valueOf;
					try {
						valueOf = Long.valueOf(id);
					} catch (Exception e) {
						String resName = getContext().getResourceDefinition(type).getName();
						throw new InvalidRequestException("Resource ID " + resName + "/" + id + " is invalid (must be numeric), specified in path: " + nextPath);
					}
					ResourceTable target = myEntityManager.find(ResourceTable.class, valueOf);
					if (target == null) {
						String resName = getContext().getResourceDefinition(type).getName();
						throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPath);
					}
					nextEntity = new ResourceLink(nextPath, theEntity, target);
				} else {
					if (!multiType) {
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}
				if (nextEntity != null) {
					retVal.add(nextEntity);
				}
			}
		}

		theEntity.setHasLinks(retVal.size() > 0);

		return retVal;
	}

	protected List<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamDate> retVal = new ArrayList<ResourceIndexedSearchParamDate>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.DATE) {
				continue;
			}

			String nextPath = nextSpDef.getPath();

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
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

	protected ArrayList<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamNumber> retVal = new ArrayList<ResourceIndexedSearchParamNumber>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.NUMBER && nextSpDef.getParamType() != SearchParamTypeEnum.QUANTITY) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
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
					ResourceIndexedSearchParamNumber nextEntity = new ResourceIndexedSearchParamNumber(resourceName, nextValue.getValue().getValue(), nextValue.getSystem().getValueAsString(), nextValue.getUnits().getValue());
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

	protected List<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamString> retVal = new ArrayList<ResourceIndexedSearchParamString>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.STRING) {
				continue;
			}
			if (nextSpDef.getPath().isEmpty()) {
				continue; // TODO: implement phoenetic, and any others that have
							// no path
			}

			String nextPath = nextSpDef.getPath();
			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
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
					ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextValue.getValueAsString()), nextValue.getValueAsString());
					nextEntity.setResource(theEntity);
					retVal.add(nextEntity);
				} else {
					if (nextObject instanceof HumanNameDt) {
						ArrayList<StringDt> allNames = new ArrayList<>();
						HumanNameDt nextHumanName = (HumanNameDt) nextObject;
						allNames.addAll(nextHumanName.getFamily());
						allNames.addAll(nextHumanName.getGiven());
						for (StringDt nextName : allNames) {
							if (nextName.isEmpty()) {
								continue;
							}
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof AddressDt) {
						ArrayList<StringDt> allNames = new ArrayList<>();
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
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextName.getValueAsString()), nextName.getValueAsString());
							nextEntity.setResource(theEntity);
							retVal.add(nextEntity);
						}
					} else if (nextObject instanceof ContactDt) {
						ContactDt nextContact = (ContactDt) nextObject;
						if (nextContact.getValue().isEmpty() == false) {
							ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(resourceName, normalizeString(nextContact.getValue().getValueAsString()), nextContact.getValue().getValueAsString());
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

	protected List<ResourceIndexedSearchParamToken> extractSearchParamTokens(ResourceTable theEntity, IResource theResource) {
		ArrayList<ResourceIndexedSearchParamToken> retVal = new ArrayList<ResourceIndexedSearchParamToken>();

		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		FhirTerser t = getContext().newTerser();
		for (RuntimeSearchParam nextSpDef : def.getSearchParams()) {
			if (nextSpDef.getParamType() != SearchParamTypeEnum.TOKEN) {
				continue;
			}

			String nextPath = nextSpDef.getPath();
			if (nextPath.isEmpty()) {
				continue;
			}

			boolean multiType = false;
			if (nextPath.endsWith("[x]")) {
				multiType = true;
			}

			List<Object> values = t.getValues(theResource, nextPath);
			for (Object nextObject : values) {
				ResourceIndexedSearchParamToken nextEntity;
				if (nextObject instanceof IdentifierDt) {
					IdentifierDt nextValue = (IdentifierDt) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), nextValue.getSystem().getValueAsString(), nextValue.getValue().getValue());
				} else if (nextObject instanceof IPrimitiveDatatype<?>) {
					IPrimitiveDatatype<?> nextValue = (IPrimitiveDatatype<?>) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), null, nextValue.getValueAsString());
				} else if (nextObject instanceof CodeableConceptDt) {
					CodeableConceptDt nextCC = (CodeableConceptDt) nextObject;
					for (CodingDt nextCoding : nextCC.getCoding()) {
						if (nextCoding.isEmpty()) {
							continue;
						}
						nextEntity = new ResourceIndexedSearchParamToken(nextSpDef.getName(), nextCoding.getSystem().getValueAsString(), nextCoding.getCode().getValue());
						nextEntity.setResource(theEntity);
						retVal.add(nextEntity);
					}
					nextEntity = null;
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

		theEntity.setParamsTokenPopulated(retVal.size() > 0);

		return retVal;
	}

	protected IFhirResourceDao<? extends IResource> getDao(Class<? extends IResource> theType) {
		if (myResourceTypeToDao == null) {
			myResourceTypeToDao = new HashMap<>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				myResourceTypeToDao.put(next.getResourceType(), next);
			}
		}

		Map<Class<? extends IResource>, IFhirResourceDao<?>> resourceTypeToDao = myResourceTypeToDao;
		return resourceTypeToDao.get(theType);
	}

	protected String normalizeString(String theString) {
		char[] out = new char[theString.length()];
		theString = Normalizer.normalize(theString, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = theString.length(); i < n; ++i) {
			char c = theString.charAt(i);
			if (c <= '\u007F') {
				out[j++] = c;
			}
		}
		return new String(out).toUpperCase();
	}

	protected void populateResourceIntoEntity(IResource theResource, ResourceTable theEntity) {

		theEntity.setResourceType(toResourceName(theResource));
		theEntity.setResource(getContext().newJsonParser().encodeResourceToString(theResource));
		theEntity.setEncoding(EncodingEnum.JSON);

		TagList tagList = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (tagList != null) {
			for (Tag next : tagList) {
				theEntity.addTag(next.getTerm(), next.getLabel(), next.getScheme());
			}
		}

	}

	protected ResourceTable toEntity(IResource theResource) {
		ResourceTable retVal = new ResourceTable();

		populateResourceIntoEntity(theResource, retVal);

		return retVal;
	}

	protected String toResourceName(IResource theResource) {
		return myContext.getResourceDefinition(theResource).getName();
	}

}
