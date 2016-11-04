package com.wishtack.pysynthetic;

import com.intellij.psi.PsiElement;
import com.intellij.util.PlatformIcons;
import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.types.PyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticPropertyMember extends SyntheticMemberInfo {

    private Collection<PyCustomMember> myPyMembers;

    public SyntheticPropertyMember(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly, @Nullable PyType memberType) {
        super(pyClass, definitionDecorator, name, readOnly, memberType);
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            String pyClassName = getDefinitionClass().getQualifiedName();

            PyCustomMember[] membersArray = new PyCustomMember[1];

            PyCustomMember propertyMember = new PyCustomMember(getName(), pyClassName, this::getPropertyType);
            propertyMember.withIcon(PlatformIcons.PROPERTY_ICON);
            propertyMember.toPsiElement(getDefinitionDecorator());

            membersArray[0] = propertyMember;

            myPyMembers = Collections.unmodifiableList(Arrays.asList(membersArray));
        }

        return myPyMembers;
    }

    private PyType getPropertyType(PsiElement context) {
        return getMemberType();
    }

    @Override
    public String toString() {
        return String.format("SyntheticPropertyMember: '%s', readOnly=%b", getName(), isReadOnly());
    }
}
