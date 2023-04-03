package com.sismics.music.core.dao.dbi.dto;

import com.sismics.music.core.constant.AccessType;

/**
 * Playlist DTO.
 *
 * @author jtremeaux
 */
public class PlaylistDto {
    /**
     * Playlist ID.
     */
    private String id;
    
    /**
     * Playlist name.
     */
    private String name;
    
    /**
     * Owner user ID.
     */
    private String userId;

    /**
     * Number of tracks in the playlist.
     */
    private Long playlistTrackCount;

    /**
     * Number of plays in the playlist.
     */
    private Long userTrackPlayCount;

    /**
     * Access type.
     */
    private AccessType access;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getPlaylistTrackCount() {
        return playlistTrackCount;
    }

    public void setPlaylistTrackCount(Long playlistTrackCount) {
        this.playlistTrackCount = playlistTrackCount;
    }

    public Long getUserTrackPlayCount() {
        return userTrackPlayCount;
    }

    public void setUserTrackPlayCount(Long userTrackPlayCount) {
        this.userTrackPlayCount = userTrackPlayCount;
    }

    public AccessType getAccess() {
        return access;
    }

    public void setAccess(String access) {
        switch(access) {
            case "PUBLIC":
                this.access = AccessType.PUBLIC;
                break;
            case "PRIVATE":
                this.access = AccessType.PRIVATE;
                break;
            default:
                throw new IllegalArgumentException("Invalid access: " + access);
        }
    }
}
