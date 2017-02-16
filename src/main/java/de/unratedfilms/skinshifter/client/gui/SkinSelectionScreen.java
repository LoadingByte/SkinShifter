
package de.unratedfilms.skinshifter.client.gui;

import static de.unratedfilms.skinshifter.Consts.MOD_ID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.layouts.AlignLayout;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.Scrollbar;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerScrollableImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelFocusableImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ScrollbarImpl;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.net.NetworkService;
import de.unratedfilms.skinshifter.net.messages.ClearSkinServerMessage;
import de.unratedfilms.skinshifter.net.messages.SetSkinServerMessage;

public class SkinSelectionScreen extends BasicScreen {

    private static final int  MARGIN                                = 5;
    private static final int  PADDING                               = 5;

    private static final int  SCROLLABLE_CONTAINER_WIDGET_V_PADDING = 5; // The vertical padding between to widgets inside the container

    private final List<Skin>  skins;

    private ContainerFlexible mainContainer;

    private Label             titleLabel;
    private ContainerFlexible leftContainer;
    private PlayerDisplay     selectedSkinPlayerDisplay;

    private ButtonLabel       setSkinButton;
    private ButtonLabel       clearSkinButton;
    private ButtonLabel       cancelButton;

    private ContainerFlexible scrollableContainer;
    private Scrollbar         scrollbar;

    public SkinSelectionScreen(GuiScreen parent, Set<Skin> skins) {

        super(parent);

        this.skins = new ArrayList<>(skins);
        Collections.sort(this.skins);
    }

    @Override
    protected void createGui() {

        ContainerFlexible rootContainer = new ContainerClippingImpl();
        setRootWidget(rootContainer);

        mainContainer = new ContainerClippingImpl();
        rootContainer.addWidgets(mainContainer);

        titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".skinSelection.title"));
        leftContainer = new ContainerClippingImpl();
        selectedSkinPlayerDisplay = new PlayerDisplay();
        mainContainer.addWidgets(titleLabel, leftContainer, selectedSkinPlayerDisplay);

        setSkinButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".skinSelection.setSkin"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
            setSkinToSelected();
            close();
        }));
        clearSkinButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".skinSelection.clearSkin"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
            clearSkin();
            close();
        }));
        cancelButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".skinSelection.cancel"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> close()));
        ContainerFlexible buttonContainer = new ContainerClippingImpl(setSkinButton, clearSkinButton, cancelButton);
        leftContainer.addWidgets(buttonContainer);

        scrollbar = new ScrollbarImpl(0);
        scrollableContainer = new ContainerScrollableImpl(scrollbar);
        leftContainer.addWidgets(scrollableContainer);

        for (Skin skin : skins) {
            scrollableContainer.addWidgets(new FocusableSkinLabel(skin));
        }

        // ----- Revalidation -----

        rootContainer
                .appendLayoutManager(c -> {
                    mainContainer.setBounds(MARGIN, MARGIN, rootContainer.getWidth() - 2 * MARGIN, rootContainer.getHeight() - 2 * MARGIN);
                });

        mainContainer
                .appendLayoutManager(c -> {
                    // Title label at the top
                    titleLabel.setPosition( (mainContainer.getWidth() - titleLabel.getWidth()) / 2, PADDING);

                    // Player display at the rightmost position, taking up 1/3 of the main container
                    selectedSkinPlayerDisplay.setSize( (mainContainer.getWidth() - 2 * PADDING) * 1 / 3, mainContainer.getHeight() - 2 * PADDING);
                    selectedSkinPlayerDisplay.setPosition(mainContainer.getWidth() - PADDING - selectedSkinPlayerDisplay.getWidth(),
                            (mainContainer.getHeight() - selectedSkinPlayerDisplay.getHeight()) / 2);

                    // The left container should use up all the available space next to the title label and the player display, with a gap of 10 pixels at each side
                    int titleLabelBottom = titleLabel.getY() + titleLabel.getHeight();
                    leftContainer.setBounds(PADDING, titleLabelBottom + 10,
                            selectedSkinPlayerDisplay.getX() - 10 - PADDING, mainContainer.getHeight() - PADDING - (titleLabelBottom + 10));
                });

        leftContainer
                .appendLayoutManager(c -> {
                    // Buttons at the bottom center
                    buttonContainer.setBounds(0, leftContainer.getHeight() - PADDING - 20, leftContainer.getWidth(), 20);

                    // The scrollable container should use up all the remaining available space in the left container, keeping a gap of 10 pixels to the buttons
                    scrollableContainer.setBounds(0, 0, leftContainer.getWidth(), buttonContainer.getY() - 10);
                });

        buttonContainer
                .appendLayoutManager(new AlignLayout(Axis.Y, 0))
                .appendLayoutManager(new SqueezeLayout(Axis.X, 0, 4)
                        .weight(1.1, setSkinButton)
                        .weight(1.5, clearSkinButton)
                        .weight(1, cancelButton));

        scrollableContainer
                .appendLayoutManager(c -> {
                    scrollbar.setPosition(scrollableContainer.getWidth() - scrollbar.getWidth(), 0);
                })
                .appendLayoutManager(new AlignLayout(Axis.X, 0))
                .appendLayoutManager(new FlowLayout(Axis.Y, 0, SCROLLABLE_CONTAINER_WIDGET_V_PADDING));
    }

    private void setSkinToSelected() {

        // This quickly gives us the currently selected skin
        Skin skin = selectedSkinPlayerDisplay.getCustomSkin();

        if (skin != null) {
            // Tell the server that this player has selected a new skin to wear
            NetworkService.DISPATCHER.sendToServer(new SetSkinServerMessage(skin));
        }
    }

    private void clearSkin() {

        // Tell the server that this player has chosen to wear his default skin again
        NetworkService.DISPATCHER.sendToServer(new ClearSkinServerMessage());
    }

    @Override
    public void drawBackground() {

        // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
        drawRect(mainContainer.getX(), mainContainer.getY(), mainContainer.getX() + mainContainer.getWidth(), mainContainer.getY() + mainContainer.getHeight(), 0xC0101010);
    }

    private class FocusableSkinLabel extends LabelFocusableImpl {

        private FocusableSkinLabel(Skin skin) {

            super(skin.getName());

            setUserData(skin);
        }

        @Override
        public void focusGained() {

            super.focusGained();

            selectedSkinPlayerDisplay.setCustomSkin((Skin) getUserData());
        }

        @Override
        public void focusLost() {

            super.focusLost();

            if (selectedSkinPlayerDisplay.getCustomSkin() == getUserData()) {
                selectedSkinPlayerDisplay.setCustomSkin(null);
            }
        }

    }

}
