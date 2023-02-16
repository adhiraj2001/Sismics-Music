package com.sismics.music.core.listener.async;

import com.google.common.eventbus.Subscribe;
import com.sismics.music.core.dao.dbi.UserDao;
import com.sismics.music.core.dao.dbi.UserTrackDao;
import com.sismics.music.core.event.async.PlayEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.service.lastfm.LastFmService;
import com.sismics.music.core.util.TransactionUtil;

import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play completed listener.
 *
 * @author jtremeaux
 */
public abstract class PlayAsyncListener {
    /**
     * Logger.
     */
    protected static final Logger log = LoggerFactory.getLogger(PlayCompletedAsyncListener.class);

    protected UserTrackDao userTrackDao;
    protected User user;
    protected LastFmService lastFmService;

    /**
     * Process the event.
     *
     * @param playCompletedEvent Play completed event
     */
    @Subscribe
    public void processPlayEvent(PlayEvent event, BiConsumer<String, Track> operation){
        if (log.isInfoEnabled()) {
            log.info(event.getClass().getSimpleName() + ": " + event.toString());
        }

        final String userId = event.getUserId();
        final Track track = event.getTrack();

        this.userTrackDao = new UserTrackDao();
        this.user = new UserDao().getActiveById(userId);
        this.lastFmService = AppContext.getInstance().getLastFmService();

        TransactionUtil.handle(() -> {
            operation.accept(userId, track);
        });
    }
}
