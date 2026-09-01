package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.neutronio.phi.ui.commons.DefaultPaginator;
import com.neutronio.phi.ui.commons.widgets.PaginatorComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A pagination controller to split up large amounts of text
 * into multiple pages.
 */
public class TextPaginator {

    private int charactersPerPage = 300;
    private int charactersPerLine = 35;
    private List<String> pages = new ArrayList<>();
    private AstraXLabel label;
    private DefaultPaginator defaultPaginator = new DefaultPaginator();
    private PaginatorComponent paginatorComponent;

    public TextPaginator(AstraXLabel label, PaginatorComponent paginatorComponent) {
        this.label = label;
        this.paginatorComponent = paginatorComponent;
        this.paginatorComponent.setPaginator(defaultPaginator);
        this.paginatorComponent.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                update();
            }
        });
    }

    public void setCharactersPerPage(int charactersPerPage) {
        this.charactersPerPage = charactersPerPage;
    }

    public void setCharactersPerLine(int charactersPerLine) {
        this.charactersPerLine = charactersPerLine;
    }

    public void setText(String text) {
        this.pages.clear();
        if( text == null || text.isEmpty()) {
            this.defaultPaginator.configure(0, 0);
            return;
        }
        String[] words = text.split(Pattern.quote(" "));
        int charCount = 0;
        StringBuilder strBuffer = new StringBuilder();
        for( String word : words) {
            strBuffer.append(word).append(" ");
            if( charCount > this.charactersPerPage) {
                this.pages.add(strBuffer.toString());
                strBuffer.delete(0, strBuffer.length());
                charCount = 0;
            }
            if( word.equals("\n")) {
                // add character penalty for new lines. This is just a rough estimate
                charCount += this.charactersPerLine;
            } else {
                charCount += word.length() + 1; // +1 for whitespace
            }
        }
        if( charCount > 0) { // add remaining characters to last page
            this.pages.add(strBuffer.toString());
        }
        this.defaultPaginator.configure(this.pages.size(), 1);
        this.defaultPaginator.setCurrentPage(1);
    }

    public void update() {
        if( this.pages.isEmpty() ) {
            this.label.setText("");
            this.paginatorComponent.update();
        } else {
            this.label.setText(this.pages.get(defaultPaginator.getCurrentPage() - 1));
            this.paginatorComponent.update();
        }
    }
}
