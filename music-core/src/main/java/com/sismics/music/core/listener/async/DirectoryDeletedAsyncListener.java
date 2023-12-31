package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.event.async.DirectoryAsyncEvent;
import com.sismics.music.core.model.dbi.Directory;

/**
 * New directory created listener.
 *
 * @author jtremeaux
 */
public class DirectoryDeletedAsyncListener extends DirectoryAsyncListener {
    /**
     * Process the event.
     *
     * @param event New directory created event
     */
    @Subscribe
    public void onDirectoryDeleted(final DirectoryAsyncEvent event) throws Exception {
        processDirectoryEvent(event, this::updateCollection);
    }

    private void updateCollection(Directory directory) {
        // Stop watching the directory
        this.unwatchDirectory(directory);

        // Remove directory from index
        this.removeDirectory(directory);
    }

    private void removeDirectory(Directory directory) {
        this.collectionService.removeDirectoryFromIndex(directory);
    }

    private void unwatchDirectory(Directory directory) {
        this.collectionWatchService.unwatchDirectory(directory);
    }
}
