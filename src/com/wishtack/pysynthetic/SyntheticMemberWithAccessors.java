package com.wishtack.pysynthetic;

import com.intellij.util.PlatformIcons;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

    public SyntheticMemberWithAccessors(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, @NotNull String getterName, String setterName) {
        super(pyClass, definitionDecorator, name, setterName == null);

        myGetterName = getterName;
        mySetterName = setterName;
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            String pyClassName = getDefinitionClass().getQualifiedName();

            ArrayList<PyCustomMember> membersArray = new ArrayList<>(2);

            PyCustomMember getterMember = new PyCustomMember(myGetterName, pyClassName, null);
            getterMember.withIcon(PlatformIcons.METHOD_ICON);
            getterMember.toPsiElement(getDefinitionDecorator());
            getterMember.asFunction();

            membersArray.add(getterMember);

            if (mySetterName != null) {
                PyCustomMember setterMember = new PyCustomMember(mySetterName, pyClassName, null);
                setterMember.withIcon(PlatformIcons.METHOD_ICON);
                setterMember.toPsiElement(getDefinitionDecorator());
                setterMember.asFunction();

                membersArray.add(setterMember);
            }

            myPyMembers = Collections.unmodifiableList(membersArray);
        }

        return myPyMembers;
    }

    @Override
    public String toString() {
        return String.format("SyntheticMemberWithAccessors: '%s', readOnly=%b, (%s/%s)", getName(), isReadOnly(), myGetterName, mySetterName);
    }
}
