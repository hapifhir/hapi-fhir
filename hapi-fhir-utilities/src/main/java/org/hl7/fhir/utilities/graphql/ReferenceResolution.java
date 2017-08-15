package org.hl7.fhir.utilities.graphql;

public class ReferenceResolution<RT> {
  private final RT targetContext;
  private final RT target;

  public ReferenceResolution(RT targetContext, RT target) {
    super();
    this.targetContext = targetContext;
    this.target = target;
  }

  public RT getTargetContext() {
    return targetContext;
  }

  public RT getTarget() {
    return target;
  }


}
