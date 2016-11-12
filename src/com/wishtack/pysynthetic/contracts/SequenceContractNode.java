package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class SequenceContractNode extends ContractNode {

    @NotNull
    private final String sequenceType;
    @Nullable
    private final IntValueContractNode lengthContract;
    @Nullable
    private final ContractNode elementContract;

    public SequenceContractNode(@NotNull String sequenceType, @Nullable IntValueContractNode lengthContract, @Nullable ContractNode elementContract) {

        this.sequenceType = sequenceType;
        this.lengthContract = lengthContract;
        this.elementContract = elementContract;
    }

    @NotNull
    public String getSequenceType() {
        return sequenceType;
    }

    @Nullable
    public IntValueContractNode getLengthContract() {
        return lengthContract;
    }

    @Nullable
    public ContractNode getElementContract() {
        return elementContract;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitSequence(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof SequenceContractNode) {
            SequenceContractNode other = (SequenceContractNode)obj;
            return Objects.equals(sequenceType, other.sequenceType) &&
                    Objects.equals(lengthContract, other.lengthContract) &&
                    Objects.equals(elementContract, other.elementContract);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + Objects.hashCode(sequenceType);
        hashCode = 31 * hashCode + Objects.hashCode(lengthContract);
        hashCode = 31 * hashCode + Objects.hashCode(elementContract);

        return hashCode;
    }

    @Override
    public String toString() {
        return "Sequence(" + sequenceType + ",length=" + Objects.toString(lengthContract) +
                ",element=" + Objects.toString(elementContract) + ")";
    }
}
