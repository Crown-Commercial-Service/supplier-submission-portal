package models;

import siena.*;

import java.util.List;

import static siena.Json.*;


@Table("listing")
public class Listing extends Model {
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("title")
    @Max(200)
    @NotNull
    public String title;

    @Column("description")
    @Max(200)
    @NotNull
    public String description;

    public Listing(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static List<Listing> all() {
        return Model.all(Listing.class).fetch();
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
