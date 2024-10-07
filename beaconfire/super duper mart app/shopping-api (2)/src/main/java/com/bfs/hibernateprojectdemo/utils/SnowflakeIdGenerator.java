package com.bfs.hibernateprojectdemo.utils;

public class SnowflakeIdGenerator {
    private final long twepoch;
    private final long datacenterId;
    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private long timestamp = -1L;
    private final long sequenceMask = -1L ^ (-1L << 12);

    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        this.twepoch = 1288834974657L;
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        if (this.timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (this.lastTimestamp == this.timestamp) {
            this.sequence = (this.sequence + 1) & sequenceMask;
            if (this.sequence == 0) {
                this.timestamp = tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        this.lastTimestamp = this.timestamp;
        this.timestamp = tilNextMillis(this.lastTimestamp);

        long id = ((timestamp - twepoch) << 22) | (datacenterId << 12) | (workerId << 7) | sequence;
        return id & 0x7FFFFFFFFFFFFFFFL;
    }

    private long tilNextMillis(long lastTimestamp) {
        while (true) {
            long timestamp = timeGen();
            if (timestamp > lastTimestamp) {
                return timestamp;
            }
        }
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}