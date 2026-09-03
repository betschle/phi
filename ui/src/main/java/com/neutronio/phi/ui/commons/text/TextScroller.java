package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.AbstractPanel;
import com.neutronio.phi.ui.skin.ReactiveColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A read-only text field component that allows per-line text scrolling.
 * Intended to display non-formatted long form text. Not suited for
 * displaying large amount of text that grows over time.
 * @see ConsoleTextArea
 */
public class TextScroller extends AbstractPanel {
    // TODO mark the scroller scrollable with a tiny arrow in the corner
    // TODO use font cache like in gdx label?
    // TODO styling

    public static class TextScrollerStyle {
        public BitmapFont font;
        public ReactiveColor fontColor;
    }

    private BitmapFont font;
    /** The content text rows, contentText.len > displayText.len */
    private List<String> contentText = new ArrayList<>();
    /** The display text rows */
    private String[] displayText;

    /** The original text added to this scroller */
    private String sourceText;
    /** Contains source text with inserted paragraphs */
    private StringBuilder builder = new StringBuilder();

    /** The row index at which the scroller starts rendering text. */
    private int displayStart = 0;
    /** The line width */
    private int lineWidth = 0;

    public TextScroller(ComponentFactory componentFactory, int rows) {
        super(componentFactory, "default", "transparent");
        this.displayText = new String[rows];
        this.font = componentFactory.getSkin().getFont("default"); //tcf_14px
        this.background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getStage().setScrollFocus(background);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                scroll((int)amountY);
                return true;
            }
        });
        this.setSize(300, rows * font.getData().lineHeight);
        this.construct();
    }

    /**
     * Display text is split into rows with this call. Does not alter text.
     * This algo is expensive and should be used with care
     * @param text
     */
    public void setText(String text) {
        this.sourceText = text;
        this.contentText.clear();
        this.splitIntoRows(text, 300);
        String[] split = this.builder.toString().split(Pattern.quote("\n"));
        for( String row : split) {
            this.contentText.add(row);
        }
        this.updateText();
    }

    /**
     *
     * @return the original, unaltered text
     */
    public String getText() {
        return this.builder.toString();
    }

    // Method should also used by ConsoleTextArea
    private void splitIntoRows(String str, int maxWidth) {
        this.builder.delete(0, builder.length());
        this.lineWidth = maxWidth;
        int padding = this.background.getStyle().padding * 2;
        int width = padding;
        int wordWidth = 0;
        // TODO need to split twice, first for \n and then for space
        float spaceWidth = font.getData().spaceXadvance; // font.getData().getGlyph(' ').width;
        String[] words = str.split(Pattern.quote(" "));
        for( String word : words) {
            wordWidth = (int) (calculateWidth(word) + spaceWidth);
            // TODO if word contains \n => append it as is
            if(width + wordWidth > maxWidth) {
                System.out.println("\nline width: " + width);
                System.out.println("word: " + word);
                System.out.print("> ");
                width = padding + wordWidth;
                this.builder.append("\n").append(word).append(" ");
            } else {
                width += wordWidth;
                System.out.print(word + " ");
                this.builder.append(word).append(" ");
            }
        }
    }

    private int calculateWidth(String str) {
        int width = 0;
        for(char c : str.toCharArray()) {
            BitmapFont.Glyph glyph = font.getData().getGlyph(c);
            width += glyph.width + glyph.getKerning(c);
        }
        return width;
    }

    /**
     * Invokes changes after updating either text or scroll amount
     */
    public void updateText() {
        // clear display text first
        for( int d = 0; d < this.displayText.length; d++) {
            this.displayText[d] = "";
        }

        int rowIndex = 0;
        for( int i = this.displayStart;
                i < this.displayStart + this.contentText.size() &&
                i < this.contentText.size() &&
                rowIndex < this.displayText.length;
             i++) {
            this.displayText[rowIndex] = this.contentText.get(i);
            rowIndex++;
        }
    }

    public void renderText(Batch batch) {
        int r = 0;
        for(String text : this.displayText) {
            if(text != null) {
                font.draw(batch, text,
                        getX() + this.background.getStyle().padding,
                        getY() + getHeight() - font.getLineHeight() * r - this.background.getStyle().padding * 2 );
            }
            r++;
        }
    }

    @Override
    public float getPrefWidth() {
        return this.lineWidth;
    }

    @Override
    public float getPrefHeight() {
        return font.getLineHeight() * this.displayText.length + this.background.getStyle().padding * 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.renderText(batch);
    }

    public void scroll(int byRows) {
        this.displayStart = MathUtils.clamp(this.displayStart + byRows, 0, this.contentText.size()-1);
        this.updateText();
    }

    @Override
    public void resetValues() {

    }
}
