package com.wishtack.pysynthetic;

import com.jetbrains.python.psi.types.PyType;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jean Hominal on 2016-11-12.
 */
class PyContractAnalysisResult {

    @Nullable
    private final PyType contractType;
    private final boolean acceptNone;

    PyContractAnalysisResult(@Nullable PyType contractType, boolean acceptNone) {
        this.contractType = contractType;
        this.acceptNone = acceptNone;
    }

    @Nullable
    PyType getContractType() {
        return contractType;
    }

    boolean isAcceptNone() {
        return acceptNone;
    }
}
