package com.example.zagrajmywculko.models

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class SensorActivity : Activity(), SensorEventListener {
    private val mSensorManager: SensorManager
    private val mGyroscope: Sensor

    init {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent) {}
}