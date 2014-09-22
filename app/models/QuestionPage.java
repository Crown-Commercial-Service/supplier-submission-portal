package models;

import siena.*;
import siena.embed.Embedded;
import siena.embed.EmbeddedMap;

import java.util.Map;

@EmbeddedMap
public class QuestionPage extends Model {

    @Id
    public Long id;

    @Column("title")
    @NotNull
    public String title;

    @Embedded
    public Map<String,String> content;

    public static QuestionPage findByQpId(Long id) {
        return siena.Model.all(QuestionPage.class).filter("id", id).get();
    }

    @Override
    public String toString() {
        return "QuestionPage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content=" + content +
                '}';
    }
}
