package ca.uhn.fhir.jpa.term;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.dstu3.hapi.validation.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.UrlUtil;

public class HapiTerminologySvcDstu3 extends BaseHapiTerminologySvc implements IValidationSupport, IHapiTerminologySvcDstu3 {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiTerminologySvcDstu3.class);

	@Autowired
	private IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemResourceDao;

	@Autowired
	private IValidationSupport myValidationSupport;

	private void addCodeIfNotAlreadyAdded(String system, ValueSetExpansionComponent retVal, Set<String> addedCodes, TermConcept nextConcept) {
		if (addedCodes.add(nextConcept.getCode())) {
			ValueSetExpansionContainsComponent contains = retVal.addContains();
			contains.setCode(nextConcept.getCode());
			contains.setSystem(system);
			contains.setDisplay(nextConcept.getDisplay());
		}
	}

	@Override
	protected List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>();
		CodeSystem system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate, List<ConceptDefinitionComponent> conceptList) {
		for (ConceptDefinitionComponent next : conceptList) {
			if (theCode.equals(next.getCode())) {
				addAllChildren(theSystemString, next, theListToPopulate);
			} else {
				findCodesBelow(theSystemString, theCode, theListToPopulate, next.getConcept());
			}
		}
	}

	private void findCodesAbove(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		for (ConceptDefinitionComponent next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, ConceptDefinitionComponent theNext, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (ConceptDefinitionComponent nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	private void addAllChildren(String theSystemString, ConceptDefinitionComponent theCode, List<VersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	@Override
	protected List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>();
		CodeSystem system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void addDisplayFilterExact(QueryBuilder qb, BooleanJunction<?> bool, ConceptSetFilterComponent nextFilter) {
		bool.must(qb.phrase().onField("myDisplay").sentence(nextFilter.getValue()).createQuery());
	}

	private void addDisplayFilterInexact(QueryBuilder qb, BooleanJunction<?> bool, ConceptSetFilterComponent nextFilter) {
		Query textQuery = qb
				.phrase()
				.withSlop(2)
				.onField("myDisplay").boostedTo(4.0f)
				.andField("myDisplayEdgeNGram").boostedTo(2.0f)
				// .andField("myDisplayNGram").boostedTo(1.0f)
				// .andField("myDisplayPhonetic").boostedTo(0.5f)
				.sentence(nextFilter.getValue().toLowerCase()).createQuery();
		bool.must(textQuery);
	}

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		String system = theInclude.getSystem();
		ourLog.info("Starting expansion around code system: {}", system);

		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(system);
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		ValueSetExpansionComponent retVal = new ValueSetExpansionComponent();
		Set<String> addedCodes = new HashSet<String>();
		boolean haveIncludeCriteria = false;

		/*
		 * Include Concepts
		 */
		for (ConceptReferenceComponent next : theInclude.getConcept()) {
			String nextCode = next.getCode();
			if (isNotBlank(nextCode) && !addedCodes.contains(nextCode)) {
				haveIncludeCriteria = true;
				TermConcept code = super.findCode(system, nextCode);
				if (code != null) {
					addedCodes.add(nextCode);
					ValueSetExpansionContainsComponent contains = retVal.addContains();
					contains.setCode(nextCode);
					contains.setSystem(system);
					contains.setDisplay(code.getDisplay());
				}
			}
		}

		/*
		 * Filters
		 */

		if (theInclude.getFilter().size() > 0) {
			haveIncludeCriteria = true;

			FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);
			QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(TermConcept.class).get();
			BooleanJunction<?> bool = qb.bool();

			bool.must(qb.keyword().onField("myCodeSystemVersionPid").matching(csv.getPid()).createQuery());

			for (ConceptSetFilterComponent nextFilter : theInclude.getFilter()) {
				if (isBlank(nextFilter.getValue()) && nextFilter.getOp() == null && isBlank(nextFilter.getProperty())) {
					continue;
				}

				if (isBlank(nextFilter.getValue()) || nextFilter.getOp() == null || isBlank(nextFilter.getProperty())) {
					throw new InvalidRequestException("Invalid filter, must have fields populated: property op value");
				}
				
				
				if (nextFilter.getProperty().equals("display:exact") && nextFilter.getOp() == FilterOperator.EQUAL) {
					addDisplayFilterExact(qb, bool, nextFilter);
				} else if ("display".equals(nextFilter.getProperty()) && nextFilter.getOp() == FilterOperator.EQUAL) {
					if (nextFilter.getValue().trim().contains(" ")) {
						addDisplayFilterExact(qb, bool, nextFilter);
					} else {
						addDisplayFilterInexact(qb, bool, nextFilter);
					}
				} else if ((nextFilter.getProperty().equals("concept") || nextFilter.getProperty().equals("code")) && nextFilter.getOp() == FilterOperator.ISA) {
					TermConcept code = super.findCode(system, nextFilter.getValue());
					if (code == null) {
						throw new InvalidRequestException("Invalid filter criteria - code does not exist: {" + system + "}" + nextFilter.getValue());
					}

					ourLog.info(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());
					bool.must(qb.keyword().onField("myParentPids").matching("" + code.getId()).createQuery());
				} else {
					throw new InvalidRequestException("Unknown filter property[" + nextFilter + "] + op[" + nextFilter.getOpElement().getValueAsString() + "]");
				}
			}

			Query luceneQuery = bool.createQuery();
			FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, TermConcept.class);
			jpaQuery.setMaxResults(1000);

			StopWatch sw = new StopWatch();
			
			@SuppressWarnings("unchecked")
			List<TermConcept> result = jpaQuery.getResultList();
			
			ourLog.info("Expansion completed in {}ms", sw.getMillis());
			
			for (TermConcept nextConcept : result) {
				addCodeIfNotAlreadyAdded(system, retVal, addedCodes, nextConcept);
			}

			retVal.setTotal(jpaQuery.getResultSize());
		}

		if (!haveIncludeCriteria) {
			List<TermConcept> allCodes = super.findCodes(system);
			for (TermConcept nextConcept : allCodes) {
				addCodeIfNotAlreadyAdded(system, retVal, addedCodes, nextConcept);
			}
		}

		return retVal;
	}

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		ValueSet source = new ValueSet();
		source.getCompose().addInclude().addValueSet(theValueSet);
		try {
			ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>();

			HapiWorkerContext worker = new HapiWorkerContext(myContext, myValidationSupport);
			ValueSetExpansionOutcome outcome = worker.expand(source, null);
			for (ValueSetExpansionContainsComponent next : outcome.getValueset().getExpansion().getContains()) {
				retVal.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			}

			return retVal;

		} catch (BaseServerResponseException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}

	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return Collections.emptyList();
	}

	@CoverageIgnore
	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return null;
	}

	@CoverageIgnore
	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return super.supportsSystem(theSystem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(String theSystem, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails) {
		CodeSystem cs = new org.hl7.fhir.dstu3.model.CodeSystem();
		cs.setUrl(theSystem);
		cs.setContent(CodeSystemContentMode.NOTPRESENT);

		DaoMethodOutcome createOutcome = myCodeSystemResourceDao.create(cs, "CodeSystem?url=" + UrlUtil.escape(theSystem), theRequestDetails);
		IIdType csId = createOutcome.getId().toUnqualifiedVersionless();
		if (createOutcome.getCreated() != Boolean.TRUE) {
			CodeSystem existing = myCodeSystemResourceDao.read(csId, theRequestDetails);
			csId = myCodeSystemResourceDao.update(existing, null, false, true, theRequestDetails).getId();

			ourLog.info("Created new version of CodeSystem, got ID: {}", csId.toUnqualified().getValue());
		}

		ResourceTable resource = (ResourceTable) myCodeSystemResourceDao.readEntity(csId);
		Long codeSystemResourcePid = resource.getId();

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		theCodeSystemVersion.setResource(resource);
		theCodeSystemVersion.setResourceVersionId(resource.getVersion());
		super.storeNewCodeSystemVersion(codeSystemResourcePid, theSystem, theCodeSystemVersion);

	}

	@CoverageIgnore
	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		TermConcept code = super.findCode(theCodeSystem, theCode);
		if (code != null) {
			ConceptDefinitionComponent def = new ConceptDefinitionComponent();
			def.setCode(code.getCode());
			def.setDisplay(code.getDisplay());
			return new CodeValidationResult(def);
		}

		return new CodeValidationResult(IssueSeverity.ERROR, "Unkonwn code {" + theCodeSystem + "}" + theCode);
	}

}
