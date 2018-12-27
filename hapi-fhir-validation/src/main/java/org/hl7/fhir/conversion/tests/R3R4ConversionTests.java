package org.hl7.fhir.conversion.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.convertors.R3ToR4Loader;
import org.hl7.fhir.r4.context.BaseWorkerContext;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceFactory;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.model.Factory;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.r4.utils.IResourceValidator.IValidatorResourceFetcher;
import org.hl7.fhir.r4.utils.IResourceValidator.ReferenceValidationPolicy;
import org.hl7.fhir.r4.utils.StructureMapUtilities;
import org.hl7.fhir.r4.utils.StructureMapUtilities.ITransformerServices;
import org.hl7.fhir.r4.utils.StructureMapUtilities.TransformContext;
import org.hl7.fhir.r4.validation.InstanceValidator;
import org.hl7.fhir.r4.validation.InstanceValidatorFactory;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.IniFile;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@RunWith(Parameterized.class)
public class R3R4ConversionTests implements ITransformerServices, IValidatorResourceFetcher {

  private static final boolean SAVING = true;

  @Parameters(name = "{index}: id {0}")
  public static Iterable<Object[]> data() throws ParserConfigurationException, SAXException, IOException {
    if (!(new File(Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "outcomes.json")).exists()))
      throw new Error("You must set the default directory to the build directory when you execute these tests");
    r3r4Outcomes = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.fileToString(Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "outcomes.json")));
    rules = new IniFile(Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-rules.ini"));

    String srcFile = Utilities.path(TestingUtilities.home(), "source", "release3", "examples.zip");
    ZipInputStream stream = new ZipInputStream(new FileInputStream(srcFile));

    filter = System.getProperty("resource");
    if (filter != null)
      filter = filter.toLowerCase();
    Map<String, byte[]> examples = new HashMap<String, byte[]>();
    ZipEntry entry;
    while((entry = stream.getNextEntry())!=null) {
      String n = entry.getName();
      byte[] buffer = new byte[2048];
      ByteArrayOutputStream output = null;
      output = new ByteArrayOutputStream();
      int len = 0;
      while ((len = stream.read(buffer)) > 0)
        output.write(buffer, 0, len);
      if (Utilities.noString(filter) || n.contains(filter))
        examples.put(n, output.toByteArray());
    }
    List<String> names = new ArrayList<String>(examples.size());
    names.addAll(examples.keySet());
    Collections.sort(names);

    List<Object[]> objects = new ArrayList<Object[]>(examples.size());

    for (String id : names) {
      objects.add(new Object[] { id, examples.get(id)});
    }

    return objects;
  }

  private static SimpleWorkerContext contextR3;
  private static SimpleWorkerContext contextR4;
  private static JsonObject r3r4Outcomes;
  private static IniFile rules;
  private List<Resource> extras;
  private final byte[] content;
  private final String name;
  private String workingid;
  private static Map<String, Exception> loadErrors = new HashMap<String, Exception>();
  private static String filter;

  public R3R4ConversionTests(String name, byte[] content) {
    this.name = name;
    this.content = content;
  }

  @SuppressWarnings("deprecation")
  @Test
  public void test() throws Exception {
    checkLoad();
    StructureMapUtilities smu4 = new StructureMapUtilities(contextR4, this);
    StructureMapUtilities smu3 = new StructureMapUtilities(contextR3, this);
    String tn = null;
    workingid = null;

    byte[] cnt = content;

    Exception executionError = null;
    List<ValidationMessage> r4validationErrors = new ArrayList<ValidationMessage>();
    String roundTripError = null;
    try {
      extras = new ArrayList<Resource>();

      // load the example (r3)
      org.hl7.fhir.r4.elementmodel.Element r3 = new org.hl7.fhir.r4.elementmodel.XmlParser(contextR3).parse(new ByteArrayInputStream(content));
      tn = r3.fhirType();
      workingid = r3.getChildValue("id");
      if (SAVING) {
        ByteArrayOutputStream bso = new ByteArrayOutputStream();
        new org.hl7.fhir.r4.elementmodel.JsonParser(contextR3).compose(r3, bso, OutputStyle.PRETTY, null);
        cnt = bso.toByteArray();
        Utilities.createDirectory(Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-output"));
        TextFile.bytesToFile(cnt, Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-output", tn+"-"+workingid+".input.json"));
      }

      // load the r3 to R4 map
      String mapFile = Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "R3toR4", r3.fhirType()+".map");
      if (new File(mapFile).exists()) {
        StructureMap sm = smu4.parse(TextFile.fileToString(mapFile), mapFile);
        tn = smu4.getTargetType(sm).getType();

        // convert from r3 to r4
        Resource r4 = ResourceFactory.createResource(tn);
        smu4.transform(contextR4, r3, sm, r4);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        new org.hl7.fhir.r4.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(bs, r4);
        if (SAVING) {
          TextFile.bytesToFile(bs.toByteArray(), Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-output", tn+"-"+workingid+".r4.json"));
          for (Resource r : extras) {
            bs = new ByteArrayOutputStream();
            new org.hl7.fhir.r4.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(bs, r);
            TextFile.bytesToFile(bs.toByteArray(), Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-output", r.fhirType()+"-"+r.getId()+".r4.json"));
          }
        }

        // validate against R4
        IResourceValidator validator = contextR4.newValidator();
        validator.setNoTerminologyChecks(true);
        validator.setFetcher(this);
        validator.validate(null, r4validationErrors, r4);

        // load the R4 to R3 map
        //      mapFile = Utilities.path(root, "implementations", "r3maps", "R4toR3", r3.fhirType()+".map");
        //      s = sm.parse(TextFile.fileToString(mapFile));
        mapFile = Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "R4toR3", getMapFor(r4.fhirType(), r3.fhirType())+".map");
        sm = smu3.parse(TextFile.fileToString(mapFile), mapFile);

        // convert to R3
        StructureDefinition sd = smu3.getTargetType(sm);
        org.hl7.fhir.r4.elementmodel.Element ro3 = Manager.build(contextR3, sd);
        smu3.transform(contextR3, r4, sm, ro3);

        // compare the XML
        bs = new ByteArrayOutputStream();
        new org.hl7.fhir.r4.elementmodel.JsonParser(contextR3).compose(ro3, bs, OutputStyle.PRETTY, null);
        if (SAVING)
          TextFile.bytesToFile(bs.toByteArray(), Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "test-output", tn+"-"+workingid+".output.json"));

        //        check(errors, tn, workingid);
        roundTripError = TestingUtilities.checkJsonSrcIsSame(new String(cnt), new String(bs.toByteArray()), filter != null);
        if (roundTripError != null && roundTripError.equals(rules.getStringProperty(tn+"/"+workingid, "roundtrip")))
          roundTripError = null;
      } else {
        if (loadErrors.containsKey(r3.fhirType()+".map")) {
          executionError = loadErrors.get(r3.fhirType()+".map");
        }
      }
    } catch (Exception e) {
      executionError = e;
    }
    if (tn != null && workingid != null)
      updateOutcomes(tn, workingid, executionError, r4validationErrors, roundTripError);
    if (executionError != null)
      throw executionError;
  }

  private String getMapFor(String r4, String r3) {
    if (!r4.equals("ServiceRequest"))
      return r4;
    if (r3.equals("ReferralRequest"))
      return "ServiceRequestRR";
//    if (r3.equals("ProcedureRequest"))
      return "ServiceRequestPR";
  }

  private void updateOutcomes(String tn, String id, Exception executionError, List<ValidationMessage> r4validationErrors, String roundTripError) throws IOException {
    JsonObject r = r3r4Outcomes.getAsJsonObject(tn);
    if (r == null) {
      r = new JsonObject();
      r3r4Outcomes.add(tn, r);
    }
    JsonObject i = r.getAsJsonObject(id);
    if (i == null) {
      i = new JsonObject();
      r.add(id, i);
    }
    // now, update with outcomes
    // execution
    if (i.has("execution"))
      i.remove("execution");
    if (executionError == null)
      i.addProperty("execution", true);
    else
      i.addProperty("execution", executionError.getMessage());
    // round-trip check  
    if (i.has("round-trip"))
      i.remove("round-trip");
    if (roundTripError != null)
      i.addProperty("round-trip", roundTripError);
    // r4.validation errors
    if (i.has("r4.errors")) {
      i.remove("r4.errors");
    }
    if (r4validationErrors.size() > 0) {
      JsonArray arr = new JsonArray();
      i.add("r4.errors", arr);
      for (ValidationMessage e : r4validationErrors)
        arr.add(new JsonPrimitive(e.summary()));      
    }
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(r3r4Outcomes);
    TextFile.stringToFile(json, (Utilities.path(TestingUtilities.home(), "implementations", "r3maps", "outcomes.json")));

  }

  private void check(List<ValidationMessage> errors, String tn, String id) throws FHIRException {
    StringBuilder b = new StringBuilder();
    for (ValidationMessage vm : errors) {
      if (vm.getMessage() == null)
        break;
      if (vm.getMessage().contains("Error null validating Coding"))
        break;
      String s = rules.getStringProperty(tn+"/"+id, "validation");
      if (!Utilities.noString(s)) {
        boolean ok = false;
        for (String m : s.split("\\;"))
          if (vm.getMessage().contains(m.trim()))
            ok = true;
        if (ok)
          break;
      }
      s = rules.getStringProperty(tn, "validation");
      if (!Utilities.noString(s)) {
        boolean ok = false;
        for (String m : s.split("\\;"))
          if (vm.getMessage().contains(m.trim()))
            ok = true;
        if (ok)
          break;
      }
      if (vm.getLevel() == IssueSeverity.ERROR || vm.getLevel() == IssueSeverity.FATAL) {
        b.append("[R4 validation error] "+vm.getLocation()+": "+vm.getMessage()+"\r\n");
      }
    }
    if (b.length() > 0)
      throw new FHIRException(b.toString());
  }

  /*
   * Supporting multiple versions at once is a little tricky. We're going to have 2 contexts:
   * - an R3 context which is used to read/write R3 instances 
   * - an R4 context which is used to perform the transforms
   * 
   * R3 structure definitions are cloned into R3 context with a modified URL (as 3.0/)  
   *  
   */
  private void checkLoad() throws IOException, FHIRException {
    if (contextR3 != null)
      return;
    R3ToR4Loader ldr = new R3ToR4Loader().setPatchUrls(true).setKillPrimitives(true);
    System.out.println("loading R3");
    contextR3 = new SimpleWorkerContext();
    contextR3.setAllowLoadingDuplicates(true);
    contextR3.setOverrideVersionNs("http://hl7.org/fhir/3.0/StructureDefinition");
    contextR3.loadFromFile(Utilities.path(TestingUtilities.home(),"source","release3","profiles-types.xml"), ldr);
    contextR3.loadFromFile(Utilities.path(TestingUtilities.home(),"source","release3","profiles-resources.xml"), ldr);
    contextR3.loadFromFile(Utilities.path(TestingUtilities.home(),"source","release3","expansions.xml"), ldr);

    System.out.println("loading R4");
    contextR4 = new SimpleWorkerContext();
    contextR4.setAllowLoadingDuplicates(true);
    contextR4.loadFromFile(Utilities.path(TestingUtilities.content(),"profiles-types.xml"), null);
    contextR4.loadFromFile(Utilities.path(TestingUtilities.content(),"profiles-resources.xml"), null);
    contextR4.loadFromFile(Utilities.path(TestingUtilities.content(),"extension-definitions.xml"), null);
    contextR4.loadFromFile(Utilities.path(TestingUtilities.content(),"valuesets.xml"), null);
    contextR4.setCanRunWithoutTerminology(true);

    for (StructureDefinition sd : contextR3.allStructures()) {
      StructureDefinition sdn = sd.copy();
      sdn.getExtension().clear();
      contextR4.cacheResource(sdn);
    }

    for (StructureDefinition sd : contextR4.allStructures()) {
      if (sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE) {
        contextR3.cacheResource(sd);
        StructureDefinition sdn = sd.copy();
        sdn.setUrl(sdn.getUrl().replace("http://hl7.org/fhir/", "http://hl7.org/fhir/3.0/"));
        sdn.addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace").setValue(new UriType("http://hl7.org/fhir"));
        contextR3.cacheResource(sdn);
        contextR4.cacheResource(sdn);
      }
    }

    contextR3.setExpansionProfile(new org.hl7.fhir.r4.model.Parameters());
    contextR4.setExpansionProfile(new org.hl7.fhir.r4.model.Parameters());
    contextR3.setName("R3");
    contextR4.setName("R4");
    contextR4.setValidatorFactory(new InstanceValidatorFactory());

    System.out.println("loading Maps");
    loadLib(Utilities.path(TestingUtilities.home(),"implementations","r3maps", "R3toR4"));
    loadLib(Utilities.path(TestingUtilities.home(),"implementations","r3maps", "R4toR3"));
    System.out.println("loaded");
  }

  private void loadLib(String dir) throws FileNotFoundException, IOException {
    StructureMapUtilities smu = new StructureMapUtilities(contextR4);
    for (String s : new File(dir).list()) {
      String map = TextFile.fileToString(Utilities.path(dir, s));
      try {
        StructureMap sm = smu.parse(map, s);
        contextR3.cacheResource(sm);
        contextR4.cacheResource(sm);
        for (Resource r : sm.getContained()) {
          if (r instanceof MetadataResource) {
            MetadataResource mr = (MetadataResource) r.copy();
            mr.setUrl(sm.getUrl()+"#"+r.getId());
            contextR3.cacheResource(mr);
            contextR4.cacheResource(mr);
          }
        }
      } catch (FHIRException e) {
        System.out.println("Unable to load "+Utilities.path(dir, s)+": "+e.getMessage());
        loadErrors.put(s, e);
        //        e.printStackTrace();
      }
    }
  }

  @Override
  public void log(String message) {
    System.out.println(message);
  }

  @Override
  public Base createResource(Object appInfo, Base res, boolean atRootofTransform) {
    if (res instanceof Resource && (res.fhirType().equals("CodeSystem") || res.fhirType().equals("CareTeam")) || res.fhirType().equals("PractitionerRole")) {
      Resource r = (Resource) res;
      extras.add(r);
      r.setId(workingid+"-"+extras.size());
    }
    return res;
  }

  @Override
  public Coding translate(Object appInfo, Coding source, String conceptMapUrl) throws FHIRException {
    throw new Error("translate not done yet");
  }

  @Override
  public Base createType(Object appInfo, String name) throws FHIRException {
    BaseWorkerContext context = (BaseWorkerContext) appInfo;
    if (context == contextR3) {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/3.0/StructureDefinition/"+name);
      if (sd == null)
        throw new FHIRException("Type not found: '"+name+"'");
      return Manager.build(context, sd);
    } else
      return ResourceFactory.createResourceOrType(name);
  }

  @Override
  public Base resolveReference(Object appContext, String url) {
    for (Resource r : extras) {
      if (r instanceof MetadataResource) {
        MetadataResource mr = (MetadataResource) r;
        if (url.equals(mr.getUrl()))
          return mr;
      }
      if (url.equals(r.fhirType()+"/"+r.getId()))
        return r;
    }

    return null;
  }

  @Override
  public List<Base> performSearch(Object appContext, String url) {
    List<Base> results = new ArrayList<Base>();
    String[] parts = url.split("\\?");
    if (parts.length == 2 && parts[0].substring(1).equals("PractitionerRole")) {
      String[] vals = parts[1].split("\\=");
      if (vals.length == 2 && vals[0].equals("practitioner"))
        for (Resource r : extras) {
          if (r instanceof PractitionerRole && ((PractitionerRole) r).getPractitioner().getReference().equals("Practitioner/"+vals[1])) {
            results.add(r);
          }
        }
    }
    return results;
  }

  @Override
  public Element fetch(Object appContext, String url) throws FHIRFormatError, DefinitionException, IOException, FHIRException {
    return null;
  }

  @Override
  public ReferenceValidationPolicy validationPolicy(Object appContext, String path, String url) {
    return ReferenceValidationPolicy.IGNORE;
  }

  @Override
  public boolean resolveURL(Object appContext, String path, String url) throws IOException, FHIRException {
    return true;
  }


}
