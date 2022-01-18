-- views
drop table if exists PurchasePerSP;
create table PurchasePerSP as
	select o.servicePkgId as spid, count(distinct o.id) as ordercnt
	from orders o
	where o.validityFlag = 1
	group by o.servicePkgId;

drop table if exists PurchasePerSPVP;
create table PurchasePerSPVP as
	select o.servicePkgId as spid, o.validityPeriodId as vpid, count(distinct o.id) as ordercnt
	from orders o
	where o.validityFlag = 1
	group by o.servicePkgId, o.validityPeriodId;

drop table if exists AvgProdSalesPerSP;
create table AvgProdSalesPerSP as
	select o.servicePkgId as spid, count(p.optProdId)/count(distinct o.id) as avgProdcnt 
	from orders o
	left join chosenOptProd p on o.id = p.orderId
	where o.validityFlag = 1
	group by o.servicePkgId;

drop table if exists ServicePkgSaleWithProd;
create table ServicePkgSaleWithProd as
    select o.servicePkgId as spid, sum(o.totalValue) as valSale
	from orders o
    where o.validityFlag = 1 and o.id in (select distinct(orderId) from chosenOptProd)
    group by o.servicePkgId;

drop table if exists ServicePkgSaleNoProd;
create table ServicePkgSaleNoProd as
    select o.servicePkgId as spid, sum(distinct o.totalValue) as valSale
	from orders o
	left join chosenOptProd p on o.id = p.orderId
    where o.validityFlag = 1 and p.optProdId is null
    group by o.servicePkgId;

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
	-- number of optional products associated with this order
	DECLARE prodCount INT;
    --  Average number of optional products sold together with each service package.
    DECLARE avgProdCount FLOAT;
    -- temp product id
    DECLARE tmpid INT;
    -- End flag variable （ The default is 0 ）
	DECLARE done INT DEFAULT 0;
    -- cursor for optional products associated with this order
    DECLARE prod_cur CURSOR for SELECT optProdId FROM chosenOptProd WHERE orderId=new.id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    SELECT count(optProdId) INTO prodCount FROM chosenOptProd WHERE orderId=new.id;
    select count(p.optProdId)/count(distinct o.id) into avgProdCount from orders o left join chosenOptProd p on o.id = p.orderId where o.validityFlag = 1 and o.servicePkgId = new.servicePkgId;

	IF (new.validityFlag != old.validityFlag and new.validityFlag = 1) THEN
		-- Update: Number of total purchases per package. 
        IF (exists
			(select * from PurchasePerSP where spid=new.servicePkgId)) THEN
			update PurchasePerSP
            set ordercnt=ordercnt+1
            where spid=new.servicePkgId;
		ELSE
			insert into PurchasePerSP (spid, ordercnt)
            values (new.servicePkgId, 1);
		END IF;
        -- Update: Number of total purchases per package and validity period.
        IF (exists
			(select * from PurchasePerSPVP where spid=new.servicePkgId and vpid=new.validityPeriodId)) THEN
			update PurchasePerSPVP
            set ordercnt=ordercnt+1
            where spid=new.servicePkgId and vpid=new.validityPeriodId;
		ELSE
			insert into PurchasePerSPVP (spid, vpid, ordercnt)
            values (new.servicePkgId, new.validityPeriodId, 1);
		END IF;
        -- Update: Average number of optional products sold together with each service package.
		IF (exists
			(select * from AvgProdSalesPerSP where spid=new.servicePkgId)) THEN
			update AvgProdSalesPerSP
            set avgProdcnt = avgProdCount
            where spid=new.servicePkgId;
		ELSE
			insert into AvgProdSalesPerSP (spid, avgProdcnt)
            values (new.servicePkgId, prodCount);
		END IF;

		IF (prodCount > 0) THEN
			-- update total value of sales per package with products
			IF (exists
				(select * from ServicePkgSaleWithProd where spid=new.servicePkgId)) THEN
				update ServicePkgSaleWithProd
                set valSale = valSale + new.totalValue
                where spid=new.servicePkgId;
			ELSE -- no exist
				insert into ServicePkgSaleWithProd (spid, valSale)
                values (new.servicePkgId, new.totalValue);
			END IF;
            
            -- update the product sales count
            OPEN prod_cur;
            prod_loop: LOOP
				FETCH prod_cur into tmpid;
                IF done = 1 THEN
					LEAVE prod_loop;
                END IF;
                -- update
				IF (exists
					(select * from prodSale where pid=tmpid)) THEN
					update prodSale
					set saleCnt = saleCnt + 1
					where pid = tmpid;
				ELSE
					insert into prodSale (pid, saleCnt) values (tmpid, 1);
				END IF;
			END LOOP;
            close prod_cur;
			
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
        
	END IF;
    
END; 

DROP TRIGGER IF EXISTS user_AFTER_UPDATE;
CREATE TRIGGER `checkInsolventUserAfterUpdate`
AFTER UPDATE ON `user`
FOR EACH ROW BEGIN
	DECLARE totalAmount float;
	DECLARE currentTime timestamp;
    set currentTime = current_timestamp();
	IF (new.numFailedPayments = 3 and new.numFailedPayments <> old.numFailedPayments) THEN
	set totalAmount = (SELECT sum(totalValue) FROM orders where userId = new.id and rejectedFlag = 1);

	insert into telcoservicedb.auditing (userId, username, email, amount, lastRejectionTime) values (new.id, new.username, new.email, totalAmount, currentTime);
	END IF;
END;



//

DELIMITER ;