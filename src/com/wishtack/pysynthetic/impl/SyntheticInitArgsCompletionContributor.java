package com.wishtack.pysynthetic.impl;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import com.jetbrains.python.psi.PyCallExpression;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyKeywordArgument;
import com.jetbrains.python.psi.PyReferenceExpression;
import com.wishtack.pysynthetic.SyntheticMemberInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import com.wishtack.pysynthetic.SyntheticTypeInfoReader;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

/**
 * Created by Jean Hominal on 2016-11-05.
 *
 * This class was created by taking inspiration from the Python IntelliJ plugin
 * com.jetbrains.python.codeInsight.completion.PyStringFormatCompletionContributor
 *
 * This class is now only used for cases where __init__ is not declared on the class
 * with the synthetic constructor decorator - when __init__ is declared,
 * SyntheticTypeProvider.getCallableType is called by the IDE, which provides all
 * useful assistance.
 */
public class SyntheticInitArgsCompletionContributor extends CompletionContributor {

    private static final Key<SyntheticTypeInfo> SYNTHETIC_TYPE_INFO_KEY = new Key<>("SyntheticTypeInfoKey");

    private static final PatternCondition<PyReferenceExpression> SYNTHETIC_CTOR_CALL_PATTERN_CONDITION =
            new PatternCondition<PyReferenceExpression>("isSyntheticCtorCall") {
                @Override
                public boolean accepts(@NotNull PyReferenceExpression pyReferenceExpression, ProcessingContext context) {
                    PsiElement referencedElement = pyReferenceExpression.getReference().resolve();
                    if (!(referencedElement instanceof PyClass)) {
                        return false;
                    }
                    PyClass pyClass = (PyClass)referencedElement;
                    SyntheticTypeInfo sti = new SyntheticTypeInfoReader(pyClass).read();
                    if (sti.hasSyntheticConstructor() && pyClass.findInitOrNew(false, null) == null) {
                        // Need to put the SyntheticTypeInfo inside the ProcessingContext,
                        // because reference.resolve() cannot be done in addCompletions.
                        context.put(SYNTHETIC_TYPE_INFO_KEY, sti);
                        return true;
                    }
                    return false;
                }
            };

    private static final PsiElementPattern.Capture<PyKeywordArgument> SYNTHETIC_CTOR_ARGUMENT_CAPTURE =
            psiElement(PyKeywordArgument.class)
                    // The first parent is the argument list, the grand parent is the call.
                    .withSuperParent(2, psiElement(PyCallExpression.class)
                            // One of the children of the call is the reference to the callable object.
                            .withChild(psiElement(PyReferenceExpression.class).with(SYNTHETIC_CTOR_CALL_PATTERN_CONDITION)));

    // To provide completion for cases where the prefix matches an existing reference
    private static final PsiElementPattern.Capture<PyReferenceExpression> SYNTHETIC_CTOR_REFERENCE_ARGUMENT_CAPTURE =
            psiElement(PyReferenceExpression.class)
                    // The first parent is the argument list, the grand parent is the call.
                    .withSuperParent(2, psiElement(PyCallExpression.class)
                            // One of the children of the call is the reference to the callable object.
                            .withChild(psiElement(PyReferenceExpression.class).with(SYNTHETIC_CTOR_CALL_PATTERN_CONDITION)));


    public SyntheticInitArgsCompletionContributor() {
        extend(
            CompletionType.BASIC,
            or(
                psiElement().inside(SYNTHETIC_CTOR_ARGUMENT_CAPTURE),
                psiElement().inside(SYNTHETIC_CTOR_REFERENCE_ARGUMENT_CAPTURE)
            ),
            new SyntheticConstructorArgumentsCompletionProvider()
        );
    }

    private static class SyntheticConstructorArgumentsCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters,
                                      ProcessingContext processingContext,
                                      @NotNull CompletionResultSet result) {

            final PsiElement original = parameters.getOriginalPosition();
            if (original != null) {
                result = result.withPrefixMatcher(getPrefix(parameters.getOffset(), parameters.getOriginalFile()));
                SyntheticTypeInfo sti = processingContext.get(SYNTHETIC_TYPE_INFO_KEY);
                if (sti.hasSyntheticConstructor()) {
                    for (SyntheticMemberInfo smi : sti.getMembers()) {
                        LookupElement lookupElement =
                                LookupElementBuilder
                                        .create(smi.getName() + "=")
                                        .withIcon(PlatformIcons.PARAMETER_ICON);
                        result.addElement(lookupElement);
                    }
                }
            }
        }
    }

    @NotNull
    private static String getPrefix(int offset, @NotNull final PsiFile file) {
        if (offset > 0) {
            offset--;
        }
        final String text = file.getText();
        final StringBuilder prefixBuilder = new StringBuilder();
        while (offset > 0 && Character.isLetterOrDigit(text.charAt(offset))) {
            prefixBuilder.insert(0, text.charAt(offset));
            offset--;
        }
        return prefixBuilder.toString();
    }
}
