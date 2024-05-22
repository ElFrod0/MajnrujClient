package me.elfrodo.majnruj.client.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TitleTextsUtil {
    public enum TitleTextsEnum {
        A("/m code majnruj-client-enjoyer"),
        B("2444666668888888"),
        C("První poutník stále žije!"),
        D("Herobrine?"),
        E("Šest jevů Goldsteinů"),
        F("Heimdallr viděl co jsi provedl!"),
        G("Nyní i bez cukru!"),
        H("Powered by HHC-P"),
        I("Hamburger Cheeseburger Big Mac Whopper"),
        J("Zase práce? Tak já teda jdu."),
        K("Samvěd Křepelka je ten hrdina!"),
        L("9. Opakování je zde."),
        M("Hlavní pravidlo nemluvit o..."),
        N("Cítíte zlou přítomnost, která vás sleduje..."),
        O("Jsou to 3 dimenze, 4 aspekty moci a 9 světů?"),
        P("Nebo 22 dimenzí, 6 aspektů moci a nespočet světů?"),
        Q("A nebo 29 dimenzí, nespočet aspektů moci a nespočet světů?"),
        R("Chvála slunci!"),
        S("HLUK = SMÍCH"),
        T("1 dřevo =/= 85.33 tlačítek"),
        U("¯\\_(ツ)_/¯"),
        V("Hipopotomonstroseskvipedaliofobie"),
        W("I II II I_"),
        X("Řím nebyl postaven za jeden den."),
        Y("boxpig41");
    
        private final String text;
    
        TitleTextsEnum(String text) {
            this.text = text;
        }
    
        public String getText() {
            return text;
        }
    
        private static final List<TitleTextsEnum> VALUES = Arrays.asList(values());
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();
    
        private static TitleTextsEnum getRandomText() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }
    private static TitleTextsEnum titleText;

    public static String getTitleText() {
        if (titleText == null) {
            titleText = TitleTextsEnum.getRandomText();
        }
        return titleText.getText();
    }
}

