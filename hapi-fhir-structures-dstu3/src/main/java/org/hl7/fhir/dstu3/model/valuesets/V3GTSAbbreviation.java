package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3GTSAbbreviation {

        /**
         * Every morning at institution specified times.
         */
        AM, 
        /**
         * Two times a day at institution specified time
         */
        BID, 
        /**
         * Regular business days (Monday to Friday excluding holidays)
         */
        JB, 
        /**
         * Regular weekends (Saturday and Sunday excluding holidays)
         */
        JE, 
        /**
         * Holidays
         */
        JH, 
        /**
         * Christian Holidays (Roman/Gregorian [Western] Tradition.)
         */
        _GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN, 
        /**
         * Easter Sunday.  The Easter date is a rather complex calculation based on Astronomical tables describing full moon dates.  Details can be found at [http://www.assa.org.au/edm.html, and http://aa.usno.navy.mil/AA/faq/docs/easter.html].  Note that the Christian Orthodox Holidays are based on the Julian calendar.
         */
        JHCHREAS, 
        /**
         * Good Friday, is the Friday right before Easter Sunday.
         */
        JHCHRGFR, 
        /**
         * New Year's Day (January 1)
         */
        JHCHRNEW, 
        /**
         * Pentecost Sunday, is seven weeks after Easter (the 50th day of Easter).
         */
        JHCHRPEN, 
        /**
         * Christmas Eve (December 24)
         */
        JHCHRXME, 
        /**
         * Christmas Day (December 25)
         */
        JHCHRXMS, 
        /**
         * Description:The Netherlands National Holidays.
         */
        JHNNL, 
        /**
         * Description:Liberation day  (May 5 every five years)
         */
        JHNNLLD, 
        /**
         * Description:Queen's day (April 30)
         */
        JHNNLQD, 
        /**
         * Description:Sinterklaas (December 5)
         */
        JHNNLSK, 
        /**
         * United States National Holidays (public holidays for federal employees established by U.S. Federal law 5 U.S.C. 6103).
         */
        JHNUS, 
        /**
         * Columbus Day, the second Monday in October.
         */
        JHNUSCLM, 
        /**
         * Independence Day (4th of July)
         */
        JHNUSIND, 
        /**
         * Alternative Monday after 4th of July Weekend [5 U.S.C. 6103(b)].
         */
        JHNUSIND1, 
        /**
         * Alternative Friday before 4th of July Weekend [5 U.S.C. 6103(b)].
         */
        JHNUSIND5, 
        /**
         * Labor Day, the first Monday in September.
         */
        JHNUSLBR, 
        /**
         * Memorial Day, the last Monday in May.
         */
        JHNUSMEM, 
        /**
         * Friday before Memorial Day Weekend
         */
        JHNUSMEM5, 
        /**
         * Saturday of Memorial Day Weekend
         */
        JHNUSMEM6, 
        /**
         * Dr. Martin Luther King, Jr. Day, the third Monday in January.
         */
        JHNUSMLK, 
        /**
         * Washington's Birthday (Presidential Day) the third Monday in February.
         */
        JHNUSPRE, 
        /**
         * Thanksgiving Day, the fourth Thursday in November.
         */
        JHNUSTKS, 
        /**
         * Friday after Thanksgiving.
         */
        JHNUSTKS5, 
        /**
         * Veteran's Day, November 11.
         */
        JHNUSVET, 
        /**
         * Every afternoon at institution specified times.
         */
        PM, 
        /**
         * Four times a day at institution specified time
         */
        QID, 
        /**
         * Three times a day at institution specified time
         */
        TID, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3GTSAbbreviation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AM".equals(codeString))
          return AM;
        if ("BID".equals(codeString))
          return BID;
        if ("JB".equals(codeString))
          return JB;
        if ("JE".equals(codeString))
          return JE;
        if ("JH".equals(codeString))
          return JH;
        if ("_GTSAbbreviationHolidaysChristianRoman".equals(codeString))
          return _GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN;
        if ("JHCHREAS".equals(codeString))
          return JHCHREAS;
        if ("JHCHRGFR".equals(codeString))
          return JHCHRGFR;
        if ("JHCHRNEW".equals(codeString))
          return JHCHRNEW;
        if ("JHCHRPEN".equals(codeString))
          return JHCHRPEN;
        if ("JHCHRXME".equals(codeString))
          return JHCHRXME;
        if ("JHCHRXMS".equals(codeString))
          return JHCHRXMS;
        if ("JHNNL".equals(codeString))
          return JHNNL;
        if ("JHNNLLD".equals(codeString))
          return JHNNLLD;
        if ("JHNNLQD".equals(codeString))
          return JHNNLQD;
        if ("JHNNLSK".equals(codeString))
          return JHNNLSK;
        if ("JHNUS".equals(codeString))
          return JHNUS;
        if ("JHNUSCLM".equals(codeString))
          return JHNUSCLM;
        if ("JHNUSIND".equals(codeString))
          return JHNUSIND;
        if ("JHNUSIND1".equals(codeString))
          return JHNUSIND1;
        if ("JHNUSIND5".equals(codeString))
          return JHNUSIND5;
        if ("JHNUSLBR".equals(codeString))
          return JHNUSLBR;
        if ("JHNUSMEM".equals(codeString))
          return JHNUSMEM;
        if ("JHNUSMEM5".equals(codeString))
          return JHNUSMEM5;
        if ("JHNUSMEM6".equals(codeString))
          return JHNUSMEM6;
        if ("JHNUSMLK".equals(codeString))
          return JHNUSMLK;
        if ("JHNUSPRE".equals(codeString))
          return JHNUSPRE;
        if ("JHNUSTKS".equals(codeString))
          return JHNUSTKS;
        if ("JHNUSTKS5".equals(codeString))
          return JHNUSTKS5;
        if ("JHNUSVET".equals(codeString))
          return JHNUSVET;
        if ("PM".equals(codeString))
          return PM;
        if ("QID".equals(codeString))
          return QID;
        if ("TID".equals(codeString))
          return TID;
        throw new FHIRException("Unknown V3GTSAbbreviation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AM: return "AM";
            case BID: return "BID";
            case JB: return "JB";
            case JE: return "JE";
            case JH: return "JH";
            case _GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN: return "_GTSAbbreviationHolidaysChristianRoman";
            case JHCHREAS: return "JHCHREAS";
            case JHCHRGFR: return "JHCHRGFR";
            case JHCHRNEW: return "JHCHRNEW";
            case JHCHRPEN: return "JHCHRPEN";
            case JHCHRXME: return "JHCHRXME";
            case JHCHRXMS: return "JHCHRXMS";
            case JHNNL: return "JHNNL";
            case JHNNLLD: return "JHNNLLD";
            case JHNNLQD: return "JHNNLQD";
            case JHNNLSK: return "JHNNLSK";
            case JHNUS: return "JHNUS";
            case JHNUSCLM: return "JHNUSCLM";
            case JHNUSIND: return "JHNUSIND";
            case JHNUSIND1: return "JHNUSIND1";
            case JHNUSIND5: return "JHNUSIND5";
            case JHNUSLBR: return "JHNUSLBR";
            case JHNUSMEM: return "JHNUSMEM";
            case JHNUSMEM5: return "JHNUSMEM5";
            case JHNUSMEM6: return "JHNUSMEM6";
            case JHNUSMLK: return "JHNUSMLK";
            case JHNUSPRE: return "JHNUSPRE";
            case JHNUSTKS: return "JHNUSTKS";
            case JHNUSTKS5: return "JHNUSTKS5";
            case JHNUSVET: return "JHNUSVET";
            case PM: return "PM";
            case QID: return "QID";
            case TID: return "TID";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/GTSAbbreviation";
        }
        public String getDefinition() {
          switch (this) {
            case AM: return "Every morning at institution specified times.";
            case BID: return "Two times a day at institution specified time";
            case JB: return "Regular business days (Monday to Friday excluding holidays)";
            case JE: return "Regular weekends (Saturday and Sunday excluding holidays)";
            case JH: return "Holidays";
            case _GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN: return "Christian Holidays (Roman/Gregorian [Western] Tradition.)";
            case JHCHREAS: return "Easter Sunday.  The Easter date is a rather complex calculation based on Astronomical tables describing full moon dates.  Details can be found at [http://www.assa.org.au/edm.html, and http://aa.usno.navy.mil/AA/faq/docs/easter.html].  Note that the Christian Orthodox Holidays are based on the Julian calendar.";
            case JHCHRGFR: return "Good Friday, is the Friday right before Easter Sunday.";
            case JHCHRNEW: return "New Year's Day (January 1)";
            case JHCHRPEN: return "Pentecost Sunday, is seven weeks after Easter (the 50th day of Easter).";
            case JHCHRXME: return "Christmas Eve (December 24)";
            case JHCHRXMS: return "Christmas Day (December 25)";
            case JHNNL: return "Description:The Netherlands National Holidays.";
            case JHNNLLD: return "Description:Liberation day  (May 5 every five years)";
            case JHNNLQD: return "Description:Queen's day (April 30)";
            case JHNNLSK: return "Description:Sinterklaas (December 5)";
            case JHNUS: return "United States National Holidays (public holidays for federal employees established by U.S. Federal law 5 U.S.C. 6103).";
            case JHNUSCLM: return "Columbus Day, the second Monday in October.";
            case JHNUSIND: return "Independence Day (4th of July)";
            case JHNUSIND1: return "Alternative Monday after 4th of July Weekend [5 U.S.C. 6103(b)].";
            case JHNUSIND5: return "Alternative Friday before 4th of July Weekend [5 U.S.C. 6103(b)].";
            case JHNUSLBR: return "Labor Day, the first Monday in September.";
            case JHNUSMEM: return "Memorial Day, the last Monday in May.";
            case JHNUSMEM5: return "Friday before Memorial Day Weekend";
            case JHNUSMEM6: return "Saturday of Memorial Day Weekend";
            case JHNUSMLK: return "Dr. Martin Luther King, Jr. Day, the third Monday in January.";
            case JHNUSPRE: return "Washington's Birthday (Presidential Day) the third Monday in February.";
            case JHNUSTKS: return "Thanksgiving Day, the fourth Thursday in November.";
            case JHNUSTKS5: return "Friday after Thanksgiving.";
            case JHNUSVET: return "Veteran's Day, November 11.";
            case PM: return "Every afternoon at institution specified times.";
            case QID: return "Four times a day at institution specified time";
            case TID: return "Three times a day at institution specified time";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AM: return "AM";
            case BID: return "BID";
            case JB: return "JB";
            case JE: return "JE";
            case JH: return "GTSAbbreviationHolidays";
            case _GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN: return "GTSAbbreviationHolidaysChristianRoman";
            case JHCHREAS: return "JHCHREAS";
            case JHCHRGFR: return "JHCHRGFR";
            case JHCHRNEW: return "JHCHRNEW";
            case JHCHRPEN: return "JHCHRPEN";
            case JHCHRXME: return "JHCHRXME";
            case JHCHRXMS: return "JHCHRXMS";
            case JHNNL: return "The Netherlands National Holidays";
            case JHNNLLD: return "Liberation day (May 5 every five years)";
            case JHNNLQD: return "Queen's day (April 30)";
            case JHNNLSK: return "Sinterklaas (December 5)";
            case JHNUS: return "GTSAbbreviationHolidaysUSNational";
            case JHNUSCLM: return "JHNUSCLM";
            case JHNUSIND: return "JHNUSIND";
            case JHNUSIND1: return "JHNUSIND1";
            case JHNUSIND5: return "JHNUSIND5";
            case JHNUSLBR: return "JHNUSLBR";
            case JHNUSMEM: return "JHNUSMEM";
            case JHNUSMEM5: return "JHNUSMEM5";
            case JHNUSMEM6: return "JHNUSMEM6";
            case JHNUSMLK: return "JHNUSMLK";
            case JHNUSPRE: return "JHNUSPRE";
            case JHNUSTKS: return "JHNUSTKS";
            case JHNUSTKS5: return "JHNUSTKS5";
            case JHNUSVET: return "JHNUSVET";
            case PM: return "PM";
            case QID: return "QID";
            case TID: return "TID";
            default: return "?";
          }
    }


}

