package com.github.deweyreed.bluetooth.qs

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context

private fun Context.bluetoothAdapter(): BluetoothAdapter {
    return getSystemService(BluetoothManager::class.java).adapter
}

@SuppressLint("MissingPermission")
fun Context.enableBluetooth() {
    @Suppress("DEPRECATION")
    bluetoothAdapter().enable()
}

@SuppressLint("MissingPermission")
fun Context.disableBluetooth() {
    @Suppress("DEPRECATION")
    bluetoothAdapter().disable()
}

fun Context.isBluetoothEnabled(): Boolean {
    return bluetoothAdapter().isEnabled
}

fun Context.toggleBluetooth() {
    if (isBluetoothEnabled()) {
        disableBluetooth()
    } else {
        enableBluetooth()
    }
}
