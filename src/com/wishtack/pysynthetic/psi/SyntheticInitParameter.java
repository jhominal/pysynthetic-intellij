package com.wishtack.pysynthetic.psi;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.PyElementTypes;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.impl.PyNamedParameterImpl;
import com.jetbrains.python.psi.impl.stubs.PyNamedParameterStubImpl;
import com.jetbrains.python.psi.stubs.PyNamedParameterStub;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticMemberInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jean Hominal on 2016-11-06.
 */
public class SyntheticInitParameter extends PyNamedParameterImpl {

    @NotNull
    private static PyNamedParameterStub createVirtualStub(@NotNull SyntheticMemberInfo syntheticMemberInfo) {
        boolean hasDefault = syntheticMemberInfo.getDefaultValue() != null || syntheticMemberInfo.acceptsNone();
        return new PyNamedParameterStubImpl(syntheticMemberInfo.getName(), false, false, hasDefault, null, null, PyElementTypes.NAMED_PARAMETER);
    }

    @NotNull
    private final SyntheticMemberInfo myMemberInfo;

    public SyntheticInitParameter(@NotNull SyntheticMemberInfo syntheticMemberInfo) {
        super(createVirtualStub(syntheticMemberInfo));
        myMemberInfo = syntheticMemberInfo;
    }

    @Override
    public PyType getType(@NotNull TypeEvalContext context, @NotNull TypeEvalContext.Key key) {
        return myMemberInfo.getMemberType();
    }

    @Nullable
    @Override
    public PyExpression getDefaultValue() {
        return myMemberInfo.getDefaultValue();
    }

    @Override
    public PsiElement getParent() {
        return null;
    }
}
