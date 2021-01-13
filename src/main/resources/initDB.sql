DROP VIEW IF EXISTS public.v_lessons_students;
DROP VIEW IF EXISTS public.v_lessons_groups;
DROP VIEW IF EXISTS public.v_courses_students;
DROP TABLE IF EXISTS public.t_lessons;
DROP TABLE IF EXISTS public.t_groups_students;
DROP TABLE IF EXISTS public.t_groups_courses;
DROP TABLE IF EXISTS public.t_groups;
DROP TABLE IF EXISTS public.t_courses;
DROP TABLE IF EXISTS public.t_persons;
DROP TABLE IF EXISTS public.t_roles;
DROP TABLE IF EXISTS public.t_timeslots;

DROP SEQUENCE IF EXISTS public.seq_roles;
DROP SEQUENCE IF EXISTS public.seq_timeslots;
DROP SEQUENCE IF EXISTS public.seq_persons;
DROP SEQUENCE IF EXISTS public.seq_courses;
DROP SEQUENCE IF EXISTS public.seq_groups;
DROP SEQUENCE IF EXISTS public.seq_lessons;

CREATE SEQUENCE public.seq_roles START WITH 1;
CREATE SEQUENCE public.seq_timeslots START WITH 1;
CREATE SEQUENCE public.seq_persons START WITH 10001;
CREATE SEQUENCE public.seq_courses START WITH 10;
CREATE SEQUENCE public.seq_groups START WITH 101;
CREATE SEQUENCE public.seq_lessons START WITH 1000001;

CREATE TABLE public.t_roles
(
  role_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_roles'),
  role_name   VARCHAR(15) NOT NULL
);
CREATE INDEX ix_role_name ON public.t_roles (role_name);

INSERT INTO public.t_roles (role_name) VALUES ('STUDENT'), ('LECTURER');

CREATE TABLE public.t_timeslots
(
  timeslot_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_timeslots'),
  timeslot_name   VARCHAR(255) NOT NULL,
  timeslot_start_time     TIME NOT NULL,
  timeslot_end_time     TIME NOT NULL
);

INSERT INTO public.t_timeslots (timeslot_name, timeslot_start_time, timeslot_end_time) VALUES
 ('first lesson','08:00:00','09:30:00')
,('second lesson','09:40:00','11:10:00')
,('third lesson','11:20:00','12:50:00')
,('fourth lesson','13:20:00','14:50:00')
,('fifth lesson','15:00:00','16:30:00')
,('sixth lesson','16:40:00','18:10:00')
,('seventh lesson','18:20:00','19:50:00')
,('eighth lesson','20:00:00','21:30:00');

CREATE TABLE public.t_persons
(
  person_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_persons'),
  person_first_name   VARCHAR(255) NULL,
  person_last_name   VARCHAR(255) NULL,
  person_role_id     INTEGER NOT NULL,
  person_email   VARCHAR(255) NOT NULL,
  person_comment   VARCHAR(2048) NULL,
  person_active     BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (person_role_id) REFERENCES public.t_roles (role_id)
);
CREATE INDEX ix_person_role_id ON public.t_persons (person_role_id);
CREATE INDEX ix_person_email ON public.t_persons (person_email);

CREATE TABLE public.t_courses
(
  course_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_courses'),
  course_name   VARCHAR(255) NULL,
  course_description VARCHAR(2048) NULL,
  course_author_id     INTEGER NULL,
  course_active     BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (course_author_id) REFERENCES public.t_persons (person_id)
);

CREATE TABLE public.t_groups
(
  group_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_groups'),
  group_name   VARCHAR(255) NULL,
  group_capacity INTEGER NOT NULL DEFAULT 0,
  group_active     BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE public.t_groups_courses
(
  group_id     INTEGER NOT NULL,
  course_id     INTEGER NOT NULL,
  FOREIGN KEY (group_id) REFERENCES public.t_groups (group_id),
  FOREIGN KEY (course_id) REFERENCES public.t_courses (course_id),
  UNIQUE (group_id, course_id)
);
CREATE INDEX ix_groups_courses_group_id ON public.t_groups_courses (group_id);
CREATE INDEX ix_groups_courses_course_id ON public.t_groups_courses (course_id);

CREATE TABLE public.t_groups_students
(
  group_id     INTEGER NOT NULL,
  student_id     INTEGER NOT NULL,
  FOREIGN KEY (group_id) REFERENCES public.t_groups (group_id),
  FOREIGN KEY (student_id) REFERENCES public.t_persons (person_id),
  UNIQUE (student_id)
);
CREATE INDEX ix_groups_students_group_id ON public.t_groups_students (group_id);
CREATE INDEX ix_groups_students_student_id ON public.t_groups_students (student_id);

CREATE TABLE public.t_lessons
(
  lesson_id     INTEGER PRIMARY KEY DEFAULT nextval('public.seq_lessons'),
  course_id     INTEGER NOT NULL,
  lecturer_id     INTEGER NOT NULL,
  timeslot_id     INTEGER NULL,
  lesson_date   DATE NOT NULL,
  lesson_start_time     TIME NULL,
  lesson_end_time     TIME NULL,
  FOREIGN KEY (course_id) REFERENCES public.t_courses (course_id),
  FOREIGN KEY (lecturer_id) REFERENCES public.t_persons (person_id),
  FOREIGN KEY (timeslot_id) REFERENCES public.t_timeslots (timeslot_id)
);
