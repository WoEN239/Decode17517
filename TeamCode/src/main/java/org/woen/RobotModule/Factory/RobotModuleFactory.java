package org.woen.RobotModule.Factory;

import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.ModuleCreate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RobotModuleFactory {
    private final ArrayList<IRobotModuleFactory> factories = new ArrayList<>(Arrays.asList(
           //TODO ad modules factories
    ));

    public ArrayList<IRobotModule> getModules() {
        return modules;
    }

    private final ArrayList<IRobotModule> modules =new ArrayList<>();

    public void init(){
        try {
            for (IRobotModuleFactory i : factories) {
                Method[] methods = i.getClass().getDeclaredMethods();
                List<Method> createMethods = Arrays.stream(methods)
                        .filter(a -> a.isAnnotationPresent(ModuleCreate.class)).collect(Collectors.toList());
                for (Method j : createMethods) {
                    modules.add((IRobotModule) j.invoke(i));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("robot modules factoring error occurred");
        }
    }
}
