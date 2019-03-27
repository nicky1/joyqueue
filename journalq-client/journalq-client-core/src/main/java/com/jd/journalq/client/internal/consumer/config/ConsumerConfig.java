package com.jd.journalq.client.internal.consumer.config;

/**
 * ConsumerConfig
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/12/7
 */
public class ConsumerConfig {

    public static final int NONE_BATCH_SIZE = -1;

    public static final long NONE_ACK_TIMEOUT = -1;

    public static final int NONE_BROADCAST_INDEX_EXPIRE_TIME = -1;

    private String app;
    private String group;
    private int batchSize = NONE_BATCH_SIZE;
    private long ackTimeout = NONE_ACK_TIMEOUT;
    private long timeout = 1000 * 10;
    private long pollTimeout = 1000 * 10;
    private long longPollTimeout = 1000 * 5;
    private long interval = 0;
    private long idleInterval = 1000 * 1;
    private long sessionTimeout = 1000 * 60 * 1;
    private int thread = 1;
    private boolean failover = true;

    private boolean loadBalance = true;
    private String loadBalanceType = "roundrobin";

    private String broadcastGroup;
    private String broadcastLocalPath;
    private int broadcastPersistInterval = 1000 * 10;
    private int broadcastIndexExpireTime = NONE_BROADCAST_INDEX_EXPIRE_TIME;

    public ConsumerConfig copy() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setApp(app);
        consumerConfig.setGroup(group);
        consumerConfig.setBatchSize(batchSize);
        consumerConfig.setAckTimeout(ackTimeout);
        consumerConfig.setTimeout(timeout);
        consumerConfig.setPollTimeout(pollTimeout);
        consumerConfig.setLongPollTimeout(longPollTimeout);
        consumerConfig.setInterval(interval);
        consumerConfig.setIdleInterval(idleInterval);
        consumerConfig.setSessionTimeout(sessionTimeout);
        consumerConfig.setThread(thread);
        consumerConfig.setFailover(failover);
        consumerConfig.setLoadBalance(loadBalance);
        consumerConfig.setLoadBalanceType(loadBalanceType);
        consumerConfig.setBroadcastGroup(broadcastGroup);
        consumerConfig.setBroadcastLocalPath(broadcastLocalPath);
        consumerConfig.setBroadcastPersistInterval(broadcastPersistInterval);
        consumerConfig.setBroadcastIndexExpireTime(broadcastIndexExpireTime);
        return consumerConfig;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        if (broadcastGroup == null) {
            broadcastGroup = app;
        }
        this.app = app;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setAckTimeout(long ackTimeout) {
        this.ackTimeout = ackTimeout;
    }

    public long getAckTimeout() {
        return ackTimeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public long getPollTimeout() {
        return pollTimeout;
    }

    public long getLongPollTimeout() {
        return longPollTimeout;
    }

    public void setLongPollTimeout(long longPollTimeout) {
        this.longPollTimeout = longPollTimeout;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getIdleInterval() {
        return idleInterval;
    }

    public void setIdleInterval(long idleInterval) {
        this.idleInterval = idleInterval;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getThread() {
        return thread;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setLoadBalance(boolean loadBalance) {
        this.loadBalance = loadBalance;
    }

    public boolean isLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalanceType(String loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }

    public String getLoadBalanceType() {
        return loadBalanceType;
    }

    public String getBroadcastGroup() {
        return broadcastGroup;
    }

    public void setBroadcastGroup(String broadcastGroup) {
        this.broadcastGroup = broadcastGroup;
    }

    public void setBroadcastLocalPath(String broadcastLocalPath) {
        this.broadcastLocalPath = broadcastLocalPath;
    }

    public String getBroadcastLocalPath() {
        return broadcastLocalPath;
    }

    public int getBroadcastPersistInterval() {
        return broadcastPersistInterval;
    }

    public void setBroadcastPersistInterval(int broadcastPersistInterval) {
        this.broadcastPersistInterval = broadcastPersistInterval;
    }

    public int getBroadcastIndexExpireTime() {
        return broadcastIndexExpireTime;
    }

    public void setBroadcastIndexExpireTime(int broadcastIndexExpireTime) {
        this.broadcastIndexExpireTime = broadcastIndexExpireTime;
    }
}