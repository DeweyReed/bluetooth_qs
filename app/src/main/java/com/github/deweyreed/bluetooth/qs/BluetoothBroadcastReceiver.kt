package com.github.deweyreed.bluetooth.qs

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothBroadcastReceiver(
    private val listener: BlueToothStateListener,
) : BroadcastReceiver() {
    interface BlueToothStateListener {
        fun onBluetoothEnabled()
        fun onBluetoothConnected()
        fun onBluetoothDisabled()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val action = intent.action
        if (action != BluetoothAdapter.ACTION_STATE_CHANGED &&
            action != BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED &&
            action != BluetoothDevice.ACTION_ACL_CONNECTED &&
            action != BluetoothDevice.ACTION_ACL_DISCONNECTED
        ) {
            return
        }
        updateState(context)
    }

    private fun updateState(context: Context) {
        if (context.isBluetoothEnabled()) {
            if (context.isBluetoothConnected()) {
                listener.onBluetoothConnected()
            } else {
                listener.onBluetoothEnabled()
            }
        } else {
            listener.onBluetoothDisabled()
        }
    }

    companion object {
        fun register(context: Context, listener: BlueToothStateListener): BroadcastReceiver {
            val receiver = BluetoothBroadcastReceiver(listener)
            receiver.updateState(context)
            context.registerReceiver(
                receiver,
                IntentFilter().apply {
                    addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                    addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                    addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                    addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                },
            )
            return receiver
        }
    }
}
