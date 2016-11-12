package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class NamedContractNode extends ContractNode {

    @NotNull
    private final String identifier;

    public NamedContractNode(@NotNull String identifier) {
        this.identifier = identifier;
    }

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitNamed(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof NamedContractNode) {
            NamedContractNode other = (NamedContractNode)obj;
            return Objects.equals(identifier, other.identifier);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return "Named(" + identifier + ")";
    }
}
