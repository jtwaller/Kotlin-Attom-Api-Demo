package com.jtwaller.attomdemo

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
