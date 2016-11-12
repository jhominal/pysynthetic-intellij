package com.wishtack.pysynthetic.contracts;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

/**
 * Created by Jean Hominal on 2016-11-11.
 */
public class PyContractsUtil {

    private static final PyContractsParser FACTORY = Parboiled.createParser(PyContractsParser.class);;

    public static ContractNode parse(String contract) {
        PyContractsParser parser = FACTORY.newInstance();
        ParsingResult<ContractNode> result = new ReportingParseRunner<ContractNode>(parser.Input()).run(contract);
        if (result.matched) {
            return result.resultValue;
        }
        return null;
    }
}
