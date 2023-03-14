package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class ReindexDryRunServiceImpl implements IReindexDryRunService {

	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	@Override
	public IBaseParameters reindexDryRun(IIdType theResourceId) {
		return null;
	}

}
