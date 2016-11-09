package org.hl7.fhir.dstu3.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.StructureDefinition;

public class ValidationProfileSet {

  private List<String> canonical = new ArrayList<String>();
  private List<StructureDefinition> definitions = new ArrayList<StructureDefinition>();
  
  public ValidationProfileSet(String profile) {
    super();
    canonical.add(profile);
  }

  public ValidationProfileSet() {
    super();
  }

  public ValidationProfileSet(StructureDefinition profile) {
    super();
    definitions.add(profile);
  }

  public ValidationProfileSet(List<String> profiles) {
    super();
    if (profiles != null)
    canonical.addAll(profiles);
  }

  public List<String> getCanonical() {
    return canonical;
  }

  public List<StructureDefinition> getDefinitions() {
    return definitions;
  }

  public boolean empty() {
    return canonical.isEmpty() && definitions.isEmpty();
  }

  public List<String> getCanonicalAll() {
    Set<String> res = new HashSet<String>();
    res.addAll(canonical);
    for (StructureDefinition sd : definitions)
      res.add(sd.getUrl());
    return new ArrayList<String>(res);
  }

}
