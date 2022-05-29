package dev.cbyrne.compactchat.gui;

import dev.cbyrne.compactchat.config.Configuration;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.option.SpruceToggleBooleanOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OptionsScreen extends SpruceScreen {
    private final Screen parent;
    private final SpruceOption infiniteChatSwitch;

    public OptionsScreen(Screen parent) {
        super(Text.literal("CompactChat Settings"));

        this.parent = parent;
        this.infiniteChatSwitch = new SpruceToggleBooleanOption("compactchat.infiniteChatSwitch",
            () -> Configuration.INSTANCE.infiniteChatHistory,
            newValue -> Configuration.INSTANCE.infiniteChatHistory = newValue,
            Text.literal("Modify the chat history's length to be infinite instead of 100 messages")
        );
    }

    @Override
    protected void init() {
        super.init();

        var list = new SpruceOptionListWidget(Position.of(0, 22), this.width, this.height - 35 - 22);
        list.addOptionEntry(this.infiniteChatSwitch, null);

        var doneButton = new SpruceButtonWidget(
            Position.of(this, 50, this.height - 29),
            this.width - 100,
            20,
            SpruceTexts.GUI_DONE,
            (buttonWidget) -> this.close()
        );

        this.addDrawableChild(list);
        this.addDrawableChild(doneButton);
    }

    @Override
    public void close() {
        Configuration.INSTANCE.save();
        MinecraftClient.getInstance().setScreen(this.parent);
        super.close();
    }
}
