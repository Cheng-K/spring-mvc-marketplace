CREATE TABLE "Cart" AS
(SELECT id FROM "Users");

ALTER TABLE "Cart"
ADD PRIMARY KEY (id);

ALTER TABLE "Users" 
ADD COLUMN "cart_id" INTEGER NOT NULL DEFAULT 1;

UPDATE "Users"
SET cart_id=id;

ALTER TABLE "Users"
ADD CONSTRAINT "Users_Cart" FOREIGN KEY ("cart_id") REFERENCES "Cart" ("id");

CREATE TABLE "Cart_Products" (
    "cart_id" INTEGER,
    "product_id" INTEGER,
    PRIMARY KEY ("cart_id", "product_id")
);

ALTER TABLE "Cart_Products"
ADD CONSTRAINT "Cart_Cart_Products" FOREIGN KEY ("cart_id") REFERENCES "Cart" ("id");

ALTER TABLE "Cart_Products"
ADD CONSTRAINT "Products_Cart_Products" FOREIGN KEY ("product_id") REFERENCES "Products" ("id");

