create database 
students_assignment;

use  students_assignment;

create table students (
    student_id int primary key,
    student_name varchar(50),
    department varchar(30),
    marks int
);

insert into students values
(1, 'Arun', 'Computer Science', 85),
(2, 'Priya', 'Computer Science', 92),
(3, 'Rahul', 'Computer Science', 78),
(4, 'Sneha', 'Computer Science', 88),
(5, 'Kiran', 'Computer Science', 72),
(6, 'Meena', 'Computer Science', 95),
(7, 'Ravi', 'Electronics', 80),
(8, 'Anu', 'Electronics', 75),
(9, 'Vijay', 'Electronics', 90),
(10, 'Divya', 'Mechanical', 68),
(11, 'Manu', 'Mechanical', 82),
(12, 'Pooja', 'Mechanical', 96);

select * from students;

-- q31: find the average marks for each department
select department, avg(marks) as average_marks
from students
group by department;


-- q32: find departments whose average marks are greater than 75
select department, avg(marks) as average_marks
from students
group by department
having avg(marks) > 75;


-- q33: find the highest mark in each department
select department, max(marks) as highest_marks
from students
group by department;


-- q34: find the total number of students in each department
select department, count(student_id) as total_students
from students
group by department;


-- q35: find departments with more than 5 students
select department, count(student_id) as total_students
from students
group by department
having count(student_id) > 5;


-- q36: display departments sorted by average marks in descending order
select department, avg(marks) as average_marks
from students
group by department
order by average_marks desc;


-- q37: display the top 3 departments based on average marks
select department, avg(marks) as average_marks
from students
group by department
order by average_marks desc
limit 3;


-- q38: find departments whose average marks are between 70 and 90
select department, avg(marks) as average_marks
from students
group by department
having avg(marks) between 70 and 90;


-- q39: find the total marks for each department
select department, sum(marks) as total_marks
from students
group by department;


-- q40: display departments ordered by the total number of students
select department, count(student_id) as total_students
from students
group by department
order by total_students desc;


-- q41: find the lowest mark in each department
select department, min(marks) as lowest_marks
from students
group by department;


-- q42: find departments where the highest mark is greater than 90
select department, max(marks) as highest_marks
from students
group by department
having max(marks) > 90;


-- q43: count students scoring above 80 in each department
select department, count(*) as students_above_80
from students
where marks > 80
group by department;


-- q44: find departments where more than 3 students scored above 75
select department, count(*) as students_above_75
from students
where marks > 75
group by department
having count(*) > 3;


-- q45: display departments ordered by their highest marks
select department, max(marks) as highest_marks
from students
group by department
order by highest_marks desc;