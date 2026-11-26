package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.util.PartitionedIdJpaRepository;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.persistence.IdClass;
import jakarta.persistence.metamodel.Type;
import org.hibernate.Hibernate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.DecimalType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Location;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.RiskAssessment;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@code save()} on the application's repositories, which {@code JpaConfig} gives
 * {@link PartitionedIdJpaRepository} as their base class, persists new entities whose key is {@link IdAndPartitionId}.
 */
// Created by claude-fable-5-2
@TestPropertySource(properties = {JpaConstants.HAPI_DATABASE_PARTITION_MODE + "=true"})
public class PartitionedIdJpaRepositoryDbpmEnabledTest extends BaseDbpmResourceProviderR5Test {

	@Autowired
	private IMdmLinkJpaRepository myMdmLinkJpaRepository;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setDatabasePartitionMode(isDatabasePartitionMode());
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		registerPartitionInterceptorAndCreatePartitions();
		initResourceTypeCacheFromConfig();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://example.com/vs");
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myValueSetDao.create(valueSet, new SystemRequestDetails());
	}

	protected boolean isDatabasePartitionMode() {
		return true;
	}

	@Test
	void save_newEntity_persistsTheSameInstanceWithoutSelect() {
		runInTransaction(() -> {
			TermValueSetConcept concept = newConcept("A");
			myCaptureQueriesListener.clear();

			TermValueSetConcept saved = myTermValueSetConceptDao.save(concept);
			myEntityManager.flush();

			assertThat(saved).isSameAs(concept);
			assertThat(concept.getId()).isNotNull();
			assertThat(myEntityManager.contains(concept)).isTrue();
			assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread())
					.isEmpty();
			assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread())
					.hasSize(1);
		});
	}

	@Test
	void saveAll_newEntities_persistsTheSameInstances() {
		runInTransaction(() -> {
			TermValueSetConcept conceptA = newConcept("A");
			TermValueSetConcept conceptB = newConcept("B");

			List<TermValueSetConcept> saved = myTermValueSetConceptDao.saveAll(List.of(conceptA, conceptB));

			assertThat(saved).hasSize(2);
			assertThat(saved.get(0)).isSameAs(conceptA);
			assertThat(saved.get(1)).isSameAs(conceptB);
			assertThat(conceptA.getId()).isNotNull();
			assertThat(conceptB.getId()).isNotNull();
		});
	}

	@Test
	void save_detachedExistingEntity_mergesTheChange() {
		TermValueSetConcept detached = runInTransaction(() -> myTermValueSetConceptDao.save(newConcept("A")));
		detached.setDisplay("changed");

		runInTransaction(() -> {
			TermValueSetConcept saved = myTermValueSetConceptDao.save(detached);

			assertThat(saved).isNotSameAs(detached);
			assertThat(saved.getId()).isEqualTo(detached.getId());
		});

		runInTransaction(() -> {
			List<TermValueSetConcept> all = myTermValueSetConceptDao.findAll();
			assertThat(all).hasSize(1);
			assertThat(all.get(0).getDisplay()).isEqualTo("changed");
		});
	}

	@Test
	void delete_detachedExistingEntity_removesIt() {
		TermValueSetConcept detached = runInTransaction(() -> myTermValueSetConceptDao.save(newConcept("A")));

		runInTransaction(() -> myTermValueSetConceptDao.delete(detached));

		runInTransaction(() -> assertThat(myTermValueSetConceptDao.count()).isZero());
	}

	@Test
	void delete_uninitializedProxy_removesIt() {
		TermValueSetConcept existing = runInTransaction(() -> myTermValueSetConceptDao.save(newConcept("A")));

		runInTransaction(() -> {
			TermValueSetConcept proxy = myTermValueSetConceptDao.getReferenceById(existing.getPartitionedId());
			assertThat(Hibernate.isInitialized(proxy)).isFalse();

			myTermValueSetConceptDao.delete(proxy);
		});

		runInTransaction(() -> assertThat(myTermValueSetConceptDao.count()).isZero());
	}

	@Test
	void findRevisions_revisionRepository_stillWorks() {
		runInTransaction(() -> assertThat(myMdmLinkJpaRepository.findRevisions(-1L)).isEmpty());
	}

	/**
	 * The entities whose key is {@link IdAndPartitionId} and whose PID is generated
	 */
	static Stream<Class<?>> partitionedIdEntityTypes() {
		return Stream.of(
				ResourceHistoryTag.class,
				ResourceIndexedComboStringUnique.class,
				ResourceIndexedComboTokenNonUnique.class,
				ResourceIndexedSearchParamCoords.class,
				ResourceIndexedSearchParamDate.class,
				ResourceIndexedSearchParamNumber.class,
				ResourceIndexedSearchParamQuantity.class,
				ResourceIndexedSearchParamQuantityNormalized.class,
				ResourceIndexedSearchParamString.class,
				ResourceIndexedSearchParamToken.class,
				ResourceIndexedSearchParamUri.class,
				ResourceLink.class,
				ResourceTag.class,
				SearchParamPresentEntity.class,
				TermCodeSystem.class,
				TermCodeSystemVersion.class,
				TermConceptDesignation.class,
				TermConceptMap.class,
				TermConceptMapGroup.class,
				TermConceptMapGroupElement.class,
				TermConceptMapGroupElementTarget.class,
				TermConceptProperty.class,
				TermValueSet.class,
				TermValueSetConcept.class,
				TermValueSetConceptDesignation.class);
	}

	@ParameterizedTest
	@MethodSource("partitionedIdEntityTypes")
	void save_newEntityOfEachPartitionedType_persistsTheSameInstance(Class<?> theEntityType) {
		createFixtureResources();
		JpaRepository<Object, ?> repository = repositoryFor(theEntityType);

		runInTransaction(() -> {
			Object entity = newEntity(theEntityType);
			myEntityManager.flush();

			Object saved = repository.save(entity);
			myEntityManager.flush();

			assertThat(saved).isSameAs(entity);
			assertThat(myEntityManager.contains(entity)).isTrue();
			assertThat(ReflectionTestUtils.getField(entity, "myId")).isNotNull();
		});
	}

	@Test
	void partitionedIdEntityTypes_coverEveryEntityWithAGeneratedPid() {
		Set<Class<?>> expected = myEntityManager.getMetamodel().getEntities().stream()
				.map(Type::getJavaType)
				.filter(t -> {
					IdClass idClass = t.getAnnotation(IdClass.class);
					return idClass != null && IdAndPartitionId.class.equals(idClass.value());
				})
				// Its PID is assigned, so a new one is never recognized by a null PID
				.filter(t -> !ResourceHistoryProvenanceEntity.class.equals(t))
				.collect(Collectors.toSet());

		assertThat(partitionedIdEntityTypes().collect(Collectors.toSet())).isEqualTo(expected);
	}

	@AfterEach
	void resetNormalizedQuantitySearchLevel() {
		myStorageSettings.setNormalizedQuantitySearchLevel(new JpaStorageSettings().getNormalizedQuantitySearchLevel());
	}

	/**
	 * Stores resources that leave at least one row of every entity that {@link #newEntity(Class)} copies
	 */
	private void createFixtureResources() {
		myStorageSettings.setNormalizedQuantitySearchLevel(
				NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);

		Patient patient = new Patient();
		patient.getMeta().addTag("http://example.com/tags", "tag", "Tag");
		patient.addIdentifier().setSystem("http://example.com/ids").setValue("1");
		patient.addName().setFamily("Family");
		patient.setBirthDateElement(new DateType("2020-01-01"));
		IIdType patientId = create(patient);

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.getValue());
		observation.setValue(new Quantity()
				.setValue(1)
				.setSystem("http://unitsofmeasure.org")
				.setCode("mg"));
		create(observation);

		Location location = new Location();
		location.getPosition().setLatitude(43.0).setLongitude(-79.0);
		create(location);

		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.addPrediction().setProbability(new DecimalType("0.5"));
		create(riskAssessment);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/cs");
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		codeSystem.addProperty().setCode("property").setType(CodeSystem.PropertyType.STRING);
		CodeSystem.ConceptDefinitionComponent concept =
				codeSystem.addConcept().setCode("A").setDisplay("Concept A");
		concept.addDesignation().setLanguage("en").setValue("Designation A");
		concept.addProperty().setCode("property").setValue(new StringType("value"));
		create(codeSystem);

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl("http://example.com/cm");
		conceptMap.setStatus(Enumerations.PublicationStatus.ACTIVE);
		conceptMap
				.addGroup()
				.setSource("http://example.com/cs")
				.setTarget("http://example.com/cs-target")
				.addElement()
				.setCode("A")
				.addTarget()
				.setCode("B")
				.setRelationship(Enumerations.ConceptMapRelationship.EQUIVALENT);
		create(conceptMap);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private IIdType create(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.fhirType());
		return dao.create(theResource, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
	}

	/**
	 * @return A new entity with a partition ID and no PID, ready to be saved. Must be called in a transaction.
	 */
	private Object newEntity(Class<?> theEntityType) {
		if (TermValueSetConcept.class.equals(theEntityType)) {
			return newConcept("A");
		}
		if (TermValueSetConceptDesignation.class.equals(theEntityType)) {
			TermValueSetConcept concept = newConcept("A");
			myEntityManager.persist(concept);
			TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
			designation.setPartitionId(concept.getPartitionId());
			designation.setConcept(concept);
			designation.setValueSet(myTermValueSetDao.findAll().get(0));
			designation.setValue("Designation A");
			return designation;
		}

		ResourceTable patient = myEntityManager
				.createQuery("SELECT r FROM ResourceTable r WHERE r.myResourceType = 'Patient'", ResourceTable.class)
				.getSingleResult();
		if (SearchParamPresentEntity.class.equals(theEntityType)) {
			SearchParamPresentEntity present = new SearchParamPresentEntity("name", true);
			present.setResource(patient);
			present.setPartitionId(patient.getPartitionId());
			present.setPartitionSettings(myPartitionSettings);
			return present;
		}
		if (ResourceIndexedComboStringUnique.class.equals(theEntityType)) {
			return new ResourceIndexedComboStringUnique(
					patient, "Patient?family=NEW", new IdType("SearchParameter/unique"));
		}
		if (ResourceIndexedComboTokenNonUnique.class.equals(theEntityType)) {
			ResourceIndexedComboTokenNonUnique nonUnique =
					new ResourceIndexedComboTokenNonUnique(myPartitionSettings, patient, "Patient?family=NEW");
			nonUnique.setPartitionId(patient.getPartitionId());
			nonUnique.setHashComplete(1L);
			return nonUnique;
		}

		Object existing = findAny(theEntityType);
		Object retVal = copyWithoutPid(existing);
		if (retVal instanceof TermCodeSystem termCodeSystem) {
			termCodeSystem.setCodeSystemUri("http://example.com/new-cs");
		} else if (retVal instanceof TermCodeSystemVersion termCodeSystemVersion) {
			termCodeSystemVersion.setCodeSystemVersionId("new");
		} else if (retVal instanceof TermConceptMap termConceptMap) {
			termConceptMap.setUrl("http://example.com/new-cm");
		} else if (retVal instanceof TermValueSet termValueSet) {
			termValueSet.setUrl("http://example.com/new-vs");
		} else if (retVal instanceof ResourceTag || retVal instanceof ResourceHistoryTag) {
			// A resource can't have the same tag twice
			myEntityManager.remove(existing);
		}
		return retVal;
	}

	private <T> T findAny(Class<T> theEntityType) {
		List<T> found = myEntityManager
				.createQuery("SELECT e FROM " + theEntityType.getName() + " e", theEntityType)
				.setMaxResults(1)
				.getResultList();
		assertThat(found).as("Stored rows of %s", theEntityType.getSimpleName()).isNotEmpty();
		return found.get(0);
	}

	/**
	 * @return A new entity with the column values and associations of the given one, without its PID or collections
	 */
	@SuppressWarnings("unchecked")
	private <T> T copyWithoutPid(T theExisting) {
		Class<T> entityType = (Class<T>) theExisting.getClass();
		T retVal = BeanUtils.instantiateClass(entityType);
		ReflectionUtils.doWithFields(
				entityType,
				field -> {
					ReflectionUtils.makeAccessible(field);
					Object value = field.get(theExisting);
					if (!(value instanceof Collection)) {
						field.set(retVal, value);
					}
				},
				field -> !Modifier.isStatic(field.getModifiers()));
		ReflectionTestUtils.setField(retVal, "myId", null);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private JpaRepository<Object, ?> repositoryFor(Class<?> theEntityType) {
		return (JpaRepository<Object, ?>)
				new Repositories(myApplicationContext).getRepositoryFor(theEntityType).orElseThrow();
	}

	private TermValueSetConcept newConcept(String theCode) {
		TermValueSet valueSet = myTermValueSetDao.findAll().get(0);
		TermValueSetConcept retVal = new TermValueSetConcept();
		retVal.setValueSet(valueSet);
		retVal.setSystem("http://example.com/cs");
		retVal.setCode(theCode);
		retVal.setOrder(theCode.charAt(0));
		return retVal;
	}
}
