COPY product (id, article, name, description, category, price, count, creation_date)
    FROM '/Users/mihailkondrasin/Documents/Работа/Mediasoft/GoodsWarehouse/row_generation/products.csv'
    DELIMITER ','
    CSV HEADER;