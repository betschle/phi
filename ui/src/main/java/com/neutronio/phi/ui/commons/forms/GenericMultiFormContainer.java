package com.neutronio.phi.ui.commons.forms;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.Button;
import com.neutronio.phi.ui.commons.tabs.FlexTab;

import java.util.List;

/**
 * A FormContainer for multiple Forms that are being controlled by the
 * same form button group. Use the static method to generically create one.
 * Add a FormListener to the FormController to Listen to button events.
 */
public class GenericMultiFormContainer extends SimplePanel {

    public MultiFormController multiFormController;
    public FlexTab tabController;

    public GenericMultiFormContainer(ComponentFactory componentFactory, String background) {
        super(componentFactory, "default", background);
        this.multiFormController = new MultiFormController(componentFactory);
        this.tabController = new FlexTab(componentFactory, "default");
    }

    public GenericMultiFormContainer(ComponentFactory componentFactory) {
        this(componentFactory, "panel-tech-deco");
    }

    /**
     *
     * @param componentFactory
     * @param forms the name of the actor/form is expected to be a translation modifier and is used as tab text.
     * @param cancelTranslationID a translation ID that is translated when creating the cancel button. Null for no button
     * @param resetTranslationID a translation ID that is translated when creating the reset button. Null for no button
     * @param submitTranslationID a translation ID that is translated when creating the submit button. Null for no button
     * @return
     */
    public static GenericMultiFormContainer createFormContainer (
            ComponentFactory componentFactory, List<AbstractForm> forms, boolean vertical,
            String cancelTranslationID, String resetTranslationID, String submitTranslationID
    ) {
        GenericMultiFormContainer formContainer = new GenericMultiFormContainer(componentFactory);
        int colspan = 0;
        if(cancelTranslationID != null) colspan++;
        if(resetTranslationID != null) colspan++;
        if(submitTranslationID != null) colspan++;

        // create tab buttons vertically or horizontally
        if( vertical ) {
            VerticalGroup tabButtons = new VerticalGroup();
            tabButtons.space(10);
            tabButtons.top();
            for (AbstractForm form : forms) {
                formContainer.getFormController().putForm(form);
                AstraXTextButton tabButton = formContainer.tabController.addTabWithTextButton(form.getName(), "secondary-tab", form);
                tabButtons.addActor(tabButton);
            }
            formContainer.add(tabButtons).padRight(10).top();
        } else {
            HorizontalGroup tabButtons = new HorizontalGroup();
            tabButtons.space(10);
            tabButtons.top();
            for (AbstractForm form : forms) {
                formContainer.getFormController().putForm(form);
                AstraXTextButton tabButton = formContainer.tabController.addTabWithTextButton(form.getName(), "secondary-tab", form);
                tabButtons.addActor(tabButton);
            }
            formContainer.add(tabButtons).padBottom(10).colspan(colspan).top();
            formContainer.row();
        }

        formContainer.addValue(formContainer.tabController.getContentStack()).colspan(colspan).top();
        formContainer.row();

        // prepare form buttons
        SimplePanel formButtons = new SimplePanel(componentFactory);
        formButtons.align(Align.center);
        if( cancelTranslationID != null) {
            AstraXTextButton cancelButton = formContainer.multiFormController
                    .addBackButton(componentFactory.translate(cancelTranslationID));
            cancelButton.setSquishable(false);
            formButtons.addValue(cancelButton);
        }
        if( resetTranslationID != null) {
            AstraXTextButton resetButton = formContainer.multiFormController
                    .addResetButton(componentFactory.translate(resetTranslationID));
            resetButton.setSquishable(false);
            formButtons.addValue(resetButton);
        }
        if( submitTranslationID != null) {
            AstraXTextButton submitButton = formContainer.multiFormController
                    .addSubmitButton(componentFactory.translate(submitTranslationID));
            submitButton.setSquishable(false);
            formButtons.addValue(submitButton);
        }
        // TODO why the eff is this not centered omg
        formButtons.pack();
        formContainer.add(formButtons).colspan(colspan).padTop(10).center().maxWidth(ComponentFactory.GAME_MENU_WIDTH);
        formContainer.pack();
        return formContainer;
    }

    public List<Button> getTabButtons() {
        return this.tabController.getTabButtons();
    }

    public MultiFormController getFormController() {
        return this.multiFormController;
    }
}
