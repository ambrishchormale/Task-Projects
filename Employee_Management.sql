create database if not exists employee_management;

use employee_management;

create table employee (
    emp_id int primary key,
    emp_name varchar(50),
    department varchar(30),
    salary decimal(10,2),
    city varchar(30),
    joining_date date
);

insert into employee values
(101, 'John', 'IT', 60000, 'Chennai', '2022-01-15'),
(102, 'David', 'HR', 45000, 'Bangalore', '2021-03-10'),
(103, 'Smith', 'IT', 70000, 'Chennai', '2020-07-12'),
(104, 'Mary', 'Finance', 55000, 'Mumbai', '2023-01-20'),
(105, 'James', 'HR', 48000, 'Delhi', '2022-05-05'),
(106, 'Linda', 'Finance', 65000, 'Mumbai', '2021-08-18');

select * from employee;

-- q1: find the total number of employees in each department
select department, count(*) as total_employees
from employee
group by department;


-- q2: find the average salary of employees in each department
select department, avg(salary) as average_salary
from employee
group by department;


-- q3: display departments having more than one employee
select department, count(*) as total_employees
from employee
group by department
having count(*) > 1;


-- q4: find the highest salary in each department
select department, max(salary) as highest_salary
from employee
group by department;


-- q5: find the lowest salary in each department
select department, min(salary) as lowest_salary
from employee
group by department;


-- q6: find departments whose average salary is greater than 50000
select department, avg(salary) as average_salary
from employee
group by department
having avg(salary) > 50000;


-- q7: calculate the total salary expenditure for each department
select department, sum(salary) as total_salary
from employee
group by department;


-- q8: display all employees sorted by salary in descending order
select *
from employee
order by salary desc;


-- q9: display employees sorted by department and salary in descending order
select *
from employee
order by department asc, salary desc;


-- q10: find cities that have more than one employee
select city, count(*) as total_employees
from employee
group by city
having count(*) > 1;


-- q11: find the total salary paid in each city
select city, sum(salary) as total_salary
from employee
group by city;


-- q12: display departments ordered by total salary from highest to lowest
select department, sum(salary) as total_salary
from employee
group by department
order by total_salary desc;


-- q13: find the number of employees in each department whose salary is greater than 50000
select department, count(*) as total_employees
from employee
where salary > 50000
group by department;


-- q14: find the difference between the highest and lowest salary in each department
select department,
       max(salary) - min(salary) as salary_difference
from employee
group by department;


-- q15: display the top 3 highest-paid employees
select *
from employee
order by salary desc
limit 3;