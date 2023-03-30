package org.radonlab.raterm.core.typeahead;

public interface Debouncer {
    void call();

    void terminateCall();
}
