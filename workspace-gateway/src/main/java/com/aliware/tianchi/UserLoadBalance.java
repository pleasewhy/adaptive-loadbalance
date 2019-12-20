package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    public static int totalCount = 0;
    Map<String,Integer> requestCount= new HashMap<>();
    static Map<String,Integer> lastSecondFailRequest = new HashMap<>();
    static int perCountDiscard = -1;
    String large = "large";
    String medium = "medium";
    String small = "small";
    public UserLoadBalance(){
        init();
    }
    public void init(){
//        requestCount.put(large, 0);
//        requestCount.put(medium, 0);
//        requestCount.put(small, 0);
//        lastSecondFailRequest.put(large,0);
//        lastSecondFailRequest.put(medium, 0);
//        lastSecondFailRequest.put(small, 0);
    }
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
//        if(perCountDiscard!=-1&&totalCount%perCountDiscard == 0){
//            throw new RpcException();
//        }

        int tmp = totalCount++%6;
        if(tmp==0||tmp==2||tmp==5) {
//            requestCount.replace(large, requestCount.get(large)+1);
            return invokers.get(2);
        } else if(tmp==1||tmp==4) {
//            requestCount.replace(medium,requestCount.get(medium)+1);
            return invokers.get(1);
        } else {
//            requestCount.replace(small,requestCount.get(small)+1);
            return invokers.get(0);
        }
    }
}
