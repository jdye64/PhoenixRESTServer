package com.hortonworks.util;

import org.apache.commons.lang.StringUtils;

/**
 * Provides some common utility methods that assist in creating the Phoenix queries.
 *
 * Created by Jeremy Dyer on 7/15/15.
 */
public class PhoenixQueryUtil {

    public static String removeTailingSemiColon(String query) {
        if (query != null && StringUtils.length(query) > 0 && StringUtils.endsWith(query, ";")) {
            query = query.substring(0, query.length()-1);
        }
        return query;
    }
}
