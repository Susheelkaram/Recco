package com.susheelkaram.recco.util

import android.content.Context
import android.widget.Toast

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
}

