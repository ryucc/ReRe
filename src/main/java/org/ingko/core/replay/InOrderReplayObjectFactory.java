package org.ingko.core.replay;

import org.ingko.core.listener.ClassRepo;
import org.ingko.core.listener.ObjectInitializer;

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
