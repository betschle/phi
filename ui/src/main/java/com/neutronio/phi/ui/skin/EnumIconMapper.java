package com.neutronio.phi.ui.skin;

import com.neutronio.phi.PhiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps icons to enums
 */
public class EnumIconMapper {
    // isnt this technically a properties file?
    protected Map<Enum, String> mappings = new HashMap<>();

    /**
     * Maps a single enum to an icon. Overwrites existing mapping
     * @param enumConstant the enum
     * @param icon the icon to map the enum to skin drawables
     */
    public void mapEnumToIcon(Enum enumConstant, String icon) {
        this.mappings.put(enumConstant, icon);
    }

    /**
     * Maps each enum element in the array to a string icon in the array at the same index
     * @param enums an array of enums, obtained via enum.values()
     * @param icons an array of string references to skin drawables
     */
    public void mapEnumsToIcons(Enum[] enums, String[] icons) {
        if(enums.length != icons.length) throw new PhiException(PhiException.ErrorCode.E0004, "array lengths not equal");
        for(int i =0; i < enums.length; i++) {
            this.mappings.put(enums[i], icons[i]);
        }
    }

    /**
     * Gets an icon for an enum
     * @param forEnumConstant
     * @return
     */
    public String getIcon(Enum forEnumConstant) {
        return this.mappings.get(forEnumConstant);
    }

    /**
     * Removes a mapping for an enum
     * @param forEnumConstant
     */
    public void removeMapping(Enum forEnumConstant) {
        this.mappings.remove(forEnumConstant);
    }

    /**
     * Clears all mappings
     */
    public void clearMappings() {
        this.mappings.clear();
    }

    public Set<Enum> getKeys() {
        return this.mappings.keySet();
    }
}
