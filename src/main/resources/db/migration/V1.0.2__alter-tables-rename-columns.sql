-- Soltar chaves estrangeiras
ALTER TABLE loans DROP CONSTRAINT IF EXISTS loans_user_id_fkey;
ALTER TABLE loans DROP CONSTRAINT IF EXISTS loans_book_id_fkey;
ALTER TABLE reservations DROP CONSTRAINT IF EXISTS reservations_user_id_fkey;
ALTER TABLE reservations DROP CONSTRAINT IF EXISTS reservations_book_id_fkey;

--Rename columns at users
ALTER TABLE users RENAME COLUMN user_id TO userId;

--Rename columns at books
ALTER TABLE books RENAME COLUMN book_id TO bookId;
ALTER TABLE books RENAME COLUMN publication_date TO publicationDate;
ALTER TABLE books RENAME COLUMN is_available TO isAvailable;
ALTER TABLE books ALTER COLUMN isbn TYPE VARCHAR(25);

--Rename columns at loans
ALTER TABLE loans RENAME COLUMN loan_id TO loanId;
ALTER TABLE loans RENAME COLUMN user_id TO userId;
ALTER TABLE loans RENAME COLUMN book_id TO bookId;
ALTER TABLE loans RENAME COLUMN loan_date TO loanDate;
ALTER TABLE loans RENAME COLUMN loan_due_date TO loanDueDate;
ALTER TABLE loans RENAME COLUMN return_date TO returnDate;
ALTER TABLE loans RENAME COLUMN loan_status TO loanStatus;

--Rename columns at reservations
ALTER TABLE reservations RENAME COLUMN reservation_id TO reservationId;
ALTER TABLE reservations RENAME COLUMN user_id TO userId;
ALTER TABLE reservations RENAME COLUMN book_id TO bookId;
ALTER TABLE reservations RENAME COLUMN reservation_date TO reservationDate;

--Recreate foreign keys
ALTER TABLE loans ADD CONSTRAINT loans_user_id_fkey FOREIGN KEY (userId) REFERENCES users(userId);
ALTER TABLE loans ADD CONSTRAINT loans_book_id_fkey FOREIGN KEY (bookId) REFERENCES books(bookId);
ALTER TABLE reservations ADD CONSTRAINT reservations_user_id_fkey FOREIGN KEY (userId) REFERENCES users(userId);
ALTER TABLE reservations ADD CONSTRAINT reservations_book_id_fkey FOREIGN KEY (bookId) REFERENCES books(bookId);