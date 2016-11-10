package com.wishtack.pysynthetic.impl;

import com.intellij.codeInsight.completion.*;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import com.jetbrains.python.PyTokenTypes;
import com.jetbrains.python.psi.PyTypedElement;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.PyUnionType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by Jean Hominal on 2016-11-06.
 */
public class SyntheticMembersCompletionContributor extends CompletionContributor {

    private static final Key<List<SyntheticTypeInfo>> SYNTHETIC_TYPE_INFO_KEY = new Key<>("SyntheticTypeInfoKey");

    private static final PatternCondition<PsiElement> PREVIOUS_SIBLING_HAS_SYNTHETIC_MEMBERS_PATTERN_CONDITION =
            new PatternCondition<PsiElement>("previousHasSyntheticMembers") {
                @Override
                public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                    PsiElement previous = element.getPrevSibling();

                    if (!(previous instanceof PyTypedElement)) {
                        return false;
                    }

                    TypeEvalContext typeEval = TypeEvalContext.codeCompletion(element.getProject(), element.getContainingFile());
                    PyType type = typeEval.getType((PyTypedElement)previous);

                    SmartList<SyntheticTypeInfo> nonTrivialSyntheticTypeInfo = new SmartList<>();

                    if (type instanceof PyClassType) {
                        SyntheticTypeInfo sti = new SyntheticTypeInfoReader(((PyClassType)type).getPyClass()).read();
                        if (!sti.getMembers().isEmpty()) {
                            nonTrivialSyntheticTypeInfo.add(sti);
                        }
                    } else if (type instanceof PyUnionType) {
                        for (PyType typePossibility : ((PyUnionType)type).getMembers()) {
                            if (typePossibility instanceof PyClassType) {
                                SyntheticTypeInfo sti = new SyntheticTypeInfoReader(((PyClassType)typePossibility).getPyClass()).read();
                                if (!sti.getMembers().isEmpty()) {
                                    nonTrivialSyntheticTypeInfo.add(sti);
                                }
                            }
                        }
                    }

                    if (nonTrivialSyntheticTypeInfo.isEmpty()) {
                        return false;
                    }

                    // Need to put the SyntheticTypeInfo inside the ProcessingContext,
                    // because reference.resolve() cannot be done in addCompletions,
                    // and SyntheticTypeInfo cannot be computed without resolving.
                    context.put(SYNTHETIC_TYPE_INFO_KEY, nonTrivialSyntheticTypeInfo);
                    return true;
                }
            };

    // Tip on writing these patterns: look inside
    // https://github.com/JetBrains/intellij-community/blob/master/python/testData/psi/
    // There is a series of test cases for sample Python code and the expected PSI trees.
    // The following capture was crafted by looking at the QualifiedTarget test case.
    // Warning: The 'afterSibling' method does not work, because it operates on an
    // Abstract Syntax Tree, not on the PsiElement tree.
    private static final PsiElementPattern.Capture<PsiElement> POTENTIAL_SYNTHETIC_MEMBER_ACCESS =
            psiElement(PyTokenTypes.DOT).with(PREVIOUS_SIBLING_HAS_SYNTHETIC_MEMBERS_PATTERN_CONDITION);

    public SyntheticMembersCompletionContributor() {
        extend(
            CompletionType.BASIC,
            psiElement().afterLeaf(POTENTIAL_SYNTHETIC_MEMBER_ACCESS),
            new SyntheticMembersCompletionProvider()
        );
    }

    private static class SyntheticMembersCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters,
                                      ProcessingContext context,
                                      @NotNull CompletionResultSet result) {
            List<SyntheticTypeInfo> typeInfoList = context.get(SYNTHETIC_TYPE_INFO_KEY);

            for (SyntheticTypeInfo typeInfo : typeInfoList) {
                result.addAllElements(typeInfo.getLookupElements());
            }
        }
    }
}
