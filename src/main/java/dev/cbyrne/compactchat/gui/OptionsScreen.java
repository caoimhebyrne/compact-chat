package dev.cbyrne.compactchat.gui;

import dev.cbyrne.compactchat.config.Configuration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class OptionsScreen extends GameOptionsScreen {
    private final Screen parent;
    private ButtonListWidget listWidget;

    public OptionsScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.literal("CompactChat Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        var doneButton = new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> saveAndClose());
        var enableInfiniteChatHistory = SimpleOption.ofBoolean(
            "compactchat.infiniteChatSwitch",
            Configuration.INSTANCE.infiniteChatHistory,
            value -> Configuration.INSTANCE.infiniteChatHistory = value
        );

        this.listWidget = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.listWidget.addSingleOptionEntry(enableInfiniteChatHistory);

        this.addSelectableChild(this.listWidget);
        this.addDrawableChild(doneButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.listWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);

        // We don't have any tooltips at the moment (didn't really look that great with our current options), but let's do this anyways
        var tooltip = getHoveredButtonTooltip(this.listWidget, mouseX, mouseY);
        if (tooltip != null) {
            this.renderOrderedTooltip(matrices, tooltip, mouseX, mouseY);
        }
    }

    @Override
    public void removed() {
        super.removed();
        Configuration.INSTANCE.save();
    }

    private void saveAndClose() {
        Configuration.INSTANCE.save();
        MinecraftClient.getInstance().setScreen(this.parent);
    }
}
