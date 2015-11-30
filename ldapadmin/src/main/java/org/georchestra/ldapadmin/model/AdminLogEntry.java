package org.georchestra.ldapadmin.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "ldapadmin", name = "admin_log")
public class AdminLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private UUID admin;
    private UUID target;
    private AdminLogType type;

    public AdminLogEntry() {}

    public AdminLogEntry(UUID admin, UUID target, AdminLogType type) {
        this.admin = admin;
        this.target = target;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getAdmin() {
        return admin;
    }

    public void setAdmin(UUID admin) {
        this.admin = admin;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public AdminLogType getType() {
        return type;
    }

    public void setType(AdminLogType type) {
        this.type = type;
    }
}
