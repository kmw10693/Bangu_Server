package com.ott.ott_server.dto.movie.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    public String title;

    public String link;

    public String image;

    public String subtitle;

    public Date pubDate;

    public String director;

    public String actor;

    public float userRating;
}
