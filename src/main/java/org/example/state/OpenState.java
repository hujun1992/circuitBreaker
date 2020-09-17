package org.example.state;


import org.example.cb.AbstractCircuitBreaker;

public class OpenState implements State {

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
