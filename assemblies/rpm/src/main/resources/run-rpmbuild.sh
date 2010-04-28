#!/bin/sh

export RPM_TOPDIR=$PWD

mkdir -p $RPM_TOPDIR/BUILD
mkdir -p $RPM_TOPDIR/RPMS

rm -rf /tmp/jbossesb*
rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS/oc-soatools.spec
rpmbuild --define "_topdir $RPM_TOPDIR" -bb $RPM_TOPDIR/SPECS/bettermarks-esb.spec
