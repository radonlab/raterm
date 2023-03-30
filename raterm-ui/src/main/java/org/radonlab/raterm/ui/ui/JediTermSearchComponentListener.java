package org.radonlab.raterm.ui.ui;

import org.jetbrains.annotations.NotNull;

public interface JediTermSearchComponentListener {
    void searchSettingsChanged(@NotNull String textToFind, boolean ignoreCase);

    void hideSearchComponent();

    void selectNextFindResult();

    void selectPrevFindResult();
}
