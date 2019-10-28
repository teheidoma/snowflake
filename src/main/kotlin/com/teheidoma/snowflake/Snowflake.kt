package com.teheidoma.snowflake

interface Snowflake {
    fun nextId(): Long
    fun getTimestamp(id: Long): Long
    fun getEpoch(): Long
}


data class SnowflakeComponent(
    val bits: Int,
    val factory: () -> Long
)
