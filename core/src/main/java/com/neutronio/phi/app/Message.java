package com.neutronio.phi.app;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * An in-game Message.
 */
public class Message implements Serializable {

    /** The timestamp of this message */
    private Date timestamp;
    /** The message type*/
    private MessageType type;
    /** The message */
    private String message;
    /** A voice message that belongs to the text message, if available */
    private String voiceMessage;

    public static class MessageComparator implements Comparator<Message> {

        @Override
        public int compare(Message o1, Message o2) {
            return Integer.compare(o1.getType().ordinal(), o2.getType().ordinal()) ;
        }
    }

    public enum MessageType {
        // TODO the sounds here are non-optimal
        /** Used for successful actions etc. */
        SUCCESS("notification_success", "beep1"),
        /** Used as additional info */
        INFO("notification_info", "chime"),
        /** Used if an action could not be performed, but not as severe as an alert or error.
         * This level indicates that functionality may be impaired, but this is not critical. */
        WARNING("notification_warning", "warning"),
        /** An Alert. This level indicates that A) something may require additional user intervention and/or B) some
         * critical functionality may not be available. This level is not as severe as an error. */
        ALERT("notification_critical", "alert"),
        /** For unexpected errors. This level indicates that functionality was forcefully and fatally aborted. */
        EXCEPTION("notification_critical", "error");

        private String style;
        private String sound;

        MessageType( String style, String uisound) {
            this.sound = uisound;
            this.style = style;
        }

        public String getSound() {
            return sound;
        }

        public String getStyle() {
            return style;
        }

        public String toLabelStyle() {
            switch (this) {
                case INFO: return "label-transparent";
                case SUCCESS: return "label-success";
                case WARNING: return "label-warning";
                case ALERT: return "label-error";
                case EXCEPTION: return "label-error";
            }
            return "label-transparent";
        }

        public String toIconLabelStyle() {
            switch(this) {
                case SUCCESS:
                    return "success";
                case INFO:
                    return "info";
                case WARNING:
                    return "warning";
                case ALERT:
                case EXCEPTION:
                    return "error";
                default: return "info";
            }
        }
    }

    public Message() {
        this.timestamp = new Date();
    }

    public Message(Date timestamp, MessageType type, String message) {
        this.timestamp = timestamp;
        this.type = type;
        this.message = message;
    }

    public Message(MessageType type, String message) {
        this.timestamp = new Date();
        this.type = type;
        this.message = message;
    }

    public Message(MessageType type, String message, String voiceMessage) {
        this.timestamp = new Date();
        this.type = type;
        this.message = message;
        this.voiceMessage = voiceMessage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }
}
