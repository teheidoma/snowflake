/*
 * created by teheidoma
 * Copyright (c) 2019.
 */

package com.teheidoma.snowflake

class DefaultSnowflakeImpl(epoch: Long, machineId: Long) : SnowflakeAbstractImpl(
    epoch,
    12,
    SnowflakeComponent(10) {
        machineId
    })