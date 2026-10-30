package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hollowknight.view.ui.Theme;

public class LabelStyle {

    public static Label.LabelStyle getStyle(int fontSize) {
        return Theme.labelStyle(fontSize);
    }
}
