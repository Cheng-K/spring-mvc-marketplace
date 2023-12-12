ALTER TABLE "Products" ALTER COLUMN condition TYPE varchar(50);

UPDATE "Products"
SET condition = CASE WHEN condition = 'like_new' THEN 'LIKE_NEW'
                ELSE 'USED'
                END;

ALTER TABLE "Products" ADD CONSTRAINT valid_condition CHECK (condition IN ('LIKE_NEW', 'USED'));

DROP TYPE Condition;