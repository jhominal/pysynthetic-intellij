package com.wishtack.pysynthetic;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.psi.PyNamedParameter;
import com.wishtack.pysynthetic.psi.SyntheticInitParameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * Created by Jean Hominal on 2016-11-01.
 */
public final class SyntheticTypeInfo {

    @NotNull
    private final Collection<SyntheticMemberInfo> myMembers;
    private final boolean mySyntheticConstructor;
    private List<LookupElement> myLookupElements;
    private Map<String, PsiElement> myResolveMap;
    private List<PyNamedParameter> mySyntheticInitParameters;

    SyntheticTypeInfo(@NotNull Collection<SyntheticMemberInfo> members, boolean syntheticConstructor) {
        myMembers = members;
        mySyntheticConstructor = syntheticConstructor;
    }

    public Collection<SyntheticMemberInfo> getMembers() {
        return myMembers;
    }

    public boolean hasSyntheticConstructor() {
        return mySyntheticConstructor;
    }

    @NotNull
    public List<LookupElement> getLookupElements() {
        if (myLookupElements == null) {

            if (myMembers.isEmpty()) {
                myLookupElements = Collections.emptyList();
                return myLookupElements;
            }

            List<LookupElement> lookupElements = new ArrayList<>(getMembers().size() * 2);

            for (SyntheticMemberInfo smi : getMembers()) {
                smi.fillLookupElementsList(lookupElements);
            }

            myLookupElements = Collections.unmodifiableList(lookupElements);
        }

        return myLookupElements;
    }

    @NotNull
    public Map<String, PsiElement> getResolveMap() {
        if (myResolveMap == null) {

            if (myMembers.isEmpty()) {
                myResolveMap = Collections.emptyMap();
                return myResolveMap;
            }

            Map<String, PsiElement> resolveMap = new HashMap<>(getMembers().size() * 2);

            for (SyntheticMemberInfo smi : getMembers()) {
                smi.fillPsiElementMap(resolveMap);
            }

            myResolveMap = Collections.unmodifiableMap(resolveMap);
        }

        return myResolveMap;
    }

    @NotNull
    public List<PyNamedParameter> getSyntheticInitParameters() {
        if (mySyntheticInitParameters == null) {

            if (!hasSyntheticConstructor()) {
                mySyntheticInitParameters = Collections.emptyList();
                return mySyntheticInitParameters;
            }

            ArrayList<PyNamedParameter> parameters = new ArrayList<>(getMembers().size());

            for (SyntheticMemberInfo smi : getMembers()) {
                parameters.add(new SyntheticInitParameter(smi));
            }

            mySyntheticInitParameters = Collections.unmodifiableList(parameters);
        }

        return mySyntheticInitParameters;
    }
}
