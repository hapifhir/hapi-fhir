package org.hl7.fhir.utilities.graphql;

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


import java.util.ArrayList;
import java.util.List;

public class Document {
  private List<Fragment> fragments = new ArrayList<Fragment>();
  private List<Operation> operations = new ArrayList<Operation>();
  
  public List<Fragment> getFragments() {
    return fragments;
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public Fragment fragment(String name) {
    for (Fragment f : fragments)
      if (f.name.equals(name))
        return f;
    return null;
  }

  public Operation operation(String name) {
    for (Operation  o : operations)
      if (o.getName().equals(name))
        return o;
    return null;
  }
}
