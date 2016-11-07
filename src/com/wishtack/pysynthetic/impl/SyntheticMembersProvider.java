package com.wishtack.pysynthetic.impl;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.types.PyClassMembersProviderBase;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public class SyntheticMembersProvider extends PyClassMembersProviderBase {
    @NotNull
    @Override
    public Collection<PyCustomMember> getMembers(PyClassType clazz, PsiElement location, TypeEvalContext typeEvalContext) {
        // No need to return custom members, because SyntheticMembersCompletionContributor
        // does everything useful that PyClassTypeImpl does with the return value of getMembers.
        return Collections.emptyList();
    }

    @Override
    public PsiElement resolveMember(PyClassType clazz, String name, PsiElement location, TypeEvalContext context) {
        SyntheticTypeInfo typeInfo = new SyntheticTypeInfoReader(clazz.getPyClass()).read();
        return typeInfo.getResolveMap().get(name);
    }
}
