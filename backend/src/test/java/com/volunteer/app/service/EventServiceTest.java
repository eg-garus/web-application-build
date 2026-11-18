package com.volunteer.app.service;

import com.volunteer.app.entity.Event;
import com.volunteer.app.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    void testFindAll_ReturnsEventsOrderedByStartDate() {
        createTestEvent("Фестиваль А", LocalDateTime.now().plusDays(5));
        createTestEvent("Концерт Б", LocalDateTime.now().plusDays(2));
        createTestEvent("Субботник В", LocalDateTime.now().plusDays(10));

        List<Event> events = eventService.findAll();

        assertEquals(3, events.size());
        assertEquals("Концерт Б", events.get(0).getName()); // самый ранний
        assertEquals("Фестиваль А", events.get(1).getName());
        assertEquals("Субботник В", events.get(2).getName());
    }

    @Test
    void testFindById_Success() {
        Event saved = createTestEvent("Тестовое событие", LocalDateTime.now().plusDays(3));
        Long id = saved.getId();

        Event found = eventService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Тестовое событие", found.getName());
    }

    @Test
    void testFindByCity() {
        createTestEvent("Событие в Самаре", LocalDateTime.now().plusDays(5), "Самара");
        createTestEvent("Событие в Тольятти", LocalDateTime.now().plusDays(6), "Тольятти");
        createTestEvent("Ещё в Самаре", LocalDateTime.now().plusDays(7), "Самара");

        List<Event> samaraEvents = eventService.findByCity("Самара");

        assertEquals(2, samaraEvents.size());
        assertTrue(samaraEvents.stream().allMatch(e -> e.getCity().equalsIgnoreCase("Самара")));
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        event.setName("Новое тестовое событие");
        event.setDescription("Описание события");
        event.setStartDate(LocalDateTime.now().plusDays(10));
        event.setEndDate(LocalDateTime.now().plusDays(11));
        event.setCity("Самара");
        event.setKind("Фестиваль");
        event.setType("Культурный");

        Event created = eventService.create(event);

        assertNotNull(created.getId());
        assertEquals("Новое тестовое событие", created.getName());
        assertEquals("Самара", created.getCity());
    }

    @Test
    void testUpdateEvent() {
        Event saved = createTestEvent("Старое название", LocalDateTime.now().plusDays(5), "Самара");
        Long id = saved.getId();

        Event updateData = new Event();
        updateData.setName("Новое название");
        updateData.setDescription("Обновлённое описание");
        updateData.setCity("Тольятти");

        Event updated = eventService.update(id, updateData);

        assertEquals("Новое название", updated.getName());
        assertEquals("Обновлённое описание", updated.getDescription());
        assertEquals("Тольятти", updated.getCity());
    }

    @Test
    void testDeleteEvent() {
        Event saved = createTestEvent("На удаление", LocalDateTime.now().plusDays(3));
        Long id = saved.getId();

        eventService.delete(id);

        assertTrue(eventRepository.findById(id).isEmpty());
    }

    private Event createTestEvent(String name, LocalDateTime startDate) {
        return createTestEvent(name, startDate, "Самара");
    }

    private Event createTestEvent(String name, LocalDateTime startDate, String city) {
        Event event = new Event();
        event.setName(name);
        event.setDescription("Описание " + name);
        event.setStartDate(startDate);
        event.setEndDate(startDate.plusHours(5));
        event.setCity(city);
        event.setKind("Тестовый вид");
        event.setType("Тестовый тип");
        return eventRepository.save(event);
    }
}