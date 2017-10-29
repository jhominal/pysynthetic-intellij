package com.wishtack.pysynthetic;

import com.jetbrains.python.PyNames;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.types.*;
import com.wishtack.pysynthetic.contracts.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Produce types that are useful for IDE assistance (auto-completion, member resolution, etc.)
 * The types produced by this class are not useful for type checking.
 *
 * Created by Jean Hominal on 2016-11-12.
 */
public class PyContractsTypeComputer extends ContractNodeVisitor<PyType> {

    private final PyClass definitionClass;

    PyContractsTypeComputer(@NotNull PyClass definitionClass) {
        this.definitionClass = definitionClass;
    }

    @Override
    public PyType visitNamed(@NotNull NamedContractNode namedNode) {
        if (PyNames.NONE.equals(namedNode.getIdentifier())) {
            return PyNoneType.INSTANCE;
        }
        return PyTypeParser.getTypeByName(definitionClass, namedNode.getIdentifier());
    }

    @Override
    public PyType visitOr(@NotNull OrContractNode orNode) {
        ArrayList<PyType> elementTypes = new ArrayList<>(orNode.getElements().size());
        boolean addedNull = false;

        for (ContractNode orElement : orNode.getElements()) {
            PyType elementType = orElement.accept(this);
            if (elementType != null) {
                elementTypes.add(elementType);
            } else if (!addedNull) {
                // Add member even if null, because it results in a weak union, which corresponds to the case where we
                // lack type information in one of the union options. However, only add it once to avoid duplicates.
                addedNull = true;
                elementTypes.add(null);
            }
        }

        switch (elementTypes.size()) {
            case 0:
                return null;
            case 1:
                return elementTypes.get(0);
            default:
                return PyUnionType.union(Collections.unmodifiableCollection(elementTypes));
        }
    }

    @Override
    public PyType visitAnd(@NotNull AndContractNode andNode) {
        ArrayList<PyType> elementTypes = new ArrayList<>(andNode.getElements().size());

        for (ContractNode andElement : andNode.getElements()) {
            PyType elementType = andElement.accept(this);
            // Do not add null members because we do not want a weak union if one of the subcontracts is untyped.
            if (elementType != null) {
                elementTypes.add(elementType);
            }
        }

        switch (elementTypes.size()) {
            case 0:
                return null;
            case 1:
                return elementTypes.get(0);
            default:
                // Using PyUnionType because:
                // 1. I do not see an intersection type for this need.
                // 2. The implementation of the members of PyType is be the same for PyUnionType and PyIntersectionType:
                //    in both cases, getCompletionVariants, getMembers and resolveMember must consider the union of
                //    the members of all component types.
                return PyUnionType.union(Collections.unmodifiableCollection(elementTypes));
        }
    }

    @Override
    public PyType visitConstant(@NotNull ConstantContractNode constantNode) {
        // Return null - in both cases there is no useful type information to forward.
        return null;
    }

    @Override
    public PyType visitIntValue(@NotNull IntValueContractNode intValueNode) {
        return PyTypeParser.getTypeByName(definitionClass, "numbers.Number");
    }

    @Override
    public PyType visitMapping(@NotNull MappingContractNode mappingNode) {
        if ("map".equals(mappingNode.getMappingType())) {
            return PyTypeParser.getTypeByName(definitionClass, "collections.MutableMapping");
        } else {
            return PyTypeParser.getTypeByName(definitionClass, "dict");
        }
    }

    @Override
    public PyType visitSequence(@NotNull SequenceContractNode sequenceNode) {
        if ("seq".equals(sequenceNode.getSequenceType())) {
            return PyTypeParser.getTypeByName(definitionClass, "collections.MutableSequence");
        } else {
            return PyTypeParser.getTypeByName(definitionClass, "list");
        }
    }

    @Override
    public PyType visitTuple(@NotNull TupleContractNode tupleNode) {
        // Code inserted because it feels right, but it currently has no observable effect.
        if (tupleNode.getLengthContract() != null) {
            if (tupleNode.getLengthContract().getOperator() == null) {
                // The array is filled with null, which is what we want.
                @SuppressWarnings("MismatchedReadAndWriteOfArray")
                PyType[] elementTypes = new PyType[tupleNode.getLengthContract().getValue()];
                return PyTupleType.create(definitionClass, Arrays.asList(elementTypes));
            }
        } else if (tupleNode.getElements().size() != 0) {
            PyType[] elementTypes = new PyType[tupleNode.getElements().size()];
            for (int i = 0; i < elementTypes.length; i++) {
                elementTypes[i] = tupleNode.getElements().get(i).accept(this);
            }
            return PyTupleType.create(definitionClass, Arrays.asList(elementTypes));
        }

        return PyTypeParser.getTypeByName(definitionClass, "tuple");
    }
}
