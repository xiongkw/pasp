select count(1) from (select t.name, t.id, r.name role_name
from t_user t left join t_role r
on t.id = r.user_id
    

) count_temp