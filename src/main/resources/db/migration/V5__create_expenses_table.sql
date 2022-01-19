CREATE TABLE expenses
(
    id SERIAL PRIMARY KEY,
    lender_id INT,
    borrower_id INT,
    amount DOUBLE PRECISION,
    time TIMESTAMP,
    currency VARCHAR,
    expense_type VARCHAR
);