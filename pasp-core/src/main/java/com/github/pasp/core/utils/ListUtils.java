package com.github.pasp.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/3/22.
 */
public class ListUtils {
    private static final List<Object> NULL_LIST = new ArrayList<Object>(1);

    static {
        NULL_LIST.add(null);
    }

    private ListUtils() {

    }

    public static void removeNullElements(List<?> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        list.removeAll(NULL_LIST);
    }

}
