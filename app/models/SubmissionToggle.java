package models;

import siena.*;

public class SubmissionToggle extends Model {
    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("enabled")
    @NotNull
    public boolean enabled;

    public SubmissionToggle() {
        this.enabled = true;
    }
}
