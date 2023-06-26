package ca.uhn.fhir.parser.i423;

import java.math.BigDecimal;
import java.util.List;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.util.ElementUtil;

@DatatypeDef(name = "Timing")
public class CustomTimingDt extends TimingDt {

    /**
     * repeat
     */
    @Child(name = FIELD_REPEAT, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {_Repeat.class})
    @Description(shortDefinition = "When the event is to occur", formalDefinition = "A set of rules that describe when the event should occur.")
    protected _Repeat ourRepeat;
    public static final String FIELD_REPEAT = "repeat";

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(ourRepeat);
    }

    @Override
    public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
        return ElementUtil.allPopulatedChildElements(theType, ourRepeat);
    }

    public _Repeat _getRepeat() {
        if (ourRepeat == null)
            ourRepeat = new _Repeat();
        return ourRepeat;
    }

    public CustomTimingDt _setRepeat(_Repeat theValue) {
        ourRepeat = theValue;
        return this;
    }

    @Block
    public static class _Repeat extends Repeat {
        /**
         * bounds
         */
        @Child(name = FIELD_BOUNDS, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {RangeDt.class, PeriodDt.class})
        @Description(shortDefinition = "Length/Range of lengths, or (Start and/or end) limits", formalDefinition = "Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.")
        protected IDatatype ourBounds;
        public static final String FIELD_BOUNDS = "bounds";
        /**
         * count
         */
        @Child(name = FIELD_COUNT, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {IntegerDt.class})
        @Description(shortDefinition = "Number of times to repeat", formalDefinition = "A total count of the desired number of repetitions.")
        protected IntegerDt ourCount;
        public static final String FIELD_COUNT = "count";
        /**
         * duration
         */
        @Child(name = FIELD_DURATION, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {DecimalDt.class})
        @Description(shortDefinition = "How long when it happens", formalDefinition = "How long this thing happens for when it happens.")
        protected DecimalDt ourDuration;
        public static final String FIELD_DURATION = "duration";
        /**
         * durationMax
         */
        @Child(name = FIELD_DURATIONMAX, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {DecimalDt.class})
        @Description(shortDefinition = "How long when it happens (Max)", formalDefinition = "The upper limit of how long this thing happens for when it happens.")
        protected DecimalDt ourDurationMax;
        public static final String FIELD_DURATIONMAX = "durationMax";
        /**
         * durationUnits
         */
        @Child(name = FIELD_DURATIONUNITS, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {CodeDt.class})
        @Description(shortDefinition = "s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition = "The units of time for the duration, in UCUM units.")
        protected BoundCodeDt<UnitsOfTimeEnum> ourDurationUnits;
        public static final String FIELD_DURATIONUNITS = "durationUnits";
        /**
         * frequency
         */
        @Child(name = FIELD_FREQUENCY, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {IntegerDt.class})
        @Description(shortDefinition = "Event occurs frequency times per period", formalDefinition = "The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).")
        protected IntegerDt ourFrequency;
        public static final String FIELD_FREQUENCY = "frequency";
        /**
         * frequencyMax
         */
        @Child(name = FIELD_FREQUENCYMAX, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {IntegerDt.class})
        @Description(shortDefinition = "Event occurs up to frequencyMax times per period", formalDefinition = "If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.")
        protected IntegerDt ourFrequencyMax;
        public static final String FIELD_FREQUENCYMAX = "frequencyMax";
        /**
         * period
         */
        @Child(name = FIELD_PERIOD, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {DecimalDt.class})
        @Description(shortDefinition = "Event occurs frequency times per period", formalDefinition = "Indicates the duration of time over which repetitions are to occur; e.g. to express \"3 times per day\", 3 would be the frequency and \"1 day\" would be the period.")
        protected DecimalDt ourPeriod;
        public static final String FIELD_PERIOD = "period";
        /**
         * periodMax
         */
        @Child(name = FIELD_PERIODMAX, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {DecimalDt.class})
        @Description(shortDefinition = "Upper limit of period (3-4 hours)", formalDefinition = "If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as \"do this once every 3-5 days.")
        protected DecimalDt ourPeriodMax;
        public static final String FIELD_PERIODMAX = "periodMax";
        /**
         * periodUnits
         */
        @Child(name = FIELD_PERIODUNITS, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {CodeDt.class})
        @Description(shortDefinition = "s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition = "The units of time for the period in UCUM units.")
        protected BoundCodeDt<UnitsOfTimeEnum> ourPeriodUnits;
        public static final String FIELD_PERIODUNITS = "periodUnits";
        /**
         * when
         */
        @Child(name = FIELD_WHEN, min = 0, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {CodeDt.class})
        @Description(shortDefinition = "Regular life events the event is tied to", formalDefinition = "A real world event that the occurrence of the event should be tied to.")
        protected BoundCodeDt<EventTimingEnum> ourWhen;
        public static final String FIELD_WHEN = "when";

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(ourBounds, ourCount, ourDuration, ourDurationMax, ourDurationUnits, ourFrequency, ourFrequencyMax, ourPeriod, ourPeriodMax, ourPeriodUnits, ourWhen);
        }

        @Override
        public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
            return ElementUtil.allPopulatedChildElements(theType, ourBounds, ourCount, ourDuration, ourDurationMax, ourDurationUnits, ourFrequency, ourFrequencyMax, ourPeriod, ourPeriodMax, ourPeriodUnits, ourWhen);
        }

        public IDatatype _getBounds() {
            return ourBounds;
        }

        public _Repeat _setBounds(IDatatype theValue) {
            ourBounds = theValue;
            return this;
        }

        public IntegerDt _getCount() {
            if (ourCount == null)
                ourCount = new IntegerDt();
            return ourCount;
        }

        public _Repeat _setCount(IntegerDt theValue) {
            ourCount = theValue;
            return this;
        }

        public DecimalDt _getDuration() {
            if (ourDuration == null)
                ourDuration = new DecimalDt();
            return ourDuration;
        }

        public _Repeat _setDuration(DecimalDt theValue) {
            ourDuration = theValue;
            return this;
        }

        public DecimalDt _getDurationMax() {
            if (ourDurationMax == null)
                ourDurationMax = new DecimalDt();
            return ourDurationMax;
        }

        public _Repeat _setDurationMax(DecimalDt theValue) {
            ourDurationMax = theValue;
            return this;
        }

        public BoundCodeDt<UnitsOfTimeEnum> _getDurationUnits() {
            if (ourDurationUnits == null)
                ourDurationUnits = new BoundCodeDt<UnitsOfTimeEnum>(UnitsOfTimeEnum.VALUESET_BINDER);
            return ourDurationUnits;
        }

        public _Repeat _setDurationUnits(BoundCodeDt<UnitsOfTimeEnum> theValue) {
            ourDurationUnits = theValue;
            return this;
        }

        public IntegerDt _getFrequency() {
            if (ourFrequency == null)
                ourFrequency = new IntegerDt();
            return ourFrequency;
        }

        public _Repeat _setFrequency(IntegerDt theValue) {
            ourFrequency = theValue;
            return this;
        }

        public IntegerDt _getFrequencyMax() {
            if (ourFrequencyMax == null)
                ourFrequencyMax = new IntegerDt();
            return ourFrequencyMax;
        }

        public _Repeat _setFrequencyMax(IntegerDt theValue) {
            ourFrequencyMax = theValue;
            return this;
        }

        public DecimalDt _getPeriod() {
            if (ourPeriod == null)
                ourPeriod = new DecimalDt();
            return ourPeriod;
        }

        public _Repeat _setPeriod(DecimalDt theValue) {
            ourPeriod = theValue;
            return this;
        }

        public DecimalDt _getPeriodMax() {
            if (ourPeriodMax == null)
                ourPeriodMax = new DecimalDt();
            return ourPeriodMax;
        }

        public _Repeat _setPeriodMax(DecimalDt theValue) {
            ourPeriodMax = theValue;
            return this;
        }

        public BoundCodeDt<UnitsOfTimeEnum> _getPeriodUnits() {
            if (ourPeriodUnits == null)
                ourPeriodUnits = new BoundCodeDt<UnitsOfTimeEnum>(UnitsOfTimeEnum.VALUESET_BINDER);
            return ourPeriodUnits;
        }

        public _Repeat _setPeriodUnits(BoundCodeDt<UnitsOfTimeEnum> theValue) {
            ourPeriodUnits = theValue;
            return this;
        }

        public BoundCodeDt<EventTimingEnum> _getWhen() {
            if (ourWhen == null)
                ourWhen = new BoundCodeDt<EventTimingEnum>(EventTimingEnum.VALUESET_BINDER);
            return ourWhen;
        }

        public _Repeat _setWhen(BoundCodeDt<EventTimingEnum> theValue) {
            ourWhen = theValue;
            return this;
        }

        @Override
        @Deprecated
        public IDatatype getBounds() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setBounds(IDatatype p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setCount(IntegerDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setCount(int p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDuration(DecimalDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDuration(double p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDuration(BigDecimal p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDuration(long p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationMax(DecimalDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationMax(double p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationMax(BigDecimal p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationMax(long p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationUnits(UnitsOfTimeEnum p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setDurationUnits(BoundCodeDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setFrequency(IntegerDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setFrequency(int p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setFrequencyMax(IntegerDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setFrequencyMax(int p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriod(DecimalDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriod(double p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriod(BigDecimal p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriod(long p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodMax(DecimalDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodMax(double p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodMax(BigDecimal p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodMax(long p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodUnits(UnitsOfTimeEnum p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setPeriodUnits(BoundCodeDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setWhen(EventTimingEnum p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Repeat setWhen(BoundCodeDt p0) {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BoundCodeDt getDurationUnitsElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BoundCodeDt getPeriodUnitsElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BoundCodeDt getWhenElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public DecimalDt getDurationElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public DecimalDt getDurationMaxElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public DecimalDt getPeriodElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public DecimalDt getPeriodMaxElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public IntegerDt getCountElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public IntegerDt getFrequencyElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public IntegerDt getFrequencyMaxElement() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Integer getCount() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Integer getFrequency() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public Integer getFrequencyMax() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public String getDurationUnits() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public String getPeriodUnits() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public String getWhen() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BigDecimal getDuration() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BigDecimal getDurationMax() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BigDecimal getPeriod() {
            throw new UnsupportedOperationException("Deprecated method");
        }

        @Override
        @Deprecated
        public BigDecimal getPeriodMax() {
            throw new UnsupportedOperationException("Deprecated method");
        }
    }
}

