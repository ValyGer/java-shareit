CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(2048) NOT NULL,
  owner_id BIGINT NOT NULL,
  status BOOLEAN,
  request_id BIGINT,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT FK_ITEM_ON_OWNER FOREIGN KEY(owner_id) REFERENCES users(id),
  CONSTRAINT FK_ITEM_ON_REQUEST FOREIGN KEY(request_id) REFERENCES item_requests(id),
  CONSTRAINT UQ_OWNER_ITEM_NAME UNIQUE(owner_id, name)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(2048) NOT NULL,
    requester_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_item_request PRIMARY KEY(id),
    CONSTRAINT FK_ITEM_REQUEST_ON_REQUESTER FOREIGN KEY(requester_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id BIGINT,
    booker_id BIGINT,
    start_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status varchar(50),
    CONSTRAINT pk_booking PRIMARY KEY(id),
    CONSTRAINT FK_BOOKING_ON_BOOKER FOREIGN KEY(booker_id) REFERENCES users(id),
    CONSTRAINT FK_BOOKING_ON_ITEM FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(2048) NOT NULL,
    item_id BIGINT,
    author_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY(id),
    CONSTRAINT FK_COMMENT_ON_AUTHOR FOREIGN KEY(author_id) REFERENCES users(id),
    CONSTRAINT FK_COMMENT_ON_ITEM FOREIGN KEY(item_id) REFERENCES items(id)
);




