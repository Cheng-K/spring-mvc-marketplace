BEGIN; 

ALTER TABLE "Products" DROP CONSTRAINT "Product_Seller";

DROP TABLE IF EXISTS "Sellers";

ALTER TABLE "Products" ADD CONSTRAINT "Product_User" FOREIGN KEY (seller_id) REFERENCES "Users" ("id");

COMMIT;