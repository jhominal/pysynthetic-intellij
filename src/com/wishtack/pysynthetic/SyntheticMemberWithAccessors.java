package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
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

    public SyntheticMemberWithAccessors(@NotNull PyDecorator definitionDecorator, @NotNull String name, @NotNull String getterName, String setterName) {
        super(definitionDecorator, name, setterName == null);

        myGetterName = getterName;
        mySetterName = setterName;
    }

    @NotNull
    @Override
    public Collection<PyCustomMember> getPyMembers() {

        if (myPyMembers == null) {
            ArrayList<PyCustomMember> membersArray = new ArrayList<>(2);

            membersArray.add(new PyCustomMember(myGetterName, getDefinitionDecorator()).asFunction());

            if (mySetterName != null) {
                membersArray.add(new PyCustomMember(mySetterName, getDefinitionDecorator()).asFunction());
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
