/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.api.ReReSettings;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.methods.Signature;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.replay.unwrap.GraphRootUnwrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
TODO: better type inference
 */
public class ReplayObjectListener implements ReReMethodInterceptor<InOrderReplayNode> {

    private GraphRootUnwrapper graphRootUnwrapper;

    public ReplayObjectListener() {
        this(new ReReSettings());
    }

    public ReplayObjectListener(ReReSettings reReSettings) {
    }

    public void setGraphRootUnwrapper(GraphRootUnwrapper graphRootUnwrapper) {
        this.graphRootUnwrapper = graphRootUnwrapper;
    }

    public Object interceptInterface(Object original,
                                     Method orignalMethod,
                                     InOrderReplayNode sourceNode,
                                     Object[] allArguments) throws Throwable {
        List<EnvironmentMethodCall> methodCalls = sourceNode.getMethodCalls();
        Signature signature = new Signature(orignalMethod);
        EnvironmentMethodCall target = null;
        // todo: start from current state
        int start = sourceNode.getNextCandidateMethod();
        for (int i = start; i < methodCalls.size(); i++) {
            EnvironmentMethodCall methodCall = methodCalls.get(i);
            if (signature.equals(methodCall.getSignature())) {
                target = methodCall;
                sourceNode.setNextCandidateMethod(i + 1);
                break;
            }
        }
        if (target == null) {
            // pure replay: throw some error
            // checkpointing: call real object.
            throw new RuntimeException("Method call was not recorded.");
        }
        List<Object> returnList = new ArrayList<>();
        List<Throwable> throwableList = new ArrayList<>();
        for (UserMethodCall userMethodCall : target.getUserMethodCalls()) {
            Object source = getVariable(userMethodCall.getSource(),
                    allArguments,
                    returnList,
                    userMethodCall.getLocalParameters());
            Method method = source.getClass().getMethod(userMethodCall.getMethodName());
            List<Object> params = new ArrayList<>();
            for (LocalSymbol paramSymbol : userMethodCall.getParameters()) {
                Object p = getVariable(paramSymbol, allArguments, returnList, userMethodCall.getLocalParameters());
                params.add(p);
            }
            try {
                Object returnValue = method.invoke(source, params.toArray());
                returnList.add(returnValue);
            } catch (Exception e) {
                throwableList.add(e);
            }
        }

        if (!target.getEndResult().isEmpty()) {
            for (int key : target.getEndResult().keySet()) {
                Object resultArray = target.getEndResult().get(key);
                ClassUtils.shallowCopyIntoArray(resultArray, allArguments[key]);
            }
        }

        if (target.getResult().equals(MethodResult.THROW)) {
            throw (Throwable) unwrap(target.getDest());
        }

        if (target.getReturnSymbol().getSource().equals(LocalSymbol.Source.LOCAL_ENV)) {
            return unwrap(target.getDest());
        }
        return getVariable(target.getReturnSymbol(), allArguments, returnList, new ArrayList<>());
    }

    private Object getVariable(LocalSymbol symbol,
                               Object[] args,
                               List<Object> returnList,
                               List<EnvironmentNode> locals) {
        Object root = null;
        if (symbol.getSource().equals(LocalSymbol.Source.PARAMETER)) {
            root = args[symbol.getIndex()];
        } else if (symbol.getSource().equals(LocalSymbol.Source.RETURN_VALUE)) {
            root = returnList.get(symbol.getIndex());
        } else if (symbol.getSource().equals(LocalSymbol.Source.LOCAL_ENV)) {
            root = unwrap(locals.get(symbol.getIndex()));
        }
        return new LocalVariableAccessor().getChild(root, symbol);
    }


    public Object unwrap(EnvironmentNode node) {
        return graphRootUnwrapper.unwrap(node);
    }
}