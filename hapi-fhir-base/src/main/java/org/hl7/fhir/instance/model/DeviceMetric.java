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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Describes a measurement, calculation or setting capability of a medical device.
 */
@ResourceDef(name="DeviceMetric", profile="http://hl7.org/fhir/Profile/DeviceMetric")
public class DeviceMetric extends Resource {

    public enum MetricOperationalStatus implements FhirEnum {
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

      public static final MetricOperationalStatusEnumFactory ENUM_FACTORY = new MetricOperationalStatusEnumFactory();

        public static MetricOperationalStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("on".equals(codeString))
          return ON;
        if ("off".equals(codeString))
          return OFF;
        if ("standby".equals(codeString))
          return STANDBY;
        throw new IllegalArgumentException("Unknown MetricOperationalStatus code '"+codeString+"'");
        }
        @Override
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
            case ON: return "";
            case OFF: return "";
            case STANDBY: return "";
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
            case ON: return "on";
            case OFF: return "off";
            case STANDBY: return "standby";
            default: return "?";
          }
        }
    }

  public static class MetricOperationalStatusEnumFactory implements EnumFactory<MetricOperationalStatus> {
    public MetricOperationalStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("on".equals(codeString))
          return MetricOperationalStatus.ON;
        if ("off".equals(codeString))
          return MetricOperationalStatus.OFF;
        if ("standby".equals(codeString))
          return MetricOperationalStatus.STANDBY;
        throw new IllegalArgumentException("Unknown MetricOperationalStatus code '"+codeString+"'");
        }
    public String toCode(MetricOperationalStatus code) throws IllegalArgumentException {
      if (code == MetricOperationalStatus.ON)
        return "on";
      if (code == MetricOperationalStatus.OFF)
        return "off";
      if (code == MetricOperationalStatus.STANDBY)
        return "standby";
      return "?";
      }
    }

    public enum MetricCategory implements FhirEnum {
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

      public static final MetricCategoryEnumFactory ENUM_FACTORY = new MetricCategoryEnumFactory();

        public static MetricCategory fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown MetricCategory code '"+codeString+"'");
        }
        @Override
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
            case MEASUREMENT: return "";
            case SETTING: return "";
            case CALCULATION: return "";
            case UNSPECIFIED: return "";
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
            case MEASUREMENT: return "measurement";
            case SETTING: return "setting";
            case CALCULATION: return "calculation";
            case UNSPECIFIED: return "unspecified";
            default: return "?";
          }
        }
    }

  public static class MetricCategoryEnumFactory implements EnumFactory<MetricCategory> {
    public MetricCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("measurement".equals(codeString))
          return MetricCategory.MEASUREMENT;
        if ("setting".equals(codeString))
          return MetricCategory.SETTING;
        if ("calculation".equals(codeString))
          return MetricCategory.CALCULATION;
        if ("unspecified".equals(codeString))
          return MetricCategory.UNSPECIFIED;
        throw new IllegalArgumentException("Unknown MetricCategory code '"+codeString+"'");
        }
    public String toCode(MetricCategory code) throws IllegalArgumentException {
      if (code == MetricCategory.MEASUREMENT)
        return "measurement";
      if (code == MetricCategory.SETTING)
        return "setting";
      if (code == MetricCategory.CALCULATION)
        return "calculation";
      if (code == MetricCategory.UNSPECIFIED)
        return "unspecified";
      return "?";
      }
    }

    public enum MetricCalibrationType implements FhirEnum {
        /**
         * TODO.
         */
        UNSPECIFIED, 
        /**
         * TODO.
         */
        OFFSET, 
        /**
         * TODO.
         */
        GAIN, 
        /**
         * TODO.
         */
        TWOPOINT, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final MetricCalibrationTypeEnumFactory ENUM_FACTORY = new MetricCalibrationTypeEnumFactory();

        public static MetricCalibrationType fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown MetricCalibrationType code '"+codeString+"'");
        }
        @Override
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
            case UNSPECIFIED: return "";
            case OFFSET: return "";
            case GAIN: return "";
            case TWOPOINT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNSPECIFIED: return "TODO.";
            case OFFSET: return "TODO.";
            case GAIN: return "TODO.";
            case TWOPOINT: return "TODO.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNSPECIFIED: return "unspecified";
            case OFFSET: return "offset";
            case GAIN: return "gain";
            case TWOPOINT: return "two-point";
            default: return "?";
          }
        }
    }

  public static class MetricCalibrationTypeEnumFactory implements EnumFactory<MetricCalibrationType> {
    public MetricCalibrationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return MetricCalibrationType.UNSPECIFIED;
        if ("offset".equals(codeString))
          return MetricCalibrationType.OFFSET;
        if ("gain".equals(codeString))
          return MetricCalibrationType.GAIN;
        if ("two-point".equals(codeString))
          return MetricCalibrationType.TWOPOINT;
        throw new IllegalArgumentException("Unknown MetricCalibrationType code '"+codeString+"'");
        }
    public String toCode(MetricCalibrationType code) throws IllegalArgumentException {
      if (code == MetricCalibrationType.UNSPECIFIED)
        return "unspecified";
      if (code == MetricCalibrationType.OFFSET)
        return "offset";
      if (code == MetricCalibrationType.GAIN)
        return "gain";
      if (code == MetricCalibrationType.TWOPOINT)
        return "two-point";
      return "?";
      }
    }

    public enum MetricCalibrationState implements FhirEnum {
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

      public static final MetricCalibrationStateEnumFactory ENUM_FACTORY = new MetricCalibrationStateEnumFactory();

        public static MetricCalibrationState fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown MetricCalibrationState code '"+codeString+"'");
        }
        @Override
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
            case NOTCALIBRATED: return "";
            case CALIBRATIONREQUIRED: return "";
            case CALIBRATED: return "";
            case UNSPECIFIED: return "";
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
            case NOTCALIBRATED: return "not-calibrated";
            case CALIBRATIONREQUIRED: return "calibration-required";
            case CALIBRATED: return "calibrated";
            case UNSPECIFIED: return "unspecified";
            default: return "?";
          }
        }
    }

  public static class MetricCalibrationStateEnumFactory implements EnumFactory<MetricCalibrationState> {
    public MetricCalibrationState fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-calibrated".equals(codeString))
          return MetricCalibrationState.NOTCALIBRATED;
        if ("calibration-required".equals(codeString))
          return MetricCalibrationState.CALIBRATIONREQUIRED;
        if ("calibrated".equals(codeString))
          return MetricCalibrationState.CALIBRATED;
        if ("unspecified".equals(codeString))
          return MetricCalibrationState.UNSPECIFIED;
        throw new IllegalArgumentException("Unknown MetricCalibrationState code '"+codeString+"'");
        }
    public String toCode(MetricCalibrationState code) throws IllegalArgumentException {
      if (code == MetricCalibrationState.NOTCALIBRATED)
        return "not-calibrated";
      if (code == MetricCalibrationState.CALIBRATIONREQUIRED)
        return "calibration-required";
      if (code == MetricCalibrationState.CALIBRATED)
        return "calibrated";
      if (code == MetricCalibrationState.UNSPECIFIED)
        return "unspecified";
      return "?";
      }
    }

    @Block()
    public static class DeviceMetricCalibrationInfoComponent extends BackboneElement {
        /**
         * Describes the type of the calibration method.
         */
        @Child(name="type", type={CodeType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="unspecified | offset | gain | two-point", formalDefinition="Describes the type of the calibration method." )
        protected Enumeration<MetricCalibrationType> type;

        /**
         * Describes the state of the calibration.
         */
        @Child(name="state", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="not-calibrated | calibration-required | calibrated | unspecified", formalDefinition="Describes the state of the calibration." )
        protected Enumeration<MetricCalibrationState> state;

        /**
         * Describes the time last calibration has been performed.
         */
        @Child(name="time", type={InstantType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Describes the time last calibration has been performed", formalDefinition="Describes the time last calibration has been performed." )
        protected InstantType time;

        private static final long serialVersionUID = 407720126L;

      public DeviceMetricCalibrationInfoComponent() {
        super();
      }

        /**
         * @return {@link #type} (Describes the type of the calibration method.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<MetricCalibrationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceMetricCalibrationInfoComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<MetricCalibrationType>();
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
        public DeviceMetricCalibrationInfoComponent setTypeElement(Enumeration<MetricCalibrationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Describes the type of the calibration method.
         */
        public MetricCalibrationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Describes the type of the calibration method.
         */
        public DeviceMetricCalibrationInfoComponent setType(MetricCalibrationType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<MetricCalibrationType>(MetricCalibrationType.ENUM_FACTORY);
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #state} (Describes the state of the calibration.). This is the underlying object with id, value and extensions. The accessor "getState" gives direct access to the value
         */
        public Enumeration<MetricCalibrationState> getStateElement() { 
          if (this.state == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DeviceMetricCalibrationInfoComponent.state");
            else if (Configuration.doAutoCreate())
              this.state = new Enumeration<MetricCalibrationState>();
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
        public DeviceMetricCalibrationInfoComponent setStateElement(Enumeration<MetricCalibrationState> value) { 
          this.state = value;
          return this;
        }

        /**
         * @return Describes the state of the calibration.
         */
        public MetricCalibrationState getState() { 
          return this.state == null ? null : this.state.getValue();
        }

        /**
         * @param value Describes the state of the calibration.
         */
        public DeviceMetricCalibrationInfoComponent setState(MetricCalibrationState value) { 
          if (value == null)
            this.state = null;
          else {
            if (this.state == null)
              this.state = new Enumeration<MetricCalibrationState>(MetricCalibrationState.ENUM_FACTORY);
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
              throw new Error("Attempt to auto-create DeviceMetricCalibrationInfoComponent.time");
            else if (Configuration.doAutoCreate())
              this.time = new InstantType();
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
        public DeviceMetricCalibrationInfoComponent setTimeElement(InstantType value) { 
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
        public DeviceMetricCalibrationInfoComponent setTime(Date value) { 
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

      public DeviceMetricCalibrationInfoComponent copy() {
        DeviceMetricCalibrationInfoComponent dst = new DeviceMetricCalibrationInfoComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.state = state == null ? null : state.copy();
        dst.time = time == null ? null : time.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (state == null || state.isEmpty())
           && (time == null || time.isEmpty());
      }

  }

    /**
     * Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.
     */
    @Child(name="type", type={CodeableConcept.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="Type of metric", formalDefinition="Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc." )
    protected CodeableConcept type;

    /**
     * Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID. 
It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.
     */
    @Child(name="identifier", type={Identifier.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Unique identifier of this DeviceMetric", formalDefinition="Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID. \nIt should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device." )
    protected Identifier identifier;

    /**
     * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
     */
    @Child(name="unit", type={CodeableConcept.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Unit of metric", formalDefinition="Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc." )
    protected CodeableConcept unit;

    /**
     * Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.
     */
    @Child(name="source", type={Device.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Describes the link to the source Device", formalDefinition="Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.)
     */
    protected Device sourceTarget;

    /**
     * Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.
     */
    @Child(name="parent", type={DeviceComponent.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Describes the link to the parent DeviceComponent", formalDefinition="Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.\nAn example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    protected DeviceComponent parentTarget;

    /**
     * Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    @Child(name="operationalState", type={CodeType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="on | off | standby", formalDefinition="Indicates current operational state of the device. For example: On, Off, Standby, etc." )
    protected Enumeration<MetricOperationalStatus> operationalState;

    /**
     * Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.
     */
    @Child(name="measurementMode", type={Identifier.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Describes the physical principle of the measurement", formalDefinition="Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc." )
    protected Identifier measurementMode;

    /**
     * Describes the typical color of the representation of observations that have been generated for this DeviceMetric.
     */
    @Child(name="color", type={Identifier.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Describes the typical color of representation", formalDefinition="Describes the typical color of the representation of observations that have been generated for this DeviceMetric." )
    protected Identifier color;

    /**
     * Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    @Child(name="category", type={CodeType.class}, order=7, min=1, max=1)
    @Description(shortDefinition="measurement | setting | calculation | unspecified", formalDefinition="Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation." )
    protected Enumeration<MetricCategory> category;

    /**
     * Describes the measurement repetition time. This is not
necessarily the same as the update
period.
     */
    @Child(name="measurementPeriod", type={Timing.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Describes the measurement repetition time", formalDefinition="Describes the measurement repetition time. This is not\nnecessarily the same as the update\nperiod." )
    protected Timing measurementPeriod;

    /**
     * Describes the calibrations that have been performed or that are required to be performed.
     */
    @Child(name="calibrationInfo", type={}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Describes the calibrations that have been performed or that are required to be performed", formalDefinition="Describes the calibrations that have been performed or that are required to be performed." )
    protected List<DeviceMetricCalibrationInfoComponent> calibrationInfo;

    private static final long serialVersionUID = 600373390L;

    public DeviceMetric() {
      super();
    }

    public DeviceMetric(CodeableConcept type, Identifier identifier, Enumeration<MetricCategory> category) {
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
          this.type = new CodeableConcept();
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
     * @return {@link #identifier} (Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID. 
It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID. 
It should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.)
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
          this.unit = new CodeableConcept();
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
          this.source = new Reference();
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
          this.sourceTarget = new Device();
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
     * @return {@link #parent} (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public Reference getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Reference();
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceMetric setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceComponent getParentTarget() { 
      if (this.parentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.parent");
        else if (Configuration.doAutoCreate())
          this.parentTarget = new DeviceComponent();
      return this.parentTarget;
    }

    /**
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.
An example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.)
     */
    public DeviceMetric setParentTarget(DeviceComponent value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #operationalState} (Indicates current operational state of the device. For example: On, Off, Standby, etc.). This is the underlying object with id, value and extensions. The accessor "getOperationalState" gives direct access to the value
     */
    public Enumeration<MetricOperationalStatus> getOperationalStateElement() { 
      if (this.operationalState == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.operationalState");
        else if (Configuration.doAutoCreate())
          this.operationalState = new Enumeration<MetricOperationalStatus>();
      return this.operationalState;
    }

    public boolean hasOperationalStateElement() { 
      return this.operationalState != null && !this.operationalState.isEmpty();
    }

    public boolean hasOperationalState() { 
      return this.operationalState != null && !this.operationalState.isEmpty();
    }

    /**
     * @param value {@link #operationalState} (Indicates current operational state of the device. For example: On, Off, Standby, etc.). This is the underlying object with id, value and extensions. The accessor "getOperationalState" gives direct access to the value
     */
    public DeviceMetric setOperationalStateElement(Enumeration<MetricOperationalStatus> value) { 
      this.operationalState = value;
      return this;
    }

    /**
     * @return Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    public MetricOperationalStatus getOperationalState() { 
      return this.operationalState == null ? null : this.operationalState.getValue();
    }

    /**
     * @param value Indicates current operational state of the device. For example: On, Off, Standby, etc.
     */
    public DeviceMetric setOperationalState(MetricOperationalStatus value) { 
      if (value == null)
        this.operationalState = null;
      else {
        if (this.operationalState == null)
          this.operationalState = new Enumeration<MetricOperationalStatus>(MetricOperationalStatus.ENUM_FACTORY);
        this.operationalState.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #measurementMode} (Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.)
     */
    public Identifier getMeasurementMode() { 
      if (this.measurementMode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.measurementMode");
        else if (Configuration.doAutoCreate())
          this.measurementMode = new Identifier();
      return this.measurementMode;
    }

    public boolean hasMeasurementMode() { 
      return this.measurementMode != null && !this.measurementMode.isEmpty();
    }

    /**
     * @param value {@link #measurementMode} (Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.)
     */
    public DeviceMetric setMeasurementMode(Identifier value) { 
      this.measurementMode = value;
      return this;
    }

    /**
     * @return {@link #color} (Describes the typical color of the representation of observations that have been generated for this DeviceMetric.)
     */
    public Identifier getColor() { 
      if (this.color == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.color");
        else if (Configuration.doAutoCreate())
          this.color = new Identifier();
      return this.color;
    }

    public boolean hasColor() { 
      return this.color != null && !this.color.isEmpty();
    }

    /**
     * @param value {@link #color} (Describes the typical color of the representation of observations that have been generated for this DeviceMetric.)
     */
    public DeviceMetric setColor(Identifier value) { 
      this.color = value;
      return this;
    }

    /**
     * @return {@link #category} (Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<MetricCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<MetricCategory>();
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
    public DeviceMetric setCategoryElement(Enumeration<MetricCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    public MetricCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.
     */
    public DeviceMetric setCategory(MetricCategory value) { 
        if (this.category == null)
          this.category = new Enumeration<MetricCategory>(MetricCategory.ENUM_FACTORY);
        this.category.setValue(value);
      return this;
    }

    /**
     * @return {@link #measurementPeriod} (Describes the measurement repetition time. This is not
necessarily the same as the update
period.)
     */
    public Timing getMeasurementPeriod() { 
      if (this.measurementPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceMetric.measurementPeriod");
        else if (Configuration.doAutoCreate())
          this.measurementPeriod = new Timing();
      return this.measurementPeriod;
    }

    public boolean hasMeasurementPeriod() { 
      return this.measurementPeriod != null && !this.measurementPeriod.isEmpty();
    }

    /**
     * @param value {@link #measurementPeriod} (Describes the measurement repetition time. This is not
necessarily the same as the update
period.)
     */
    public DeviceMetric setMeasurementPeriod(Timing value) { 
      this.measurementPeriod = value;
      return this;
    }

    /**
     * @return {@link #calibrationInfo} (Describes the calibrations that have been performed or that are required to be performed.)
     */
    public List<DeviceMetricCalibrationInfoComponent> getCalibrationInfo() { 
      if (this.calibrationInfo == null)
        this.calibrationInfo = new ArrayList<DeviceMetricCalibrationInfoComponent>();
      return this.calibrationInfo;
    }

    public boolean hasCalibrationInfo() { 
      if (this.calibrationInfo == null)
        return false;
      for (DeviceMetricCalibrationInfoComponent item : this.calibrationInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #calibrationInfo} (Describes the calibrations that have been performed or that are required to be performed.)
     */
    // syntactic sugar
    public DeviceMetricCalibrationInfoComponent addCalibrationInfo() { //3
      DeviceMetricCalibrationInfoComponent t = new DeviceMetricCalibrationInfoComponent();
      if (this.calibrationInfo == null)
        this.calibrationInfo = new ArrayList<DeviceMetricCalibrationInfoComponent>();
      this.calibrationInfo.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "CodeableConcept", "Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("identifier", "Identifier", "Describes the unique identification of this metric that has been assigned by the device or gateway software. For example: handle ID. \nIt should be noted that in order to make the identifier unique, the system element of the identifier should be set to the unique identifier of the device.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("unit", "CodeableConcept", "Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.", 0, java.lang.Integer.MAX_VALUE, unit));
        childrenList.add(new Property("source", "Reference(Device)", "Describes the link to the  Device that this DeviceMetric belongs to and that contains administrative device information such as manufacture, serial number, etc.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("parent", "Reference(DeviceComponent)", "Describes the link to the  DeviceComponent that this DeviceMetric belongs to and that provide information about the location of this DeviceMetric in the containment structure of the parent Device.\nAn example would be a DeviceComponent that represents a Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but should be interpreted based on their containment location.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("operationalState", "code", "Indicates current operational state of the device. For example: On, Off, Standby, etc.", 0, java.lang.Integer.MAX_VALUE, operationalState));
        childrenList.add(new Property("measurementMode", "Identifier", "Describes the physical principle of the measurement. For example: thermal, chemical, acoustical, etc.", 0, java.lang.Integer.MAX_VALUE, measurementMode));
        childrenList.add(new Property("color", "Identifier", "Describes the typical color of the representation of observations that have been generated for this DeviceMetric.", 0, java.lang.Integer.MAX_VALUE, color));
        childrenList.add(new Property("category", "code", "Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, measurement, or calculation.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("measurementPeriod", "Timing", "Describes the measurement repetition time. This is not\nnecessarily the same as the update\nperiod.", 0, java.lang.Integer.MAX_VALUE, measurementPeriod));
        childrenList.add(new Property("calibrationInfo", "", "Describes the calibrations that have been performed or that are required to be performed.", 0, java.lang.Integer.MAX_VALUE, calibrationInfo));
      }

      public DeviceMetric copy() {
        DeviceMetric dst = new DeviceMetric();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.source = source == null ? null : source.copy();
        dst.parent = parent == null ? null : parent.copy();
        dst.operationalState = operationalState == null ? null : operationalState.copy();
        dst.measurementMode = measurementMode == null ? null : measurementMode.copy();
        dst.color = color == null ? null : color.copy();
        dst.category = category == null ? null : category.copy();
        dst.measurementPeriod = measurementPeriod == null ? null : measurementPeriod.copy();
        if (calibrationInfo != null) {
          dst.calibrationInfo = new ArrayList<DeviceMetricCalibrationInfoComponent>();
          for (DeviceMetricCalibrationInfoComponent i : calibrationInfo)
            dst.calibrationInfo.add(i.copy());
        };
        return dst;
      }

      protected DeviceMetric typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (unit == null || unit.isEmpty()) && (source == null || source.isEmpty()) && (parent == null || parent.isEmpty())
           && (operationalState == null || operationalState.isEmpty()) && (measurementMode == null || measurementMode.isEmpty())
           && (color == null || color.isEmpty()) && (category == null || category.isEmpty()) && (measurementPeriod == null || measurementPeriod.isEmpty())
           && (calibrationInfo == null || calibrationInfo.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceMetric;
   }

  @SearchParamDefinition(name="category", path="DeviceMetric.category", description="The category of the metric", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="source", path="DeviceMetric.source", description="The device resource", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="parent", path="DeviceMetric.parent", description="The parent DeviceMetric resource", type="reference" )
  public static final String SP_PARENT = "parent";
  @SearchParamDefinition(name="type", path="DeviceMetric.type", description="The component type", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="identifier", path="DeviceMetric.identifier", description="The identifier of the metric", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

