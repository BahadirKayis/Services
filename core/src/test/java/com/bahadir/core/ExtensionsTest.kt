package com.bahadir.core


import org.junit.Test
import java.util.Calendar

class ExtensionsTest {

    @Test
    fun `time of use of applications`() {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis / 1000
        calendar.add(Calendar.MINUTE, -25)
        val startTime = calendar.timeInMillis / 1000

        val difference = (endTime - startTime)
        if (difference < 120) {
            println(1)
        } else {
            val minutes = (difference / 60).toInt()
            println("Else :$minutes")
        }


    }
}