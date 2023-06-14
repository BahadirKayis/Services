package com.bahadir.service.infrastructure

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.PowerManager
import android.util.Log

class CustomAccelerometerEventListener(private val context: Context) :
    SensorEventListener {
    private var turnOnScreen = true

    override fun onSensorChanged(event: SensorEvent) {
        val y = event.values[1]
        val z = event.values[2]
        // X değeri cihazın sağa veya sola eğimini verir
        // Y değeri cihazın yukarı veya aşağı eğimini verir
        // Z değeri cihazın öne veya arkaya eğimini verir

        if (z > HORIZONTAL) {
            turnOnScreen = true
            Log.i("CustomSensorEventListener", "Ekran Kapandı")
        }
        if (isVerticalOrientation(y, z) && turnOnScreen) {

            //Cihaz yatay moddan dikey moda geçiyor ise ekranı aç
            Log.i("CustomSensorEventListener", "Ekran Açıldı")
            turnOnScreen = false
            turnOnScreen()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do nothing
    }

    private fun isVerticalOrientation(y: Float, z: Float): Boolean {
        // Dikey konumu kontrol etmek için uygun bir eşik değeri belirleyebilirsiniz
        return (kotlin.math.abs(y) > TO_VERTICAL_MAX && (z > TO_VERTICAL_MIN && z < TO_VERTICAL_MAX))
    }


    private fun turnOnScreen() {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp:MyWakeLockTag"
        )

        wakeLock.acquire(1*1000L)
        wakeLock.release()
    }

    companion object {
        private const val TO_VERTICAL_MIN = 4.0f
        private const val TO_VERTICAL_MAX = 7.0f
        const val HORIZONTAL = 8.0f//9.8f civarında tam anlamıyla yatay modda olur
    }
}
