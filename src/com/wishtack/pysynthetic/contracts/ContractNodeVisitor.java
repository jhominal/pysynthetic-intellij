package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;

/**
 * A visitor class for ContractNode, for implementation of various functionality.
 *
 * Created by Jean Hominal on 2016-11-12.
 */
public abstract class ContractNodeVisitor<TResult> {

    public TResult visitAnd(@NotNull AndContractNode andNode) {
        return null;
    }

    public TResult visitConstant(@NotNull ConstantContractNode constantNode) {
        return null;
    }

    public TResult visitIntValue(@NotNull IntValueContractNode intValueNode) {
        return null;
    }

    public TResult visitMapping(@NotNull MappingContractNode mappingNode) {
        return null;
    }

    public TResult visitNamed(@NotNull NamedContractNode namedNode) {
        return null;
    }

    public TResult visitOr(@NotNull OrContractNode orNode) {
        return null;
    }

    public TResult visitSequence(@NotNull SequenceContractNode sequenceNode) {
        return null;
    }

    public TResult visitTuple(@NotNull TupleContractNode tupleNode) {
        return null;
    }

}
