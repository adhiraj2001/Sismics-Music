package com.sismics.music.core.listener.async;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.event.async.TrackAsyncEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.service.lastfm.LastFmService;
import com.sismics.music.core.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.function.BiConsumer;

/**
 * Track liked listener.
 *
 * @author jtremeaux
 */
public abstract class TrackAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(TrackLikedAsyncListener.class);

    /**
     * Process the event.
     *
     * @param trackLikedAsyncEvent New directory created event
     */
    @Subscribe
    public void processTrackEvent(TrackAsyncEvent event, BiConsumer<User, Track> operation){
        if (log.isInfoEnabled()) {
            log.info(event.getClass().getSimpleName() + ": " + event.toString());

        }
        Stopwatch stopwatch = Stopwatch.createStarted();

        final User user = event.getUser();
        final Track track = event.getTrack();

        TransactionUtil.handle(() -> {
            operation.accept(user, track);
        });

        if (log.isInfoEnabled()) {
            log.info(MessageFormat.format("Track liked completed in {0}", stopwatch));
        }
    }
}
