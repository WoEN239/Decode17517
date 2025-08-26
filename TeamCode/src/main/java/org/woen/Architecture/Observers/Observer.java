package org.woen.Architecture.Observers;

import org.woen.Architecture.EventBus.Bus.IEvent;
import java.util.ArrayList;

public abstract class Observer <D,R extends IEvent<IListener<D>>>{
    protected final ArrayList<IListener<D>> listeners = new ArrayList<>();
    public void remove(IListener<D> listener){
        listeners.remove(listener);
    }

    public void notifyListeners(D data){
        for (IListener<D> i: listeners) {
            i.set(data);
        }
    }
    public abstract void onEvent(R registration);
}
