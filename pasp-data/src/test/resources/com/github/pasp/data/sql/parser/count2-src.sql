select t.name, t.id, r.name role_name
from t_user t left join t_role r
on t.id = r.user_id
    order
    BY      t.name
 DESc,      a.name    desc

;