package com.wishtack.pysynthetic.impl;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.types.PyClassMembersProviderBase;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;

import java.util.Collection;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public class SyntheticMembersProvider extends PyClassMembersProviderBase {
    @Override
    public Collection<PyCustomMember> getMembers(PyClassType clazz, PsiElement location, TypeEvalContext typeEvalContext) {
        SyntheticTypeInfo typeInfo = SyntheticTypeInfo.read(clazz.getPyClass());

        return typeInfo.getPyMembers();
    }
}
