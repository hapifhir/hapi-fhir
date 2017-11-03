package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.StructureDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared context for a set of transformation executions.
 *
 * @author Claude Nanjo
 */
public class BatchContext {
  private Map<String, TransformContext> transformationContextMap;
  private Map<String, StructureDefinition> generatedStructureDefinitionMap;
  private String baseGeneratedProfileUrl;

  public BatchContext() {
    transformationContextMap = new HashMap<>();
    generatedStructureDefinitionMap = new HashMap<>();
  }

  public void addTransformationContext(String structureMapUrl, TransformContext transformContext) {
    this.transformationContextMap.put(structureMapUrl, transformContext);
  }

  public TransformContext getTransformationContext(String structureMapUrl) {
    return this.transformationContextMap.get(structureMapUrl);
  }

  public void addStructureDefinition(StructureDefinition structureDefinition) {
    this.generatedStructureDefinitionMap.put(structureDefinition.getUrl(), structureDefinition);
  }

  public StructureDefinition getStructureDefinition(String structureDefinitionUrl) {
    return this.generatedStructureDefinitionMap.get(structureDefinitionUrl);
  }

  public String getBaseGeneratedProfileUrl() {
    return baseGeneratedProfileUrl == null ? null : baseGeneratedProfileUrl.endsWith("/") ? baseGeneratedProfileUrl.substring(0, baseGeneratedProfileUrl.length() - 1) : baseGeneratedProfileUrl;
  }

  public void setBaseGeneratedProfileUrl(String baseGeneratedProfileUrl) {
    this.baseGeneratedProfileUrl = baseGeneratedProfileUrl;
  }
}
