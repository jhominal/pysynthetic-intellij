package com.wishtack.pysynthetic.contracts;

import org.jetbrains.annotations.NotNull;

/**
 * A base class for every contract type returned by the API.
 *
 * Created by Jean Hominal on 2016-11-11
 */
public abstract class ContractNode {

    public abstract <TResult> TResult accept(@NotNull ContractNodeVisitor<TResult> visitor);

}
