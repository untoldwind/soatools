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
ln -s $BASE/logging/logstore-ui/target/soatools-logging-logstore-ui soatools-logging-logstore-ui.war
ln -s $BASE/model/mfm/target/soatools-model-mfm soatools-model-mfm.esb
ln -s $BASE/utils/generic/target/soatools-utils-generic soatools-utils-generic.esb
ln -s $BASE/utils/invoker/target/soatools-utils-invoker soatools-utils-invoker.esb
ln -s $BASE/utils/smooks/target/soatools-utils-smooks soatools-utils-smooks.esb
ln -s $BASE/camel/target/soatools-camel soatools-camel.esb

cd $CURRENT
