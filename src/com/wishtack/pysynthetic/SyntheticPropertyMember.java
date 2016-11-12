package com.wishtack.pysynthetic;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyDecorator;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.types.PyType;
import com.wishtack.pysynthetic.psi.SyntheticPropertyElement;
import icons.PythonIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;


/**
 * Created by Jean Hominal on 2016-11-01
 */
public final class SyntheticPropertyMember extends SyntheticMemberInfo {

    public SyntheticPropertyMember(@NotNull PyClass pyClass, @NotNull PyDecorator definitionDecorator, @NotNull String name, boolean readOnly, @NotNull PyContractAnalysisResult contractAnalysis, @Nullable PyExpression defaultValue) {
        super(pyClass, definitionDecorator, name, readOnly, contractAnalysis, defaultValue);
    }

    @Override
    void fillLookupElementsList(@NotNull List<LookupElement> list) {
        LookupElement propertyLookupElement =
                LookupElementBuilder.create(getName())
                        .withTypeText(getDefinitionClass().getName())
                        .withIcon(isReadOnly() ? PythonIcons.Python.PropertyGetter : PythonIcons.Python.PropertySetter);
        list.add(propertyLookupElement);
    }

    @Override
    void fillPsiElementMap(@NotNull Map<String, PsiElement> map) {
        map.putIfAbsent(getName(), new SyntheticPropertyElement(this));
    }

    @Override
    public String toString() {
        return String.format("SyntheticPropertyMember: '%s', readOnly=%b", getName(), isReadOnly());
    }
}
