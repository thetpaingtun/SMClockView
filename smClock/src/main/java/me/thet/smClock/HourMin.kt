package me.thet.smClock

import java.util.*

data class HourMin(var hour: Int, var min: Int) : Comparable<HourMin> {

    /**
     * @param other the other HourMin to be compared (00:00 being the smallest and 23:59 is the largest)
     *
     */
    override fun compareTo(other: HourMin): Int {
        return this.toMinutes() - other.toMinutes()
    }


    /**
     * @return the equivalent value in minutes
     */
    fun toMinutes(): Int {
        return (60 * hour) + min
    }


    /**
     *
     *the difference between this (as a end hour) and @param other (the start hour)
     *@param other the start HourMin to find diff
     *@return the difference between this and other HourMin
     */
    operator fun minus(that: HourMin): HourMin {
        var diff: Int = 0

        if (this >= that) {
            //if this 5:45 and that is 5:30
            diff = this.toMinutes() - that.toMinutes()
        } else {
            //if this is 2:20 and that is 5:30
            return HourMin(24, 0) - (that - this)
        }

        return toHourMin(diff)
    }

    /**
     * @return the remaining of 24hour by subtracting this HourMin
     */
    fun invert(): HourMin {
        return HourMin(24, 0) - this
    }


    /**
     * @return format this HourMin(24) to 12-hour format with AM and PM postfix
     * eg . 13:30 -> 1:30PM
     */
    fun formatIn12Hour(): String {
        var postfix = "AM"
        var hour = this.hour
        var min = this.min

        if (hour >= 12) {
            postfix = "PM"
        }

        when {
            hour > 12 -> hour = hour - 12
            hour == 0 -> hour = 12
        }

        var hourStr = hour.toString()
        if (hour < 10) {
            hourStr = "0$hour"
        }

        var minStr = "$min"
        if (min < 10) {
            minStr = "0$min"
        }

        return "$hourStr:$minStr $postfix"

    }

    companion object {
        /**
         * @param min the minute to be converted to HourMin
         * @return the equivalent HourMin
         */
        fun toHourMin(min: Int): HourMin {
            val hour = min / 60
            val min = min % 60

            return HourMin(hour, min)
        }

        /**
         * get the current HourMin
         */
        val currentHourMin: HourMin
            get() {
                val cal = Calendar.getInstance()

                val min = cal.get(Calendar.MINUTE)
                val hour = cal.get(Calendar.HOUR_OF_DAY)

                Logger.log("min => " + min)
                Logger.log("hour => " + hour)
                return HourMin(hour, min)
            }

    }

}