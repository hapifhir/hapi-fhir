package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeElemContainedResources;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceReferenceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.IModelVisitor;


class ParserState<T>
{


  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParserState.class);

  private final FhirContext myContext;

  private final boolean myJsonMode;

  private T myObject;

  private BaseState myState;



  private ParserState(final FhirContext theContext, final boolean theJsonMode)
  {
    this.myContext = theContext;
    this.myJsonMode = theJsonMode;
  }



  public void attributeValue(final String theName, final String theValue) throws DataFormatException
  {
    this.myState.attributeValue(theName, theValue);
  }



  public void endingElement() throws DataFormatException
  {
    this.myState.endingElement();
  }



  public void enteringNewElement(final String theNamespaceURI, final String theName) throws DataFormatException
  {
    this.myState.enteringNewElement(theNamespaceURI, theName);
  }



  public void enteringNewElementExtension(final StartElement theElem, final String theUrlAttr,
      final boolean theIsModifier)
  {
    this.myState.enteringNewElementExtension(theElem, theUrlAttr, theIsModifier);
  }



  @SuppressWarnings("unchecked")
  public T getObject()
  {
    return (T) this.myState.getCurrentElement();
  }



  public boolean isComplete()
  {
    return this.myObject != null;
  }



  public boolean isPreResource()
  {
    return this.myState.isPreResource();
  }



  private void pop()
  {
    this.myState = this.myState.myStack;
    this.myState.wereBack();
  }



  private void push(final BaseState theState)
  {
    theState.setStack(this.myState);
    this.myState = theState;
  }



  private void putPlacerResourceInDeletedEntry(final BundleEntry entry)
  {
    IdDt id = null;
    if (entry.getLinkSelf() != null && entry.getLinkSelf().isEmpty() == false)
    {
      id = new IdDt(entry.getLinkSelf().getValue());
    }
    else
    {
      id = entry.getId();
    }

    IResource resource = entry.getResource();
    if (resource == null && id != null && isNotBlank(id.getResourceType()))
    {
      final String resourceType = id.getResourceType();
      final RuntimeResourceDefinition def = this.myContext.getResourceDefinition(resourceType);
      if (def == null)
      {
        throw new DataFormatException("Entry references unknown resource type: " + resourceType);
      }
      resource = def.newInstance();
      resource.setId(id);
      entry.setResource(resource);
    }

    if (resource != null)
    {
      resource.getResourceMetadata().put(ResourceMetadataKeyEnum.DELETED_AT, entry.getDeletedAt());
      resource.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, id);
    }
  }



  public void string(final String theData)
  {
    this.myState.string(theData);
  }



  public boolean verifyNamespace(final String theExpect, final String theActual)
  {
    if (this.myJsonMode)
    {
      return true;
    }
    return StringUtils.equals(theExpect, theActual);
  }



  /**
   * Invoked after any new XML event is individually processed, containing a copy of the XML event. This is basically
   * intended for embedded XHTML content
   */
  public void xmlEvent(final XMLEvent theNextEvent)
  {
    this.myState.xmlEvent(theNextEvent);
  }



  public static ParserState<Bundle> getPreAtomInstance(final FhirContext theContext,
      final Class<? extends IResource> theResourceType, final boolean theJsonMode) throws DataFormatException
  {
    final ParserState<Bundle> retVal = new ParserState<Bundle>(theContext, theJsonMode);
    retVal.push(retVal.new PreAtomState(theResourceType));
    return retVal;
  }



  /**
   * @param theResourceType
   *          May be null
   */
  public static <T extends IResource> ParserState<T> getPreResourceInstance(final Class<T> theResourceType,
      final FhirContext theContext, final boolean theJsonMode) throws DataFormatException
  {
    final ParserState<T> retVal = new ParserState<T>(theContext, theJsonMode);
    retVal.push(retVal.new PreResourceState(theResourceType));
    return retVal;
  }



  public static ParserState<TagList> getPreTagListInstance(final FhirContext theContext, final boolean theJsonMode)
  {
    final ParserState<TagList> retVal = new ParserState<TagList>(theContext, theJsonMode);
    retVal.push(retVal.new PreTagListState());
    return retVal;
  }





  public class AtomAuthorState extends BaseState
  {


    private final BaseBundle myInstance;



    public AtomAuthorState(final BaseBundle theEntry)
    {
      super(null);
      this.myInstance = theEntry;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if ("name".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myInstance.getAuthorName()));
      }
      else if ("uri".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myInstance.getAuthorUri()));
      }
      else
      {
        throw new DataFormatException("Unexpected element: " + theLocalPart);
      }
    }

  }





  public class AtomCategoryState extends BaseState
  {


    private static final int STATE_LABEL = 2;

    private static final int STATE_NONE = 0;

    private static final int STATE_SCHEME = 3;

    private static final int STATE_TERM = 1;

    private int myCatState = STATE_NONE;

    private final Tag myInstance;



    public AtomCategoryState(final Tag theEntry)
    {
      super(null);
      this.myInstance = theEntry;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("term".equals(theName))
      {
        this.myInstance.setTerm(theValue);
      }
      else if ("label".equals(theName))
      {
        this.myInstance.setLabel(theValue);
      }
      else if ("scheme".equals(theName))
      {
        this.myInstance.setScheme(theValue);
      }
      else if ("value".equals(theName))
      {
        /*
         * This handles XML parsing, which is odd for this quasi-resource type, since the tag has three values
         * instead of one like everything else.
         */
        switch (this.myCatState)
        {
          case STATE_LABEL:
            this.myInstance.setLabel(theValue);
            break;
          case STATE_TERM:
            this.myInstance.setTerm(theValue);
            break;
          case STATE_SCHEME:
            this.myInstance.setScheme(theValue);
            break;
          default:
            super.string(theValue);
            break;
        }

      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (this.myCatState != STATE_NONE)
      {
        this.myCatState = STATE_NONE;
      }
      else
      {
        pop();
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theName) throws DataFormatException
    {
      if (this.myCatState != STATE_NONE)
      {
        throw new DataFormatException("Unexpected element in entry: " + theName);
      }
      if ("term".equals(theName))
      {
        this.myCatState = STATE_TERM;
      }
      else if ("label".equals(theName))
      {
        this.myCatState = STATE_LABEL;
      }
      else if ("scheme".equals(theName))
      {
        this.myCatState = STATE_SCHEME;
      }
    }

  }





  public class AtomDeletedEntryState extends AtomEntryState
  {


    public AtomDeletedEntryState(final Bundle theInstance, final Class<? extends IResource> theResourceType)
    {
      super(theInstance, theResourceType);
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("ref".equals(theName))
      {
        getEntry().setId(new IdDt(theValue));
      }
      else if ("when".equals(theName))
      {
        getEntry().setDeleted(new InstantDt(theValue));
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if ("by".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceURI))
      {
        push(new AtomDeletedEntryByState(getEntry()));
      }
      else if ("comment".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(getEntry().getDeletedComment()));
      }
      else
      {
        super.enteringNewElement(theNamespaceURI, theLocalPart);
      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      putPlacerResourceInDeletedEntry(getEntry());
      super.endingElement();
    }

  }





  public class AtomDeletedEntryByState extends BaseState
  {


    private final BundleEntry myEntry;



    public AtomDeletedEntryByState(final BundleEntry theEntry)
    {
      super(null);
      this.myEntry = theEntry;
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if ("name".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getDeletedByName()));
      }
      else if ("email".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getDeletedByEmail()));
      }
      else
      {
        throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }

  }





  private class AtomDeletedJsonWhenState extends BaseState
  {


    private String myData;

    private final IPrimitiveDatatype<?> myPrimitive;



    public AtomDeletedJsonWhenState(final IPrimitiveDatatype<?> thePrimitive)
    {
      super(null);
      Validate.notNull(thePrimitive, "thePrimitive");
      this.myPrimitive = thePrimitive;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      this.myData = theValue;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      this.myPrimitive.setValueAsString(this.myData);
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
    }



    @Override
    protected IElement getCurrentElement()
    {
      return null;
    }

  }





  public class AtomEntryState extends BaseState
  {


    private boolean myDeleted;

    private final BundleEntry myEntry;

    private final Class<? extends IResource> myResourceType;



    public AtomEntryState(final Bundle theInstance, final Class<? extends IResource> theResourceType)
    {
      super(null);
      this.myEntry = new BundleEntry();
      this.myResourceType = theResourceType;
      theInstance.getEntries().add(this.myEntry);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      populateResourceMetadata();
      pop();

      if (this.myDeleted)
      {
        putPlacerResourceInDeletedEntry(this.myEntry);
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if ("title".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getTitle()));
      }
      else if ("id".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getId()));
      }
      else if ("link".equals(theLocalPart))
      {
        push(new AtomLinkState(this.myEntry));
      }
      else if ("updated".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getUpdated()));
      }
      else if ("published".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myEntry.getPublished()));
      }
      else if ("author".equals(theLocalPart))
      {
        push(new AtomAuthorState(this.myEntry));
      }
      else if ("content".equals(theLocalPart))
      {
        push(new PreResourceState(this.myEntry, this.myResourceType));
      }
      else if ("summary".equals(theLocalPart))
      {
        push(new XhtmlState(getPreResourceState(), this.myEntry.getSummary(), false));
      }
      else if ("category".equals(theLocalPart))
      {
        push(new AtomCategoryState(this.myEntry.addCategory()));
      }
      else if ("deleted".equals(theLocalPart) && ParserState.this.myJsonMode)
      {
        // JSON and XML deleted entries are completely different for some reason
        this.myDeleted = true;
        push(new AtomDeletedJsonWhenState(this.myEntry.getDeletedAt()));
      }
      else
      {
        throw new DataFormatException("Unexpected element in entry: " + theLocalPart);
      }

      // TODO: handle category
    }



    protected BundleEntry getEntry()
    {
      return this.myEntry;
    }



    @SuppressWarnings("deprecation")
    private void populateResourceMetadata()
    {
      if (this.myEntry.getResource() == null)
      {
        return;
      }

      final IdDt id = this.myEntry.getId();
      if (id != null && id.isEmpty() == false)
      {
        this.myEntry.getResource().setId(id);
      }

      final Map<ResourceMetadataKeyEnum<?>, Object> metadata = this.myEntry.getResource().getResourceMetadata();
      if (this.myEntry.getPublished().isEmpty() == false)
      {
        ResourceMetadataKeyEnum.PUBLISHED.put(this.myEntry.getResource(), this.myEntry.getPublished());
      }
      if (this.myEntry.getUpdated().isEmpty() == false)
      {
        ResourceMetadataKeyEnum.UPDATED.put(this.myEntry.getResource(), this.myEntry.getUpdated());
      }

      ResourceMetadataKeyEnum.TITLE.put(this.myEntry.getResource(), this.myEntry.getTitle().getValue());

      if (this.myEntry.getCategories().isEmpty() == false)
      {
        final TagList tagList = new TagList();
        for (final Tag next : this.myEntry.getCategories())
        {
          tagList.add(next);
        }
        ResourceMetadataKeyEnum.TAG_LIST.put(this.myEntry.getResource(), tagList);
      }
      if (!this.myEntry.getLinkSelf().isEmpty())
      {
        final String linkSelfValue = this.myEntry.getLinkSelf().getValue();
        final IdDt linkSelf = new IdDt(linkSelfValue);
        this.myEntry.getResource().setId(linkSelf);
        if (isNotBlank(linkSelf.getVersionIdPart()))
        {
          metadata.put(ResourceMetadataKeyEnum.VERSION_ID, linkSelf);
        }
      }
      if (!this.myEntry.getLinkAlternate().isEmpty())
      {
        ResourceMetadataKeyEnum.LINK_ALTERNATE.put(this.myEntry.getResource(), this.myEntry.getLinkAlternate()
            .getValue());
      }
      if (!this.myEntry.getLinkSearch().isEmpty())
      {
        ResourceMetadataKeyEnum.LINK_SEARCH.put(this.myEntry.getResource(), this.myEntry.getLinkSearch().getValue());
      }

    }

  }





  private class AtomLinkState extends BaseState
  {


    private BundleEntry myEntry;

    private String myHref;

    private Bundle myInstance;

    private String myRel;



    public AtomLinkState(final Bundle theInstance)
    {
      super(null);
      this.myInstance = theInstance;
    }



    public AtomLinkState(final BundleEntry theEntry)
    {
      super(null);
      this.myEntry = theEntry;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("rel".equals(theName))
      {
        this.myRel = theValue;
      }
      else if ("href".equals(theName))
      {
        this.myHref = theValue;
      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (this.myInstance != null)
      {
        if ("self".equals(this.myRel))
        {
          this.myInstance.getLinkSelf().setValueAsString(this.myHref);
        }
        else if ("first".equals(this.myRel))
        {
          this.myInstance.getLinkFirst().setValueAsString(this.myHref);
        }
        else if ("previous".equals(this.myRel))
        {
          this.myInstance.getLinkPrevious().setValueAsString(this.myHref);
        }
        else if ("next".equals(this.myRel))
        {
          this.myInstance.getLinkNext().setValueAsString(this.myHref);
        }
        else if ("last".equals(this.myRel))
        {
          this.myInstance.getLinkLast().setValueAsString(this.myHref);
        }
        else if ("fhir-base".equals(this.myRel))
        {
          this.myInstance.getLinkBase().setValueAsString(this.myHref);
        }
      }
      else
      {
        if ("self".equals(this.myRel))
        {
          this.myEntry.getLinkSelf().setValueAsString(this.myHref);
        }
        else if ("search".equals(this.myRel))
        {
          this.myEntry.getLinkSearch().setValueAsString(this.myHref);
        }
        else if ("alternate".equals(this.myRel))
        {
          this.myEntry.getLinkAlternate().setValueAsString(this.myHref);
        }
      }
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      throw new DataFormatException("Found unexpected element content '" + theLocalPart + "' within <link>");
    }

  }





  private class AtomPrimitiveState extends BaseState
  {


    private String myData;

    private final IPrimitiveDatatype<?> myPrimitive;



    public AtomPrimitiveState(final IPrimitiveDatatype<?> thePrimitive)
    {
      super(null);
      Validate.notNull(thePrimitive, "thePrimitive");
      this.myPrimitive = thePrimitive;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if (ParserState.this.myJsonMode)
      {
        string(theValue);
      }
      super.attributeValue(theName, theValue);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      this.myPrimitive.setValueAsString(this.myData);
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
    }



    @Override
    protected IElement getCurrentElement()
    {
      return null;
    }



    @Override
    public void string(final String theData)
    {
      if (this.myData == null)
      {
        this.myData = theData;
      }
      else
      {
        // this shouldn't generally happen so it's ok that it's
        // inefficient
        this.myData = this.myData + theData;
      }
    }

  }





  private class AtomState extends BaseState
  {


    private final Bundle myInstance;

    private final Class<? extends IResource> myResourceType;



    public AtomState(final Bundle theInstance, final Class<? extends IResource> theResourceType)
    {
      super(null);
      this.myInstance = theInstance;
      this.myResourceType = theResourceType;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if ("entry".equals(theLocalPart) && verifyNamespace(XmlParser.ATOM_NS, theNamespaceURI))
      {
        push(new AtomEntryState(this.myInstance, this.myResourceType));
      }
      else if (theLocalPart.equals("published"))
      {
        push(new AtomPrimitiveState(this.myInstance.getPublished()));
      }
      else if (theLocalPart.equals("title"))
      {
        push(new AtomPrimitiveState(this.myInstance.getTitle()));
      }
      else if ("id".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myInstance.getBundleId()));
      }
      else if ("link".equals(theLocalPart))
      {
        push(new AtomLinkState(this.myInstance));
      }
      else if ("totalResults".equals(theLocalPart)
          && (verifyNamespace(XmlParser.OPENSEARCH_NS, theNamespaceURI) || verifyNamespace(
              Constants.OPENSEARCH_NS_OLDER, theNamespaceURI)))
      {
        push(new AtomPrimitiveState(this.myInstance.getTotalResults()));
      }
      else if ("updated".equals(theLocalPart))
      {
        push(new AtomPrimitiveState(this.myInstance.getUpdated()));
      }
      else if ("author".equals(theLocalPart))
      {
        push(new AtomAuthorState(this.myInstance));
      }
      else if ("category".equals(theLocalPart))
      {
        push(new AtomCategoryState(this.myInstance.getCategories().addTag()));
      }
      else if ("deleted-entry".equals(theLocalPart) && verifyNamespace(XmlParser.TOMBSTONES_NS, theNamespaceURI))
      {
        push(new AtomDeletedEntryState(this.myInstance, this.myResourceType));
      }
      else
      {
        if (theNamespaceURI != null)
        {
          throw new DataFormatException("Unexpected element: {" + theNamespaceURI + "}" + theLocalPart);
        }
        else
        {
          throw new DataFormatException("Unexpected element: " + theLocalPart);
        }
      }

      // TODO: handle category and DSig
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myInstance;
    }

  }





  private abstract class BaseState
  {


    private final PreResourceState myPreResourceState;

    private BaseState myStack;



    public BaseState(final PreResourceState thePreResourceState)
    {
      super();
      this.myPreResourceState = thePreResourceState;
    }



    @SuppressWarnings("unused")
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      // ignore by default
    }



    public void endingElement() throws DataFormatException
    {
      // ignore by default
    }



    @SuppressWarnings("unused")
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      // ignore by default
    }



    /**
     * Default implementation just handles undeclared extensions
     */
    @SuppressWarnings("unused")
    public void enteringNewElementExtension(final StartElement theElement, final String theUrlAttr,
        final boolean theIsModifier)
    {
      if (this.myPreResourceState != null && getCurrentElement() instanceof ISupportsUndeclaredExtensions)
      {
        final ExtensionDt newExtension = new ExtensionDt(theIsModifier, theUrlAttr);
        final ISupportsUndeclaredExtensions elem = (ISupportsUndeclaredExtensions) getCurrentElement();
        elem.addUndeclaredExtension(newExtension);
        final ExtensionState newState = new ExtensionState(this.myPreResourceState, newExtension);
        push(newState);
      }
      else
      {
        throw new DataFormatException("Type " + getCurrentElement()
            + " does not support undeclared extentions, and found an extension with URL: " + theUrlAttr);
      }
    }



    protected Object getCurrentElement()
    {
      return null;
    }



    public PreResourceState getPreResourceState()
    {
      return this.myPreResourceState;
    }



    public boolean isPreResource()
    {
      return false;
    }



    public void setStack(final BaseState theState)
    {
      this.myStack = theState;
    }



    /**
     * @param theData
     *          The string value
     */
    public void string(final String theData)
    {
      // ignore by default
    }



    public void wereBack()
    {
      // allow an implementor to override
    }



    /**
     * @param theNextEvent
     *          The XML event
     */
    public void xmlEvent(final XMLEvent theNextEvent)
    {
      // ignore
    }

  }





  private class BinaryResourceState extends BaseState
  {


    private static final int SUBSTATE_CONTENT = 2;

    private static final int SUBSTATE_CT = 1;

    private String myData;

    private final Binary myInstance;

    private int mySubState = 0;



    public BinaryResourceState(final PreResourceState thePreResourceState, final Binary theInstance)
    {
      super(thePreResourceState);
      this.myInstance = theInstance;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("id".equals(theName))
      {
        if (this.myInstance instanceof IIdentifiableElement)
        {
          ((IIdentifiableElement) this.myInstance).setElementSpecificId((theValue));
        }
        else if (this.myInstance instanceof IResource)
        {
          ((IResource) this.myInstance).setId(new IdDt(theValue));
        }
      }
      else if ("contentType".equals(theName))
      {
        this.myInstance.setContentType(theValue);
      }
      else if (ParserState.this.myJsonMode && "value".equals(theName))
      {
        string(theValue);
      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (this.mySubState == SUBSTATE_CT)
      {
        this.myInstance.setContentType(this.myData);
        this.mySubState = 0;
        this.myData = null;
        return;
      }
      else if (this.mySubState == SUBSTATE_CONTENT)
      {
        this.myInstance.setContentAsBase64(this.myData);
        this.mySubState = 0;
        this.myData = null;
        return;
      }
      else
      {
        if (!ParserState.this.myJsonMode)
        {
          this.myInstance.setContentAsBase64(this.myData);
        }
        pop();
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if (ParserState.this.myJsonMode && "contentType".equals(theLocalPart) && this.mySubState == 0)
      {
        this.mySubState = SUBSTATE_CT;
      }
      else if (ParserState.this.myJsonMode && "content".equals(theLocalPart) && this.mySubState == 0)
      {
        this.mySubState = SUBSTATE_CONTENT;
      }
      else
      {
        throw new DataFormatException("Unexpected nested element in atom tag: " + theLocalPart);
      }
    }



    @Override
    protected IElement getCurrentElement()
    {
      return null;
    }



    @Override
    public void string(final String theData)
    {
      if (this.myData == null)
      {
        this.myData = theData;
      }
      else
      {
        // this shouldn't generally happen so it's ok that it's
        // inefficient
        this.myData = this.myData + theData;
      }
    }

  }





  private class ContainedResourcesState extends PreResourceState
  {


    public ContainedResourcesState(final PreResourceState thePreResourcesState)
    {
      super(thePreResourcesState);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void wereBack()
    {
      final IResource res = getCurrentElement();
      assert res != null;
      if (res.getId() == null || res.getId().isEmpty())
      {
        ourLog.debug("Discarding contained resource with no ID!");
      }
      else
      {
        getPreResourceState().getContainedResources().put(res.getId().getValueAsString(), res);
      }
      getPreResourceState().getCurrentElement().getContained().getContainedResources().add(res);
    }

  }





  private class DeclaredExtensionState extends BaseState
  {


    private IElement myChildInstance;

    private final RuntimeChildDeclaredExtensionDefinition myDefinition;

    private final IElement myParentInstance;

    private final PreResourceState myPreResourceState;



    public DeclaredExtensionState(final PreResourceState thePreResourceState,
        final RuntimeChildDeclaredExtensionDefinition theDefinition, final IElement theParentInstance)
    {
      super(thePreResourceState);
      this.myPreResourceState = thePreResourceState;
      this.myDefinition = theDefinition;
      this.myParentInstance = theParentInstance;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      final BaseRuntimeElementDefinition<?> target = this.myDefinition.getChildByName(theLocalPart);
      if (target == null)
      {
        throw new DataFormatException("Unknown extension element name: " + theLocalPart);
      }

      switch (target.getChildType())
      {
        case COMPOSITE_DATATYPE:
        {
          final BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
          final ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
          this.myDefinition.getMutator().addValue(this.myParentInstance, newChildInstance);
          final ElementCompositeState newState = new ElementCompositeState(this.myPreResourceState, compositeTarget,
              newChildInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_DATATYPE:
        {
          final RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
          final IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
          this.myDefinition.getMutator().addValue(this.myParentInstance, newChildInstance);
          final PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case RESOURCE_REF:
        {
          final ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
          this.myDefinition.getMutator().addValue(this.myParentInstance, newChildInstance);
          final ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_XHTML:
        case RESOURCE:
        case RESOURCE_BLOCK:
        case UNDECL_EXT:
        case EXTENSION_DECLARED:
        default:
          break;
      }
    }



    @Override
    public void enteringNewElementExtension(final StartElement theElement, final String theUrlAttr,
        final boolean theIsModifier)
    {
      final RuntimeChildDeclaredExtensionDefinition declaredExtension = this.myDefinition
          .getChildExtensionForUrl(theUrlAttr);
      if (declaredExtension != null)
      {
        if (this.myChildInstance == null)
        {
          this.myChildInstance = this.myDefinition.newInstance();
          this.myDefinition.getMutator().addValue(this.myParentInstance, this.myChildInstance);
        }
        final BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension,
            this.myChildInstance);
        push(newState);
      }
      else
      {
        super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier);
      }
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myParentInstance;
    }

  }





  private class ElementCompositeState extends BaseState
  {


    private final BaseRuntimeElementCompositeDefinition<?> myDefinition;

    private final ICompositeElement myInstance;



    public ElementCompositeState(final PreResourceState thePreResourceState,
        final BaseRuntimeElementCompositeDefinition<?> theDef, final ICompositeElement theInstance)
    {
      super(thePreResourceState);
      this.myDefinition = theDef;
      this.myInstance = theInstance;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("id".equals(theName))
      {
        if (this.myInstance instanceof IIdentifiableElement)
        {
          ((IIdentifiableElement) this.myInstance).setElementSpecificId((theValue));
        }
        else if (this.myInstance instanceof IResource)
        {
          ((IResource) this.myInstance).setId(new IdDt(theValue));
        }
      }
      else if ("url".equals(theName) && this.myInstance instanceof ExtensionDt)
      {
        ((ExtensionDt) this.myInstance).setUrl(theValue);
      }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void endingElement()
    {
      pop();
      if (ParserState.this.myState == null)
      {
        ParserState.this.myObject = (T) this.myInstance;
      }
    }



    @Override
    public void enteringNewElement(final String theNamespace, final String theChildName) throws DataFormatException
    {
      BaseRuntimeChildDefinition child;
      try
      {
        child = this.myDefinition.getChildByNameOrThrowDataFormatException(theChildName);
      }
      catch (final DataFormatException e)
      {
        if (false)
        {// TODO: make this configurable
          throw e;
        }
        ourLog.warn(e.getMessage());
        push(new SwallowChildrenWholeState(getPreResourceState()));
        return;
      }
      final BaseRuntimeElementDefinition<?> target = child.getChildByName(theChildName);
      if (target == null)
      {
        throw new DataFormatException("Found unexpected element '" + theChildName + "' in parent element '"
            + this.myDefinition.getName() + "'. Valid names are: " + child.getValidChildNames());
      }

      switch (target.getChildType())
      {
        case COMPOSITE_DATATYPE:
        {
          final BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
          final ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance(child
              .getInstanceConstructorArguments());
          child.getMutator().addValue(this.myInstance, newChildInstance);
          final ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), compositeTarget,
              newChildInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_DATATYPE:
        {
          final RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
          IPrimitiveDatatype<?> newChildInstance;
          newChildInstance = primitiveTarget.newInstance(child.getInstanceConstructorArguments());
          child.getMutator().addValue(this.myInstance, newChildInstance);
          final PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case RESOURCE_REF:
        {
          final RuntimeResourceReferenceDefinition resourceRefTarget = (RuntimeResourceReferenceDefinition) target;
          final ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
          getPreResourceState().getResourceReferences().add(newChildInstance);
          child.getMutator().addValue(this.myInstance, newChildInstance);
          final ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case RESOURCE_BLOCK:
        {
          final RuntimeResourceBlockDefinition blockTarget = (RuntimeResourceBlockDefinition) target;
          final IResourceBlock newBlockInstance = blockTarget.newInstance();
          child.getMutator().addValue(this.myInstance, newBlockInstance);
          final ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), blockTarget,
              newBlockInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_XHTML:
        {
          final RuntimePrimitiveDatatypeNarrativeDefinition xhtmlTarget = (RuntimePrimitiveDatatypeNarrativeDefinition) target;
          final XhtmlDt newDt = xhtmlTarget.newInstance();
          child.getMutator().addValue(this.myInstance, newDt);
          final XhtmlState state = new XhtmlState(getPreResourceState(), newDt, true);
          push(state);
          return;
        }
        case CONTAINED_RESOURCES:
        {
          final RuntimeElemContainedResources targetElem = (RuntimeElemContainedResources) target;
          final List<? extends IElement> values = child.getAccessor().getValues(this.myInstance);
          ContainedDt newDt;
          if (values == null || values.isEmpty() || values.get(0) == null)
          {
            newDt = targetElem.newInstance();
            child.getMutator().addValue(this.myInstance, newDt);
          }
          else
          {
            newDt = (ContainedDt) values.get(0);
          }
          final ContainedResourcesState state = new ContainedResourcesState(getPreResourceState());
          push(state);
          return;
        }
        case UNDECL_EXT:
        case RESOURCE:
        case EXTENSION_DECLARED:
        {
          // Throw an exception because this shouldn't happen here
          break;
        }
      }

      throw new DataFormatException("Illegal resource position: " + target.getChildType());
    }



    @Override
    public void enteringNewElementExtension(final StartElement theElement, final String theUrlAttr,
        final boolean theIsModifier)
    {
      final RuntimeChildDeclaredExtensionDefinition declaredExtension = this.myDefinition
          .getDeclaredExtension(theUrlAttr);
      if (declaredExtension != null)
      {
        final BaseState newState = new DeclaredExtensionState(getPreResourceState(), declaredExtension, this.myInstance);
        push(newState);
      }
      else
      {
        super.enteringNewElementExtension(theElement, theUrlAttr, theIsModifier);
      }
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myInstance;
    }

  }





  private class ExtensionState extends BaseState
  {


    private final ExtensionDt myExtension;



    public ExtensionState(final PreResourceState thePreResourceState, final ExtensionDt theExtension)
    {
      super(thePreResourceState);
      this.myExtension = theExtension;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (this.myExtension.getValue() != null && this.myExtension.getUndeclaredExtensions().size() > 0)
      {
        throw new DataFormatException("Extension must not have both a value and other contained extensions");
      }
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      final BaseRuntimeElementDefinition<?> target = ParserState.this.myContext
          .getRuntimeChildUndeclaredExtensionDefinition().getChildByName(theLocalPart);
      if (target == null)
      {
        throw new DataFormatException("Unknown extension element name: " + theLocalPart);
      }

      switch (target.getChildType())
      {
        case COMPOSITE_DATATYPE:
        {
          final BaseRuntimeElementCompositeDefinition<?> compositeTarget = (BaseRuntimeElementCompositeDefinition<?>) target;
          final ICompositeDatatype newChildInstance = (ICompositeDatatype) compositeTarget.newInstance();
          this.myExtension.setValue(newChildInstance);
          final ElementCompositeState newState = new ElementCompositeState(getPreResourceState(), compositeTarget,
              newChildInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_DATATYPE:
        {
          final RuntimePrimitiveDatatypeDefinition primitiveTarget = (RuntimePrimitiveDatatypeDefinition) target;
          final IPrimitiveDatatype<?> newChildInstance = primitiveTarget.newInstance();
          this.myExtension.setValue(newChildInstance);
          final PrimitiveState newState = new PrimitiveState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case RESOURCE_REF:
        {
          final ResourceReferenceDt newChildInstance = new ResourceReferenceDt();
          this.myExtension.setValue(newChildInstance);
          final ResourceReferenceState newState = new ResourceReferenceState(getPreResourceState(), newChildInstance);
          push(newState);
          return;
        }
        case PRIMITIVE_XHTML:
        case RESOURCE:
        case RESOURCE_BLOCK:
        case UNDECL_EXT:
        case EXTENSION_DECLARED:
        case CONTAINED_RESOURCES:
          break;
      }
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myExtension;
    }

  }





  private class PreAtomState extends BaseState
  {


    private Bundle myInstance;

    private final Class<? extends IResource> myResourceType;



    public PreAtomState(final Class<? extends IResource> theResourceType)
    {
      super(null);
      this.myResourceType = theResourceType;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      // ignore
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if (!"feed".equals(theLocalPart))
      {
        throw new DataFormatException("Expecting outer element called 'feed', found: " + theLocalPart);
      }

      this.myInstance = new Bundle();
      push(new AtomState(this.myInstance, this.myResourceType));

    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myInstance;
    }



    @SuppressWarnings("unchecked")
    @Override
    public void wereBack()
    {
      ParserState.this.myObject = (T) this.myInstance;

      /*
       * Stitch together resource references
       */

      final Map<String, IResource> idToResource = new HashMap<String, IResource>();
      final List<IResource> resources = this.myInstance.toListOfResources();
      for (final IResource next : resources)
      {
        if (next.getId() != null && next.getId().isEmpty() == false)
        {
          idToResource.put(next.getId().toUnqualifiedVersionless().getValue(), next);
        }
      }

      for (final IResource next : resources)
      {
        final List<ResourceReferenceDt> refs = ParserState.this.myContext.newTerser()
            .getAllPopulatedChildElementsOfType(next, ResourceReferenceDt.class);
        for (final ResourceReferenceDt nextRef : refs)
        {
          if (nextRef.isEmpty() == false && nextRef.getReference() != null)
          {
            final IResource target = idToResource.get(nextRef.getReference().getValue());
            if (target != null)
            {
              nextRef.setResource(target);
            }
          }
        }
      }

    }

  }





  private class PreResourceState extends BaseState
  {


    private final Map<String, IResource> myContainedResources = new HashMap<String, IResource>();

    private BundleEntry myEntry;

    private IResource myInstance;

    private final List<ResourceReferenceDt> myResourceReferences = new ArrayList<ResourceReferenceDt>();

    private Class<? extends IResource> myResourceType;



    public PreResourceState(final BundleEntry theEntry, final Class<? extends IResource> theResourceType)
    {
      super(null);
      this.myEntry = theEntry;
      this.myResourceType = theResourceType;
    }



    /**
     * @param theResourceType
     *          May be null
     */
    public PreResourceState(final Class<? extends IResource> theResourceType)
    {
      super(null);
      this.myResourceType = theResourceType;
    }



    public PreResourceState(final PreResourceState thePreResourcesState)
    {
      super(thePreResourcesState);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      BaseRuntimeElementDefinition<?> definition;
      if (this.myResourceType == null)
      {
        definition = ParserState.this.myContext.getResourceDefinition(theLocalPart);
        if ((definition == null))
        {
          throw new DataFormatException("Element '" + theLocalPart
              + "' is not a resource, expected a resource at this position");
        }
      }
      else
      {
        definition = ParserState.this.myContext.getResourceDefinition(this.myResourceType);
        if (!StringUtils.equals(theLocalPart, definition.getName()))
        {
          definition = ParserState.this.myContext.getResourceDefinition(theLocalPart);
          if (!(definition instanceof RuntimeResourceDefinition))
          {
            throw new DataFormatException("Element '" + theLocalPart
                + "' is not a resource, expected a resource at this position");
          }
        }
      }

      final RuntimeResourceDefinition def = (RuntimeResourceDefinition) definition;
      this.myInstance = def.newInstance();
      if (this.myEntry != null)
      {
        this.myEntry.setResource(this.myInstance);
      }

      if ("Binary".equals(def.getName()))
      {
        push(new BinaryResourceState(getRootPreResourceState(), (Binary) this.myInstance));
      }
      else
      {
        push(new ElementCompositeState(getRootPreResourceState(), def, this.myInstance));
      }
    }



    public Map<String, IResource> getContainedResources()
    {
      return this.myContainedResources;
    }



    @Override
    protected IResource getCurrentElement()
    {
      return this.myInstance;
    }



    public List<ResourceReferenceDt> getResourceReferences()
    {
      return this.myResourceReferences;
    }



    private PreResourceState getRootPreResourceState()
    {
      if (getPreResourceState() != null)
      {
        return getPreResourceState();
      }
      else
      {
        return this;
      }
    }



    @Override
    public boolean isPreResource()
    {
      return true;
    }



    @SuppressWarnings("unchecked")
    @Override
    public void wereBack()
    {
      if (this.myEntry == null)
      {
        ParserState.this.myObject = (T) this.myInstance;
      }

      ParserState.this.myContext.newTerser().visit(this.myInstance, new IModelVisitor()
      {


        @Override
        public void acceptUndeclaredExtension(final ISupportsUndeclaredExtensions theContainingElement,
            final BaseRuntimeChildDefinition theChildDefinition, final BaseRuntimeElementDefinition<?> theDefinition,
            final ExtensionDt theNextExt)
        {
          acceptElement(theNextExt.getValue(), null, null);
        }



        @Override
        public void acceptElement(final IElement theElement, final BaseRuntimeChildDefinition theChildDefinition,
            final BaseRuntimeElementDefinition<?> theDefinition)
        {
          if (theElement instanceof ResourceReferenceDt)
          {
            final ResourceReferenceDt nextRef = (ResourceReferenceDt) theElement;
            final String ref = nextRef.getReference().getValue();
            if (isNotBlank(ref))
            {
              if (ref.startsWith("#"))
              {
                final IResource target = PreResourceState.this.myContainedResources.get(ref.substring(1));
                if (target != null)
                {
                  nextRef.setResource(target);
                }
                else
                {
                  ourLog.warn("Resource contains unknown local ref: " + ref);
                }
              }
            }
          }
        }
      });

    }

  }





  private class PreTagListState extends BaseState
  {


    private final TagList myTagList;



    public PreTagListState()
    {
      super(null);
      this.myTagList = new TagList();
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if (!TagList.ELEMENT_NAME_LC.equals(theLocalPart.toLowerCase()))
      {
        throw new DataFormatException("resourceType does not appear to be 'TagList', found: " + theLocalPart);
      }

      push(new TagListState(this.myTagList));
    }



    @Override
    protected TagList getCurrentElement()
    {
      return this.myTagList;
    }



    @Override
    public boolean isPreResource()
    {
      return true;
    }



    @SuppressWarnings("unchecked")
    @Override
    public void wereBack()
    {
      ParserState.this.myObject = (T) this.myTagList;
    }

  }





  private class PrimitiveState extends BaseState
  {


    private final IPrimitiveDatatype<?> myInstance;



    public PrimitiveState(final PreResourceState thePreResourceState, final IPrimitiveDatatype<?> theInstance)
    {
      super(thePreResourceState);
      this.myInstance = theInstance;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if ("value".equals(theName))
      {
        this.myInstance.setValueAsString(theValue);
      }
      else if ("id".equals(theName))
      {
        if (this.myInstance instanceof IIdentifiableElement)
        {
          ((IIdentifiableElement) this.myInstance).setElementSpecificId(theValue);
        }
        else if (this.myInstance instanceof IResource)
        {
          ((IResource) this.myInstance).setId(new IdDt(theValue));
        }
      }
    }



    @Override
    public void endingElement()
    {
      pop();
    }



    // @Override
    // public void enteringNewElementExtension(StartElement theElement,
    // String theUrlAttr) {
    // if (myInstance instanceof ISupportsUndeclaredExtensions) {
    // UndeclaredExtension ext = new UndeclaredExtension(theUrlAttr);
    // ((ISupportsUndeclaredExtensions)
    // myInstance).getUndeclaredExtensions().add(ext);
    // push(new ExtensionState(ext));
    // }
    // }

    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      throw new Error("Element " + theLocalPart + " in primitive!"); // TODO:
      // can
      // this
      // happen?
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myInstance;
    }

  }





  private class ResourceReferenceState extends BaseState
  {


    private final ResourceReferenceDt myInstance;

    private ResourceReferenceSubState mySubState;



    public ResourceReferenceState(final PreResourceState thePreResourceState, final ResourceReferenceDt theInstance)
    {
      super(thePreResourceState);
      this.myInstance = theInstance;
      this.mySubState = ResourceReferenceSubState.INITIAL;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if (!"value".equals(theName))
      {
        return;
      }

      switch (this.mySubState)
      {
        case DISPLAY:
          this.myInstance.setDisplay(theValue);
          break;
        case INITIAL:
          throw new DataFormatException("Unexpected attribute: " + theValue);
        case REFERENCE:
          this.myInstance.setReference(theValue);
          break;
      }
    }



    @Override
    public void endingElement()
    {
      switch (this.mySubState)
      {
        case INITIAL:
          pop();
          break;
        case DISPLAY:
        case REFERENCE:
          this.mySubState = ResourceReferenceSubState.INITIAL;
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      switch (this.mySubState)
      {
        case INITIAL:
          if ("display".equals(theLocalPart))
          {
            this.mySubState = ResourceReferenceSubState.DISPLAY;
            break;
          }
          else if ("reference".equals(theLocalPart))
          {
            this.mySubState = ResourceReferenceSubState.REFERENCE;
            break;
          }
          else if ("resource".equals(theLocalPart))
          {
            this.mySubState = ResourceReferenceSubState.REFERENCE;
            break;
          }
          //$FALL-THROUGH$
        case DISPLAY:
        case REFERENCE:
          throw new DataFormatException("Unexpected element: " + theLocalPart);
      }
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myInstance;
    }

  }





  private enum ResourceReferenceSubState
  {
    DISPLAY,
    INITIAL,
    REFERENCE
  }





  private class SwallowChildrenWholeState extends BaseState
  {


    private int myDepth;



    public SwallowChildrenWholeState(final PreResourceState thePreResourceState)
    {
      super(thePreResourceState);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      this.myDepth--;
      if (this.myDepth < 0)
      {
        pop();
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      this.myDepth++;
    }

  }





  private class TagListState extends BaseState
  {


    private final TagList myTagList;



    public TagListState(final TagList theTagList)
    {
      super(null);
      this.myTagList = theTagList;
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      pop();
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if (TagList.ATTR_CATEGORY.equals(theLocalPart))
      {
        push(new TagState(this.myTagList.addTag()));
      }
      else
      {
        throw new DataFormatException("Unexpected element: " + theLocalPart);
      }
    }

  }





  private class TagState extends BaseState
  {


    private static final int LABEL = 2;

    private static final int NONE = 0;

    private static final int SCHEME = 3;

    private static final int TERM = 1;

    private int mySubState = 0;

    private final Tag myTag;



    public TagState(final Tag theTag)
    {
      super(null);
      this.myTag = theTag;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      final String value = defaultIfBlank(theValue, null);

      switch (this.mySubState)
      {
        case TERM:
          this.myTag.setTerm(value);
          break;
        case LABEL:
          this.myTag.setLabel(value);
          break;
        case SCHEME:
          this.myTag.setScheme(value);
          break;
        case NONE:
          // This handles JSON encoding, which is a bit weird
          enteringNewElement(null, theName);
          attributeValue(null, value);
          endingElement();
          break;
      }
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (this.mySubState != NONE)
      {
        this.mySubState = NONE;
      }
      else
      {
        pop();
      }
    }



    @Override
    public void enteringNewElement(final String theNamespaceURI, final String theLocalPart) throws DataFormatException
    {
      if (Tag.ATTR_TERM.equals(theLocalPart))
      {
        this.mySubState = TERM;
      }
      else if (Tag.ATTR_SCHEME.equals(theLocalPart))
      {
        this.mySubState = SCHEME;
      }
      else if (Tag.ATTR_LABEL.equals(theLocalPart))
      {
        this.mySubState = LABEL;
      }
      else
      {
        throw new DataFormatException("Unexpected element: " + theLocalPart);
      }
    }

  }





  private class XhtmlState extends BaseState
  {


    private int myDepth;

    private final XhtmlDt myDt;

    private final List<XMLEvent> myEvents = new ArrayList<XMLEvent>();

    private final boolean myIncludeOuterEvent;



    private XhtmlState(final PreResourceState thePreResourceState, final XhtmlDt theXhtmlDt,
        final boolean theIncludeOuterEvent) throws DataFormatException
    {
      super(thePreResourceState);
      this.myDepth = 0;
      this.myDt = theXhtmlDt;
      this.myIncludeOuterEvent = theIncludeOuterEvent;
    }



    @Override
    public void attributeValue(final String theName, final String theValue) throws DataFormatException
    {
      if (ParserState.this.myJsonMode)
      {
        this.myDt.setValueAsString(theValue);
        return;
      }
      super.attributeValue(theName, theValue);
    }



    @Override
    public void endingElement() throws DataFormatException
    {
      if (ParserState.this.myJsonMode)
      {
        pop();
        return;
      }
      super.endingElement();
    }



    @Override
    protected IElement getCurrentElement()
    {
      return this.myDt;
    }



    @Override
    public void xmlEvent(final XMLEvent theEvent)
    {
      if (theEvent.isEndElement())
      {
        this.myDepth--;
      }

      if (this.myIncludeOuterEvent || this.myDepth > 0)
      {
        this.myEvents.add(theEvent);
      }

      if (theEvent.isStartElement())
      {
        this.myDepth++;
      }

      if (theEvent.isEndElement())
      {
        if (this.myDepth == 0)
        {
          this.myDt.setValue(this.myEvents);
          pop();
        }
      }
    }

  }

}
