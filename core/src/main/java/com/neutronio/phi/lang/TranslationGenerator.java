package com.neutronio.phi.lang;

import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.io.JavaFileHandler;
import com.neutronio.phi.util.format.DateFormats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * A code generator tool that generates a class based on a properties file used for localization.
 * The class will have a field for each property, e.g. for property "button_text=..."
 * there will be a public static String field called "BUTTON_TEXT" with an assigned value of "button_text" as
 * translation identifier.
 */
public class TranslationGenerator { // TODO rename to CodeExporter?

    // TODO csv import/export would be nice
    // TODO this should use a configurable FileHandler everywhere
    public static class TranslationConfiguration {
        /** Used for enum translations that differ from the actual enum name, and offers
         * possibility to add name and description for each. */
        public EnumTranslations enumTranslations;
        /** Used for simple enum translation (name only) */
        public List<Class<? extends Enum>> enums = new ArrayList<>();
        /** Use for translations from a property file. */
        public List<Properties> properties = new ArrayList<>();
    }

    /**
     * Turns a properties object into a list of property keys.
     * @param properties
     */
    public static List<String> convertToIdentifiers(Properties properties) {
        List<String> propertyList = new ArrayList<>();
        Enumeration<Object> keys = properties.keys();
        while( keys.hasMoreElements() ) {
            propertyList.add((String) keys.nextElement());
        }
        Collections.sort(propertyList);
        return propertyList;
    }

    /**
     * Creates a list of translation identifiers for an enum,
     * for an enum variable called FieldType.METHOD the output identifier is "FieldType_method"
     * @param values
     * @return
     */
    public static List<String> convertToIdentifiers(Enum[] values) {
        List<String> propertyList = new ArrayList<>();
        for(Enum element : values) {
            propertyList.add(element.getClass().getSimpleName() + "_" + element.name().toLowerCase() +"_name");
        }
        return propertyList;
    }

    /**
     * Lists up all available identifiers found in an {@link com.neutronio.phi.lang.EnumTranslations.EnumTranslation}
     * object. Skips null description identifiers
     * @param enumTranslations
     * @return
     */
    public static List<String> convertToIdentifiers(EnumTranslations enumTranslations) {
        List<String> propertyList = new ArrayList<>();
        Map<Enum, EnumTranslations.EnumTranslation> enumMap = enumTranslations.getEnumTranslations();
        for(EnumTranslations.EnumTranslation translation : enumMap.values()) {
            propertyList.add(translation.nameTranslation);
            if(translation.descriptionTranslation != null) propertyList.add(translation.descriptionTranslation);
        }
        return propertyList;
    }

    /**
     * Generates code for a class containing the specified property keys.
     * For a property "button_text=..." there will be a public static String field called "BUTTON_TEXT"
     * with an assigned value of "button_text" as translation identifier.
     * @param className
     * @param propertyKeys null for line break
     * @return
     */
    public String generateCode(String packageName, String className, List<String> propertyKeys) {
        String packageCode = String.format( "package %s;\n\n", packageName) ;
        String comment = "/**\n" +
                " * A generated class providing access to translation identifiers.\n"+
                " * Do not manually edit this! Instead, add translation strings to the \n" +
                " * translation properties file and run the generator again. \n" +
                " * \n"+
                " * Generated: " + DateFormats.FULL.format(new Date()) + "\n" +
                " * \n" +
                " * @see " + this.getClass().getCanonicalName() + "\n" +
                " */\n" ;
        String classCode = "public class %s {\n%s}";
        StringBuilder fields = new StringBuilder();
        for(String key : propertyKeys) {
            if(key != null) {
                fields.append("   ").append("public static final String ").append(key.toUpperCase()).append(" = ").append("\"").append(key).append("\";\n");
            } else {
                fields.append("\n");
            }

        }
        return packageCode + comment + String.format(classCode, className, fields);
    }

    /**
     * Generates code for the given configuration
     * @param packageName
     * @param className
     * @param configuration
     * @return
     */
    public String generateCode(String packageName, String className, TranslationConfiguration configuration) {
        List<String> identifiers = new ArrayList<>();

        for(Properties properties : configuration.properties) {
            identifiers.addAll(convertToIdentifiers(properties));
        }
        identifiers.add(null);
        for(Class<? extends Enum> enums : configuration.enums) {
            identifiers.addAll(convertToIdentifiers(enums.getEnumConstants()));
            identifiers.add(null);
        }

        if(configuration.enumTranslations != null) {
            identifiers.addAll(convertToIdentifiers(configuration.enumTranslations));
        }

        return this.generateCode(packageName, className, identifiers);
    }

    /**
     * Generates a class containing translation identifiers based on sourcePropertyFile, then saves it as .java class to the specified package
     * and destination path.
     * @param sourcePropertyFile the source property file that contains translations. Use the main translation file for this
     * @param className the name of the class to be generated
     * @param packageName in shape of <pre>com.test.app</pre>. The package where the class should be located. This will be used as file path too.
     * @param destPath the destination path, the bit that comes before the package name (e.g. src/main/java/)
     */
    public void generateAndSaveCode(String sourcePropertyFile, String className, String packageName, String destPath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(sourcePropertyFile));
        } catch (IOException e) {

            e.printStackTrace();
        }

        List<String> convert = this.convertToIdentifiers(properties);

        String generatedCode = this.generateCode(packageName, className, convert);

        JavaFileHandler javaFileHandler = new JavaFileHandler();
        String destination = destPath + "/"+ packageName.replace('.', '/') + "/" + className + ".java";
        try {
            File file = new File(destination);
            if(!file.exists()) file.createNewFile();
            javaFileHandler.saveFileContents(generatedCode, file.getPath(), FileHandler.FileLocation.LOCAL, true );
        } catch (IOException e) {
            // TODO error here means that destination deosnt exist
            e.printStackTrace();
        }
    }

    /**
     *  Generates a class containing translation identifiers using a translation configuration
     * @param configuration
     * @param className the name of the class to be generated
     * @param packageName in shape of <pre>com.test.app</pre>. The package where the class should be located. This will be used as file path too.
     * @param destPath the destination path, the bit that comes before the package name (e.g. src/main/java/)
     */
    public void generateAndSaveCode(TranslationConfiguration configuration, String className, String packageName, String destPath) {
        String generatedCode = this.generateCode(packageName, className, configuration);

        JavaFileHandler javaFileHandler = new JavaFileHandler();
        String destination = destPath + "/"+ packageName.replace('.', '/') + "/" + className + ".java";
        try {
            File file = new File(destination);
            if(!file.exists()) file.createNewFile();
            javaFileHandler.saveFileContents(generatedCode, file.getPath(), null, true );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
