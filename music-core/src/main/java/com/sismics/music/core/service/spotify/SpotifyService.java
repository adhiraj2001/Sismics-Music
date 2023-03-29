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
import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.model.dbi.Artist;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.model.dbi.User;
import com.sismics.music.core.util.ConfigUtil;
import com.sismics.music.core.util.TransactionUtil;
import com.sismics.util.spotify.SpotifyUtil;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import org.apache.hc.core5.http.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.Timestamp;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Spotify service.
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
                authorizationCodeRefresh_Sync(user);

                // AppContext.getInstance().getSpotifyEventBus().post(new SpotifyRefreshAsyncEvent(user));
            }
        });
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(expires_in, expires_in, TimeUnit.SECONDS);
    }

    /**
     * Create a new user session.
     *
     * @param spotifyUsername User name
     * @param spotifyPassword Password
     * @return Spotify session
     */


    private String clientId = ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_KEY);
    private String clientSecret = ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_SECRET);
    private URI redirectUri = SpotifyHttpManager.makeUri(ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_REDIRECT_URI));

    private int expires_in = 3500;

    public int getExpires_in() {
        return expires_in;
    }

    public void authorizationCodeUri_Sync(User user) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authorizationCodeUriRequest.execute();

        String code = uri.getQuery().split("=")[1].split("&")[0];
        user.setSpotifyAuthCode(code);

        System.out.println("Code: " + code);
    }

    public void authorizationCode_Sync(User user) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(user.getSpotifyAuthCode()).build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            expires_in = authorizationCodeCredentials.getExpiresIn();
            System.out.println("Expires in: " + expires_in);

            // Set access and refresh token for further "user" object usage
            user.setSpotifyAccessToken(authorizationCodeCredentials.getAccessToken());
            user.setSpotifyRefreshToken(authorizationCodeCredentials.getRefreshToken());
            user.setSpotifyRefreshTime(new Timestamp(System.currentTimeMillis()));

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



    public void authorizationCodeRefresh_Sync(User user) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .setAccessToken(user.getSpotifyAccessToken())
        .setRefreshToken(user.getSpotifyRefreshToken())
        .build();
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            // Set access and refresh token for further "user" object usage
            user.setSpotifyAccessToken(authorizationCodeCredentials.getAccessToken());
            expires_in = authorizationCodeCredentials.getExpiresIn();
            System.out.println("Expires in: " + expires_in);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
