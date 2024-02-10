package com.speakwithai.basestructure.ui.crypto.utilities

import android.os.Build
import android.os.Bundle
import java.io.Serializable


fun <T : Serializable?> Bundle?.getSerializableArg(name: String, clazz: Class<T>): T?
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this!!.getSerializable(name, clazz)
    else
        this!!.getSerializable(name) as T?
}