package org.rere.core.replay;

import org.rere.core.listener.wrap.bytebuddy.ClassRepo;

public class InOrderReplayObjectFactory implements EmptyObjectFactory {

    private final ClassRepo repo;

    public InOrderReplayObjectFactory(ClassRepo repo) {
        this.repo = repo;
    }

    public <T> T getObject(Class<T> clazz) {
        /*
        Class<T> mockedClass = repo.getOrDefineSubclass(clazz);
        T mocked = ObjectInitializer.create(mockedClass);

         */

        return null;
    }
}
