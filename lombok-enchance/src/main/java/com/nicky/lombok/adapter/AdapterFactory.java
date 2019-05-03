package com.nicky.lombok.adapter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/5/27 at 22:47
 */
public final class AdapterFactory {


    public static String builderStringAdapter(int capacity) {
        return new StringAdapter(capacity).toString();
    }

    public static String builderStyleAdapter(Object t) {
        return new StyleAdapter(t, ToStringStyle.JSON_STYLE).toString();
    }

    private static class StringAdapter implements AdapteeTarget {

        private int capacity;

        StringAdapter(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public String toString() {
            return builderToString(capacity);
        }
    }

    private static class StyleAdapter implements AdapteeTarget {

        private Object t;

        private ToStringStyle stringStyle;

        StyleAdapter(Object t, ToStringStyle style) {
            this.t = t;
            this.stringStyle = style;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(t, stringStyle);
        }

    }

}
