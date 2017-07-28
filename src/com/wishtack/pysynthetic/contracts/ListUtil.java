package com.wishtack.pysynthetic.contracts;

import java.util.List;
import java.util.Objects;

/**
 * A class to easily implement equality and hashes on list elements.
 * If the lists are mutated, there will be issues with the hashCode generation.
 *
 * Created by Jean Hominal on 2016-11-11.
 */
final class ListUtil {

    static <T> boolean equivalent(List<T> first, List<T> second) {
        if (first == null) return second == null;
        if (second == null) return false;
        if (second == first) return true;

        if (first.size() != second.size()) return false;

        for (int i = 0; i < first.size(); i++) {
            if (!Objects.equals(first.get(i), second.get(i))) {
                return false;
            }
        }

        return true;
    }

    static int hashCode(List<?> list) {
        if (list == null) return 0;

        int hashCode = 1;
        for (Object element : list) {
            hashCode = 31 * hashCode + Objects.hashCode(element);
        }
        return hashCode;
    }

    static String toString(List<?> list) {
        if (list == null) return "null";

        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (Object element : list) {
            sb.append(element);
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        sb.append(']');

        return sb.toString();
    }
}
