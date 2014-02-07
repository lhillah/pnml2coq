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
package fr.lipn.lcr.pnmltocoq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

import fr.lip6.move.pnml.framework.general.PnmlImport;
import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.pnml.framework.utils.exception.AssociatedPluginNotFound;
import fr.lip6.move.pnml.framework.utils.exception.BadFileFormatException;
import fr.lip6.move.pnml.framework.utils.exception.InnerBuildException;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.OCLValidationFailed;
import fr.lip6.move.pnml.framework.utils.exception.OtherException;
import fr.lip6.move.pnml.framework.utils.exception.UnhandledNetType;
import fr.lip6.move.pnml.framework.utils.exception.ValidationFailedException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lipn.lcr.pnmltocoq.exceptions.UnHandledType;
import fr.lipn.lcr.pnmltocoq.processors.MainProcessor;
import fr.lipn.lcr.pnmltocoq.processors.Processor;

/**
 * @author Laure Petrucci
 */
public final class PnmltoCoq {
	private static final Logger journal = LoggerFactory
			.getLogger(PnmltoCoq.class.getCanonicalName());
	private static final String TOTAL_TIME_ALL_TRANSLATIONS = "Total time taken by all translations: ";
	private static final char CH = '.';
	private static final String COQ = ".coq";
	private static final String IMPORTING_FILE = "importing file ";
	private static final String OUTPUT_COQ_FILE_IS = "Output Coq file is: ";
	private static final String TIME_TAKEN_BY_THIS_TRANSLATION = "Total time taken by this translation: ";
	private static final String SECONDS = " seconds";
	private static final String TIME_TAKEN_BY_IMPORT = "Time taken by the import step: ";
	private static final String TIME_TAKEN_BY_PNML2COQ = "Time taken by the PNML to coq step: ";

	/**
	 * @param args
	 *            .
	 * @throws VoidRepositoryException .
	 * @throws InvalidIDException .
	 * @throws AssociatedPluginNotFound .
	 * @throws OtherException .
	 * @throws OCLValidationFailed .
	 * @throws InnerBuildException .
	 * @throws ValidationFailedException .
	 * @throws UnhandledNetType .
	 * @throws BadFileFormatException .
	 * @throws IOException .
	 */
	public static void main(String[] args) throws IOException,
			BadFileFormatException, UnhandledNetType,
			ValidationFailedException, InnerBuildException,
			OCLValidationFailed, OtherException, AssociatedPluginNotFound,
			InvalidIDException, VoidRepositoryException {
		try {
			if (args.length < 1) {
				journal.error(
						"Expecting at least the path to a PNML file.");
				return;
			}

			long tempsT1s, tempsT1e = 0L, tempsT2s = 0L, tempsT2e = 0L, tempsT3s, tempsT3e, tempsTo1, tempsTo2;
			HLAPIRootClass imported = null;
			Processor proc = null;
			String outputFilePath = null;
			tempsTo1 = System.nanoTime();
			PnmlImport pim = new PnmlImport(null);
			pim.setFallUse(true);
			for (String s : args) {
				System.out.println(IMPORTING_FILE + s);
				tempsT1s = System.nanoTime();
				try {
					ModelRepository.getInstance().createDocumentWorkspace(s);
					imported = (HLAPIRootClass) pim.importFile(s);
					proc = MainProcessor.getProcessor(imported);
					tempsT1e = System.nanoTime();

					tempsT2s = System.nanoTime();
					outputFilePath = computeCoqFilePath(s);
					proc.process(imported, outputFilePath);
					ModelRepository.getInstance().destroyCurrentWorkspace();
					tempsT2e = System.nanoTime();

				} catch (InvalidIDException | VoidRepositoryException
						| UnHandledType | IOException e1) {
					e1.printStackTrace();
				}

				journal.info(OUTPUT_COQ_FILE_IS + outputFilePath);
				journal.info(TIME_TAKEN_BY_THIS_TRANSLATION
						+ ((tempsT2e - tempsT1s) / 1.0e9) + SECONDS);
				journal.info(TIME_TAKEN_BY_IMPORT
						+ +((tempsT1e - tempsT1s) / 1.0e9) + SECONDS);
				journal.info(TIME_TAKEN_BY_PNML2COQ
						+ +((tempsT2e - tempsT2s) / 1.0e9) + SECONDS);
			}
			tempsTo2 = System.nanoTime();
			journal.info(TOTAL_TIME_ALL_TRANSLATIONS
					+ ((tempsTo2 - tempsTo1) / 1.0e9) + SECONDS);
		} finally {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory
					.getILoggerFactory();
			loggerContext.stop();
		}
	}

	private static String computeCoqFilePath(String InPath) {
		int ind = InPath.lastIndexOf(CH);
		return InPath.substring(0, ind) + COQ;
	}
}
