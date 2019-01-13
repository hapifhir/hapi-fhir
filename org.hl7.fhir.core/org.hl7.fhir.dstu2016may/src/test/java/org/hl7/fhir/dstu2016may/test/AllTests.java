package org.hl7.fhir.dstu2016may.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BaseDateTimeTypeTest.class, FluentPathTests.class, 
    ShexGeneratorTests.class, StructureMapTests.class, RoundTripTest.class })
public class AllTests {

}
