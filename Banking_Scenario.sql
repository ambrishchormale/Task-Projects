create database if not exists banking_scenario;

use banking_scenario;

create table customers (
    customer_id int primary key auto_increment,
    name varchar(50) not null,
    city varchar(50)
);

create table accounts (
    account_id int primary key auto_increment,
    customer_id int,
    account_type varchar(20),
    balance decimal(10,2),
    foreign key (customer_id)
        references customers(customer_id)
);

insert into customers (name, city) values
('janani', 'chennai'),
('arun', 'coimbatore'),
('priya', 'madurai'),
('karthik', 'salem');

insert into accounts
(customer_id, account_type, balance) values
(1, 'savings', 50000),
(1, 'current', 20000),
(2, 'savings', 30000),
(3, 'savings', 15000),
(4, 'current', 40000);

select * from customers;

select * from accounts;

# Q1: display accounts with balance greater than 20000
select * from accounts
where balance > 20000;

# Q2: display customers who live in chennai
select * from customers
where city = 'chennai';

# Q3: display accounts with balance between 20000 and 50000
select * from accounts
where balance between 20000 and 50000;

# Q4: display customers in alphabetical order
select * from customers
order by name asc;

# Q5: display accounts from highest to lowest balance
select * from accounts
order by balance desc;

# Q6: display the total number of customers
select count(*) as total_customers
from customers;

# Q7: display the total balance of all accounts
select sum(balance) as total_balance
from accounts;

# Q8: display the average account balance
select avg(balance) as average_balance
from accounts;

# Q9: display the highest account balance
select max(balance) as highest_balance
from accounts;

# Q10: display the lowest account balance
select min(balance) as lowest_balance
from accounts;

# Q11: display account types and their total balances
select account_type, sum(balance) as total_balance
from accounts
group by account_type;

# Q12: display account types having total balance greater than 50000
select account_type, sum(balance) as total_balance
from accounts
group by account_type
having sum(balance) > 50000;

# Q13: display customer names with their account details
select customers.name, accounts.account_type, accounts.balance
from customers
inner join accounts
on customers.customer_id = accounts.customer_id;

# Q14: display customers who have a savings account
select distinct customers.name
from customers
inner join accounts
on customers.customer_id = accounts.customer_id
where accounts.account_type = 'savings';

# Q15: display accounts belonging to customer ids 1 and 3
select * from accounts
where customer_id in (1, 3);

# Q16: display accounts with balance less than 30000
select * from accounts
where balance < 30000;

# Q17: display accounts with balance greater than or equal to 30000
select * from accounts
where balance >= 30000;

# Q18: display customers whose city starts with 'c'
select * from customers
where city like 'c%';

# Q19: display accounts with balance between 15000 and 40000
select * from accounts
where balance between 15000 and 40000;

# Q20: display each customer and the total number of accounts
select customers.name, count(accounts.account_id) as total_accounts
from customers
left join accounts
on customers.customer_id = accounts.customer_id
group by customers.customer_id, customers.name;

# Q21: find the minimum account balance
select min(balance) as minimum_balance
from accounts;

# Q22: count the total number of customers
select count(*) as total_customers
from customers;

# Q23: find total balance grouped by account type
select account_type, sum(balance) as total_balance
from accounts
group by account_type;

# Q24: find average balance for each account type
select account_type, avg(balance) as average_balance
from accounts
group by account_type;

# Q25: display account types having average balance greater than 20000
select account_type, avg(balance) as average_balance
from accounts
group by account_type
having avg(balance) > 20000;

# Q26: count the number of accounts for each customer
select customer_id, count(*) as total_accounts
from accounts
group by customer_id;

# Q27: display customers having more than one account
select customers.name, count(accounts.account_id) as total_accounts
from customers
join accounts
on customers.customer_id = accounts.customer_id
group by customers.customer_id, customers.name
having count(accounts.account_id) > 1;

# Q28: retrieve customer names along with their account balances
select customers.name, accounts.balance
from customers
join accounts
on customers.customer_id = accounts.customer_id;

# Q29: display all customers and their accounts, including customers without accounts
select customers.name, accounts.account_type, accounts.balance
from customers
left join accounts
on customers.customer_id = accounts.customer_id;

# Q30: display all accounts and corresponding customer details
select accounts.account_id, customers.name, customers.city,
       accounts.account_type, accounts.balance
from accounts
join customers
on accounts.customer_id = customers.customer_id;

# Q31: retrieve customer names and account types where balance is greater than 20000
select customers.name, accounts.account_type
from customers
join accounts
on customers.customer_id = accounts.customer_id
where accounts.balance > 20000;

# Q32: list customers with their total balance using join
select customers.name, sum(accounts.balance) as total_balance
from customers
join accounts
on customers.customer_id = accounts.customer_id
group by customers.customer_id, customers.name;

# Q33: display customer names and balances sorted by balance
select customers.name, accounts.balance
from customers
join accounts
on customers.customer_id = accounts.customer_id
order by accounts.balance;

# Q34: count number of accounts for each city using join
select customers.city, count(accounts.account_id) as total_accounts
from customers
join accounts
on customers.customer_id = accounts.customer_id
group by customers.city;

# Q35: find accounts with balance greater than average balance
select * from accounts
where balance > (
    select avg(balance)
    from accounts
);

# Q36: retrieve customers who have accounts
select * from customers
where customer_id in (
    select customer_id
    from accounts
);

# Q37: find customers who do not have any accounts
select *
from customers
where customer_id not in (
    select customer_id
    from accounts
    where customer_id is not null
);

# Q38: display account or accounts with maximum balance
select *
from accounts
where balance = (
    select max(balance)
    from accounts
);

# Q39: find customers whose total balance is greater than 40000
select customers.name, sum(accounts.balance) as total_balance
from customers
join accounts
on customers.customer_id = accounts.customer_id
group by customers.customer_id, customers.name
having sum(accounts.balance) > 40000;

# Q40: display customers whose names end with i
select *
from customers
where name like '%i';