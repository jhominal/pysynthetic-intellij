package com.wishtack.pysynthetic.impl;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.psi.*;
import com.jetbrains.python.psi.types.*;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import com.wishtack.pysynthetic.psi.ClassWithSyntheticMembersType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Jean Hominal on 2016-11-05.
 */
public class SyntheticTypeProvider extends PyTypeProviderBase {

    @Nullable
    @Override
    public PyType getCallableType(@NotNull PyCallable callable, @NotNull TypeEvalContext context) {
        if (!(callable instanceof PyPossibleClassMember)) {
            return null;
        }
        PyClass pyClass = ((PyPossibleClassMember)callable).getContainingClass();
        if (pyClass == null) {
            return null;
        }

        SyntheticTypeInfo info = new SyntheticTypeInfoReader(pyClass).read();
        if (info.hasSyntheticConstructor() && callable.equals(pyClass.findInitOrNew(false, null))) {
            PyParameter[] originalParameters = callable.getParameterList().getParameters();
            List<PyNamedParameter> syntheticParameters = info.getSyntheticInitParameters();

            // Get the index of the **kwargs arguments, so that synthetic parameters
            // can be inserted before it.
            int originalKeywordContainerIndex = originalParameters.length;
            for (int i = 0; i < originalParameters.length; i++) {
                PyNamedParameter namedParameter = originalParameters[i].getAsNamed();
                if (namedParameter != null && namedParameter.isKeywordContainer()) {
                    originalKeywordContainerIndex = i;
                    break;
                }
            }

            // Collect argument names in order to avoid producing duplicates.
            // Synthetic parameters lose to any parameter named in the original __init__.
            // This does not prevent duplicates if the original definition contains them, but that
            // does not really matter as the IDE will complain loudly in that case anyway.
            HashSet<String> unavailableParameterNames = new HashSet<>(originalParameters.length);
            for (PyParameter originalParameter : originalParameters) {
                unavailableParameterNames.add(originalParameter.getName());
            }

            ArrayList<PyCallableParameter> allInitParameters = new ArrayList<>(originalParameters.length + syntheticParameters.size());

            for (int i = 0; i < originalKeywordContainerIndex; i++) {
                allInitParameters.add(new PyCallableParameterImpl(originalParameters[i]));
            }

            for (PyNamedParameter syntheticParameter : syntheticParameters) {
                if (unavailableParameterNames.add(syntheticParameter.getName())) {
                    allInitParameters.add(new PyCallableParameterImpl(syntheticParameter));
                }
            }

            for (int i = originalKeywordContainerIndex; i < originalParameters.length; i++) {
                allInitParameters.add(new PyCallableParameterImpl(originalParameters[i]));
            }

            return new PyCallableTypeImpl(Collections.unmodifiableList(allInitParameters), context.getReturnType(callable));
        }

        return super.getCallableType(callable, context);
    }

    @Nullable
    @Override
    public PyType getCallType(@NotNull PyFunction function, @Nullable PyCallSiteExpression callSite, @NotNull TypeEvalContext context) {

//        if (callSite instanceof PyCallExpression) {
//            PyCallExpression call = (PyCallExpression)callSite;
//            PyExpression callee = call.getCallee();
//            if (callee != null) {
//                PyType calleeType = context.getType(callee);
//                if (calleeType instanceof PyClassType) {
//                    PyClassType pyClassType = (PyClassType)calleeType;
//                    if (pyClassType.isDefinition()) {
//                        PyClass pyClass = pyClassType.getPyClass();
//                        if (function.equals(pyClass.findInitOrNew(true, context))) {
//                            SyntheticTypeInfo sti = new SyntheticTypeInfoReader(pyClass).read();
//                            if (!sti.getMembers().isEmpty()) {
//                                return new ClassWithSyntheticMembersType(sti, pyClass, false);
//                            }
//                        }
//                    }
//                }
//            }
//            if (callee instanceof PyReferenceExpression) {
//                PsiElement resolvedCallee = ((PyReferenceExpression)callee).getReference().resolve();
//                if (resolvedCallee instanceof PyClass) {
//                    PyClass pyClass = (PyClass)resolvedCallee;
//                    SyntheticTypeInfo sti = new SyntheticTypeInfoReader(pyClass).read();
//                    if (!sti.getMembers().isEmpty()) {
//                        return new ClassWithSyntheticMembersType(sti, pyClass, false);
//                    }
//                }
//            }
//        }

        return super.getCallType(function, callSite, context);
    }

    @Override
    public PyType getReferenceType(@NotNull PsiElement referenceTarget, TypeEvalContext context, @Nullable PsiElement anchor) {

//        if (referenceTarget instanceof PyTypedElement) {
//            PyType referenceType = context.getType((PyTypedElement)referenceTarget);
//            if (referenceType instanceof PyClassType) {
//                PyClass pyClass = ((PyClassType)referenceType).getPyClass();
//                SyntheticTypeInfo sti = new SyntheticTypeInfoReader(pyClass).read();
//                if (!sti.getMembers().isEmpty()) {
//                    return new ClassWithSyntheticMembersType(sti, pyClass, referenceTarget.equals(pyClass));
//                }
//            }
//        }

        return super.getReferenceType(referenceTarget, context, anchor);
    }
}
