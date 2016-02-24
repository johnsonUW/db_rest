#
# build war file
#
./gradlew clean war

#
# deploy to local tomcat
#
rm -fr $CATALINA_HOME/webapps/db_*
cp build/libs/db_rest.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh



#
# deploy to ec2 instance tomcat
#
#sshpass -p "dbuser" ssh -o StrictHostKeyChecking=no root@52.193.219.37 "rm -fr /var/lib/tomcat7/webapps/db_*"
#scp build/libs/db_rest.war root@52.193.219.37:/var/lib/tomcat7/webapps/ 
#sshpass -p "dbuser" ssh -o StrictHostKeyChecking=no root@52.193.219.37 "service tomcat7 restart"
