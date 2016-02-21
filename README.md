url design

Response is json.

- restaurant
1. GET
a. /db_rest/rest/restaurant
get all restaurant id, name and address
[{"id": 1, "name": "restaurant_1", "address": "address_1"},
 {"id": 2, "name": "restaurant_2", "address": "address_2"}
]

b. /db_rest/rest/restaurant/name/address
get restaurant by name and address

c. /db_rest/rest/restaurant/id
get restaurant by id

2. POST
/db_rest/rest/restaurant
add a restaurant
{"name": "restaurant_1", "address": "address_1"}



- review
1. GET
/db_rest/rest/review/restaurant_id
return all reviews for a restaurant
[{"id": 1, "restaurant_id": 1, "review": "review_1", "date": "2016-02-19 22:20:13"},
 {"id": 2, "restaurant_id": 2, "review": "review_2", "date": "2016-02-19 22:20:13"}
]

2. POST
/db_rest/rest/review
add a review for restaurant
{"restaurant_id": 1, "review": "review_1"}



- dish
1. GET
/db_rest/rest/dish/restaurant_id
get voting for all dishes of a restaurant
[{"id": 1, "restaurant_id": 1, "name": "dish_1", "voting": 5},
 {"id": 2, "restaurant_id": 2, "name": "dish_2", "voting": 9}
]

2. POST
/db_rest/rest/dish/restaurant_id
increase voting by 1
{"name": "dish_1"}
	