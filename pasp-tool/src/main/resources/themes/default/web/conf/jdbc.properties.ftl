##用户自定义
jdbc.driver=${jdbc.driverClass}
jdbc.url = ${jdbc.url}
jdbc.username = ${jdbc.username}
jdbc.password = ${jdbc.password}

#jdbc.decrypt=true
#jdbc.password=bNVOqb7WKLX5Bjnw+LMv92taj25KOxDimXxILPQjw42wgv+1lHzOH8kr97xDwWdhpY67QuYCS7sWN4W46YbkFA==

jdbc.initialSize=1
jdbc.minIdle=1
jdbc.maxActive=300

##默认无需变化
stat.slowSqlMillis=3000
stat.logSlowSql=true
stat.connectionStackTraceEnable=true
log.statementExecutableSqlLogEnable=true

###以下值无特殊情况，可以不变化
jdbc.maxWait=60000
jdbc.timeBetweenEvictionRunsMillis=60000
jdbc.minEvictableIdleTimeMillis=300000
jdbc.validationQuery=select user()
jdbc.testWhileIdle=true
jdbc.testOnBorrow=false
jdbc.testOnReturn=false
jdbc.poolPreparedStatements=false
jdbc.maxPoolPreparedStatementPerConnectionSize=200
jdbc.removeAbandoned=true
jdbc.removeAbandonedTimeoutMillis=1800000
jdbc.resetStatEnable=false
