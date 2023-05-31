package com.bahadir.service.presentation.background

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SensorService : Service() {
    @Inject
    lateinit var sensorEventListener: CustomSensorEventListener

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        registerReceiver(broadcastReceiver, setBroadcastReceiver())
    }

    private fun setBroadcastReceiver(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_USER_PRESENT)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        return filter
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    sensorRegister()
                }

                Intent.ACTION_SCREEN_ON -> {
                    sensorUnRegister()
                }
            }
        }
    }


    private fun sensorRegister() {
        sensorManager.registerListener(
            sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun sensorUnRegister() {
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                sensorRegister()
            }

            Intent.ACTION_SCREEN_ON -> {
                Log.e("SensorService", "ACTION_SCREEN_ON")
                sensorUnRegister()
            }

        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        sensorUnRegister()
        Log.e("SensorService", "onDestroy")

    }

}

class CustomSensorEventListener(private val context: Context) : SensorEventListener {
    private var turnOnScreen = true

    //var isTurnOff = false
    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // X değeri cihazın sağa veya sola eğimini verir
        // Y değeri cihazın yukarı veya aşağı eğimini verir
        // Z değeri cihazın öne veya arkaya eğimini verir
        Log.e("CustomSensorEventListener", "x: $x, y: $y, z: $z")
        if (z > 8.0f) {
            turnOnScreen = true
            Log.e("CustomSensorEventListener", "Ekran Kapandı")
        }
        if (isVerticalOrientation(y, z) && turnOnScreen) {
            // Cihaz dikey konumdaysa ve ekran kapalıysa ekranı açabilirsiniz
            Log.e("CustomSensorEventListener", "Ekran Açıldı")
            turnOnScreen = false
            turnOnScreen()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do nothing
    }

    private fun isVerticalOrientation(y: Float, z: Float): Boolean {
        //x: 0.06361389, y: 0.29811096, z: 9.941956
        //x: 0.07299805, y: 0.30265808, z: 9.948059 telefon yerde düz halde iken > 0 doğru gidiyor z
        // Dikey konumu kontrol etmek için uygun bir eşik değeri belirleyebilirsiniz
        return (kotlin.math.abs(y) > THRESHOLD_MAX && (z > THRESHOLD_MIN && z < THRESHOLD_MAX))
    }


    private fun turnOnScreen() {
        // Ekranı açmak için gerekli işlemleri burada gerçekleştirin
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp:MyWakeLockTag"
        )

        wakeLock.acquire(1 * 1000L /*10 seconds*/)
        wakeLock.release()
    }

    companion object {
        private const val THRESHOLD_MIN = 4.0f
        private const val THRESHOLD_MAX = 7.0f
    }
}



