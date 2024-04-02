package com.github.deweyreed.bluetooth.qs

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context

private fun Context.bluetoothManager(): BluetoothManager {
    return checkNotNull(getSystemService(BluetoothManager::class.java))
}

private fun Context.bluetoothAdapter(): BluetoothAdapter {
    return bluetoothManager().adapter
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
    return bluetoothAdapter().state.isBluetoothStateEnabled()
}

fun Int.isBluetoothStateEnabled(): Boolean {
    return this == BluetoothAdapter.STATE_ON || this == BluetoothAdapter.STATE_TURNING_ON
}

fun Context.isBluetoothConnected(): Boolean {
    val adapter = bluetoothAdapter()

    val profiles = listOf(
        BluetoothProfile.HEADSET,
        BluetoothProfile.A2DP,
        BluetoothProfile.GATT,
        BluetoothProfile.GATT_SERVER,
        BluetoothProfile.SAP,
        BluetoothProfile.HID_DEVICE,
        BluetoothProfile.HEARING_AID,
    )

    for (profile in profiles) {
        val state = @Suppress("MissingPermission") adapter.getProfileConnectionState(profile)
        if (state == BluetoothProfile.STATE_CONNECTED || state == BluetoothProfile.STATE_CONNECTING) {
            return true
        }
    }
    return false
}

fun Context.toggleBluetooth() {
    if (isBluetoothEnabled()) {
        disableBluetooth()
    } else {
        enableBluetooth()
    }
}
