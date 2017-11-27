package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Coding;

import java.util.List;

/**
 * Transform
 */
public interface ITransformerServices {
  //    public boolean validateByValueSet(Coding code, String valueSetId);

  /**
   * Interface method for log
   *
   * @param message message written to log
   */
  void log(String message); // log internal progress

  /**
   * Interface method for createType
   *
   * @param appInfo appInfo
   * @param name    name of type
   * @return returns base object
   * @throws FHIRException if name is invalid
   */
  Base createType(Object appInfo, String name) throws FHIRException;

  /**
   * Interface method for createResource
   *
   * @param appInfo appInfo
   * @param res     resource
   * @return base object
   */
  Base createResource(Object appInfo, Base res); // an already created resource is provided; this is to identify/store it

  /**
   * Interface method for translate
   *
   * @param appInfo       appInfo
   * @param source        source object
   * @param conceptMapUrl translate map url
   * @return translated coding object
   * @throws FHIRException if translation is invalid
   */
  Coding translate(Object appInfo, Coding source, String conceptMapUrl) throws FHIRException;

  //    public Coding translate(Coding code)
  //    ValueSet validation operation
  //    Translation operation
  //    Lookup another tree of data
  //    Create an instance tree
  //    Return the correct string format to refer to a tree (input or output)

  /**
   * Interface method for resolve reference
   *
   * @param appContext appContext
   * @param url        referenced url
   * @return Base object
   */
  Base resolveReference(Object appContext, String url);

  /**
   * Interface method for performSearch
   *
   * @param appContext appContext
   * @param url        Url to be searched
   * @return List of Base objects as search results
   */
  List<Base> performSearch(Object appContext, String url);
}
