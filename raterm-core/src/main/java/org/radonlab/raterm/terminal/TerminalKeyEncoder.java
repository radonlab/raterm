package org.radonlab.raterm.terminal;

import org.radonlab.raterm.core.Platform;
import org.radonlab.raterm.core.input.InputEvent;
import org.radonlab.raterm.core.util.Ascii;
import org.radonlab.raterm.terminal.util.CharUtils;
import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.core.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author traff
 */
public class TerminalKeyEncoder {
    private static final int ESC = Ascii.ESC;

    private final Platform myPlatform;
    private final Map<KeyCodeAndModifier, byte[]> myKeyCodes = new HashMap<>();

    private boolean myAltSendsEscape = true;
    private boolean myMetaSendsEscape = false;

    public TerminalKeyEncoder() {
        this(Platform.current());
    }

    public TerminalKeyEncoder(@NotNull Platform platform) {
        myPlatform = platform;
        setAutoNewLine(false);
        arrowKeysAnsiCursorSequences();
        configureLeftRight();
        keypadAnsiSequences();
        putCode(KeyEvent.VK_BACK_SPACE, Ascii.DEL);
        putCode(KeyEvent.VK_F1, ESC, 'O', 'P');
        putCode(KeyEvent.VK_F2, ESC, 'O', 'Q');
        putCode(KeyEvent.VK_F3, ESC, 'O', 'R');
        putCode(KeyEvent.VK_F4, ESC, 'O', 'S');
        putCode(KeyEvent.VK_F5, ESC, '[', '1', '5', '~');
        putCode(KeyEvent.VK_F6, ESC, '[', '1', '7', '~');
        putCode(KeyEvent.VK_F7, ESC, '[', '1', '8', '~');
        putCode(KeyEvent.VK_F8, ESC, '[', '1', '9', '~');
        putCode(KeyEvent.VK_F9, ESC, '[', '2', '0', '~');
        putCode(KeyEvent.VK_F10, ESC, '[', '2', '1', '~');
        putCode(KeyEvent.VK_F11, ESC, '[', '2', '3', '~');
        putCode(KeyEvent.VK_F12, ESC, '[', '2', '4', '~');

        putCode(KeyEvent.VK_INSERT, ESC, '[', '2', '~');
        putCode(KeyEvent.VK_DELETE, ESC, '[', '3', '~');

        putCode(KeyEvent.VK_PAGE_UP, ESC, '[', '5', '~');
        putCode(KeyEvent.VK_PAGE_DOWN, ESC, '[', '6', '~');

        putCode(KeyEvent.VK_HOME, ESC, '[', 'H');
        putCode(KeyEvent.VK_END, ESC, '[', 'F');

        putCode(new KeyCodeAndModifier(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK), ESC, '[', 'Z');
    }

    public void arrowKeysApplicationSequences() {
        putCode(KeyEvent.VK_UP, ESC, 'O', 'A');
        putCode(KeyEvent.VK_DOWN, ESC, 'O', 'B');
        putCode(KeyEvent.VK_RIGHT, ESC, 'O', 'C');
        putCode(KeyEvent.VK_LEFT, ESC, 'O', 'D');
    }

    public void arrowKeysAnsiCursorSequences() {
        putCode(KeyEvent.VK_UP, ESC, '[', 'A');
        putCode(KeyEvent.VK_DOWN, ESC, '[', 'B');
        putCode(KeyEvent.VK_RIGHT, ESC, '[', 'C');
        putCode(KeyEvent.VK_LEFT, ESC, '[', 'D');
    }

    private void configureLeftRight() {
        if (myPlatform == Platform.Mac) {
            putCode(new KeyCodeAndModifier(KeyEvent.VK_RIGHT, InputEvent.ALT_MASK), ESC, 'f'); // ^[f
            putCode(new KeyCodeAndModifier(KeyEvent.VK_LEFT, InputEvent.ALT_MASK), ESC, 'b'); // ^[b
        } else {
            putCode(new KeyCodeAndModifier(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK), ESC, '[', '1', ';', '5', 'D'); // ^[[1;5D
            putCode(new KeyCodeAndModifier(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK), ESC, '[', '1', ';', '5', 'C'); // ^[[1;5C
            putCode(new KeyCodeAndModifier(KeyEvent.VK_LEFT, InputEvent.ALT_MASK), ESC, '[', '1', ';', '3', 'D'); // ^[[1;3D
            putCode(new KeyCodeAndModifier(KeyEvent.VK_RIGHT, InputEvent.ALT_MASK), ESC, '[', '1', ';', '3', 'C'); // ^[[1;3C
        }
    }

    public void keypadApplicationSequences() {
        putCode(KeyEvent.VK_KP_DOWN, ESC, 'O', 'B'); //2
        putCode(KeyEvent.VK_KP_LEFT, ESC, 'O', 'D'); //4
        putCode(KeyEvent.VK_KP_RIGHT, ESC, 'O', 'C'); //6
        putCode(KeyEvent.VK_KP_UP, ESC, 'O', 'A'); //8

        putCode(KeyEvent.VK_HOME, ESC, 'O', 'H');
        putCode(KeyEvent.VK_END, ESC, 'O', 'F');
    }

    public void keypadAnsiSequences() {
        putCode(KeyEvent.VK_KP_DOWN, ESC, '[', 'B'); //2
        putCode(KeyEvent.VK_KP_LEFT, ESC, '[', 'D'); //4
        putCode(KeyEvent.VK_KP_RIGHT, ESC, '[', 'C'); //6
        putCode(KeyEvent.VK_KP_UP, ESC, '[', 'A'); //8

        putCode(KeyEvent.VK_HOME, ESC, '[', 'H');
        putCode(KeyEvent.VK_END, ESC, '[', 'F');
    }

    void putCode(final int code, final int... bytesAsInt) {
        myKeyCodes.put(new KeyCodeAndModifier(code, 0), CharUtils.makeCode(bytesAsInt));
    }

    private void putCode(@NotNull KeyCodeAndModifier key, final int... bytesAsInt) {
        myKeyCodes.put(key, CharUtils.makeCode(bytesAsInt));
    }

    public byte[] getCode(final int key, int modifiers) {
        byte[] bytes = myKeyCodes.get(new KeyCodeAndModifier(key, modifiers));
        if (bytes != null) {
            return bytes;
        }
        bytes = myKeyCodes.get(new KeyCodeAndModifier(key, 0));
        if (bytes == null) {
            return null;
        }

        if ((myAltSendsEscape || alwaysSendEsc(key)) && (modifiers & InputEvent.ALT_MASK) != 0) {
            return insertCodeAt(bytes, CharUtils.makeCode(ESC), 0);
        }

        if ((myMetaSendsEscape || alwaysSendEsc(key)) && (modifiers & InputEvent.META_MASK) != 0) {
            return insertCodeAt(bytes, CharUtils.makeCode(ESC), 0);
        }

        if (isCursorKey(key) || isFunctionKey(key)) {
            return getCodeWithModifiers(bytes, modifiers);
        }

        return bytes;
    }

    private boolean alwaysSendEsc(int key) {
        return isCursorKey(key) || key == '\b';
    }

    private boolean isCursorKey(int key) {
        return key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_HOME || key == KeyEvent.VK_END;
    }

    private boolean isFunctionKey(int key) {
        return key >= KeyEvent.VK_F1 && key <= KeyEvent.VK_F12 || key == KeyEvent.VK_INSERT || key == KeyEvent.VK_DELETE || key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_PAGE_DOWN;
    }

    /**
     * Refer to section PC-Style Function Keys in http://invisible-island.net/xterm/ctlseqs/ctlseqs.html
     */
    private byte[] getCodeWithModifiers(byte[] bytes, int modifiers) {
        int code = modifiersToCode(modifiers);

        if (code > 0 && bytes.length > 2) {
            // SS3 needs to become CSI.
            if (bytes[0] == ESC && bytes[1] == 'O') bytes[1] = '[';
            // If the control sequence has no parameters, it needs a default parameter.
            // Either way it also needs a semicolon separator.
            String prefix = bytes.length == 3 ? "1;" : ";";
            return insertCodeAt(bytes, (prefix + code).getBytes(), bytes.length - 1);
        }
        return bytes;
    }

    private static byte[] insertCodeAt(byte[] bytes, byte[] code, int at) {
        byte[] res = new byte[bytes.length + code.length];
        System.arraycopy(bytes, 0, res, 0, bytes.length);
        System.arraycopy(bytes, at, res, at + code.length, bytes.length - at);
        System.arraycopy(code, 0, res, at, code.length);
        return res;
    }

    /**
     * Code     Modifiers
     * ------+--------------------------
     * 2     | Shift
     * 3     | Alt
     * 4     | Shift + Alt
     * 5     | Control
     * 6     | Shift + Control
     * 7     | Alt + Control
     * 8     | Shift + Alt + Control
     * 9     | Meta
     * 10    | Meta + Shift
     * 11    | Meta + Alt
     * 12    | Meta + Alt + Shift
     * 13    | Meta + Ctrl
     * 14    | Meta + Ctrl + Shift
     * 15    | Meta + Ctrl + Alt
     * 16    | Meta + Ctrl + Alt + Shift
     * ------+--------------------------
     *
     * @param modifiers
     * @return
     */
    private static int modifiersToCode(int modifiers) {
        int code = 0;
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            code |= 1;
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            code |= 2;
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            code |= 4;
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            code |= 8;
        }
        return code != 0 ? code + 1 : code;
    }

    public void setAutoNewLine(boolean enabled) {
        if (enabled) {
            putCode(KeyEvent.VK_ENTER, Ascii.CR, Ascii.LF);
        } else {
            putCode(KeyEvent.VK_ENTER, Ascii.CR);
        }
    }

    public void setAltSendsEscape(boolean altSendsEscape) {
        myAltSendsEscape = altSendsEscape;
    }

    public void setMetaSendsEscape(boolean metaSendsEscape) {
        myMetaSendsEscape = metaSendsEscape;
    }

    private static class KeyCodeAndModifier {
        private final int myCode;
        private final int myModifier;

        public KeyCodeAndModifier(int code, int modifier) {
            myCode = code;
            myModifier = modifier;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeyCodeAndModifier that = (KeyCodeAndModifier) o;
            return myCode == that.myCode && myModifier == that.myModifier;
        }

        @Override
        public int hashCode() {
            return Objects.hash(myCode, myModifier);
        }
    }
}
