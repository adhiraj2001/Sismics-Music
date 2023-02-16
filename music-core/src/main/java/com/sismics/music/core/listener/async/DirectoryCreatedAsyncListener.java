package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.event.async.DirectoryAsyncEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Directory;
import com.sismics.music.core.service.collection.CollectionService;


/**
 * New directory created listener.
 *
 * @author jtremeaux
 */
public class DirectoryCreatedAsyncListener extends DirectoryAsyncListener {
    /**
     * Process the event.
     *
     * @param event New directory created event
     */
    @Subscribe
    public void onDirectoryCreated(final DirectoryAsyncEvent event) throws Exception {
        processDirectoryEvent(event, this::updateCollection);
    }

    private void updateCollection(Directory directory) {
        // Index new directory
        this.indexNewDirectory(directory);

        // Watch new directory
        this.watchNewDirectory(directory);

        // Update the scores
        this.updateCollectionScore();
    }

    private void indexNewDirectory(Directory directory) {
        this.collectionService.addDirectoryToIndex(directory);
    }

    private void watchNewDirectory(Directory directory) {
        this.collectionWatchService.watchDirectory(directory);
    }

    private void updateCollectionScore() {
        this.collectionService.updateScore();
    }
}
