# Modern webszabványok

Local Spring Boot API for study decks, flashcards, and notes.

## Run

```bash
mvn spring-boot:run
```

The database is a file at `data/webszabvanyok`. Swagger UI is at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

Two users are created on startup. Each only sees their own decks and notes.

| Username | Password |
| --- | --- |
| anna | anna |
| bela | bela |

Use HTTP Basic. In Swagger, click Authorize and enter one of those pairs.

## Endpoints

- `GET/POST /api/flashcards/decks`, `PUT/DELETE /api/flashcards/decks/{id}`
- `GET/POST /api/flashcards/decks/{id}/cards`
- `PUT/DELETE /api/flashcards/cards/{id}`
- `GET /api/flashcards/due`, `POST /api/flashcards/cards/{id}/review`
- `GET/POST /api/notes`, `PUT/DELETE /api/notes/{id}`

A review rating is `good` or `again`.
