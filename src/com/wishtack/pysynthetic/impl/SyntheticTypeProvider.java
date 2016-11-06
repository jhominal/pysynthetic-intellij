package com.wishtack.pysynthetic.impl;

import com.jetbrains.python.psi.PyCallable;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyParameter;
import com.jetbrains.python.psi.PyPossibleClassMember;
import com.jetbrains.python.psi.types.*;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jean Hominal on 2016-11-05.
 */
public class SyntheticTypeProvider extends PyTypeProviderBase {

    @Nullable
    @Override
    public PyType getCallableType(@NotNull PyCallable callable, @NotNull TypeEvalContext context) {
        if (!(callable instanceof PyPossibleClassMember)) {
            return null;
        }
        PyClass pyClass = ((PyPossibleClassMember)callable).getContainingClass();
        if (pyClass == null) {
            return null;
        }

        SyntheticTypeInfo info = new SyntheticTypeInfoReader(pyClass).read();
        if (info.hasSyntheticConstructor() && callable.equals(pyClass.findInitOrNew(false, null))) {
            PyParameter[] originalParameters = callable.getParameterList().getParameters();

            List<PyCallableParameter> allInitParameters =
                Stream.concat(Arrays.stream(originalParameters), info.getSyntheticInitParameters().stream())
                    .map(PyCallableParameterImpl::new)
                    .collect(Collectors.toList());

            return new PyCallableTypeImpl(allInitParameters, context.getReturnType(callable));
        }

        return super.getCallableType(callable, context);
    }
}
