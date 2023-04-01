package com.sismics.music.core.service.spotify;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.neovisionaries.i18n.CountryCode;
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
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

// import se.michaelthelin.spotify.SpotifyApi;
// import se.michaelthelin.spotify.SpotifyHttpManager;
// import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
// import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
// import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
// import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
// import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

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

    private String clientId;
    private String clientSecret;
    private URI redirectUri;

    private int expires_in;

    @Override
    protected void startUp() throws Exception {
        log.info("Starting SpotifyService");
        clientId = ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_KEY);
        clientSecret = ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_SECRET);
        redirectUri = SpotifyHttpManager.makeUri(ConfigUtil.getConfigStringValue(ConfigType.SPOTIFY_API_REDIRECT_URI));
    
        expires_in = 3500;
    }

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
            user.setSpotifyRefreshToken(authorizationCodeCredentials.getRefreshToken());
            expires_in = authorizationCodeCredentials.getExpiresIn();
            System.out.println("Expires in: " + expires_in);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Paging<com.wrapper.spotify.model_objects.specification.Track> searchTracks_Sync(User user, String query) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setAccessToken(user.getSpotifyAccessToken())
        .build();
        final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query)
            //  .market(CountryCode.SE)
            //  .limit(10)
            //  .offset(0)
            //  .includeExternal("audio")
        .build();
        try {
          final Paging<com.wrapper.spotify.model_objects.specification.Track> trackPaging = searchTracksRequest.execute();
          return trackPaging;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
          System.out.println("Error: " + e.getMessage());
          return null;
        }
    }

    public Paging<AlbumSimplified> searchAlbums_Sync(User user, String query) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setAccessToken(user.getSpotifyAccessToken())
        .build();
        final SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(query)
            //  .market(CountryCode.SE)
            //  .limit(10)
            //  .offset(0)
            //  .includeExternal("audio")
        .build();
        try {
          final Paging<AlbumSimplified> albumPaging = searchAlbumsRequest.execute();
          return albumPaging;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
          System.out.println("Error: " + e.getMessage());
          return null;
        }
    }

    public Paging<com.wrapper.spotify.model_objects.specification.Artist> searchArtists_Sync(User user, String query) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setAccessToken(user.getSpotifyAccessToken())
        .build();
        final SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(query)
            //  .market(CountryCode.SE)
            //  .limit(10)
            //  .offset(0)
            //  .includeExternal("audio")
        .build();
        try {
          final Paging<com.wrapper.spotify.model_objects.specification.Artist> artistPaging = searchArtistsRequest.execute();
          return artistPaging;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
          System.out.println("Error: " + e.getMessage());
          return null;
        }
    }
}
