package com.hollowknight.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Scaling;
import com.hollowknight.i18n.Text;

public final class Theme {
    public static final Color TEXT = new Color(0.93f, 0.95f, 0.99f, 1f);
    public static final Color MUTED = new Color(0.62f, 0.67f, 0.76f, 1f);
    public static final Color GOLD = new Color(0.98f, 0.84f, 0.48f, 1f);
    public static final Color GOOD = new Color(0.55f, 0.92f, 0.66f, 1f);
    public static final Color DARK = new Color(0.05f, 0.06f, 0.09f, 1f);

    private static final String FONT_PATH = "fonts/TrajanPro-Bold.otf";
    private static final String EXTRA_CHARS = "àâäçéèêëîïôöùûüÿœæÀÂÄÇÉÈÊËÎÏÔÖÙÛÜŸŒÆ«»’…–°";

    private static final IntMap<BitmapFont> fonts = new IntMap<>();
    private static final Array<Texture> textures = new Array<>();

    private static Skin skin;
    private static Texture pixel;
    private static Texture shade;
    private static Texture divider;
    private static Drawable panel;
    private static Drawable card;
    private static Drawable chip;
    private static Drawable chipOn;

    private Theme() {
    }

    public static Skin skin() {
        if (skin == null) {
            build();
        }
        return skin;
    }

    public static BitmapFont font(int size) {
        BitmapFont font = fonts.get(size);
        if (font != null) {
            return font;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + EXTRA_CHARS;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = new Color(0f, 0f, 0f, 0.55f);
        font = generator.generateFont(parameter);
        generator.dispose();
        fonts.put(size, font);
        return font;
    }

    public static Label.LabelStyle labelStyle(int size) {
        return new Label.LabelStyle(font(size), Color.WHITE);
    }

    public static Texture pixel() {
        skin();
        return pixel;
    }

    public static Texture shade() {
        skin();
        return shade;
    }

    public static Texture dividerTexture() {
        skin();
        return divider;
    }

    public static Drawable panel() {
        skin();
        return panel;
    }

    public static Drawable card() {
        skin();
        return card;
    }

    public static Drawable chip() {
        skin();
        return chip;
    }

    public static Drawable chipOn() {
        skin();
        return chipOn;
    }

    public static Drawable dim(float alpha) {
        return new TextureRegionDrawable(new TextureRegion(pixel())).tint(new Color(0f, 0f, 0f, alpha));
    }

    public static Label label(String text, String style) {
        return new Label(text, skin(), style);
    }

    public static Label label(String text) {
        return new Label(text, skin());
    }

    public static TextButton button(String text) {
        return new TextButton(text, skin());
    }

    public static TextButton button(String text, String style) {
        return new TextButton(text, skin(), style);
    }

    public static Image divider(float width) {
        Image image = new Image(dividerTexture());
        image.setScaling(Scaling.stretch);
        image.setSize(width, 13f);
        return image;
    }

    public static String formatTime(float seconds) {
        int total = (int) seconds;
        int hours = total / 3600;
        int minutes = (total % 3600) / 60;
        int secs = total % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        }
        return String.format("%02d:%02d", minutes, secs);
    }

    public static String keyName(int keycode) {
        return Input.Keys.toString(keycode).toUpperCase();
    }

    public static Dialog dialog(Stage stage, String title, Actor content, String closeText) {
        Dialog dialog = new Dialog("", skin());
        dialog.setMovable(false);
        dialog.pad(26f);
        Table body = dialog.getContentTable();
        body.add(label(title, "heading")).padBottom(8f).row();
        body.add(divider(360f)).width(360f).height(13f).padBottom(18f).row();
        body.add(content).row();
        dialog.getButtonTable().padTop(22f);
        dialog.getButtonTable().defaults().width(180f).height(44f).pad(6f);
        dialog.button(closeText, null);
        dialog.key(Input.Keys.ESCAPE, null);
        dialog.show(stage);
        return dialog;
    }

    public static Dialog confirm(Stage stage, String title, String message, Runnable onYes) {
        Dialog dialog = new Dialog("", skin()) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    onYes.run();
                }
            }
        };
        dialog.setMovable(false);
        dialog.pad(26f);
        Table body = dialog.getContentTable();
        body.add(label(title, "heading")).padBottom(8f).row();
        body.add(divider(360f)).width(360f).height(13f).padBottom(18f).row();
        Label text = label(message, "small");
        text.setWrap(true);
        text.setAlignment(Align.center);
        body.add(text).width(420f).row();
        dialog.getButtonTable().padTop(22f);
        dialog.getButtonTable().defaults().width(150f).height(44f).pad(6f);
        dialog.button(Text.YES.get(), true);
        dialog.button(Text.NO.get(), false);
        dialog.key(Input.Keys.ESCAPE, false);
        dialog.show(stage);
        return dialog;
    }

    public static boolean hasDialog(Stage stage) {
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Dialog) {
                return true;
            }
        }
        return false;
    }

    public static void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
        skin = null;
        pixel = null;
        shade = null;
        divider = null;
        panel = null;
        card = null;
        chip = null;
        chipOn = null;
    }

    private static void build() {
        skin = new Skin();
        pixel = createPixel();
        shade = createShade();
        divider = createDivider();

        panel = rounded(14, new Color(0.03f, 0.04f, 0.06f, 0.9f), new Color(0.6f, 0.65f, 0.76f, 0.55f), 2);
        card = rounded(10, new Color(0.06f, 0.07f, 0.1f, 0.85f), new Color(0.45f, 0.5f, 0.6f, 0.45f), 1);
        chip = rounded(6, new Color(0.1f, 0.12f, 0.17f, 0.95f), new Color(0.7f, 0.74f, 0.84f, 0.7f), 1);
        chipOn = rounded(6, new Color(0.93f, 0.95f, 1f, 1f), Color.WHITE, 1);

        skin.add("default", new Label.LabelStyle(font(20), TEXT));
        skin.add("small", new Label.LabelStyle(font(16), TEXT));
        skin.add("muted", new Label.LabelStyle(font(16), MUTED));
        skin.add("heading", new Label.LabelStyle(font(26), TEXT));
        skin.add("gold", new Label.LabelStyle(font(22), GOLD));
        skin.add("title", new Label.LabelStyle(font(46), Color.WHITE));
        skin.add("stat", new Label.LabelStyle(font(44), GOLD));

        Drawable up = rounded(10, new Color(0.05f, 0.06f, 0.09f, 0.82f), new Color(0.62f, 0.67f, 0.78f, 0.55f), 2);
        Drawable over = rounded(10, new Color(0.16f, 0.19f, 0.25f, 0.95f), new Color(0.97f, 0.98f, 1f, 1f), 2);
        Drawable down = rounded(10, new Color(0.93f, 0.95f, 1f, 1f), Color.WHITE, 2);
        Drawable disabled = rounded(10, new Color(0.05f, 0.06f, 0.09f, 0.5f), new Color(0.4f, 0.43f, 0.5f, 0.3f), 2);
        Drawable selected = rounded(10, new Color(0.82f, 0.86f, 0.95f, 0.96f), Color.WHITE, 2);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = up;
        buttonStyle.over = over;
        buttonStyle.down = down;
        buttonStyle.disabled = disabled;
        buttonStyle.font = font(22);
        buttonStyle.fontColor = TEXT;
        buttonStyle.overFontColor = Color.WHITE;
        buttonStyle.downFontColor = DARK;
        buttonStyle.disabledFontColor = new Color(0.5f, 0.53f, 0.6f, 0.6f);
        skin.add("default", buttonStyle);

        TextButton.TextButtonStyle selectedStyle = new TextButton.TextButtonStyle(buttonStyle);
        selectedStyle.up = selected;
        selectedStyle.over = selected;
        selectedStyle.down = selected;
        selectedStyle.fontColor = DARK;
        selectedStyle.overFontColor = DARK;
        selectedStyle.downFontColor = DARK;
        skin.add("selected", selectedStyle);

        TextButton.TextButtonStyle smallStyle = new TextButton.TextButtonStyle(buttonStyle);
        smallStyle.font = font(16);
        skin.add("small", smallStyle);

        NinePatchDrawable track = rounded(4, new Color(0.04f, 0.05f, 0.08f, 0.9f), new Color(0.5f, 0.55f, 0.65f, 0.7f), 1);
        track.setMinHeight(12f);
        NinePatchDrawable fill = rounded(4, new Color(0.82f, 0.86f, 0.95f, 0.95f), new Color(0.82f, 0.86f, 0.95f, 0.95f), 1);
        fill.setMinHeight(12f);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(track, createKnob());
        sliderStyle.knobBefore = fill;
        skin.add("default-horizontal", sliderStyle);

        NinePatchDrawable scrollTrack = rounded(3, new Color(0.04f, 0.05f, 0.08f, 0.6f), new Color(0.4f, 0.44f, 0.52f, 0.4f), 1);
        scrollTrack.setMinWidth(10f);
        NinePatchDrawable scrollKnob = rounded(3, new Color(0.8f, 0.85f, 0.95f, 0.85f), new Color(0.8f, 0.85f, 0.95f, 0.85f), 1);
        scrollKnob.setMinWidth(10f);
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScroll = scrollTrack;
        scrollStyle.vScrollKnob = scrollKnob;
        skin.add("default", scrollStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font(22), GOLD, panel);
        windowStyle.stageBackground = dim(0.6f);
        skin.add("default", windowStyle);
    }

    private static NinePatchDrawable rounded(int radius, Color fill, Color border, int borderWidth) {
        int edge = radius + 1;
        int size = edge * 2 + 2;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        fillRound(pixmap, 0, 0, size, size, radius, border);
        fillRound(pixmap, borderWidth, borderWidth, size - borderWidth * 2, size - borderWidth * 2, Math.max(1, radius - borderWidth), fill);
        Texture texture = register(pixmap);
        return new NinePatchDrawable(new NinePatch(texture, edge, edge, edge, edge));
    }

    private static void fillRound(Pixmap pixmap, int x, int y, int w, int h, int r, Color color) {
        pixmap.setColor(color);
        pixmap.fillRectangle(x + r, y, w - 2 * r, h);
        pixmap.fillRectangle(x, y + r, w, h - 2 * r);
        pixmap.fillCircle(x + r, y + r, r);
        pixmap.fillCircle(x + w - r - 1, y + r, r);
        pixmap.fillCircle(x + r, y + h - r - 1, r);
        pixmap.fillCircle(x + w - r - 1, y + h - r - 1, r);
    }

    private static Drawable createKnob() {
        int size = 26;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        pixmap.setColor(GOLD);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 1);
        pixmap.setColor(0.95f, 0.97f, 1f, 1f);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 4);
        return new TextureRegionDrawable(new TextureRegion(register(pixmap)));
    }

    private static Texture createPixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        return register(pixmap);
    }

    private static Texture createShade() {
        int height = 128;
        Pixmap pixmap = new Pixmap(4, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < height; y++) {
            float t = y / (float) (height - 1);
            pixmap.setColor(0f, 0f, 0f, (float) Math.pow(t, 1.6) * 0.92f);
            pixmap.drawLine(0, y, 3, y);
        }
        return register(pixmap);
    }

    private static Texture createDivider() {
        int width = 512;
        int height = 13;
        int cx = width / 2;
        int cy = height / 2;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();
        for (int x = 0; x < width; x++) {
            float t = 1f - Math.abs(x - cx) / (float) cx;
            pixmap.setColor(0.85f, 0.89f, 1f, (float) Math.pow(t, 1.3) * 0.75f);
            pixmap.drawPixel(x, cy);
        }
        for (int dy = -6; dy <= 6; dy++) {
            for (int dx = -6; dx <= 6; dx++) {
                int distance = Math.abs(dx) + Math.abs(dy);
                if (distance <= 6) {
                    if (distance <= 3) {
                        pixmap.setColor(0.03f, 0.04f, 0.06f, 1f);
                    } else {
                        pixmap.setColor(1f, 1f, 1f, 1f);
                    }
                    pixmap.drawPixel(cx + dx, cy + dy);
                }
            }
        }
        return register(pixmap);
    }

    private static Texture register(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        textures.add(texture);
        return texture;
    }
}
