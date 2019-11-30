package com.gmail.thetpaingtun93.myanmarprayertime

import com.gmail.thetpaingtun93.myanmarprayertime.newMain.sunPosition.HourMin
import com.gmail.thetpaingtun93.myanmarprayertime.newMain.sunPosition.SunPositionCalculator
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class SunPositionCalculatorTest {

    private lateinit var cal: SunPositionCalculator

    @Before
    fun setUp() {
        //dayPeriod => 13 hours
        //nightPeriod => 11 hours
        cal = SunPositionCalculator(HourMin(5, 30), HourMin(18, 30))
    }

    @Test
    fun test() {
        assertTrue(true)
    }

    @Test
    fun test_degreePerMinute() {

        assertEquals(0.23076923f, cal.mDayDegreePerMin, 0.0001f)
        assertEquals(0.27272728f, cal.mNightDegreePerMin, 0.0001f)
    }

    @Test
    fun test_getCurrentAngle() {


        val a1 = cal.getCurrentAngle(HourMin(11, 17))
        val a2 = cal.getCurrentAngle(HourMin(14, 52))
        val a3 = cal.getCurrentAngle(HourMin(21, 3))
        val a4 = cal.getCurrentAngle(HourMin(3, 43))
        val a5 = cal.getCurrentAngle(HourMin(5, 25))


        assertEquals(80.0769f, a1, 0.01f)
        assertEquals(129.6923f, a2, 0.01f)
        assertEquals(221.7272384f, a3, 0.01f)
        assertEquals(330.81818584f, a4, 0.01f)
        assertEquals(358.6363684f, a5, 0.01f)
    }


    @Test
    fun test_getCurrentDegreePerMin() {
        val day = 0.23076923f
        val night = 0.27272728f

        assertEquals(cal.getCurrentDegreePerMin(HourMin(18, 30)), night)
        assertEquals(cal.getCurrentDegreePerMin(HourMin(18, 29)), day)
        assertEquals(cal.getCurrentDegreePerMin(HourMin(5, 30)), day)
        assertEquals(cal.getCurrentDegreePerMin(HourMin(5, 29)), night)

    }

    @Test
    fun test_getCurrentPeriod() {
        assertEquals(cal.getCurrentPeriod(HourMin(18, 30)), SunPositionCalculator.PERIOD.NIGHT)
        assertEquals(cal.getCurrentPeriod(HourMin(18, 29)), SunPositionCalculator.PERIOD.DAY)
        assertEquals(cal.getCurrentPeriod(HourMin(5, 30)), SunPositionCalculator.PERIOD.DAY)
        assertEquals(cal.getCurrentPeriod(HourMin(5, 29)), SunPositionCalculator.PERIOD.NIGHT)
    }
}