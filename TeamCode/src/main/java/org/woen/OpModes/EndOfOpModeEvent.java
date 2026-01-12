package org.woen.OpModes;

import org.woen.Architecture.EventBus.IEvent;

public class EndOfOpModeEvent implements IEvent<Integer> {
    @Override
    public Integer getData() {
        return 0;
    }
}
