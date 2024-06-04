package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId; // идентификатор в базе
    private String name; // наименование
    private String description; // описание
    @Column(name = "owner_id")
    private long owner; // номер пользователя которому принадлежит вещь
    @Column(name = "status")
    private Boolean isAvailable; // статус вещи: доступна не доступна
    @Column(name = "request_id")
    private Long request; // если вещь создана по запросу, то тут будет ссылка на запрос
    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>(); // список комментариев
    @OneToMany(mappedBy = "item")
    private List<Booking> bookings; // список бронирований данной вещи

    public Item(String name, String description, Boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public Item(long itemId, String name, String description, long owner, Boolean isAvailable) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
    }

    public Item(String name, String description, long owner, boolean isAvailable, Long request) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
        this.request = request;
    }

    public Item(long itemId, String name, String description, long owner, Boolean isAvailable, Long request) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
        this.request = request;
        this.comments = comments;
        this.bookings = bookings;
    }
}
