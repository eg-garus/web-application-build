    package com.volunteer.app.controller;

    import com.volunteer.app.entity.Event;
    import com.volunteer.app.repository.EventRepository;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;

    import java.time.LocalDateTime;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest
    class EventControllerTest {

        @Autowired
        private EventController eventController;

        @Autowired
        private EventRepository eventRepository;

        @BeforeEach
        void setUp() {
            eventRepository.deleteAll();

            Event event1 = new Event();
            event1.setName("Тестовый фестиваль 2026");
            event1.setDescription("Большой зимний фестиваль с концертами");
            event1.setStartDate(LocalDateTime.of(2026, 12, 20, 12, 0));
            event1.setEndDate(LocalDateTime.of(2026, 12, 31, 23, 0));
            event1.setCity("Самара");
            event1.setType("Культурный");
            event1.setKind("Фестиваль");
            eventRepository.save(event1);

            Event event2 = new Event();
            event2.setName("Эко-субботник в парке");
            event2.setDescription("Масштабная уборка территории");
            event2.setStartDate(LocalDateTime.of(2026, 10, 15, 9, 0));
            event2.setEndDate(LocalDateTime.of(2026, 10, 15, 14, 0));
            event2.setCity("Самара");
            event2.setType("Социальный");
            event2.setKind("Экологический");
            eventRepository.save(event2);

            Event event3 = new Event();
            event3.setName("Благотворительный забег");
            event3.setDescription("Забег в поддержку детей");
            event3.setStartDate(LocalDateTime.of(2026, 11, 5, 8, 0));
            event3.setEndDate(LocalDateTime.of(2026, 11, 5, 12, 0));
            event3.setCity("Тольятти");
            event3.setType("Спортивный");
            event3.setKind("Благотворительность");
            eventRepository.save(event3);
        }

        @AfterEach
        void tearDown() {
            eventRepository.deleteAll();
        }

        @Test
        void testGetAllEvents() {
            List<Event> events = eventController.getAll();

            assertNotNull(events);
            assertEquals(3, events.size(), "Должно быть ровно 3 тестовых события");
        }

        @Test
        void testGetEventById() {
            Event savedEvent = eventRepository.findAll().get(0);
            Long id = savedEvent.getId();

            Event found = eventController.getById(id).getBody();

            assertNotNull(found);
            assertEquals(id, found.getId());
            assertEquals("Тестовый фестиваль 2026", found.getName());
            assertEquals("Самара", found.getCity());
            assertNotNull(found.getStartDate());
            assertNotNull(found.getEndDate());
        }

        @Test
        void testGetEventsByCity() {
            List<Event> samaraEvents = eventRepository.findByCityContainingIgnoreCase("Самара");

            assertEquals(2, samaraEvents.size());
            assertTrue(samaraEvents.stream().allMatch(e -> e.getCity().equalsIgnoreCase("Самара")));
        }

        @Test
        void testFutureEvents() {
            List<Event> allEvents = eventController.getAll();

            LocalDateTime now = LocalDateTime.now();
            assertTrue(allEvents.stream().allMatch(event ->
                    event.getStartDate().isAfter(now) || event.getStartDate().isEqual(now)
            ));
        }

        @Test
        void testEventFieldsNotNull() {
            List<Event> events = eventController.getAll();

            for (Event event : events) {
                assertNotNull(event.getName(), "Название события не должно быть null");
                assertNotNull(event.getCity(), "Город не должен быть null");
                assertNotNull(event.getStartDate(), "Дата начала не должна быть null");
                assertNotNull(event.getEndDate(), "Дата окончания не должна быть null");
            }
        }
    }