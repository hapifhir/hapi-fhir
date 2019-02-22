package org.hl7.fhir.dstu3.hapi.fluentpath;


import ca.uhn.fhir.fluentpath.INarrativeConstantMap;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.Property;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NarrativeConstantMapDstu3 extends Base implements INarrativeConstantMap {
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

  @Override
  public void addConstant(String name, List<IBase> values) {
    this.addProperty(name, (List<Base>)(List<?>)values);
  }
}

