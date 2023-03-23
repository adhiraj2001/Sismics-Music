package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.event.async.TrackAsyncEvent;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;

/**
 * Track liked listener.
 *
 * @author jtremeaux
 */
public class TrackLikedAsyncListener extends TrackAsyncListener{
    /**
     * Process the event.
     *
     * @param event New directory created event
     */
    @Subscribe
    public void onTrackLiked(final TrackAsyncEvent event) throws Exception {
        processTrackEvent(event, this::updateTrack);

    }

    private void updateTrack(User user, Track track) {
        if (user.getLastFmSessionToken() != null) {
            this.loveTrack(user, track);
        }
    }

    private void loveTrack(User user, Track track) {
        this.lastFmService.loveTrack(user, track);
    }
}
