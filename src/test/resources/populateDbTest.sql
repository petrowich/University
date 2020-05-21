ALTER SEQUENCE public.seq_persons RESTART WITH 10001;
ALTER SEQUENCE public.seq_courses RESTART WITH 10;
ALTER SEQUENCE public.seq_groups RESTART WITH 101;
ALTER SEQUENCE public.seq_lessons RESTART WITH 1000001;
ALTER SEQUENCE public.seq_timeslots RESTART WITH 1;

TRUNCATE TABLE public.t_groups_students;
TRUNCATE TABLE public.t_groups_courses;
TRUNCATE TABLE public.t_lessons;
DELETE FROM public.t_courses;
DELETE FROM public.t_persons;
DELETE FROM public.t_groups;
DELETE FROM public.t_timeslots;

INSERT INTO public.t_timeslots (timeslot_name, timeslot_start_time, timeslot_end_time) VALUES
  ('first lesson','08:00:00','09:30:00')
, ('second lesson','09:40:00','11:10:00')
, ('third lesson','11:20:00','12:50:00')
, ('fourth lesson','13:20:00','14:50:00')
, ('fifth lesson','15:00:00','16:30:00')
, ('sixth lesson','16:40:00','18:10:00')
, ('seventh lesson','18:20:00','19:50:00')
, ('eighth lesson','20:00:00','21:30:00');

INSERT INTO public.t_groups (group_id, group_name, group_capacity, group_active) VALUES
  (501, 'AA-01', 3, TRUE)
, (502, 'BB-02', 1, TRUE)
, (503, 'CC-03', 0, FALSE);

insert into public.t_persons (person_id, person_first_name, person_last_name, person_role_id, person_email, person_comment, person_active) VALUES
  (50001,'Рулон','Обоев',1, 'rulon.oboev@university.edu', 'stupid', TRUE)
, (50002,'Обвал','Забоев',1, 'obval.zaboev@university.edu', NULL, TRUE)
, (50003, 'Рекорд','Надоев',1, 'record.nadoev@university.edu', '', TRUE)
, (50004,'Подрыв','Устоев',1, 'podryv.ustoev@university.edu', 'expelled', FALSE)
, (50005,'Отряд','Ковбоев',2, 'otryad.kovboev@university.edu', '', TRUE)
, (50006, 'Ушат','Помоев',2, 'ushat.pomoev@university.edu', 'died', FALSE);

INSERT INTO public.t_courses (course_id, course_name, course_description, course_author_id, course_active) VALUES
  (51, 'math', 'exact', 50005, TRUE)
, (52, 'biology', 'natural', 50005, TRUE)
, (53, 'physics', 'exact', 50006, TRUE)
, (54, 'literature', 'humanities', NULL, TRUE)
, (55, 'psychology', 'humanities', 50006, FALSE)
, (56, 'litrball', 'sport', 50003, TRUE);

INSERT INTO public.t_groups_courses (group_id, course_id) VALUES
  (501, 51)
, (501, 52)
, (501, 54)
, (502, 53)
, (502, 54)
, (502, 55)
, (503, 56);

INSERT INTO public.t_groups_students (group_id, student_id) VALUES
  (501, 50001)
, (501, 50002)
, (502, 50003);

INSERT INTO public.t_lessons (lesson_id, course_id, lecturer_id, timeslot_id, lesson_date, lesson_start_time, lesson_end_time) VALUES
  (5000001, 51, 50005, 1, '2020-06-01', '08:00:00', '09:30:00')
, (5000002, 52, 50005, 2, '2020-06-01', '09:40:00', '11:10:00')
, (5000003, 53, 50005, 3, '2020-06-01', '11:20:00', '12:50:00')
, (5000004, 56, 50005, NULL, '2020-07-01', '08:00:00', '21:30:00')
, (5000005, 55, 50006, 2, '2020-06-01', '09:40:00', '11:10:00');