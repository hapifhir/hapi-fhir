package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.stream.Collectors;

public class EmpiLinkQuerySvcImpl implements IEmpiLinkQuerySvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkQuerySvcImpl.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	IResourceTableDao myResourceTableDao;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Override
	public IBaseParameters queryLinks(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(thePersonId, theTargetId, theMatchResult, theLinkSource);
		List<EmpiLink> empiLinks = myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink).stream()
			.filter(empiLink -> empiLink.getMatchResult() != EmpiMatchResultEnum.POSSIBLE_DUPLICATE)
			.collect(Collectors.toList());
		// FIXME KHS page this
		return parametersFromEmpiLinks(empiLinks);
	}

	@Override
	public IBaseParameters getPossibleDuplicates(EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(null, null, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, null);
		List<EmpiLink> empiLinks = myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink);
		// FIXME KHS page this
		return parametersFromEmpiLinks(empiLinks);
	}

	private IBaseParameters parametersFromEmpiLinks(List<EmpiLink> theEmpiLinks) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);

		for (EmpiLink empiLink : theEmpiLinks) {
			IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retval, "link");
			String personId = myResourceTableDao.findById(empiLink.getPersonPid()).get().getIdDt().toVersionless().getValue();
			ParametersUtil.addPartString(myFhirContext, resultPart, "personId", personId);

			String targetId = myResourceTableDao.findById(empiLink.getTargetPid()).get().getIdDt().toVersionless().getValue();
			ParametersUtil.addPartString(myFhirContext, resultPart, "targetId", targetId);

			ParametersUtil.addPartString(myFhirContext, resultPart, "matchResult", empiLink.getMatchResult().name());
			ParametersUtil.addPartString(myFhirContext, resultPart, "linkSource", empiLink.getLinkSource().name());
		}
		return retval;
	}

	private Example<EmpiLink> exampleLinkFromParameters(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource) {
		EmpiLink empiLink = new EmpiLink();
		if (thePersonId != null) {
			empiLink.setPersonPid(myIdHelperService.getPidOrThrowException(thePersonId));
		}
		if (theTargetId != null) {
			empiLink.setTargetPid(myIdHelperService.getPidOrThrowException(theTargetId));
		}
		if (theMatchResult != null) {
			empiLink.setMatchResult(theMatchResult);
		}
		if (theLinkSource != null) {
			empiLink.setLinkSource(theLinkSource);
		}
		return Example.of(empiLink);
	}
}
