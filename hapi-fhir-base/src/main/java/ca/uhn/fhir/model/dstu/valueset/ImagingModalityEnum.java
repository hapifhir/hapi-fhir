
package ca.uhn.fhir.model.dstu.valueset;

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

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum ImagingModalityEnum {

	/**
	 * Code Value: <b>AR</b>
	 */
	AR("AR", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>BMD</b>
	 */
	BMD("BMD", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>BDUS</b>
	 */
	BDUS("BDUS", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>EPS</b>
	 */
	EPS("EPS", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>CR</b>
	 */
	CR("CR", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>CT</b>
	 */
	CT("CT", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>DX</b>
	 */
	DX("DX", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>ECG</b>
	 */
	ECG("ECG", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>ES</b>
	 */
	ES("ES", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>XC</b>
	 */
	XC("XC", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>GM</b>
	 */
	GM("GM", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>HD</b>
	 */
	HD("HD", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>IO</b>
	 */
	IO("IO", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>IVOCT</b>
	 */
	IVOCT("IVOCT", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>IVUS</b>
	 */
	IVUS("IVUS", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>KER</b>
	 */
	KER("KER", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>LEN</b>
	 */
	LEN("LEN", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>MR</b>
	 */
	MR("MR", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>MG</b>
	 */
	MG("MG", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>NM</b>
	 */
	NM("NM", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OAM</b>
	 */
	OAM("OAM", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OCT</b>
	 */
	OCT("OCT", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OPM</b>
	 */
	OPM("OPM", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OP</b>
	 */
	OP("OP", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OPR</b>
	 */
	OPR("OPR", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OPT</b>
	 */
	OPT("OPT", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>OPV</b>
	 */
	OPV("OPV", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>PX</b>
	 */
	PX("PX", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>PT</b>
	 */
	PT("PT", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>RF</b>
	 */
	RF("RF", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>RG</b>
	 */
	RG("RG", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>SM</b>
	 */
	SM("SM", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>SRF</b>
	 */
	SRF("SRF", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>US</b>
	 */
	US("US", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>VA</b>
	 */
	VA("VA", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>XA</b>
	 */
	XA("XA", "http://nema.org/dicom/dcid"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/imaging-modality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/imaging-modality";

	/**
	 * Name for this Value Set:
	 * ImagingModality
	 */
	public static final String VALUESET_NAME = "ImagingModality";

	private static Map<String, ImagingModalityEnum> CODE_TO_ENUM = new HashMap<String, ImagingModalityEnum>();
	private static Map<String, Map<String, ImagingModalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ImagingModalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ImagingModalityEnum next : ImagingModalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ImagingModalityEnum>());
			}
			SYSTEM_TO_CODE_TO_ENUM.get(next.getSystem()).put(next.getCode(), next);			
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the code system associated with this enumerated value
	 */
	public String getSystem() {
		return mySystem;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public ImagingModalityEnum forCode(String theCode) {
		ImagingModalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ImagingModalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ImagingModalityEnum>() {
		@Override
		public String toCodeString(ImagingModalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ImagingModalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ImagingModalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ImagingModalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ImagingModalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ImagingModalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
