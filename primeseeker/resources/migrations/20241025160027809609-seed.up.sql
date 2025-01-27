insert or ignore into natnum (num, is_prime, created_at, processed_at)
values
    (2, true, datetime('now'), datetime('now')),
    (3, true, datetime('now'), datetime('now')),
    (5, null, datetime('now'), null)
