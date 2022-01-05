DROP TRIGGER IF EXISTS after_order_valid;

-- views
drop view if exists ServicePkgPurchase;
create view ServicePkgPurchase as
    select o.servicePkgId as spid, o.validityPeriodId as vpid, count(o.id) as ordercnt, count(p.optProdId) as prodcnt 
    from orders o 
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1
    group by o.servicePkgId, o.validityPeriodId;

drop view if exists ServicePkgSaleWithProd;
create view ServicePkgSaleWithProd as
    select o.servicePkgId as spid, sum(o.totalValue) as valSale
    from orders o
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1 
    group by o.servicePkgId
    having count(p.optProdId) > 0;

drop view if exists ServicePkgSaleNoProd;
create view ServicePkgSaleNoProd as
    select o.servicePkgId as spid, sum(o.totalValue) as valSale
    from orders o
    join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1 
    group by o.servicePkgId
    having count(p.optProdId) = 0;

drop view if exists prodSale;
create view prodSale as
    select optProdId as pid, count(orderId) as saleCnt 
    from chosenOptProd p
    join orders o on o.id = p.orderId
    where o.validityFlag = 1 -- sold
    group by optProdId;
    
-- trigger
DELIMITER //
CREATE TRIGGER after_order_valid
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
	DECLARE prodCnt INT;
    SET prodCnt := (select count(optProdId) from chosenOptProd where orderId=new.id);
    
	IF (new.validityFlag <> old.validityFlag and new.validityFlag = 1) THEN
		IF (exists
			(select * from ServicePkgPurchase where spid=new.servicePkgId and vpid=new.validityPeriodId)) THEN
			update ServicePkgPurchase set ordercnt = ordercnt + 1 and prodcnt = prodcnt +prodCnt where spid=new.servicePkgId and vpid=new.validityPeriodId;
		ELSE
			insert into ServicePkgPurchase values (new.servicePkgId, new.validityPeriodId, 1, prodCnt);
		END IF;

		IF (prodCnt > 0) THEN -- update total value of sales per package with products
			IF (exists
				(select * from ServicePkgSaleWithProd where spid=new.servicePkgId)) THEN
				update ServicePkgSaleWithProd set valSale = valSale + new.totalValue where spid=new.servicePkgId;
			ELSE -- no exist
				insert into ServicePkgSaleWithProd values (new.servicePkgId, new.totalValue);
			END IF;
		ELSE -- update total value of sales per package without products
			IF (exists
				(select * from ServicePkgSaleNoProd where spid=new.servicePkgId)) THEN
				update ServicePkgSaleNoProd set valSale = valSale + new.totalValue where spid=new.servicePkgId;
			ELSE -- no exist
				insert into ServicePkgSaleNoProd values (new.servicePkgId, new.totalValue);
			END IF;
		END IF;

		IF (exists
			(select * from prodSale where pid in (select optProdId from chosenOptProd where orderId=new.id))) THEN
			update prodSale set saleCnt = saleCnt + 1 where pid in (select optProdId from chosenOptProd where orderId=new.id);
		ELSE
			insert into prodSale (pid, saleCnt) select optProdId, 1 from chosenOptProd;
		END IF;
	END IF;
    
END; //

DELIMITER ;