package com.wishtack.pysynthetic.impl;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.types.PyClassMembersProviderBase;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import com.wishtack.pysynthetic.psi.AbstractAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public class SyntheticMembersProvider extends PyClassMembersProviderBase {
    @NotNull
    @Override
    public Collection<PyCustomMember> getMembers(PyClassType clazz, PsiElement location, TypeEvalContext typeEvalContext) {
        SyntheticTypeInfo typeInfo = new SyntheticTypeInfoReader(clazz.getPyClass()).read();
        return typeInfo.getResolveMap().entrySet().stream()
                .map(e -> computeMember(e.getKey(), e.getValue(), clazz))
                .collect(Collectors.toList());
    }

    private static PyCustomMember computeMember(String name, PsiElement element, PyClassType clazz) {
        PyCustomMember customMember = new PyCustomMember(name, element, clazz.getName());
        if (element instanceof AbstractAccessor) {
            customMember.asFunction();
        }
        return customMember;
    }

    @Override
    public PsiElement resolveMember(PyClassType clazz, String name, PsiElement location, TypeEvalContext context) {
        SyntheticTypeInfo typeInfo = new SyntheticTypeInfoReader(clazz.getPyClass()).read();
        return typeInfo.getResolveMap().get(name);
    }
}
