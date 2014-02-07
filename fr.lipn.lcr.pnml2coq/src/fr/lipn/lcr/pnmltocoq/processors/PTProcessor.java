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

import java.io.IOException;
import java.io.PrintWriter;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;

/**
 * Transforms P/T nets from PNML into Coq.
 * @author laure
 *
 */
public final class PTProcessor extends Processor {

    private int nbplaces = 0;
    private int nbtransitions = 0;
    private int nbarcs = 0;
    private String allPlaces = "Definition P := ";
    private String initMarking = "Definition InitMarkI := ";
    private String allTransitions = "Definition T := ";
    private String ptArcs = "Definition Apt := ";
    private String tpArcs = "Definition Atp := ";
    
    public PTProcessor() {
    	super();
    }

    public void process(HLAPIRootClass rcl, PrintWriter fr) {
        fwloc = fr;
        try {
			processPnDoc(rcl);
		} catch (IOException e) {
			e.printStackTrace();
		}
        fr.close();
        resetDecalage();
    }
    
    private void processPnDoc(HLAPIRootClass rcl) throws IOException { 
    	PetriNetDocHLAPI root = (PetriNetDocHLAPI) rcl;
        System.out.println(root.getNets().size());
        for (PetriNetHLAPI iterable_element : root.getNetsHLAPI()) {
            processNets(iterable_element);
        }
    }

    private void processNets(PetriNetHLAPI ptn) throws IOException {
        print("Require Export List.");
        print("Record Place : Type := mkPlace {Pl : nat}.");
        print("Record Transition : Type := mkTransition {Tr : nat}.");
        print("");

        //incrementDecalage();
        for (PageHLAPI iterable_element : ptn.getPagesHLAPI()) {
            processPages(iterable_element);
        }
        //decrementdecalage();
        print("");
        print(allPlaces + "nil.");
        print(initMarking + "nil.");
        print(allTransitions + "nil.");
        print(ptArcs + "nil.");
        print(tpArcs + "nil.");
   }

    private void processPages(PageHLAPI pth) throws IOException {
        for (PageHLAPI iterable_element : pth.getObjects_PageHLAPI()) {
            processPages(iterable_element);
        }
        for (PlaceHLAPI iterable_element : pth.getObjects_PlaceHLAPI()) {
            processPlace(iterable_element);
        }
        for (TransitionHLAPI iterable_element : pth.getObjects_TransitionHLAPI()) {
            processTransition(iterable_element);
        }
        for (ArcHLAPI iterable_element : pth.getObjects_ArcHLAPI()) {
            processArc(iterable_element);
        }
    }

    private void processNode(NodeHLAPI nhp, String nodeType, StringBuilder sb, int nodenum) {
    	sb.append("Definition " + nhp.getName() != null ? nhp.getName().getText() : nhp.getId() + " := mk" + nodeType + " " + nodenum + ".");
    }
 
    private void processMarking(PlaceHLAPI pla, StringBuilder sb) {
    	sb.append("Definition m" + pla.getName().getText() + " := (" + pla.getName().getText() + "," + 
    			extractMarking(pla) + ").");
    }

    private void processPlace(PlaceHLAPI pla) throws IOException {
        StringBuilder sb = new StringBuilder();
        nbplaces++;
        processNode(pla, "Place", sb, nbplaces);
        allPlaces = allPlaces + pla.getName().getText() +"::";
        sb.append("\n");
        processMarking(pla, sb);
        initMarking = initMarking + "m" + pla.getName().getText() +"::";
        print(sb.toString());
    }

    private void processTransition(TransitionHLAPI tra) throws IOException {
        StringBuilder sb = new StringBuilder();
        nbtransitions++;
        processNode(tra, "Transition", sb, nbtransitions);
        allTransitions = allTransitions + tra.getName().getText() +"::";
        print(sb.toString());
    }

    private void processArc(ArcHLAPI arc) throws IOException {
        StringBuilder sb = new StringBuilder();
        nbarcs++;
        sb.append("Definition A" + nbarcs + " := (" + arc.getSource().getName().getText() + ","
                + arc.getTarget().getName().getText() + ","+ extractInscription(arc) + ").");
        if (arc.getSource() instanceof fr.lip6.move.pnml.ptnet.Place)
        	ptArcs = ptArcs + "A" + nbarcs + "::";
        else tpArcs = tpArcs + "A" + nbarcs + "::";
        print(sb.toString());
    }
    
    private String extractMarking(PlaceHLAPI pla) {
    	String marking = null;
    	if (pla.getInitialMarkingHLAPI() != null) {
    		Integer intMark = pla.getInitialMarkingHLAPI().getText();
    		marking = intMark != null ? intMark.toString() : "0"; 
    	} else {
    		marking = "0";
    	}	
    	return marking;
    }
    
    private String extractInscription(ArcHLAPI arc) {
		String inscription = null;
		if (arc.getInscriptionHLAPI() != null) {
			Integer intInsc = arc.getInscriptionHLAPI().getText();
			inscription = intInsc != null ? intInsc.toString() : "1";
		} else {
			inscription = "1";
		}
		return inscription;
	}

	@Override
	public void process(HLAPIRootClass rcl, String outputFilePath) throws IOException {
		openOutputChannel(outputFilePath);
		processPnDoc(rcl);
		closeOutputChannel();
		resetDecalage();
	}

}