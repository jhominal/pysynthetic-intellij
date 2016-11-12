package com.wishtack.pysynthetic.contracts;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.Var;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An object for parsing PyContract contract expressions.
 *
 * Created by Jean Hominal on 2016-11-11.
 */
public class PyContractsParser extends BaseParser<ContractNode> {

    public Rule Input() {
        return Sequence(Contract(), EOI);
    }

    public Rule Contract() {
        return PossibleOr();
    }

    public Rule PossibleOr() {
        Var<List<ContractNode>> elementContracts = new Var<>(new ArrayList<>());
        return Sequence(
                PossibleAnd(),
                elementContracts.get().add(pop()),
                ZeroOrMore(
                        '|',
                        Whitespace(),
                        PossibleAnd(),
                        elementContracts.get().add(pop())
                ),
                push(elementContracts.get().size() == 1
                        ? elementContracts.get().get(0)
                        : new OrContractNode(Collections.unmodifiableList(elementContracts.get()))
                )
        );
    }

    public Rule PossibleAnd() {
        Var<List<ContractNode>> elementContracts = new Var<>(new ArrayList<>());
        return Sequence(
                HighPriorityElement(),
                elementContracts.get().add(pop()),
                ZeroOrMore(
                        ',',
                        Whitespace(),
                        HighPriorityElement(),
                        elementContracts.get().add(pop())
                ),
                push(elementContracts.get().size() == 1
                        ? elementContracts.get().get(0)
                        : new AndContractNode(Collections.unmodifiableList(elementContracts.get()))
                )
        );
    }

    public Rule HighPriorityElement() {
        return FirstOf(
                Seq(),
                Tuple(),
                Map(),
                Named(),
                IntValue(),
                Constant(),
                Sequence(
                        '(',
                        Whitespace(),
                        Contract(),
                        ')',
                        Whitespace()
                )
        );
    }

    public Rule Seq() {
        Var<String> seqType = new Var<>();
        Var<IntValueContractNode> lengthContract = new Var<>();
        Var<ContractNode> elementContract = new Var<>();
        return Sequence(
                FirstOf("list", "seq"),
                seqType.set(match()),
                Whitespace(),
                Optional(
                        '[',
                        Whitespace(),
                        IntValue(),
                        lengthContract.set((IntValueContractNode) pop()),
                        ']',
                        Whitespace()
                ),
                Optional(
                        '(',
                        Whitespace(),
                        Contract(),
                        elementContract.set(pop()),
                        ')',
                        Whitespace()
                ),
                push(new SequenceContractNode(seqType.get(), lengthContract.get(), elementContract.get()))
        );
    }

    public Rule Tuple() {
        Var<IntValueContractNode> lengthContract = new Var<>();
        Var<List<ContractNode>> elementContracts = new Var<>(new ArrayList<>());

        return Sequence(
                "tuple",
                Whitespace(),
                Optional(FirstOf(
                        Sequence(
                                '[',
                                Whitespace(),
                                IntValue(),
                                lengthContract.set((IntValueContractNode) pop()),
                                ']',
                                Whitespace()
                        ),
                        Sequence(
                                '(',
                                Whitespace(),
                                PossibleOrTuple(),
                                elementContracts.get().add(pop()),
                                ZeroOrMore(
                                        ',',
                                        Whitespace(),
                                        PossibleOrTuple(),
                                        elementContracts.get().add(pop())
                                ),
                                ')',
                                Whitespace()
                        )
                )),
                push(new TupleContractNode(lengthContract.get(), Collections.unmodifiableList(elementContracts.get())))
        );
    }

    // Need a separate item for tuple element because precedence rules are not the same.
    public Rule PossibleOrTuple() {
        Var<List<ContractNode>> elementContracts = new Var<>(new ArrayList<>());
        return Sequence(
                HighPriorityElement(),
                elementContracts.get().add(pop()),
                ZeroOrMore(
                        '|',
                        Whitespace(),
                        HighPriorityElement(),
                        elementContracts.get().add(pop())
                ),
                push(elementContracts.get().size() == 1
                        ? elementContracts.get().get(0)
                        : new OrContractNode(Collections.unmodifiableList(elementContracts.get())))
        );
    }

    public Rule Map() {
        Var<String> mapType = new Var<>();
        Var<IntValueContractNode> lengthContract = new Var<>();
        Var<ContractNode> keyContract = new Var<>();
        Var<ContractNode> valueContract = new Var<>();
        return Sequence(
                FirstOf("dict", "map"),
                mapType.set(match()),
                Whitespace(),
                Optional(
                        '[',
                        Whitespace(),
                        IntValue(),
                        ']',
                        Whitespace(),
                        lengthContract.set((IntValueContractNode) pop())
                ),
                Optional(
                        '(',
                        Whitespace(),
                        Contract(),
                        keyContract.set(pop()),
                        ':',
                        Whitespace(),
                        Contract(),
                        valueContract.set(pop()),
                        ')',
                        Whitespace()
                ),
                push(new MappingContractNode(mapType.get(), lengthContract.get(), keyContract.get(), valueContract.get()))
        );
    }

    public Rule Named() {
        return Sequence(
                Sequence(
                        FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_'),
                        ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', CharRange('0', '9')))
                ),
                push(new NamedContractNode(match())),
                Whitespace()
        );
    }

    public Rule IntValue() {
        Var<String> operator = new Var<>();
        Var<String> value = new Var<>();

        return Sequence(
                Optional(
                        FirstOf(">=", ">", "<=", "<"),
                        operator.set(match())
                ),
                OneOrMore(CharRange('0', '9')),
                value.set(match()),
                Whitespace(),
                push(new IntValueContractNode(operator.get(), Integer.parseInt(value.get(), 10)))
        );
    }

    public Rule Constant() {
        return Sequence(
                AnyOf("#*"),
                push(new ConstantContractNode(matchedChar() == '*')),
                Whitespace()
        );
    }

    public Rule Whitespace() {
        return ZeroOrMore(
                AnyOf(" \t")
        );
    }
}
