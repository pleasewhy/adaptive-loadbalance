package com.aliware.tianchi;

import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.transport.RequestLimiter;
import org.apache.dubbo.remoting.transport.ThreadPollExhaustedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import sun.nio.ch.ThreadPool;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端限流
 * 可选接口
 * 在提交给后端线程池之前的扩展，可以用于服务端控制拒绝请求
 */
public class TestRequestLimiter implements RequestLimiter {

    public ProviderConfig providerConfig = new ProviderConfig();
    int poolSize = providerConfig.getThreads();
    /**
     * @param request 服务请求
     * @param activeTaskCount 服务端对应线程池的活跃线程数
     * @return false 不提交给服务端业务线程池直接返回，客户端可以在 Filter 中捕获 RpcException
     * true 不限流
     */
    static int totalCount = 0;
    static int failCount = 0;
    static int perCountDiscard = -1;
    long lastTimeStamp = System.currentTimeMillis();

    @Override
    public boolean tryAcquire(Request request, int activeTaskCount) {
        if (activeTaskCount >= poolSize-5) {
            failCount++;
        }
        if (System.nanoTime() - lastTimeStamp > 1000) {
            if (failCount == 0 || totalCount == 0) {
            } else {
                failCount = 0;
                totalCount = 0;
                perCountDiscard = failCount / totalCount + 10;
            }
        }

        lastTimeStamp = System.currentTimeMillis();
        if (perCountDiscard != -1 && totalCount % perCountDiscard == 0) {
            System.out.println("false");
        }
        return true;
    }

}
