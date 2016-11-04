package com.wishtack.pysynthetic;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Jean Hominal on 2016-11-04.
 */
@RunWith(Parameterized.class)
public class PyContractsSimpleParserTests {

    @Parameters(name="''{0}'' => {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", null},
                {"None", null},
                {"None|User", "User"},
                {"User|None", "User"},
                {"SomeType1|SomeType2", "SomeType1"},
                {"None|SomeType1|SomeType2", "SomeType1"},
        });
    }

    private final String _contractExpression;
    private final String _expectedResult;

    public PyContractsSimpleParserTests(String contractExpression, String expectedResult) {
        _contractExpression = contractExpression;
        _expectedResult = expectedResult;
    }

    @Test
    public void testGetSimpleNonTrivialTypeContractTestCases() {
        assertEquals(_expectedResult, PyContractsSimpleParser.getSimpleNonTrivialTypeContractString(_contractExpression));
    }

}
