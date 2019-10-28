/*
 * created by teheidoma
 * Copyright (c) 2019.
 */

package com.teheidoma.snowflake

import java.util.concurrent.atomic.AtomicLong

abstract class SnowflakeAbstractImpl(
    private val epoch: Long,
    private val sequenceBits: Int,
    vararg val components: SnowflakeComponent
) : Snowflake {
    private val TOTAL_BITS = 64
    private val EPOCH_BITS = 42

    private val maxSequence = Math.pow(2.0, sequenceBits.toDouble()).toInt()

    private val lastTimestamp = AtomicLong(-1)
    private val sequence = AtomicLong(0)
    private val ntpSync = NTPSync()

    @Synchronized
    override fun nextId(): Long {
        var timestamp = timestamp()
        check(timestamp >= lastTimestamp.get()) { "Invalid System Clock!" }

        if (timestamp == lastTimestamp.get()) {
            sequence.set(sequence.incrementAndGet() and maxSequence.toLong())
            if (sequence.get() == 0L) {
                timestamp = waitNextMillis(timestamp)

            }
        } else {
            sequence.set(0)
        }

        lastTimestamp.set(timestamp)

        var remaining = TOTAL_BITS

        var id = timestamp shl (TOTAL_BITS - EPOCH_BITS)
        remaining -= EPOCH_BITS

        components.forEach {
            id = id or (it.factory() shl remaining)
            remaining -= it.bits
        }

        id = id or sequence.get()

        return id

    }

    init {
        check(EPOCH_BITS + sequenceBits + components.map { it.bits }.reduce { acc, i -> acc + i } > 64) { "invalid snowflake components (too much bits)" }
        ntpSync.sync()
    }

    override fun getTimestamp(id: Long): Long {
        return (id shr (TOTAL_BITS - EPOCH_BITS)) + epoch
    }

    override fun getEpoch(): Long {
        return epoch
    }

    private fun waitNextMillis(timestamp: Long): Long {
        var currentTimestamp = timestamp
        while (currentTimestamp == lastTimestamp.get()) {
            currentTimestamp = timestamp()
        }
        return currentTimestamp
    }

    private fun timestamp(): Long {
        return ntpSync.getTime() - epoch
    }
}