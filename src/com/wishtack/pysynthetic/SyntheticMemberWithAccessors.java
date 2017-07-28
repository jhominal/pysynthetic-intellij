package com.wishtack.pysynthetic;

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.PlatformIcons;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.PyExpression;
import com.wishtack.pysynthetic.psi.SyntheticGetterCallable;
import com.wishtack.pysynthetic.psi.SyntheticSetterCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;


/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticMemberWithAccessors extends SyntheticMemberInfo {

    @NotNull
    private final String myGetterName;
    @Nullable
    private final String mySetterName;

    public SyntheticMemberWithAccessors(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, @NotNull String getterName, @Nullable String setterName, @NotNull PyContractAnalysisResult contractAnalysis, @Nullable PyExpression defaultValue) {
        super(pyClass, definitionDecorator, name, setterName == null, contractAnalysis, defaultValue);

        myGetterName = getterName;
        mySetterName = setterName;
    }

    @NotNull
    public String getGetterName() {
        return myGetterName;
    }

    @Nullable
    public String getSetterName() {
        return mySetterName;
    }

    @Override
    void fillLookupElementsList(@NotNull List<LookupElement> list) {
        LookupElement getterLookupElement =
                LookupElementBuilder.create(getGetterName())
                        .withTypeText(getDefinitionClass().getName())
                        .withTailText("(self)")
                        .withInsertHandler(ParenthesesInsertHandler.NO_PARAMETERS)
                        .withIcon(PlatformIcons.METHOD_ICON);
        list.add(getterLookupElement);

        if (getSetterName() != null) {
            LookupElement setterLookupElement =
                    LookupElementBuilder.create(getSetterName())
                            .withTypeText(getDefinitionClass().getName())
                            .withTailText("(self, value)")
                            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
                            .withIcon(PlatformIcons.METHOD_ICON);
            list.add(setterLookupElement);
        }
    }

    @Override
    void fillPsiElementMap(@NotNull Map<String, PsiElement> map) {
        map.putIfAbsent(getGetterName(), new SyntheticGetterCallable(this));
        if (getSetterName() != null) {
            map.putIfAbsent(getSetterName(), new SyntheticSetterCallable(this));
        }
    }

    @Override
    public String toString() {
        return String.format("SyntheticMemberWithAccessors: '%s', readOnly=%b, (%s/%s)", getName(), isReadOnly(), myGetterName, mySetterName);
    }
}
