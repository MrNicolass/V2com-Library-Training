CREATE TABLE IF NOT EXISTS users (
    userId UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role SMALLINT NOT NULL DEFAULT 0 CHECK (role IN (0, 1))
);

CREATE TABLE IF NOT EXISTS books (
    bookId UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13),
    publicationDate DATE,
    isAvailable BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS loans (
    loanId UUID PRIMARY KEY,
    userId UUID NOT NULL,
    bookId UUID NOT NULL,
    loanDate DATE NOT NULL,
    loanDueDate DATE NOT NULL,
    returnDate DATE,
    loanStatus SMALLINT NOT NULL DEFAULT 0 CHECK (loan_status IN (0, 1, 2)),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE IF NOT EXISTS reservations (
    reservationId UUID PRIMARY KEY,
    userId UUID NOT NULL,
    bookId UUID NOT NULL,
    reservationDate DATE NOT NULL,
    status SMALLINT NOT NULL DEFAULT 0 CHECK (status IN (0, 1, 2)),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);