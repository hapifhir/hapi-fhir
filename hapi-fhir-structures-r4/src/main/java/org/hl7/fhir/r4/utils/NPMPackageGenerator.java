package org.hl7.fhir.r4.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations.FHIRVersion;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.ImplementationGuide.ImplementationGuideDependsOnComponent;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.cache.PackageGenerator.PackageType;
import org.hl7.fhir.utilities.cache.ToolsVersion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NPMPackageGenerator {

  public enum Category {
    RESOURCE, OPENAPI, SCHEMATRON, RDF, OTHER, TOOL;
    
    private String getDirectory() {
      switch (this) {
      case RESOURCE: return "/package/";
      case OPENAPI: return "/openapi/";
      case SCHEMATRON: return "/xml/";
      case RDF: return "/rdf/";      
      case OTHER: return "/other/";      
      case TOOL: return "/bin/";      
      }
      return "/";
    }
  }

 

  private String destFile;
  private Set<String> created = new HashSet<String>();
  private TarArchiveOutputStream tar;
  private ByteArrayOutputStream OutputStream;
  private BufferedOutputStream bufferedOutputStream;
  private GzipCompressorOutputStream gzipOutputStream;
  
  public NPMPackageGenerator(String destFile, String canonical, String url, PackageType kind, ImplementationGuide ig, String genDate) throws FHIRException, IOException {
    super();
    System.out.println("create package file at "+destFile);
    this.destFile = destFile;
    start();
    buildPackageJson(canonical, kind, url, genDate, ig);
  }
  
  public NPMPackageGenerator(String destFile, JsonObject npm) throws FHIRException, IOException {
    super();
    System.out.println("create package file at "+destFile);
    this.destFile = destFile;
    start();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(npm);
    try {
      addFile(Category.RESOURCE, "package.json", json.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
    }
  }
  
  
  private void buildPackageJson(String canonical, PackageType kind, String web, String genDate, ImplementationGuide ig) throws FHIRException, IOException {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (!ig.hasPackageId())
      b.append("packageId");
    if (!ig.hasVersion())
      b.append("version");
    if (!ig.hasFhirVersion())
      b.append("fhirVersion");
    if (!ig.hasLicense())
      b.append("license");
    for (ImplementationGuideDependsOnComponent d : ig.getDependsOn()) {
      if (!d.hasVersion()) {
        b.append("dependsOn.version("+d.getUri()+")");
      }
    }

    JsonObject npm = new JsonObject();
    npm.addProperty("name", ig.getPackageId());
    npm.addProperty("version", ig.getVersion());
    npm.addProperty("tools-version", ToolsVersion.TOOLS_VERSION);
    npm.addProperty("type", kind.getCode());
    if (ig.hasLicense())
      npm.addProperty("license", ig.getLicense().toCode());
    npm.addProperty("canonical", canonical);
    npm.addProperty("url", web);
    if (ig.hasTitle())
      npm.addProperty("title", ig.getTitle());
    if (ig.hasDescription())
      npm.addProperty("description", ig.getDescription()+ " (built "+genDate+timezone()+")");
    if (kind != PackageType.CORE) {
      JsonObject dep = new JsonObject();
      npm.add("dependencies", dep);
      for (Enumeration<FHIRVersion> v : ig.getFhirVersion()) { // TODO: fix for multiple versions
        dep.addProperty("hl7.fhir.core", v.asStringValue());
      }
      for (ImplementationGuideDependsOnComponent d : ig.getDependsOn()) {
        dep.addProperty(d.getPackageId(), d.getVersion());
      }
    }
    if (ig.hasPublisher())
      npm.addProperty("author", ig.getPublisher());
    JsonArray m = new JsonArray();
    for (ContactDetail t : ig.getContact()) {
      String email = email(t.getTelecom());
      String url = url(t.getTelecom());
      if (t.hasName() & (email != null || url != null)) {
        JsonObject md = new JsonObject();
        m.add(md);
        md.addProperty("name", t.getName());
        if (email != null)
          md.addProperty("email", email);
        if (url != null)
          md.addProperty("url", url);
      }
    }
    if (m.size() > 0)
      npm.add("maintainers", m);
    if (ig.getManifest().hasRendering())
      npm.addProperty("homepage", ig.getManifest().getRendering());
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(npm);
    try {
      addFile(Category.RESOURCE, "package.json", json.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
    }
  }


  private String timezone() {
    TimeZone tz = TimeZone.getDefault();  
    Calendar cal = GregorianCalendar.getInstance(tz);
    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

    String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

    return offset;
  }


  private String url(List<ContactPoint> telecom) {
    for (ContactPoint cp : telecom) {
      if (cp.getSystem() == ContactPointSystem.URL)
        return cp.getValue();
    }
    return null;
  }


  private String email(List<ContactPoint> telecom) {
    for (ContactPoint cp : telecom) {
      if (cp.getSystem() == ContactPointSystem.EMAIL)
        return cp.getValue();
    }
    return null;
  }

  private void start() throws IOException {
    OutputStream = new ByteArrayOutputStream();
    bufferedOutputStream = new BufferedOutputStream(OutputStream);
    gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
    tar = new TarArchiveOutputStream(gzipOutputStream);
  }


  public void addFile(Category cat, String name, byte[] content) throws IOException {
    String path = cat.getDirectory()+name;
    if (created.contains(path)) 
      System.out.println("Duplicate package file "+path);
    else {
      created.add(path);
      TarArchiveEntry entry = new TarArchiveEntry(path);
      entry.setSize(content.length);
      tar.putArchiveEntry(entry);
      tar.write(content);
      tar.closeArchiveEntry();
    }
  }
  
  public void finish() throws IOException {
    tar.finish();
    tar.close();
    gzipOutputStream.close();
    bufferedOutputStream.close();
    OutputStream.close();
    TextFile.bytesToFile(OutputStream.toByteArray(), destFile);
  }

  public String filename() {
    return destFile;
  }
}
