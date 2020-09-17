package org.example.state;


import org.example.cb.AbstractCircuitBreaker;

public interface State {
    /**
     * 获取当前状态名称
     */
    String getStateName();

    /**
     * 检查以及校验当前状态是否需要扭转
     */
    void checkAndSwitchState(AbstractCircuitBreaker cb);

    /**
     * 熔断器拦截操作
     */
    boolean checkBeforeService(AbstractCircuitBreaker cb);

    /**
     * 统计失败次数
     */
    void countFailNum(AbstractCircuitBreaker cb);
}
