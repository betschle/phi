package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.CustomAction;
import com.neutronio.phi.ui.commons.Background;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.tooltips.HelpTooltip;
import com.neutronio.phi.ui.tooltips.ToolTipManager;

/**
 * A console UI component
 */
public class ConsoleComponent extends SimplePanel {

    private ConsoleTextArea textArea;
    private TextField commandInput;
    private AstraXButton clearButton;
    private AstraXButton copyButton;

    private ConsoleListener consoleListener;

    public interface ConsoleListener {
        void onCommandExecute(String input);
        void onOutputCleared();
        void onOutputCopied();
    }

    public ConsoleComponent(final ComponentFactory componentFactory, String style, String backgroundStyle) {
        super(componentFactory, style, backgroundStyle);
        this.textArea = new ConsoleTextArea(componentFactory, 12);
        this.commandInput = new TextField("", componentFactory.getSkin());
        this.commandInput.addListener( new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(Input.Keys.ENTER == keycode) {
                    System.out.println("Enter: Executing command...");
                    if(getConsoleListener() != null) getConsoleListener().onCommandExecute(commandInput.getText());
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });
        this.clearButton = new AstraXButton(componentFactory, "icon_inline_trash", "secondary");
        this.clearButton.setCanCheck(false);
        this.clearButton.toolTipInfo = new ToolTipManager.ToolTipInfo(HelpTooltip.class);
        this.clearButton.toolTipInfo.toolTipData = "Clear Console";
        this.clearButton.toolTipListener = componentFactory.getToolTipManager();
        this.clearButton.setButtonAction(CustomAction.get(new Runnable() {
            @Override
            public void run() {
                textArea.clearConsole();
                if(getConsoleListener() != null) getConsoleListener().onOutputCleared();
            }
        }));
        this.copyButton = new AstraXButton(componentFactory, "icon_inline_new", "secondary");
        this.copyButton.setCanCheck(false);
        this.copyButton.toolTipInfo = new ToolTipManager.ToolTipInfo(HelpTooltip.class);
        this.copyButton.toolTipInfo.toolTipData = "Copy Console Output";
        this.clearButton.toolTipListener = componentFactory.getToolTipManager();
        this.copyButton.setButtonAction(CustomAction.get(new Runnable() {
            @Override
            public void run() {
                textArea.copyToClipboard();
                if(getConsoleListener() != null) getConsoleListener().onOutputCopied();
            }
        }));
        this.addLabel("Console", "default", (Background.BackgroundStyle) null);
        this.row();
        this.addValue(this.textArea, 3);
        this.row();
        this.addValue(this.commandInput).minWidth(300);
        this.addValueCentered(this.copyButton);
        this.addValueCentered(this.clearButton);
        this.pack();
    }

    private ConsoleListener getConsoleListener() {
        return consoleListener;
    }

    public void addLine(String line) {
        this.textArea.addLine(line);
    }

    public void addLines(String lines) {
        this.textArea.addLines(lines);
    }

    public void setConsoleListener(ConsoleListener consoleListener) {
        this.consoleListener = consoleListener;
    }
}
