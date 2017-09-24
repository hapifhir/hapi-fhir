package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.graphql.EGraphEngine;
import org.hl7.fhir.utilities.graphql.EGraphQLException;
import org.hl7.fhir.utilities.graphql.Package;
import org.hl7.fhir.utilities.graphql.Parser;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class GraphQLParserTests {

  @Parameters(name = "{index}: {0}")
  public static Iterable<Object[]> data() throws FileNotFoundException, IOException  {
    String src = TextFile.fileToString(Utilities.path(TestingUtilities.home(), "tests", "graphql", "parser-tests.gql"));
    String[] tests = src.split("###");
    int i = 0;
    for (String s : tests) 
      if (!Utilities.noString(s.trim()))
        i++;
    List<Object[]> objects = new ArrayList<Object[]>(i);
    i = 0;
    for (String s : tests) {
      if (!Utilities.noString(s.trim())) {
        int l = s.indexOf('\r');        
        objects.add(new Object[] { s.substring(0,  l), s.substring(l+2).trim()});
      }
    }
    return objects;
  }

  private final String test;
  private final String name;

  public GraphQLParserTests(String name, String test) {
    this.name = name;
    this.test = test;
  }

  @Test
  public void test() throws IOException, EGraphQLException, EGraphEngine {
    Package doc = Parser.parse(test);
    Assert.assertTrue(doc != null);
  }


}
