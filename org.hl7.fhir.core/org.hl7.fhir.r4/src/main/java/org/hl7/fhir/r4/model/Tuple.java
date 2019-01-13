package org.hl7.fhir.r4.model;

/*-
 * #%L
 * org.hl7.fhir.r4
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.exceptions.FHIRException;

public class Tuple extends Base {
  private Map<String, List<Base>> properties = new HashMap<>();

  @Override
  public String fhirType() {
    return "Tuple";
  }

  @Override
  protected void listChildren(List<Property> result) {
    for (String s : properties.keySet()) {
      result.add(new Property(s, "Base", null, 0, 1, properties.get(s)));
    }    
  }

  @Override
  public String getIdBase() {
    return null;
  }

  @Override
  public void setIdBase(String value) {
  }

  public void addProperty(String s, List<Base> list) {
    properties.put(s, list); 
  }
  
  public Base[] listChildrenByName(String name, boolean checkValid) throws FHIRException {
    if (name.equals("*")) {
      List<Property> children = new ArrayList<Property>();
      listChildren(children);
      List<Base> result = new ArrayList<Base>();
      for (Property c : children)
        result.addAll(c.getValues());
      return result.toArray(new Base[result.size()]);
    } else if (properties.containsKey(name)) {
      return properties.get(name).toArray(new Base[0]);
    }
      return getProperty(name.hashCode(), name, checkValid);
  }


}
