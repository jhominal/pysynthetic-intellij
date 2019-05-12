package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class IntValueContractNode extends ContractNode {

    @Nullable
    private final String operator;
    private final int value;

    IntValueContractNode(@Nullable String operator, int value) {
        this.operator = operator;
        this.value = value;
    }

    @Nullable
    public String getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitIntValue(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof IntValueContractNode) {
            IntValueContractNode other = (IntValueContractNode)obj;
            return value == other.value && Objects.equals(operator, other.operator);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = Objects.hashCode(operator);
        return hashCode ^ value;
    }

    @Override
    public String toString() {
        if (operator == null) {
            return "IntValue(" + value + ")";
        } else {
            return "IntValue(" + operator + value + ")";
        }
    }
}
