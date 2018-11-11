package org.hl7.fhir.utilities;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonMerger {


  public void merge(JsonObject dest, JsonObject source) {
    for (Entry<String, JsonElement> e : source.entrySet()) {
      if (dest.has(e.getKey())) {
        if (e.getValue() instanceof JsonObject && dest.get(e.getKey()) instanceof JsonObject) 
          merge((JsonObject) dest.get(e.getKey()), (JsonObject) e.getValue());
        else if (e.getValue() instanceof JsonPrimitive && dest.get(e.getKey()) instanceof JsonPrimitive) {
          dest.remove(e.getKey());
          dest.add(e.getKey(), e.getValue());
        } else
          throw new Error("Not supported yet?");
      } else 
        dest.add(e.getKey(), e.getValue());
    }
    
  }

}
