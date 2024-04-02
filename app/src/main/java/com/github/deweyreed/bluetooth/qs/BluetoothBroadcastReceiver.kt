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
        updateState(
            context = context,
            isEnabled = if (intent.hasExtra(BluetoothAdapter.EXTRA_STATE)) {
                intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF
                ).isBluetoothStateEnabled()
            } else {
                context.isBluetoothEnabled()
            },
            isConnected = if (intent.hasExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE)) {
                intent.getIntExtra(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE,
                    BluetoothAdapter.STATE_DISCONNECTED
                ).let {
                    it == BluetoothAdapter.STATE_CONNECTED ||
                            it == BluetoothAdapter.STATE_CONNECTING
                }
            } else {
                context.isBluetoothConnected()
            }
        )
    }

    private fun updateState(
        context: Context,
        isEnabled: Boolean = context.isBluetoothEnabled(),
        isConnected: Boolean = context.isBluetoothConnected(),
    ) {
        if (isEnabled) {
            if (isConnected) {
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
