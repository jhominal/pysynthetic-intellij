package com.wishtack.pysynthetic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jean Hominal on 2016-11-04.
 */
public class PyContractsSimpleParser {
    @Nullable
    public static String getSimpleNonTrivialTypeContractString(@NotNull String contractString) {
        for (String individualContract : contractString.split("[|]")) {
            if (individualContract.length() == 0 || individualContract.equals("None")) {
                continue;
            }
            return individualContract;
        }
        return null;
    }
}
