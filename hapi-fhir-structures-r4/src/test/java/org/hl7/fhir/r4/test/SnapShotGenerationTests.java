package org.hl7.fhir.r4.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ExpressionNode;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.ExpressionNode.CollectionStatus;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.TestScript;
import org.hl7.fhir.r4.model.TestScript.SetupActionAssertComponent;
import org.hl7.fhir.r4.model.TestScript.SetupActionComponent;
import org.hl7.fhir.r4.model.TestScript.SetupActionOperationComponent;
import org.hl7.fhir.r4.model.TestScript.TestActionComponent;
import org.hl7.fhir.r4.model.TestScript.TestScriptFixtureComponent;
import org.hl7.fhir.r4.model.TestScript.TestScriptTestComponent;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.utils.CodingUtilities;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import junit.framework.Assert;

@RunWith(Parameterized.class)
public class SnapShotGenerationTests {

  private static class SnapShotGenerationTestsContext implements IEvaluationContext {
    private Map<String, Resource> fixtures;
    private Map<String, StructureDefinition> snapshots = new HashMap<String, StructureDefinition>();
    public TestScript tests;

    public void checkTestsDetails() {
      if (!"http://hl7.org/fhir/tests/snapshotgeneration".equals(tests.getUrl()))
        throw new Error("Wrong URL on test script");
      if (!tests.getSetup().isEmpty())
        throw new Error("Setup is not supported");
      if (!tests.getTeardown().isEmpty())
        throw new Error("Teardown is not supported");
      Set<String> ids = new HashSet<String>();
      Set<String> urls = new HashSet<String>();
      for (Resource r : tests.getContained()) {
        if (ids.contains(r.getId()))
          throw new Error("Unsupported: duplicate contained resource on fixture id  "+r.getId());
        ids.add(r.getId());
        if (r instanceof MetadataResource) {
          MetadataResource md = (MetadataResource) r;
          if (urls.contains(md.getUrl()))
            throw new Error("Unsupported: duplicate canonical url "+md.getUrl()+" on fixture id  "+r.getId());
          urls.add(md.getUrl());
        }
      }
      for (TestScriptFixtureComponent r : tests.getFixture()) {
        if (ids.contains(r.getId()))
          throw new Error("Unsupported: duplicate contained resource or fixture id  "+r.getId());
        ids.add(r.getId());
      }
      Set<String> names = new HashSet<String>();
      for (TestScriptTestComponent test : tests.getTest()) {
        if (names.contains(test.getName()))
          throw new Error("Unsupported: duplicate name "+test.getName());
        names.add(test.getName());
        if (test.getAction().size() < 2)
          throw new Error("Unsupported: multiple actions required");
        if (!test.getActionFirstRep().hasOperation())
          throw new Error("Unsupported: first action must be an operation");
        for (int i = 0; i < test.getAction().size(); i++) {
//          if (!test.getAction().get(i).hasAssert())
//            throw new Error("Unsupported: following actions must be an asserts");
          TestActionComponent action = test.getAction().get(i);
          if (action.hasOperation()) {
            SetupActionOperationComponent op = test.getActionFirstRep().getOperation();
            if (!CodingUtilities.matches(op.getType(), "http://hl7.org/fhir/testscript-operation-codes", "snapshot")
            && !CodingUtilities.matches(op.getType(), "http://hl7.org/fhir/testscript-operation-codes", "sortDifferential"))
              throw new Error("Unsupported action operation type "+CodingUtilities.present(op.getType()));
            if (!"StructureDefinition".equals(op.getResource()))
              throw new Error("Unsupported action operation resource "+op.getResource());
            if (!op.hasResponseId())
              throw new Error("Unsupported action operation: no response id");
            if (!op.hasSourceId())
              throw new Error("Unsupported action operation: no source id");
            if (!hasSource(op.getSourceId()))
              throw new Error("Unsupported action operation: source id could not be resolved");
          } else if (action.hasAssert()) {
            SetupActionAssertComponent a = action.getAssert();
            if (!a.hasLabel())
              throw new Error("Unsupported: actions must have a label");
            if (!a.hasDescription())
              throw new Error("Unsupported: actions must have a description");
            if (!a.hasExpression())
              throw new Error("Unsupported: actions must have an expression");
          } else {
            throw new Error("Unsupported: Unrecognized action type");            
          }
        }
      }
    }

    private boolean hasSource(String sourceId) {
      for (TestScriptFixtureComponent ds : tests.getFixture()) {
        if (sourceId.equals(ds.getId()))
          return true;
      }
      for (Resource r : tests.getContained()) {
        if (sourceId.equals(r.getId()))
          return true;
      }
      return false;
    }

    public Resource fetchFixture(String id) {
      if (fixtures.containsKey(id))
        return fixtures.get(id);
      
      for (TestScriptFixtureComponent ds : tests.getFixture()) {
        if (id.equals(ds.getId()))
          throw new Error("not done yet");
      }
      for (Resource r : tests.getContained()) {
        if (id.equals(r.getId()))
          return r;
      }
      return null;
    }

    // FHIRPath methods
    @Override
    public Base resolveConstant(Object appContext, String name) throws PathEngineException {
      return null;
    }

    @Override
    public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
      return null;
    }

    @Override
    public boolean log(String argument, List<Base> focus) {
      System.out.println(argument+": "+fp.convertToString(focus));
      return true;
    }

    @Override
    public FunctionDetails resolveFunction(String functionName) {
      if ("fixture".equals(functionName))
        return new FunctionDetails("Access a fixture defined in the testing context", 0, 1);
      return null;
    }

    @Override
    public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
      if ("fixture".equals(functionName))
        return new TypeDetails(CollectionStatus.SINGLETON, TestingUtilities.context.getResourceNamesAsSet());
      return null;
    }

    @Override
    public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
      if ("fixture".equals(functionName)) {
        String id = fp.convertToString(parameters.get(0));
        Resource res = fetchFixture(id);
        if (res != null) {
          List<Base> list = new ArrayList<Base>();
          list.add(res);
          return list;
        }
      }
      return null;
    }

    @Override
    public Base resolveReference(Object appContext, String url) {
      // TODO Auto-generated method stub
      return null;
    }

  }


  private static FHIRPathEngine fp;

  @Parameters(name = "{index}: file {0}")
  public static Iterable<Object[]> data() throws ParserConfigurationException, IOException, FHIRFormatError {
    SnapShotGenerationTestsContext context = new SnapShotGenerationTestsContext();
    context.tests = (TestScript) new XmlParser().parse(new FileInputStream(Utilities.path(TestingUtilities.home(), "tests", "resources", "snapshot-generation-tests.xml")));

    context.checkTestsDetails();

    List<Object[]> objects = new ArrayList<Object[]>(context.tests.getTest().size());

    for (TestScriptTestComponent e : context.tests.getTest()) {
      objects.add(new Object[] { e.getName(), e, context });
    }
    return objects;
  }


  private final TestScriptTestComponent test;
  private final String name;
  private SnapShotGenerationTestsContext context;

  public SnapShotGenerationTests(String name, TestScriptTestComponent e, SnapShotGenerationTestsContext context) {
    this.name = name;
    this.test = e;
    this.context = context;
  }

  @SuppressWarnings("deprecation")
  @Test
  public void test() throws FileNotFoundException, IOException, FHIRException, org.hl7.fhir.exceptions.FHIRException {
    if (TestingUtilities.context == null)
      TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.home(), "publish", "definitions.xml.zip"));
    if (fp == null)
      fp = new FHIRPathEngine(TestingUtilities.context);
    fp.setHostServices(context);

    resolveFixtures();
    
    for (int i = 0; i < test.getAction().size(); i++) {
      TestActionComponent action = test.getAction().get(i);
      if (action.hasOperation()) {
        SetupActionOperationComponent op = action.getOperation();
        Coding opType = op.getType();
        if (opType.getSystem().equals("http://hl7.org/fhir/testscript-operation-codes") && opType.getCode().equals("snapshot")) {
          StructureDefinition source = (StructureDefinition) context.fetchFixture(op.getSourceId());
          StructureDefinition base = getSD(source.getBaseDefinition()); 
          StructureDefinition output = source.copy();
          ProfileUtilities pu = new ProfileUtilities(TestingUtilities.context, null, null);
          pu.setIds(source, false);
          if ("sort=true".equals(op.getParams())) {
            List<String> errors = new ArrayList<String>();
            pu.sortDifferential(base, output, source.getName(), errors);
            if (errors.size() > 0)
              throw new FHIRException("Sort failed: "+errors.toString());
          }
          pu.generateSnapshot(base, output, source.getUrl(), source.getName());
          context.fixtures.put(op.getResponseId(), output);
          context.snapshots.put(output.getUrl(), output);
          
          new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(System.getProperty("java.io.tmpdir"), op.getResponseId()+".xml")), output);
          
        } else if (opType.getSystem().equals("http://hl7.org/fhir/testscript-operation-codes") && opType.getCode().equals("sortDifferential")) {
          StructureDefinition source = (StructureDefinition) context.fetchFixture(op.getSourceId());
          StructureDefinition base = getSD(source.getBaseDefinition()); 
          StructureDefinition output = source.copy();
          ProfileUtilities pu = new ProfileUtilities(TestingUtilities.context, null, null);
          pu.setIds(source, false);
          List<String> errors = new ArrayList<String>();          
          pu.sortDifferential(base, output, output.getUrl(), errors);
          if (!errors.isEmpty())
            throw new FHIRException(errors.get(0));
          context.fixtures.put(op.getResponseId(), output);
          context.snapshots.put(output.getUrl(), output);
          
          new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(System.getProperty("java.io.tmpdir"), op.getResponseId()+".xml")), output);
            
        } else {
          throw new Error("Unsupported operation: " + opType.getSystem() + " : " + opType.getCode());
        }
      } else if (action.hasAssert()) {
        SetupActionAssertComponent a = action.getAssert();
        Assert.assertTrue(a.getLabel()+": "+a.getDescription(), fp.evaluateToBoolean(new StructureDefinition(), new StructureDefinition(), a.getExpression()));
      }
    }
  }


  private StructureDefinition getSD(String url) throws DefinitionException, FHIRException {
    StructureDefinition sd = TestingUtilities.context.fetchResource(StructureDefinition.class, url);
    if (sd == null)
      sd = context.snapshots.get(url);
    if (sd == null)
      sd = findContainedProfile(url);
    return sd;
  }

  private StructureDefinition findContainedProfile(String url) throws DefinitionException, FHIRException {
    for (Resource r : context.tests.getContained()) {
      if (r instanceof StructureDefinition) {
        StructureDefinition sd = (StructureDefinition) r;
        if  (sd.getUrl().equals(url)) {
          StructureDefinition p = sd.copy();
          ProfileUtilities pu = new ProfileUtilities(TestingUtilities.context, null, null);
          pu.setIds(p, false);
          List<String> errors = new ArrayList<String>();          
          pu.sortDifferential(getSD(p.getBaseDefinition()), p, url, errors);
          if (!errors.isEmpty())
            throw new FHIRException(errors.get(0));
          pu.generateSnapshot(getSD(p.getBaseDefinition()), p, p.getUrl(), p.getName());
          return p;
        }
      }
    }
    return null;
  }

  private void resolveFixtures() {
    if (context.fixtures == null) {
      context.fixtures = new HashMap<String, Resource>();
      for (TestScriptFixtureComponent fd : context.tests.getFixture()) {
        Resource r = TestingUtilities.context.fetchResource(Resource.class, fd.getResource().getReference());
        context.fixtures.put(fd.getId(), r);
      }
    }

  }
}
