rm -fr $CATALINA_HOME/webapps/db_*
cp build/libs/db_rest.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
