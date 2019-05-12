package com.wishtack.pysynthetic.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.jetbrains.python.PyElementTypes;
import com.jetbrains.python.psi.*;
import com.jetbrains.python.psi.stubs.PyParameterListStub;
import com.jetbrains.python.psi.types.*;
import com.wishtack.pysynthetic.SyntheticMemberInfo;
import com.wishtack.pysynthetic.SyntheticMemberWithAccessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jean Hominal on 2016-11-06.
 */
public class SyntheticGetterCallable extends AbstractAccessor {

    @NotNull
    private final SyntheticMemberWithAccessors myMemberInfo;

    @NotNull
    private final PyParameterList myParameterList;

    @NotNull
    private final List<PyCallableParameter> myParameters;

    public SyntheticGetterCallable(@NotNull SyntheticMemberWithAccessors memberInfo) {
        super(memberInfo.getDefinitionDecorator().getNode());
        myMemberInfo = memberInfo;
        myParameterList = new ParameterList(memberInfo);
        myParameters = Collections.unmodifiableList(
                Arrays.stream(myParameterList.getParameters())
                        .map(PyCallableParameterImpl::psi)
                        .collect(Collectors.toList())
        );
    }

    private final class ParameterList extends ASTWrapperPsiElement implements PyParameterList {

        @NotNull
        private final PyParameter[] myParameters;

        ParameterList(SyntheticMemberInfo memberInfo) {
            super(memberInfo.getDefinitionDecorator().getNode());
            myParameters = new PyNamedParameter[] {
                    new VirtualParameter(new PyClassTypeImpl(memberInfo.getDefinitionClass(), false), "self", true),
            };
        }

        @NotNull
        @Override
        public PyParameter[] getParameters() {
            return myParameters;
        }

        @Nullable
        @Override
        public PyNamedParameter findParameterByName(@NotNull String s) {
            switch (s) {
                case "self":
                    return myParameters[0].getAsNamed();
            }
            return null;
        }

        @Override
        public void addParameter(PyNamedParameter pyNamedParameter) {
        }

        @Override
        public boolean hasPositionalContainer() {
            return false;
        }

        @Override
        public boolean hasKeywordContainer() {
            return false;
        }

        @Override
        public String getPresentableText(boolean b) {
            return "(self)";
        }

        @NotNull
        @Override
        public String getPresentableText(boolean b, @Nullable TypeEvalContext typeEvalContext) {
            return "(self)";
        }

        @Override
        public PyFunction getContainingFunction() {
            return SyntheticGetterCallable.this;
        }

        @Override
        public IStubElementType getElementType() {
            return PyElementTypes.PARAMETER_LIST;
        }

        @Override
        public PyParameterListStub getStub() {
            return null;
        }
    }

    @NotNull
    @Override
    public List<PyCallableParameter> getParameters(@NotNull TypeEvalContext typeEvalContext) {
        return myParameters;
    }

    @NotNull
    @Override
    public PyParameterList getParameterList() {
        return myParameterList;
    }

    @Nullable
    @Override
    public PyType getReturnType(@NotNull TypeEvalContext typeEvalContext, @NotNull TypeEvalContext.Key key) {
        return myMemberInfo.getMemberType();
    }

    @Nullable
    @Override
    public PyType getReturnStatementType(@NotNull TypeEvalContext typeEvalContext) {
        return myMemberInfo.getMemberType();
    }

    @Nullable
    @Override
    public PyType getCallType(@NotNull TypeEvalContext typeEvalContext, @NotNull PyCallSiteExpression pyCallSiteExpression) {
        return myMemberInfo.getMemberType();
    }

    @Nullable
    @Override
    public PyType getCallType(@Nullable PyExpression pyExpression, @NotNull Map<PyExpression, PyCallableParameter> map, @NotNull TypeEvalContext typeEvalContext) {
        return myMemberInfo.getMemberType();
    }

    @Nullable
    @Override
    public PyClass getContainingClass() {
        return myMemberInfo.getDefinitionClass();
    }

    @Override
    public String getName() {
        return myMemberInfo.getGetterName();
    }

    @Nullable
    @Override
    public String getQualifiedName() {
        return myMemberInfo.getDefinitionClass().getQualifiedName() + "." + myMemberInfo.getGetterName();
    }

    @Nullable
    @Override
    public PyType getType(@NotNull TypeEvalContext typeEvalContext, @NotNull TypeEvalContext.Key key) {
        return new PyCallableTypeImpl(myParameters, myMemberInfo.getMemberType());
    }
}
