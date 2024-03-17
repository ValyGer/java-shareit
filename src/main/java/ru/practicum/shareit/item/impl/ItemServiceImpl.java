package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // Добавление вещи
    public Item createItem(Long userId, Item item) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Вещь успешно добавлена");
            item.setOwner(userId);
            return itemRepository.save(item);
        } else {
            log.info("Пользователь с Id = {} существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Обновление вещи
    public Item updateItem(Long userId, Long itemId, Item item) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Пользователь с Id = {} существует в базе", userId);
            try {
                Item saved = itemRepository.getReferenceById(itemId);
                if (item.getName() != null) {
                    saved.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    saved.setDescription(item.getDescription());
                }
                if (item.getIsAvailable() != null) {
                    saved.setIsAvailable(item.getIsAvailable());
                }
                log.info("Вещь с Id = {} обновлена", itemId);
                return itemRepository.save(saved);
            } catch (NotFoundException e) {
                log.info("Данной вещи с Id = {} нет у пользователя", itemId);
                throw new NotFoundException("Вещи с указанным Id не принадлежит данному пользователю");
            }
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Получение вещи пользователя
    @Transactional
    public List<Item> getAllItemsUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Пользователь с Id = {} существует в базе", userId);
            List<Item> items = itemRepository.findItemsByOwner(userId);
            log.info("Список вещей успешно получен");
            return items;
        } else {
            log.info("Пользователь с Id = {} отсутствует в баз", userId);
            throw new NotFoundException("Пользователя с указанным Id не существует");
        }
    }

    // Получение вещи по Id
    public Item getItemsById(Long userId, Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            log.info("Получена вещь с Id = {}", itemId);
            return item.get();
        } else {
            log.info("Данной вещи с Id = {} нет в базе", itemId);
            throw new NotFoundException("Вещи с указанным Id не существует");
        }
    }

    // Получение списка доступных вещей по поиску
    @Transactional
    public List<Item> searchAvailableItems(String text) {
        if (text.isBlank()) {
            log.debug("Передан пустой запрос, возвращен пустой список");
            return new ArrayList<>();
        } else {
            log.debug("Вызван метод поиска доступной вещи");
            return itemRepository.searchAvailableItems(text);
        }
    }
}
