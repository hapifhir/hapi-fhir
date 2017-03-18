package org.hl7.fhir.utilities.ucum;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

/*******************************************************************************
 * Crown Copyright (c) 2006+, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Health Intersections - ongoing maintenance
 *******************************************************************************/

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.exceptions.UcumException;


/**
 * General Ucum Service
 * 
 * UCUM functionality that is useful for applications that make use of UCUM codes
 * 
 * This is a tightly bound interface - consumers use the internal model classes
 * 
 * @author Grahame Grieve
 *
 */
public interface UcumService {

	public class UcumVersionDetails {
		private Date releaseDate;
		private String version;
		/**
		 * @param releaseDate
		 * @param version
		 */
		public UcumVersionDetails(Date releaseDate, String version) {
			super();
			this.releaseDate = releaseDate;
			this.version = version;
		}
		/**
		 * @return the releaseDate
		 */
		public Date getReleaseDate() {
			return releaseDate;
		}
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		
	}
	
	/**
	 * return Ucum Identification details for the version in use
	 */
	public abstract UcumVersionDetails ucumIdentification();


	/**
	 * Check UCUM. Note that this stands as a test of the service
	 * more than UCUM itself (for version 1.7, there are no known
	 * semantic errors in UCUM). But you should always run this test at least
	 * once with the version of UCUM you are using to ensure that 
	 * the service implementation correctly understands the UCUM data
	 * to which it is bound
	 *   
	 * @return a list of internal errors in the UCUM spec.
	 * 
	 */
	public abstract List<String> validateUCUM();


	/**
	 * return a list of the defined types of units in this UCUM version
	 * 
	 * @return
	 */
	public abstract Set<String> getProperties();

	/**
	 * validate whether a unit code are valid UCUM units
	 *  
	 * @param units - the unit code to check
	 * @return nil if valid, or an error message describing the problem
	 */
	public abstract String validate(String unit);

	/**
	 * given a unit, return a formal description of what the units stand for using
	 * full names 
	 * @param units the unit code
	 * @return formal description
	 * @throws UcumException 
	 * @throws OHFException 
	 */
	public String analyse(String unit) throws UcumException ;
	
	/**
	 * validate whether a units are valid UCUM units and additionally require that the 
	 * units from a particular property
	 *  
	 * @param units - the unit code to check
	 * @return nil if valid, or an error message describing the problem
	 */
	public abstract String validateInProperty(String unit, String property);

	/**
	 * validate whether a units are valid UCUM units and additionally require that the 
	 * units match a particular base canonical unit
	 *  
	 * @param units - the unit code to check
	 * @return nil if valid, or an error message describing the problem
	 */
	public abstract String validateCanonicalUnits(String unit, String canonical);

	/**
	 * given a set of units, return their canonical form
	 * @param unit
	 * @return the canonical form
	 * @throws UcumException 
	 * @throws OHFException 
	 */
	public abstract String getCanonicalUnits(String unit) throws UcumException ;

  /**
   * given two pairs of units, return true if they sahre the same canonical base
   * 
   * @param units1
   * @param units2
   * @return
   * @throws UcumException 
   * @ 
   */
  public abstract boolean isComparable(String units1, String units2) throws UcumException ;
  

	/**
	 * given a value and source unit, return the value in the given dest unit
	 * an exception is thrown if the conversion is not possible
	 * 
	 * @param value
	 * @param sourceUnit
	 * @param destUnit
	 * @return the value if a conversion is possible
	 * @throws UcumException 
	 * @throws OHFException
	 */
	public abstract Decimal convert(Decimal value, String sourceUnit, String destUnit) throws UcumException ;



	/**
	 * given a set of UCUM units, return a likely preferred human dense form
	 * 
	 * SI units - as is. 
	 * Other units - improved by manual fixes, or the removal of [] 
	 * 
	 * @param code
	 * @return the preferred human display form
	 */
	public abstract String getCommonDisplay(String code);



}
