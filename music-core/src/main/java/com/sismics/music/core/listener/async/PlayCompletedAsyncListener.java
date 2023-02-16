package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.dao.dbi.UserDao;
import com.sismics.music.core.dao.dbi.UserTrackDao;
import com.sismics.music.core.event.async.PlayEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.service.lastfm.LastFmService;

/**
 * Play completed listener.
 *
 * @author jtremeaux
 */
public class PlayCompletedAsyncListener extends PlayAsyncListener{

    /**
     * Process the event.
     *
     * @param event Play completed event
     */
    @Subscribe
    public void onPlayCompleted(final PlayEvent event) throws Exception {
        processPlayEvent(event, this::PlayCompletedEvent);
    }
    private void PlayCompletedEvent(String userId, Track track) {
        // Increment the play count
        this.incrementPlayCount(userId, track.getId());

        this.scrobbleTrack(track);
    }

    private void incrementPlayCount(String userId, String trackId) {
        this.userTrackDao.incrementPlayCount(userId, trackId);
    }

    private void scrobbleTrack(Track track) {
        if (this.user != null && this.user.getLastFmSessionToken() != null) {
            this.lastFmService.scrobbleTrack(this.user, track);
        }
    }
}
