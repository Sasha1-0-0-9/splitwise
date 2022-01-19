CREATE TABLE account_group_info
(
    id SERIAL PRIMARY KEY,
    account_id INT REFERENCES accounts(id),
    group_id INT REFERENCES "groups"(id)
)