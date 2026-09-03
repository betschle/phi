package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.app.Message;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.pagination.DefaultPaginator;
import com.neutronio.phi.util.format.DateFormats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Displays a simple list of {@link Message}s. Font color changes depending on
 * Message Type. Suitable for large amount of messages. Hook up the {@link Paginator} to
 * a {@link PaginatorComponent} to use pagination functionality.
 */
public class MessageLog extends SimplePanel {

    // TODO needs a style
    /** Shows timestamps on the log. This should be set once during initialization, before adding messages, and not be changed
     * afterwards. */
    private boolean showTimestamps = false;
    /** The amount of entries per page. */
    private int entriesPerPage = 5;
    /** Component factory. */
    private ComponentFactory componentFactory;
    /** For sorting messages according to the message type. */
    private Message.MessageComparator messageComparator = new Message.MessageComparator();
    /** A list of messages. */
    private List<Message> messages = new ArrayList<>();
    /** Labels used to display messages. */
    private List<Label> labels = new ArrayList<>();
    /** Labels used to display timestamps. */
    private List<Label> timestamps = new ArrayList<>();
    /** A Paginator loginc controller. */
    private DefaultPaginator paginator;

    public MessageLog (ComponentFactory componentFactory, String backgroundStyle, int entriesPerPage) {
        super(componentFactory, "default", backgroundStyle);
        this.componentFactory = componentFactory;
        this.paginator = new DefaultPaginator();
        this.align(Align.left);
        this.setEntriesPerPage(entriesPerPage);
    }

    public boolean isShowTimestamps() {
        return showTimestamps;
    }

    /**
     * Shows timestamps on the log. This should be set once during initialization,
     * before adding messages, and not be changed afterwards.
     * @param showTimestamps
     */
    public void setShowTimestamps(boolean showTimestamps) {
        this.showTimestamps = showTimestamps;
    }

    public int getMessageCount() {
        return this.messages.size();
    }

    /**
     * THe amount of labels that can be used to display messages.
     * @return
     */
    public int getEntriesPerPage() {
        return this.entriesPerPage;
    }

    public DefaultPaginator getPaginator() {
        return paginator;
    }

    public void setEntriesPerPage(int entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
    }

    public void construct() {
        for(int i = 0; i < entriesPerPage; i++) {
            if( this.showTimestamps ) {
                Label timestamp = new Label("", componentFactory.getSkin(), "default");
                timestamp.setAlignment(Align.left, Align.left);
                this.timestamps.add(timestamp);
                this.add(timestamp).left().fill().padBottom(5).padRight(5);
            }
            Label entry = new Label("", componentFactory.getSkin(), "default");
            entry.setAlignment(Align.left, Align.left);
            this.labels.add(entry);
            this.add(entry).left().fill().padBottom(5);
            this.row();
        }
        this.pack();
    }

    public void clearMessages() {
        this.messages.clear();
        this.paginator.setMaxPages( 1 );
    }

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
        int maxPageCount = this.entriesPerPage > 0 ? (int) Math.ceil( this.messages.size() / (float) this.entriesPerPage) : 1;
        this.paginator.setMaxPages( maxPageCount );
    }

    public void addMessage(Message message) {
        if( message == null) return;
        this.messages.add(message);
        int maxPageCount = this.entriesPerPage > 0 ? (int) Math.ceil( this.messages.size() / (float) this.entriesPerPage) : 1;
        this.paginator.setMaxPages( maxPageCount );
    }

    /**
     * Sorts the messages according to their type. Only works
     * if showTimestamps is deactivated, as this configuration indicates that
     * chronological order is important
     */
    public void sortMessages() {
        if( !this.showTimestamps ) {
            Collections.sort(this.messages, this.messageComparator);
        }
    }

    /**
     * Updates the message log to display currently present Messages.
     */
    public void update() {

        int startIndex = (this.paginator.getCurrentPage()-1) * this.entriesPerPage;
        int labelIndex = 0;

        // clear display
        {
            int i = 0;
            for( Label label : this.labels) {
                label.setText("");
                label.setStyle( componentFactory.getSkin().get("label-transparent", Label.LabelStyle.class) );
                if( this.showTimestamps ) {
                    this.timestamps.get(i).setText("");
                }
                i++;
            }
        }

        for( int k = startIndex;
             k < startIndex + this.messages.size() &&
                     k < this.messages.size()  &&
                     labelIndex < this.labels.size(); k++ ) {

            Message message = this.messages.get(k);
            this.labels.get(labelIndex).setText( message.getMessage());
            this.labels.get(labelIndex).setStyle( componentFactory.getSkin().get(message.getType().toLabelStyle(), Label.LabelStyle.class) );
            if( this.showTimestamps ) {
                this.timestamps.get(k).setText(DateFormats.HOUR_MINUTE.format( message.getTimestamp() ));
            }
            labelIndex++;
        }
        this.pack();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if( this.isVisible() ) {
            this.update();
        }
    }
}
