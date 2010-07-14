Summary: OC soatools
Name: oc-soatools
Version: ${project.version}
Release: 3
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
rm -rf $RPM_BUILD_ROOT/etc
mkdir -p $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
mkdir -p $RPM_BUILD_ROOT/etc/jbossesb
cp $RPM_SOURCE_DIR/soatools-*.esb $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/soatools-*.war $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/jbm/soatools-*-queue-service.xml $RPM_BUILD_ROOT/srv/jbossesb/server/default/deploy
cp $RPM_SOURCE_DIR/mysql-soatools-init.sql $RPM_BUILD_ROOT/etc/jbossesb

%files
%defattr(-,jbossesb,jboss)
/srv/jbossesb
%attr(-,root,root) /etc/jbossesb
