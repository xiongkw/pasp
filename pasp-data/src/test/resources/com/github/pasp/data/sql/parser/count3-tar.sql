select count(1) from (select * from
(select * from t_user

where name like '%xx%'

    ) t
    left outer join
    (select * from t_role
        where name like '%dd%'
        

    ) r
    on t.id = r.user_id

    ) count_temp