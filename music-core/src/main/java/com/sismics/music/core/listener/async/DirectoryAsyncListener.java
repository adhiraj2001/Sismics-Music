package com.sismics.music.core.listener.async;

import com.google.common.base.Stopwatch;
import com.sismics.music.core.event.async.DirectoryAsyncEvent;
import com.sismics.music.core.model.dbi.Directory;
import com.sismics.music.core.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.function.Consumer;

/**
 * New directory created listener.
 *
 * @author jtremeaux
 */

public abstract class DirectoryAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(DirectoryAsyncListener.class);

    /**
     * Process the event.
     *
     * @param event Directory event
     * @param operation Operation to perform on the directory
     */
    protected void processDirectoryEvent(DirectoryAsyncEvent event, Consumer<Directory> operation) {
        if (log.isInfoEnabled()) {
            log.info(event.getClass().getSimpleName() + ": " + event.toString());
        }
        Stopwatch stopwatch = Stopwatch.createStarted();

        final Directory directory = event.getDirectory();

        TransactionUtil.handle(() -> {
            operation.accept(directory);
        });

        if (log.isInfoEnabled()) {
            log.info(MessageFormat.format("Collection updated in {0}ms", stopwatch));
        }
    }
}
