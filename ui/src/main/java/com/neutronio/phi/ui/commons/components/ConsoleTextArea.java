package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Clipboard;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Intended to display large amount of text with
 * growing String content.
 */
public class ConsoleTextArea extends AstraXComponent {
    // TODO output exceeds width still

    private BitmapFont font;
    /** The content text rows, contentText.len > displayText.len */
    private List<String> contentText = new ArrayList<>();
    /** The display text rows */
    private String[] displayText;

    /** The row index at which the scroller starts rendering text. */
    private int displayStart = 0;
    /** Max lines that can be added to the content of this console.
     * The oldest output will be truncated going beyond this limit. */
    private int lineLimit = 300;

    /** Clipboard for copy functionality */
    private Clipboard clipboard;
    /** Intended for the clipboard */
    private StringBuilder content;

    public ConsoleTextArea(ComponentFactory componentFactory, int rows) {
        super(componentFactory, "transparent");
        this.displayText = new String[rows];
        this.font = componentFactory.getSkin().getFont("default"); //tcf_14px
        this.background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Clicked");
                getStage().setScrollFocus(background);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                scroll(-(int)amountY);
                System.out.println("Scrolled");
                return true;
            }
        });
        this.clipboard = Gdx.app.getClipboard();
        this.construct();
    }

    public void setLineLimit(int lineLimit) {
        this.lineLimit = lineLimit;
    }

    /**
     * Splits up the text into multiple lines (by existing line breaks) and adds them
     * to the console. Replaces tab spaces (\t) with 4 white spaces.
     * @param text
     */
    public void addLines(String text) {
        text = text.replaceAll("\t", "    ");
        String[] split = text.split(Pattern.quote("\n"));
        for(String line : split) {
            this.addLine(line);
        }
        updateText();
    }

    /**
     * Adds a line to the console, adds a line break when necessary
     * @param line
     */
    public void addLine(String line) {
//        int lineWidth = this.calculateWidth(line);
        // todo if line width > max width then add x lines to contentText
        if(this.contentText.size() < this.lineLimit) {
            this.contentText.add(0, line);
        } else {
            this.contentText.remove(this.contentText.size()-1);
            this.contentText.add(0, line);
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
     * Scrolls the console by amount of rows
     * @param byRows
     */
    public void scroll(int byRows) {
        this.displayStart = MathUtils.clamp(this.displayStart + byRows, 0, this.contentText.size()-1);
        this.updateText();
    }

    /**
     * Clears the console from all text
     */
    public void clearConsole() {
        this.contentText.clear();
        this.updateText();
    }

    /**
     * Copies content of the console to the system clipboard
     */
    public void copyToClipboard() {
        if(this.content == null) this.content = new StringBuilder();
        else this.content.delete(0, this.content.length());
        for(String line : this.contentText) {
            this.content.append(line).append("\n");
        }
        this.clipboard.setContents(this.content.toString());
    }

    @Override
    public float getPrefHeight() {
        return font.getLineHeight() * this.displayText.length + this.background.getStyle().padding * 2;
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
        int r = 1;
        for(String text : this.displayText) {
            if(text != null) {
                font.draw(batch, text,
                        getX() + this.background.getStyle().padding,
                        getY() + font.getLineHeight() * r + this.background.getStyle().padding);
            }
            r++;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.renderText(batch);
    }
}
