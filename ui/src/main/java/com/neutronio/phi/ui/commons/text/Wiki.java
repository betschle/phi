package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;

/**
 * A component that lists multiple entries where one entry can reveal more details.
 * Goes with an optional image
 */
public class Wiki
        extends
            AstraXComponent {

    private ScrollPane entryScrollPane;
    private Table outerEntryTable;

    private ScrollPane textScrollPane;
    private Table outerTextTable;

    private List<WikiEntry> entries;

    private TextArea textArea;

    public static class WikiEntry {
        public String entryText;
        public String entryDescription;

        public WikiEntry(String entryText, String entryDescription) {
            this.entryText = entryText;
            this.entryDescription = entryDescription;
        }

        @Override
        public String toString() {
            return entryText;
        }
    }

    public Wiki(ComponentFactory componentFactory) {
        super(componentFactory, "border");
        this.textArea = new TextArea("", componentFactory.getSkin());
        this.textArea.setDisabled(true);
        this.textArea.setPrefRows(10f);

        this.entries = new List<WikiEntry>( componentFactory.getSkin() );
        this.entries.setItems(
                new WikiEntry("Entry 1", "Some Entry text."),
                new WikiEntry("Entry 2", "Yadda"),
                new WikiEntry("Entry 3", "Lorem ipsum blablabla")
        );

        this.entries.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSelectedChanged();
            }
        });
//        this.entries.addListener( componentFactory.getButtonSoundListener("checkBox"));
        this.entryScrollPane = new ScrollPane(this.entries, getSkin(), "default-vertical");
        this.entryScrollPane.setFadeScrollBars(false);
        this.entryScrollPane.pack();

        this.outerEntryTable = new Table();
        this.outerEntryTable.add(entryScrollPane).height(300).top().left(); // height = display height
        this.outerEntryTable.pack();

        this.textScrollPane = new ScrollPane(this.textArea, getSkin(), "default-vertical");
        this.textScrollPane.setFadeScrollBars(false);
        this.textScrollPane.pack();

        this.outerTextTable = new Table();
        this.outerTextTable.add(this.textScrollPane).height(300).width(200).top(); // height = display height
        this.outerTextTable.pack();
    }

    /**
     * Defines scrolling behavior. If content exceeds the here specified heights
     * scrolling will be activated.
     */
    public void setDisplayHeight( int entryHeight, int entryWidth, int textAreaHeight, int textAreaWidth) {
        this.outerEntryTable.getCell( this.entryScrollPane ).size(entryWidth, entryHeight);
        this.outerEntryTable.pack();
        this.outerTextTable.getCell( this.textScrollPane ).size(textAreaWidth, textAreaHeight);
        this.outerTextTable.pack();
        this.pack();
    }

    /**
     * Defines how many rows the text area should display
     * @param rows
     */
    public void setPreferredRows( int rows) {
        this.textArea.setPrefRows(rows);
        this.pack();
    }

    public void setEntries( WikiEntry[] entries ) {
        this.entries.setItems(entries);
    }

    private void onSelectedChanged() {
        if( this.entries.getSelected() != null) {
            this.textArea.setText(entries.getSelected().entryDescription);
        } else {
            this.textArea.setText("");
        }
    }

    @Override
    public void construct() {
        super.construct();
        this.add( this.outerEntryTable ).padRight(10).top();
        this.add( this.outerTextTable  ).top().fill();
        this.pack();
    }
}
