package com.sismics.music.core.listener.async;

import com.google.common.base.Stopwatch;
import com.sismics.music.core.event.async.LastFmLovedTrackAsyncEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.service.lastfm.LastFmService;
import com.sismics.music.core.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.function.Consumer;

/**
 * Last.fm update loved tracks listener.
 *
 * @author jtremeaux
 */
public abstract class LastFmLovedTrackAsyncListener {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(LastFmUpdateLovedTrackAsyncListener.class);

    protected LastFmService lastFmService;

    /**
     * Process the event.
     *
     * @param lastFmUpdateLovedTrackAsyncEvent Update loved track event
     */
    public void processLastFmLovedTrackEvent(LastFmLovedTrackAsyncEvent event, Consumer<User> operation)  {
        
        if (log.isInfoEnabled()) {
            log.info(event.getClass().getSimpleName() + ": " + event.toString());
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        final User user = event.getUser();

        this.lastFmService = AppContext.getInstance().getLastFmService();

        TransactionUtil.handle(() -> {
            operation.accept(user);
        });

        if (log.isInfoEnabled()) {
            log.info(MessageFormat.format("Last.fm update loved track event completed in {0}", stopwatch));
        }
    }
}
