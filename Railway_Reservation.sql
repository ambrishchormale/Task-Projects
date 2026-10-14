create database if not exists railway_reservation;

use railway_reservation;

create table trains (
    train_id int primary key auto_increment,
    train_name varchar(50),
    source varchar(50),
    destination varchar(50)
);

create table bookings (
    booking_id int primary key auto_increment,
    train_id int,
    passenger_name varchar(50),
    fare decimal(10,2),
    status varchar(20),
    foreign key (train_id) references trains(train_id)
);

insert into trains (train_name, source, destination) values
('Express1', 'Chennai', 'Madurai'),
('Express2', 'Coimbatore', 'Salem'),
('Express3', 'Madurai', 'Chennai');

insert into bookings (train_id, passenger_name, fare, status) values
(1, 'Pooja', 500, 'Confirmed'),
(1, 'Abhishek', 500, 'Waiting'),
(2, 'Priya', 300, 'Confirmed'),
(3, 'Karthik', 450, 'Cancelled'),
(2, 'Diksha', 300, 'Confirmed');

select * from trains;
select * from bookings;

-- q41: retrieve all bookings with fare greater than 400
select *
from bookings
where fare > 400;


-- q42: find bookings where status is not 'Confirmed'
select *
from bookings
where status != 'Confirmed';


-- q43: display trains starting from Chennai
select *
from trains
where source = 'Chennai';


-- q44: retrieve bookings with fare between 300 and 500
select *
from bookings
where fare between 300 and 500;


-- q45: find passengers whose names start with 'A'
select *
from bookings
where passenger_name like 'A%';


-- q46: retrieve train names along with passenger names
select trains.train_name, bookings.passenger_name
from trains
join bookings
on trains.train_id = bookings.train_id;


-- q47: count number of bookings for each train
select trains.train_name,
       count(bookings.booking_id) as total_bookings
from trains
left join bookings
on trains.train_id = bookings.train_id
group by trains.train_id, trains.train_name;


-- q48: display train names and total fare collected for each train
select trains.train_name,
       sum(bookings.fare) as total_fare
from trains
join bookings
on trains.train_id = bookings.train_id
group by trains.train_id, trains.train_name;


-- q49: find bookings with fare equal to the highest fare
select *
from bookings
where fare = (select max(fare) from bookings);


-- q50: retrieve trains that have more than one booking
select trains.train_name,
       count(bookings.booking_id) as total_bookings
from trains
join bookings
on trains.train_id = bookings.train_id
group by trains.train_id, trains.train_name
having count(bookings.booking_id) > 1;