package org.radonlab.raterm.terminal.model.hyperlinks;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author traff
 */
public class LinkResult {
    private final List<LinkResultItem> myItemList;

    public LinkResult(@NotNull LinkResultItem item) {
        this(Collections.singletonList(item));
    }

    public LinkResult(@NotNull List<LinkResultItem> itemList) {
        myItemList = itemList;
    }

    public List<LinkResultItem> getItems() {
        return myItemList;
    }
}
