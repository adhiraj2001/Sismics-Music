package com.sismics.music.core.dao.dbi.criteria;

import com.sismics.music.core.constant.AccessType;

/**
 * Playlist criteria.
 *
 * @author jtremeaux
 */
public class PlaylistCriteria {
    /**
     * Playlist ID.
     */
    private String id;
    
    /**
     * Returns the default playlist.
     */
    private Boolean defaultPlaylist;
    
    /**
     * Name (like).
     */
    private String nameLike;

    /**
     * Access type.
     */
    private AccessType access;

    /**
     * User ID.
     */
    private String userId;

    public String getId() {
        return this.id;
    }

    public PlaylistCriteria setId(String id) {
        this.id = id;
        return this;
    }

    public Boolean getDefaultPlaylist() {
        return this.defaultPlaylist;
    }

    public PlaylistCriteria setDefaultPlaylist(Boolean defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
        return this;
    }

    public String getNameLike() {
        return nameLike;
    }

    public PlaylistCriteria setNameLike(String nameLike) {
        this.nameLike = nameLike;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public PlaylistCriteria setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AccessType getAccess() {
        return access;
    }

    public PlaylistCriteria setPublic(boolean flag) {
        this.access = flag ? AccessType.PUBLIC : AccessType.PRIVATE;
        return this;
    }
}
