package com.dnd.dndonlineserver.models;

import com.dnd.dndonlineserver.models.light.UserLight;
import com.dnd.dndonlineserver.models.util.TrackedEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room extends TrackedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "min_people")
    private int minPeople;
    @Column(name = "max_people")
    private int maxPeople;
    @Column(name = "closed")
    private boolean closed;
    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="room_participant",
            joinColumns={@JoinColumn(name="room_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")}
    )
    private Set<UserLight> participants;
    @Column(name = "eternal")
    @JsonIgnore
    private boolean eternal;
}
