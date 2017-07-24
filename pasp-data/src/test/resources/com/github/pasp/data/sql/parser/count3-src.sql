select * from
(select * from t_user

where name like '%xx%'
order
    by name  desc, id asc
    ) t
    left outer join
    (select * from t_role
        where name like '%dd%'
        order by id , name asc

    ) r
    on t.id = r.user_id

    order by

    t.name desc, r.name desc

