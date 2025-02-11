CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role SMALLINT NOT NULL DEFAULT 0 CHECK (role IN (0, 1))
);

CREATE TABLE IF NOT EXISTS books (
    book_id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13),
    publication_date DATE,
    is_available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS loans (
    loan_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    loan_date DATE NOT NULL,
    loan_due_date DATE NOT NULL,
    return_date DATE,
    loan_status SMALLINT NOT NULL DEFAULT 0 CHECK (loan_status IN (0, 1, 2)),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE IF NOT EXISTS reservations (
    reservation_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    reservation_date DATE NOT NULL,
    status SMALLINT NOT NULL DEFAULT 0 CHECK (status IN (0, 1, 2)),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);