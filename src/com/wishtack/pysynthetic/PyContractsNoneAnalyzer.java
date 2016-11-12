package com.wishtack.pysynthetic;

import com.jetbrains.python.PyNames;
import com.wishtack.pysynthetic.contracts.*;
import org.jetbrains.annotations.NotNull;

/**
 * Analyzes whether, for the given contract, None is an acceptable value.
 *
 * Created by Jean Hominal on 2016-11-12.
 */
public class PyContractsNoneAnalyzer extends ContractNodeVisitor<Boolean> {

    @Override
    public Boolean visitAnd(@NotNull AndContractNode andNode) {
        for (ContractNode element : andNode.getElements()) {
            if (!element.accept(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitConstant(@NotNull ConstantContractNode constantNode) {
        return constantNode.getValue();
    }

    @Override
    public Boolean visitIntValue(@NotNull IntValueContractNode intValueNode) {
        return false;
    }

    @Override
    public Boolean visitMapping(@NotNull MappingContractNode mappingNode) {
        return false;
    }

    @Override
    public Boolean visitNamed(@NotNull NamedContractNode namedNode) {
        return PyNames.NONE.equals(namedNode.getIdentifier());
    }

    @Override
    public Boolean visitOr(@NotNull OrContractNode orNode) {
        for (ContractNode element : orNode.getElements()) {
            if (element.accept(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean visitSequence(@NotNull SequenceContractNode sequenceNode) {
        return false;
    }

    @Override
    public Boolean visitTuple(@NotNull TupleContractNode tupleNode) {
        return false;
    }
}
