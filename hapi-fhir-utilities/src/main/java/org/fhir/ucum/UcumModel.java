/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *******************************************************************************/

package org.fhir.ucum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UcumModel {

	/**
	 * version="1.7" 
	 */
	private String version;
	
	/**
	 * revision="$Revision: 1.1 $"
	 */
	private String revision;
	
	/**
	 * date this revision was made public
	 */
	private Date revisionDate;
	
	private List<Prefix> prefixes = new ArrayList<Prefix>();
	private List<BaseUnit> baseUnits = new ArrayList<BaseUnit>();
	private List<DefinedUnit> definedUnits = new ArrayList<DefinedUnit>();

	
	/**
	 * @param revision
	 * @param revisionDate
	 */
	public UcumModel(String version, String revision, Date revisionDate) {
		super();
		this.version = version;
		this.revision = revision;
		this.revisionDate = revisionDate;
	}
	/**
	 * @return the prefixes
	 */
	public List<Prefix> getPrefixes() {
		return prefixes;
	}
	/**
	 * @return the baseUnits
	 */
	public List<BaseUnit> getBaseUnits() {
		return baseUnits;
	}
	/**
	 * @return the units
	 */
	public List<DefinedUnit> getDefinedUnits() {
		return definedUnits;
	}
	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}
	/**
	 * @param revision the revision to set
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}
	/**
	 * @return the revisionDate
	 */
	public Date getRevisionDate() {
		return revisionDate;
	}
	/**
	 * @param revisionDate the revisionDate to set
	 */
	public void setRevisionDate(Date revisionDate) {
		this.revisionDate = revisionDate;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Unit getUnit(String code) {
		for (Unit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		for (Unit unit : getDefinedUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}
	
	public BaseUnit getBaseUnit(String code) {
		for (BaseUnit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}
	
	
}
