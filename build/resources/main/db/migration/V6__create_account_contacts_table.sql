CREATE TABLE account_contacts
(
    account_id INT,
    phone_number CHARACTER VARYING(12) REFERENCES contacts(phone_number)
)