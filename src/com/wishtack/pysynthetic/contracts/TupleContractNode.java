package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class TupleContractNode extends ContractNode {

    @Nullable
    private final IntValueContractNode lengthContract;

    @NotNull
    private final List<ContractNode> elements;

    TupleContractNode(@Nullable IntValueContractNode lengthContract, @NotNull List<ContractNode> elements) {
        this.lengthContract = lengthContract;
        this.elements = elements;
    }

    @Nullable
    public IntValueContractNode getLengthContract() {
        return lengthContract;
    }

    @NotNull
    public List<ContractNode> getElements() {
        return elements;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitTuple(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof TupleContractNode) {
            TupleContractNode other = (TupleContractNode)obj;
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
        return "Tuple(length=" + lengthContract + ",elements=" + ListUtil.toString(elements) + ")";
    }
}
