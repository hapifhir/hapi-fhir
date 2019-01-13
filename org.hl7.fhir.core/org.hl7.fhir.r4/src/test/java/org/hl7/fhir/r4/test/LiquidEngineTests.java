package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections4.map.HashedMap;
import org.fhir.ucum.UcumEssenceService;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.LiquidEngine;
import org.hl7.fhir.r4.utils.LiquidEngine.ILiquidEngineIcludeResolver;
import org.hl7.fhir.r4.utils.LiquidEngine.LiquidDocument;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junit.framework.Assert;

@RunWith(Parameterized.class)
public class LiquidEngineTests implements ILiquidEngineIcludeResolver {

  private static Map<String, Resource> resources = new HashedMap<>();
  private static JsonObject testdoc = null;
  
  private JsonObject test;
  private LiquidEngine engine;
  
  @Parameters(name = "{index}: file{0}")
  public static Iterable<Object[]> data() throws ParserConfigurationException, SAXException, IOException {
    testdoc = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.fileToString(Utilities.path(TestingUtilities.home(), "tests", "resources", "liquid-tests.json")));
    JsonArray tests = testdoc.getAsJsonArray("tests");
    List<Object[]> objects = new ArrayList<Object[]>(tests.size());
    for (JsonElement n : tests) {
      objects.add(new Object[] {n});
    }
    return objects;
  }

  public LiquidEngineTests(JsonObject test) {
    super();
    this.test = test;
  }


  @Before
  public void setUp() throws Exception {
    if (TestingUtilities.context == null) {
      SimpleWorkerContext wc = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.content(), "definitions.xml.zip"));
      wc.setUcumService(new UcumEssenceService(Utilities.path(TestingUtilities.home(), "tests", "ucum-essence.xml")));
      TestingUtilities.context = wc;
    }
    engine = new LiquidEngine(TestingUtilities.context, null);
    engine.setIncludeResolver(this);
  }

  @Override
  public String fetchInclude(LiquidEngine engine, String name) {
    if (test.has("includes") && test.getAsJsonObject("includes").has(name))
      return test.getAsJsonObject("includes").get(name).getAsString();
    else
      return null;
  }

  private Resource loadResource() throws IOException, FHIRFormatError {
    String name = test.get("focus").getAsString();
    if (!resources.containsKey(name)) {
      String fn = Utilities.path(TestingUtilities.content(), name.replace("/", "-")+".xml");
      resources.put(name, new XmlParser().parse(new FileInputStream(fn)));
    }
    return resources.get(test.get("focus").getAsString());
  }

  
  @Test
  public void test() throws Exception {
    LiquidDocument doc = engine.parse(test.get("template").getAsString(), "test-script");
    String output = engine.evaluate(doc, loadResource(), null);
    Assert.assertTrue(test.get("output").getAsString().equals(output));
  }

}
