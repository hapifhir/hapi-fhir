package org.hl7.fhir.dstu2016may.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
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


import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.helpers.XMLFilterImpl;

// http://javacoalface.blogspot.com.au/2011/04/line-and-column-numbers-in-xml-dom.html

public class XmlLocationAnnotator extends XMLFilterImpl  {

  private Locator locator;
  private Stack<Locator> locatorStack = new Stack<Locator>();
  private Stack<Element> elementStack = new Stack<Element>();
  private UserDataHandler dataHandler = new LocationDataHandler();

  public XmlLocationAnnotator(XMLReader xmlReader, Document dom) {
      super(xmlReader);

      // Add listener to DOM, so we know which node was added.
      EventListener modListener = new EventListener() {
          @Override
          public void handleEvent(Event e) {
              EventTarget target = ((MutationEvent) e).getTarget();
              elementStack.push((Element) target);
          }
      };
      ((EventTarget) dom).addEventListener("DOMNodeInserted", modListener, true);
  }

  @Override
  public void setDocumentLocator(Locator locator) {
      super.setDocumentLocator(locator);
      this.locator = locator;
  }

  @Override
  public void startElement(String uri, String localName,
          String qName, Attributes atts) throws SAXException {
      super.startElement(uri, localName, qName, atts);

      // Keep snapshot of start location,
      // for later when end of element is found.
      locatorStack.push(new LocatorImpl(locator));
  }

  @Override
  public void endElement(String uri, String localName, String qName)
          throws SAXException {

      // Mutation event fired by the adding of element end,
      // and so lastAddedElement will be set.
      super.endElement(uri, localName, qName);
     
      if (locatorStack.size() > 0) {
          Locator startLocator = locatorStack.pop();
         
          XmlLocationData location = new XmlLocationData(
                  startLocator.getSystemId(),
                  startLocator.getLineNumber(),
                  startLocator.getColumnNumber(),
                  locator.getLineNumber(),
                  locator.getColumnNumber());
         Element lastAddedElement = elementStack.pop();
         
          lastAddedElement.setUserData(
                  XmlLocationData.LOCATION_DATA_KEY, location,
                  dataHandler);
      }
  }

  // Ensure location data copied to any new DOM node.
  private class LocationDataHandler implements UserDataHandler {

      @Override
      public void handle(short operation, String key, Object data,
              Node src, Node dst) {
         
          if (src != null && dst != null) {
              XmlLocationData locatonData = (XmlLocationData)
                      src.getUserData(XmlLocationData.LOCATION_DATA_KEY);
             
              if (locatonData != null) {
                  dst.setUserData(XmlLocationData.LOCATION_DATA_KEY,
                          locatonData, dataHandler);
              }
          }
      }
  }
}
