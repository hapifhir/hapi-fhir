package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ValueSetExpansionWithHierarchyR4Test extends BaseTermR4Test {
   private static final String myCodeSystemId = "CodeSystem-WithHierarchy", myCodeSystemUrl = "http://example/" + myCodeSystemId;
   private static final String myCodeA = "CodeA", myCodeB = "CodeB", myCodeC = "CodeC", myCodeD = "CodeD";
   private static final String myValueSetAUrl = "http://example/ValueSetA", myValueSetBUrl = "http://example/ValueSetB", myValueSetUrl = "http://example/ValueSet";
   private static final int myChildConceptCount = 10;
   @BeforeAll
   public static void setup() {
      TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(true);
   }
   @AfterAll
   public static void tearDown() {
      TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);
   }
   @BeforeEach
   public void setupHierarchicalCodeSystemWithValueSets() {
      myStorageSettings.setPreExpandValueSets(true);

      CodeSystem codeSystem = new CodeSystem();
      codeSystem.setId(myCodeSystemId);
      codeSystem.setUrl(myCodeSystemUrl);
      CodeSystem.ConceptDefinitionComponent concept1 = codeSystem.addConcept().setCode(myCodeA);
      CodeSystem.ConceptDefinitionComponent concept2 = codeSystem.addConcept().setCode(myCodeB);
      for (int i = 0; i < myChildConceptCount; i++) {
         concept1.addConcept().setCode(concept1.getCode() + i);
         concept2.addConcept().setCode(concept2.getCode() + i);
      }
      codeSystem.addConcept().setCode(myCodeC);
      codeSystem.addConcept().setCode(myCodeD);
      myCodeSystemDao.create(codeSystem, mySrd);

      ValueSet valueSetA = new ValueSet();
      valueSetA.setUrl(myValueSetAUrl);
      valueSetA.getCompose().addInclude().setSystem(myCodeSystemUrl)
            .addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue(myCodeA);
      myValueSetDao.create(valueSetA, mySrd);

      ValueSet valueSetB = new ValueSet();
      valueSetB.setUrl(myValueSetBUrl);
      valueSetA.getCompose().addInclude().setSystem(myCodeSystemUrl)
            .addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue(myCodeB);
      myValueSetDao.create(valueSetB, mySrd);
   }

   static Stream<Arguments> parametersValueSets() {
      ValueSet valueSet0 = new ValueSet();
      valueSet0.setUrl(myValueSetUrl + "-WithIncludeChildValueSet");
      valueSet0.getCompose().addInclude().addValueSet(myValueSetAUrl);

      ValueSet valueSet1 = new ValueSet();
      valueSet1.setUrl(myValueSetUrl + "-WithIncludeChildValueSetAndCodeSystem");
      valueSet1.getCompose().addInclude().addValueSet(myValueSetAUrl);
      valueSet1.getCompose().addInclude().addValueSet(myValueSetBUrl);
      valueSet1.getCompose().addInclude().setSystem(myCodeSystemUrl);

      ValueSet valueSet2 = new ValueSet();
      valueSet2.setUrl(myValueSetUrl + "-WithIncludeChildValueSetAndCodeSystemConceptSet-NoIntersectionCodes");
      valueSet2.getCompose().addInclude().addValueSet(myValueSetAUrl);
      ValueSet.ConceptSetComponent conceptSetWithCodeSystem = valueSet2.getCompose().addInclude().setSystem(myCodeSystemUrl);
      conceptSetWithCodeSystem.addConcept().setCode(myCodeC);
      conceptSetWithCodeSystem.addConcept().setCode(myCodeD);

      ValueSet valueSet3 = new ValueSet();
      valueSet3.setUrl(myValueSetUrl + "-WithIncludeChildValueSetAndCodeSystemConceptSet-WithIntersectionCodes");
      valueSet3.getCompose().addInclude().addValueSet(myValueSetAUrl);
      conceptSetWithCodeSystem = valueSet3.getCompose().addInclude().setSystem(myCodeSystemUrl);
      conceptSetWithCodeSystem.addConcept().setCode(myCodeA + "1");
      conceptSetWithCodeSystem.addConcept().setCode(myCodeA + "2");

      return Stream.of(
            arguments(valueSet0, myChildConceptCount),
            arguments(valueSet1, 4 + myChildConceptCount * 2),
            arguments(valueSet2, 2 + myChildConceptCount),
            arguments(valueSet3, myChildConceptCount)
      );
   }

   @ParameterizedTest
   @MethodSource(value = "parametersValueSets")
   public void testExpandValueSet_UsesHierarchicalCodeSystem_ExpandsSuccessfully(ValueSet theValueSet, int theTotalConcepts) {
      myValueSetDao.create(theValueSet, mySrd);
      myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

      runInTransaction(() -> {
         Optional<TermValueSet> optionalTermValueSet = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(theValueSet.getUrl());
         assertTrue(optionalTermValueSet.isPresent());
         assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, optionalTermValueSet.get().getExpansionStatus());
         assertEquals(theTotalConcepts, optionalTermValueSet.get().getTotalConcepts());
      });
   }
}
