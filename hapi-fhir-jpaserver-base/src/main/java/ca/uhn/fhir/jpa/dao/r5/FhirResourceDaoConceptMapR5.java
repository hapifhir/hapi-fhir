package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.ConceptMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class FhirResourceDaoConceptMapR5 extends BaseHapiFhirResourceDao<ConceptMap> implements IFhirResourceDaoConceptMap<ConceptMap> {
	@Autowired
	private ITermConceptMappingSvc myTermConceptMappingSvc;

	@Override
	public TranslationResult translate(TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		if (theTranslationRequest.hasReverse() && theTranslationRequest.getReverseAsBoolean()) {
			return myTermConceptMappingSvc.translateWithReverse(theTranslationRequest);
		}

		return myTermConceptMappingSvc.translate(theTranslationRequest);
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequestDetails, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequestDetails, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theTransactionDetails, theForceUpdate, theCreateNewHistoryEntry);
		if (!retVal.isUnchangedInCurrentOperation()) {

			if (retVal.getDeleted() == null) {
				ConceptMap conceptMap = (ConceptMap) theResource;
				myTermConceptMappingSvc.storeTermConceptMapAndChildren(retVal, org.hl7.fhir.convertors.conv40_50.ConceptMap40_50.convertConceptMap(conceptMap));
			} else {
				myTermConceptMappingSvc.deleteConceptMapAndChildren(retVal);
			}
		}

		return retVal;
	}
}
