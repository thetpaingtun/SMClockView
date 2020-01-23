package com.gmail.thetpaingtun93.myanmarprayertime

import me.thet.smClock.HourMin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HourMinTest {
    @Test
    fun test_comparable() {
        val d1 = HourMin(5, 30)
        val d2 = HourMin(5, 29)
        val d3 = HourMin(0, 30)
        val d4 = HourMin(15, 25)


        assertTrue(d1 > d2)
        assertTrue(d3 < d2)
        assertTrue(d4 > d1)

    }

    @Test
    fun test_toMinutes() {
        val v1 = HourMin(1, 30)
        val v2 = HourMin(18, 15)

        assertEquals(90, v1.toMinutes())
        assertEquals(1095, v2.toMinutes())
    }


    @Test
    fun test_diff() {
        val v1 = HourMin(5, 30)
        val v2 = HourMin(18, 15)
        val v3 = HourMin(1, 37)
        val v4 = HourMin(5, 14)


        assertEquals(765, (v2 - v1).toMinutes())
        assertEquals(1207, (v3 - v1).toMinutes())
        assertEquals(HourMin(23, 44), v4 - v1)
        assertEquals(HourMin(0, 16), v1 - v4)

    }

    @Test
    fun test_toHourMin() {
        val min1 = 1439
        val min2 = 60
        val min3 = 926

        assertEquals(HourMin(23, 59), HourMin.toHourMin(min1))
        assertEquals(HourMin(1, 0), HourMin.toHourMin(min2))
        assertEquals(HourMin(15, 26), HourMin.toHourMin(min3))

    }

    @Test
    fun test_invert() {

        assertEquals(HourMin(11, 0), HourMin(13, 0).invert())
        assertEquals(HourMin(20, 13), HourMin(3, 47).invert())
    }

    @Test
    fun test_formatIn12Hour() {

        val d1 = HourMin(12, 30)
        val d2 = HourMin(0, 29)
        val d3 = HourMin(16, 34)
        val d4 = HourMin(23, 22)
        val d5 = HourMin(3, 37)
        val d6 = HourMin(3, 7)



        assertEquals("12:30 PM", d1.formatIn12Hour())
        assertEquals("12:29 AM", d2.formatIn12Hour())
        assertEquals("04:34 PM", d3.formatIn12Hour())
        assertEquals("11:22 PM", d4.formatIn12Hour())
        assertEquals("03:37 AM", d5.formatIn12Hour())
        assertEquals("03:07 AM", d6.formatIn12Hour())


    }
}