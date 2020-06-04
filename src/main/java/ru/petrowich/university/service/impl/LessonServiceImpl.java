package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.LessonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LessonServiceImpl implements LessonService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonDAO lessonDAO;
    private final CourseDAO courseDAO;
    private final LecturerDAO lecturerDAO;
    private final TimeSlotDAO timeSlotDAO;
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;

    @Autowired
    public LessonServiceImpl(LessonDAO lessonDAO,
                             CourseDAO courseDAO,
                             LecturerDAO lecturerDAO,
                             TimeSlotDAO timeSlotDAO,
                             GroupDAO groupDAO,
                             StudentDAO studentDAO) {
        this.lessonDAO = lessonDAO;
        this.courseDAO = courseDAO;
        this.lecturerDAO = lecturerDAO;
        this.timeSlotDAO = timeSlotDAO;
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }

    @Override
    public Lesson getById(Long lessonId) {
        LOGGER.info("getById {}", lessonId);
        Lesson lesson = lessonDAO.getById(lessonId);

        if (lesson != null) {
            lesson.setGroups(groupDAO.getByLessonId(lessonId));
            lesson.setStudents(studentDAO.getByLessonId(lessonId));
            fillWithTimeSlot(lesson);
            fillWithLecturer(lesson);
            fillWithCourse(lesson);
        }

        return lesson;
    }

    @Override
    public void add(Lesson lesson) {
        LOGGER.info("add {}", lesson);
        lessonDAO.add(lesson);
    }

    @Override
    public void update(Lesson lesson) {
        LOGGER.info("update {}", lesson);
        lessonDAO.update(lesson);
    }

    @Override
    public void delete(Lesson lesson) {
        LOGGER.info("delete {}", lesson);
        lessonDAO.delete(lesson);
    }

    @Override
    public List<Lesson> getAll() {
        LOGGER.info("getAll");
        List<Lesson> lessons = lessonDAO.getAll().stream()
                .map(lesson -> lesson.setGroups(groupDAO.getByLessonId(lesson.getId())))
                .map(lesson -> lesson.setStudents(studentDAO.getByLessonId(lesson.getId())))
                .collect(toList());

        fillWithCourses(lessons);
        fillWithLecturers(lessons);
        fillWithTimeSlots(lessons);

        return lessons;
    }

    @Override
    public List<Lesson> getByLecturerId(Integer lecturerId) {
        LOGGER.info("getByLecturerId {}", lecturerId);
        List<Lesson> lessons = lessonDAO.getByLecturerId(lecturerId).stream()
                .map(lesson -> lesson.setGroups(groupDAO.getByLessonId(lesson.getId())))
                .map(lesson -> lesson.setStudents(studentDAO.getByLessonId(lesson.getId())))
                .collect(toList());

        fillWithCourses(lessons);
        fillWithLecturers(lessons);
        fillWithTimeSlots(lessons);

        return lessons;
    }

    @Override
    public List<Lesson> getByStudentId(Integer studentId) {
        LOGGER.info("getByStudentId {}", studentId);
        List<Lesson> lessons = lessonDAO.getByStudentId(studentId).stream()
                .map(lesson -> lesson.setGroups(groupDAO.getByLessonId(lesson.getId())))
                .map(lesson -> lesson.setStudents(studentDAO.getByLessonId(lesson.getId())))
                .collect(toList());

        fillWithCourses(lessons);
        fillWithLecturers(lessons);
        fillWithTimeSlots(lessons);

        return lessons;
    }

    private void fillWithCourse(Lesson lesson) {
        Integer courseId = lesson.getCourse().getId();

        if (courseId == null) {
            return;
        }

        Course course = courseDAO.getById(courseId);

        if (course != null) {
            lesson.setCourse(course);
        }
    }

    private void fillWithLecturer(Lesson lesson) {
        Integer lecturerId = lesson.getLecturer().getId();

        if (lecturerId == null) {
            return;
        }

        Lecturer lecturer = lecturerDAO.getById(lecturerId);

        if (lecturer != null) {
            lesson.setLecturer(lecturer);
        }
    }

    private void fillWithTimeSlot(Lesson lesson) {
        Integer timeSlotId = lesson.getTimeSlot().getId();

        if (timeSlotId == null) {
            return;
        }

        TimeSlot timeSlot = timeSlotDAO.getById(timeSlotId);

        if (timeSlot != null) {
            lesson.setTimeSlot(timeSlot);
        }
    }

    private void fillWithCourses(List<Lesson> lessons) {
        Map<Integer, Course> courseMap = new HashMap<>();

        lessons.stream().map(lesson -> lesson.getCourse().getId())
                .distinct()
                .forEach((Integer courseId) -> {
                    if (courseId != null) {
                        courseMap.put(courseId, courseDAO.getById(courseId));
                    }
                });

        lessons.forEach((Lesson lesson) -> {
            Integer courseId = lesson.getCourse().getId();

            if (courseId != null) {
                lesson.setCourse(courseMap.get(courseId));
            }
        });
    }

    private void fillWithLecturers(List<Lesson> lessons) {
        Map<Integer, Lecturer> lecturerMap = new HashMap<>();

        lessons.stream().map(lesson -> lesson.getLecturer().getId())
                .distinct()
                .forEach((Integer lecturerId) -> {
                    if (lecturerId != null) {
                        lecturerMap.put(lecturerId, lecturerDAO.getById(lecturerId));
                    }
                });

        lessons.forEach((Lesson lesson) -> {
            Integer lecturerId = lesson.getLecturer().getId();

            if (lecturerId != null) {
                lesson.setLecturer(lecturerMap.get(lecturerId));
            }
        });
    }

    private void fillWithTimeSlots(List<Lesson> lessons) {
        Map<Integer, TimeSlot> timeSlotMap = new HashMap<>();

        lessons.stream().map(lesson -> lesson.getTimeSlot().getId())
                .distinct()
                .forEach((Integer timeSlotId) -> {
                    if (timeSlotId != null)
                        timeSlotMap.put(timeSlotId, timeSlotDAO.getById(timeSlotId));
                });

        lessons.forEach((Lesson lesson) -> {
            Integer timeSlotId = lesson.getTimeSlot().getId();

            if (timeSlotId != null) {
                lesson.setTimeSlot(timeSlotMap.get(timeSlotId));
            }
        });
    }
}
