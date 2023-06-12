package com.bahadir.service.presentation.background

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.bahadir.service.infrastructure.CustomAccelerometerEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SensorService : Service() {
    @Inject
    lateinit var sensorAccelerometer: CustomAccelerometerEventListener

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        ContextCompat.registerReceiver(
            baseContext, broadcastReceiver, setBroadcastReceiver(),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        sensorRegister()
        return START_STICKY
    }

    private fun setBroadcastReceiver(): IntentFilter {
        return IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
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
            sensorAccelerometer, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun sensorUnRegister() {
        sensorManager.unregisterListener(sensorAccelerometer)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        sensorUnRegister()
    }
}




