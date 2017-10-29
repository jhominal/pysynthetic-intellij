package com.wishtack.pysynthetic.impl;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.resolve.PyResolveContext;
import com.jetbrains.python.psi.types.PyClassMembersProviderBase;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.PyClassTypeImpl;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import com.wishtack.pysynthetic.psi.AbstractAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public class SyntheticMembersProvider extends PyClassMembersProviderBase {

    // HACK: Inspect stack trace to return empty collection if doing code completion.
    private static boolean isGettingCompletionVariants() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i = 2; i < stackTrace.length; i++) {
            StackTraceElement current = stackTrace[i];
            if (!PyClassTypeImpl.class.getName().equals(current.getClassName())) {
                return false;
            } else if ("getCompletionVariants".equals(current.getMethodName())) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getMembers(PyClassType clazz, PsiElement location, TypeEvalContext typeEvalContext) {
        if (isGettingCompletionVariants()) {
            return Collections.emptyList();
        } else {
            SyntheticTypeInfo typeInfo = new SyntheticTypeInfoReader(clazz.getPyClass()).read();
            return typeInfo.getResolveMap().entrySet().stream()
                    .map(e -> computeMember(e.getKey(), e.getValue(), clazz))
                    .collect(Collectors.toList());
        }
    }

    private static PyCustomMember computeMember(String name, PsiElement element, PyClassType clazz) {
        PyCustomMember customMember = new PyCustomMember(name, element, clazz.getName());
        if (element instanceof AbstractAccessor) {
            customMember.asFunction();
        }
        return customMember;
    }

    @Override
    public PsiElement resolveMember(@NotNull PyClassType type, @NotNull String name, PsiElement location, @NotNull PyResolveContext resolveContext) {
        SyntheticTypeInfo typeInfo = new SyntheticTypeInfoReader(type.getPyClass()).read();
        return typeInfo.getResolveMap().get(name);
    }
}
