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
