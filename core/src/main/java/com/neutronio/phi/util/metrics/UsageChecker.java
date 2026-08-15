package com.neutronio.phi.util.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks usage of string based ids to determine
 * unused objects in a repository or other data storage.
 */
public class UsageChecker {
    private List<String> allIDs = new ArrayList<>();
    private List<String> usedIDs = new ArrayList<>();

    public UsageChecker( List<String> keys) {
        this.allIDs.addAll(keys);
    }

    public UsageChecker() {

    }

    /**
     * For modifying the original ID set.
     * Do not use {@link #record(String)} until all available
     * IDs have been added
     * @param id
     */
    public void addId( String id) {
        this.allIDs.add(id);
    }
    /**
     * Records a String and marks it as used
     * @param id must be contained inside allIDs
     */
    public void record( String id) {
        if( this.allIDs.contains(id) )
            this.usedIDs.add(id);
    }

    /**
     * Gets a collection of unused strings
     * @return
     */
    public List<String> getUnusedStrings() {
        List<String> unusedIDs = new ArrayList<>();
        for( String id : this.allIDs) {
            if( !this.usedIDs.contains(id) ) {
                unusedIDs.add(id);
            }
        }
        return unusedIDs;
    }

    public int getTotalStringCount() {
        return this.allIDs.size();
    }
}

