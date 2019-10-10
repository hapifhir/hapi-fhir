package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TermVersionAdapterSvcDstu3 extends BaseTermVersionAdapterSvcImpl implements ITermVersionAdapterSvc {

	private IFhirResourceDao<ConceptMap> myConceptMapResourceDao;
	private IFhirResourceDao<CodeSystem> myCodeSystemResourceDao;
	private IFhirResourceDao<ValueSet> myValueSetResourceDao;

	@Autowired
	private ApplicationContext myAppCtx;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void start() {
		myCodeSystemResourceDao = (IFhirResourceDao<CodeSystem>) myAppCtx.getBean("myCodeSystemDaoDstu3");
		myValueSetResourceDao = (IFhirResourceDao<ValueSet>) myAppCtx.getBean("myValueSetDaoDstu3");
		myConceptMapResourceDao = (IFhirResourceDao<ConceptMap>) myAppCtx.getBean("myConceptMapDaoDstu3");
	}

	@Override
	public IIdType createOrUpdateCodeSystem(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource) {
		CodeSystem resourceToStore;
		try {
			resourceToStore = VersionConvertor_30_40.convertCodeSystem(theCodeSystemResource);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
		validateCodeSystemForStorage(theCodeSystemResource);
		if (isBlank(resourceToStore.getIdElement().getIdPart())) {
			String matchUrl = "CodeSystem?url=" + UrlUtil.escapeUrlParam(theCodeSystemResource.getUrl());
			return myCodeSystemResourceDao.update(resourceToStore, matchUrl).getId();
		} else {
			return myCodeSystemResourceDao.update(resourceToStore).getId();
		}
	}

	@Override
	public void createOrUpdateConceptMap(org.hl7.fhir.r4.model.ConceptMap theConceptMap) {
		ConceptMap resourceToStore;
		try {
			resourceToStore = VersionConvertor_30_40.convertConceptMap(theConceptMap);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
		if (isBlank(resourceToStore.getIdElement().getIdPart())) {
			String matchUrl = "ConceptMap?url=" + UrlUtil.escapeUrlParam(theConceptMap.getUrl());
			myConceptMapResourceDao.update(resourceToStore, matchUrl);
		} else {
			myConceptMapResourceDao.update(resourceToStore);
		}
	}

	@Override
	public void createOrUpdateValueSet(org.hl7.fhir.r4.model.ValueSet theValueSet) {
		ValueSet valueSetDstu3;
		try {
			valueSetDstu3 = VersionConvertor_30_40.convertValueSet(theValueSet);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		if (isBlank(valueSetDstu3.getIdElement().getIdPart())) {
			String matchUrl = "ValueSet?url=" + UrlUtil.escapeUrlParam(theValueSet.getUrl());
			myValueSetResourceDao.update(valueSetDstu3, matchUrl);
		} else {
			myValueSetResourceDao.update(valueSetDstu3);
		}
	}

}
