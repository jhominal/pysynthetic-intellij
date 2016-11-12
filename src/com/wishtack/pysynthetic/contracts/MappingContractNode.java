package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public final class MappingContractNode extends ContractNode {

    @NotNull
    private final String mappingType;
    @Nullable
    private final IntValueContractNode lengthContract;
    @Nullable
    private final ContractNode keyContract;
    @Nullable
    private final ContractNode valueContract;

    public MappingContractNode(@NotNull String mappingType, @Nullable IntValueContractNode lengthContract, @Nullable ContractNode keyContract, @Nullable ContractNode valueContract) {
        this.mappingType = mappingType;
        this.lengthContract = lengthContract;
        this.keyContract = keyContract;
        this.valueContract = valueContract;
    }

    @NotNull
    public String getMappingType() {
        return mappingType;
    }

    @Nullable
    public IntValueContractNode getLengthContract() {
        return lengthContract;
    }

    @Nullable
    public ContractNode getKeyContract() {
        return keyContract;
    }

    @Nullable
    public ContractNode getValueContract() {
        return valueContract;
    }

    @Override
    public <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor) {
        return visitor.visitMapping(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj instanceof MappingContractNode) {
            MappingContractNode other = (MappingContractNode)obj;
            return Objects.equals(mappingType, other.mappingType) &&
                    Objects.equals(lengthContract, other.lengthContract) &&
                    Objects.equals(keyContract, other.keyContract) &&
                    Objects.equals(valueContract, other.valueContract);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + Objects.hashCode(mappingType);
        hashCode = 31 * hashCode + Objects.hashCode(lengthContract);
        hashCode = 31 * hashCode + Objects.hashCode(keyContract);
        hashCode = 31 * hashCode + Objects.hashCode(valueContract);
        return hashCode;
    }

    @Override
    public String toString() {
        return "Mapping(" + mappingType + ",length=" + Objects.toString(lengthContract) +
                ",key=" + Objects.toString(keyContract) + ",value=" + Objects.toString(valueContract) + ")";
    }
}
