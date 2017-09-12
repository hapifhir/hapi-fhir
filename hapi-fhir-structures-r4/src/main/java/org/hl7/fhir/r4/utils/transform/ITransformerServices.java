package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Coding;

import java.util.List;

public interface ITransformerServices {
  //    public boolean validateByValueSet(Coding code, String valuesetId);
  public void log(String message); // log internal progress

  public Base createType(Object appInfo, String name) throws FHIRException;

  public Base createResource(Object appInfo, Base res); // an already created resource is provided; this is to identify/store it

  public Coding translate(Object appInfo, Coding source, String conceptMapUrl) throws FHIRException;

  //    public Coding translate(Coding code)
  //    ValueSet validation operation
  //    Translation operation
  //    Lookup another tree of data
  //    Create an instance tree
  //    Return the correct string format to refer to a tree (input or output)
  public Base resolveReference(Object appContext, String url);

  public List<Base> performSearch(Object appContext, String url);
}
