package org.eldron.shorty.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Builder
@Entity
@Table(name = "URL")
@NoArgsConstructor
@AllArgsConstructor
public class UrlEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shortenedUrl;
    private String originalUrl;

    public UrlEntity(String shortenedUrl, String originalUrl) {
        this.shortenedUrl = shortenedUrl;
        this.originalUrl = originalUrl;
    }
}
