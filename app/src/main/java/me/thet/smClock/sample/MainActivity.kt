package me.thet.smClock.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.thet.smClock.HourMin
import me.thet.smclock.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(300)
            smClock.setDayBreakAndNightFallHourMin(HourMin(5, 30), HourMin(18, 37))
                .start()
        }
    }
}
