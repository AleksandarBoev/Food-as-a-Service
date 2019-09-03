package tu.faas.domain.models.view;

import java.time.LocalDate;
import java.util.Set;

//terrible name
public class UserUsersViewModel {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
    private LocalDate bannedUntil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDate getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(LocalDate bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
