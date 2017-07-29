package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class AndContractNode extends ContractNode {

    @NotNull
    private final List<ContractNode> elements;

    AndContractNode(@NotNull List<ContractNode> elements) {
        this.elements = elements;
    }

    @NotNull
    public List<ContractNode> getElements() {
        return elements;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitAnd(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof AndContractNode) {
            AndContractNode other = (AndContractNode)obj;
            return ListUtil.equivalent(elements, other.elements);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return ListUtil.hashCode(elements);
    }

    @Override
    public String toString() {
        return "And(" + ListUtil.toString(elements) + ")";
    }
}
