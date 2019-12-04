package me.thet.smClock.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import me.thet.smClock.HourMin
import me.thet.smclock.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        smClock.setDayBreakAndNightFallHourMin(HourMin(5, 30), HourMin(21, 30))
    }

}
