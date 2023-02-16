package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.dao.dbi.UserDao;
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
public class PlayStartedAsyncListener extends PlayAsyncListener{

    /**
     * Process the event.
     *
     * @param event Play completed event
     */
    @Subscribe
    public void onPlayCompleted(final PlayEvent event) throws Exception {
        processPlayEvent(event, this::PlayStartedEvent);
    }
    private void PlayStartedEvent(String userId, Track track) {
        this.nowPlayingTrack(track);
    }

    private void nowPlayingTrack(final Track track) {
        if (this.user != null && this.user.getLastFmSessionToken() != null) {
            if (this.log.isInfoEnabled()) {
                this.log.info("Now playing track: " + track.toString());
            }

            this.lastFmService.nowPlayingTrack(this.user, track);
        }
    }
}
