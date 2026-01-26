import {
    CarbonEquivalent,
    CarbonLog,
    CarbonMeasurement,
} from "carbonlog-model";

import {describe, expect, it} from 'vitest'

describe('Carbon Model Integration Tests', () => {
    describe('CarbonMeasurement and CarbonEquivalent', () => {
        it('should work', () => {
            const carbonEq = CarbonEquivalent.Companion.ofPlaneKm(2000);
            const measurement = CarbonMeasurement.Companion.ofCarbonEquivalent(carbonEq);

            expect(measurement.co2Kg).closeTo(371.75, 0.01);

            expect(measurement.asCarbonEquivalent().planeKm()).closeTo(2000, 0.01);
        });
    });

    describe('CarbonLog', () => {
        //     private val date1: LocalDate = LocalDate.parse("2022-01-01")
        const date1 = new Date("2022-01-01T00:00:00.000Z")
        const date2 = new Date("2022-02-01T00:00:00.000Z")
        const date3 = new Date("2022-03-01T00:00:00.000Z")
        const date4 = new Date("2022-04-01T00:00:00.000Z")
        const date_2021 = new Date("2021-01-04T00:00:00.000Z")
        //     private val date2: LocalDate = LocalDate.parse("2022-01-02")
        //     private val date3: LocalDate = LocalDate.parse("2022-01-03")
        //     private val date4: LocalDate = LocalDate.parse("2022-01-04")
        //     private val date_2021: LocalDate = LocalDate.parse("2021-01-04")
        // @Test
        //     fun `should get results when in range`() {
        //         // given
        //         val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDayIn(TimeZone.UTC))
        //         val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDayIn(TimeZone.UTC))
        //         val log = CarbonLog(listOf(cm2, cm3))
        //
        //         // when
        //         val result = log.getRangeCarbonKgs(
        //             date1.atStartOfDayIn(TimeZone.UTC),
        //             date4.atStartOfDayIn(TimeZone.UTC)
        //         )
        //
        //         // then
        //         assertEquals(result, 5.0)
        //     }
        it('should get results when in range', () => {
            const cm2 = new CarbonMeasurement('cm2', 2.0, date2)
            const cm3 = new CarbonMeasurement('cm3', 3.0, date3)
            // from
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(date1, date2)
        });
    })
});
