package org.radonlab.raterm.term;

import com.google.common.base.Strings;
import lombok.Getter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.plist.XMLPropertyListConfiguration;
import org.jetbrains.annotations.Nullable;
import org.radonlab.raterm.conf.Configs;
import org.radonlab.raterm.core.Color;
import org.radonlab.raterm.term.palette.ITermColorPalette;
import org.radonlab.raterm.terminal.emulator.ColorPalette;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ColorPaletteLoader {
    private static final ColorDef[] colorIndexes = {
            ColorDef.BlackColor,
            ColorDef.RedColor,
            ColorDef.GreenColor,
            ColorDef.YellowColor,
            ColorDef.BlueColor,
            ColorDef.MagentaColor,
            ColorDef.CyanColor,
            ColorDef.WhiteColor,
            ColorDef.BrightBlackColor,
            ColorDef.BrightRedColor,
            ColorDef.BrightGreenColor,
            ColorDef.BrightYellowColor,
            ColorDef.BrightBlueColor,
            ColorDef.BrightMagentaColor,
            ColorDef.BrightCyanColor,
            ColorDef.BrightWhiteColor,
    };

    private static final String schemeExtName = ".itermcolors";

    public URL findSchemaByName(@Nullable String schemeName) {
        try {
            if (!Strings.isNullOrEmpty(schemeName)) {
                if (!schemeName.endsWith(schemeExtName)) {
                    schemeName = schemeName + schemeExtName;
                }
                Path schemeFile = Paths.get(Configs.getAppConfigDir(), "schemes", schemeName);
                if (Files.exists(schemeFile)) {
                    // user scheme
                    return schemeFile.toUri().toURL();
                }
                // builtin scheme
                String schemeId = schemeName.replace(" ", ".").toLowerCase();
                URL schemeRes = getClass().getResource("/schemes/" + schemeId);
                if (schemeRes != null) {
                    return schemeRes;
                }
            }
            return getClass().getResource("/schemes/" + Configs.getTerminalDefaultScheme() + schemeExtName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public ColorPalette loadFromSchema(@Nullable String schemeName) {
        try (InputStream is = findSchemaByName(schemeName).openStream()) {
            XMLPropertyListConfiguration config = new XMLPropertyListConfiguration();
            config.read(new InputStreamReader(is));
            Color[] indexColors = new Color[colorIndexes.length];
            for (int i = 0; i < colorIndexes.length; i++) {
                ColorDef index = colorIndexes[i];
                Configuration dict = config.subset(index.getKey());
                indexColors[i] = createColorFromConfig(dict);
            }
            Configuration textColorDict = config.subset(ColorDef.TextColor.getKey());
            Color textColor = createColorFromConfig(textColorDict);
            Configuration backgroundColorDict = config.subset(ColorDef.BackgroundColor.getKey());
            Color backgroundColor = createColorFromConfig(backgroundColorDict);
            return new ITermColorPalette(indexColors, textColor, backgroundColor);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Color createColorFromConfig(Configuration dict) {
        float red = dict.getFloat(ColorComponent.Red.getKey());
        float green = dict.getFloat(ColorComponent.Green.getKey());
        float blue = dict.getFloat(ColorComponent.Blue.getKey());
        float alpha = dict.getFloat(ColorComponent.Alpha.getKey());
        int r = (int) (red * 0xFF);
        int g = (int) (green * 0xFF);
        int b = (int) (blue * 0xFF);
        int a = (int) (alpha * 0xFF);
        return new Color(r, g, b, a);
    }

    private enum ColorDef {
        BlackColor("Ansi 0 Color"),
        RedColor("Ansi 1 Color"),
        GreenColor("Ansi 2 Color"),
        YellowColor("Ansi 3 Color"),
        BlueColor("Ansi 4 Color"),
        MagentaColor("Ansi 5 Color"),
        CyanColor("Ansi 6 Color"),
        WhiteColor("Ansi 7 Color"),
        BrightBlackColor("Ansi 8 Color"),
        BrightRedColor("Ansi 9 Color"),
        BrightGreenColor("Ansi 10 Color"),
        BrightYellowColor("Ansi 11 Color"),
        BrightBlueColor("Ansi 12 Color"),
        BrightMagentaColor("Ansi 13 Color"),
        BrightCyanColor("Ansi 14 Color"),
        BrightWhiteColor("Ansi 15 Color"),
        BackgroundColor("Background Color"),
        TextColor("Foreground Color"),
        TextBoldColor("Bold Color"),
        SelectionColor("Selection Color"),
        CursorColor("Cursor Color");

        @Getter
        private final String key;

        ColorDef(String key) {
            this.key = key;
        }
    }

    private enum ColorComponent {
        Red("Red Component"),
        Green("Green Component"),
        Blue("Blue Component"),
        Alpha("Alpha Component");

        @Getter
        private final String key;

        ColorComponent(String key) {
            this.key = key;
        }
    }
}
