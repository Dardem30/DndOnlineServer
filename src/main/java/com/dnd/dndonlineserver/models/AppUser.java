package com.dnd.dndonlineserver.models;

import com.dnd.dndonlineserver.models.light.UserLight;
import com.dnd.dndonlineserver.models.util.TrackedEntity;
import com.google.firebase.auth.FirebaseToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class AppUser extends TrackedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "firebase_uid")
    private String firebaseUid;
    @Column(name = "name")
    private String name;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "nickname")
    private String nickname;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private List<Tag> tags = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="contacts",
            joinColumns={@JoinColumn(name="contact_owner_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")}
    )
    private List<UserLight> contacts;

    public AppUser(final FirebaseToken firebaseToken) {
        firebaseUid = firebaseToken.getUid();
    }
}
