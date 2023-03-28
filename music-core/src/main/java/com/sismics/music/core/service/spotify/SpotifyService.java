package com.sismics.music.core.service.spotify;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.sismics.music.core.constant.ConfigType;
import com.sismics.music.core.dao.dbi.ArtistDao;
import com.sismics.music.core.dao.dbi.TrackDao;
import com.sismics.music.core.dao.dbi.UserDao;
import com.sismics.music.core.dao.dbi.UserTrackDao;
import com.sismics.music.core.dao.dbi.criteria.TrackCriteria;
import com.sismics.music.core.dao.dbi.criteria.UserCriteria;
import com.sismics.music.core.dao.dbi.dto.TrackDto;
import com.sismics.music.core.dao.dbi.dto.UserDto;
import com.sismics.music.core.event.async.SpotifyUpdateLovedTrackAsyncEvent;
import com.sismics.music.core.event.async.SpotifyUpdateTrackPlayCountAsyncEvent;
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Artist;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.util.ConfigUtil;
import com.sismics.music.core.util.TransactionUtil;
import com.sismics.util.spotify.SpotifyUtil;
// import de.umass.lastfm.*;
// import de.umass.lastfm.scrobble.ScrobbleData;
// import de.umass.lastfm.scrobble.ScrobbleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Last.fm service.
 *
 * @author pratham
 */

public class SpotifyService extends AbstractScheduledService {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class);

    @Override
    protected void runOneIteration() throws Exception {
        TransactionUtil.handle(() -> {
            UserDao userDao = new UserDao();
            List<UserDto> userList = userDao.findByCriteria(new UserCriteria().setSpotifySessionTokenNotNull(true));
            for (UserDto userDto : userList) {
                User user = userDao.getActiveById(userDto.getId());
                AppContext.getInstance().getSpotifyEventBus().post(new SpotifyUpdateLovedTrackAsyncEvent(user));
                AppContext.getInstance().getSpotifyEventBus().post(new SpotifyUpdateTrackPlayCountAsyncEvent(user));
            }
        });
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(23, 24, TimeUnit.HOURS);
    }
    
}
