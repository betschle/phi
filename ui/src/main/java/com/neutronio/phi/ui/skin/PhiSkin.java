package com.neutronio.phi.ui.skin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.io.GDXFileHandler;
import com.neutronio.phi.util.ColorUtil;
import com.neutronio.phi.util.metrics.UsageChecker;

import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Map;

/**
 * A skin that allows for color themes, that are injected into the skin file before it is loaded.
 */
public class PhiSkin extends Skin { // AstraXSkin

    private String baseDirectory;
    private FileHandler fileHandler = new GDXFileHandler();
    private SkinConfiguration skinConfiguration;
    private UsageChecker usageChecker = new UsageChecker();
//    private SoundSettings soundSettings; // TODO reactivate

    static private final Class[] defaultTagClasses = {BitmapFont.class, Color.class, TintedDrawable.class, NinePatchDrawable.class,
        SpriteDrawable.class, TextureRegionDrawable.class, TiledDrawable.class, Button.ButtonStyle.class,
        CheckBox.CheckBoxStyle.class, ImageButton.ImageButtonStyle.class, ImageTextButton.ImageTextButtonStyle.class,
        Label.LabelStyle.class, List.ListStyle.class, ProgressBar.ProgressBarStyle.class, ScrollPane.ScrollPaneStyle.class,
        SelectBox.SelectBoxStyle.class, Slider.SliderStyle.class, SplitPane.SplitPaneStyle.class, TextButton.TextButtonStyle.class,
        TextField.TextFieldStyle.class, TextTooltip.TextTooltipStyle.class, Touchpad.TouchpadStyle.class, Tree.TreeStyle.class,
        Window.WindowStyle.class,
//        ButtonSounds.class
    };

    private final ObjectMap<String, Class> jsonClassTags = new ObjectMap(defaultTagClasses.length);
    {
        for (Class c : defaultTagClasses)
            jsonClassTags.put(c.getSimpleName(), c);
    }

    public PhiSkin(String baseDirectory, TextureAtlas atlas) {
        super(atlas);
        this.baseDirectory = baseDirectory;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

//    public void setSoundSettings(SoundSettings soundSettings) {
//        this.soundSettings = soundSettings;
//    }
//
//    public SoundSettings getSoundSettings() {
//        return soundSettings;
//    }

    /** Adds all resources in the specified skin JSON file. */
    public void load (String path, FileHandler.FileLocation fileLocation, SkinConfiguration skinSettings) {
        this.skinConfiguration = skinSettings;
        FileHandle fileHandle = GDXFileHandler.toGdxFileHandle(path, fileLocation);
        try {
            // inject GUI colors from skinSettings
            if( skinSettings.uiColors.isEmpty() ) {
                // use default UI colors if colors are not defined
                skinSettings.uiColors = SkinConfiguration.getDefaultColors();
            }
            StringBuilder builder = this.fileHandler.readFileContents(path, fileLocation);

            for( Map.Entry<String, Color> colorEntry : skinSettings.uiColors.entrySet()) {
                int startIndex = builder.indexOf("$" + colorEntry.getKey());
                if( startIndex < 0) continue;
                String hex = ColorUtil.colorToHexRGBAString(colorEntry.getValue().cpy().mul(1.2f));
                builder.replace(startIndex, startIndex + hex.length(), hex);
            }

            Json jsonLoader = getJsonLoader(fileHandle);
            jsonLoader.readValue(Skin.class, null, new JsonReader().parse(builder.toString()));
        } catch (SerializationException ex) {
            throw new SerializationException("Error reading file: " + fileHandle, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /** Adds all resources in the specified skin JSON file. */
    public void load (FileHandle skinFile) {
        throw new UnsupportedOperationException("Do not use this method");
    }


    @Override
    public <T> T get(String name, Class<T> type) {
        this.usageChecker.record(type.getSimpleName()+":"+name);
        return super.get(name, type);
    }

    @Override
    protected Json getJsonLoader (final FileHandle skinFile) {
        final Skin skin = this;

        final Json json = new Json() {
            static private final String parentFieldName = "parent";

            public <T> T readValue (Class<T> type, Class elementType, JsonValue jsonData) {
                // If the JSON is a string but the type is not, look up the actual value by name.
                if (jsonData != null && jsonData.isString() && !ClassReflection.isAssignableFrom(CharSequence.class, type))
                    return get(jsonData.asString(), type);
                return super.readValue(type, elementType, jsonData);
            }

            protected boolean ignoreUnknownField (Class type, String fieldName) {
                return fieldName.equals(parentFieldName);
            }

            public void readFields (Object object, JsonValue jsonMap) {
                if (jsonMap.has(parentFieldName)) {
                    String parentName = readValue(parentFieldName, String.class, jsonMap);
                    Class parentType = object.getClass();
                    while (true) {
                        try {
                            copyFields(get(parentName, parentType), object);
                            break;
                        } catch (GdxRuntimeException ex) { // Parent resource doesn't exist.
                            parentType = parentType.getSuperclass(); // Try resource for super class.
                            if (parentType == Object.class) {
                                SerializationException se = new SerializationException(
                                    "Unable to find parent resource with name: " + parentName);
                                se.addTrace(jsonMap.child.trace());
                                throw se;
                            }
                        }
                    }
                }
                super.readFields(object, jsonMap);
            }
        };
        json.setTypeName(null);
        json.setUsePrototypes(false);

        json.setSerializer(Skin.class, new Json.ReadOnlySerializer<Skin>() {
            public Skin read (Json json, JsonValue typeToValueMap, Class ignored) {
                for (JsonValue valueMap = typeToValueMap.child; valueMap != null; valueMap = valueMap.next) {
                    try {
                        Class type = json.getClass(valueMap.name());
                        if (type == null) type = ClassReflection.forName(valueMap.name());
                        readNamedObjects(json, type, valueMap);
                    } catch (ReflectionException ex) {
                        throw new SerializationException(ex);
                    }
                }
                return skin;
            }

            private void readNamedObjects (Json json, Class type, JsonValue valueMap) {
                Class addType = type == TintedDrawable.class ? Drawable.class : type;
                for (JsonValue valueEntry = valueMap.child; valueEntry != null; valueEntry = valueEntry.next) {
                    Object object = json.readValue(type, valueEntry);
                    if (object == null) continue;
                    usageChecker.addId(type.getSimpleName() + ":"+valueEntry.name);
                    try {
                        add(valueEntry.name, object, addType);
                        if (addType != Drawable.class && ClassReflection.isAssignableFrom(Drawable.class, addType))
                            add(valueEntry.name, object, Drawable.class);
                    } catch (Exception ex) {
                        throw new SerializationException(
                            "Error reading " + ClassReflection.getSimpleName(type) + ": " + valueEntry.name, ex);
                    }
                }
            }
        });

        json.setSerializer(BitmapFont.class, new Json.ReadOnlySerializer<BitmapFont>() {
            public BitmapFont read (Json json, JsonValue jsonData, Class type) {
                String path = json.readValue("file", String.class, jsonData);
                int scaledSize = json.readValue("scaledSize", int.class, -1, jsonData);
                Boolean flip = json.readValue("flip", Boolean.class, false, jsonData);
                Boolean markupEnabled = json.readValue("markupEnabled", Boolean.class, false, jsonData);

                FileHandle fontFile = skinFile.parent().child(path);
                if (!fontFile.exists()) fontFile = Gdx.files.internal(path);
                if (!fontFile.exists()) throw new SerializationException("Font file not found: " + fontFile);

                // Use a region with the same name as the font, else use a PNG file in the same directory as the FNT file.
                String regionName = fontFile.nameWithoutExtension();
                try {
                    BitmapFont font;
                    Array<TextureRegion> regions = skin.getRegions(regionName);
                    if (regions != null)
                        font = new BitmapFont(new BitmapFont.BitmapFontData(fontFile, flip), regions, true);
                    else {
                        TextureRegion region = skin.optional(regionName, TextureRegion.class);
                        if (region != null)
                            font = new BitmapFont(fontFile, region, flip);
                        else {
                            FileHandle imageFile = fontFile.parent().child(regionName + ".png");
                            if (imageFile.exists())
                                font = new BitmapFont(fontFile, imageFile, flip);
                            else
                                font = new BitmapFont(fontFile, flip);
                        }
                    }
                    font.getData().markupEnabled = markupEnabled;
                    // Scaled size is the desired cap height to scale the font to.
                    if (scaledSize != -1) font.getData().setScale(scaledSize / font.getCapHeight());
                    return font;
                } catch (RuntimeException ex) {
                    throw new SerializationException("Error loading bitmap font: " + fontFile, ex);
                }
            }
        });

        json.setSerializer(Color.class, new Json.ReadOnlySerializer<Color>() {
            public Color read (Json json, JsonValue jsonData, Class type) {
                if (jsonData.isString()) return get(jsonData.asString(), Color.class);
                String hex = json.readValue("hex", String.class, (String)null, jsonData);
                if (hex != null) return Color.valueOf(hex);
                float r = json.readValue("r", float.class, 0f, jsonData);
                float g = json.readValue("g", float.class, 0f, jsonData);
                float b = json.readValue("b", float.class, 0f, jsonData);
                float a = json.readValue("a", float.class, 1f, jsonData);
                return new Color(r, g, b, a);
            }
        });

        json.setSerializer(TintedDrawable.class, new Json.ReadOnlySerializer() {
            public Object read (Json json, JsonValue jsonData, Class type) {
                String name = json.readValue("name", String.class, jsonData);
                Color color = json.readValue("color", Color.class, jsonData);
                if (color == null) throw new SerializationException("TintedDrawable missing color: " + jsonData);
                Drawable drawable = newDrawable(name, color);
                if (drawable instanceof BaseDrawable) {
                    BaseDrawable named = (BaseDrawable)drawable;
                    named.setName(jsonData.name + " (" + name + ", " + color + ")");
                }
                return drawable;
            }
        });

        json.setSerializer(ReactiveDrawable.class, new Json.ReadOnlySerializer() {
            public Object read (Json json, JsonValue jsonData, Class type) {
                ReactiveDrawable reactiveDrawable = new ReactiveDrawable();
                reactiveDrawable.upRegion = createDrawable(json, jsonData, "upRegion");
                reactiveDrawable.downRegion = createDrawable(json, jsonData, "downRegion");
                reactiveDrawable.overRegion = createDrawable(json, jsonData, "overRegion");
                reactiveDrawable.checkedRegion = createDrawable(json, jsonData, "checkedRegion");
                reactiveDrawable.disabledRegion = createDrawable(json, jsonData, "disabledRegion");
                reactiveDrawable.focusedRegion = createDrawable(json, jsonData, "focusedRegion");
                return reactiveDrawable;
            }

            public Drawable createDrawable(Json json, JsonValue jsonData, String fieldName) {
                String regionName = json.readValue(fieldName, String.class, jsonData);
                return regionName != null ? newDrawable(regionName) : null;
            }
        });

        json.setSerializer(ReactiveColor.class, new Json.ReadOnlySerializer() {
            public Object read (Json json, JsonValue jsonData, Class type) {
                ReactiveColor reactiveColor = new ReactiveColor();
                reactiveColor.upColor = json.readValue("upColor", Color.class, jsonData);
                reactiveColor.downColor = json.readValue("downColor", Color.class, jsonData);
                reactiveColor.overColor = json.readValue("overColor", Color.class, jsonData);
                reactiveColor.checkedColor = json.readValue("checkedColor", Color.class, jsonData);
                reactiveColor.checkedOverColor = json.readValue("checkedOverColor", Color.class, jsonData);
                reactiveColor.checkedDownColor = json.readValue("checkedDownColor", Color.class, jsonData);
                reactiveColor.disabledColor = json.readValue("disabledColor", Color.class, jsonData);
                reactiveColor.focusedColor = json.readValue("focusedColor", Color.class, jsonData);
                reactiveColor.focusedCheckedColor = json.readValue("focusedCheckedColor", Color.class, jsonData);
                return reactiveColor;
            }
        });

//        json.setSerializer(ButtonSounds.class, new Json.ReadOnlySerializer() {
//            public Object read (Json json, JsonValue jsonData, Class type) {
//                ButtonSounds buttonSounds = new ButtonSounds();
//                buttonSounds.soundPressed = json.readValue("soundPressed", String.class, (String) null, jsonData);
//                buttonSounds.soundRelease = json.readValue("soundRelease", String.class, (String) null, jsonData);
//                buttonSounds.soundOver = json.readValue("soundOver", String.class, (String) null, jsonData);
//                return buttonSounds;
//            }
//        });

        for (ObjectMap.Entry<String, Class> entry : jsonClassTags)
            json.addClassTag(entry.key, entry.value);

        return json;
    }

    public UsageChecker getUsageChecker() {
        return usageChecker;
    }
}

