package io.github.larsrosenkilde.musicplayer.utils

import android.database.Cursor

fun <T> Cursor.getColumnValueNullable(
    name: String,
    fn: (Int) -> T?
): T? {
    val idx = getColumnIndex(name)
    return if (idx > -1) fn(idx) else null
}

fun <T> Cursor.getColumnValue(
    name: String,
    fn: (Int) -> T
): T {
    val idx = getColumnIndex(name)
    return fn(idx)
}