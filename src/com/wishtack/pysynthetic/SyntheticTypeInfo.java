package com.wishtack.pysynthetic;

import com.jetbrains.python.codeInsight.PyCustomMember;
import com.jetbrains.python.psi.*;
import com.jetbrains.python.psi.resolve.PyResolveContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jean Hominal on 2016-11-01.
 */
public final class SyntheticTypeInfo {

    @NotNull
    public static SyntheticTypeInfo read(@NotNull PyClass pyClass) {

        PyDecoratorList decoratorList = pyClass.getDecoratorList();

        if (decoratorList == null) {
            return empty;
        }

        ArrayList<SyntheticMemberInfo> syntheticMemberInfoList = new ArrayList<>();
        boolean withConstructor = false;

        for (PyDecorator d : decoratorList.getDecorators()) {

            SyntheticMemberInfo m = null;

            switch (d.resolveCalleeFunction(PyResolveContext.defaultContext()).getQualifiedName()) {
                case "synthetic.decorators.synthesize_constructor":
                case "synthetic.decorators.synthesizeConstructor":
                    withConstructor = true;
                    break;
                case "synthetic.decorators.synthesize_property":
                    m = readProperty(d, false);
                    break;
                case "synthetic.decorators.synthesizeProperty":
                    m = readProperty(d, true);
                    break;
                case "synthetic.decorators.synthesize_member":
                    m = readMemberWithAccessors(d, false);
                    break;
                case "synthetic.decorators.synthesizeMember":
                    m = readMemberWithAccessors(d, true);
                    break;
            }

            if (m != null) {
                syntheticMemberInfoList.add(m);
            }
        }

        if (!withConstructor && syntheticMemberInfoList.isEmpty()) {
            return empty;
        }

        return new SyntheticTypeInfo(Collections.unmodifiableList(syntheticMemberInfoList), withConstructor);
    }

    private static SyntheticPropertyMember readProperty(PyDecorator decorator, boolean camelCase) {
        StringLiteralExpression memberNameExpression = decorator.getArgument(0, StringLiteralExpression.class);
        if (memberNameExpression == null) return null;

        boolean readOnly = readReadOnlyValue(decorator, camelCase);

        return new SyntheticPropertyMember(decorator, memberNameExpression.getStringValue(), readOnly);
    }

    private static SyntheticMemberWithAccessors readMemberWithAccessors(PyDecorator decorator, boolean camelCase) {
        StringLiteralExpression memberNameExpression = decorator.getArgument(0, StringLiteralExpression.class);
        if (memberNameExpression == null) return null;
        String memberName = memberNameExpression.getStringValue();

        boolean readOnly = readReadOnlyValue(decorator, camelCase);

        String getterName;
        PyExpression getterNameExpression = decorator.getKeywordArgument(camelCase ? "getterName" : "getter_name");
        if (getterNameExpression == null || !(getterNameExpression instanceof StringLiteralExpression)) {
            getterName = memberName;
        } else {
            getterName = ((StringLiteralExpression)getterNameExpression).getStringValue();
        }

        String setterName = null;
        if (!readOnly) {
            PyExpression setterNameExpression = decorator.getKeywordArgument(camelCase ? "setterName" : "setter_name");
            if (setterNameExpression == null || !(setterNameExpression instanceof StringLiteralExpression)) {
                if (camelCase) {
                    setterName = "set" + Character.toUpperCase(memberName.charAt(0)) + memberName.substring(1);
                } else {
                    setterName = "set_" + memberName;
                }
            } else {
                setterName = ((StringLiteralExpression)setterNameExpression).getStringValue();
            }
        }

        return new SyntheticMemberWithAccessors(decorator, memberName, getterName, setterName);
    }

    private static boolean readReadOnlyValue(PyDecorator decorator, boolean camelCase) {
        boolean result = false;
        PyExpression readOnlyExpression = decorator.getKeywordArgument(camelCase ? "readOnly" : "read_only");

        if (readOnlyExpression != null && readOnlyExpression instanceof PyReferenceExpression) {
            result = "True".equals(readOnlyExpression.getName());
        }
        return result;
    }

    private static final SyntheticTypeInfo empty = new SyntheticTypeInfo(Collections.emptyList(), false);

    @NotNull
    private final Collection<SyntheticMemberInfo> myMembers;
    private final boolean myWithConstructor;
    private Collection<PyCustomMember> myPyMembers;

    private SyntheticTypeInfo(@NotNull Collection<SyntheticMemberInfo> members, boolean withConstructor) {
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
