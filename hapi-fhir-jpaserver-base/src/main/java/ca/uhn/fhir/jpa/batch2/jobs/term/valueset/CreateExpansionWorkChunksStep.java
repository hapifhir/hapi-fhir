package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CreateExpansionWorkChunksStep implements IJobStepWorker<PreExpandValueSetParameters, VoidModel, ExpansionWorkChunkJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(CreateExpansionWorkChunksStep.class);

	@Autowired
	private ITermValueSetDao myValueSetDao;
	@Autowired
	private IHapiTransactionService myTxService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ExpansionWorkChunkJson> theDataSink) throws JobExecutionFailedException {

		String stagingVersion = UUID.randomUUID().toString();

		TermValueSet termValueSet = myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			PreExpandValueSetParameters parameters = theStepExecutionDetails.getParameters();
			Optional<TermValueSet> valueSetOpt;
			if (isBlank(parameters.getVersion())) {
				valueSetOpt = myValueSetDao.findTermValueSetByUrlAndNullVersion(parameters.getUrl());
			} else {
				valueSetOpt = myValueSetDao.findTermValueSetByUrlAndVersion(parameters.getUrl(), parameters.getVersion());
			}

			UrlUtil.CanonicalUrlParts canonicalUrl = parameters.getCanonicalUrl();
			if (valueSetOpt.isEmpty()) {
				// FIXME: add code and test
				throw new JobExecutionFailedException(Msg.code(1) + "No ValueSet found with URL: " + canonicalUrl);
			}

			TermValueSet valueSet = valueSetOpt.get();
			valueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
			myValueSetDao.save(valueSet);

			String fhirId = valueSet.getResource().getFhirId();
			ourLog.info("Found ValueSet[{}] with ID: ValueSet/{}", fhirId, canonicalUrl);

			return valueSet;
		});

		RequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
		IIdType id = myFhirContext.getVersion().newIdType("ValueSet", termValueSet.getResource().getFhirId());
		IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(id, requestDetails);

		ValueSet valueSetCanonical = myVersionCanonicalizer.valueSetToCanonical(valueSet);

		for (ValueSet.ConceptSetComponent include : valueSetCanonical.getCompose().getInclude()) {

			ExpansionWorkChunkJson workChunk = new ExpansionWorkChunkJson();
			workChunk.setCompose(include);

		}

		return null;
	}

}
