package com.sismics.music.core.dao.dbi.criteria;

import com.sismics.music.core.constant.AccessType;
/**
 * Album criteria.
 *
 * @author jtremeaux
 */
public class AlbumCriteria {
    /**
     * Album ID.
     */
    private String id;

    /**
     * Directory ID.
     */
    private String directoryId;
    
    /**
     * Album name (like).
     */
    private String nameLike;
    
    /**
     * Artist ID.
     */
    private String artistId;

    /**
     * User ID.
     */
    private String userId;

    /**
     * Access type.
     */
    private AccessType access;
    
    public String getId() {
        return this.id;
    }

    public AlbumCriteria setId(String id) {
        this.id = id;
        return this;
    }

    public String getDirectoryId() {
        return this.directoryId;
    }

    public AlbumCriteria setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
        return this;
    }

    public String getArtistId() {
        return artistId;
    }

    public AlbumCriteria setArtistId(String artistId) {
        this.artistId = artistId;
        return this;
    }

    public String getNameLike() {
        return nameLike;
    }

    public AlbumCriteria setNameLike(String nameLike) {
        this.nameLike = nameLike;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AlbumCriteria setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AccessType getAccess() {
        return access;
    }

    public AlbumCriteria setPublic(boolean flag) {
        this.access = flag ? AccessType.PUBLIC : AccessType.PRIVATE;
        return this;
    }
}
