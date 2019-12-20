package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;

import java.util.Map;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {
    public static int receiveCount = 0;
    @Override
    public void receiveServerMsg(String msg) {
        receiveCount++;
        if (receiveCount == 3) {
            receiveCount = 0;
            if(UserLoadBalance.totalCount==0||sum(UserLoadBalance.lastSecondFailRequest)==0){
                return;
            }

            UserLoadBalance.perCountDiscard =sum(UserLoadBalance.lastSecondFailRequest)/UserLoadBalance.totalCount;
            UserLoadBalance.totalCount = 0;
        }
        String[] t = msg.split(" ");
        System.out.println("receive msg from server :" + msg);
        UserLoadBalance.lastSecondFailRequest.put(t[0], Integer.valueOf(t[1]));
    }

    private int sum(Map<String,Integer> map){
        int rev = 0;
        for (String i:map.keySet()
             ) {
            rev += map.get(i);
        };
        return rev;
    }
}
