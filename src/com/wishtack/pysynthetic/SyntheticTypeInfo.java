package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01.
 */
public final class SyntheticTypeInfo {

    @NotNull
    private final Collection<SyntheticMemberInfo> myMembers;
    private final boolean myWithConstructor;
    private Collection<PyCustomMember> myPyMembers;

    SyntheticTypeInfo(@NotNull Collection<SyntheticMemberInfo> members, boolean withConstructor) {
        myMembers = members;
        myWithConstructor = withConstructor;
    }

    public Collection<SyntheticMemberInfo> getMembers() {
        return myMembers;
    }

    public boolean withConstructor() {
        return myWithConstructor;
    }

    @NotNull
    public Collection<PyCustomMember> getPyMembers() {
        if (myPyMembers == null) {
            ArrayList<PyCustomMember> typeMembers = new ArrayList<>();

            for (SyntheticMemberInfo smi : getMembers()) {
                typeMembers.addAll(smi.getPyMembers());
            }

            myPyMembers = Collections.unmodifiableList(typeMembers);
        }

        return myPyMembers;
    }
}
