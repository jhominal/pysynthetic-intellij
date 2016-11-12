package com.wishtack.pysynthetic.contracts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jean Hominal on 11/11/2016.
 */
@RunWith(Parameterized.class)
public class PyContractParsingTests {
    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        "",
                        null,
                },
                {
                        "None",
                        new NamedContractNode("None"),
                },
                {
                        "None|User",
                        new OrContractNode(Arrays.asList(new NamedContractNode("None"), new NamedContractNode("User")))
                },
                {
                        "User|None",
                        new OrContractNode(Arrays.asList(new NamedContractNode("User"), new NamedContractNode("None"))),
                },
                {
                        "SomeType1|SomeType2",
                        new OrContractNode(Arrays.asList(new NamedContractNode("SomeType1"), new NamedContractNode("SomeType2"))),
                },
                {
                        "None|SomeType1|SomeType2",
                        new OrContractNode(Arrays.asList(
                                new NamedContractNode("None"),
                                new NamedContractNode("SomeType1"),
                                new NamedContractNode("SomeType2")
                        )),
                },
                {
                        "a,b|c",
                        new OrContractNode(Arrays.asList(
                                new AndContractNode(Arrays.asList(
                                        new NamedContractNode("a"),
                                        new NamedContractNode("b")
                                )),
                                new NamedContractNode("c")
                        )),
                },
                {
                        "a, b | c",
                        new OrContractNode(Arrays.asList(
                                new AndContractNode(Arrays.asList(
                                        new NamedContractNode("a"),
                                        new NamedContractNode("b")
                                )),
                                new NamedContractNode("c")
                        )),
                },
                {
                        "(a,b)|c",
                        new OrContractNode(Arrays.asList(
                                new AndContractNode(Arrays.asList(
                                        new NamedContractNode("a"),
                                        new NamedContractNode("b")
                                )),
                                new NamedContractNode("c")
                        )),
                },
                {
                        "a, (b | c)",
                        new AndContractNode(Arrays.asList(
                                new NamedContractNode("a"),
                                new OrContractNode(Arrays.asList(
                                        new NamedContractNode("b"),
                                        new NamedContractNode("c")
                                ))
                        )),
                },
                {
                        "list",
                        new SequenceContractNode("list", null, null),
                },
                {
                        "list[2]",
                        new SequenceContractNode("list", new IntValueContractNode(null, 2), null),
                },
                {
                        "list(int)",
                        new SequenceContractNode("list", null, new NamedContractNode("int")),
                },
                {
                        "list(number)",
                        new SequenceContractNode("list", null, new NamedContractNode("number")),
                },
                {
                        "list[3](number)",
                        new SequenceContractNode("list", new IntValueContractNode(null, 3), new NamedContractNode("number")),
                },
                {
                        "list[>=3](number)",
                        new SequenceContractNode("list", new IntValueContractNode(">=", 3), new NamedContractNode("number")),
                },
                {
                        "list[>=3](number, >0)",
                        new SequenceContractNode(
                                "list",
                                new IntValueContractNode(">=", 3),
                                new AndContractNode(Arrays.asList(
                                        new NamedContractNode("number"),
                                        new IntValueContractNode(">", 0)
                                ))
                        ),
                },
                {
                        "tuple",
                        new TupleContractNode(null, Collections.emptyList()),
                },
                {
                        "tuple[2]",
                        new TupleContractNode(new IntValueContractNode(null, 2), Collections.emptyList()),
                },
                {
                        "tuple(*,*)",
                        new TupleContractNode(null, Arrays.asList(
                                new ConstantContractNode(true),
                                new ConstantContractNode(true)
                        )),
                },
                {
                        "tuple(int)",
                        new TupleContractNode(null, Collections.singletonList(
                                new NamedContractNode("int")
                        )),
                },
                {
                        "tuple(int, int)",
                        new TupleContractNode(null, Arrays.asList(
                                new NamedContractNode("int"),
                                new NamedContractNode("int")
                        )),
                },
                {
                        "tuple[>=2]",
                        new TupleContractNode(new IntValueContractNode(">=", 2), Collections.emptyList()),
                },
                {
                        "dict",
                        new MappingContractNode("dict", null, null, null),
                },
                {
                        "dict[2]",
                        new MappingContractNode("dict", new IntValueContractNode(null, 2), null, null),
                },
                {
                        "dict(*: *)",
                        new MappingContractNode(
                                "dict",
                                null,
                                new ConstantContractNode(true),
                                new ConstantContractNode(true)
                        ),
                },
                {
                        "dict(*: int)",
                        new MappingContractNode(
                                "dict",
                                null,
                                new ConstantContractNode(true),
                                new NamedContractNode("int")
                        ),
                },
                {
                        "dict(str: *)",
                        new MappingContractNode(
                                "dict",
                                null,
                                new NamedContractNode("str"),
                                new ConstantContractNode(true)
                        ),
                },
                {
                        "dict(str: int)",
                        new MappingContractNode(
                                "dict",
                                null,
                                new NamedContractNode("str"),
                                new NamedContractNode("int")
                        ),
                },
        });
    }

    private final String _contractExpression;
    private final ContractNode _expectedResult;

    public PyContractParsingTests(String contractExpression, ContractNode expectedResult) {
        _contractExpression = contractExpression;
        _expectedResult = expectedResult;
    }

    @Test
    public void testParse() {
        assertEquals(_expectedResult, PyContractsUtil.parse(_contractExpression));
    }

}
