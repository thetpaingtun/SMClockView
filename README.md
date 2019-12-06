# SMClockView
Clock that show current sun or moon position

<img src="sm.gif" height="550" width="300"/>

## Installation

Include the following dependency build.gradle file

```
implementation 'me.thet.smClock:SMClockView:$version'
```

## Usage 

Add `SMClockView` widget in your view 

```
<me.thet.smClock.SMClockView
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

## Customization

| No. | XML Attribute | Default Value |
|:---|:---|:---|
| 1 |sm_sun_icon | default sun icon |
| 2 |sm_moon_icon | default moon icon |
| 3 |sm_icon_size | adaptive to the widget size |
| 4 |sm_small_circle_color |black |
| 5 |sm_dash_line_color | transparent black|
| 6 |sm_large_circle_color |black|
| 7 |sm_clock_sub_label ||
| 8 |sm_clock_sub_label_color |black|
| 9 |sm_clock_color |black|
| 10 |sm_day_break_hour |5|
| 10 |sm_day_break_min |30|
| 11 |sm_night_fall_hour |18|
| 11 |sm_night_fall_min |30|
| 12 |sm_total_animation_duration |2000ms|


You can also change the daybreak and night fall hour dynamically like:

```
smClock.setDayBreakAndNightFallHourMin(HourMin(5, 30), HourMin(21, 30))
```
## License

MIT License

Copyright (c) 2019 thetpaingtun

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.





