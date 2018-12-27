package org.hl7.fhir.utilities.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.IniFile;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Package cache manager
 * 
 * API:
 * 
 * constructor
 *  getPackageUrl
 *  getPackageId
 *  findPackageCache 
 *  addPackageToCache  
 * 
 * @author Grahame Grieve
 *
 */
public class PackageCacheManager {

  public class PackageEntry {

    private byte[] bytes;
    private String name;

    public PackageEntry(String name) {
      this.name = name;
    }

    public PackageEntry(String name, byte[] bytes) {
      this.name = name;
      this.bytes = bytes;
    }
  }

  public static final String PACKAGE_REGEX = "^[a-z][a-z0-9\\_\\-]*(\\.[a-z0-9\\_\\-]+)+$";
  public static final String PACKAGE_VERSION_REGEX = "^[a-z][a-z0-9\\_\\-]*(\\.[a-z0-9\\_\\-]+)+\\#[a-z0-9\\-\\_]+(\\.[a-z0-9\\-\\_]+)*$";

  private static final int BUFFER_SIZE = 1024;
  private static final String CACHE_VERSION = "2"; // second version - see wiki page
  private static final int ANALYSIS_VERSION = 2;

  private String cacheFolder;
  private boolean buildLoaded;
  private JsonArray buildInfo;
  private boolean progress = true;
  private List<NpmPackage> temporaryPackages = new ArrayList<NpmPackage>();
  private Map<String, String> ciList = new HashMap<String, String>();
  
  public PackageCacheManager(boolean userMode, int toolsVersion) throws IOException {
    if (userMode)
      cacheFolder = Utilities.path(System.getProperty("user.home"), ".fhir", "packages");
    else
      cacheFolder = Utilities.path("var", "lib", ".fhir", "packages");
    if (!(new File(cacheFolder).exists()))
      Utilities.createDirectory(cacheFolder);
    if (!(new File(Utilities.path(cacheFolder, "packages.ini")).exists()))
      TextFile.stringToFile("[urls]\r\n\r\n[local]\r\n\r\n", Utilities.path(cacheFolder, "packages.ini"));  
    IniFile ini = new IniFile(Utilities.path(cacheFolder, "packages.ini"));
    boolean save = false;
    if ("1".equals(ini.getStringProperty("cache", "version"))) {
      convertPackageCacheFrom1To2();
      ini.setStringProperty("cache", "version", "2", null);
      save = true;
    }
    if (!CACHE_VERSION.equals(ini.getStringProperty("cache", "version"))) {
      clearCache();
      ini.setStringProperty("cache", "version", CACHE_VERSION, null);
      save = true;
    }
    save = checkIniHasMapping("hl7.fhir.core", "http://hl7.org/fhir", ini) || save;
    save = checkIniHasMapping("fhir.argonaut.ehr", "http://fhir.org/guides/argonaut", ini) || save;
    save = checkIniHasMapping("fhir.argonaut.pd", "http://fhir.org/guides/argonaut-pd", ini) || save;
    save = checkIniHasMapping("fhir.argonaut.scheduling", "http://fhir.org/guides/argonaut-scheduling", ini) || save;
    save = checkIniHasMapping("fhir.hspc.acog", "http://hl7.org/fhir/fpar", ini) || save;
    save = checkIniHasMapping("hl7.fhir.au.argonaut", "http://hl7.org.au/fhir/argonaut", ini) || save;
    save = checkIniHasMapping("hl7.fhir.au.base", "http://hl7.org.au/fhir", ini) || save;
    save = checkIniHasMapping("hl7.fhir.au.pd", "http://hl7.org.au/fhir", ini) || save;
    save = checkIniHasMapping("hl7.fhir.smart", "http://hl7.org/fhir/smart-app-launch", ini) || save;
    save = checkIniHasMapping("hl7.fhir.snomed", "http://hl7.org/fhir/ig/snomed", ini) || save;
    save = checkIniHasMapping("hl7.fhir.test.v10", "http://hl7.org/fhir/test-ig-10", ini) || save;
    save = checkIniHasMapping("hl7.fhir.test.v30", "http://hl7.org/fhir/test", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.breastcancer", "http://hl7.org/fhir/us/breastcancer", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.ccda", "http://hl7.org/fhir/us/ccda", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.cds.opioids", "http://hl7.org/fhir/ig/opioid-cds", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.core", "http://hl7.org/fhir/us/core", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.dafresearch", "http://hl7.org/fhir/us/daf-research", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.ecr", "http://fhir.hl7.org/us/ecr", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.hai", "http://hl7.org/fhir/us/hai", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.hedis", "http://hl7.org/fhir/us/hedis", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.meds", "http://hl7.org/fhir/us/meds", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.qicore", "http://hl7.org/fhir/us/qicore", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.sdc", "http://hl7.org/fhir/us/sdc", ini) || save;
    save = checkIniHasMapping("hl7.fhir.us.sdcde", "http://hl7.org/fhir/us/sdcde", ini) || save;
    save = checkIniHasMapping("hl7.fhir.uv.genomicsreporting", "http://hl7.org/fhir/uv/genomics-reporting", ini) || save;
    save = checkIniHasMapping("hl7.fhir.uv.immds", "http://hl7.org/fhir/uv/cdsi", ini) || save;
    save = checkIniHasMapping("hl7.fhir.uv.ips", "http://hl7.org/fhir/uv/ips", ini) || save;
    save = checkIniHasMapping("hl7.fhir.uv.phd", "http://hl7.org/fhir/devices", ini) || save;
    save = checkIniHasMapping("hl7.fhir.uv.vhdir", "http://hl7.org/fhir/ig/vhdir", ini) || save;
    save = checkIniHasMapping("hl7.fhir.vn.base", "http://hl7.org/fhir/ig/vietnam", ini) || save;
    save = checkIniHasMapping("hl7.fhir.vocabpoc", "http://hl7.org/fhir/ig/vocab-poc", ini) || save;
    if (save)
      ini.save();    
    checkDeleteVersion("hl7.fhir.core", "1.0.2", 2);
    checkDeleteVersion("hl7.fhir.core", "1.4.0", 2);
    checkDeleteVersion("hl7.fhir.core", "current", toolsVersion);
    checkDeleteVersion("hl7.fhir.core", "4.0.0", toolsVersion);
  }
  

  private void checkDeleteVersion(String id, String ver, int minVer) {
    if (hasPackage(id, ver)) {
      boolean del = true;
      NpmPackage pck;
      try {
        pck = resolvePackage(id, ver, "xx");
        if (pck.getNpm().has("tools-version")) {
          del = pck.getNpm().get("tools-version").getAsInt() < minVer;
        }
      } catch (Exception e) {
      }
      if (del)
        try {
          removePackage(id, ver);
        } catch (IOException e) {
        }
    }
  }


  public void removePackage(String id, String ver) throws IOException  {
    String f = Utilities.path(cacheFolder, id+"#"+ver);
    Utilities.clearDirectory(f);    
    new File(f).delete();
  }


  private void convertPackageCacheFrom1To2() throws IOException {
    for (File f : new File(cacheFolder).listFiles()) {
      if (f.isDirectory() && f.getName().contains("-")) {
        String s = f.getName();
        int i = s.lastIndexOf("-");
        s = s.substring(0, i)+"#"+s.substring(i+1);
        File nf = new File(Utilities.path(cacheFolder, s));
        if (!f.renameTo(nf))
          throw new IOException("Unable to rename "+f.getAbsolutePath()+" to "+nf.getAbsolutePath());
      }
    }
  }


  private boolean checkIniHasMapping(String pid, String curl, IniFile ini) {
    if (curl.equals(ini.getStringProperty("urls", pid)))
      return false;
    ini.setStringProperty("urls", pid, curl, null);
    return true;
  }


  private void clearCache() throws IOException {
    for (File f : new File(cacheFolder).listFiles()) {
      if (f.isDirectory())
        FileUtils.deleteDirectory(f);
      else if (!f.getName().equals("packages.ini"))
        FileUtils.forceDelete(f);
    }    
  }


  public void recordMap(String url, String id) throws IOException {
    IniFile ini = new IniFile(Utilities.path(cacheFolder, "packages.ini"));
    ini.setStringProperty("urls", id, url, null);
    ini.save();
  }

  public String getPackageUrl(String id) throws IOException {
    IniFile ini = new IniFile(Utilities.path(cacheFolder, "packages.ini"));
    return ini.getStringProperty("urls", id); 
  }
  
  public String getPackageId(String url) throws IOException {
    IniFile ini = new IniFile(Utilities.path(cacheFolder, "packages.ini"));
    String[] ids = ini.getPropertyNames("urls");
    if (ids != null) {
      for (String id : ids) {
        if (url.equals(ini.getStringProperty("urls", id)))
          return id;
      }
    }
    return null;
  }
  
  private List<String> sorted(String[] keys) {
    List<String> names = new ArrayList<String>();
    for (String s : keys)
      names.add(s);
    Collections.sort(names);
    return names;
  }

  public NpmPackage loadPackageCacheLatest(String id) throws IOException {
    String match = null;
    List<String> l = sorted(new File(cacheFolder).list());
    for (int i = l.size()-1; i >= 0; i--) {
      String f = l.get(i);
      if (f.startsWith(id+"#")) {
        return loadPackageInfo(Utilities.path(cacheFolder, f)); 
      }
    }
    return null;    
  }
  
  public NpmPackage loadPackageCache(String id, String version) throws IOException {
    for (NpmPackage p : temporaryPackages) {
      if (p.name().equals(id) && ("current".equals(version) || "dev".equals(version) || p.version().equals(version)))
        return p;
    }
    String match = null;
    for (String f : sorted(new File(cacheFolder).list())) {
      if (f.equals(id+"#"+version)) {
        return loadPackageInfo(Utilities.path(cacheFolder, f)); 
      }
    }
    if ("dev".equals(version))
      return loadPackageCache(id, "current");
    else
      return null;
  }
  
  private NpmPackage loadPackageInfo(String path) throws IOException {
    NpmPackage pi = new NpmPackage(path);
    return pi;
  }

  public NpmPackage addPackageToCache(String id, String version, InputStream tgz) throws IOException {
    if (progress ) {
      System.out.println("Installing "+id+"#"+(version == null ? "?" : version)+" to the package cache");
      System.out.print("  Fetching:");
    }
    List<PackageEntry> files = new ArrayList<PackageEntry>();
    
    long size = unPackage(tgz, files);  

    byte[] npmb = null;
    for (PackageEntry e : files) {
      if (e.name.equals("package/package.json"))
        npmb = e.bytes;
    }
    if (npmb == null)
      throw new IOException("Unable to find package/package.json in the package file");
    
    if (progress )
      System.out.print("|");
    JsonObject npm = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.bytesToString(npmb));
    if (npm.get("name") == null || id == null || !id.equals(npm.get("name").getAsString()))
      throw new IOException("Attempt to import a mis-identified package "+id);
    if (version == null)
      version = npm.get("version").getAsString();
    
    String packRoot = Utilities.path(cacheFolder, id+"#"+version);
    Utilities.createDirectory(packRoot);
    Utilities.clearDirectory(packRoot);
      
    for (PackageEntry e : files) {
      if (e.bytes == null) {
        File f = new File(Utilities.path(packRoot, e.name));
        if (!f.mkdir()) 
          throw new IOException("Unable to create directory '%s', during extraction of archive contents: "+ f.getAbsolutePath());
      } else {
        String fn = Utilities.path(packRoot, e.name);
        String dir = Utilities.getDirectoryForFile(fn);
        if (!(new File(dir).exists()))
          Utilities.createDirectory(dir);
        TextFile.bytesToFile(e.bytes, fn);
      }
    }
    if (progress) {
      System.out.println("");
      System.out.print("  Analysing");
    }      
    
    Map<String, String> profiles = new HashMap<String, String>(); 
    Map<String, String> canonicals = new HashMap<String, String>(); 
    if ("hl7.fhir.core".equals(npm.get("name").getAsString()))
      analysePackage(packRoot, npm.get("version").getAsString(), profiles, canonicals);
    else
      analysePackage(packRoot, npm.getAsJsonObject("dependencies").get("hl7.fhir.core").getAsString(), profiles, canonicals);
    IniFile ini = new IniFile(Utilities.path(packRoot, "cache.ini"));
    ini.setTimeStampFormat("dd/MM/yyyy h:mm:ss a");
    ini.setLongProperty("Package", "size", size, null);
    ini.setTimestampProperty("Package", "install", Timestamp.from(Instant.now()), null);
    for (String p : profiles.keySet()) 
      ini.setStringProperty("Profiles", p, profiles.get(p), null);
    for (String p : canonicals.keySet()) 
      ini.setStringProperty("Canonicals", p, canonicals.get(p), null);
    ini.setIntegerProperty("Packages", "analysis", ANALYSIS_VERSION, null);
    ini.save();
    if (progress )
      System.out.println(" done.");
    return loadPackageInfo(packRoot);
  }


  public long unPackage(InputStream tgz, List<PackageEntry> files) throws IOException {
    long size = 0;
    GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(tgz);
    try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
      TarArchiveEntry entry;

      int i = 0;
      int c = 12;
      while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
        i++;
        if (progress && i % 20 == 0) {
          c++;
          System.out.print(".");
          if (c == 120) {
            System.out.println("");
            System.out.print("  ");
            c = 2;
          }
        }
        if (entry.isDirectory()) {
          files.add(new PackageEntry(entry.getName()));
        } else {
          int count;
          byte data[] = new byte[BUFFER_SIZE];
          
          ByteArrayOutputStream fos = new ByteArrayOutputStream();
          try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
            while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
              dest.write(data, 0, count);
            }
          }
          fos.close();
          files.add(new PackageEntry(entry.getName(), fos.toByteArray()));
          size = size + fos.size();
        }
      }
    }
    return size;
  }

  private void analysePackage(String dir, String v, Map<String, String> profiles, Map<String, String> canonicals) throws IOException {
    int i = 0;
    int c = 11;
    for (File f : new File(Utilities.path(dir, "package")).listFiles()) {
      i++;
      if (progress && i % 20 == 0) {
        c++;
        System.out.print(".");
        if (c == 120) {
          System.out.println("");
          System.out.print("  ");
          c = 2;
        }
      }    
      if (!f.getName().startsWith("Bundle-")) {
        try {
          JsonObject j = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.fileToString(f));
          if (!Utilities.noString(j.get("url").getAsString()) && !Utilities.noString(j.get("resourceType").getAsString())) 
            canonicals.put(j.get("url").getAsString(), f.getName());
          if ("StructureDefinition".equals(j.get("resourceType").getAsString()) && "resource".equals(j.get("kind").getAsString())) {
            String bd = null;
            if ("1.0.2".equals(v)) 
              bd = j.get("constrainedType").getAsString();
            else
              bd = j.get("type").getAsString();
            if (Utilities.noString(bd))
              bd = j.get("name").getAsString();
            if (!"Extension".equals(bd))
              profiles.put(j.get("url").getAsString(), bd);
          }
        } catch (Exception e) {
          // nothing
        }
      }
    }
  }

  public NpmPackage extractLocally(String filename) throws IOException {
    return extractLocally(new FileInputStream(filename), filename);
  }

  public NpmPackage extractLocally(InputStream tgz, String name) throws IOException {
    if (progress ) {
      System.out.println("Loading "+name+" to the package cache");
      System.out.print("  Fetching:");
    }
    List<PackageEntry> files = new ArrayList<PackageEntry>();
    
    unPackage(tgz, files);
    
    byte[] npmb = null;
    for (PackageEntry e : files) {
      if (e.name.equals("package/package.json"))
        npmb = e.bytes;
    }
    if (npmb == null)
      throw new IOException("Unable to find package/package.json in the package file");
    
    JsonObject npm = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.bytesToString(npmb));

    Map<String, byte[]> content = new HashMap<String, byte[]>();
    List<String> folders = new ArrayList<String>();
    
    for (PackageEntry e : files) {
      if (e.bytes == null) {
        folders.add(e.name);
      } else {
        content.put(e.name, e.bytes);
      }
    }
    System.out.println(" done.");
    NpmPackage p = new NpmPackage(npm, content, folders);
    recordMap(p.canonical(), p.name());
    return p;
  }

  public String getFolder() {
    return cacheFolder;
  }


  public JsonArray loadFromBuildServer() throws IOException {
    buildLoaded = true; // whether it succeeds or not
    URL url = new URL("https://build.fhir.org/ig/qas.json?nocache=" + System.currentTimeMillis());
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    InputStream json = connection.getInputStream();
    buildInfo = (JsonArray) new com.google.gson.JsonParser().parse(TextFile.streamToString(json));
  
    for (JsonElement n : buildInfo) {
      JsonObject o = (JsonObject) n;
      if (o.has("url") && o.has("package-id") && o.get("package-id").getAsString().contains(".")) {
        String u = o.get("url").getAsString();
        if (u.contains("/ImplementationGuide/"))
          u = u.substring(0, u.indexOf("/ImplementationGuide/"));
        recordMap(u, o.get("package-id").getAsString());
        ciList.put(o.get("package-id").getAsString(), "https://build.fhir.org/ig/"+o.get("repo").getAsString());
      }
    }
    return buildInfo;
  }


  public boolean isBuildLoaded() {
    return buildLoaded;
  }


  public String buildPath(String url) {
    for (JsonElement e : buildInfo) {
      JsonObject j = (JsonObject) e;
      if (j.has("url") && (url.equals(j.get("url").getAsString()) || j.get("url").getAsString().startsWith(url+"/ImplementationGuide"))) {
        return "https://build.fhir.org/ig/"+j.get("repo").getAsString();
      }
    }
    return null;
  }
 
  public boolean checkBuildLoaded() throws IOException {
    if (isBuildLoaded())
      return true;
    loadFromBuildServer();
    return false;
  }
  
  public NpmPackage resolvePackage(String id, String v, String currentVersion) throws FHIRException, IOException {
    NpmPackage p = loadPackageCache(id, v);
    if (p != null)
      return p;

    if ("dev".equals(v)) {
      p = loadPackageCache(id, "current");
      if (p != null)
        return p;
      v = "current";
    }

    String url = getPackageUrl(id);
    if (url == null)
      throw new FHIRException("Unable to resolve the package '"+id+"'");
    if (v == null) {
      InputStream stream = fetchFromUrlSpecific(Utilities.pathURL(url, "package.tgz"), true);
      if (stream == null && isBuildLoaded()) { 
        stream = fetchFromUrlSpecific(Utilities.pathURL(buildPath(url), "package.tgz"), true);
      }
      if (stream != null)
        return addPackageToCache(id, null,  stream);
      throw new FHIRException("Unable to find the package source for '"+id+"' at "+url);
    } else if ("current".equals(v) && ciList.containsKey(id)){
      InputStream stream = fetchFromUrlSpecific(Utilities.pathURL(ciList.get(id), "package.tgz"), true);
      return addPackageToCache(id, v, stream);
    } else {
      String pu = Utilities.pathURL(url, "package-list.json");
      JsonObject json;
      try {
        json = fetchJson(pu);
      } catch (Exception e) {
        throw new FHIRException("Error fetching package list for "+id+" from "+pu+": "+e.getMessage(), e);
      }
      if (!id.equals(json.get("package-id").getAsString()))
        throw new FHIRException("Package ids do not match in "+pu+": "+id+" vs "+json.get("package-id").getAsString());
      for (JsonElement e : json.getAsJsonArray("list")) {
        JsonObject vo = (JsonObject) e;
        if (v.equals(vo.get("version").getAsString())) {
          InputStream stream = fetchFromUrlSpecific(Utilities.pathURL(vo.get("path").getAsString(), "package.tgz"), true);
          if (stream == null)
            throw new FHIRException("Unable to find the package source for '"+id+"#"+v+"' at "+Utilities.pathURL(vo.get("path").getAsString(), "package.tgz"));
          return addPackageToCache(id, v, stream);
        }
      }
      // special case: current version
      if (id.equals("hl7.fhir.core") && v.equals(currentVersion)) {
        InputStream stream = fetchFromUrlSpecific(Utilities.pathURL("http://build.fhir.org", "package.tgz"), true);
        if (stream == null)
          throw new FHIRException("Unable to find the package source for '"+id+"#"+v+"' at "+Utilities.pathURL("http://build.fhir.org", "package.tgz"));
        return addPackageToCache(id, v, stream);
      }
      throw new FHIRException("Unable to resolve version "+v+" for package "+id);
    }
  }


//  !!!
//  if (packageId != null) {
//    return loadPackage();      
//  } else 
//    return loadPackage(pcm.extractLocally(stream));


  private JsonObject fetchJson(String source) throws IOException {
    URL url = new URL(source);
    URLConnection c = url.openConnection();
    return (JsonObject) new com.google.gson.JsonParser().parse(TextFile.streamToString(c.getInputStream()));
  }
  
  private InputStream fetchFromUrlSpecific(String source, boolean optional) throws FHIRException {
    try {
      URL url = new URL(source);
      URLConnection c = url.openConnection();
      return c.getInputStream();
    } catch (Exception e) {
      if (optional)
        return null;
      else
        throw new FHIRException(e.getMessage(), e);
    }
  }


  public void loadFromFolder(String packagesFolder) throws IOException {
    for (File f : new File(packagesFolder).listFiles()) {
      if (f.getName().endsWith(".tgz")) {
        temporaryPackages.add(extractLocally(new FileInputStream(f), f.getName()));
      }
    }
  }

  public Map<String, String> getCiList() {
    return ciList;
  }


  public boolean hasPackage(String id, String version) {
    for (NpmPackage p : temporaryPackages) {
      if (p.name().equals(id) && ("current".equals(version) || "dev".equals(version) || p.version().equals(version)))
        return true;
    }
    for (String f : sorted(new File(cacheFolder).list())) {
      if (f.equals(id+"#"+version)) {
        return true; 
      }
    }
    if ("dev".equals(version))
      return hasPackage(id, "current");
    else
      return false;
  }

}
