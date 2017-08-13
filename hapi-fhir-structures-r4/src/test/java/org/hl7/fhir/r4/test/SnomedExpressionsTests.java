package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.utils.SnomedExpressions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SnomedExpressionsTests {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void test() throws FHIRException {
    p("116680003");
    p("128045006:{363698007=56459004}");
    p("128045006|cellulitis (disorder)|:{363698007|finding site|=56459004|foot structure|}");
    p("31978002: 272741003=7771000");
    p("31978002|fracture of tibia|: 272741003|laterality|=7771000|left|");
    p("64572001|disease|:{116676008|associated morphology|=72704001|fracture|,363698007|finding site|=(12611008|bone structure of  tibia|:272741003|laterality|=7771000|left|)}");
    p("417662000|past history of clinical finding|:246090004|associated finding|=      (31978002|fracture of tibia|: 272741003|laterality|=7771000|left|)");
    p("243796009|situation with explicit context|:246090004|associated finding|=    (64572001|disease|:{116676008|associated morphology|=72704001|fracture|,"+"    363698007|finding site|=(12611008|bone structure of tibia|:    272741003|laterality|=7771000|left|)}),408729009|finding context|=    "+"410515003|known present|,408731000|temporal context|=410513005|past|,    408732007|subject relationship context|=410604004|subject of record|");

    // from IHTSDO expression documentation:
    p("125605004 |fracture of bone|");
    p("284003005 |bone injury| :{ 363698007 |finding site| = 272673000 |bone structure|,116676008 |associated morphology| = 72704001 |fracture| }");
    p("421720008 |spray dose form| + 7946007 |drug suspension|");
    p("182201002 |hip joint| : 272741003 |laterality| = 24028007 |right|");
    p("397956004 |prosthetic arthroplasty of the hip| : 363704007 |procedure site| = (182201002 |hip joint| : 272741003 |laterality| = 24028007 |right|)");
    p("71388002 |procedure| : {260686004 |method| = 129304002 |excision - action|, 405813007 |procedure site - direct| = 28231008 |gallbladder structure|}, {260686004 |method| = 281615006 "+"|exploration|, 405813007 |procedure site - direct| = 28273000 |bile duct structure|}");
    p("27658006 |amoxicillin|:411116001 |has dose form| = 385049006 |capsule|,{ 127489000 |has active ingredient| = 372687004 |amoxicillin|,111115 |has basis of strength| = (111115 |"+"amoxicillin only|:111115 |strength magnitude| = #500,111115 |strength unit| = 258684004 |mg|)}");
    p("91143003 |albuterol|:411116001 |has dose form| = 385023001 |oral solution|,{ 127489000 |has active ingredient| = 372897005 |albuterol|,111115 |has basis of strength| = (111115 |a"+"lbuterol only|:111115 |strength magnitude| = #0.083,111115 |strength unit| = 118582008 |%|)}");
    p("322236009 |paracetamol 500mg tablet| : 111115 |trade name| = \"PANADOL\"");
    p("=== 46866001 |fracture of lower limb| + 428881005 |injury of tibia| :116676008 |associated morphology| = 72704001 |fracture|,363698007 |finding site| = 12611008 |bone structure of tibia|");
    p("<<< 73211009 |diabetes mellitus| : 363698007 |finding site| = 113331007 |endocrine system|");

    // from Overview of Expression Normalization, Equivalence and Subsumption Testing

    p("28012007 |Closed fracture of shaft of tibia|");
    p("125605004 |Fracture of bone| :{ 363698007 |Finding site| = 52687003 |Bone structure of shaft of tibia|,116676008 |Associated morphology| = 20946005 |Fracture, closed | }");
    p("423125000 |Closed fracture of bone|:363698007 |Finding site| = 52687003 |Bone structure of shaft of tibia|");
    p("6990005 |Fracture of shaft of tibia |: 116676008 |Associated morphology| = 20946005 |Fracture, closed |");
    p("64572001 |Disease| : { 363698007 |Finding site| = 52687003 |Bone structure of shaft of tibia|, 116676008 |Associated morphology| = 20946005 |Fracture, closed | }");
   // p("10925361000119108 |Closed fracture of shaft of left tibia|"); //  US Extension
    p("28012007 |Closed fracture of shaft of tibia| : 363698007 |Finding site| = (52687003 |Bone structure of shaft of tibia| : 272741003 |Laterality|= 7771000 |Left|)");
    p("28012007 |Closed fracture of shaft of tibia| : 272741003 |Laterality|= 7771000 |Left|"); //Close to user form omits restatement of finding site");
    p("64572001 |Disease| : {363698007 |Finding site| = (52687003 |Bone structure of shaft of tibia| : 272741003 |Laterality|= 7771000 |Left|), 116676008 |Associated morphology| = 20946005 |Fracture, closed | }");
    p("28012007 |Closed fracture of shaft of tibia| : 363698007 |Finding site| = 31156008 |Structure of left half of body|");
  }

  private void p(String expression) throws FHIRException {
    Assert.assertNotNull("must be present", SnomedExpressions.parse(expression));
    
  }

}
