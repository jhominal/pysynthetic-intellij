package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class ConstantContractNode extends ContractNode {

    private final boolean value;

    public ConstantContractNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitConstant(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof ConstantContractNode) {
            ConstantContractNode other = (ConstantContractNode)obj;
            return value == other.value;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public String toString() {
        return "Constant(" + Boolean.toString(value) + ")";
    }
}
