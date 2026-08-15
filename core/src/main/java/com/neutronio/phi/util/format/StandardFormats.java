package com.neutronio.phi.util.format;

/**
 * Describes standard formats for measurements, distances or currency.
 * In theory, also be used for more complex formatting with strings.
 */
public enum StandardFormats implements Formatter {

    // TODO write tests for this class
    // TODO make the formatter work with one value only
    // TODO Add additional rules: if formatting a float with INTEGER, make sure to convert it first
    INTEGER("%,d") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    FLOAT_AS_INTEGER("%,.0f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    DISTANCE("%,.0f"){
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    PERCENT("%.2f"){
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    WEIGHT("%.2f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    FACTOR("%.3f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    MONEY_ITEM("%,.2f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    MONEY_HUD("%09d") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    /**A positional 2D Vector. Requires 2 input arguments. */
    VECTOR2_POS("%,.1f | %,.1f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    },
    /** A count in float, for inventories*/
    FLOAT_COUNT("%,.1f") {
        @Override
        public String format(String format, Object... values) {
            if( values == null) throw new IllegalArgumentException("Value cannot be null!");
            return String.format(format, values);
        }
    };


    private String format = "";

    StandardFormats( String format) {
        this.format = format;
    }

    public String format(Object... values) {
        if( values == null) throw new IllegalArgumentException("Value cannot be null!");
        return format(this.format, values);
    }
}
