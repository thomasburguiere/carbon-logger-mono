import {
    asCarbonEquivalent,
    CarbonEquivalent,
    CarbonLog,
    CarbonMeasurement,
    CarbonMeasurementBuilder,
} from "carbonlog-model";

import {describe, expect, it} from 'vitest'

describe('Carbon Model Integration Tests', () => {
    describe('CarbonMeasurement and CarbonEquivalent', () => {
        it('should work', () => {
            const carbonEq = CarbonEquivalent.Companion.ofPlaneKm(2000);
            const measurement = CarbonMeasurement.Companion.ofCarbonEquivalent(carbonEq);

            expect(measurement.co2Kg).closeTo(371.75, 0.01);

            expect(asCarbonEquivalent(measurement).planeKm()).closeTo(2000, 0.01);
        });

        describe('CarbonMeasurementBuilder', () => {

            it('should fail when missing co2Kg', () => {
                expect(() => new CarbonMeasurementBuilder().id("id").build()).toThrow("builder.co2Kg cannot be null")
            })
            it('should build with default id and dtIso', () => {
                const dateIsoBefore = (new Date()).toISOString()
                const ms: CarbonMeasurement = new CarbonMeasurementBuilder().co2Kg(1).build()
                const dateIsoAfter = (new Date()).toISOString()

                expect(ms.id).not.toBeFalsy()
                expect(ms.id.length).toBeGreaterThan(0)

                expect(ms.dtIso).not.toBeFalsy()
                expect(ms.dtIso.length).toBeGreaterThan(0)
                expect(ms.dtIso >= dateIsoBefore).toBe(true)
                expect(ms.dtIso <= dateIsoAfter).toBe(true)
            });
        })
    });

    describe('CarbonLog', () => {
        const date1Iso = "2022-01-01T00:00:00.000Z";
        const date2Iso = "2022-02-01T00:00:00.000Z";
        const date3Iso = "2022-03-01T00:00:00.000Z";
        const date4Iso = "2022-04-01T00:00:00.000Z";
        const date_2021_iso = "2021-01-04T00:00:00.000Z";

        it('should get results when in range', () => {

            const cm2: CarbonMeasurement = new CarbonMeasurementBuilder().id("cm2").co2Kg(2.0).dtIso(date2Iso).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date3Iso).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(date1Iso, date4Iso)

            // then
            expect(result).equals(5.0)
        });

        it('should get results when in range with added data', () => {
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(date2Iso).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date3Iso).build()

            let log = new CarbonLog([cm2, cm3])

            const addedCm = new CarbonMeasurementBuilder().id('added').co2Kg(11.0).dtIso(date2Iso).build()

            log = log.copyAdding(addedCm)

            // when
            const result = log.getRangeCarbonKgs(date1Iso, date4Iso)

            // then
            expect(result).equals(16.0)
        });

        it(`should get nothing when outside range`, () => {
            // given
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(date2Iso).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date3Iso).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(
                date4Iso,
                date4Iso
            )

            // then
            expect(result).equals(0.0)
        });

        it(`should get nothing when search range exclusive`, () => {
            // given
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(date2Iso).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date3Iso).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(
                date3Iso,
                date4Iso,
            )

            // then
            expect(result).equals(0.0)
        });

        it(`should get result when search range inclusive`, () => {
            // given
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(date2Iso).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date3Iso).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(
                date3Iso,
                date4Iso,
                true
            )

            // then
            expect(result).equals(3.0)
        });

        it(`should get result for current year`, () => {
            // given
            const todayDate = (new Date()).toISOString()
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(todayDate).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(date_2021_iso).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getCurrentYearCarbonKgs()

            // then
            expect(result).equals(cm2.co2Kg)
        });

        it(`should get total CO2 Kg`, () => {
            // given
            const todayDate = (new Date()).toISOString()
            const cm2 = new CarbonMeasurementBuilder().id('cm2').co2Kg(2.0).dtIso(todayDate).build()
            const cm3 = new CarbonMeasurementBuilder().id('cm3').co2Kg(3.0).dtIso(todayDate).build()
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getTotalCarbonKgs()

            // then
            expect(result).equals(5.0)
        });
    });
});
