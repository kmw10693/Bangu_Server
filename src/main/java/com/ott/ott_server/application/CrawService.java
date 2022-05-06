package com.ott.ott_server.application;

import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.MovieOtt;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.dto.movie.MovieOttResponseData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.errors.OttNameNotFoundException;
import com.ott.ott_server.infra.MovieOttRepository;
import com.ott.ott_server.infra.MovieRepository;
import com.ott.ott_server.infra.OttRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawService {
    private static String URL = "https://tvnng.com/find?keyword=";
    private static String MOVIE_URL = "https://tvnng.com";

    private final OttRepository ottRepository;

    @Transactional
    public Page<MovieResponseData> getSearch(String title, Pageable pageable) throws IOException {

        List<MovieResponseData> movieResponseData = new ArrayList<>();

        Document doc = Jsoup.connect(URL + title).get();

        Elements ele = doc.select("div#top_items");
        int size = ele.select("div.item").size();
        for (int i = 0; i < size; i++) {
            String info;
            String director = null;
            String actor = null;
            String movieTitle = ele.select("div.item").get(i).select("a").get(0).select("div.item_description").get(0).select("h2.title").text();
            String image = ele.select("div.item").get(i).select("a").get(0).select("div.item_thumb").attr("style");
            String ott = ele.select("div.item").get(i).select("a").get(0).select("div.item_description").get(0).select("div.service").text();
            String movieUrl = ele.select("div.item").get(i).select("a").attr("href");
            if (!image.isEmpty()) {
                image = image.substring(image.indexOf("\'") + 1);
                image = image.substring(0, image.indexOf("\'"));
            }
            Document url = connect(MOVIE_URL + movieUrl);
            Elements element = url.select("p.info-text");

            String genre = null;
            if (!element.isEmpty()) {
                genre = element.first().text();
            }

            Ott movieOtt = ottRepository.findByName(ott).orElseThrow(OttNameNotFoundException::new);
            MovieOttResponseData response = MovieOttResponseData.builder().ottId(movieOtt.getId()).ottName(movieOtt.getName()).build();

            boolean duplicate = false;
            for (MovieResponseData m : movieResponseData) {
                if (m.getTitle().equals(movieTitle)) {
                    duplicate = true;
                    boolean check = false;
                    for (MovieOttResponseData movieOttResponseData : m.getMovieOtts()) {
                        if (movieOttResponseData.getOttName().equals(response.getOttName())) {
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        // 만약 이전의 감독 이나 배우가 NULL 값이라면
                        if (m.getDirector() == null || m.getActor() == null) {
                            info = url.select("h2.contents-btn").select("a").attr("href");
                            if (response.getOttName().equals("WATCHAPLAY")) {
                                Document document = connect(info);
                                if (document != null) {
                                    director = document.select("ul").get(1).select("li").get(0).text();
                                    actor = document.select("ul").get(1).select("li").get(1).text();
                                }
                            } else if (response.getOttName().equals("TVING")) {
                                Document document = connect(info);
                                if (document != null) {
                                    if (document.select("dd").size() >= 2) {
                                        director = document.select("dd").get(0).text();
                                        actor = document.select("dd").get(1).text();
                                    }
                                }
                            }
                            if (director != null) m.setDirector(director);
                            if (actor != null) m.setActor(actor);
                        }
                        m.addMovieOtts(response);
                        break;
                    }
                }
            }
            if (duplicate == false) {
                info = url.select("h2.contents-btn").select("a").attr("href");
                if (response.getOttName().equals("WATCHAPLAY")) {
                    Document document = connect(info);
                    if (document != null) {
                        director = document.select("ul").get(1).select("li").get(0).text();
                        actor = document.select("ul").get(1).select("li").get(1).text();
                    }
                } else if (response.getOttName().equals("TVING")) {
                    Document document = connect(info);
                    if (document != null) {
                        if (document.select("dd").size() >= 2) {
                            director = document.select("dd").get(0).text();
                            actor = document.select("dd").get(1).text();
                        }
                    }
                }
                List<MovieOttResponseData> m = new ArrayList<>();
                m.add(response);
                MovieResponseData movie = MovieResponseData.builder()
                        .title(movieTitle)
                        .movieOtts(m)
                        .actor(actor)
                        .director(director)
                        .imageUrl(image)
                        .genre(genre)
                        .build();
                movieResponseData.add(movie);
            }
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), movieResponseData.size());
        return new PageImpl<>(movieResponseData.subList(start, end), pageable, movieResponseData.size());
    }

    private static Document connect(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Chrome")
                    .get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (HttpStatusException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return doc;
    }
}

