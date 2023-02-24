package com.example.zagrajmywculko.models

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.content.ContentValues.TAG
import android.content.Context
import androidx.core.content.getSystemService

import com.google.firebase.database.core.Tag


open class SensorActivity(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
): MeasurableSensor(sensorType), SensorEventListener{
    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor?= null

    override fun startListening() {
        if(!doesSensorExist){
            return
        }
        if(!::sensorManager.isInitialized && sensor==null){
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        Log.i(TAG,sensor.toString())
        sensor?.let{
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopListening() {
        if(!doesSensorExist||!::sensorManager.isInitialized){
            return
        }
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(!doesSensorExist){
            Log.i(TAG,"NIe ma sensora")
            return
        }
        if(event?.sensor?.type==sensorType){
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

}
