package com.github.alfixjanuarivinter.intellium;

import com.github.alfixjanuarivinter.intellium.config.IntelliumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class SimpleConfigScreen extends Screen {

    private final Screen parent;

    public SimpleConfigScreen(Screen parent) {
        super(Text.of("Intellium Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = this.height / 4;

        IntelliumConfig config = IntelliumConfig.getInstance();

        // Toggle Intel Optimizations
        this.addDrawableChild(ButtonWidget.builder(
                Text.of("Intel Optimizations: " + (config.enableIntelOptimizations ? "ON" : "OFF")),
                button -> {
                    config.enableIntelOptimizations = !config.enableIntelOptimizations;
                    button.setMessage(Text.of("Intel Optimizations: " + (config.enableIntelOptimizations ? "ON" : "OFF")));
                }
        ).dimensions(this.width / 2 - 100, y, 200, 20).build());

        y += 24;

        // View Distance Slider
        this.addDrawableChild(new SliderWidget(this.width / 2 - 100, y, 200, 20, Text.of("View Distance: " + config.viewDistance), (config.viewDistance - 2) / 14.0f) {
            @Override
            protected void updateMessage() {
                setMessage(Text.of("View Distance: " + config.viewDistance));
            }

            @Override
            protected void applyValue() {
                config.viewDistance = (int) (2 + this.value * 14);
                updateMessage();
            }
        });

        y += 24;

        // Done Button
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.done"), // Replaces ScreenTexts.DONE
                button -> {
                    config.save();
                    this.client.setScreen(parent);
                }
        ).dimensions(this.width / 2 - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int titleWidth = this.textRenderer.getWidth(this.title);
        context.drawTextWithShadow(this.textRenderer, this.title, (this.width - titleWidth) / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
