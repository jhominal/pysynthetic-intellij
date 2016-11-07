package com.wishtack.pysynthetic.psi;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.PyElementTypes;
import com.jetbrains.python.psi.impl.PyNamedParameterImpl;
import com.jetbrains.python.psi.impl.stubs.PyNamedParameterStubImpl;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Jean Hominal on 2016-11-06.
 */
public class VirtualParameter extends PyNamedParameterImpl {

    private final PyType myParameterType;
    private final boolean mySelf;

    public VirtualParameter(PyType parameterType, String name, boolean isSelf) {
        super(new PyNamedParameterStubImpl(name, false, false, false, null, null, PyElementTypes.NAMED_PARAMETER));
        myParameterType = parameterType;
        mySelf = isSelf;
    }

    @Override
    public boolean isSelf() {
        return mySelf;
    }

    @Override
    public PyType getType(@NotNull TypeEvalContext context, @NotNull TypeEvalContext.Key key) {
        return myParameterType;
    }

    @Override
    public PsiElement getParent() {
        return null;
    }
}
