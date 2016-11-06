package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.types.PyType;
import com.wishtack.pysynthetic.psi.SyntheticGetterCallable;
import com.wishtack.pysynthetic.psi.SyntheticSetterCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticMemberWithAccessors extends SyntheticMemberInfo {

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
    public String getGetterName() {
        return myGetterName;
    }

    @Nullable
    public String getSetterName() {
        return mySetterName;
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            String pyClassName = getDefinitionClass().getName();

            PyCustomMember[] membersArray = new PyCustomMember[mySetterName == null ? 1 : 2];

            PyCustomMember getterMember = new PyCustomMember(myGetterName, new SyntheticGetterCallable(this), pyClassName);
            getterMember.asFunction();

            membersArray[0] = getterMember;

            if (mySetterName != null) {
                PyCustomMember setterMember = new PyCustomMember(mySetterName, new SyntheticSetterCallable(this), pyClassName);
                setterMember.asFunction();

                membersArray[1] = setterMember;
            }

            myPyMembers = Collections.unmodifiableList(Arrays.asList(membersArray));
        }

        return myPyMembers;
    }

    @Override
    public String toString() {
        return String.format("SyntheticMemberWithAccessors: '%s', readOnly=%b, (%s/%s)", getName(), isReadOnly(), myGetterName, mySetterName);
    }
}
