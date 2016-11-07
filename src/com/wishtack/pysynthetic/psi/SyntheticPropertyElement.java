package com.wishtack.pysynthetic.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyPossibleClassMember;
import com.jetbrains.python.psi.PyTypedElement;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticPropertyMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jean Hominal on 2016-11-07.
 */
public class SyntheticPropertyElement extends ASTWrapperPsiElement implements PyTypedElement, PyPossibleClassMember {

    @NotNull
    private final SyntheticPropertyMember myPropertyMember;

    public SyntheticPropertyElement(@NotNull SyntheticPropertyMember propertyMember) {
        super(propertyMember.getDefinitionDecorator().getNode());
        myPropertyMember = propertyMember;
    }

    @Nullable
    @Override
    public PyClass getContainingClass() {
        return myPropertyMember.getDefinitionClass();
    }

    @Nullable
    @Override
    public PyType getType(@NotNull TypeEvalContext typeEvalContext, @NotNull TypeEvalContext.Key key) {
        return myPropertyMember.getMemberType();
    }
}
