CREATE TABLE IF NOT EXISTS productdb.Product (
    `id` varchar(50) NOT NULL PRIMARY KEY,
    `title` varchar(50),
    `description` varchar(500),
    `price` decimal(10,2)
);

