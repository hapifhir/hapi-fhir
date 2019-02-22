package org.hl7.fhir.utilities.liquid;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquidIncludeMap implements IBase {
  private final Map<String, List<IBase>> properties = new HashMap<>();

  @Override
  public boolean isEmpty() {
    return properties.isEmpty();
  }

  @Override
  public boolean hasFormatComment() {
    return false;
  }

  @Override
  public List<String> getFormatCommentsPre() {
    return Collections.emptyList();
  }

  @Override
  public List<String> getFormatCommentsPost() {
    return Collections.emptyList();
  }

  public void addProperty(String s, List<IBase> list) {
    properties.put(s, list);
  }
}
