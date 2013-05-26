# system variables
KNIME_VER=2.7.3
ARCH=`arch`
REPO="adudek/ic-depress"
CI_HOME=`pwd`/$REPO/ic-depress-data-anonymisation
ECLIPSE_HOME=$CI_HOME/../../eclipse
COMMON_LIBS_HOME=$CI_HOME/../dev-buildtools/common-libs

# before install
wget http://www.knime.org/knime_downloads/linux/eclipse_knime_$KNIME_VER.linux.gtk.$ARCH.tar.gz
tar -zxf eclipse_knime_$KNIME_VER.linux.gtk.$ARCH.tar.gz
ln -s `pwd`/eclipse_knime_$KNIME_VER $ECLIPSE_HOME
cd $CI_HOME

# compilation
ant build
ant compress

# installation
mv bin/org.impressivecode.depress.data.anonymisation.jar $ECLIPSE_HOME/plugins/

language: java
jdk:
oraclejdk7
openjdk7

branches:
  only:
dev

notifications:
  email: true