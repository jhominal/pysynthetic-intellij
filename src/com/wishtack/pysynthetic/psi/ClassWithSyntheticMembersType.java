package com.wishtack.pysynthetic.psi;

import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.jetbrains.python.psi.AccessDirection;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyExpression;
import com.jetbrains.python.psi.impl.ResolveResultList;
import com.jetbrains.python.psi.resolve.PyResolveContext;
import com.jetbrains.python.psi.resolve.RatedResolveResult;
import com.jetbrains.python.psi.types.PyClassType;
import com.jetbrains.python.psi.types.PyClassTypeImpl;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.wishtack.pysynthetic.SyntheticTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Jean Hominal
 */
public class ClassWithSyntheticMembersType extends PyClassTypeImpl {

    @NotNull
    private final SyntheticTypeInfo myTypeInfo;

    public ClassWithSyntheticMembersType(@NotNull SyntheticTypeInfo syntheticTypeInfo, @NotNull PyClass source, boolean isDefinition) {
        super(source, isDefinition);
        myTypeInfo = syntheticTypeInfo;
    }

    @Nullable
    @Override
    public List<? extends RatedResolveResult> resolveMember(@NotNull String name, @Nullable PyExpression location, @NotNull AccessDirection direction, @NotNull PyResolveContext resolveContext, boolean inherited) {

        PsiElement resolvedSyntheticMember = myTypeInfo.getResolveMap().get(name);
        if (resolvedSyntheticMember != null) {
            return ResolveResultList.to(resolvedSyntheticMember);
        }

        return super.resolveMember(name, location, direction, resolveContext, inherited);
    }

    @Override
    public void visitMembers(@NotNull Processor<PsiElement> processor, boolean inherited, @NotNull TypeEvalContext context) {

        boolean continuing;

        for (PsiElement syntheticMember : myTypeInfo.getResolveMap().values()) {
            continuing = processor.process(syntheticMember);
            if (!continuing) {
                return;
            }
        }

        super.visitMembers(processor, inherited, context);
    }

    @NotNull
    @Override
    public Set<String> getMemberNames(boolean inherited, @NotNull TypeEvalContext context) {
        Set<String> result = super.getMemberNames(inherited, context);
        result.addAll(myTypeInfo.getResolveMap().keySet());
        return result;
    }

    @Override
    public Object[] getCompletionVariants(String prefix, PsiElement location, ProcessingContext context) {
        return Stream.concat(
                myTypeInfo.getLookupElements().stream(),
                Arrays.stream(super.getCompletionVariants(prefix, location, context))
        ).toArray();
    }

    @Override
    public PyClassType toInstance() {
        return this.isDefinition() ? new ClassWithSyntheticMembersType(myTypeInfo, getPyClass(), false) : this;
    }
}
