package com.neutronio.phi;

/**
 * Phi Exceptions with Error Codes.
 * Following ranges are defined:
 * <ul>
 * <li>E0000 - E0999 - General Errors</li>
 * <li>E1000 - E1999 - Mostly IO Operations/IO Exceptions</li>
 * <li>E2000 - E2999 - Persistence, Serialization errors</li>
 * <li>E3000 - E3999 - ID errors for assets or gamedata</li>
 * <li>E4000 - E5999 - reserved</li>
 * </ul>
 */
public class PhiException extends RuntimeException {
    // Update with AstraXException extends PhiException
    // TODO needs framework specific error codes
    /*
        Instead of error codes I can just use classes... duh
     */
    public enum ErrorCode {

        // ======== General, low-level Errors ========
        /** Error not specified */
        E0000("Error not specified"),
        /** Something was not initialized properly */
        E0001("Initialization Error"),
        /** Validation error. Values are not consistent */
        E0003("Validation error"),
        /** Argument validation Error */
        E0004("Illegal Argument(s)"),
        /** Syntax error */
        E0005("Syntax Error"), // new

        // ==== reflection ====
        /** Class not found */
        E0010("Class not found"), // new
        /** Method not found */
        E0011("Method not found"), // new
        /** Class instantiation Error */
        E0012("Type could not be instantiated"), // AstraX E2003
        /** Program expected an annotation, but it was not found. */
        E0013("Annotation expected, but is missing"),

        // ==== IO ====
        /** File not found */
        E0020("File not found"),
        /** File already exists */
        E0021("File already exists"), // new
        /** Could not open file */
        E0022("Could not open file"),
        /** Could not open file due to file already in use */
        E0023("File already in use"),
        /** Could not open file due to no read access*/
        E0024("No access to file"),

        // ==== Serialization ====
        /** Could not read Object */
        E2000("Could not read Object"),
        /** Could not write Object */
        E2001("Could not write Object"),
        /** Version mismatch */
        E2002("Version mismatch"),
        /** Expected Field is missing */
        E2003("Missing field in parsed data"), // AstraX E2010

        // ID errors RE assets or gamedata
        // these are JSON Framework related actually!
        /** Generic error: could not find ID in a Repository or similar */
        E3000("Could not find ID"),
        /** Could not find a registered entity */
        E3001("Registered Entity not found"), // what does this mean? is this in use?
        /** Could not find Datapack */
        E3100("Datapack not found"), // very framework specific
        /** Could not find a texture in DataPack texture atlas */
        E3110("Texture not found"),
        /** Could not find TextureRegions in DataPack texture atlas*/
        E3111("TextureRegions not found"),
        /** Could not find data model */
        E3102("Data not found"), // matches Phi 2005 and AstraX E2010
        /** API Mismatch */ // this is related to reflection errors
        E3103("API Mismatch: provided data does not match expected API"),
        /** Type Mismatch */ // this is related to reflection errors
        E3104("Type mismatch: provided data does not match expected data"),
        ;

        private String message = "-";

        ErrorCode(String message) {
            this.message = message;
        }
    }
    private ErrorCode errorCode = ErrorCode.E0000;

    public PhiException() {
    }

    /**
     *
     * @param errorcode
     */
    public PhiException(ErrorCode errorcode) {
        super(getMessage(errorcode, (String) null));
        this.errorCode = errorcode;
    }

    /**
     *
     * @param errorcode
     * @param comment
     */
    public PhiException(ErrorCode errorcode, String comment) {
        super(getMessage(errorcode, comment));
        this.errorCode = errorcode;
    }

    /**
     *
     * @param errorcode
     * @param comment
     * @param cause
     */
    public PhiException(ErrorCode errorcode, String comment, Throwable cause) {
        super(getMessage(errorcode, comment), cause);
        this.errorCode = errorcode;
    }

    /**
     *
     * @param cause
     */
    public PhiException(Throwable cause) {
        super(getMessage(ErrorCode.E0000, null), cause);
    }

    /**
     *
     * @param errorcode
     * @param cause
     */
    public PhiException(ErrorCode errorcode, Throwable cause) {
        super(getMessage(errorcode, null), cause);
    }

    private static String getMessage(ErrorCode errorcode, String comment) {
        return "[" + errorcode.name() + "] " + errorcode.message + ( comment != null ? ": " + comment : "" );
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
