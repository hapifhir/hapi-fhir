package org.hl7.fhir.r4.terminologies;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;

public class ConceptMapEngine {

  private SimpleWorkerContext context;

  public ConceptMapEngine(SimpleWorkerContext context) {
    this.context = context;
  }

  public Coding translate(Coding source, String url) throws FHIRException {
    ConceptMap cm = context.fetchResource(ConceptMap.class, url);
    if (cm == null)
      throw new FHIRException("Unable to find ConceptMap '"+url+"'");
    if (source.hasSystem()) 
      return translateBySystem(cm, source.getSystem(), source.getCode());
    else
      return translateByJustCode(cm, source.getCode());
  }

  private Coding translateByJustCode(ConceptMap cm, String code) throws FHIRException {
    SourceElementComponent ct = null;
    ConceptMapGroupComponent cg = null;
    for (ConceptMapGroupComponent g : cm.getGroup()) {
      for (SourceElementComponent e : g.getElement()) {
        if (code.equals(e.getCode())) {
          if (e != null)
            throw new FHIRException("Unable to process translate "+code+" because multiple candidate matches were found in concept map "+cm.getUrl());
          ct = e;
          cg = g;
        }
      }
    }
    if (ct == null)
      return null;
    TargetElementComponent tt = null;
    for (TargetElementComponent t : ct.getTarget()) {
      if (!t.hasDependsOn() && !t.hasProduct() && isOkEquivalence(t.getEquivalence())) {
        if (tt != null)
          throw new FHIRException("Unable to process translate "+code+" because multiple targets were found in concept map "+cm.getUrl());
        tt = t;       
      }
    }
    if (tt == null)
      return null;
    return new Coding().setSystem(cg.getTarget()).setVersion(cg.getTargetVersion()).setCode(tt.getCode()).setDisplay(tt.getDisplay());
  }

  private boolean isOkEquivalence(ConceptMapEquivalence equivalence) {
    return equivalence != null && equivalence != ConceptMapEquivalence.DISJOINT && equivalence != ConceptMapEquivalence.UNMATCHED;
  }

  private Coding translateBySystem(ConceptMap cm, String system, String code) {
    throw new Error("Not done yet");
  }

}
