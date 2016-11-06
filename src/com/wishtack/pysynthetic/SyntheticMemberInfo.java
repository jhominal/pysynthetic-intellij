package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.types.PyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by Jean Hominal on 2016-11-01.
 */
public abstract class SyntheticMemberInfo {
    @NotNull
    private final PyClass myClass;
    @NotNull
    private final PyDecorator myDefinitionDecorator;
    @NotNull
    private final String myName;
    private final boolean myReadOnly;
    @Nullable
    private final PyType myMemberType;
    @Nullable
    private final PyExpression myDefaultValue;

    protected SyntheticMemberInfo(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly, @Nullable PyType memberType, PyExpression defaultValue) {
        myClass = pyClass;
        myDefinitionDecorator = definitionDecorator;
        myName = name;
        myReadOnly = readOnly;
        myMemberType = memberType;
        myDefaultValue = defaultValue;
    }

    protected PyClass getDefinitionClass() { return myClass; }

    public PyDecorator getDefinitionDecorator() {
        return myDefinitionDecorator;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    public boolean isReadOnly() {
        return myReadOnly;
    }

    @Nullable
    public PyType getMemberType() {
        return myMemberType;
    }

    @Nullable
    public PyExpression getDefaultValue() {
        return myDefaultValue;
    }

    @NotNull
    public abstract Collection<PyCustomMember> getPyMembers();
}
