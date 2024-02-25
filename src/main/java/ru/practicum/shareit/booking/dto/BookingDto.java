package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    public BookingDto(Item item, User booker, LocalDateTime start, LocalDateTime end, Status status) {
        this.item = item;
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
