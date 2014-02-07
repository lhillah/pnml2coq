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
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.NodeHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PageHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.TransitionHLAPI;

public final class CoreProcessor extends Processor {

    private int nbplaces = 0;
    private int nbtransitions = 0;
    private int nbarcs = 0;
    private String allPlaces = "Definition P := ";
    private String initMarking = "Definition InitMarkI := ";
    private String allTransitions = "Definition T := ";
    private String ptArcs = "Definition Apt := ";
    private String tpArcs = "Definition Atp := ";
    
    public CoreProcessor() {
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

    private void processNode(NodeHLAPI nhp, String nodeType, StringBuffer sb, int nodenum) {
    	sb.append("Definition " + nhp.getId() + " := mk" + nodeType + " " + nodenum + ".");
    }

    private void processMarking(PlaceHLAPI pla, StringBuffer sb) {
    	sb.append("Definition m" + pla.getId() + " := (" + pla.getId() + ",0).");
    }

    private void processPlace(PlaceHLAPI pla) throws IOException {
        StringBuffer sb = new StringBuffer();
        nbplaces++;
        processNode(pla, "Place", sb, nbplaces);
        allPlaces = allPlaces + pla.getId() +"::";
        sb.append("\n");
        processMarking(pla, sb);
        initMarking = initMarking + "m" + pla.getId() +"::";
        print(sb.toString());
    }

    private void processTransition(TransitionHLAPI tra) throws IOException {
        StringBuffer sb = new StringBuffer();
        nbtransitions++;
        processNode(tra, "Transition", sb, nbtransitions);
        allTransitions = allTransitions + tra.getId() +"::";
        print(sb.toString());
    }

    private void processArc(ArcHLAPI arc) throws IOException {
        StringBuffer sb = new StringBuffer();
        nbarcs++;
        sb.append("Definition A" + nbarcs + " := (" + arc.getSource().getId() + ","
                + arc.getTarget().getId() + ",1).");
        if (arc.getSource() instanceof fr.lip6.move.pnml.pnmlcoremodel.Place)
        	ptArcs = ptArcs + "A" + nbarcs + "::";
        else tpArcs = tpArcs + "A" + nbarcs + "::";
        print(sb.toString());
    }

	@Override
	public void process(HLAPIRootClass rcl, String outputFilePath) throws IOException {
		openOutputChannel(outputFilePath);
		processPnDoc(rcl);
		closeOutputChannel();
		resetDecalage();
	}
}