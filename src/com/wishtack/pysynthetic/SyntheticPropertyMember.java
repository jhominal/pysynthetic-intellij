package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
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

    public SyntheticPropertyMember(@NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly) {
        super(definitionDecorator, name, readOnly);
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            ArrayList<PyCustomMember> membersArray = new ArrayList<>(1);

            membersArray.add(new PyCustomMember(getName(), getDefinitionDecorator()));

            myPyMembers = Collections.unmodifiableList(membersArray);
        }

        return myPyMembers;
    }

    @Override
    public String toString() {
        return String.format("SyntheticPropertyMember: '%s', readOnly=%b", getName(), isReadOnly());
    }
}
