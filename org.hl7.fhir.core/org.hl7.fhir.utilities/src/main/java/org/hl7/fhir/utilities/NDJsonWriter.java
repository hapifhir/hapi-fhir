package org.hl7.fhir.utilities;

/*-
 * #%L
 * org.hl7.fhir.utilities
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class NDJsonWriter {

  private class ResourceInfo {
    private FileOutputStream stream;
    private Set<String> ids = new HashSet<String>();
    public OutputStreamWriter writer;
  }
  
  private static com.google.gson.JsonParser  parser = new com.google.gson.JsonParser();
  private Gson gson = new GsonBuilder().create();
  private Map<String, ResourceInfo> outputs = new HashMap<String, ResourceInfo>();
  private String filename;
  private String scratch;
  
  public NDJsonWriter(String filename, String scratch) {
    this.filename = filename;
    this.scratch = scratch;
    outputs.clear();
  }

  public void addFilesFiltered(String actualDir, String ext, String[] noExt) throws IOException {
    File f = new CSFile(actualDir);

    String files[] = f.list();
    for (int i = 0; i < files.length; i++) {
      if ( new CSFile(actualDir + files[i]).isFile() && ((ext == null || files[i].endsWith(ext)))) {
        boolean ok = true;
        for (String n : noExt) {
          ok = ok && !files[i].endsWith(n);
        }
        if (ok) {
          addFile(Utilities.path(actualDir, files[i]));
        }
      }
    }
  }

  private void addFile(String path) throws JsonSyntaxException, FileNotFoundException, IOException {
    JsonObject js = parser.parse(TextFile.fileToString(path)).getAsJsonObject();
    if (js.has("resourceType")) {
      addResource(js);  
    }
  }

  private void addResource(JsonObject js) throws IOException {
    String rn = js.get("resourceType").getAsString();    
    if (rn.equals("Bundle")) {
      if (js.has("entry")) {
        for (JsonElement item : js.getAsJsonArray("entry")) {
          if (item instanceof JsonObject && ((JsonObject) item).has("resource")) {
            JsonObject r = (JsonObject) ((JsonObject) item).get("resource");
            rn = r.get("resourceType").getAsString();
            addResource(r);  
          }
        }
      }
    } else {
      if (!js.has("id"))
        return;

      String id = js.get("id").getAsString();
      String json = gson.toJson(js);
      
      if (outputs.containsKey(rn)) { 
        ResourceInfo ri = outputs.get(rn);
        if (!ri.ids.contains(id)) {
          ri.ids.add(id);
          ri.writer.append("\r\n");      
          ri.writer.append(json);
        }
      } else {
        ResourceInfo ri = new ResourceInfo();
        outputs.put(rn, ri);
        ri.ids.add(id);
        ri.stream = new FileOutputStream(Utilities.path(scratch, rn+".ndjson"));
        ri.writer = new OutputStreamWriter(ri.stream, "UTF-8");
        ri.writer.append(json);      
      }
    }
  }

  public void close() throws IOException {
    ZipGenerator zip = new ZipGenerator(filename);
    for (String rn : sorted(outputs.keySet())) {
      ResourceInfo ri = outputs.get(rn);
      ri.writer.flush();
      ri.writer.close();
      ri.stream.close();
      
      zip.addStream(rn+".ndjson", new FileInputStream(Utilities.path(scratch, rn+".ndjson")), false);
    }
    zip.close();
  }

  private List<String> sorted(Set<String> keys) {
    List<String> res = new ArrayList<String>();
    res.addAll(keys);
    Collections.sort(res);
    return res;
  }

  public static void main(String[] args) throws IOException {
    String dstDir = "C:\\work\\org.hl7.fhir\\build\\publish\\";
    NDJsonWriter ndjson = new NDJsonWriter(dstDir + "examples-ndjson.zip", "c:\\temp\\ndjson");
    ndjson.addFilesFiltered(dstDir, ".json", new String[] {".schema.json", ".canonical.json", ".diff.json", "expansions.json", "package.json"});
    ndjson.close();
  }

}
