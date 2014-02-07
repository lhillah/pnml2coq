#! /bin/sh
#
#  Copyright 2010 Universite Paris 13 - CNRS UMR 7030 (LIPN)
#
#  All rights reserved.   This program and the accompanying materials
#  are made available under the terms of the Eclipse Public License v1.0
#  which accompanies this distribution, and is available at
#  http://www.eclipse.org/legal/epl-v10.html
#
#  Project leader / Initial Contributor:
#    Laure Petrucci - <Laure.Petrucci@lipn.univ-paris13.fr>
#
#  Contributors:
#    Lom Messan Hillah - <$oemails}>
#
#  Mailing list:
#    Laure.Petrucci@lipn.univ-paris13.fr
#


############################################################################################ 
# Script to launch PNML 2 COQ model transformation.                                        #
# Version: 2014-02-05                                                                      #
# Contributors: Laure Petrucci & Lom M. Hillah                                             #
# Institutions: Université Paris 13 and Sorbonne Universités, Univ. Paris 06               #
# Example: ./pnml2coq.sh pathToASinglePNMLFile [pathToAnotherPNMLFile...]                  #
############################################################################################

# Path to executable Jar file of the PNML2Coq tool.
JAR_FILE=fr.lipn.lcr.pnml2coq-1.0.1.jar

# Constants
NBPARAM=1
E_NOFILE=66
E_ERROR=1
E_SUCCESS=0

# Set of advanced arguments for JVM. Increase or decrease memory for the heap if needed by modifying the value of -Xmx
JVM_ARGS="-d64 -server -Xmx2g -Xmn128m -XX:NewSize=2g -XX:MaxNewSize=2g -XX:+UseNUMA -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"


if [ $# -lt "$NBPARAM" ] 
	then
	 echo "At least one argument is expected, a path to a PNML file."
	 echo "Usage: ./`basename $0` pathToASinglePNMLFile [pathToAnotherPNMLFile...]"
	 exit "$E_NOFILE" 
fi

echo "Launching PNML2Coq program"

java $JVM_ARGS -jar $JAR_FILE "$@" || exit "$E_ERROR"

exit "$E_SUCCESS"
