package com.wishtack.pysynthetic;

import com.jetbrains.python.psi.types.PyType;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jean Hominal on 2016-11-12.
 */
public class PyContractAnalysisResult {

    @Nullable
    private final PyType contractType;
    private final boolean acceptNone;

    public PyContractAnalysisResult(@Nullable PyType contractType, boolean acceptNone) {
        this.contractType = contractType;
        this.acceptNone = acceptNone;
    }

    @Nullable
    public PyType getContractType() {
        return contractType;
    }

    public boolean isAcceptNone() {
        return acceptNone;
    }
}
