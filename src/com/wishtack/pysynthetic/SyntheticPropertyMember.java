package com.wishtack.pysynthetic;

import com.intellij.util.PlatformIcons;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticPropertyMember extends SyntheticMemberInfo {

    private Collection<PyCustomMember> myPyMembers;

    public SyntheticPropertyMember(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly) {
        super(pyClass, definitionDecorator, name, readOnly);
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            String pyClassName = getDefinitionClass().getQualifiedName();

            ArrayList<PyCustomMember> membersArray = new ArrayList<>(1);

            PyCustomMember propertyMember = new PyCustomMember(getName(), pyClassName, null);
            propertyMember.withIcon(PlatformIcons.PROPERTY_ICON);
            propertyMember.toPsiElement(getDefinitionDecorator());

            membersArray.add(propertyMember);

            myPyMembers = Collections.unmodifiableList(membersArray);
        }

        return myPyMembers;
    }

    @Override
    public String toString() {
        return String.format("SyntheticPropertyMember: '%s', readOnly=%b", getName(), isReadOnly());
    }
}
