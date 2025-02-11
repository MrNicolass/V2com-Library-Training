--Insert into Books
INSERT INTO books (bookId, title, author, isbn, publicationDate, isAvailable) VALUES
('1e2f3a4b-5c6d-4e8f-9a0b-1c2d3e4f5a6b', 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', '1925-04-10', TRUE),
('2f3a4b5c-6d7e-4f9a-b1c2-d3e4f5a6b7c8', 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', '1960-07-11', TRUE),
('3a4b5c6d-7e8f-4a0b-b1c2-d3e4f5a6b7c8', '1984', 'George Orwell', '9780451524935', '1949-06-08', TRUE),
('4b5c6d7e-8f9a-4b1c-b2d3-e4f5a6b7c8d9', 'Pride and Prejudice', 'Jane Austen', '9781503290563', '1813-01-28', TRUE),
('5c6d7e8f-9a0b-4c1d-b3e4-f5a6b7c8d9e0', 'The Catcher in the Rye', 'J.D. Salinger', '9780316769488', '1951-07-16', TRUE);

--Insert into Users
INSERT INTO users (userId, name, email, password, role) VALUES
('a7a53b81-1283-4de0-b532-783fe0916c5e', 'Eduarda Muller', 'eduwarfinha@gmail.com', '$2a$12$J9CmiXUlejBdx.pettfzEOMhGncRaZymhgii2HEQtiyUwKSKobB9S', 1);
