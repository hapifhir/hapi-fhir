package org.hl7.fhir.utilities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JSONUtil {

  public static JsonObject parse(String json) {
    return (JsonObject) new com.google.gson.JsonParser().parse(json);    
    
  }

  public static JsonObject forceObject(JsonObject obj, String name) {
    if (obj.has(name) && obj.get(name).isJsonObject())
      return obj.getAsJsonObject(name);
    if (obj.has(name))
      obj.remove(name);
    JsonObject res = new JsonObject();
    obj.add(name, res);
    return res;
  }

  public static JsonArray forceArray(JsonObject obj, String name) {
    if (obj.has(name) && obj.get(name).isJsonArray())
      return obj.getAsJsonArray(name);
    if (obj.has(name))
      obj.remove(name);
    JsonArray res = new JsonArray();
    obj.add(name, res);
    return res;  }

  public static JsonObject addObj(JsonArray arr) {
    JsonObject res = new JsonObject();
    arr.add(res);
    return res;
  }

}
