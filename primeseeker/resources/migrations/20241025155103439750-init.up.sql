create table if not exists natnum (
    num integer primary key,
    is_prime boolean,
    created_at timestamp,
    processed_at timestamp
)
