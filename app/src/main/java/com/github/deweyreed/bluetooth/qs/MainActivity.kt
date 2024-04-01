package com.github.deweyreed.bluetooth.qs

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toggleBluetooth()
        finish()
    }
}
