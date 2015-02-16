package org.hl7.fhir.instance.model;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sat, Feb 14, 2015 16:12-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Representation of the content produced in a DICOM imaging study. A study comprises a set of Series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A Series is of only one modality (e.g., X-ray, CT, MR, ultrasound), but a Study may have multiple Series of different modalities.
 */
@ResourceDef(name="ImagingStudy", profile="http://hl7.org/fhir/Profile/ImagingStudy")
public class ImagingStudy extends DomainResource {

    public enum ImagingModality {
        /**
         * 
         */
        AR, 
        /**
         * 
         */
        BMD, 
        /**
         * 
         */
        BDUS, 
        /**
         * 
         */
        EPS, 
        /**
         * 
         */
        CR, 
        /**
         * 
         */
        CT, 
        /**
         * 
         */
        DX, 
        /**
         * 
         */
        ECG, 
        /**
         * 
         */
        ES, 
        /**
         * 
         */
        XC, 
        /**
         * 
         */
        GM, 
        /**
         * 
         */
        HD, 
        /**
         * 
         */
        IO, 
        /**
         * 
         */
        IVOCT, 
        /**
         * 
         */
        IVUS, 
        /**
         * 
         */
        KER, 
        /**
         * 
         */
        LEN, 
        /**
         * 
         */
        MR, 
        /**
         * 
         */
        MG, 
        /**
         * 
         */
        NM, 
        /**
         * 
         */
        OAM, 
        /**
         * 
         */
        OCT, 
        /**
         * 
         */
        OPM, 
        /**
         * 
         */
        OP, 
        /**
         * 
         */
        OPR, 
        /**
         * 
         */
        OPT, 
        /**
         * 
         */
        OPV, 
        /**
         * 
         */
        PX, 
        /**
         * 
         */
        PT, 
        /**
         * 
         */
        RF, 
        /**
         * 
         */
        RG, 
        /**
         * 
         */
        SM, 
        /**
         * 
         */
        SRF, 
        /**
         * 
         */
        US, 
        /**
         * 
         */
        VA, 
        /**
         * 
         */
        XA, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImagingModality fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AR".equals(codeString))
          return AR;
        if ("BMD".equals(codeString))
          return BMD;
        if ("BDUS".equals(codeString))
          return BDUS;
        if ("EPS".equals(codeString))
          return EPS;
        if ("CR".equals(codeString))
          return CR;
        if ("CT".equals(codeString))
          return CT;
        if ("DX".equals(codeString))
          return DX;
        if ("ECG".equals(codeString))
          return ECG;
        if ("ES".equals(codeString))
          return ES;
        if ("XC".equals(codeString))
          return XC;
        if ("GM".equals(codeString))
          return GM;
        if ("HD".equals(codeString))
          return HD;
        if ("IO".equals(codeString))
          return IO;
        if ("IVOCT".equals(codeString))
          return IVOCT;
        if ("IVUS".equals(codeString))
          return IVUS;
        if ("KER".equals(codeString))
          return KER;
        if ("LEN".equals(codeString))
          return LEN;
        if ("MR".equals(codeString))
          return MR;
        if ("MG".equals(codeString))
          return MG;
        if ("NM".equals(codeString))
          return NM;
        if ("OAM".equals(codeString))
          return OAM;
        if ("OCT".equals(codeString))
          return OCT;
        if ("OPM".equals(codeString))
          return OPM;
        if ("OP".equals(codeString))
          return OP;
        if ("OPR".equals(codeString))
          return OPR;
        if ("OPT".equals(codeString))
          return OPT;
        if ("OPV".equals(codeString))
          return OPV;
        if ("PX".equals(codeString))
          return PX;
        if ("PT".equals(codeString))
          return PT;
        if ("RF".equals(codeString))
          return RF;
        if ("RG".equals(codeString))
          return RG;
        if ("SM".equals(codeString))
          return SM;
        if ("SRF".equals(codeString))
          return SRF;
        if ("US".equals(codeString))
          return US;
        if ("VA".equals(codeString))
          return VA;
        if ("XA".equals(codeString))
          return XA;
        throw new Exception("Unknown ImagingModality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AR: return "AR";
            case BMD: return "BMD";
            case BDUS: return "BDUS";
            case EPS: return "EPS";
            case CR: return "CR";
            case CT: return "CT";
            case DX: return "DX";
            case ECG: return "ECG";
            case ES: return "ES";
            case XC: return "XC";
            case GM: return "GM";
            case HD: return "HD";
            case IO: return "IO";
            case IVOCT: return "IVOCT";
            case IVUS: return "IVUS";
            case KER: return "KER";
            case LEN: return "LEN";
            case MR: return "MR";
            case MG: return "MG";
            case NM: return "NM";
            case OAM: return "OAM";
            case OCT: return "OCT";
            case OPM: return "OPM";
            case OP: return "OP";
            case OPR: return "OPR";
            case OPT: return "OPT";
            case OPV: return "OPV";
            case PX: return "PX";
            case PT: return "PT";
            case RF: return "RF";
            case RG: return "RG";
            case SM: return "SM";
            case SRF: return "SRF";
            case US: return "US";
            case VA: return "VA";
            case XA: return "XA";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AR: return "http://nema.org/dicom/dicm";
            case BMD: return "http://nema.org/dicom/dicm";
            case BDUS: return "http://nema.org/dicom/dicm";
            case EPS: return "http://nema.org/dicom/dicm";
            case CR: return "http://nema.org/dicom/dicm";
            case CT: return "http://nema.org/dicom/dicm";
            case DX: return "http://nema.org/dicom/dicm";
            case ECG: return "http://nema.org/dicom/dicm";
            case ES: return "http://nema.org/dicom/dicm";
            case XC: return "http://nema.org/dicom/dicm";
            case GM: return "http://nema.org/dicom/dicm";
            case HD: return "http://nema.org/dicom/dicm";
            case IO: return "http://nema.org/dicom/dicm";
            case IVOCT: return "http://nema.org/dicom/dicm";
            case IVUS: return "http://nema.org/dicom/dicm";
            case KER: return "http://nema.org/dicom/dicm";
            case LEN: return "http://nema.org/dicom/dicm";
            case MR: return "http://nema.org/dicom/dicm";
            case MG: return "http://nema.org/dicom/dicm";
            case NM: return "http://nema.org/dicom/dicm";
            case OAM: return "http://nema.org/dicom/dicm";
            case OCT: return "http://nema.org/dicom/dicm";
            case OPM: return "http://nema.org/dicom/dicm";
            case OP: return "http://nema.org/dicom/dicm";
            case OPR: return "http://nema.org/dicom/dicm";
            case OPT: return "http://nema.org/dicom/dicm";
            case OPV: return "http://nema.org/dicom/dicm";
            case PX: return "http://nema.org/dicom/dicm";
            case PT: return "http://nema.org/dicom/dicm";
            case RF: return "http://nema.org/dicom/dicm";
            case RG: return "http://nema.org/dicom/dicm";
            case SM: return "http://nema.org/dicom/dicm";
            case SRF: return "http://nema.org/dicom/dicm";
            case US: return "http://nema.org/dicom/dicm";
            case VA: return "http://nema.org/dicom/dicm";
            case XA: return "http://nema.org/dicom/dicm";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AR: return "";
            case BMD: return "";
            case BDUS: return "";
            case EPS: return "";
            case CR: return "";
            case CT: return "";
            case DX: return "";
            case ECG: return "";
            case ES: return "";
            case XC: return "";
            case GM: return "";
            case HD: return "";
            case IO: return "";
            case IVOCT: return "";
            case IVUS: return "";
            case KER: return "";
            case LEN: return "";
            case MR: return "";
            case MG: return "";
            case NM: return "";
            case OAM: return "";
            case OCT: return "";
            case OPM: return "";
            case OP: return "";
            case OPR: return "";
            case OPT: return "";
            case OPV: return "";
            case PX: return "";
            case PT: return "";
            case RF: return "";
            case RG: return "";
            case SM: return "";
            case SRF: return "";
            case US: return "";
            case VA: return "";
            case XA: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AR: return "AR";
            case BMD: return "BMD";
            case BDUS: return "BDUS";
            case EPS: return "EPS";
            case CR: return "CR";
            case CT: return "CT";
            case DX: return "DX";
            case ECG: return "ECG";
            case ES: return "ES";
            case XC: return "XC";
            case GM: return "GM";
            case HD: return "HD";
            case IO: return "IO";
            case IVOCT: return "IVOCT";
            case IVUS: return "IVUS";
            case KER: return "KER";
            case LEN: return "LEN";
            case MR: return "MR";
            case MG: return "MG";
            case NM: return "NM";
            case OAM: return "OAM";
            case OCT: return "OCT";
            case OPM: return "OPM";
            case OP: return "OP";
            case OPR: return "OPR";
            case OPT: return "OPT";
            case OPV: return "OPV";
            case PX: return "PX";
            case PT: return "PT";
            case RF: return "RF";
            case RG: return "RG";
            case SM: return "SM";
            case SRF: return "SRF";
            case US: return "US";
            case VA: return "VA";
            case XA: return "XA";
            default: return "?";
          }
        }
    }

  public static class ImagingModalityEnumFactory implements EnumFactory<ImagingModality> {
    public ImagingModality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AR".equals(codeString))
          return ImagingModality.AR;
        if ("BMD".equals(codeString))
          return ImagingModality.BMD;
        if ("BDUS".equals(codeString))
          return ImagingModality.BDUS;
        if ("EPS".equals(codeString))
          return ImagingModality.EPS;
        if ("CR".equals(codeString))
          return ImagingModality.CR;
        if ("CT".equals(codeString))
          return ImagingModality.CT;
        if ("DX".equals(codeString))
          return ImagingModality.DX;
        if ("ECG".equals(codeString))
          return ImagingModality.ECG;
        if ("ES".equals(codeString))
          return ImagingModality.ES;
        if ("XC".equals(codeString))
          return ImagingModality.XC;
        if ("GM".equals(codeString))
          return ImagingModality.GM;
        if ("HD".equals(codeString))
          return ImagingModality.HD;
        if ("IO".equals(codeString))
          return ImagingModality.IO;
        if ("IVOCT".equals(codeString))
          return ImagingModality.IVOCT;
        if ("IVUS".equals(codeString))
          return ImagingModality.IVUS;
        if ("KER".equals(codeString))
          return ImagingModality.KER;
        if ("LEN".equals(codeString))
          return ImagingModality.LEN;
        if ("MR".equals(codeString))
          return ImagingModality.MR;
        if ("MG".equals(codeString))
          return ImagingModality.MG;
        if ("NM".equals(codeString))
          return ImagingModality.NM;
        if ("OAM".equals(codeString))
          return ImagingModality.OAM;
        if ("OCT".equals(codeString))
          return ImagingModality.OCT;
        if ("OPM".equals(codeString))
          return ImagingModality.OPM;
        if ("OP".equals(codeString))
          return ImagingModality.OP;
        if ("OPR".equals(codeString))
          return ImagingModality.OPR;
        if ("OPT".equals(codeString))
          return ImagingModality.OPT;
        if ("OPV".equals(codeString))
          return ImagingModality.OPV;
        if ("PX".equals(codeString))
          return ImagingModality.PX;
        if ("PT".equals(codeString))
          return ImagingModality.PT;
        if ("RF".equals(codeString))
          return ImagingModality.RF;
        if ("RG".equals(codeString))
          return ImagingModality.RG;
        if ("SM".equals(codeString))
          return ImagingModality.SM;
        if ("SRF".equals(codeString))
          return ImagingModality.SRF;
        if ("US".equals(codeString))
          return ImagingModality.US;
        if ("VA".equals(codeString))
          return ImagingModality.VA;
        if ("XA".equals(codeString))
          return ImagingModality.XA;
        throw new IllegalArgumentException("Unknown ImagingModality code '"+codeString+"'");
        }
    public String toCode(ImagingModality code) {
      if (code == ImagingModality.AR)
        return "AR";
      if (code == ImagingModality.BMD)
        return "BMD";
      if (code == ImagingModality.BDUS)
        return "BDUS";
      if (code == ImagingModality.EPS)
        return "EPS";
      if (code == ImagingModality.CR)
        return "CR";
      if (code == ImagingModality.CT)
        return "CT";
      if (code == ImagingModality.DX)
        return "DX";
      if (code == ImagingModality.ECG)
        return "ECG";
      if (code == ImagingModality.ES)
        return "ES";
      if (code == ImagingModality.XC)
        return "XC";
      if (code == ImagingModality.GM)
        return "GM";
      if (code == ImagingModality.HD)
        return "HD";
      if (code == ImagingModality.IO)
        return "IO";
      if (code == ImagingModality.IVOCT)
        return "IVOCT";
      if (code == ImagingModality.IVUS)
        return "IVUS";
      if (code == ImagingModality.KER)
        return "KER";
      if (code == ImagingModality.LEN)
        return "LEN";
      if (code == ImagingModality.MR)
        return "MR";
      if (code == ImagingModality.MG)
        return "MG";
      if (code == ImagingModality.NM)
        return "NM";
      if (code == ImagingModality.OAM)
        return "OAM";
      if (code == ImagingModality.OCT)
        return "OCT";
      if (code == ImagingModality.OPM)
        return "OPM";
      if (code == ImagingModality.OP)
        return "OP";
      if (code == ImagingModality.OPR)
        return "OPR";
      if (code == ImagingModality.OPT)
        return "OPT";
      if (code == ImagingModality.OPV)
        return "OPV";
      if (code == ImagingModality.PX)
        return "PX";
      if (code == ImagingModality.PT)
        return "PT";
      if (code == ImagingModality.RF)
        return "RF";
      if (code == ImagingModality.RG)
        return "RG";
      if (code == ImagingModality.SM)
        return "SM";
      if (code == ImagingModality.SRF)
        return "SRF";
      if (code == ImagingModality.US)
        return "US";
      if (code == ImagingModality.VA)
        return "VA";
      if (code == ImagingModality.XA)
        return "XA";
      return "?";
      }
    }

    public enum InstanceAvailability {
        /**
         * Resources are immediately available,.
         */
        ONLINE, 
        /**
         * Resources need to be retrieved by manual intervention.
         */
        OFFLINE, 
        /**
         * Resources need to be retrieved from relatively slow media.
         */
        NEARLINE, 
        /**
         * Resources cannot be retrieved.
         */
        UNAVAILABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static InstanceAvailability fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ONLINE".equals(codeString))
          return ONLINE;
        if ("OFFLINE".equals(codeString))
          return OFFLINE;
        if ("NEARLINE".equals(codeString))
          return NEARLINE;
        if ("UNAVAILABLE".equals(codeString))
          return UNAVAILABLE;
        throw new Exception("Unknown InstanceAvailability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ONLINE: return "ONLINE";
            case OFFLINE: return "OFFLINE";
            case NEARLINE: return "NEARLINE";
            case UNAVAILABLE: return "UNAVAILABLE";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ONLINE: return "http://nema.org/dicom/dicm";
            case OFFLINE: return "http://nema.org/dicom/dicm";
            case NEARLINE: return "http://nema.org/dicom/dicm";
            case UNAVAILABLE: return "http://nema.org/dicom/dicm";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ONLINE: return "Resources are immediately available,.";
            case OFFLINE: return "Resources need to be retrieved by manual intervention.";
            case NEARLINE: return "Resources need to be retrieved from relatively slow media.";
            case UNAVAILABLE: return "Resources cannot be retrieved.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ONLINE: return "ONLINE";
            case OFFLINE: return "OFFLINE";
            case NEARLINE: return "NEARLINE";
            case UNAVAILABLE: return "UNAVAILABLE";
            default: return "?";
          }
        }
    }

  public static class InstanceAvailabilityEnumFactory implements EnumFactory<InstanceAvailability> {
    public InstanceAvailability fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ONLINE".equals(codeString))
          return InstanceAvailability.ONLINE;
        if ("OFFLINE".equals(codeString))
          return InstanceAvailability.OFFLINE;
        if ("NEARLINE".equals(codeString))
          return InstanceAvailability.NEARLINE;
        if ("UNAVAILABLE".equals(codeString))
          return InstanceAvailability.UNAVAILABLE;
        throw new IllegalArgumentException("Unknown InstanceAvailability code '"+codeString+"'");
        }
    public String toCode(InstanceAvailability code) {
      if (code == InstanceAvailability.ONLINE)
        return "ONLINE";
      if (code == InstanceAvailability.OFFLINE)
        return "OFFLINE";
      if (code == InstanceAvailability.NEARLINE)
        return "NEARLINE";
      if (code == InstanceAvailability.UNAVAILABLE)
        return "UNAVAILABLE";
      return "?";
      }
    }

    public enum Modality {
        /**
         * 
         */
        AR, 
        /**
         * 
         */
        AU, 
        /**
         * 
         */
        BDUS, 
        /**
         * 
         */
        BI, 
        /**
         * 
         */
        BMD, 
        /**
         * 
         */
        CR, 
        /**
         * 
         */
        CT, 
        /**
         * 
         */
        DG, 
        /**
         * 
         */
        DX, 
        /**
         * 
         */
        ECG, 
        /**
         * 
         */
        EPS, 
        /**
         * 
         */
        ES, 
        /**
         * 
         */
        GM, 
        /**
         * 
         */
        HC, 
        /**
         * 
         */
        HD, 
        /**
         * 
         */
        IO, 
        /**
         * 
         */
        IVOCT, 
        /**
         * 
         */
        IVUS, 
        /**
         * 
         */
        KER, 
        /**
         * 
         */
        KO, 
        /**
         * 
         */
        LEN, 
        /**
         * 
         */
        LS, 
        /**
         * 
         */
        MG, 
        /**
         * 
         */
        MR, 
        /**
         * 
         */
        NM, 
        /**
         * 
         */
        OAM, 
        /**
         * 
         */
        OCT, 
        /**
         * 
         */
        OP, 
        /**
         * 
         */
        OPM, 
        /**
         * 
         */
        OPT, 
        /**
         * 
         */
        OPV, 
        /**
         * 
         */
        OT, 
        /**
         * 
         */
        PR, 
        /**
         * 
         */
        PT, 
        /**
         * 
         */
        PX, 
        /**
         * 
         */
        REG, 
        /**
         * 
         */
        RF, 
        /**
         * 
         */
        RG, 
        /**
         * 
         */
        RTDOSE, 
        /**
         * 
         */
        RTIMAGE, 
        /**
         * 
         */
        RTPLAN, 
        /**
         * 
         */
        RTRECORD, 
        /**
         * 
         */
        RTSTRUCT, 
        /**
         * 
         */
        SEG, 
        /**
         * 
         */
        SM, 
        /**
         * 
         */
        SMR, 
        /**
         * 
         */
        SR, 
        /**
         * 
         */
        SRF, 
        /**
         * 
         */
        TG, 
        /**
         * 
         */
        US, 
        /**
         * 
         */
        VA, 
        /**
         * 
         */
        XA, 
        /**
         * 
         */
        XC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Modality fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AR".equals(codeString))
          return AR;
        if ("AU".equals(codeString))
          return AU;
        if ("BDUS".equals(codeString))
          return BDUS;
        if ("BI".equals(codeString))
          return BI;
        if ("BMD".equals(codeString))
          return BMD;
        if ("CR".equals(codeString))
          return CR;
        if ("CT".equals(codeString))
          return CT;
        if ("DG".equals(codeString))
          return DG;
        if ("DX".equals(codeString))
          return DX;
        if ("ECG".equals(codeString))
          return ECG;
        if ("EPS".equals(codeString))
          return EPS;
        if ("ES".equals(codeString))
          return ES;
        if ("GM".equals(codeString))
          return GM;
        if ("HC".equals(codeString))
          return HC;
        if ("HD".equals(codeString))
          return HD;
        if ("IO".equals(codeString))
          return IO;
        if ("IVOCT".equals(codeString))
          return IVOCT;
        if ("IVUS".equals(codeString))
          return IVUS;
        if ("KER".equals(codeString))
          return KER;
        if ("KO".equals(codeString))
          return KO;
        if ("LEN".equals(codeString))
          return LEN;
        if ("LS".equals(codeString))
          return LS;
        if ("MG".equals(codeString))
          return MG;
        if ("MR".equals(codeString))
          return MR;
        if ("NM".equals(codeString))
          return NM;
        if ("OAM".equals(codeString))
          return OAM;
        if ("OCT".equals(codeString))
          return OCT;
        if ("OP".equals(codeString))
          return OP;
        if ("OPM".equals(codeString))
          return OPM;
        if ("OPT".equals(codeString))
          return OPT;
        if ("OPV".equals(codeString))
          return OPV;
        if ("OT".equals(codeString))
          return OT;
        if ("PR".equals(codeString))
          return PR;
        if ("PT".equals(codeString))
          return PT;
        if ("PX".equals(codeString))
          return PX;
        if ("REG".equals(codeString))
          return REG;
        if ("RF".equals(codeString))
          return RF;
        if ("RG".equals(codeString))
          return RG;
        if ("RTDOSE".equals(codeString))
          return RTDOSE;
        if ("RTIMAGE".equals(codeString))
          return RTIMAGE;
        if ("RTPLAN".equals(codeString))
          return RTPLAN;
        if ("RTRECORD".equals(codeString))
          return RTRECORD;
        if ("RTSTRUCT".equals(codeString))
          return RTSTRUCT;
        if ("SEG".equals(codeString))
          return SEG;
        if ("SM".equals(codeString))
          return SM;
        if ("SMR".equals(codeString))
          return SMR;
        if ("SR".equals(codeString))
          return SR;
        if ("SRF".equals(codeString))
          return SRF;
        if ("TG".equals(codeString))
          return TG;
        if ("US".equals(codeString))
          return US;
        if ("VA".equals(codeString))
          return VA;
        if ("XA".equals(codeString))
          return XA;
        if ("XC".equals(codeString))
          return XC;
        throw new Exception("Unknown Modality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AR: return "AR";
            case AU: return "AU";
            case BDUS: return "BDUS";
            case BI: return "BI";
            case BMD: return "BMD";
            case CR: return "CR";
            case CT: return "CT";
            case DG: return "DG";
            case DX: return "DX";
            case ECG: return "ECG";
            case EPS: return "EPS";
            case ES: return "ES";
            case GM: return "GM";
            case HC: return "HC";
            case HD: return "HD";
            case IO: return "IO";
            case IVOCT: return "IVOCT";
            case IVUS: return "IVUS";
            case KER: return "KER";
            case KO: return "KO";
            case LEN: return "LEN";
            case LS: return "LS";
            case MG: return "MG";
            case MR: return "MR";
            case NM: return "NM";
            case OAM: return "OAM";
            case OCT: return "OCT";
            case OP: return "OP";
            case OPM: return "OPM";
            case OPT: return "OPT";
            case OPV: return "OPV";
            case OT: return "OT";
            case PR: return "PR";
            case PT: return "PT";
            case PX: return "PX";
            case REG: return "REG";
            case RF: return "RF";
            case RG: return "RG";
            case RTDOSE: return "RTDOSE";
            case RTIMAGE: return "RTIMAGE";
            case RTPLAN: return "RTPLAN";
            case RTRECORD: return "RTRECORD";
            case RTSTRUCT: return "RTSTRUCT";
            case SEG: return "SEG";
            case SM: return "SM";
            case SMR: return "SMR";
            case SR: return "SR";
            case SRF: return "SRF";
            case TG: return "TG";
            case US: return "US";
            case VA: return "VA";
            case XA: return "XA";
            case XC: return "XC";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AR: return "http://nema.org/dicom/dicm";
            case AU: return "http://nema.org/dicom/dicm";
            case BDUS: return "http://nema.org/dicom/dicm";
            case BI: return "http://nema.org/dicom/dicm";
            case BMD: return "http://nema.org/dicom/dicm";
            case CR: return "http://nema.org/dicom/dicm";
            case CT: return "http://nema.org/dicom/dicm";
            case DG: return "http://nema.org/dicom/dicm";
            case DX: return "http://nema.org/dicom/dicm";
            case ECG: return "http://nema.org/dicom/dicm";
            case EPS: return "http://nema.org/dicom/dicm";
            case ES: return "http://nema.org/dicom/dicm";
            case GM: return "http://nema.org/dicom/dicm";
            case HC: return "http://nema.org/dicom/dicm";
            case HD: return "http://nema.org/dicom/dicm";
            case IO: return "http://nema.org/dicom/dicm";
            case IVOCT: return "http://nema.org/dicom/dicm";
            case IVUS: return "http://nema.org/dicom/dicm";
            case KER: return "http://nema.org/dicom/dicm";
            case KO: return "http://nema.org/dicom/dicm";
            case LEN: return "http://nema.org/dicom/dicm";
            case LS: return "http://nema.org/dicom/dicm";
            case MG: return "http://nema.org/dicom/dicm";
            case MR: return "http://nema.org/dicom/dicm";
            case NM: return "http://nema.org/dicom/dicm";
            case OAM: return "http://nema.org/dicom/dicm";
            case OCT: return "http://nema.org/dicom/dicm";
            case OP: return "http://nema.org/dicom/dicm";
            case OPM: return "http://nema.org/dicom/dicm";
            case OPT: return "http://nema.org/dicom/dicm";
            case OPV: return "http://nema.org/dicom/dicm";
            case OT: return "http://nema.org/dicom/dicm";
            case PR: return "http://nema.org/dicom/dicm";
            case PT: return "http://nema.org/dicom/dicm";
            case PX: return "http://nema.org/dicom/dicm";
            case REG: return "http://nema.org/dicom/dicm";
            case RF: return "http://nema.org/dicom/dicm";
            case RG: return "http://nema.org/dicom/dicm";
            case RTDOSE: return "http://nema.org/dicom/dicm";
            case RTIMAGE: return "http://nema.org/dicom/dicm";
            case RTPLAN: return "http://nema.org/dicom/dicm";
            case RTRECORD: return "http://nema.org/dicom/dicm";
            case RTSTRUCT: return "http://nema.org/dicom/dicm";
            case SEG: return "http://nema.org/dicom/dicm";
            case SM: return "http://nema.org/dicom/dicm";
            case SMR: return "http://nema.org/dicom/dicm";
            case SR: return "http://nema.org/dicom/dicm";
            case SRF: return "http://nema.org/dicom/dicm";
            case TG: return "http://nema.org/dicom/dicm";
            case US: return "http://nema.org/dicom/dicm";
            case VA: return "http://nema.org/dicom/dicm";
            case XA: return "http://nema.org/dicom/dicm";
            case XC: return "http://nema.org/dicom/dicm";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AR: return "";
            case AU: return "";
            case BDUS: return "";
            case BI: return "";
            case BMD: return "";
            case CR: return "";
            case CT: return "";
            case DG: return "";
            case DX: return "";
            case ECG: return "";
            case EPS: return "";
            case ES: return "";
            case GM: return "";
            case HC: return "";
            case HD: return "";
            case IO: return "";
            case IVOCT: return "";
            case IVUS: return "";
            case KER: return "";
            case KO: return "";
            case LEN: return "";
            case LS: return "";
            case MG: return "";
            case MR: return "";
            case NM: return "";
            case OAM: return "";
            case OCT: return "";
            case OP: return "";
            case OPM: return "";
            case OPT: return "";
            case OPV: return "";
            case OT: return "";
            case PR: return "";
            case PT: return "";
            case PX: return "";
            case REG: return "";
            case RF: return "";
            case RG: return "";
            case RTDOSE: return "";
            case RTIMAGE: return "";
            case RTPLAN: return "";
            case RTRECORD: return "";
            case RTSTRUCT: return "";
            case SEG: return "";
            case SM: return "";
            case SMR: return "";
            case SR: return "";
            case SRF: return "";
            case TG: return "";
            case US: return "";
            case VA: return "";
            case XA: return "";
            case XC: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AR: return "AR";
            case AU: return "AU";
            case BDUS: return "BDUS";
            case BI: return "BI";
            case BMD: return "BMD";
            case CR: return "CR";
            case CT: return "CT";
            case DG: return "DG";
            case DX: return "DX";
            case ECG: return "ECG";
            case EPS: return "EPS";
            case ES: return "ES";
            case GM: return "GM";
            case HC: return "HC";
            case HD: return "HD";
            case IO: return "IO";
            case IVOCT: return "IVOCT";
            case IVUS: return "IVUS";
            case KER: return "KER";
            case KO: return "KO";
            case LEN: return "LEN";
            case LS: return "LS";
            case MG: return "MG";
            case MR: return "MR";
            case NM: return "NM";
            case OAM: return "OAM";
            case OCT: return "OCT";
            case OP: return "OP";
            case OPM: return "OPM";
            case OPT: return "OPT";
            case OPV: return "OPV";
            case OT: return "OT";
            case PR: return "PR";
            case PT: return "PT";
            case PX: return "PX";
            case REG: return "REG";
            case RF: return "RF";
            case RG: return "RG";
            case RTDOSE: return "RTDOSE";
            case RTIMAGE: return "RTIMAGE";
            case RTPLAN: return "RTPLAN";
            case RTRECORD: return "RTRECORD";
            case RTSTRUCT: return "RTSTRUCT";
            case SEG: return "SEG";
            case SM: return "SM";
            case SMR: return "SMR";
            case SR: return "SR";
            case SRF: return "SRF";
            case TG: return "TG";
            case US: return "US";
            case VA: return "VA";
            case XA: return "XA";
            case XC: return "XC";
            default: return "?";
          }
        }
    }

  public static class ModalityEnumFactory implements EnumFactory<Modality> {
    public Modality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AR".equals(codeString))
          return Modality.AR;
        if ("AU".equals(codeString))
          return Modality.AU;
        if ("BDUS".equals(codeString))
          return Modality.BDUS;
        if ("BI".equals(codeString))
          return Modality.BI;
        if ("BMD".equals(codeString))
          return Modality.BMD;
        if ("CR".equals(codeString))
          return Modality.CR;
        if ("CT".equals(codeString))
          return Modality.CT;
        if ("DG".equals(codeString))
          return Modality.DG;
        if ("DX".equals(codeString))
          return Modality.DX;
        if ("ECG".equals(codeString))
          return Modality.ECG;
        if ("EPS".equals(codeString))
          return Modality.EPS;
        if ("ES".equals(codeString))
          return Modality.ES;
        if ("GM".equals(codeString))
          return Modality.GM;
        if ("HC".equals(codeString))
          return Modality.HC;
        if ("HD".equals(codeString))
          return Modality.HD;
        if ("IO".equals(codeString))
          return Modality.IO;
        if ("IVOCT".equals(codeString))
          return Modality.IVOCT;
        if ("IVUS".equals(codeString))
          return Modality.IVUS;
        if ("KER".equals(codeString))
          return Modality.KER;
        if ("KO".equals(codeString))
          return Modality.KO;
        if ("LEN".equals(codeString))
          return Modality.LEN;
        if ("LS".equals(codeString))
          return Modality.LS;
        if ("MG".equals(codeString))
          return Modality.MG;
        if ("MR".equals(codeString))
          return Modality.MR;
        if ("NM".equals(codeString))
          return Modality.NM;
        if ("OAM".equals(codeString))
          return Modality.OAM;
        if ("OCT".equals(codeString))
          return Modality.OCT;
        if ("OP".equals(codeString))
          return Modality.OP;
        if ("OPM".equals(codeString))
          return Modality.OPM;
        if ("OPT".equals(codeString))
          return Modality.OPT;
        if ("OPV".equals(codeString))
          return Modality.OPV;
        if ("OT".equals(codeString))
          return Modality.OT;
        if ("PR".equals(codeString))
          return Modality.PR;
        if ("PT".equals(codeString))
          return Modality.PT;
        if ("PX".equals(codeString))
          return Modality.PX;
        if ("REG".equals(codeString))
          return Modality.REG;
        if ("RF".equals(codeString))
          return Modality.RF;
        if ("RG".equals(codeString))
          return Modality.RG;
        if ("RTDOSE".equals(codeString))
          return Modality.RTDOSE;
        if ("RTIMAGE".equals(codeString))
          return Modality.RTIMAGE;
        if ("RTPLAN".equals(codeString))
          return Modality.RTPLAN;
        if ("RTRECORD".equals(codeString))
          return Modality.RTRECORD;
        if ("RTSTRUCT".equals(codeString))
          return Modality.RTSTRUCT;
        if ("SEG".equals(codeString))
          return Modality.SEG;
        if ("SM".equals(codeString))
          return Modality.SM;
        if ("SMR".equals(codeString))
          return Modality.SMR;
        if ("SR".equals(codeString))
          return Modality.SR;
        if ("SRF".equals(codeString))
          return Modality.SRF;
        if ("TG".equals(codeString))
          return Modality.TG;
        if ("US".equals(codeString))
          return Modality.US;
        if ("VA".equals(codeString))
          return Modality.VA;
        if ("XA".equals(codeString))
          return Modality.XA;
        if ("XC".equals(codeString))
          return Modality.XC;
        throw new IllegalArgumentException("Unknown Modality code '"+codeString+"'");
        }
    public String toCode(Modality code) {
      if (code == Modality.AR)
        return "AR";
      if (code == Modality.AU)
        return "AU";
      if (code == Modality.BDUS)
        return "BDUS";
      if (code == Modality.BI)
        return "BI";
      if (code == Modality.BMD)
        return "BMD";
      if (code == Modality.CR)
        return "CR";
      if (code == Modality.CT)
        return "CT";
      if (code == Modality.DG)
        return "DG";
      if (code == Modality.DX)
        return "DX";
      if (code == Modality.ECG)
        return "ECG";
      if (code == Modality.EPS)
        return "EPS";
      if (code == Modality.ES)
        return "ES";
      if (code == Modality.GM)
        return "GM";
      if (code == Modality.HC)
        return "HC";
      if (code == Modality.HD)
        return "HD";
      if (code == Modality.IO)
        return "IO";
      if (code == Modality.IVOCT)
        return "IVOCT";
      if (code == Modality.IVUS)
        return "IVUS";
      if (code == Modality.KER)
        return "KER";
      if (code == Modality.KO)
        return "KO";
      if (code == Modality.LEN)
        return "LEN";
      if (code == Modality.LS)
        return "LS";
      if (code == Modality.MG)
        return "MG";
      if (code == Modality.MR)
        return "MR";
      if (code == Modality.NM)
        return "NM";
      if (code == Modality.OAM)
        return "OAM";
      if (code == Modality.OCT)
        return "OCT";
      if (code == Modality.OP)
        return "OP";
      if (code == Modality.OPM)
        return "OPM";
      if (code == Modality.OPT)
        return "OPT";
      if (code == Modality.OPV)
        return "OPV";
      if (code == Modality.OT)
        return "OT";
      if (code == Modality.PR)
        return "PR";
      if (code == Modality.PT)
        return "PT";
      if (code == Modality.PX)
        return "PX";
      if (code == Modality.REG)
        return "REG";
      if (code == Modality.RF)
        return "RF";
      if (code == Modality.RG)
        return "RG";
      if (code == Modality.RTDOSE)
        return "RTDOSE";
      if (code == Modality.RTIMAGE)
        return "RTIMAGE";
      if (code == Modality.RTPLAN)
        return "RTPLAN";
      if (code == Modality.RTRECORD)
        return "RTRECORD";
      if (code == Modality.RTSTRUCT)
        return "RTSTRUCT";
      if (code == Modality.SEG)
        return "SEG";
      if (code == Modality.SM)
        return "SM";
      if (code == Modality.SMR)
        return "SMR";
      if (code == Modality.SR)
        return "SR";
      if (code == Modality.SRF)
        return "SRF";
      if (code == Modality.TG)
        return "TG";
      if (code == Modality.US)
        return "US";
      if (code == Modality.VA)
        return "VA";
      if (code == Modality.XA)
        return "XA";
      if (code == Modality.XC)
        return "XC";
      return "?";
      }
    }

    @Block()
    public static class ImagingStudySeriesComponent extends BackboneElement {
        /**
         * The Numeric identifier of this series in the study.
         */
        @Child(name="number", type={IntegerType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Numeric identifier of this series (0020,0011)", formalDefinition="The Numeric identifier of this series in the study." )
        protected IntegerType number;

        /**
         * The modality of this series sequence.
         */
        @Child(name="modality", type={CodeType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="The modality of the instances in the series (0008,0060)", formalDefinition="The modality of this series sequence." )
        protected Enumeration<Modality> modality;

        /**
         * Formal identifier for this series.
         */
        @Child(name="uid", type={OidType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Formal identifier for this series (0020,000E)", formalDefinition="Formal identifier for this series." )
        protected OidType uid;

        /**
         * A description of the series.
         */
        @Child(name="description", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="A description of the series (0008,103E)", formalDefinition="A description of the series." )
        protected StringType description;

        /**
         * Sequence that contains attributes from the.
         */
        @Child(name="numberOfInstances", type={IntegerType.class}, order=5, min=1, max=1)
        @Description(shortDefinition="Number of Series Related Instances (0020,1209)", formalDefinition="Sequence that contains attributes from the." )
        protected IntegerType numberOfInstances;

        /**
         * Availability of series (online, offline or nearline).
         */
        @Child(name="availability", type={CodeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)", formalDefinition="Availability of series (online, offline or nearline)." )
        protected Enumeration<InstanceAvailability> availability;

        /**
         * WADO-RS URI where Series is available.
         */
        @Child(name="url", type={UriType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Retrieve URI (0008,1115 > 0008,1190)", formalDefinition="WADO-RS URI where Series is available." )
        protected UriType url;

        /**
         * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.
         */
        @Child(name="bodySite", type={Coding.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Body part examined (Map from 0018,0015)", formalDefinition="Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed." )
        protected Coding bodySite;

        /**
         * The date when the series was started.
         */
        @Child(name="dateTime", type={DateTimeType.class}, order=9, min=0, max=1)
        @Description(shortDefinition="When the series started", formalDefinition="The date when the series was started." )
        protected DateTimeType dateTime;

        /**
         * A single image taken from a patient.
         */
        @Child(name="instance", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A single instance taken from a patient (image or other)", formalDefinition="A single image taken from a patient." )
        protected List<ImagingStudySeriesInstanceComponent> instance;

        private static final long serialVersionUID = -1442035574L;

      public ImagingStudySeriesComponent() {
        super();
      }

      public ImagingStudySeriesComponent(Enumeration<Modality> modality, OidType uid, IntegerType numberOfInstances) {
        super();
        this.modality = modality;
        this.uid = uid;
        this.numberOfInstances = numberOfInstances;
      }

        /**
         * @return {@link #number} (The Numeric identifier of this series in the study.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public IntegerType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new IntegerType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The Numeric identifier of this series in the study.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ImagingStudySeriesComponent setNumberElement(IntegerType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The Numeric identifier of this series in the study.
         */
        public int getNumber() { 
          return this.number == null ? 0 : this.number.getValue();
        }

        /**
         * @param value The Numeric identifier of this series in the study.
         */
        public ImagingStudySeriesComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new IntegerType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #modality} (The modality of this series sequence.). This is the underlying object with id, value and extensions. The accessor "getModality" gives direct access to the value
         */
        public Enumeration<Modality> getModalityElement() { 
          if (this.modality == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.modality");
            else if (Configuration.doAutoCreate())
              this.modality = new Enumeration<Modality>(new ModalityEnumFactory()); // bb
          return this.modality;
        }

        public boolean hasModalityElement() { 
          return this.modality != null && !this.modality.isEmpty();
        }

        public boolean hasModality() { 
          return this.modality != null && !this.modality.isEmpty();
        }

        /**
         * @param value {@link #modality} (The modality of this series sequence.). This is the underlying object with id, value and extensions. The accessor "getModality" gives direct access to the value
         */
        public ImagingStudySeriesComponent setModalityElement(Enumeration<Modality> value) { 
          this.modality = value;
          return this;
        }

        /**
         * @return The modality of this series sequence.
         */
        public Modality getModality() { 
          return this.modality == null ? null : this.modality.getValue();
        }

        /**
         * @param value The modality of this series sequence.
         */
        public ImagingStudySeriesComponent setModality(Modality value) { 
            if (this.modality == null)
              this.modality = new Enumeration<Modality>(new ModalityEnumFactory());
            this.modality.setValue(value);
          return this;
        }

        /**
         * @return {@link #uid} (Formal identifier for this series.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType(); // bb
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Formal identifier for this series.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public ImagingStudySeriesComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Formal identifier for this series.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Formal identifier for this series.
         */
        public ImagingStudySeriesComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A description of the series.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A description of the series.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImagingStudySeriesComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the series.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the series.
         */
        public ImagingStudySeriesComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #numberOfInstances} (Sequence that contains attributes from the.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
         */
        public IntegerType getNumberOfInstancesElement() { 
          if (this.numberOfInstances == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.numberOfInstances");
            else if (Configuration.doAutoCreate())
              this.numberOfInstances = new IntegerType(); // bb
          return this.numberOfInstances;
        }

        public boolean hasNumberOfInstancesElement() { 
          return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
        }

        public boolean hasNumberOfInstances() { 
          return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
        }

        /**
         * @param value {@link #numberOfInstances} (Sequence that contains attributes from the.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
         */
        public ImagingStudySeriesComponent setNumberOfInstancesElement(IntegerType value) { 
          this.numberOfInstances = value;
          return this;
        }

        /**
         * @return Sequence that contains attributes from the.
         */
        public int getNumberOfInstances() { 
          return this.numberOfInstances == null ? 0 : this.numberOfInstances.getValue();
        }

        /**
         * @param value Sequence that contains attributes from the.
         */
        public ImagingStudySeriesComponent setNumberOfInstances(int value) { 
            if (this.numberOfInstances == null)
              this.numberOfInstances = new IntegerType();
            this.numberOfInstances.setValue(value);
          return this;
        }

        /**
         * @return {@link #availability} (Availability of series (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
         */
        public Enumeration<InstanceAvailability> getAvailabilityElement() { 
          if (this.availability == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.availability");
            else if (Configuration.doAutoCreate())
              this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory()); // bb
          return this.availability;
        }

        public boolean hasAvailabilityElement() { 
          return this.availability != null && !this.availability.isEmpty();
        }

        public boolean hasAvailability() { 
          return this.availability != null && !this.availability.isEmpty();
        }

        /**
         * @param value {@link #availability} (Availability of series (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
         */
        public ImagingStudySeriesComponent setAvailabilityElement(Enumeration<InstanceAvailability> value) { 
          this.availability = value;
          return this;
        }

        /**
         * @return Availability of series (online, offline or nearline).
         */
        public InstanceAvailability getAvailability() { 
          return this.availability == null ? null : this.availability.getValue();
        }

        /**
         * @param value Availability of series (online, offline or nearline).
         */
        public ImagingStudySeriesComponent setAvailability(InstanceAvailability value) { 
          if (value == null)
            this.availability = null;
          else {
            if (this.availability == null)
              this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory());
            this.availability.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #url} (WADO-RS URI where Series is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (WADO-RS URI where Series is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public ImagingStudySeriesComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS URI where Series is available.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS URI where Series is available.
         */
        public ImagingStudySeriesComponent setUrl(String value) { 
          if (Utilities.noString(value))
            this.url = null;
          else {
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.)
         */
        public Coding getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new Coding(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.)
         */
        public ImagingStudySeriesComponent setBodySite(Coding value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #dateTime} (The date when the series was started.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DateTimeType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new DateTimeType(); // bb
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The date when the series was started.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public ImagingStudySeriesComponent setDateTimeElement(DateTimeType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The date when the series was started.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The date when the series was started.
         */
        public ImagingStudySeriesComponent setDateTime(Date value) { 
          if (value == null)
            this.dateTime = null;
          else {
            if (this.dateTime == null)
              this.dateTime = new DateTimeType();
            this.dateTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #instance} (A single image taken from a patient.)
         */
        public List<ImagingStudySeriesInstanceComponent> getInstance() { 
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          return this.instance;
        }

        public boolean hasInstance() { 
          if (this.instance == null)
            return false;
          for (ImagingStudySeriesInstanceComponent item : this.instance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #instance} (A single image taken from a patient.)
         */
    // syntactic sugar
        public ImagingStudySeriesInstanceComponent addInstance() { //3
          ImagingStudySeriesInstanceComponent t = new ImagingStudySeriesInstanceComponent();
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          this.instance.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "integer", "The Numeric identifier of this series in the study.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("modality", "code", "The modality of this series sequence.", 0, java.lang.Integer.MAX_VALUE, modality));
          childrenList.add(new Property("uid", "oid", "Formal identifier for this series.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("description", "string", "A description of the series.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("numberOfInstances", "integer", "Sequence that contains attributes from the.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
          childrenList.add(new Property("availability", "code", "Availability of series (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
          childrenList.add(new Property("url", "uri", "WADO-RS URI where Series is available.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("bodySite", "Coding", "Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("dateTime", "dateTime", "The date when the series was started.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("instance", "", "A single image taken from a patient.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      public ImagingStudySeriesComponent copy() {
        ImagingStudySeriesComponent dst = new ImagingStudySeriesComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.modality = modality == null ? null : modality.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.description = description == null ? null : description.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        dst.availability = availability == null ? null : availability.copy();
        dst.url = url == null ? null : url.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        if (instance != null) {
          dst.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          for (ImagingStudySeriesInstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudySeriesComponent))
          return false;
        ImagingStudySeriesComponent o = (ImagingStudySeriesComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(modality, o.modality, true) && compareDeep(uid, o.uid, true)
           && compareDeep(description, o.description, true) && compareDeep(numberOfInstances, o.numberOfInstances, true)
           && compareDeep(availability, o.availability, true) && compareDeep(url, o.url, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(dateTime, o.dateTime, true) && compareDeep(instance, o.instance, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesComponent))
          return false;
        ImagingStudySeriesComponent o = (ImagingStudySeriesComponent) other;
        return compareValues(number, o.number, true) && compareValues(modality, o.modality, true) && compareValues(uid, o.uid, true)
           && compareValues(description, o.description, true) && compareValues(numberOfInstances, o.numberOfInstances, true)
           && compareValues(availability, o.availability, true) && compareValues(url, o.url, true) && compareValues(dateTime, o.dateTime, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (modality == null || modality.isEmpty())
           && (uid == null || uid.isEmpty()) && (description == null || description.isEmpty()) && (numberOfInstances == null || numberOfInstances.isEmpty())
           && (availability == null || availability.isEmpty()) && (url == null || url.isEmpty()) && (bodySite == null || bodySite.isEmpty())
           && (dateTime == null || dateTime.isEmpty()) && (instance == null || instance.isEmpty());
      }

  }

    @Block()
    public static class ImagingStudySeriesInstanceComponent extends BackboneElement {
        /**
         * The number of this image in the series.
         */
        @Child(name="number", type={IntegerType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The number of this instance in the series (0020,0013)", formalDefinition="The number of this image in the series." )
        protected IntegerType number;

        /**
         * Formal identifier for this image.
         */
        @Child(name="uid", type={OidType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Formal identifier for this instance (0008,0018)", formalDefinition="Formal identifier for this image." )
        protected OidType uid;

        /**
         * DICOM Image type.
         */
        @Child(name="sopclass", type={OidType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="DICOM class type (0008,0016)", formalDefinition="DICOM Image type." )
        protected OidType sopclass;

        /**
         * The type of the instance.
         */
        @Child(name="type", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Type of instance (image etc) (0004,1430)", formalDefinition="The type of the instance." )
        protected StringType type;

        /**
         * The description of the instance.
         */
        @Child(name="title", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008)", formalDefinition="The description of the instance." )
        protected StringType title;

        /**
         * WADO-RS url where image is available.
         */
        @Child(name="url", type={UriType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="WADO-RS service where instance is available  (0008,1199 > 0008,1190)", formalDefinition="WADO-RS url where image is available." )
        protected UriType url;

        /**
         * A FHIR resource with content for this instance.
         */
        @Child(name="attachment", type={}, order=7, min=0, max=1)
        @Description(shortDefinition="Content for this instance", formalDefinition="A FHIR resource with content for this instance." )
        protected Reference attachment;

        /**
         * The actual object that is the target of the reference (A FHIR resource with content for this instance.)
         */
        protected Resource attachmentTarget;

        private static final long serialVersionUID = -2008450480L;

      public ImagingStudySeriesInstanceComponent() {
        super();
      }

      public ImagingStudySeriesInstanceComponent(OidType uid, OidType sopclass) {
        super();
        this.uid = uid;
        this.sopclass = sopclass;
      }

        /**
         * @return {@link #number} (The number of this image in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public IntegerType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new IntegerType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The number of this image in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setNumberElement(IntegerType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The number of this image in the series.
         */
        public int getNumber() { 
          return this.number == null ? 0 : this.number.getValue();
        }

        /**
         * @param value The number of this image in the series.
         */
        public ImagingStudySeriesInstanceComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new IntegerType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #uid} (Formal identifier for this image.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType(); // bb
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Formal identifier for this image.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Formal identifier for this image.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Formal identifier for this image.
         */
        public ImagingStudySeriesInstanceComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #sopclass} (DICOM Image type.). This is the underlying object with id, value and extensions. The accessor "getSopclass" gives direct access to the value
         */
        public OidType getSopclassElement() { 
          if (this.sopclass == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.sopclass");
            else if (Configuration.doAutoCreate())
              this.sopclass = new OidType(); // bb
          return this.sopclass;
        }

        public boolean hasSopclassElement() { 
          return this.sopclass != null && !this.sopclass.isEmpty();
        }

        public boolean hasSopclass() { 
          return this.sopclass != null && !this.sopclass.isEmpty();
        }

        /**
         * @param value {@link #sopclass} (DICOM Image type.). This is the underlying object with id, value and extensions. The accessor "getSopclass" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setSopclassElement(OidType value) { 
          this.sopclass = value;
          return this;
        }

        /**
         * @return DICOM Image type.
         */
        public String getSopclass() { 
          return this.sopclass == null ? null : this.sopclass.getValue();
        }

        /**
         * @param value DICOM Image type.
         */
        public ImagingStudySeriesInstanceComponent setSopclass(String value) { 
            if (this.sopclass == null)
              this.sopclass = new OidType();
            this.sopclass.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of the instance.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the instance.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the instance.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the instance.
         */
        public ImagingStudySeriesInstanceComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #title} (The description of the instance.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.title");
            else if (Configuration.doAutoCreate())
              this.title = new StringType(); // bb
          return this.title;
        }

        public boolean hasTitleElement() { 
          return this.title != null && !this.title.isEmpty();
        }

        public boolean hasTitle() { 
          return this.title != null && !this.title.isEmpty();
        }

        /**
         * @param value {@link #title} (The description of the instance.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The description of the instance.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The description of the instance.
         */
        public ImagingStudySeriesInstanceComponent setTitle(String value) { 
          if (Utilities.noString(value))
            this.title = null;
          else {
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #url} (WADO-RS url where image is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (WADO-RS url where image is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS url where image is available.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS url where image is available.
         */
        public ImagingStudySeriesInstanceComponent setUrl(String value) { 
          if (Utilities.noString(value))
            this.url = null;
          else {
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #attachment} (A FHIR resource with content for this instance.)
         */
        public Reference getAttachment() { 
          if (this.attachment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.attachment");
            else if (Configuration.doAutoCreate())
              this.attachment = new Reference(); // cc
          return this.attachment;
        }

        public boolean hasAttachment() { 
          return this.attachment != null && !this.attachment.isEmpty();
        }

        /**
         * @param value {@link #attachment} (A FHIR resource with content for this instance.)
         */
        public ImagingStudySeriesInstanceComponent setAttachment(Reference value) { 
          this.attachment = value;
          return this;
        }

        /**
         * @return {@link #attachment} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A FHIR resource with content for this instance.)
         */
        public Resource getAttachmentTarget() { 
          return this.attachmentTarget;
        }

        /**
         * @param value {@link #attachment} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A FHIR resource with content for this instance.)
         */
        public ImagingStudySeriesInstanceComponent setAttachmentTarget(Resource value) { 
          this.attachmentTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "integer", "The number of this image in the series.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("uid", "oid", "Formal identifier for this image.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("sopclass", "oid", "DICOM Image type.", 0, java.lang.Integer.MAX_VALUE, sopclass));
          childrenList.add(new Property("type", "string", "The type of the instance.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("title", "string", "The description of the instance.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("url", "uri", "WADO-RS url where image is available.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("attachment", "Reference(Any)", "A FHIR resource with content for this instance.", 0, java.lang.Integer.MAX_VALUE, attachment));
        }

      public ImagingStudySeriesInstanceComponent copy() {
        ImagingStudySeriesInstanceComponent dst = new ImagingStudySeriesInstanceComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.sopclass = sopclass == null ? null : sopclass.copy();
        dst.type = type == null ? null : type.copy();
        dst.title = title == null ? null : title.copy();
        dst.url = url == null ? null : url.copy();
        dst.attachment = attachment == null ? null : attachment.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(uid, o.uid, true) && compareDeep(sopclass, o.sopclass, true)
           && compareDeep(type, o.type, true) && compareDeep(title, o.title, true) && compareDeep(url, o.url, true)
           && compareDeep(attachment, o.attachment, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareValues(number, o.number, true) && compareValues(uid, o.uid, true) && compareValues(sopclass, o.sopclass, true)
           && compareValues(type, o.type, true) && compareValues(title, o.title, true) && compareValues(url, o.url, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (uid == null || uid.isEmpty())
           && (sopclass == null || sopclass.isEmpty()) && (type == null || type.isEmpty()) && (title == null || title.isEmpty())
           && (url == null || url.isEmpty()) && (attachment == null || attachment.isEmpty());
      }

  }

    /**
     * Date and Time the study started.
     */
    @Child(name="started", type={DateTimeType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="When the study was started (0008,0020)+(0008,0030)", formalDefinition="Date and Time the study started." )
    protected DateTimeType started;

    /**
     * The patient for whom the images are of.
     */
    @Child(name="patient", type={Patient.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Who the images are of", formalDefinition="The patient for whom the images are of." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient for whom the images are of.)
     */
    protected Patient patientTarget;

    /**
     * Formal identifier for the study.
     */
    @Child(name="uid", type={OidType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Formal identifier for the study (0020,000D)", formalDefinition="Formal identifier for the study." )
    protected OidType uid;

    /**
     * Accession Number.
     */
    @Child(name="accession", type={Identifier.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Accession Number (0008,0050)", formalDefinition="Accession Number." )
    protected Identifier accession;

    /**
     * Other identifiers for the study.
     */
    @Child(name="identifier", type={Identifier.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other identifiers for the study (0020,0010)", formalDefinition="Other identifiers for the study." )
    protected List<Identifier> identifier;

    /**
     * A list of the diagnostic orders that resulted in this imaging study being performed.
     */
    @Child(name="order", type={DiagnosticOrder.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order(s) that caused this study to be performed", formalDefinition="A list of the diagnostic orders that resulted in this imaging study being performed." )
    protected List<Reference> order;
    /**
     * The actual objects that are the target of the reference (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    protected List<DiagnosticOrder> orderTarget;


    /**
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).
     */
    @Child(name="modalityList", type={CodeType.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="All series.modality if actual acquisition modalities", formalDefinition="A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)." )
    protected List<Enumeration<ImagingModality>> modalityList;

    /**
     * The requesting/referring physician.
     */
    @Child(name="referrer", type={Practitioner.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Referring physician (0008,0090)", formalDefinition="The requesting/referring physician." )
    protected Reference referrer;

    /**
     * The actual object that is the target of the reference (The requesting/referring physician.)
     */
    protected Practitioner referrerTarget;

    /**
     * Availability of study (online, offline or nearline).
     */
    @Child(name="availability", type={CodeType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)", formalDefinition="Availability of study (online, offline or nearline)." )
    protected Enumeration<InstanceAvailability> availability;

    /**
     * WADO-RS URI where Study is available.
     */
    @Child(name="url", type={UriType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Retrieve URI (0008,1190)", formalDefinition="WADO-RS URI where Study is available." )
    protected UriType url;

    /**
     * Number of Series in Study.
     */
    @Child(name="numberOfSeries", type={IntegerType.class}, order=9, min=1, max=1)
    @Description(shortDefinition="Number of Study Related Series (0020,1206)", formalDefinition="Number of Series in Study." )
    protected IntegerType numberOfSeries;

    /**
     * Number of SOP Instances in Study.
     */
    @Child(name="numberOfInstances", type={IntegerType.class}, order=10, min=1, max=1)
    @Description(shortDefinition="Number of Study Related Instances (0020,1208)", formalDefinition="Number of SOP Instances in Study." )
    protected IntegerType numberOfInstances;

    /**
     * Diagnoses etc provided with request.
     */
    @Child(name="clinicalInformation", type={StringType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="Diagnoses etc with request (0040,1002)", formalDefinition="Diagnoses etc provided with request." )
    protected StringType clinicalInformation;

    /**
     * Type of procedure performed.
     */
    @Child(name="procedure", type={Coding.class}, order=12, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Type of procedure performed (0008,1032)", formalDefinition="Type of procedure performed." )
    protected List<Coding> procedure;

    /**
     * Who read study and interpreted the images.
     */
    @Child(name="interpreter", type={Practitioner.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Who interpreted images (0008,1060)", formalDefinition="Who read study and interpreted the images." )
    protected Reference interpreter;

    /**
     * The actual object that is the target of the reference (Who read study and interpreted the images.)
     */
    protected Practitioner interpreterTarget;

    /**
     * Institution-generated description or classification of the Study (component) performed.
     */
    @Child(name="description", type={StringType.class}, order=14, min=0, max=1)
    @Description(shortDefinition="Institution-generated description (0008,1030)", formalDefinition="Institution-generated description or classification of the Study (component) performed." )
    protected StringType description;

    /**
     * Each study has one or more series of image instances.
     */
    @Child(name="series", type={}, order=15, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Each study has one or more series of instances", formalDefinition="Each study has one or more series of image instances." )
    protected List<ImagingStudySeriesComponent> series;

    private static final long serialVersionUID = 712301092L;

    public ImagingStudy() {
      super();
    }

    public ImagingStudy(Reference patient, OidType uid, IntegerType numberOfSeries, IntegerType numberOfInstances) {
      super();
      this.patient = patient;
      this.uid = uid;
      this.numberOfSeries = numberOfSeries;
      this.numberOfInstances = numberOfInstances;
    }

    /**
     * @return {@link #started} (Date and Time the study started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
     */
    public DateTimeType getStartedElement() { 
      if (this.started == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.started");
        else if (Configuration.doAutoCreate())
          this.started = new DateTimeType(); // bb
      return this.started;
    }

    public boolean hasStartedElement() { 
      return this.started != null && !this.started.isEmpty();
    }

    public boolean hasStarted() { 
      return this.started != null && !this.started.isEmpty();
    }

    /**
     * @param value {@link #started} (Date and Time the study started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
     */
    public ImagingStudy setStartedElement(DateTimeType value) { 
      this.started = value;
      return this;
    }

    /**
     * @return Date and Time the study started.
     */
    public Date getStarted() { 
      return this.started == null ? null : this.started.getValue();
    }

    /**
     * @param value Date and Time the study started.
     */
    public ImagingStudy setStarted(Date value) { 
      if (value == null)
        this.started = null;
      else {
        if (this.started == null)
          this.started = new DateTimeType();
        this.started.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (The patient for whom the images are of.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient for whom the images are of.)
     */
    public ImagingStudy setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient for whom the images are of.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient for whom the images are of.)
     */
    public ImagingStudy setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #uid} (Formal identifier for the study.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public OidType getUidElement() { 
      if (this.uid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.uid");
        else if (Configuration.doAutoCreate())
          this.uid = new OidType(); // bb
      return this.uid;
    }

    public boolean hasUidElement() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    public boolean hasUid() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    /**
     * @param value {@link #uid} (Formal identifier for the study.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public ImagingStudy setUidElement(OidType value) { 
      this.uid = value;
      return this;
    }

    /**
     * @return Formal identifier for the study.
     */
    public String getUid() { 
      return this.uid == null ? null : this.uid.getValue();
    }

    /**
     * @param value Formal identifier for the study.
     */
    public ImagingStudy setUid(String value) { 
        if (this.uid == null)
          this.uid = new OidType();
        this.uid.setValue(value);
      return this;
    }

    /**
     * @return {@link #accession} (Accession Number.)
     */
    public Identifier getAccession() { 
      if (this.accession == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.accession");
        else if (Configuration.doAutoCreate())
          this.accession = new Identifier(); // cc
      return this.accession;
    }

    public boolean hasAccession() { 
      return this.accession != null && !this.accession.isEmpty();
    }

    /**
     * @param value {@link #accession} (Accession Number.)
     */
    public ImagingStudy setAccession(Identifier value) { 
      this.accession = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Other identifiers for the study.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Other identifiers for the study.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #order} (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public List<Reference> getOrder() { 
      if (this.order == null)
        this.order = new ArrayList<Reference>();
      return this.order;
    }

    public boolean hasOrder() { 
      if (this.order == null)
        return false;
      for (Reference item : this.order)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #order} (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    // syntactic sugar
    public Reference addOrder() { //3
      Reference t = new Reference();
      if (this.order == null)
        this.order = new ArrayList<Reference>();
      this.order.add(t);
      return t;
    }

    /**
     * @return {@link #order} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public List<DiagnosticOrder> getOrderTarget() { 
      if (this.orderTarget == null)
        this.orderTarget = new ArrayList<DiagnosticOrder>();
      return this.orderTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #order} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public DiagnosticOrder addOrderTarget() { 
      DiagnosticOrder r = new DiagnosticOrder();
      if (this.orderTarget == null)
        this.orderTarget = new ArrayList<DiagnosticOrder>();
      this.orderTarget.add(r);
      return r;
    }

    /**
     * @return {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    public List<Enumeration<ImagingModality>> getModalityList() { 
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Enumeration<ImagingModality>>();
      return this.modalityList;
    }

    public boolean hasModalityList() { 
      if (this.modalityList == null)
        return false;
      for (Enumeration<ImagingModality> item : this.modalityList)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    // syntactic sugar
    public Enumeration<ImagingModality> addModalityListElement() {//2 
      Enumeration<ImagingModality> t = new Enumeration<ImagingModality>(new ImagingModalityEnumFactory());
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Enumeration<ImagingModality>>();
      this.modalityList.add(t);
      return t;
    }

    /**
     * @param value {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    public ImagingStudy addModalityList(ImagingModality value) { //1
      Enumeration<ImagingModality> t = new Enumeration<ImagingModality>(new ImagingModalityEnumFactory());
      t.setValue(value);
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Enumeration<ImagingModality>>();
      this.modalityList.add(t);
      return this;
    }

    /**
     * @param value {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    public boolean hasModalityList(ImagingModality value) { 
      if (this.modalityList == null)
        return false;
      for (Enumeration<ImagingModality> v : this.modalityList)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #referrer} (The requesting/referring physician.)
     */
    public Reference getReferrer() { 
      if (this.referrer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.referrer");
        else if (Configuration.doAutoCreate())
          this.referrer = new Reference(); // cc
      return this.referrer;
    }

    public boolean hasReferrer() { 
      return this.referrer != null && !this.referrer.isEmpty();
    }

    /**
     * @param value {@link #referrer} (The requesting/referring physician.)
     */
    public ImagingStudy setReferrer(Reference value) { 
      this.referrer = value;
      return this;
    }

    /**
     * @return {@link #referrer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The requesting/referring physician.)
     */
    public Practitioner getReferrerTarget() { 
      if (this.referrerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.referrer");
        else if (Configuration.doAutoCreate())
          this.referrerTarget = new Practitioner(); // aa
      return this.referrerTarget;
    }

    /**
     * @param value {@link #referrer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The requesting/referring physician.)
     */
    public ImagingStudy setReferrerTarget(Practitioner value) { 
      this.referrerTarget = value;
      return this;
    }

    /**
     * @return {@link #availability} (Availability of study (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
     */
    public Enumeration<InstanceAvailability> getAvailabilityElement() { 
      if (this.availability == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.availability");
        else if (Configuration.doAutoCreate())
          this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory()); // bb
      return this.availability;
    }

    public boolean hasAvailabilityElement() { 
      return this.availability != null && !this.availability.isEmpty();
    }

    public boolean hasAvailability() { 
      return this.availability != null && !this.availability.isEmpty();
    }

    /**
     * @param value {@link #availability} (Availability of study (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
     */
    public ImagingStudy setAvailabilityElement(Enumeration<InstanceAvailability> value) { 
      this.availability = value;
      return this;
    }

    /**
     * @return Availability of study (online, offline or nearline).
     */
    public InstanceAvailability getAvailability() { 
      return this.availability == null ? null : this.availability.getValue();
    }

    /**
     * @param value Availability of study (online, offline or nearline).
     */
    public ImagingStudy setAvailability(InstanceAvailability value) { 
      if (value == null)
        this.availability = null;
      else {
        if (this.availability == null)
          this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory());
        this.availability.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #url} (WADO-RS URI where Study is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (WADO-RS URI where Study is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImagingStudy setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return WADO-RS URI where Study is available.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value WADO-RS URI where Study is available.
     */
    public ImagingStudy setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #numberOfSeries} (Number of Series in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSeries" gives direct access to the value
     */
    public IntegerType getNumberOfSeriesElement() { 
      if (this.numberOfSeries == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.numberOfSeries");
        else if (Configuration.doAutoCreate())
          this.numberOfSeries = new IntegerType(); // bb
      return this.numberOfSeries;
    }

    public boolean hasNumberOfSeriesElement() { 
      return this.numberOfSeries != null && !this.numberOfSeries.isEmpty();
    }

    public boolean hasNumberOfSeries() { 
      return this.numberOfSeries != null && !this.numberOfSeries.isEmpty();
    }

    /**
     * @param value {@link #numberOfSeries} (Number of Series in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSeries" gives direct access to the value
     */
    public ImagingStudy setNumberOfSeriesElement(IntegerType value) { 
      this.numberOfSeries = value;
      return this;
    }

    /**
     * @return Number of Series in Study.
     */
    public int getNumberOfSeries() { 
      return this.numberOfSeries == null ? 0 : this.numberOfSeries.getValue();
    }

    /**
     * @param value Number of Series in Study.
     */
    public ImagingStudy setNumberOfSeries(int value) { 
        if (this.numberOfSeries == null)
          this.numberOfSeries = new IntegerType();
        this.numberOfSeries.setValue(value);
      return this;
    }

    /**
     * @return {@link #numberOfInstances} (Number of SOP Instances in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
     */
    public IntegerType getNumberOfInstancesElement() { 
      if (this.numberOfInstances == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.numberOfInstances");
        else if (Configuration.doAutoCreate())
          this.numberOfInstances = new IntegerType(); // bb
      return this.numberOfInstances;
    }

    public boolean hasNumberOfInstancesElement() { 
      return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
    }

    public boolean hasNumberOfInstances() { 
      return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
    }

    /**
     * @param value {@link #numberOfInstances} (Number of SOP Instances in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
     */
    public ImagingStudy setNumberOfInstancesElement(IntegerType value) { 
      this.numberOfInstances = value;
      return this;
    }

    /**
     * @return Number of SOP Instances in Study.
     */
    public int getNumberOfInstances() { 
      return this.numberOfInstances == null ? 0 : this.numberOfInstances.getValue();
    }

    /**
     * @param value Number of SOP Instances in Study.
     */
    public ImagingStudy setNumberOfInstances(int value) { 
        if (this.numberOfInstances == null)
          this.numberOfInstances = new IntegerType();
        this.numberOfInstances.setValue(value);
      return this;
    }

    /**
     * @return {@link #clinicalInformation} (Diagnoses etc provided with request.). This is the underlying object with id, value and extensions. The accessor "getClinicalInformation" gives direct access to the value
     */
    public StringType getClinicalInformationElement() { 
      if (this.clinicalInformation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.clinicalInformation");
        else if (Configuration.doAutoCreate())
          this.clinicalInformation = new StringType(); // bb
      return this.clinicalInformation;
    }

    public boolean hasClinicalInformationElement() { 
      return this.clinicalInformation != null && !this.clinicalInformation.isEmpty();
    }

    public boolean hasClinicalInformation() { 
      return this.clinicalInformation != null && !this.clinicalInformation.isEmpty();
    }

    /**
     * @param value {@link #clinicalInformation} (Diagnoses etc provided with request.). This is the underlying object with id, value and extensions. The accessor "getClinicalInformation" gives direct access to the value
     */
    public ImagingStudy setClinicalInformationElement(StringType value) { 
      this.clinicalInformation = value;
      return this;
    }

    /**
     * @return Diagnoses etc provided with request.
     */
    public String getClinicalInformation() { 
      return this.clinicalInformation == null ? null : this.clinicalInformation.getValue();
    }

    /**
     * @param value Diagnoses etc provided with request.
     */
    public ImagingStudy setClinicalInformation(String value) { 
      if (Utilities.noString(value))
        this.clinicalInformation = null;
      else {
        if (this.clinicalInformation == null)
          this.clinicalInformation = new StringType();
        this.clinicalInformation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #procedure} (Type of procedure performed.)
     */
    public List<Coding> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<Coding>();
      return this.procedure;
    }

    public boolean hasProcedure() { 
      if (this.procedure == null)
        return false;
      for (Coding item : this.procedure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #procedure} (Type of procedure performed.)
     */
    // syntactic sugar
    public Coding addProcedure() { //3
      Coding t = new Coding();
      if (this.procedure == null)
        this.procedure = new ArrayList<Coding>();
      this.procedure.add(t);
      return t;
    }

    /**
     * @return {@link #interpreter} (Who read study and interpreted the images.)
     */
    public Reference getInterpreter() { 
      if (this.interpreter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.interpreter");
        else if (Configuration.doAutoCreate())
          this.interpreter = new Reference(); // cc
      return this.interpreter;
    }

    public boolean hasInterpreter() { 
      return this.interpreter != null && !this.interpreter.isEmpty();
    }

    /**
     * @param value {@link #interpreter} (Who read study and interpreted the images.)
     */
    public ImagingStudy setInterpreter(Reference value) { 
      this.interpreter = value;
      return this;
    }

    /**
     * @return {@link #interpreter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who read study and interpreted the images.)
     */
    public Practitioner getInterpreterTarget() { 
      if (this.interpreterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.interpreter");
        else if (Configuration.doAutoCreate())
          this.interpreterTarget = new Practitioner(); // aa
      return this.interpreterTarget;
    }

    /**
     * @param value {@link #interpreter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who read study and interpreted the images.)
     */
    public ImagingStudy setInterpreterTarget(Practitioner value) { 
      this.interpreterTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Institution-generated description or classification of the Study (component) performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Institution-generated description or classification of the Study (component) performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingStudy setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Institution-generated description or classification of the Study (component) performed.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Institution-generated description or classification of the Study (component) performed.
     */
    public ImagingStudy setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #series} (Each study has one or more series of image instances.)
     */
    public List<ImagingStudySeriesComponent> getSeries() { 
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      return this.series;
    }

    public boolean hasSeries() { 
      if (this.series == null)
        return false;
      for (ImagingStudySeriesComponent item : this.series)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #series} (Each study has one or more series of image instances.)
     */
    // syntactic sugar
    public ImagingStudySeriesComponent addSeries() { //3
      ImagingStudySeriesComponent t = new ImagingStudySeriesComponent();
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      this.series.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("started", "dateTime", "Date and Time the study started.", 0, java.lang.Integer.MAX_VALUE, started));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient for whom the images are of.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("uid", "oid", "Formal identifier for the study.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("accession", "Identifier", "Accession Number.", 0, java.lang.Integer.MAX_VALUE, accession));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers for the study.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("order", "Reference(DiagnosticOrder)", "A list of the diagnostic orders that resulted in this imaging study being performed.", 0, java.lang.Integer.MAX_VALUE, order));
        childrenList.add(new Property("modalityList", "code", "A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).", 0, java.lang.Integer.MAX_VALUE, modalityList));
        childrenList.add(new Property("referrer", "Reference(Practitioner)", "The requesting/referring physician.", 0, java.lang.Integer.MAX_VALUE, referrer));
        childrenList.add(new Property("availability", "code", "Availability of study (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
        childrenList.add(new Property("url", "uri", "WADO-RS URI where Study is available.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("numberOfSeries", "integer", "Number of Series in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfSeries));
        childrenList.add(new Property("numberOfInstances", "integer", "Number of SOP Instances in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
        childrenList.add(new Property("clinicalInformation", "string", "Diagnoses etc provided with request.", 0, java.lang.Integer.MAX_VALUE, clinicalInformation));
        childrenList.add(new Property("procedure", "Coding", "Type of procedure performed.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("interpreter", "Reference(Practitioner)", "Who read study and interpreted the images.", 0, java.lang.Integer.MAX_VALUE, interpreter));
        childrenList.add(new Property("description", "string", "Institution-generated description or classification of the Study (component) performed.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("series", "", "Each study has one or more series of image instances.", 0, java.lang.Integer.MAX_VALUE, series));
      }

      public ImagingStudy copy() {
        ImagingStudy dst = new ImagingStudy();
        copyValues(dst);
        dst.started = started == null ? null : started.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.accession = accession == null ? null : accession.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (order != null) {
          dst.order = new ArrayList<Reference>();
          for (Reference i : order)
            dst.order.add(i.copy());
        };
        if (modalityList != null) {
          dst.modalityList = new ArrayList<Enumeration<ImagingModality>>();
          for (Enumeration<ImagingModality> i : modalityList)
            dst.modalityList.add(i.copy());
        };
        dst.referrer = referrer == null ? null : referrer.copy();
        dst.availability = availability == null ? null : availability.copy();
        dst.url = url == null ? null : url.copy();
        dst.numberOfSeries = numberOfSeries == null ? null : numberOfSeries.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        dst.clinicalInformation = clinicalInformation == null ? null : clinicalInformation.copy();
        if (procedure != null) {
          dst.procedure = new ArrayList<Coding>();
          for (Coding i : procedure)
            dst.procedure.add(i.copy());
        };
        dst.interpreter = interpreter == null ? null : interpreter.copy();
        dst.description = description == null ? null : description.copy();
        if (series != null) {
          dst.series = new ArrayList<ImagingStudySeriesComponent>();
          for (ImagingStudySeriesComponent i : series)
            dst.series.add(i.copy());
        };
        return dst;
      }

      protected ImagingStudy typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudy))
          return false;
        ImagingStudy o = (ImagingStudy) other;
        return compareDeep(started, o.started, true) && compareDeep(patient, o.patient, true) && compareDeep(uid, o.uid, true)
           && compareDeep(accession, o.accession, true) && compareDeep(identifier, o.identifier, true) && compareDeep(order, o.order, true)
           && compareDeep(modalityList, o.modalityList, true) && compareDeep(referrer, o.referrer, true) && compareDeep(availability, o.availability, true)
           && compareDeep(url, o.url, true) && compareDeep(numberOfSeries, o.numberOfSeries, true) && compareDeep(numberOfInstances, o.numberOfInstances, true)
           && compareDeep(clinicalInformation, o.clinicalInformation, true) && compareDeep(procedure, o.procedure, true)
           && compareDeep(interpreter, o.interpreter, true) && compareDeep(description, o.description, true)
           && compareDeep(series, o.series, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudy))
          return false;
        ImagingStudy o = (ImagingStudy) other;
        return compareValues(started, o.started, true) && compareValues(uid, o.uid, true) && compareValues(modalityList, o.modalityList, true)
           && compareValues(availability, o.availability, true) && compareValues(url, o.url, true) && compareValues(numberOfSeries, o.numberOfSeries, true)
           && compareValues(numberOfInstances, o.numberOfInstances, true) && compareValues(clinicalInformation, o.clinicalInformation, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (started == null || started.isEmpty()) && (patient == null || patient.isEmpty())
           && (uid == null || uid.isEmpty()) && (accession == null || accession.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (order == null || order.isEmpty()) && (modalityList == null || modalityList.isEmpty())
           && (referrer == null || referrer.isEmpty()) && (availability == null || availability.isEmpty())
           && (url == null || url.isEmpty()) && (numberOfSeries == null || numberOfSeries.isEmpty())
           && (numberOfInstances == null || numberOfInstances.isEmpty()) && (clinicalInformation == null || clinicalInformation.isEmpty())
           && (procedure == null || procedure.isEmpty()) && (interpreter == null || interpreter.isEmpty())
           && (description == null || description.isEmpty()) && (series == null || series.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingStudy;
   }

  @SearchParamDefinition(name="uid", path="ImagingStudy.series.instance.uid", description="Formal identifier for this instance (0008,0018)", type="token" )
  public static final String SP_UID = "uid";
  @SearchParamDefinition(name="series", path="ImagingStudy.series.uid", description="The series id for the image", type="token" )
  public static final String SP_SERIES = "series";
  @SearchParamDefinition(name="patient", path="ImagingStudy.patient", description="Who the study is about", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="bodysite", path="ImagingStudy.series.bodySite", description="Body part examined (Map from 0018,0015)", type="token" )
  public static final String SP_BODYSITE = "bodysite";
  @SearchParamDefinition(name="accession", path="ImagingStudy.accession", description="The accession id for the image", type="token" )
  public static final String SP_ACCESSION = "accession";
  @SearchParamDefinition(name="study", path="ImagingStudy.uid", description="The study id for the image", type="token" )
  public static final String SP_STUDY = "study";
  @SearchParamDefinition(name="modality", path="ImagingStudy.series.modality", description="The modality of the image", type="token" )
  public static final String SP_MODALITY = "modality";
  @SearchParamDefinition(name="started", path="ImagingStudy.started", description="When the study was started", type="date" )
  public static final String SP_STARTED = "started";
  @SearchParamDefinition(name="dicom-class", path="ImagingStudy.series.instance.sopclass", description="DICOM class type (0008,0016)", type="token" )
  public static final String SP_DICOMCLASS = "dicom-class";
  @SearchParamDefinition(name="size", path="", description="The size of the image in MB - may include > or < in the value", type="number" )
  public static final String SP_SIZE = "size";

}

