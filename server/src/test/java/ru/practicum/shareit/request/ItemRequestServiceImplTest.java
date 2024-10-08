package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private UserService userService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Test
    void createItemRequest_whenResponseStatusOk() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        ItemRequest itemRequest = new ItemRequest();
        when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(itemRequest);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest itemRequestSaved = itemRequestService.createItemRequest(itemRequestDto, 1L);

        assertEquals(itemRequestSaved.getDescription(), itemRequest.getDescription());
    }

    @Test
    void createItemRequest_whenItemRequestNotCreated_thenReturnThrow() {
        User user = new User();
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        ItemRequest itemRequest = new ItemRequest();
        when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(itemRequest);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, user.getId()));
    }

    @Test
    void getAllItemRequestOfUser_thenReturnAllItem() {
        Long userId = 1L;
        when(itemRequestRepository.findAllItemRequestByRequester_IdIsNot(userId, PageRequest.of(0, 20)))
                .thenReturn(List.of(new ItemRequest()));

        List<ItemRequest> allRequestSaved = new ArrayList<>(itemRequestService.getAllItemRequestOfOtherUsers(userId, 0, 20));

        assertEquals(allRequestSaved.size(), 1);
    }

    @Test
    void getAllItemRequestOfUser_thenReturnEmptyList() {
        Long userId = 1L;
        List<ItemRequest> allItemRequestOfUser = new ArrayList<>();

        when(itemRequestRepository.findAllByRequesterId(userId, PageRequest.of(0, 20)))
                .thenReturn(allItemRequestOfUser);

        List<ItemRequest> allRequestSaved = new ArrayList<>(itemRequestService.getAllItemRequestOfUser(userId, 0, 20));

        assertEquals(allRequestSaved.size(), 0);
    }

    @Test
    void getAllItemRequestOfUser_thenReturnThrow_sizeNegative() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequestOfUser(1L, 0, -2));

        assertEquals(validationException.getMessage(), "Количество элементов для отображения должно быть положительным");
    }

    @Test
    void getAllItemRequestOfOtherUsers_thenReturnEmptyList() {
        Long userId = 1L;
        List<ItemRequest> allItemRequestOfUser = new ArrayList<>();
        when(itemRequestRepository.findAllItemRequestByRequester_IdIsNot(userId, PageRequest.of(0, 20)))
                .thenReturn(allItemRequestOfUser);

        List<ItemRequest> allRequestSaved = itemRequestService.getAllItemRequestOfOtherUsers(userId, 0, 20);

        assertEquals(allRequestSaved.size(), 0);
    }

    @Test
    void getAllItemRequestOfUser_thenReturnThrow_FromNegative() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequestOfUser(1L, -7, 20));

        assertEquals(validationException.getMessage(), "Индекс первого элемента должен быть не отрицательным");
    }

    @Test
    void getAllItemRequestOfOtherUsers_whenAllParametersIsGood_thenReturnAllItem() {
        Long userId = 1L;
        when(itemRequestRepository.findAllByRequesterId(userId, PageRequest.of(0, 20)))
                .thenReturn(List.of(new ItemRequest()));

        List<ItemRequest> allRequestSaved = new ArrayList<>(itemRequestService.getAllItemRequestOfUser(userId, 0, 20));

        assertEquals(allRequestSaved.size(), 1);
    }

    @Test
    void getAllItemRequestOfOtherUsers_thenReturnThrow_FromNegative() {
        Long userId = 1L;
        int from = -7;

        assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequestOfOtherUsers(userId, from, 20));
    }

    @Test
    void getAllItemRequestOfOtherUsers_thenReturnThrow_sizeNegative() {
        Long userId = 1L;
        int from = 1;
        int size = -2;

        assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequestOfOtherUsers(userId, from, size));
    }

    @Test
    void getItemRequest_thenReturnUser() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "ItemRequest", user, LocalDateTime.now());

        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequest itemRequestSaved = itemRequestService.getItemRequest(itemRequest.getId(), user.getId());

        assertEquals(itemRequestSaved.getRequester().getUserName(), "Name");
        assertEquals(itemRequestSaved.getDescription(), "ItemRequest");
    }

    @Test
    void getItemRequest_thenReturnThrow() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "ItemRequest", user, LocalDateTime.now());

        when(itemRequestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequest(itemRequest.getId(), user.getId()));
    }
}