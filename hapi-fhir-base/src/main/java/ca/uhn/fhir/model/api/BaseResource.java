package ca.uhn.fhir.model.api;

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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.ElementUtil;


public abstract class BaseResource<Id extends Serializable> extends BaseElement implements IResource
{


  // indexable dto
  protected String indexId;

  // indexable dto
  protected Timestamp tsIndexSync;

  // cancelable dto
  protected Timestamp tsCancellation;

  // cancelable dto
  protected String userCancelledId;

  // cancelable dto
  protected String cancellationState;

  // versionable dto
  protected Timestamp tsVersion;

  // versionable dto
  protected long versionNumber;

  // versionable dto
  protected String userVersionedId;

  protected Timestamp tsLastModified;

  protected Timestamp tsCreation;

  /**
   * Search parameter constant for <b>_language</b>
   */
  @SearchParamDefinition(name = "_language", path = "", description = "The language of the resource", type = "string")
  public static final String SP_RES_LANGUAGE = "_language";

  /**
   * Search parameter constant for <b>_id</b>
   */
  @SearchParamDefinition(name = "_id", path = "", description = "The ID of the resource", type = "string")
  public static final String SP_RES_ID = "_id";

  @Child(name = "contained", order = 2, min = 0, max = 1)
  private ContainedDt myContained;

  private IdDt myId;

  @Child(name = "language", order = 0, min = 0, max = Child.MAX_UNLIMITED)
  private CodeDt myLanguage;

  private Map<ResourceMetadataKeyEnum<?>, Object> myResourceMetadata;

  @Child(name = "text", order = 1, min = 0, max = 1)
  private NarrativeDt myText;



  @Override
  public ContainedDt getContained()
  {
    if (this.myContained == null)
    {
      this.myContained = new ContainedDt();
    }
    return this.myContained;
  }



  @Override
  public IdDt getId()
  {
    if (this.myId == null)
    {
      this.myId = new IdDt();
    }
    return this.myId;
  }



  @Override
  public CodeDt getLanguage()
  {
    if (this.myLanguage == null)
    {
      this.myLanguage = new CodeDt();
    }
    return this.myLanguage;
  }



  @Override
  public Map<ResourceMetadataKeyEnum<?>, Object> getResourceMetadata()
  {
    if (this.myResourceMetadata == null)
    {
      this.myResourceMetadata = new HashMap<ResourceMetadataKeyEnum<?>, Object>();
    }
    return this.myResourceMetadata;
  }



  @Override
  public NarrativeDt getText()
  {
    if (this.myText == null)
    {
      this.myText = new NarrativeDt();
    }
    return this.myText;
  }



  public void setContained(final ContainedDt theContained)
  {
    this.myContained = theContained;
  }



  @Override
  public void setId(final IdDt theId)
  {
    this.myId = theId;
  }



  public void setId(final String theId)
  {
    if (theId == null)
    {
      this.myId = null;
    }
    else
    {
      this.myId = new IdDt(theId);
    }
  }



  @Override
  public void setLanguage(final CodeDt theLanguage)
  {
    this.myLanguage = theLanguage;
  }



  @Override
  public void setResourceMetadata(final Map<ResourceMetadataKeyEnum<?>, Object> theMap)
  {
    Validate.notNull(theMap, "The Map must not be null");
    this.myResourceMetadata = theMap;
  }



  public void setText(final NarrativeDt theText)
  {
    this.myText = theText;
  }



  @Override
  public String toString()
  {
    final ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    b.append("id", getId().toUnqualified());
    return b.toString();
  }



  /**
   * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code> if all
   * content in this superclass instance is empty per the semantics of {@link #isEmpty()}.
   */
  @Override
  protected boolean isBaseEmpty()
  {
    return super.isBaseEmpty() && ElementUtil.isEmpty(this.myLanguage, this.myText, this.myId);
  }



  /**
   * @return the tsIndexSync
   */
  public Timestamp getTsIndexSync()
  {
    return this.tsIndexSync;
  }



  /**
   * @param tsIndexSync
   *          the tsIndexSync to set
   */
  public void setTsIndexSync(final Timestamp tsIndexSync)
  {
    this.tsIndexSync = tsIndexSync;
  }



  /**
   * @return the tsCancellation
   */
  public Timestamp getTsCancellation()
  {
    return this.tsCancellation;
  }



  /**
   * @param tsCancellation
   *          the tsCancellation to set
   */
  public void setTsCancellation(final Timestamp tsCancellation)
  {
    this.tsCancellation = tsCancellation;
  }



  /**
   * @return the userCancelledId
   */
  public String getUserCancelledId()
  {
    return this.userCancelledId;
  }



  /**
   * @param userCancelledId
   *          the userCancelledId to set
   */
  public void setUserCancelledId(final String userCancelledId)
  {
    this.userCancelledId = userCancelledId;
  }



  /**
   * @return the tsVersion
   */
  public Timestamp getTsVersion()
  {
    return this.tsVersion;
  }



  /**
   * @param tsVersion
   *          the tsVersion to set
   */
  public void setTsVersion(final Timestamp tsVersion)
  {
    this.tsVersion = tsVersion;
  }



  /**
   * @return the versionNumber
   */
  public long getVersionNumber()
  {
    return this.versionNumber;
  }



  /**
   * @param versionNumber
   *          the versionNumber to set
   */
  public void setVersionNumber(final long versionNumber)
  {
    this.versionNumber = versionNumber;
  }



  /**
   * @return the userVersionedId
   */
  public String getUserVersionedId()
  {
    return this.userVersionedId;
  }



  /**
   * @param userVersionedId
   *          the userVersionedId to set
   */
  public void setUserVersionedId(final String userVersionedId)
  {
    this.userVersionedId = userVersionedId;
  }



  /**
   * @return the tsLastModified
   */
  public Timestamp getTsLastModified()
  {
    return this.tsLastModified;
  }



  /**
   * @param tsLastModified
   *          the tsLastModified to set
   */
  public void setTsLastModified(final Timestamp tsLastModified)
  {
    this.tsLastModified = tsLastModified;
  }



  /**
   * @return the tsCreation
   */
  public Timestamp getTsCreation()
  {
    return this.tsCreation;
  }



  /**
   * @param tsCreation
   *          the tsCreation to set
   */
  public void setTsCreation(final Timestamp tsCreation)
  {
    this.tsCreation = tsCreation;
  }

}
