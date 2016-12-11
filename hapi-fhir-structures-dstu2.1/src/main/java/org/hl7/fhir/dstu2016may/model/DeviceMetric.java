package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Describes a measurement, calculation or setting capability of a medical device.
 */
@ResourceDef(name="DeviceMetric", profile="http://hl7.org/fhir/Profile/DeviceMetric")
public class DeviceMetric extends DomainResource {

    public enum DeviceMetricOperationalStatus {
        /**
         * The DeviceMetric is operating and will generate DeviceObservations.
         */
        ON, 
        /**
         * The DeviceMetric is not operating.
         */
        OFF, 
        /**
         * The DeviceMetric is operating, but will not generate any DeviceObservations.
         */
        STANDBY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceMetricOperationalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("on".equals(codeString))
          return ON;
        if ("off".equals(codeString))
          return OFF;
        if ("standby".equals(codeString))
          return STANDBY;
        throw new FHIRException("Unknown DeviceMetricOperationalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ON: return "on";
            case OFF: return "off";
            case STANDBY: return "standby";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ON: return "http://hl7.org/fhir/metric-operational-status";
            case OFF: return "http://hl7.org/fhir/metric-operational-status";
            case STANDBY: return "http://hl7.org/fhir/metric-operational-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ON: return "The DeviceMetric is operating and will generate DeviceObservations.";
            case OFF: return "The DeviceMetric is not operating.";
            case STANDBY: return "The DeviceMetric is operating, but will not generate any DeviceObservations.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ON: return "On";
            case OFF: return "Off";
            case STANDBY: return "Standby";
            default: return "?";
          }
        }
    }

  public static class DeviceMetricOperationalStatusEnumFactory implements EnumFactory<DeviceMetricOperationalStatus> {
    public DeviceMetricOperationalStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("on".equals(codeString))
          return DeviceMetricOperationalStatus.ON;
        if ("off".equals(codeString))
          return DeviceMetricOperationalStatus.OFF;
        if ("standby".equals(codeString))
          return DeviceMetricOperationalStatus.STANDBY;
        throw new IllegalArgumentException("Unknown DeviceMetricOperationalStatus code '"+codeString+"'");
        }
        public Enumeration<DeviceMetricOperationalStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("on".equals(codeString))
          return new Enumeration<DeviceMetricOperationalStatus>(this, DeviceMetricOperationalStatus.ON);
        if ("off".equals(codeString))
          return new Enumeration<DeviceMetricOperationalStatus>(this, DeviceMetricOperationalStatus.OFF);
        if ("standby".equals(codeString))
          return new Enumeration<DeviceMetricOperationalStatus>(this, DeviceMetricOperationalStatus.STANDBY);
        throw new FHIRException("Unknown DeviceMetricOperationalStatus code '"+codeString+"'");
        }
    public String toCode(DeviceMetricOperationalStatus code) {
      if (code == DeviceMetricOperationalStatus.ON)
        return "on";
      if (code == DeviceMetricOperationalStatus.OFF)
        return "off";
      if (code == DeviceMetricOperationalStatus.STANDBY)
        return "standby";
      return "?";
      }
    public String toSystem(DeviceMetricOperationalStatus code) {
      return code.getSystem();
      }
    }

    public enum DeviceMetricColor {
        /**
         * Color for representation - black.
         */
        BLACK, 
        /**
         * Color for representation - red.
         */
        RED, 
        /**
         * Color for representation - green.
         */
        GREEN, 
        /**
         * Color for representation - yellow.
         */
        YELLOW, 
        /**
         * Color for representation - blue.
         */
        BLUE, 
        /**
         * Color for representation - magenta.
         */
        MAGENTA, 
        /**
         * Color for representation - cyan.
         */
        CYAN, 
        /**
         * Color for representation - white.
         */
        WHITE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceMetricColor fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("black".equals(codeString))
          return BLACK;
        if ("red".equals(codeString))
          return RED;
        if ("green".equals(codeString))
          return GREEN;
        if ("yellow".equals(codeString))
          return YELLOW;
        if ("blue".equals(codeString))
          return BLUE;
        if ("magenta".equals(codeString))
          return MAGENTA;
        if ("cyan".equals(codeString))
          return CYAN;
        if ("white".equals(codeString))
          return WHITE;
        throw new FHIRException("Unknown DeviceMetricColor code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BLACK: return "black";
            case RED: return "red";
            case GREEN: return "green";
            case YELLOW: return "yellow";
            case BLUE: return "blue";
            case MAGENTA: return "magenta";
            case CYAN: return "cyan";
            case WHITE: return "white";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BLACK: return "http://hl7.org/fhir/metric-color";
            case RED: return "http://hl7.org/fhir/metric-color";
            case GREEN: return "http://hl7.org/fhir/metric-color";
            case YELLOW: return "http://hl7.org/fhir/metric-color";
            case BLUE: return "http://hl7.org/fhir/metric-color";
            case MAGENTA: return "http://hl7.org/fhir/metric-color";
            case CYAN: return "http://hl7.org/fhir/metric-color";
            case WHITE: return "http://hl7.org/fhir/metric-color";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BLACK: return "Color for representation - black.";
            case RED: return "Color for representation - red.";
            case GREEN: return "Color for representation - green.";
            case YELLOW: return "Color for representation - yellow.";
            case BLUE: return "Color for representation - blue.";
            case MAGENTA: return "Color for representation - magenta.";
            case CYAN: return "Color for representation - cyan.";
            case WHITE: return "Color for representation - white.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BLACK: return "Color Black";
            case RED: return "Color Red";
            case GREEN: return "Color Green";
            case YELLOW: return "Color Yellow";
            case BLUE: return "Color Blue";
            case MAGENTA: return "Color Magenta";
            case CYAN: return "Color Cyan";
            case WHITE: return "Color White";
            default: return "?";
          }
        }
    }

  public static class DeviceMetricColorEnumFactory implements EnumFactory<DeviceMetricColor> {
    public DeviceMetricColor fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("black".equals(codeString))
          return DeviceMetricColor.BLACK;
        if ("red".equals(codeString))
          return DeviceMetricColor.RED;
        if ("green".equals(codeString))
          return DeviceMetricColor.GREEN;
        if ("yellow".equals(codeString))
          return DeviceMetricColor.YELLOW;
        if ("blue".equals(codeString))
          return DeviceMetricColor.BLUE;
        if ("magenta".equals(codeString))
          return DeviceMetricColor.MAGENTA;
        if ("cyan".equals(codeString))
          return DeviceMetricColor.CYAN;
        if ("white".equals(codeString))
          return DeviceMetricColor.WHITE;
        throw new IllegalArgumentException("Unknown DeviceMetricColor code '"+codeString+"'");
        }
        public Enumeration<DeviceMetricColor> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("black".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.BLACK);
        if ("red".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.RED);
        if ("green".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.GREEN);
        if ("yellow".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.YELLOW);
        if ("blue".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.BLUE);
        if ("magenta".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.MAGENTA);
        if ("cyan".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.CYAN);
        if ("white".equals(codeString))
          return new Enumeration<DeviceMetricColor>(this, DeviceMetricColor.WHITE);
        throw new FHIRException("Unknown DeviceMetricColor code '"+codeString+"'");
        }
    public String toCode(DeviceMetricColor code) {
      if (code == DeviceMetricColor.BLACK)
        return "black";
      if (code == DeviceMetricColor.RED)
        return "red";
      if (code == DeviceMetricColor.GREEN)
        return "green";
      if (code == DeviceMetricColor.YELLOW)
        return "yellow";
      if (code == DeviceMetricColor.BLUE)
        return "blue";
      if (code == DeviceMetricColor.MAGENTA)
        return "magenta";
      if (code == DeviceMetricColor.CYAN)
        return "cyan";
      if (code == DeviceMetricColor.WHITE)
        return "white";
      return "?";
      }
    public String toSystem(DeviceMetricColor code) {
      return code.getSystem();
      }
    }

    public enum DeviceMetricCategory {
        /**
         * DeviceObservations generated for this DeviceMetric are measured.
         */
        MEASUREMENT, 
        /**
         * DeviceObservations generated for this DeviceMetric is a setting that will influence the behavior of the Device.
         */
        SETTING, 
        /**
         * DeviceObservations generated for this DeviceMetric are calculated.
         */
        CALCULATION, 
        /**
         * The category of this DeviceMetric is unspecified.
         */
        UNSPECIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceMetricCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("measurement".equals(codeString))
          return MEASUREMENT;
        if ("setting".equals(codeString))
          return SETTING;
        if ("calculation".equals(codeString))
          return CALCULATION;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        throw new FHIRException("Unknown DeviceMetricCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEASUREMENT: return "measurement";
            case SETTING: return "setting";
            case CALCULATION: return "calculation";
            case UNSPECIFIED: return "unspecified";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MEASUREMENT: return "http://hl7.org/fhir/metric-category";
            case SETTING: return "http://hl7.org/fhir/metric-category";
            case CALCULATION: return "http://hl7.org/fhir/metric-category";
            case UNSPECIFIED: return "http://hl7.org/fhir/metric-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MEASUREMENT: return "DeviceObservations generated for this DeviceMetric are measured.";
            case SETTING: return "DeviceObservations generated for this DeviceMetric is a setting that will influence the behavior of the Device.";
            case CALCULATION: return "DeviceObservations generated for this DeviceMetric are calculated.";
            case UNSPECIFIED: return "The category of this DeviceMetric is unspecified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEASUREMENT: return "Measurement";
            case SETTING: return "Setting";
            case CALCULATION: return "Calculation";
            case UNSPECIFIED: return "Unspecified";
            default: return "?";
          }
        }
    }

  public static class DeviceMetricCategoryEnumFactory implements EnumFactory<DeviceMetricCategory> {
    public DeviceMetricCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("measurement".equals(codeString))
          return DeviceMetricCategory.MEASUREMENT;
        if ("setting".equals(codeString))
          return DeviceMetricCategory.SETTING;
        if ("calculation".equals(codeString))
          return DeviceMetricCategory.CALCULATION;
        if ("unspecified".equals(codeString))
          return DeviceMetricCategory.UNSPECIFIED;
        throw new IllegalArgumentException("Unknown DeviceMetricCategory code '"+codeString+"'");
        }
        public Enumeration<DeviceMetricCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("measurement".equals(codeString))
          return new Enumeration<DeviceMetricCategory>(this, DeviceMetricCategory.MEASUREMENT);
        if ("setting".equals(codeString))
          return new Enumeration<DeviceMetricCategory>(this, DeviceMetricCategory.SETTING);
        if ("calculation".equals(codeString))
          return new Enumeration<DeviceMetricCategory>(this, DeviceMetricCategory.CALCULATION);
        if ("unspecified".equals(codeString))
          return new Enumeration<DeviceMetricCategory>(this, DeviceMetricCategory.UNSPECIFIED);
        throw new FHIRException("Unknown DeviceMetricCategory code '"+codeString+"'");
        }
    public String toCode(DeviceMetricCategory code) {
      if (code == DeviceMetricCategory.MEASUREMENT)
        return "measurement";
      if (code == DeviceMetricCategory.SETTING)
        return "setting";
      if (code == DeviceMetricCategory.CALCULATION)
        return "calculation";
      if (code == DeviceMetricCategory.UNSPECIFIED)
        return "unspecified";
      return "?";
      }
    public String toSystem(DeviceMetricCategory code) {
      return code.getSystem();
      }
    }

    public enum DeviceMetricCalibrationType {
        /**
         * TODO
         */
        UNSPECIFIED, 
        /**
         * TODO
         */
        OFFSET, 
        /**
         * TODO
         */
        GAIN, 
        /**
         * TODO
         */
        TWOPOINT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceMetricCalibrationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        if ("offset".equals(codeString))
          return OFFSET;
        if ("gain".equals(codeString))
          return GAIN;
        if ("two-point".equals(codeString))
          return TWOPOINT;
        throw new FHIRException("Unknown DeviceMetricCalibrationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNSPECIFIED: return "unspecified";
            case OFFSET: return "offset";
            case GAIN: return "gain";
            case TWOPOINT: return "two-point";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNSPECIFIED: return "http://hl7.org/fhir/metric-calibration-type";
            case OFFSET: return "http://hl7.org/fhir/metric-calibration-type";
            case GAIN: return "http://hl7.org/fhir/metric-calibration-type";
            case TWOPOINT: return "http://hl7.org/fhir/metric-calibration-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNSPECIFIED: return "TODO";
            case OFFSET: return "TODO";
            case GAIN: return "TODO";
            case TWOPOINT: return "TODO";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNSPECIFIED: return "Unspecified";
            case OFFSET: return "Offset";
            case GAIN: return "Gain";
            case TWOPOINT: return "Two Point";
            default: return "?";
          }
        }
    }

  public static class DeviceMetricCalibrationTypeEnumFactory implements EnumFactory<DeviceMetricCalibrationType> {
    public DeviceMetricCalibrationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return DeviceMetricCalibrationType.UNSPECIFIED;
        if ("offset".equals(codeString))
          return DeviceMetricCalibrationType.OFFSET;
        if ("gain".equals(codeString))
          return DeviceMetricCalibrationType.GAIN;
        if ("two-point".equals(codeString))
          return DeviceMetricCalibrationType.TWOPOINT;
        throw new IllegalArgumentException("Unknown DeviceMetricCalibrationType code '"+codeString+"'");
        }
        public Enumeration<DeviceMetricCalibrationType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("unspecified".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationType>(this, DeviceMetricCalibrationType.UNSPECIFIED);
        if ("offset".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationType>(this, DeviceMetricCalibrationType.OFFSET);
        if ("gain".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationType>(this, DeviceMetricCalibrationType.GAIN);
        if ("two-point".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationType>(this, DeviceMetricCalibrationType.TWOPOINT);
        throw new FHIRException("Unknown DeviceMetricCalibrationType code '"+codeString+"'");
        }
    public String toCode(DeviceMetricCalibrationType code) {
      if (code == DeviceMetricCalibrationType.UNSPECIFIED)
        return "unspecified";
      if (code == DeviceMetricCalibrationType.OFFSET)
        return "offset";
      if (code == DeviceMetricCalibrationType.GAIN)
        return "gain";
      if (code == DeviceMetricCalibrationType.TWOPOINT)
        return "two-point";
      return "?";
      }
    public String toSystem(DeviceMetricCalibrationType code) {
      return code.getSystem();
      }
    }

    public enum DeviceMetricCalibrationState {
        /**
         * The metric has not been calibrated.
         */
        NOTCALIBRATED, 
        /**
         * The metric needs to be calibrated.
         */
        CALIBRATIONREQUIRED, 
        /**
         * The metric has been calibrated.
         */
        CALIBRATED, 
        /**
         * The state of calibration of this metric is unspecified.
         */
        UNSPECIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceMetricCalibrationState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-calibrated".equals(codeString))
          return NOTCALIBRATED;
        if ("calibration-required".equals(codeString))
          return CALIBRATIONREQUIRED;
        if ("calibrated".equals(codeString))
          return CALIBRATED;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        throw new FHIRException("Unknown DeviceMetricCalibrationState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTCALIBRATED: return "not-calibrated";
            case CALIBRATIONREQUIRED: return "calibration-required";
            case CALIBRATED: return "calibrated";
            case UNSPECIFIED: return "unspecified";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTCALIBRATED: return "http://hl7.org/fhir/metric-calibration-state";
            case CALIBRATIONREQUIRED: return "http://hl7.org/fhir/metric-calibration-state";
            case CALIBRATED: return "http://hl7.org/fhir/metric-calibration-state";
            case UNSPECIFIED: return "http://hl7.org/fhir/metric-calibration-state";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTCALIBRATED: return "The metric has not been calibrated.";
            case CALIBRATIONREQUIRED: return "The metric needs to be calibrated.";
            case CALIBRATED: return "The metric has been calibrated.";
            case UNSPECIFIED: return "The state of calibration of this metric is unspecified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTCALIBRATED: return "Not Calibrated";
            case CALIBRATIONREQUIRED: return "Calibration Required";
            case CALIBRATED: return "Calibrated";
            case UNSPECIFIED: return "Unspecified";
            default: return "?";
          }
        }
    }

  public static class DeviceMetricCalibrationStateEnumFactory implements EnumFactory<DeviceMetricCalibrationState> {
    public DeviceMetricCalibrationState fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-calibrated".equals(codeString))
          return DeviceMetricCalibrationState.NOTCALIBRATED;
        if ("calibration-required".equals(codeString))
          return DeviceMetricCalibrationState.CALIBRATIONREQUIRED;
        if ("calibrated".equals(codeString))
          return DeviceMetricCalibrationState.CALIBRATED;
        if ("unspecified".equals(codeString))
          return DeviceMetricCalibrationState.UNSPECIFIED;
        throw new IllegalArgumentException("Unknown DeviceMetricCalibrationState code '"+codeString+"'");
        }
        public Enumeration<DeviceMetricCalibrationState> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-calibrated".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationState>(this, DeviceMetricCalibrationState.NOTCALIBRATED);
        if ("calibration-required".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationState>(this, DeviceMetricCalibrationState.CALIBRATIONREQUIRED);
        if ("calibrated".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationState>(this, DeviceMetricCalibrationState.CALIBRATED);
        if ("unspecified".equals(codeString))
          return new Enumeration<DeviceMetricCalibrationState>(this, DeviceMetricCalibrationState.UNSPECIFIED);
        throw new FHIRException("Unknown DeviceMetricCalibrationState code '"+codeString+"'");
        }
    public String toCode(DeviceMetricCalibrationState code) {
      if (code == DeviceMetricCalibrationState.NOTCALIBRATED)
        return "not-calibrated";
      if (code == DeviceMetricCalibrationState.CALIBRATIONREQUIRED)
        return "calibration-required";
      if (code == DeviceMetricCalibrationState.CALIBRATED)
        return "calibrated";
      if (code == DeviceMetricCalibrationState.UNSPECIFIED)
        return "unspecified";
      return "?";
      }
    public String toSystem(DeviceMetricCalibrationState code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DeviceMetricCalibrationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the type of the calibration method.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="unspecified | offset | gain | two-point", formalDefinition="Describes the type of the calibration method." )
        protected Enumeration<DeviceMetricCalibrationType> type;

        /**
         * Describes the state of the calibration.
         */
        @Child(name = "state", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="not-calibrated | calibration-required | calibrated | unspecified", formalDefinition="Describes the state of the calibration." )
        protected Enumeration<DeviceMetricCalibrationState> state;

        /**
         * Describes the time last calibration has been performed.
         */
        @Child(name = "time", type = {InstantType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Describes the time last calibration has been performed", formalDefinition="Describes the time last calibration has been performed." )
        protected InstantType time;

        private static final long serialVersionUID = 1163986578L;

    /**
     * Constructor
     */
      public DeviceMetricCalibrationComponent() {
        super();
      }

        /**
         * @return {@link #type} (Describes the type of the calibration method.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DeviceMetricCalibrationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceMetricCalibrationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DeviceMetricCalibrationType>(new DeviceMetricCalibrationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Describes the type of the calibration method.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public DeviceMetricCalibrationComponent setTypeElement(Enumeration<DeviceMetricCalibrationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Describes the type of the calibration method.
         */
        public DeviceMetricCalibrationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Describes the type of the calibration method.
         */
        public DeviceMetricCalibrationComponent setType(DeviceMetricCalibrationType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<DeviceMetricCalibrationType>(new DeviceMetricCalibrationTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #state} (Describes the state of the calibration.). This is the underlying object with id, value and extensions. The accessor "getState" gives direct access to the value
         */
        public Enumeration<DeviceMetricCalibrationState> getStateElement() { 
          if (this.state == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceMetricCalibrationComponent.state");
            else if (Configuration.doAutoCreate())
              this.state = new Enumeration<DeviceMetricCalibrationState>(new DeviceMetricCalibrationStateEnumFactory()); // bb
          return this.state;
        }

        public boolean hasStateElement() { 
          return this.state != null && !this.state.isEmpty();
        }

        public boolean hasState() { 
          return this.state != null && !this.state.isEmpty();
        }

        /**
         * @param value {@link #state} (Describes the state of the calibration.). This is the underlying object with id, value and extensions. The accessor "getState" gives direct access to the value
         */
        public DeviceMetricCalibrationComponent setStateElement(Enumeration<DeviceMetricCalibrationState> value) { 
          this.state = value;
          return this;
        }

        /**
         * @return Describes the state of the calibration.
         */
        public DeviceMetricCalibrationState getState() { 
          return this.state == null ? null : this.state.getValue();
        }

        /**
         * @param value Describes the state of the calibration.
         */
        public DeviceMetricCalibrationComponent setState(DeviceMetricCalibrationState value) { 
          if (value == null)
            this.state = null;
          else {
            if (this.state == null)
              this.state = new Enumeration<DeviceMetricCalibrationState>(new DeviceMetricCalibrationStateEnumFactory());
            this.state.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #time} (Describes the time last calibration has been performed.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public InstantType getTimeElement() { 
          if (this.time == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceMetricCalibrationComponent.time");
            else if (Configuration.doAutoCreate())
              this.time = new InstantType(); // bb
          return this.time;
        }

        public boolean hasTimeElement() { 
          return this.time != null && !this.time.isEmpty();
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (Describes the time last calibration has been performed.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public DeviceMetricCalibrationComponent setTimeElement(InstantType value) { 
          this.time = value;
          return this;
        }

        /**
         * @return Describes the time last calibration has been performed.
         */
        public Date getTime() { 
          return this.time == null ? null : this.time.getValue();
        }

        /**
         * @param value Describes the time last calibration has been performed.
         */
        public DeviceMetricCalibrationComponent setTime(Date value) { 
          if (value == null)
            this.time = null;
          else {
            if (this.time == null)
              this.time = new InstantType();
            this.time.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Describes the type of the calibration method.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("state", "code", "Describes the state of the calibration.", 0, java.lang.Integer.MAX_VALUE, state));
          childrenList.add(new Property("time", "instant", "Describes the time last calibration has been performed.", 0, java.lang.Integer.MAX_VALUE, time));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DeviceMetricCalibrationType>
        case 109757585: /*state*/ return this.state == null ? new Base[0] : new Base[] {this.state}; // Enumeration<DeviceMetricCalibrationState>
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // InstantType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new DeviceMetricCalibrationTypeEnumFactory().fromType(value); // Enumeration<DeviceMetricCalibrationType>
          break;
        case 109757585: // state
          this.state = new DeviceMetricCalibrationStateEnumFactory().fromType(value); // Enumeration<DeviceMetricCalibrationState>
          break;
        case 3560141: // time
          this.time = castToInstant(value); // InstantType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new DeviceMetricCalibrationTypeEnumFactory().fromType(value); // Enumeration<DeviceMetricCalibrationType>
        else if (name.equals("state"))
          this.state = new DeviceMetricCalibrationStateEnumFactory().fromType(value); // Enumeration<DeviceMetricCalibrationState>
        else if (name.equals("time"))
          this.time = castToInstant(value); // InstantType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<DeviceMetricCalibrationType>
        case 109757585: throw new FHIRException("Cannot make property state as it is not a complex type"); // Enumeration<DeviceMetricCalibrationState>
        case 3560141: throw new FHIRException("Cannot make property time as it is not a complex type"); // InstantType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.type");
        }
        else if (name.equals("state")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.state");
        }
        else if (name.equals("time")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.time");
        }
        else
          return super.addChild(name);
      }

      public DeviceMetricCalibrationComponent copy() {
        DeviceMetricCalibrationComponent dst = new DeviceMetricCalibrationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.state = state == null ? null : state.copy();
        dst.time = time == null ? null : time.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceMetricCalibrationComponent))
          return false;
        DeviceMetricCalibrationComponent o = (DeviceMetricCalibrationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(state, o.state, true) && compareDeep(time, o.time, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceMetricCalibrationComponent))
          return false;
        DeviceMetricCalibrationComponent o = (DeviceMetricCalibrationComponent) other;
        return compareValues(type, o.type, true) && compareValues(state, o.state, true) && compareValues(time, o.time, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (state == null || state.isEmpty())
           && (time == null || time.isEmpty());
      }

  public String fhirType() {
    return "DeviceMetric.calibration";

  }

  }

    /**
     * Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of metric", formalDefinition="Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc." )
    protected CodeableConcept type;

    /**
     * Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID.  It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier of this DeviceMetric", formalDefinition="Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID.  It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device." )
    protected Identifier identifier;

    /**
     * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
     */
    @Child(name = "unit", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unit of metric", formalDefinition="Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc." )
    protected CodeableConcept unit;

    /**
     * Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.
     */
    @Child(name = "source", type = {Device.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Describes the link to the source Device", formalDefinition="Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    protected Device sourceTarget;

    /**
     * Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.
     */
    @Child(name = "parent", type = {DeviceComponent.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Describes the link to the parent DeviceComponent", formalDefinition="Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    protected DeviceComponent parentTarget;

    /**
     * Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    @Child(name = "operationalStatus", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="on | off | standby", formalDefinition="Indicates current operational state of the device. For example: On, Off, Standby, etc." )
    protected Enumeration<DeviceMetricOperationalStatus> operationalStatus;

    /**
     * Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
     */
    @Child(name = "color", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="black | red | green | yellow | blue | magenta | cyan | white", formalDefinition="Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta." )
    protected Enumeration<DeviceMetricColor> color;

    /**
     * Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    @Child(name = "category", type = {CodeType.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="measurement | setting | calculation | unspecified", formalDefinition="Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation." )
    protected Enumeration<DeviceMetricCategory> category;

    /**
     * Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition time, if the device does not update the published observed value with the same frequency as it was measured.
     */
    @Child(name = "measurementPeriod", type = {Timing.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Describes the measurement repetition time", formalDefinition="Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition time, if the device does not update the published observed value with the same frequency as it was measured." )
    protected Timing measurementPeriod;

    /**
     * Describes the calibrations that have been performed or that are required to be performed.
     */
    @Child(name = "calibration", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Describes the calibrations that have been performed or that are required to be performed", formalDefinition="Describes the calibrations that have been performed or that are required to be performed." )
    protected List<DeviceMetricCalibrationComponent> calibration;

    private static final long serialVersionUID = 1786401018L;

  /**
   * Constructor
   */
    public DeviceMetric() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceMetric(CodeableConcept type, Identifier identifier, Enumeration<DeviceMetricCategory> category) {
      super();
      this.type = type;
      this.identifier = identifier;
      this.category = category;
    }

    /**
     * @return {@link #type} (Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.)
     */
    public DeviceMetric setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID.  It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID.  It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.)
     */
    public DeviceMetric setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #unit} (Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.)
     */
    public CodeableConcept getUnit() { 
      if (this.unit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.unit");
        else if (Configuration.doAutoCreate())
          this.unit = new CodeableConcept(); // cc
      return this.unit;
    }

    public boolean hasUnit() { 
      return this.unit != null && !this.unit.isEmpty();
    }

    /**
     * @param value {@link #unit} (Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.)
     */
    public DeviceMetric setUnit(CodeableConcept value) { 
      this.unit = value;
      return this;
    }

    /**
     * @return {@link #source} (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceMetric setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    public Device getSourceTarget() { 
      if (this.sourceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.source");
        else if (Configuration.doAutoCreate())
          this.sourceTarget = new Device(); // aa
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    public DeviceMetric setSourceTarget(Device value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #parent} (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public Reference getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Reference(); // cc
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceMetric setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceComponent getParentTarget() { 
      if (this.parentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.parent");
        else if (Configuration.doAutoCreate())
          this.parentTarget = new DeviceComponent(); // aa
      return this.parentTarget;
    }

    /**
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceMetric setParentTarget(DeviceComponent value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #operationalStatus} (Indicates current operational state of the device. For example: On, Off, Standby, etc.). This is the underlying object with id, value and extensions. The accessor "getOperationalStatus" gives direct access to the value
     */
    public Enumeration<DeviceMetricOperationalStatus> getOperationalStatusElement() { 
      if (this.operationalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.operationalStatus");
        else if (Configuration.doAutoCreate())
          this.operationalStatus = new Enumeration<DeviceMetricOperationalStatus>(new DeviceMetricOperationalStatusEnumFactory()); // bb
      return this.operationalStatus;
    }

    public boolean hasOperationalStatusElement() { 
      return this.operationalStatus != null && !this.operationalStatus.isEmpty();
    }

    public boolean hasOperationalStatus() { 
      return this.operationalStatus != null && !this.operationalStatus.isEmpty();
    }

    /**
     * @param value {@link #operationalStatus} (Indicates current operational state of the device. For example: On, Off, Standby, etc.). This is the underlying object with id, value and extensions. The accessor "getOperationalStatus" gives direct access to the value
     */
    public DeviceMetric setOperationalStatusElement(Enumeration<DeviceMetricOperationalStatus> value) { 
      this.operationalStatus = value;
      return this;
    }

    /**
     * @return Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    public DeviceMetricOperationalStatus getOperationalStatus() { 
      return this.operationalStatus == null ? null : this.operationalStatus.getValue();
    }

    /**
     * @param value Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    public DeviceMetric setOperationalStatus(DeviceMetricOperationalStatus value) { 
      if (value == null)
        this.operationalStatus = null;
      else {
        if (this.operationalStatus == null)
          this.operationalStatus = new Enumeration<DeviceMetricOperationalStatus>(new DeviceMetricOperationalStatusEnumFactory());
        this.operationalStatus.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #color} (Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.). This is the underlying object with id, value and extensions. The accessor "getColor" gives direct access to the value
     */
    public Enumeration<DeviceMetricColor> getColorElement() { 
      if (this.color == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.color");
        else if (Configuration.doAutoCreate())
          this.color = new Enumeration<DeviceMetricColor>(new DeviceMetricColorEnumFactory()); // bb
      return this.color;
    }

    public boolean hasColorElement() { 
      return this.color != null && !this.color.isEmpty();
    }

    public boolean hasColor() { 
      return this.color != null && !this.color.isEmpty();
    }

    /**
     * @param value {@link #color} (Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.). This is the underlying object with id, value and extensions. The accessor "getColor" gives direct access to the value
     */
    public DeviceMetric setColorElement(Enumeration<DeviceMetricColor> value) { 
      this.color = value;
      return this;
    }

    /**
     * @return Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
     */
    public DeviceMetricColor getColor() { 
      return this.color == null ? null : this.color.getValue();
    }

    /**
     * @param value Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
     */
    public DeviceMetric setColor(DeviceMetricColor value) { 
      if (value == null)
        this.color = null;
      else {
        if (this.color == null)
          this.color = new Enumeration<DeviceMetricColor>(new DeviceMetricColorEnumFactory());
        this.color.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<DeviceMetricCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<DeviceMetricCategory>(new DeviceMetricCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public DeviceMetric setCategoryElement(Enumeration<DeviceMetricCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    public DeviceMetricCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    public DeviceMetric setCategory(DeviceMetricCategory value) { 
        if (this.category == null)
          this.category = new Enumeration<DeviceMetricCategory>(new DeviceMetricCategoryEnumFactory());
        this.category.setValue(value);
      return this;
    }

    /**
     * @return {@link #measurementPeriod} (Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition time, if the device does not update the published observed value with the same frequency as it was measured.)
     */
    public Timing getMeasurementPeriod() { 
      if (this.measurementPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.measurementPeriod");
        else if (Configuration.doAutoCreate())
          this.measurementPeriod = new Timing(); // cc
      return this.measurementPeriod;
    }

    public boolean hasMeasurementPeriod() { 
      return this.measurementPeriod != null && !this.measurementPeriod.isEmpty();
    }

    /**
     * @param value {@link #measurementPeriod} (Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition time, if the device does not update the published observed value with the same frequency as it was measured.)
     */
    public DeviceMetric setMeasurementPeriod(Timing value) { 
      this.measurementPeriod = value;
      return this;
    }

    /**
     * @return {@link #calibration} (Describes the calibrations that have been performed or that are required to be performed.)
     */
    public List<DeviceMetricCalibrationComponent> getCalibration() { 
      if (this.calibration == null)
        this.calibration = new ArrayList<DeviceMetricCalibrationComponent>();
      return this.calibration;
    }

    public boolean hasCalibration() { 
      if (this.calibration == null)
        return false;
      for (DeviceMetricCalibrationComponent item : this.calibration)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #calibration} (Describes the calibrations that have been performed or that are required to be performed.)
     */
    // syntactic sugar
    public DeviceMetricCalibrationComponent addCalibration() { //3
      DeviceMetricCalibrationComponent t = new DeviceMetricCalibrationComponent();
      if (this.calibration == null)
        this.calibration = new ArrayList<DeviceMetricCalibrationComponent>();
      this.calibration.add(t);
      return t;
    }

    // syntactic sugar
    public DeviceMetric addCalibration(DeviceMetricCalibrationComponent t) { //3
      if (t == null)
        return this;
      if (this.calibration == null)
        this.calibration = new ArrayList<DeviceMetricCalibrationComponent>();
      this.calibration.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "CodeableConcept", "Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("identifier", "Identifier", "Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID.  It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("unit", "CodeableConcept", "Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.", 0, java.lang.Integer.MAX_VALUE, unit));
        childrenList.add(new Property("source", "Reference(Device)", "Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("parent", "Reference(DeviceComponent)", "Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device. An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("operationalStatus", "code", "Indicates current operational state of the device. For example: On, Off, Standby, etc.", 0, java.lang.Integer.MAX_VALUE, operationalStatus));
        childrenList.add(new Property("color", "code", "Describes the color representation for the metric. This is often used to aid clinicians to track and identify parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.", 0, java.lang.Integer.MAX_VALUE, color));
        childrenList.add(new Property("category", "code", "Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("measurementPeriod", "Timing", "Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition time, if the device does not update the published observed value with the same frequency as it was measured.", 0, java.lang.Integer.MAX_VALUE, measurementPeriod));
        childrenList.add(new Property("calibration", "", "Describes the calibrations that have been performed or that are required to be performed.", 0, java.lang.Integer.MAX_VALUE, calibration));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : new Base[] {this.parent}; // Reference
        case -2103166364: /*operationalStatus*/ return this.operationalStatus == null ? new Base[0] : new Base[] {this.operationalStatus}; // Enumeration<DeviceMetricOperationalStatus>
        case 94842723: /*color*/ return this.color == null ? new Base[0] : new Base[] {this.color}; // Enumeration<DeviceMetricColor>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<DeviceMetricCategory>
        case -1300332387: /*measurementPeriod*/ return this.measurementPeriod == null ? new Base[0] : new Base[] {this.measurementPeriod}; // Timing
        case 1421318634: /*calibration*/ return this.calibration == null ? new Base[0] : this.calibration.toArray(new Base[this.calibration.size()]); // DeviceMetricCalibrationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case 3594628: // unit
          this.unit = castToCodeableConcept(value); // CodeableConcept
          break;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          break;
        case -995424086: // parent
          this.parent = castToReference(value); // Reference
          break;
        case -2103166364: // operationalStatus
          this.operationalStatus = new DeviceMetricOperationalStatusEnumFactory().fromType(value); // Enumeration<DeviceMetricOperationalStatus>
          break;
        case 94842723: // color
          this.color = new DeviceMetricColorEnumFactory().fromType(value); // Enumeration<DeviceMetricColor>
          break;
        case 50511102: // category
          this.category = new DeviceMetricCategoryEnumFactory().fromType(value); // Enumeration<DeviceMetricCategory>
          break;
        case -1300332387: // measurementPeriod
          this.measurementPeriod = castToTiming(value); // Timing
          break;
        case 1421318634: // calibration
          this.getCalibration().add((DeviceMetricCalibrationComponent) value); // DeviceMetricCalibrationComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("unit"))
          this.unit = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("source"))
          this.source = castToReference(value); // Reference
        else if (name.equals("parent"))
          this.parent = castToReference(value); // Reference
        else if (name.equals("operationalStatus"))
          this.operationalStatus = new DeviceMetricOperationalStatusEnumFactory().fromType(value); // Enumeration<DeviceMetricOperationalStatus>
        else if (name.equals("color"))
          this.color = new DeviceMetricColorEnumFactory().fromType(value); // Enumeration<DeviceMetricColor>
        else if (name.equals("category"))
          this.category = new DeviceMetricCategoryEnumFactory().fromType(value); // Enumeration<DeviceMetricCategory>
        else if (name.equals("measurementPeriod"))
          this.measurementPeriod = castToTiming(value); // Timing
        else if (name.equals("calibration"))
          this.getCalibration().add((DeviceMetricCalibrationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -1618432855:  return getIdentifier(); // Identifier
        case 3594628:  return getUnit(); // CodeableConcept
        case -896505829:  return getSource(); // Reference
        case -995424086:  return getParent(); // Reference
        case -2103166364: throw new FHIRException("Cannot make property operationalStatus as it is not a complex type"); // Enumeration<DeviceMetricOperationalStatus>
        case 94842723: throw new FHIRException("Cannot make property color as it is not a complex type"); // Enumeration<DeviceMetricColor>
        case 50511102: throw new FHIRException("Cannot make property category as it is not a complex type"); // Enumeration<DeviceMetricCategory>
        case -1300332387:  return getMeasurementPeriod(); // Timing
        case 1421318634:  return addCalibration(); // DeviceMetricCalibrationComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("unit")) {
          this.unit = new CodeableConcept();
          return this.unit;
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("parent")) {
          this.parent = new Reference();
          return this.parent;
        }
        else if (name.equals("operationalStatus")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.operationalStatus");
        }
        else if (name.equals("color")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.color");
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceMetric.category");
        }
        else if (name.equals("measurementPeriod")) {
          this.measurementPeriod = new Timing();
          return this.measurementPeriod;
        }
        else if (name.equals("calibration")) {
          return addCalibration();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceMetric";

  }

      public DeviceMetric copy() {
        DeviceMetric dst = new DeviceMetric();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.source = source == null ? null : source.copy();
        dst.parent = parent == null ? null : parent.copy();
        dst.operationalStatus = operationalStatus == null ? null : operationalStatus.copy();
        dst.color = color == null ? null : color.copy();
        dst.category = category == null ? null : category.copy();
        dst.measurementPeriod = measurementPeriod == null ? null : measurementPeriod.copy();
        if (calibration != null) {
          dst.calibration = new ArrayList<DeviceMetricCalibrationComponent>();
          for (DeviceMetricCalibrationComponent i : calibration)
            dst.calibration.add(i.copy());
        };
        return dst;
      }

      protected DeviceMetric typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceMetric))
          return false;
        DeviceMetric o = (DeviceMetric) other;
        return compareDeep(type, o.type, true) && compareDeep(identifier, o.identifier, true) && compareDeep(unit, o.unit, true)
           && compareDeep(source, o.source, true) && compareDeep(parent, o.parent, true) && compareDeep(operationalStatus, o.operationalStatus, true)
           && compareDeep(color, o.color, true) && compareDeep(category, o.category, true) && compareDeep(measurementPeriod, o.measurementPeriod, true)
           && compareDeep(calibration, o.calibration, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceMetric))
          return false;
        DeviceMetric o = (DeviceMetric) other;
        return compareValues(operationalStatus, o.operationalStatus, true) && compareValues(color, o.color, true)
           && compareValues(category, o.category, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (unit == null || unit.isEmpty()) && (source == null || source.isEmpty()) && (parent == null || parent.isEmpty())
           && (operationalStatus == null || operationalStatus.isEmpty()) && (color == null || color.isEmpty())
           && (category == null || category.isEmpty()) && (measurementPeriod == null || measurementPeriod.isEmpty())
           && (calibration == null || calibration.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceMetric;
   }

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The category of the metric</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="DeviceMetric.category", description="The category of the metric", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The category of the metric</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>The device resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceMetric.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="DeviceMetric.source", description="The device resource", type="reference" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>The device resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceMetric.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceMetric:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("DeviceMetric:source").toLocked();

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceMetric resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceMetric.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="DeviceMetric.parent", description="The parent DeviceMetric resource", type="reference" )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>The parent DeviceMetric resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceMetric.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceMetric:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("DeviceMetric:parent").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="DeviceMetric.type", description="The component type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The component type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the metric</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DeviceMetric.identifier", description="The identifier of the metric", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the metric</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceMetric.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

