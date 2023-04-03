package com.sismics.music.rest.resource;

import com.sismics.music.core.dao.dbi.AlbumDao;
import com.sismics.music.core.dao.dbi.ArtistDao;
import com.sismics.music.core.dao.dbi.TrackDao;
import com.sismics.music.core.dao.dbi.criteria.AlbumCriteria;
import com.sismics.music.core.dao.dbi.criteria.ArtistCriteria;
import com.sismics.music.core.dao.dbi.criteria.TrackCriteria;
import com.sismics.music.core.dao.dbi.dto.AlbumDto;
import com.sismics.music.core.dao.dbi.dto.ArtistDto;
import com.sismics.music.core.dao.dbi.dto.TrackDto;
import com.sismics.music.core.util.dbi.PaginatedList;
import com.sismics.music.core.util.dbi.PaginatedLists;
import com.sismics.music.rest.util.JsonUtil;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;
import com.sismics.music.core.service.lastfm.LastFmService;
import de.umass.lastfm.*;

@Path("/externalsearch")
public class ExternalSearchResource {
    private static final Logger log = LoggerFactory.getLogger(ExternalSearchResource.class);

    @GET
    @Path("/lastfmsearch")
    public JsonObject lastfmsearch(@QueryParam("query") String query) {
        // Parse the query and extract song name
        // String[] queryParts = query.split(":");
        // String songName = queryParts[1];
        // songName = songName.substring(1, songName.length() - 2);
        String songName = query;
        final LastFmService lastFmService = new LastFmService();
        Collection<de.umass.lastfm.Track> tracks = lastFmService.searchTrack(songName, 10);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (de.umass.lastfm.Track track : tracks) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("name", track.getName());
            objectBuilder.add("artist", track.getArtist());
            arrayBuilder.add(objectBuilder);
        }
        return Json.createObjectBuilder().add("tracks", arrayBuilder).build();
    }
}
