/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.core.wrap.bytebuddy.ClassRepo;

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
