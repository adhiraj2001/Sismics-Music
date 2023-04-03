package com.sismics.music.core.dao.dbi.dto;

import com.sismics.music.core.constant.AccessType;

import java.util.Date;

/**
 * Album DTO.
 *
 * @author jtremeaux 
 */
public class AlbumDto {
    /**
     * Album ID.
     */
    private String id;
    
    /**
     * Album name.
     */
    private String name;
    
    /**
     * Album art ID.
     */
    private String albumArt;

    /**
     * Artist ID.
     */
    private String artistId;
    
    /**
     * Artist name.
     */
    private String artistName;
    
    /**
     * Last update date.
     */
    private Date updateDate;
    
    /**
     * User play count.
     */
    private Long userPlayCount;


    /**
     * User play count.
     */
    private String userId;

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

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUserPlayCount() {
        return userPlayCount;
    }

    public void setUserPlayCount(Long userPlayCount) {
        this.userPlayCount = userPlayCount;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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