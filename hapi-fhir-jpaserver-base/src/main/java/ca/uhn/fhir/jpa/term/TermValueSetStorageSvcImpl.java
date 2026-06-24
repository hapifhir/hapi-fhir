package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r5.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermValueSetStorageSvcImpl implements ITermValueSetStorageSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermValueSetStorageSvcImpl.class);

	@Autowired
	private ITermValueSetDao myTermValueSetDao;
	@Autowired
	private ITermValueSetConceptDao myTermValueSetConceptDao;
	@Autowired
	private ITermValueSetConceptDesignationDao myTermValueSetConceptDesignationDao;
	@Autowired
	private ITermValueSetConceptParentChildLinkDao myTermValueSetConceptParentChildLinkDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public String startStagingVersion(@Nonnull String theUrl, @Nullable String theVersion) {
		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {

			TermValueSet valueSet = fetchTermValueSet(theUrl, theVersion);
			valueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
			myTermValueSetDao.save(valueSet);

			String stagingVersionId = UUID.randomUUID().toString();

			TermValueSet stagingValueSet = new TermValueSet();
			stagingValueSet.setPartitionId(valueSet.getPartitionId());
			stagingValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
			stagingValueSet.setUrl(valueSet.getUrl());
			stagingValueSet.setVersion(stagingVersionId);
			stagingValueSet.setName(valueSet.getName());
			stagingValueSet.setIntendedVersionId(theVersion);
			stagingValueSet.setResource(valueSet.getResource());
			myTermValueSetDao.save(stagingValueSet);

			ourLog.atInfo()
				.setMessage("Starting staging version for ValueSet Url[{}] Version[{}] with staging version: {}")
				.addArgument(valueSet.getUrl())
				.addArgument(valueSet.getVersion())
				.addArgument(stagingVersionId)
				.log();

			return stagingVersionId;
		});
	}

	@Override
	public UploadStatistics addConceptsToExpansion(@Nonnull ValueSet theDelta, int theStartingOrder) {
		StopWatch sw = new StopWatch();
		String url = theDelta.getUrl();
		String version = theDelta.getVersion();

		UploadStatistics retVal = myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			// FIXME: split the stuff below into methods
			FlattenedValueSet flattenedValueSet = flattenValueSet(theDelta);
			IntCounter order = new IntCounter(theStartingOrder);
			Map<SystemAndCode, TermValueSetConcept> codeToStorageConcept = new HashMap<>();

			TermValueSet termValueSet = fetchTermValueSet(url, version);

			UploadStatistics statistics = new UploadStatistics(termValueSet.getResource().getIdDt());

			for (UrlUtil.CanonicalUrlParts system : flattenedValueSet.systemToCodes().keySet()) {

				Collection<String> codes = flattenedValueSet.systemToCodes().get(system);
				Map<String, TermValueSetConcept> codeToExistingConcept = myTermValueSetConceptDao
					.findByCodesForTermValueSet(termValueSet, system.url(), codes)
					.stream()
					.collect(Collectors.toMap(TermValueSetConcept::getCode, Function.identity()));
				Set<Integer> existingOrders = codeToExistingConcept.values()
					.stream()
					.map(TermValueSetConcept::getOrder)
					.collect(Collectors.toSet());

				Set<ValueSet.ValueSetExpansionContainsComponent> conceptsToAdd = flattenedValueSet.systemToConcepts().get(system);
				for (ValueSet.ValueSetExpansionContainsComponent conceptToAdd : conceptsToAdd) {

					TermValueSetConcept storageConcept = codeToExistingConcept.get(conceptToAdd.getCode());
					if (storageConcept == null) {
						storageConcept = new TermValueSetConcept();
						storageConcept.setCode(conceptToAdd.getCode());
						storageConcept.setDisplay(conceptToAdd.getDisplay());
						storageConcept.setSystem(conceptToAdd.getSystem());
						storageConcept.setSystemVersion(conceptToAdd.getVersion());
						storageConcept.setValueSet(termValueSet);

						while (existingOrders.contains(order.get())) {
							order.increment();
						}
						storageConcept.setOrder(order.getAndIncrement());

						statistics.incrementConceptsAddedCount();
						myTermValueSetConceptDao.save(storageConcept);
					}

					SystemAndCode systemAndCode = new SystemAndCode(conceptToAdd.getSystem(), conceptToAdd.getVersion(), conceptToAdd.getCode());
					codeToStorageConcept.put(systemAndCode, storageConcept);

					if (!conceptToAdd.getDesignation().isEmpty()) {
						Set<LanguageAndDesignation> existingDesignations = storageConcept
							.getDesignations()
							.stream()
							.map(t -> new LanguageAndDesignation(t.getLanguage(), t.getValue(), t.getUseSystem(), t.getUseCode()))
							.collect(Collectors.toSet());
						for (ValueSet.ConceptReferenceDesignationComponent designationToAdd : conceptToAdd.getDesignation()) {
							if (!existingDesignations.contains(new LanguageAndDesignation(designationToAdd.getLanguage(), designationToAdd.getValue(), designationToAdd.getUse().getSystem(), designationToAdd.getUse().getCode()))) {

								TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
								designation.setPartitionId(storageConcept.getPartitionId());
								designation.setConcept(storageConcept);
								designation.setValueSet(termValueSet);
								designation.setLanguage(designationToAdd.getLanguage());
								designation.setValue(designationToAdd.getValue());
								designation.setUseSystem(designationToAdd.getUse().getSystem());
								designation.setUseCode(designationToAdd.getUse().getCode());
								designation.setUseDisplay(designationToAdd.getUse().getDisplay());

								statistics.incrementDesignationsAddedCount();
								myTermValueSetConceptDesignationDao.save(designation);
							}
						}
					}

				}

			}

			for (Map.Entry<SystemAndCode, SystemAndCode> entry : flattenedValueSet.childCodeToParentCodes().entries()) {
				SystemAndCode childSystemAndCode = entry.getKey();
				SystemAndCode parentSystemAndCode = entry.getValue();
				TermValueSetConcept childConcept = codeToStorageConcept.get(childSystemAndCode);
				TermValueSetConcept parentConcept = codeToStorageConcept.get(parentSystemAndCode);

				// Sanity check - These should never fail since we always create the parents
				// in the block above, and you can't pass a child into this method without
				// also passing in its parent
				Validate.notNull(childConcept, "Failed to find concept: %s", childSystemAndCode);
				Validate.notNull(parentConcept, "Failed to find concept: %s", parentSystemAndCode);

				boolean shouldAdd = childConcept
					.getParentConcepts()
					.stream()
					.map(c -> new SystemAndCode(c.getSystem(), c.getSystemVersion(), c.getCode()))
					.noneMatch(t -> t.equals(parentSystemAndCode));
				if (shouldAdd) {

					TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk pk = new  TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk();
					pk.setPartitionId(termValueSet.getPartitionedId().getPartitionIdValue());
					pk.setParentPid(parentConcept.getId());
					pk.setChildPid(childConcept.getId());

					TermValueSetConceptParentChildLink linkToAdd = new TermValueSetConceptParentChildLink();
					linkToAdd.setId(pk);
					linkToAdd.setValueSet(termValueSet);
					myTermValueSetConceptParentChildLinkDao.save(linkToAdd);

					parentConcept.getChildren().add(linkToAdd);
					childConcept.getParents().add(linkToAdd);

					statistics.incrementConceptLinksAddedCount();
				}

			}

			termValueSet.setTotalConcepts(termValueSet.getTotalConcepts() + statistics.getAddedConceptCount());
			termValueSet.setTotalConceptDesignations(termValueSet.getTotalConceptDesignations() + statistics.getAddedDesignationCount());

			return statistics;
		});

		ourLog.atInfo()
			.setMessage("Added to ValueSet URL[{}] Version[{}] in {}: {}")
			.addArgument(url)
			.addArgument(version)
			.addArgument(sw)
			.addArgument(retVal)
			.log();

		return retVal;
	}

	private FlattenedValueSet flattenValueSet(@Nonnull ValueSet theValueSet) {
		SetMultimap<UrlUtil.CanonicalUrlParts, String> systemToCodes = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
		SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent> systemToConcepts = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
		SetMultimap<SystemAndCode, SystemAndCode> childCodeToParentCodes = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();

		List<ValueSet.ValueSetExpansionContainsComponent> containsList = theValueSet.getExpansion().getContains();
		flattenValueSetInto(containsList, null, systemToCodes, systemToConcepts, childCodeToParentCodes);

		return new FlattenedValueSet(systemToCodes, systemToConcepts, childCodeToParentCodes);
	}

	private static void flattenValueSetInto(List<ValueSet.ValueSetExpansionContainsComponent> theSourceConcepts, SystemAndCode theSourceParentConcept, SetMultimap<UrlUtil.CanonicalUrlParts, String> theTargetSystemToCodes, SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent> theTargetSystemToConcepts, SetMultimap<SystemAndCode, SystemAndCode> theTargetChildCodeToParentCodes) {
		for (ValueSet.ValueSetExpansionContainsComponent contains : theSourceConcepts) {
			String code = contains.getCode();
			if (isNotBlank(code)) {
				String systemUrl = contains.getSystem();
				if (isBlank(systemUrl)) {
					// FIXME: add code
					throw new InvalidRequestException(Msg.code(1) + "ValueSet contains a code with no system: " + UrlUtil.sanitizeUrlPart(code));
				}

				String systemVersion = contains.getVersion();
				UrlUtil.CanonicalUrlParts system = UrlUtil.parseCanonicalUrl(systemUrl, systemVersion);
				theTargetSystemToCodes.put(system, code);
				theTargetSystemToConcepts.put(system, contains);

				SystemAndCode systemAndCode = new SystemAndCode(system, code);
				if (theSourceParentConcept != null) {
					theTargetChildCodeToParentCodes.put(systemAndCode, theSourceParentConcept);
				}

				// Recurse
				flattenValueSetInto(contains.getContains(), systemAndCode, theTargetSystemToCodes, theTargetSystemToConcepts, theTargetChildCodeToParentCodes);
			}

		}
	}

	@Nonnull
	private TermValueSet fetchTermValueSet(String theUri, @Nullable String theVersion) {
		HapiTransactionService.requireTransaction();

		Optional<TermValueSet> valueSetOpt;
		if (isBlank(theVersion)) {
			valueSetOpt = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(theUri);
		} else {
			valueSetOpt = myTermValueSetDao.findTermValueSetByUrlAndVersion(theUri, theVersion);
		}

		if (valueSetOpt.isEmpty()) {
			// FIXME: add code and test
			throw new ResourceNotFoundException(Msg.code(1) + "No ValueSet found with URL[" + theUri + "] and Version[" + getIfNull(theVersion, "(none)") + "]");
		}

		return valueSetOpt.get();
	}

	private record SystemAndCode(UrlUtil.CanonicalUrlParts system, String code) {
		public SystemAndCode(String theSystem, String theVersion, String theCode) {
			this(UrlUtil.parseCanonicalUrl(theSystem, theVersion), theCode);
		}
	}

	private record LanguageAndDesignation(String language, String designation, String useSystem, String useCode) {}

	private record FlattenedValueSet(SetMultimap<UrlUtil.CanonicalUrlParts, String> systemToCodes,
	                                 SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent> systemToConcepts,
	                                 SetMultimap<SystemAndCode, SystemAndCode> childCodeToParentCodes) {}

}
