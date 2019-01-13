package org.hl7.fhir.dstu3.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.StructureDefinition;

public class ValidationProfileSet {

  public static class ProfileRegistration {
    private String profile; 
    private boolean error;  
    
    public ProfileRegistration(String profile, boolean error) {
      super();
      this.profile = profile;
      this.error = error;
    }
    public String getProfile() {
      return profile;
    }
    public boolean isError() {
      return error;
    }
    
    
  }
  private List<ProfileRegistration> canonical = new ArrayList<ProfileRegistration>();
  private List<StructureDefinition> definitions = new ArrayList<StructureDefinition>();
  
  public ValidationProfileSet(String profile, boolean isError) {
    super();
    canonical.add(new ProfileRegistration(profile, isError));
  }

  public ValidationProfileSet() {
    super();
  }

  public ValidationProfileSet(StructureDefinition profile) {
    super();
    definitions.add(profile);
  }

  public ValidationProfileSet(List<String> profiles, boolean isError) {
    super();
    if (profiles != null)
      for (String p : profiles)
        canonical.add(new ProfileRegistration(p, isError));
  }

  public List<String> getCanonicalUrls() {
    List<String> res = new ArrayList<String>();
    for (ProfileRegistration c : canonical) {
      res.add(c.getProfile());
    }
    return res;
  }

  public List<StructureDefinition> getDefinitions() {
    return definitions;
  }

  public boolean empty() {
    return canonical.isEmpty() && definitions.isEmpty();
  }

  public List<String> getCanonicalAll() {
    Set<String> res = new HashSet<String>();
    res.addAll(getCanonicalUrls());
    for (StructureDefinition sd : definitions)
      res.add(sd.getUrl());
    return new ArrayList<String>(res);
  }

  public List<ProfileRegistration> getCanonical() {
    return canonical;
  }

}
