# Spring Boot with embedded H2 database and  query DSL library for demo purpose

#### Purpose ####
This is simple spring boot application with embedded H2 database and query DSL library to demonstrate
the usage of Query DSL library for searching.

#### Features ####
* Basic CRUD operations on song and singer information stored in databse.
* Search song and singer using various search parameters

#### Available end points ####
Rest end point  	| HTTP Method   | Comments
--------------------| ------------- | -------------
`/songs`          | GET			| Retrieve the list of songs
`/songs`          | POST			| Create a new entry of song
`/songs/{id}`     | GET			| Retrieve song based on ID
`/songs/{id}`     | DELETE		| Delete a particular song
`/songs/{id}`     | PUT			| Update the song information
`/songs/search&title={}&singer={}&popularity={}&owner={}`     | GET			| Search for a song based on one or all of the parameters
`/singers/{id}`   | GET			| Retrieve singer based on ID
`/singers/{id}`   | POST			| Create a new entry of singer
`/singers/{id}`   | DELETE		| Delete a particular singer
`/singers/{id}`   | PUT			| Update the singer information
`/singers/search?name={}`   | GET			| Search the singer by name