library Participation_FHIR version '1.0.0'

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers

valueset "NCQA Payer": 'http://ncqa.org/fhir/hedis/ValueSet/ncqa-payer' //'urn:oid:2.16.840.1.113762.1.4.1165.42'
valueset "Commercial": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.46'
valueset "Medicaid": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.45'
valueset "Medicare": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.44'

context Patient

define "Medicaid Product":
	'medicaid'

define "Commercial Product":
	'commercial'

define "Medicare Product":
	'medicare'

/*
  Report in the product line based on which product they are enrolled in at a given date
	(generally the end of the measurement period) but continuous enrollment calculations
	should consider all participation periods, regardless of product

	Input parameters:
		Product Line: The product line being reported
		Index Date: Generally measurement period, but could be tied to an event
		Participation Period: Generally one year, two years, or twenty seven months, but could be based on an event
		Allowed Gap Days: Generally 45 days, but could be 0

	Example 1 - One year continuously enrolled
		"Is Enrolled"(ProductLine, end of "Measurement Period", "Measurement Period", 45)

	Example 2 - Two years continuously enrolled
	  "Is Enrolled"(ProductLine, end of "Measurement Period", Interval[start of "Measurement Period" - 1 year, end of "Measurement Period"], 45)

	Example 3 - Twenty seven months continuously enrolled
		"Is Enrolled"(ProductLine, end of "Measurement Period", Interval[start of "Measurement Period" - 2 years - 3 months, end of "Measurement Period"], 45)

	Example 4 - Continuously enrolled surrounding an index event such as a heart attack
		"Is Enrolled"(ProductLine, "Date of Heart Attack", Interval["Date of Heart Attack", "Date of Heart Attack" + 18 months], 45)

	Example 5 - Continuously enrolled with no gaps during the 9 months prior to a birth and for the following year, allowing for a gap
	  "Is Enrolled"(ProductLine, "Date Of Delivery", Interval["Date Of Delivery" - 9 months, "Date Of Delivery"], 0)
		  and "Is Enrolled"(ProductLine, "Date Of Delivery", Interval["Date Of Delivery", "Date Of Delivery" + 1 year], 45)
*/
define function "Is Enrolled"(ProductLine String, IndexDate DateTime, ParticipationPeriod Interval<DateTime>, AllowedGapDays Integer):
	case
		/* If this is not running for a specific product line, ignore enrollment considerations */
		when ProductLine is null then true
		else
			/* Is the patient enrolled in the given product line on the given index date? */
			"Is Enrolled On Date"(ProductLine, IndexDate)
			/* Is the patient continuously enrolled in every year of the participation period */
			  and AllTrue(
						("Enrollment Periods"(ParticipationPeriod)) EnrollmentPeriod
							return "Is Continuously Enrolled In Period"(EnrollmentPeriod, if duration in months of EnrollmentPeriod >= 6 then AllowedGapDays else 0)
					)
	end

/*
Returns all Commercial, Medicaid, and Medicare participation for the patient
*/
define "Participation":
  ["Coverage": type in "Commercial"]
	  union ["Coverage": type in "Medicaid"]
		union ["Coverage": type in "Medicare"]

define function "Is Enrolled On Date"(ProductLine String, IndexDate DateTime):
  exists (
		(
			case ProductLine
			  when "Commercial Product" then ["Coverage": type in "Commercial"]
				when "Medicare Product" then ["Coverage": type in "Medicare"]
				when "Medicaid Product" then ["Coverage": type in "Medicaid"]
				else null
			end
		) P
			where IndexDate during P.period
	)

/*
Returns enrollment periods of up to a year ending at the end of the participation period.
Note that this will not return more than 3 years (would need the expand from 1.3 to support more generally)

For example, given the following participation period:

        10/1/16 1/1/17       1/1/18      12/31/18
            [---||------------||------------]

The function will return the following enrollment periods:

            [---][------------][------------]

In values:
Participation Period: Interval[@2016-10-01, @2018-12-31]
Enrollment Periods: { Interval[@2016-10-01, @2016-12-31], Interval[@2017-01-01, @2017-12-31] Interval[@2018-01-01, @2018-12-31] }
*/
define function "Enrollment Periods"(ParticipationPeriod Interval<DateTime>):
  ( { 3 years, 2 years, 1 year } ) Year
	  where end of ParticipationPeriod - (Year - 1 year) after start of ParticipationPeriod
	  return
		  Interval[
		    Max({ successor of (end of ParticipationPeriod - Year), start of ParticipationPeriod }),
				end of ParticipationPeriod - (Year - 1 year)
			]

/*
//Tests:
define TestTwentySevenMonthParticipationPeriod: Interval[@2016-10-01, @2018-12-31]
define TestExpectedTwentySevenMonthEnrollmentPeriods: { Interval[@2016-10-01, @2016-12-31], Interval[@2017-01-01, @2017-12-31], Interval[@2018-01-01, @2018-12-31] }
define TestTwentySevenMonthEnrollmentPeriods: "Enrollment Periods"(TestTwentySevenMonthParticipationPeriod)
define TestTwentySevenMonthPass: TestTwentySevenMonthEnrollmentPeriods = TestExpectedTwentySevenMonthEnrollmentPeriods

define TestThreeYearParticipationPeriod: Interval[@2016-01-01, @2018-12-31]
define TestExpectedThreeYearEnrollmentPeriods: { Interval[@2016-01-01, @2016-12-31], Interval[@2017-01-01, @2017-12-31], Interval[@2018-01-01, @2018-12-31] }
define TestThreeYearEnrollmentPeriods: "Enrollment Periods"(TestThreeYearParticipationPeriod)
define TestThreeYearPass: TestThreeYearEnrollmentPeriods = TestExpectedThreeYearEnrollmentPeriods

define TestTwoYearParticipationPeriod: Interval[@2017-01-01, @2018-12-31]
define TestExpectedTwoYearEnrollmentPeriods: { Interval[@2017-01-01, @2017-12-31], Interval[@2018-01-01, @2018-12-31] }
define TestTwoYearEnrollmentPeriods: "Enrollment Periods"(TestTwoYearParticipationPeriod)
define TestTwoYearPass: TestTwoYearEnrollmentPeriods = TestExpectedTwoYearEnrollmentPeriods

define TestOneYearParticipationPeriod: Interval[@2018-01-01, @2018-12-31]
define TestExpectedOneYearEnrollmentPeriods: { Interval[@2018-01-01, @2018-12-31] }
define TestOneYearEnrollmentPeriods: "Enrollment Periods"(TestOneYearParticipationPeriod)
define TestOneYearPass: TestOneYearEnrollmentPeriods = TestExpectedOneYearEnrollmentPeriods

define TestNineMonthParticipationPeriod: Interval[@2018-03-01, @2018-12-31]
define TestExpectedNineMonthEnrollmentPeriods: { Interval[@2018-03-01, @2018-12-31] }
define TestNineMonthEnrollmentPeriods: "Enrollment Periods"(TestNineMonthParticipationPeriod)
define TestNineMonthPass: TestNineMonthEnrollmentPeriods = TestExpectedNineMonthEnrollmentPeriods
*/

/*
Returns the periods during which the patient had coverage during the given participation period
*/
define function "Participation In Period"(ParticipationPeriod Interval<DateTime>):
	collapse (
		Participation P
			where P.period overlaps ParticipationPeriod
			return all P.period intersect ParticipationPeriod
	)

/*
Returns the number of gap days in the given coverage periods, allowing only one gap.
If there is more than one gap, returns the maximum Integer
*/
define function "Gap Days In Period"(ParticipationPeriod Interval<DateTime>, Periods List<Interval<DateTime>>):
		case Count(Periods)
			when 1 then
			  if Periods[0] starts day of ParticipationPeriod then
				  difference in days between end of Periods[0] and end of ParticipationPeriod
			  else if Periods[0] ends day of ParticipationPeriod then
				  difference in days between start of ParticipationPeriod and start of Periods[0]
			  else
				  maximum Integer
			when 2 then
			  if Periods[0] starts day of ParticipationPeriod and Periods[1] ends day of ParticipationPeriod then
				  difference in days between end of Periods[0] and start of Periods[1]
			  else
				  maximum Integer
			else
			  maximum Integer
		end

/*
//Tests:
define TestParticipationPeriod: Interval[@2017-01-01, @2017-12-31]
define Test1CoveragePeriods: { Interval[@2017-01-01, @2017-12-31] }
define Test2CoveragePeriods: { Interval[@2017-01-01, @2017-12-15] }
define Test3CoveragePeriods: { Interval[@2017-01-15, @2017-12-31] }
define Test4CoveragePeriods: { Interval[@2017-01-01, @2017-11-15] }
define Test5CoveragePeriods: { Interval[@2017-02-15, @2017-12-31] }
define Test6CoveragePeriods: { Interval[@2017-01-01, @2017-10-15] }
define Test7CoveragePeriods: { Interval[@2017-03-15, @2017-12-31] }
define Test8CoveragePeriods: { Interval[@2017-01-15, @2017-11-15] }
define Test9CoveragePeriods: { Interval[@2017-01-01, @2017-01-14], Interval[@2017-02-15, @2017-12-31] }
define Test10CoveragePeriods: { Interval[@2017-01-01, @2017-01-14], Interval[@2017-03-15, @2017-12-31] }
define Test11CoveragePeriods: { Interval[@2017-01-01, @2017-01-14], Interval[@2017-02-15, @2017-06-14], Interval[@2017-07-15, @2017-12-31] }
define Test1CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test1CoveragePeriods)
define Test2CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test2CoveragePeriods)
define Test3CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test3CoveragePeriods)
define Test4CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test4CoveragePeriods)
define Test5CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test5CoveragePeriods)
define Test6CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test6CoveragePeriods)
define Test7CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test7CoveragePeriods)
define Test8CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test8CoveragePeriods)
define Test9CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test9CoveragePeriods)
define Test10CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test10CoveragePeriods)
define Test11CoverageGap: "Gap Days In Period"(TestParticipationPeriod, Test11CoveragePeriods)
define Test1ExpectedGap: 0
define Test2ExpectedGap: 16
define Test3ExpectedGap: 14
define Test4ExpectedGap: 46
define Test5ExpectedGap: 45
define Test6ExpectedGap: 77
define Test7ExpectedGap: 73
define Test8ExpectedGap: maximum Integer
define Test9ExpectedGap: 32
define Test10ExpectedGap: 60
define Test11ExpectedGap: maximum Integer
*/

define function "Is Continuously Enrolled In Period"(EnrollmentPeriod Interval<DateTime>, AllowedGapDays Integer):
	"Gap Days In Period"(EnrollmentPeriod, "Participation In Period"(EnrollmentPeriod)) <= AllowedGapDays
