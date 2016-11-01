package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Jean Hominal on 2016-11-01.
 */
public abstract class SyntheticMemberInfo {
    @NotNull
    private final PyDecorator myDefinitionDecorator;
    @NotNull
    private final String myName;
    private final boolean myReadOnly;

    protected SyntheticMemberInfo(@NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly) {
        myDefinitionDecorator = definitionDecorator;
        myName = name;
        myReadOnly = readOnly;
    }

    protected PyDecorator getDefinitionDecorator() {
        return myDefinitionDecorator;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    public boolean isReadOnly() {
        return myReadOnly;
    }

    @NotNull
    public abstract Collection<PyCustomMember> getPyMembers();
}
