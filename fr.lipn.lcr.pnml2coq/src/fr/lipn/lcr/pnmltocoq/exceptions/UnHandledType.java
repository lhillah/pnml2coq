/**
 *  Copyright 2010 Universite Paris 13 - CNRS UMR 7030 (LIPN)
 *
 *  All rights reserved.   This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Project leader / Initial Contributor:
 *    Laure Petrucci - <Laure.Petrucci@lipn.univ-paris13.fr>
 *
 *  Contributors:
 *    Lom Messan Hillah - <$oemails}>
 *
 *  Mailing list:
 *    Laure.Petrucci@lipn.univ-paris13.fr
 */
package fr.lipn.lcr.pnmltocoq.exceptions;

public class UnHandledType extends Exception {

    private static final long serialVersionUID = -544239706092600793L;

	public UnHandledType() {
		super();
	}

    public UnHandledType(String mssg) {
    	super(mssg);
    }
}
