package org.example.state;


import org.example.cb.AbstractCircuitBreaker;

import java.util.Date;

public class OpenState implements State {
    /**
     * 进入当前状态的初始化时间
     */
    private long stateTime = System.currentTimeMillis();

    public String getStateName() {
        // 获取当前状态名称
        return this.getClass().getSimpleName();
    }

    public OpenState() {

    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
    }

    public boolean checkBeforeService(AbstractCircuitBreaker cb) {
        // 检测状态
        checkAndSwitchState(cb);
        return false;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // nothing
    }
}
