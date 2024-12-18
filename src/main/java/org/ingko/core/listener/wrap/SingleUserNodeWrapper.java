package org.ingko.core.listener.wrap;

import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.wrap.bytebuddy.ClassRepo;
import org.ingko.core.listener.ObjectInitializer;
import org.ingko.core.listener.utils.UserObjectSpy;

public class SingleUserNodeWrapper implements SingleNodeWrapper<UserNode> {
    private final ClassRepo classRepo;

    public SingleUserNodeWrapper(ClassRepo classRepo) {
        this.classRepo = classRepo;
    }

    @Override
    public <T> T initiateSpied(T returnValue, UserNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            ((UserObjectSpy) mocked).setParrotUserNode(node);
            ((UserObjectSpy) mocked).setParrotOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }
}
