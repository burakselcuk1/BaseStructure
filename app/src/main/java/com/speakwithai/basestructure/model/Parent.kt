package com.speakwithai.basestructure.model

import android.graphics.drawable.Drawable

data class Parent(val icon: Drawable, val title: String, val children: List<Child>)
data class Child(val text: String)
