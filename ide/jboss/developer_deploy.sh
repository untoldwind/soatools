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

rm -rf soatools-*

ln -s $BASE/logging/logstore/src/main/jbm/soatools-logging-logstore-queue-service.xml soatools-logging-logstore-queue-service.xml
ln -s $BASE/logging/logstore/target/soatools-logging-logstore soatools-logging-logstore.esb
ln -s $BASE/logging/logstore-ws/target/soatools-logging-logstore-ws soatools-logging-logstore-ws.war
ln -s $BASE/model/mfm/target/soatools-model-mfm soatools-model-mfm.esb
ln -s $BASE/test/services/consumer/src/main/jbm/soatools-test-services-consumer-queue-service.xml soatools-test-services-consumer-queue-service.xml
ln -s $BASE/test/services/consumer/target/soatools-test-services-consumer soatools-test-services-consumer.esb
ln -s $BASE/test/services/jms-gateway/src/main/jbm/soatools-test-services-jms-gateway-queue-service.xml soatools-test-services-jms-gateway-queue-service.xml
ln -s $BASE/test/services/jms-gateway/target/soatools-test-services-jms-gateway soatools-test-services-jms-gateway.esb
ln -s $BASE/test/services/jbpm-processes/src/main/jbm/soatools-test-services-jbpm-processes-queue-service.xml soatools-test-services-jbpm-processes-queue-service.xml
ln -s $BASE/test/services/jbpm-processes/target/soatools-test-services-jbpm-processes soatools-test-services-jbpm-processes.esb
ln -s $BASE/test/services/webservice-in/target/soatools-test-services-webservice-in soatools-test-services-webservice-in.war
ln -s $BASE/utils/invoker/target/soatools-utils-invoker soatools-utils-invoker.esb

cd $CURRENT
