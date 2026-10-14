create database customers_orders;

use customers_orders;

create table customers (
    customer_id int primary key,
    customer_name varchar(50),
    city varchar(30)
);

create table orders (
    order_id int primary key,
    customer_id int,
    amount decimal(10,2),
    order_date date,
    foreign key (customer_id)
        references customers(customer_id)
);

insert into customers values
(1, 'Arun', 'Chennai'),
(2, 'Priya', 'Bangalore'),
(3, 'Rahul', 'Mumbai'),
(4, 'Sneha', 'Delhi'),
(5, 'Kiran', 'Hyderabad');

insert into orders values
(101, 1, 3000, '2026-01-05'),
(102, 1, 2500, '2026-01-10'),
(103, 1, 4000, '2026-02-01'),
(104, 1, 1500, '2026-02-15'),
(105, 2, 5000, '2026-01-12'),
(106, 2, 2500, '2026-02-10'),
(107, 2, 1800, '2026-03-01'),
(108, 3, 7000, '2026-01-20'),
(109, 3, 3500, '2026-02-18'),
(110, 4, 1200, '2026-03-05');

select * from customers;
select * from orders;


-- q16: find the total order amount for each customer
select customers.customer_name,
       sum(orders.amount) as total_amount
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q17: find customers who have placed more than 3 orders
select customers.customer_name,
       count(orders.order_id) as total_orders
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having count(orders.order_id) > 3;


-- q18: find the average order amount for each customer
select customers.customer_name,
       avg(orders.amount) as average_amount
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q19: find the highest order amount placed by each customer
select customers.customer_name,
       max(orders.amount) as highest_order
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q20: display customers sorted by total purchase amount
select customers.customer_name,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
order by total_purchase desc;


-- q21: find customers whose total purchase exceeds 10000
select customers.customer_name,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having sum(orders.amount) > 10000;


-- q22: display customer names and total number of orders
select customers.customer_name,
       count(orders.order_id) as total_orders
from customers
left join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q23: find the customer who spent the highest total amount
select customers.customer_name,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having sum(orders.amount) = (
    select max(total_amount)
    from (
        select sum(amount) as total_amount
        from orders
        group by customer_id
    ) as customer_totals
);


-- q24: find the customer who placed the maximum number of orders
select customers.customer_name,
       count(orders.order_id) as total_orders
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having count(orders.order_id) = (
    select max(order_count)
    from (
        select count(*) as order_count
        from orders
        group by customer_id
    ) as order_totals
);


-- q25: find customers whose average order amount is greater than 2000
select customers.customer_name,
       avg(orders.amount) as average_amount
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having avg(orders.amount) > 2000;


-- q26: display the top 5 customers based on total purchase amount
select customers.customer_name,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
order by total_purchase desc
limit 5;


-- q27: find the minimum order amount for each customer
select customers.customer_name,
       min(orders.amount) as minimum_order
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q28: find customers whose total purchase exceeds 5000
select customers.customer_name,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having sum(orders.amount) > 5000;


-- q29: display customer-wise total orders and total purchase amount
select customers.customer_name,
       count(orders.order_id) as total_orders,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name;


-- q30: find customers who placed more than 2 orders and spent more than 8000
select customers.customer_name,
       count(orders.order_id) as total_orders,
       sum(orders.amount) as total_purchase
from customers
join orders
on customers.customer_id = orders.customer_id
group by customers.customer_id, customers.customer_name
having count(orders.order_id) > 2
   and sum(orders.amount) > 8000;