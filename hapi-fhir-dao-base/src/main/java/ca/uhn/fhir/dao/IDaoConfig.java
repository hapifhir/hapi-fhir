/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

package ca.uhn.fhir.dao;

/**
 * @author Bill.Denton
 *
 */
public interface IDaoConfig {
	public boolean isAllowMultipleDelete ();
	public void setAllowMultipleDelete (boolean allowMultipleDelete);
	public int getHardSearchLimit ();
	public void setHardSearchLimit (int limit);
	public int getHardTagListLimit ();
	public void setHardTagListLimit (int limit);
	public int getIncludeLimit ();
	public void setIncludeLimit (int limit);
}
