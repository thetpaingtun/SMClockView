package me.thet.smClock


class SunPositionCalculator(private val dayBreak: HourMin, private val nightFall: HourMin) {

    enum class PERIOD {
        DAY,
        NIGHT
    }

    var nightPeriod: HourMin
        private set
    var dayPeriod: HourMin
        private set

    var mNightDegreePerMin: Float
        private set
    var mDayDegreePerMin: Float
        private set

    init {


        dayPeriod = (nightFall - dayBreak)
        nightPeriod = dayPeriod.invert()

        mDayDegreePerMin = 180f / dayPeriod.toMinutes()
        mNightDegreePerMin = 180f / nightPeriod.toMinutes()
    }


    /**
     *
     * @param curHourMin the current HourMin
     * @return the the sun/moon position in degree by using the current hour and min of the day
     */
    fun getCurrentAngle(curHourMin: HourMin): Float {
        var degree: Float

        when (getCurrentPeriod(curHourMin)) {
            PERIOD.DAY -> {
                degree = (curHourMin - dayBreak).toMinutes() * mDayDegreePerMin
            }

            PERIOD.NIGHT -> {
                degree = (curHourMin - nightFall).toMinutes() * mNightDegreePerMin

                //for the whole day
                degree = degree + 180
            }
        }

        return degree

    }

    /**
     * @param the current HourMin
     * @return dayDegreePerMin if its day, otherwise return nightDegreePerMin
     */
    fun getCurrentDegreePerMin(curHourMin: HourMin): Float {
        return when (getCurrentPeriod(curHourMin)) {
            PERIOD.DAY -> mDayDegreePerMin
            PERIOD.NIGHT -> mNightDegreePerMin
        }
    }


    /**
     * @param the current HourMin
     *@return the current period
     */
    fun getCurrentPeriod(curHourMin: HourMin): PERIOD {
        if (curHourMin >= nightFall || curHourMin < dayBreak) {
            //its night
            return PERIOD.NIGHT
        }
        return PERIOD.DAY
    }

}