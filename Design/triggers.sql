-- views
drop table if exists ServicePkgPurchase;
create table ServicePkgPurchase as
    select o.servicePkgId as spid, o.validityPeriodId as vpid, count(o.id) as ordercnt, count(p.optProdId) as prodcnt 
    from orders o 
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1
    group by o.servicePkgId, o.validityPeriodId;

drop table if exists ServicePkgSaleWithProd;
create table ServicePkgSaleWithProd as
    select o.servicePkgId as spid, sum(o.totalValue) as valSale
    from orders o
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1 
    group by o.servicePkgId
    having count(p.optProdId) > 0;

drop table if exists ServicePkgSaleNoProd;
create table ServicePkgSaleNoProd as
    select o.servicePkgId as spid, sum(o.totalValue) as valSale
    from orders o
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1 
    group by o.servicePkgId
    having count(p.optProdId) = 0;

drop table if exists prodSale;
create table prodSale as
    select optProdId as pid, count(orderId) as saleCnt 
    from chosenOptProd p
    join orders o on o.id = p.orderId
    where o.validityFlag = 1 -- sold
    group by optProdId;
    
-- trigger
DROP TRIGGER IF EXISTS after_order_valid;

DELIMITER //
CREATE TRIGGER after_order_valid
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
	DECLARE prodCount INT;
    SELECT count(optProdId) INTO prodCount FROM chosenOptProd WHERE orderId=new.id;
    
	IF (new.validityFlag != old.validityFlag and new.validityFlag = 1) THEN
		IF (exists
			(select * from ServicePkgPurchase where spid=new.servicePkgId and vpid=new.validityPeriodId)) THEN
			update ServicePkgPurchase
            set ordercnt=ordercnt+1, prodcnt=prodcnt+prodCount
            where spid=new.servicePkgId and vpid=new.validityPeriodId;
		ELSE
			insert into ServicePkgPurchase (spid, vpid, ordercnt, prodcnt)
            values (new.servicePkgId, new.validityPeriodId, 1, prodCount);
		END IF;

		IF (prodCount > 0) THEN -- update total value of sales per package with products
			IF (exists
				(select * from ServicePkgSaleWithProd where spid=new.servicePkgId)) THEN
				update ServicePkgSaleWithProd
                set valSale = valSale + new.totalValue
                where spid=new.servicePkgId;
			ELSE -- no exist
				insert into ServicePkgSaleWithProd (spid, valSale)
                values (new.servicePkgId, new.totalValue);
			END IF;
		ELSE -- update total value of sales per package without products
			IF (exists
				(select * from ServicePkgSaleNoProd where spid=new.servicePkgId)) THEN
				update ServicePkgSaleNoProd
                set valSale = valSale + new.totalValue
                where spid=new.servicePkgId;
			ELSE -- no exist
				insert into ServicePkgSaleNoProd (spid, valSale)
                values (new.servicePkgId, new.totalValue);
			END IF;
		END IF;

		IF (exists
			(select * from prodSale where pid in (select optProdId from chosenOptProd where orderId=new.id))) THEN
			update prodSale
            set saleCnt = saleCnt + 1
            where pid in (select optProdId from chosenOptProd where orderId=new.id);
		ELSE
			insert into prodSale (pid, saleCnt)
            select optProdId, 1 from chosenOptProd where orderId=new.id;
		END IF;
	END IF;
    
END; //

DELIMITER ;