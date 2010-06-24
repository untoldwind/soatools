#!/bin/sh

if [ "$JBOSS_HOME" = "" ]
then
  echo "JBOSS_HOME not set"
  exit 1
fi

if [ "$JBOSS_CONF" = "" ]
then
  echo "JBOSS_CONF not set"
  exit 1
fi

CURRENT=`pwd` 
BASE=$CURRENT/../..

cd $JBOSS_HOME/server/$JBOSS_CONF/deploy

rm -rf soatools-test-*

ln -s $BASE/test/services/consumer/src/main/jbm/soatools-test-services-consumer-queue-service.xml soatools-test-services-consumer-queue-service.xml
ln -s $BASE/test/services/consumer/target/soatools-test-services-consumer soatools-test-services-consumer.esb
ln -s $BASE/test/services/error/src/main/jbm/soatools-test-services-error-queue-service.xml soatools-test-services-error-queue-service.xml
ln -s $BASE/test/services/error/target/soatools-test-services-error soatools-test-services-error.esb
ln -s $BASE/test/services/webservice-call/src/main/jbm/soatools-test-services-webservice-call-queue-service.xml soatools-test-services-webservice-call-queue-service.xml
ln -s $BASE/test/services/webservice-call/target/soatools-test-services-webservice-call soatools-test-services-webservice-call.esb
ln -s $BASE/test/services/jms-gateway/src/main/jbm/soatools-test-services-jms-gateway-queue-service.xml soatools-test-services-jms-gateway-queue-service.xml
ln -s $BASE/test/services/jms-gateway/target/soatools-test-services-jms-gateway soatools-test-services-jms-gateway.esb
ln -s $BASE/test/services/jbpm-processes/src/main/jbm/soatools-test-services-jbpm-processes-queue-service.xml soatools-test-services-jbpm-processes-queue-service.xml
ln -s $BASE/test/services/jbpm-processes/target/soatools-test-services-jbpm-processes soatools-test-services-jbpm-processes.esb
ln -s $BASE/test/services/webservice-in/target/soatools-test-services-webservice-in soatools-test-services-webservice-in.war
ln -s $BASE/test/services/mock-webservice/target/soatools-test-services-mock-webservice soatools-test-services-mock-webservice.war

cd $CURRENT
