package com.neutronio.phi.util.tools;

import com.neutronio.phi.io.JavaFileHandler;
import com.neutronio.phi.util.format.DateFormats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * A tool that generates a class based on a properties file used for localization.
 * The class will have a field for each property, e.g. for property "button_text=..."
 * there will be a public static String field called "BUTTON_TEXT" with an assigned value of "button_text" as
 * translation identifier.
 */
public class TranslationGenerator {

    // TODO this does not use the FileHandler. Mostly cause of property file loading
    // TODO csv import/export would be nice
    /**
     * Turns a properties object into a list of property keys.
     * @param properties
     */
    public List<String> convert(Properties properties) {
        List<String> propertyList = new ArrayList<>();
        Enumeration<Object> keys = properties.keys();
        while( keys.hasMoreElements() ) {
            propertyList.add((String) keys.nextElement());
        }
        return propertyList;
    }

    /**
     * Creates a list of translation identifiers for an enum,
     * for an enum variable called FieldType.METHOD the output identifier is "FieldType_method"
     * @param values
     * @return
     */
    public static List<String> generatePropertiesFromEnum(Enum[] values) {
        List<String> propertyList = new ArrayList<>();
        for(Enum element : values) {
            propertyList.add(element.getClass().getSimpleName() + "_" + element.name().toLowerCase(Locale.ROOT));
        }
        return propertyList;
    }

    /**
     * Generates code for a class containing the specified property keys.
     * For a property "button_text=..." there will be a public static String field called "BUTTON_TEXT"
     * with an assigned value of "button_text" as translation identifier.
     * @param className
     * @param propertyKeys
     * @return
     */
    public String generate(String packageName, String className, List<String> propertyKeys) {
        String packageCode = String.format( "package %s;\n\n", packageName) ;
        String comment = "/**\n" +
                " * A generated class providing access to translation identifiers.\n"+
                " * Do not manually edit this! Instead, add translation strings to the \n" +
                " * translation properties file and run the generator again. \n\n"+
                " * Generated: " + DateFormats.FULL.format(new Date()) + "\n\n" +
                " * @see \n" + this.getClass().getCanonicalName() +
                " */\n" ;
        String classCode = "public class %s {\n%s}";
        StringBuilder fields = new StringBuilder();
        for(String key : propertyKeys) {
            // TODO add content of keys as explanation?
            fields.append("   ").append("public static final String ").append(key.toUpperCase()).append(" = ").append("\"").append(key).append("\";\n");
        }
        return packageCode + comment + String.format(classCode, className, fields);
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

        TranslationGenerator translationGenerator = new TranslationGenerator();
        List<String> convert = translationGenerator.convert(properties);
        Collections.sort(convert);

        String generatedCode = translationGenerator.generate(packageName, className, convert);

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

    public static void main(String[] args) {
        TranslationGenerator translationGenerator = new TranslationGenerator();
        translationGenerator.generateAndSaveCode(
                "core/assets/lang/phi.properties",
                "PhiTranslations",
                "com.neutronio.phi.lang",
                "core/src/main/java/");
    }
}
