hplsql 日期处理
==============================

hplsql -e "PRINT date(a) - 1 " -d a=2019-01-02

hplsql -e "declare x string;select to_date(add_months('2019-02-03',1)) into x;print 'xxx=>'||x;"


sh call_procedure.sh PROC_DATE_TEST 20190220                                                                                                                                                                    


set pre_date_str=replace(concat('''!',pre_date,'''!'),'!','');


CREATE FUNCTION MONTH_ADD(p_data_date CHAR(10),p_last_months INT)
  RETURNS STRING
BEGIN

    declare  v_month string;
    declare  v_months string;

    -- v_months := p_data_date;
    set v_months=replace(concat('''!',p_data_date,'''!'),'!','');
    print 'p_data_date->'||p_data_date;
    print 'v_months->'||v_months;


    for i in 1..p_last_months loop
        print 'i->'||i;
    --    print 'p_data_date->'||p_data_date;
        select to_date(add_months(p_data_date,i*-1)) into v_month;
        set v_month=replace(concat('''!',v_month,'''!'),'!','');
        v_months := v_months||','||v_month;
    end loop;

    print 'month_strings==>'||v_months;
    RETURN v_months;
END;
