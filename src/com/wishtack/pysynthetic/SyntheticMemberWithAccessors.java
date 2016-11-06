package com.wishtack.pysynthetic;

import com.intellij.psi.PsiElement;
import com.intellij.util.PlatformIcons;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticMemberWithAccessors extends SyntheticMemberInfo {

    private static final List<PyCallableParameter> emptyParameterList = Collections.emptyList();

    @NotNull
    private final String myGetterName;
    @Nullable
    private final String mySetterName;

    private Collection<PyCustomMember> myPyMembers;

    public SyntheticMemberWithAccessors(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, @NotNull String getterName, @Nullable String setterName, @Nullable PyType memberType, @Nullable PyExpression defaultValue) {
        super(pyClass, definitionDecorator, name, setterName == null, memberType, defaultValue);

        myGetterName = getterName;
        mySetterName = setterName;
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            String pyClassName = getDefinitionClass().getQualifiedName();

            PyCustomMember[] membersArray = new PyCustomMember[mySetterName == null ? 1 : 2];

            PyCustomMember getterMember = new PyCustomMember(myGetterName, pyClassName, this::getGetterType);
            getterMember.withIcon(PlatformIcons.METHOD_ICON);
            getterMember.toPsiElement(getDefinitionDecorator());
            getterMember.asFunction();

            membersArray[0] = getterMember;

            if (mySetterName != null) {
                PyCustomMember setterMember = new PyCustomMember(mySetterName, pyClassName, this::getSetterType);
                setterMember.withIcon(PlatformIcons.METHOD_ICON);
                setterMember.toPsiElement(getDefinitionDecorator());
                setterMember.asFunction();

                membersArray[1] = setterMember;
            }

            myPyMembers = Collections.unmodifiableList(Arrays.asList(membersArray));
        }

        return myPyMembers;
    }

    private PyCallableTypeImpl myGetterType;

    private PyType getGetterType(PsiElement context) {

        if (myGetterType == null) {
            myGetterType = new PyCallableTypeImpl(emptyParameterList, getMemberType());
        }

        return myGetterType;
    }

    private PyCallableTypeImpl mySetterType;

    private PyType getSetterType(PsiElement context) {
        if (mySetterType == null) {

            PyCallableParameter[] setterParameters = new PyCallableParameter[1];

            setterParameters[0] = new PyCallableParameterImpl("value", getMemberType());

            List<PyCallableParameter> setterParametersList =
                    Collections.unmodifiableList(Arrays.asList(setterParameters));

            mySetterType = new PyCallableTypeImpl(setterParametersList, PyNoneType.INSTANCE);
        }

        return mySetterType;
    }

    @Override
    public String toString() {
        return String.format("SyntheticMemberWithAccessors: '%s', readOnly=%b, (%s/%s)", getName(), isReadOnly(), myGetterName, mySetterName);
    }
}
