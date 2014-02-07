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
package fr.lipn.lcr.pnmltocoq.processors;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lipn.lcr.pnmltocoq.exceptions.UnHandledType;

public final class MainProcessor {
	
		private static final class CoreSingletonHolder {
			private static final Processor CORE = new CoreProcessor();
			
			private CoreSingletonHolder() {
				super();
			}
		}
		
		private static final class PTSingletonHolder {
			private static final Processor PT = new PTProcessor();
			
			private PTSingletonHolder() {
				super();
			}
		}
	
	    public static Processor getProcessor(HLAPIRootClass theclass)
	            throws UnHandledType {
	        Processor p = null;
	        if (theclass.getClass().equals(
	                fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetDocHLAPI.class)) {
	            p = CoreSingletonHolder.CORE;
	        }

	        if (theclass.getClass().equals(
	        		fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI.class)) {
	        	p = PTSingletonHolder.PT;
	        }
//	        if (theclass.getClass().equals(
//	                        fr.lip6.move.pnml.symmetricnet.hlcorestructure.hlapi.PetriNetDocHLAPI.class)) {
//	            p = new SNProcessor();
//	        }

	        if (p == null) {
	            throw new UnHandledType("this PNML type is not supported by this version of PNML2Coq");
	        }
	        return p;
	    }

}
