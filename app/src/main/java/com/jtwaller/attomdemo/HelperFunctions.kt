package com.jtwaller.attomdemo

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

// convert miles to meters
fun Double.milesToMeters() = this * 1609.34