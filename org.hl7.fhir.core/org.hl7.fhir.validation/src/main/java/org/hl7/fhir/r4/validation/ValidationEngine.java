package org.hl7.fhir.r4.validation;

/*-
 * #%L
 * org.hl7.fhir.validation
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.convertors.R2016MayToR4Loader;
import org.hl7.fhir.convertors.R2ToR4Loader;
import org.hl7.fhir.convertors.R3ToR4Loader;
import org.hl7.fhir.convertors.TerminologyClientFactory;
import org.hl7.fhir.convertors.VersionConvertorAdvisor40;
import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.convertors.VersionConvertor_14_40;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.context.SimpleWorkerContext.IContextResourceLoader;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.FormatUtilities;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.RdfParser;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Constants;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.FhirPublication;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceFactory;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.terminologies.ConceptMapEngine;
import org.hl7.fhir.r4.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r4.utils.IResourceValidator.CheckDisplayOption;
import org.hl7.fhir.r4.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.r4.utils.NarrativeGenerator;
import org.hl7.fhir.r4.utils.OperationOutcomeUtilities;
import org.hl7.fhir.r4.utils.StructureMapUtilities;
import org.hl7.fhir.r4.utils.StructureMapUtilities.ITransformerServices;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.utils.ValidationProfileSet;
import org.hl7.fhir.utilities.IniFile;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.hl7.fhir.utilities.cache.ToolsVersion;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.xml.sax.SAXException;

/**
 * This is just a wrapper around the InstanceValidator class for convenient use 
 * 
 * The following resource formats are supported: XML, JSON, Turtle
 * The following versions are supported: 1.0.1, 1.4.0, 3.0.1, and current
 * 
 * Note: the validation engine is intended to be threadsafe
 * To Use:
 *  
 * 1/ Initialize
 *    ValidationEngine validator = new ValidationEngine(src);
 *      - this must refer to the igpack.zip for the version of the spec against which you want to validate
 *       it can be a url or a file reference. It can nominate the igpack.zip directly, 
 *       or it can name the container alone (e.g. just the spec URL).
 *       The validation engine does not cache igpack.zip. the user must manage that if desired 
 *
 *    validator.connectToTSServer(txServer);
 *      - this is optional; in the absence of a terminology service, snomed, loinc etc will not be validated
 *      
 *    validator.loadIg(src);
 *      - call this any number of times for the Implementation Guide(s) of interest. This is a reference
 *        to the igpack.zip for the implementation guide - same rules as above
 *        the version of the IGPack must match that of the spec 
 *        Alternatively it can point to a local folder that contains conformance resources.
 *         
 *    validator.loadQuestionnaire(src)
 *      - url or filename of a questionnaire to load. Any loaded questionnaires will be used while validating
 *      
 *    validator.setNative(doNative);
 *      - whether to do xml/json/rdf schema validation as well
 *
 *   You only need to do this initialization once. You can validate as many times as you like
 *   
 * 2. validate
 *    validator.validate(src, profiles);
 *      - source (as stream, byte[]), or url or filename of a resource to validate. 
 *        Also validate against any profiles (as canonical URLS, equivalent to listing them in Resource.meta.profile)
 *        
 *        if the source is provided as byte[] or stream, you need to provide a format too, though you can 
 *        leave that as null, and the validator will guess
 * 
 * 3. Or, instead of validating, transform        
 * @author Grahame Grieve
 *
 */
public class ValidationEngine {

	public class TransformSupportServices implements ITransformerServices {

    private List<Resource> outputs;

    public TransformSupportServices(List<Resource> outputs) {
      this.outputs = outputs;
    }

    @Override
    public void log(String message) {
      if (mapLog != null)
        mapLog.println(message);
      System.out.println(message);
    }

    @Override
    public Base createType(Object appInfo, String name) throws FHIRException {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, name);
      if (sd != null && sd.getKind() == StructureDefinitionKind.LOGICAL) {
        return Manager.build(context, sd); 
      } else {
        if (name.startsWith("http://hl7.org/fhir/StructureDefinition/"))
          name = name.substring("http://hl7.org/fhir/StructureDefinition/".length());
        return ResourceFactory.createResourceOrType(name);
      }
    }

    @Override
    public Base createResource(Object appInfo, Base res, boolean atRootofTransform) {
      if (atRootofTransform)
        outputs.add((Resource) res);
      return res;
    }

    @Override
    public Coding translate(Object appInfo, Coding source, String conceptMapUrl) throws FHIRException {
      ConceptMapEngine cme = new ConceptMapEngine(context);
      return cme.translate(source, conceptMapUrl);
    }

    @Override
    public Base resolveReference(Object appContext, String url) throws FHIRException {
      throw new FHIRException("resolveReference is not supported yet");
    }

    @Override
    public List<Base> performSearch(Object appContext, String url) throws FHIRException {
      throw new FHIRException("performSearch is not supported yet");
    }

  }
	
  private SimpleWorkerContext context;
//  private FHIRPathEngine fpe;
  private Map<String, byte[]> binaries = new HashMap<String, byte[]>();
  private boolean doNative;
  private boolean noInvariantChecks;
  private boolean hintAboutNonMustSupport;
  private boolean anyExtensionsAllowed = false;
  private String version;
  private PackageCacheManager pcm;
  private PrintWriter mapLog;

  private class AsteriskFilter implements FilenameFilter {
    String dir;
    String regex;
    
    public AsteriskFilter(String filter) throws IOException {
      if (!filter.matches("(.*(\\\\|\\/))*(.*)\\*(.*)"))
        throw new IOException("Filter names must have the following syntax: [directorypath][prefix]?*[suffix]?   I.e. The asterisk must be in the filename, not the directory path");
      dir = filter.replaceAll("(.*(\\\\|\\/))*(.*)\\*(.*)", "$1");
      String expression = filter.replaceAll("(.*(\\\\|\\/))*(.*)", "$3");
      regex = "";
      for (int i = 0; i < expression.length(); i++) {
        if (Character.isAlphabetic(expression.codePointAt(i)) || Character.isDigit(expression.codePointAt(i)))
          regex = regex + expression.charAt(i);
        else if (expression.charAt(i)=='*')
          regex = regex + ".*";
        else
          regex = regex + "\\" + expression.charAt(i);
      }
      File f = new File(dir);
      if (!f.exists()) {
        throw new IOException("Directory " + dir + " does not exist");
      }
      if (!f.isDirectory()) {
        throw new IOException("Directory " + dir + " is not a directory");
      }
    }
    
    public boolean accept(File dir, String s) {
      boolean match = s.matches(regex);
      return match;
    }
    
    public String getDir() {
      return dir;
    }
  }
  
  public ValidationEngine() throws IOException {
    pcm = new PackageCacheManager(true, ToolsVersion.TOOLS_VERSION);  
  }
  
  public void loadInitialDefinitions(String src) throws Exception {
    loadDefinitions(src);   
  }
  
  public void setTerminologyServer(String src, String log, FhirPublication version) throws Exception {
    connectToTSServer(src, log, version);   
  }
  
  public boolean isHintAboutNonMustSupport() {
    return hintAboutNonMustSupport;
  }

  public void setHintAboutNonMustSupport(boolean hintAboutNonMustSupport) {
    this.hintAboutNonMustSupport = hintAboutNonMustSupport;
  }

  public boolean isAnyExtensionsAllowed() {
    return anyExtensionsAllowed;
  }

  public void setAnyExtensionsAllowed(boolean anyExtensionsAllowed) {
    this.anyExtensionsAllowed = anyExtensionsAllowed;
  }

  public ValidationEngine(String src, String txsrvr, String txLog, FhirPublication version) throws Exception {
    pcm = new PackageCacheManager(true, ToolsVersion.TOOLS_VERSION);
    loadInitialDefinitions(src);
    setTerminologyServer(txsrvr, txLog, version);
  }
  
  public ValidationEngine(String src) throws Exception {
    loadDefinitions(src);
    pcm = new PackageCacheManager(true, ToolsVersion.TOOLS_VERSION);
  }
  
  private void loadDefinitions(String src) throws Exception {
    Map<String, byte[]> source = loadIgSource(src);   
    if (version == null)
      version = getVersionFromPack(source);
    context = SimpleWorkerContext.fromDefinitions(source, loaderForVersion());
    context.setAllowLoadingDuplicates(true); // because of Forge
    context.setExpansionProfile(makeExpProfile());
    grabNatives(source, "http://hl7.org/fhir");
  }

  private IContextResourceLoader loaderForVersion() {
    if (Utilities.noString(version))
      return null;
    if (version.equals("1.0.2"))
      return new R2ToR4Loader();
    if (version.equals("1.4.0"))
      return new R2016MayToR4Loader(); // special case
    if (version.equals("3.0.1"))
      return new R3ToR4Loader();    
    return null;
  }

  private String getVersionFromPack(Map<String, byte[]> source) {
    if (source.containsKey("version.info")) {
      IniFile vi = new IniFile(new ByteArrayInputStream(removeBom(source.get("version.info"))));
      return vi.getStringProperty("FHIR", "version");
    } else {
      throw new Error("Missing version.info?");
    }
  }

  private byte[] removeBom(byte[] bs) {
    if (bs.length > 3 && bs[0] == -17 && bs[1] == -69 && bs[2] == -65)
      return Arrays.copyOfRange(bs, 3, bs.length);
    else
      return bs;
  }

  private Parameters makeExpProfile() {
    Parameters ep  = new Parameters();
    ep.addParameter("profile-url", "http://hl7.org/fhir/ExpansionProfile/dc8fd4bc-091a-424a-8a3b-6198ef146891"); // change this to blow the cache
    // all defaults....
    return ep;
  }

  private byte[] loadProfileSource(String src) throws Exception {
    if (Utilities.noString(src)) {
      throw new FHIRException("Profile Source '" + src + "' could not be processed");
    } else if (src.startsWith("https:") || src.startsWith("http:")) {
      return loadProfileFromUrl(src);
    } else if (new File(src).exists()) {
      return loadProfileFromFile(src);      
    } else {
      throw new FHIRException("Definitions Source '"+src+"' could not be processed");
    }
  }

  private byte[] loadProfileFromUrl(String src) throws Exception {
    try {
      URL url = new URL(src+"?nocache=" + System.currentTimeMillis());
      URLConnection c = url.openConnection();
      return IOUtils.toByteArray(c.getInputStream());
    } catch (Exception e) {
      throw new Exception("Unable to find definitions at URL '"+src+"': "+e.getMessage(), e);
  }
    }

  private byte[] loadProfileFromFile(String src) throws FileNotFoundException, IOException {
    File f = new File(src);
    if (f.isDirectory()) 
      throw new IOException("You must provide a file name, not a directory name");
    return TextFile.fileToBytes(src);
  }

  private Map<String, byte[]> loadIgSource(String src) throws Exception {
    // src can be one of the following:
    // - a canonical url for an ig - this will be converted to a package id and loaded into the cache
    // - a package id for an ig - this will be loaded into the cache
    // - a direct reference to a package ("package.tgz") - this will be extracted by the cache manager, but not put in the cache
    // - a folder containing resources - these will be loaded directly
    if (src.startsWith("https:") || src.startsWith("http:")) {
      String v = null;
      if (src.contains("|")) {
        v = src.substring(src.indexOf("|")+1);
        src = src.substring(0, src.indexOf("|"));
      }
      String pid = pcm.getPackageId(src);
      if (!Utilities.noString(pid))
        return fetchByPackage(pid+(v == null ? "" : "#"+v));
      else
        return fetchFromUrl(src+(v == null ? "" : "|"+v));
    }
    
    File f = new File(src);
    if (f.exists()) {
      if (f.isDirectory() && new File(Utilities.path(src, "package.tgz")).exists())
        return loadPackage(new FileInputStream(Utilities.path(src, "package.tgz")), Utilities.path(src, "package.tgz"));
      if (f.isDirectory() && new File(Utilities.path(src, "igpack.zip")).exists())
        return readZip(new FileInputStream(Utilities.path(src, "igpack.zip")));
      if (f.isDirectory() && new File(Utilities.path(src, "validator.pack")).exists())
        return readZip(new FileInputStream(Utilities.path(src, "validator.pack")));
      if (f.isDirectory())
        return scanDirectory(f);
      if (src.endsWith(".tgz"))
        return loadPackage(new FileInputStream(src), src);
      if (src.endsWith(".pack"))
        return readZip(new FileInputStream(src));
      if (src.endsWith("igpack.zip"))
        return readZip(new FileInputStream(src));
      FhirFormat fmt = checkIsResource(src);
      if (fmt != null) {
        Map<String, byte[]> res = new HashMap<String, byte[]>();
        res.put(Utilities.changeFileExt(src, "."+fmt.getExtension()), TextFile.fileToBytes(src));
        return res;
      }
    } else if ((src.matches(PackageCacheManager.PACKAGE_REGEX) || src.matches(PackageCacheManager.PACKAGE_VERSION_REGEX)) && !src.endsWith(".zip") && !src.endsWith(".tgz")) {
      return fetchByPackage(src);
    }
    throw new Exception("Unable to find/resolve/read -ig "+src);
  }

  
  private Map<String, byte[]> fetchFromUrl(String src) throws Exception {
    if (src.endsWith(".tgz"))
      return loadPackage(fetchFromUrlSpecific(src, false), src);
    if (src.endsWith(".pack"))
      return readZip(fetchFromUrlSpecific(src, false));
    if (src.endsWith("igpack.zip"))
      return readZip(fetchFromUrlSpecific(src, false));

    InputStream stream = fetchFromUrlSpecific(Utilities.pathURL(src, "package.tgz"), true);
    if (stream != null)
      return loadPackage(stream, Utilities.pathURL(src, "package.tgz"));
    stream = fetchFromUrlSpecific(Utilities.pathURL(src, "igpack.zip"), true);
    if (stream != null)
      return readZip(stream);
    stream = fetchFromUrlSpecific(Utilities.pathURL(src, "validator.pack"), true);
    if (stream != null)
      return readZip(stream);
    stream = fetchFromUrlSpecific(Utilities.pathURL(src, "validator.pack"), true);
    FhirFormat fmt = checkIsResource(stream);
    if (fmt != null) {
      Map<String, byte[]> res = new HashMap<String, byte[]>();
      res.put(Utilities.changeFileExt(src, "."+fmt.getExtension()), TextFile.fileToBytes(src));
      return res;
    }
    throw new Exception("Unable to find/resolve/read -ig "+src);
  }

  private InputStream fetchFromUrlSpecific(String source, boolean optional) throws Exception {
    try {
      URL url = new URL(source+"?nocache=" + System.currentTimeMillis());
      URLConnection c = url.openConnection();
      return c.getInputStream();
    } catch (Exception e) {
      if (optional)
        return null;
      else
        throw e;
    }
  }

  private Map<String, byte[]> scanDirectory(File f) throws FileNotFoundException, IOException {
    Map<String, byte[]> res = new HashMap<String, byte[]>();
    for (File ff : f.listFiles()) {
      FhirFormat fmt = checkIsResource(ff.getAbsolutePath());
      if (fmt != null) {
        res.put(Utilities.changeFileExt(ff.getName(), "."+fmt.getExtension()), TextFile.fileToBytes(ff.getAbsolutePath()));
      }
    }
    return res;
  }


  private Map<String, byte[]> loadPackage(InputStream stream, String name) throws FileNotFoundException, IOException {
    return loadPackage(pcm.extractLocally(stream, name));
  }

  public Map<String, byte[]> loadPackage(NpmPackage pi) throws IOException {
    Map<String, byte[]> res = new HashMap<String, byte[]>();
    for (String s : pi.list("package")) {
      if (s.startsWith("CodeSystem-") || s.startsWith("ConceptMap-") || s.startsWith("ImplementationGuide-") || s.startsWith("StructureMap-") || s.startsWith("ValueSet-") || s.startsWith("StructureDefinition-"))
        res.put(s, TextFile.streamToBytes(pi.load("package", s)));
    }
    String ini = "[FHIR]\r\nversion="+pi.fhirVersion()+"\r\n";
    res.put("version.info", ini.getBytes());
    return res;
  }

  private Map<String, byte[]> readZip(InputStream stream) throws IOException {
    Map<String, byte[]> res = new HashMap<String, byte[]>();
    ZipInputStream zip = new ZipInputStream(stream);
    ZipEntry ze;
    while ((ze = zip.getNextEntry()) != null) {
        String name = ze.getName();
        InputStream in = zip;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        int n;
        byte[] buf = new byte[1024];
        while ((n = in.read(buf, 0, 1024)) > -1) {
          b.write(buf, 0, n);
        }        
      res.put(name, b.toByteArray());
      zip.closeEntry();
    }
    zip.close();    
    return res;
  }

  public void log(String message) {
    System.out.println(message);
  }

  private Map<String, byte[]> fetchByPackage(String src) throws Exception {
    String id = src;
    String version = null;
    if (src.contains("#")) {
      id = src.substring(0, src.indexOf("#"));
      version = src.substring(src.indexOf("#")+1);
    }
    if (pcm == null) {
      log("Creating Package manager?");
      pcm = new PackageCacheManager(true, ToolsVersion.TOOLS_VERSION);
    }
    NpmPackage pi = null;
    if (version == null) {
      pi = pcm.loadPackageCacheLatest(id);
      if (pi != null)
        log("   ... Using version "+pi.version());
    } else
      pi = pcm.loadPackageCache(id, version);
    if (pi == null) {
      return resolvePackage(id, version);
    } else
      return loadPackage(pi);
  }

  private Map<String, byte[]> resolvePackage(String id, String v) throws Exception {
    try {
      pcm.checkBuildLoaded();
    } catch (IOException e) {
      log("Unable to connect to build.fhir.org to check on packages");
    }
    NpmPackage pi = pcm.resolvePackage(id, v, Constants.VERSION);
    if (pi != null && v == null)
      log("   ... Using version "+pi.version());
    return loadPackage(pi);
  }

  public SimpleWorkerContext getContext() {
    return context;
  }
  
 
  public boolean isNoInvariantChecks() {
    return noInvariantChecks;
  }

  public void setNoInvariantChecks(boolean value) {
    this.noInvariantChecks = value;
  }

  private FhirFormat checkIsResource(InputStream stream) {
    try {
      Manager.parse(context, stream, FhirFormat.XML);
      return FhirFormat.XML;
    } catch (Exception e) {
    }
    try {
      Manager.parse(context, stream, FhirFormat.JSON);
      return FhirFormat.JSON;
    } catch (Exception e) {
    }
    try {
      Manager.parse(context, stream, FhirFormat.TURTLE);
      return FhirFormat.TURTLE;
    } catch (Exception e) {
    }
    try {
      new StructureMapUtilities(context, null, null).parse(TextFile.streamToString(stream), null);
      return FhirFormat.TEXT;
    } catch (Exception e) {
    }
    return null;    
  }

  private FhirFormat checkIsResource(String path) throws FileNotFoundException {
    String ext = Utilities.getFileExtension(path);
    if (Utilities.existsInList(ext, "xml")) 
      return FhirFormat.XML;
    if (Utilities.existsInList(ext, "json")) 
      return FhirFormat.JSON;
    if (Utilities.existsInList(ext, "ttl")) 
      return FhirFormat.TURTLE;
    if (Utilities.existsInList(ext, "map")) 
      return FhirFormat.TEXT;

    return checkIsResource(new FileInputStream(path));
	}

  public void connectToTSServer(String url, String log, FhirPublication version) throws URISyntaxException, FHIRException {
    context.setTlogging(false);
    if (url == null) {
      context.setCanRunWithoutTerminology(true);
    } else 
      context.connectToTSServer(TerminologyClientFactory.makeClient(url, version), log);
	}

  public void loadProfile(String src) throws Exception {
    if (context.hasResource(StructureDefinition.class, src))
      return;
    if (context.hasResource(ImplementationGuide.class, src))
      return;
    
    byte[] source = loadProfileSource(src);
    FhirFormat fmt = FormatUtilities.determineFormat(source);
    Resource r = FormatUtilities.makeParser(fmt).parse(source);
    context.cacheResource(r);
  }
  
  public void loadIg(String src) throws IOException, FHIRException, Exception {
    String canonical = null;
    Map<String, byte[]> source = loadIgSource(src);
    String version = Constants.VERSION;
    if (this.version != null)
      version = this.version;
    if (source.containsKey("version.info"))
      version = readInfoVersion(source.get("version.info"));
    
    for (Entry<String, byte[]> t : source.entrySet()) {
      String fn = t.getKey();
      if (!exemptFile(fn)) {
        Resource r = null;
        try { 
          if (version.equals("3.0.1") || version.equals("3.0.0")) {
            org.hl7.fhir.dstu3.model.Resource res;
            if (fn.endsWith(".xml") && !fn.endsWith("template.xml"))
              res = new org.hl7.fhir.dstu3.formats.XmlParser().parse(new ByteArrayInputStream(t.getValue()));
            else if (fn.endsWith(".json") && !fn.endsWith("template.json"))
              res = new org.hl7.fhir.dstu3.formats.JsonParser().parse(new ByteArrayInputStream(t.getValue()));
            else
              throw new Exception("Unsupported format for "+fn);
            r = VersionConvertor_30_40.convertResource(res, false);
          } else if (version.equals("1.4.0")) {
            org.hl7.fhir.dstu2016may.model.Resource res;
            if (fn.endsWith(".xml") && !fn.endsWith("template.xml"))
              res = new org.hl7.fhir.dstu2016may.formats.XmlParser().parse(new ByteArrayInputStream(t.getValue()));
            else if (fn.endsWith(".json") && !fn.endsWith("template.json"))
              res = new org.hl7.fhir.dstu2016may.formats.JsonParser().parse(new ByteArrayInputStream(t.getValue()));
            else
              throw new Exception("Unsupported format for "+fn);
            r = VersionConvertor_14_40.convertResource(res);
          } else if (version.equals("1.0.2")) {
            org.hl7.fhir.dstu2.model.Resource res;
            if (fn.endsWith(".xml") && !fn.endsWith("template.xml"))
              res = new org.hl7.fhir.dstu2.formats.JsonParser().parse(new ByteArrayInputStream(t.getValue()));
            else if (fn.endsWith(".json") && !fn.endsWith("template.json"))
              res = new org.hl7.fhir.dstu2.formats.JsonParser().parse(new ByteArrayInputStream(t.getValue()));
            else
              throw new Exception("Unsupported format for "+fn);
            VersionConvertorAdvisor40 advisor = new org.hl7.fhir.convertors.IGR2ConvertorAdvisor();
            r = new VersionConvertor_10_40(advisor ).convertResource(res);
          } else if (version.equals(Constants.VERSION)) {
            if (fn.endsWith(".xml") && !fn.endsWith("template.xml"))
              r = new XmlParser().parse(new ByteArrayInputStream(t.getValue()));
            else if (fn.endsWith(".json") && !fn.endsWith("template.json"))
              r = new JsonParser().parse(new ByteArrayInputStream(t.getValue()));
            else if (fn.endsWith(".txt"))
              r = new StructureMapUtilities(context, null, null).parse(TextFile.bytesToString(t.getValue()), fn);
            else
              throw new Exception("Unsupported format for "+fn);
          } else
            throw new Exception("Unsupported version "+version);

        } catch (Exception e) {
          throw new Exception("Error parsing "+fn+": "+e.getMessage(), e);
        }
        if (r != null) {
          context.cacheResource(r);
          if (r instanceof ImplementationGuide) {
            canonical = ((ImplementationGuide) r).getUrl();
            if (canonical.contains("/ImplementationGuide/")) {
              Resource r2 = r.copy();
              ((ImplementationGuide) r2).setUrl(canonical.substring(0, canonical.indexOf("/ImplementationGuide/")));
              context.cacheResource(r2);
            }
          }
        }
      }
		}
    if (canonical != null)
      grabNatives(source, canonical);
	}

  private boolean exemptFile(String fn) {
    return Utilities.existsInList(fn, "spec.internals", "version.info", "schematron.zip");
  }

  private String readInfoVersion(byte[] bs) throws IOException {
    String is = TextFile.bytesToString(bs);
    is = is.trim();
    IniFile ini = new IniFile(new ByteArrayInputStream(TextFile.stringToBytes(is, false)));
    return ini.getStringProperty("FHIR", "version");
  }

  private void grabNatives(Map<String, byte[]> source, String prefix) {
    for (Entry<String, byte[]> e : source.entrySet()) {
      if (e.getKey().endsWith(".zip"))
        binaries.put(prefix+"#"+e.getKey(), e.getValue());
    }
	}

  public void setQuestionnaires(List<String> questionnaires) {
	}

  public void setNative(boolean doNative) {
    this.doNative = doNative;
  }

  private class Content {
    byte[] focus = null;
    FhirFormat cntType = null;
  }
  
  public Content loadContent(String source, String opName) throws Exception {
    Map<String, byte[]> s = loadIgSource(source);
    Content res = new Content();
    if (s.size() != 1)
      throw new Exception("Unable to find resource " + source + " to "+opName);
    for (Entry<String, byte[]> t: s.entrySet()) {
      res.focus = t.getValue();
      if (t.getKey().endsWith(".json"))
        res.cntType = FhirFormat.JSON; 
      else if (t.getKey().endsWith(".xml"))
        res.cntType = FhirFormat.XML; 
      else if (t.getKey().endsWith(".ttl"))
        res.cntType = FhirFormat.TURTLE; 
      else
        throw new Exception("Todo: Determining resource type is not yet done");
    }
    return res;
  }

  public OperationOutcome validate(String source, List<String> profiles) throws Exception {
    List<String> l = new ArrayList<String>();
    l.add(source);
    return (OperationOutcome)validate(l, profiles);
  }
    
  public Resource validate(List<String> sources, List<String> profiles) throws Exception {
    List<String> refs = new ArrayList<String>();
    boolean asBundle = handleSources(sources, refs);
    Bundle results = new Bundle();
    results.setType(Bundle.BundleType.COLLECTION);
    for (String ref : refs) {
      Content cnt = loadContent(ref, "validate");
      OperationOutcome outcome = validate(ref, cnt.focus, cnt.cntType, profiles);
      ToolingExtensions.addStringExtension(outcome, ToolingExtensions.EXT_OO_FILE, ref);
      results.addEntry().setResource(outcome);
    }
    if (asBundle)
      return results;
    else
      return results.getEntryFirstRep().getResource();
  }
  
  public OperationOutcome validateString(String location, String source, FhirFormat format, List<String> profiles) throws Exception {
    return validate(location, source.getBytes(), format, profiles);
  }

  // Public to allow reporting of results in alternate ways
  public boolean handleSources(List<String> sources, List<String> refs) throws IOException {
    boolean asBundle = sources.size() > 1;
    for (String source : sources) {
      if (handleSource(source, refs)) {
        asBundle = true;  // Code needs to be written this way to ensure handleSource gets called
      }
    }
    
    return asBundle;
  }
  
  private boolean handleSource(String name, List<String> refs) throws IOException {
    boolean isBundle = false;
    if (name.startsWith("https:") || name.startsWith("http:")) {
      refs.add(name);

    } else if (name.contains("*")) {
      isBundle = true;
      AsteriskFilter filter = new AsteriskFilter(name);
      File[] files = new File(filter.getDir()).listFiles(filter);
      for (int i=0; i < files.length; i++) {
        refs.add(files[i].getPath());
      }
    
    } else {
      File file = new File(name);

      if (!file.exists())
        throw new IOException("File " + name + " does not exist");
    
      if (file.isFile()) {
        refs.add(name);
        
      } else {
        isBundle = true;
        for (int i=0; i < file.listFiles().length; i++) {
          File[] fileList = file.listFiles();
          if (fileList[i].isFile())
            refs.add(fileList[i].getPath());
        }
      }
    }
    
    return isBundle;
  }

  public OperationOutcome validate(String location, byte[] source, FhirFormat cntType, List<String> profiles) throws Exception {
    List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
    if (doNative) {
      if (cntType == FhirFormat.JSON)
        validateJsonSchema(location, messages);
      if (cntType == FhirFormat.XML)
        validateXmlSchema(location, messages);
      if (cntType == FhirFormat.TURTLE)
        validateSHEX(location, messages);
    }
    InstanceValidator validator = getValidator();
    validator.validate(null, messages, new ByteArrayInputStream(source), cntType, new ValidationProfileSet(profiles, true));
    return messagesToOutcome(messages);
  }

  public OperationOutcome validate(String location, byte[] source, FhirFormat cntType, List<String> profiles, IdStatus resourceIdRule, boolean anyExtensionsAllowed, BestPracticeWarningLevel bpWarnings, CheckDisplayOption displayOption) throws Exception {
    List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
    if (doNative) {
      if (cntType == FhirFormat.JSON)
        validateJsonSchema(location, messages);
      if (cntType == FhirFormat.XML)
        validateXmlSchema(location, messages);
      if (cntType == FhirFormat.TURTLE)
        validateSHEX(location, messages);
    }
    InstanceValidator validator = getValidator();
    validator.setResourceIdRule(resourceIdRule);
    validator.setBestPracticeWarningLevel(bpWarnings);
    validator.setCheckDisplay(displayOption);   
    validator.validate(null, messages, new ByteArrayInputStream(source), cntType, new ValidationProfileSet(profiles, true));
    return messagesToOutcome(messages);
  }
  
  
  private void validateSHEX(String location, List<ValidationMessage> messages) {
    messages.add(new ValidationMessage(Source.InstanceValidator, IssueType.INFORMATIONAL, location, "SHEX Validation is not done yet", IssueSeverity.INFORMATION));
	}

  private void validateXmlSchema(String location, List<ValidationMessage> messages) throws FileNotFoundException, IOException, SAXException {
    XmlValidator xml = new XmlValidator(messages, loadSchemas(), loadTransforms());
    messages.add(new ValidationMessage(Source.InstanceValidator, IssueType.INFORMATIONAL, location, "XML Schema Validation is not done yet", IssueSeverity.INFORMATION));
	}

  private Map<String, byte[]> loadSchemas() throws IOException {
    Map<String, byte[]> res = new HashMap<String, byte[]>();
    for (Entry<String, byte[]> e : readZip(new ByteArrayInputStream(binaries.get("http://hl7.org/fhir#fhir-all-xsd.zip"))).entrySet()) {
      if (e.getKey().equals("fhir-single.xsd"))
        res.put(e.getKey(), e.getValue());
      if (e.getKey().equals("fhir-invariants.sch"))
        res.put(e.getKey(), e.getValue());
    }
    return res;
  }
  
  private Map<String, byte[]> loadTransforms() throws IOException {
    Map<String, byte[]> res = new HashMap<String, byte[]>();
    for (Entry<String, byte[]> e : readZip(new ByteArrayInputStream(binaries.get("http://hl7.org/fhir#fhir-all-xsd.zip"))).entrySet()) {
      if (e.getKey().endsWith(".xsl"))
        res.put(e.getKey(), e.getValue());
    }
    return res;
  }

  private void validateJsonSchema(String location, List<ValidationMessage> messages) {
    messages.add(new ValidationMessage(Source.InstanceValidator, IssueType.INFORMATIONAL, location, "JSON Schema Validation is not done yet", IssueSeverity.INFORMATION));   
	}

  private List<ValidationMessage> filterMessages(List<ValidationMessage> messages) {
    List<ValidationMessage> filteredValidation = new ArrayList<ValidationMessage>();
    for (ValidationMessage e : messages) {
      if (!filteredValidation.contains(e))
        filteredValidation.add(e);
    }
    filteredValidation.sort(null);
    return filteredValidation;
  }
  
  private OperationOutcome messagesToOutcome(List<ValidationMessage> messages) throws DefinitionException {
    OperationOutcome op = new OperationOutcome();
    for (ValidationMessage vm : filterMessages(messages)) {
      op.getIssue().add(OperationOutcomeUtilities.convertToIssue(vm, op));
    }
    new NarrativeGenerator("", "", context).generate(null, op);
    return op;
	}
  
  public static String issueSummary (OperationOutcomeIssueComponent issue) {
    String source = ToolingExtensions.readStringExtension(issue, ToolingExtensions.EXT_ISSUE_SOURCE);
    return issue.getSeverity().toString()+" @ "+issue.getLocation() + " " +issue.getDetails().getText() +(source != null ? " (src = "+source+")" : "");    
  }

  public Resource transform(String source, String map) throws Exception {
    Content cnt = loadContent(source, "validate");
    return transform(cnt.focus, cnt.cntType, map);
  }
  
  public Resource transform(byte[] source, FhirFormat cntType, String mapUri) throws Exception {
    List<Resource> outputs = new ArrayList<Resource>();
    
    StructureMapUtilities scu = new StructureMapUtilities(context, new TransformSupportServices(outputs));

    
    org.hl7.fhir.r4.elementmodel.Element src = Manager.parse(context, new ByteArrayInputStream(source), cntType); 
    StructureMap map = context.getTransform(mapUri);
    if (map == null)
      throw new Error("Unable to find map "+mapUri);
    
    scu.transform(null, src, map, null);
    if (outputs.size() == 0)
      throw new Exception("This transform did not produce an output");
    if (outputs.size() > 1)
      throw new Exception("This transform did produced multiple outputs which is not supported in this context");
    return outputs.get(0);
  }

  public DomainResource generate(String source) throws Exception {
    Content cnt = loadContent(source, "validate");
    Resource res;
    if (cnt.cntType == FhirFormat.XML)
      res = new XmlParser().parse(cnt.focus);
    else if (cnt.cntType == FhirFormat.JSON)
      res = new JsonParser().parse(cnt.focus);
    else if (cnt.cntType == FhirFormat.TURTLE)
      res = new RdfParser().parse(cnt.focus);
    else
      throw new Error("Not supported yet");
  
    new NarrativeGenerator("",  "", context).generate((DomainResource) res, null);
    return (DomainResource) res;
  }
  
  public StructureDefinition snapshot(String source) throws Exception {
    Content cnt = loadContent(source, "validate");
    Resource res;
    if (cnt.cntType == FhirFormat.XML)
      res = new XmlParser().parse(cnt.focus);
    else if (cnt.cntType == FhirFormat.JSON)
      res = new JsonParser().parse(cnt.focus);
    else if (cnt.cntType == FhirFormat.TURTLE)
      res = new RdfParser().parse(cnt.focus);
    else
      throw new Error("Not supported yet");
  
    if (!(res instanceof StructureDefinition))
      throw new Exception("Require a StructureDefinition for generating a snapshot");
    StructureDefinition sd = (StructureDefinition) res;
    StructureDefinition base = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
    
    new ProfileUtilities(context, null, null).generateSnapshot(base, sd, sd.getUrl(), sd.getName());
    return sd;
  }

  public void seeResource(Resource r) throws FHIRException {
    context.cacheResource(r);
  }

  public void dropResource(String type, String id) {
    context.dropResource(type, id);
    
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public InstanceValidator getValidator() {
    InstanceValidator validator = new InstanceValidator(context, null);
    validator.setHintAboutNonMustSupport(hintAboutNonMustSupport);
    validator.setAnyExtensionsAllowed(anyExtensionsAllowed);
    validator.setNoInvariantChecks(isNoInvariantChecks());
    return validator;
  }

  public void setMapLog(String mapLog) throws FileNotFoundException {
    this.mapLog = new PrintWriter(mapLog);
  }

  
}
