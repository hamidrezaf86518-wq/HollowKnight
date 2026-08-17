package com.hollowknight.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class LabelStyle {

    public static Label.LabelStyle getStyle(int fontSize) {

        FreeTypeFontGenerator gen =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/TrajanPro-Bold.otf"));

        FreeTypeFontGenerator.FreeTypeFontParameter par =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        par.size = fontSize;
        par.color = Color.WHITE;

        BitmapFont font = gen.generateFont(par);
        gen.dispose();


        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        return style;
    }
}
