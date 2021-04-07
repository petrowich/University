package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.petrowich.university.dto.students.StudentDTO;
import ru.petrowich.university.mapper.students.StudentMapper;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/students/")
public class StudentRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentRestController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("id") Integer studentId) {
        LOGGER.info("processing request of getting student id={}", studentId);

        if (studentId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.getById(studentId);

        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudentDTO studentDTO = studentMapper.toDto(student);

        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentDTO studentDTO) {
        LOGGER.info("processing request of creating new student");

        Student newStudent = studentMapper.toEntity(studentDTO);
        Student actualStudent = studentService.add(newStudent);
        StudentDTO actualStudentDTO= studentMapper.toDto(actualStudent);

        return new ResponseEntity<>(actualStudentDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO, @PathVariable("id") Integer studentId) {
        LOGGER.info("processing request of updating student id={}", studentId);

        if (studentId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Student persistentStudent = studentService.getById(studentId);

        if (persistentStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Student student = studentMapper.toEntity(studentDTO).setId(studentId);
        Student actualStudent = studentService.update(student);
        StudentDTO actualStudentDTO= studentMapper.toDto(actualStudent);

        return new ResponseEntity<>(actualStudentDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<StudentDTO> deleteStudent(@PathVariable("id") Integer studentId) {
        LOGGER.info("processing request of deactivating student id={}", studentId);

        if (studentId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Student persistentStudent = studentService.getById(studentId);

        if (persistentStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        studentService.delete(persistentStudent);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        LOGGER.info("processing request of listing courses");

        List<StudentDTO> studentDTOs = this.studentService.getAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());

        if (studentDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }
}
