Summary: OC soatools
Name: oc-soatools
Version: ${rpm.version}
Release: 1
License: MIT
Group: Productivity/Networking/Web/Servers
Source: http://github.org
BuildArch: noarch
Requires: jbossesb-server
BuildRoot: /tmp/soatools

%description
OC soatools utilities package

%prep

%install
rm -rf $RPM_BUILD_ROOT/srv
mkdir -p $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/soatools-*.esb $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/soatools-*.war $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/jbm/soatools-*-queue-service.xml $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy

%files
%defattr(-,jbossesb,jboss)
/srv/jbossesb
