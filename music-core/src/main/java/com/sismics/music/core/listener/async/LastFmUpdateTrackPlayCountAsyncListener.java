package com.sismics.music.core.listener.async;
import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.event.async.LastFmLovedTrackAsyncEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.service.lastfm.LastFmService;

/**
 * Last.fm update loved tracks listener.
 *
 * @author jtremeaux
 */
public class LastFmUpdateTrackPlayCountAsyncListener extends LastFmLovedTrackAsyncListener{
    /**
     * Process the event.
     *
     * @param event Update loved track event
     */
    @Subscribe
    public void onLastFmLovedTrackEvent(final LastFmLovedTrackAsyncEvent event) throws Exception {
        processLastFmLovedTrackEvent(event, this::updateLovedTrack);
    }


    private void updateLovedTrack(User user) {
        final LastFmService lastFmService = AppContext.getInstance().getLastFmService();
        lastFmService.importTrackPlayCount(user);
    }

}

