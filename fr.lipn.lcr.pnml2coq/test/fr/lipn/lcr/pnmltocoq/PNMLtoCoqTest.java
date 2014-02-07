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

import java.io.File;
import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import fr.lip6.move.pnml.framework.utils.exception.AssociatedPluginNotFound;
import fr.lip6.move.pnml.framework.utils.exception.BadFileFormatException;
import fr.lip6.move.pnml.framework.utils.exception.InnerBuildException;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.OCLValidationFailed;
import fr.lip6.move.pnml.framework.utils.exception.OtherException;
import fr.lip6.move.pnml.framework.utils.exception.UnhandledNetType;
import fr.lip6.move.pnml.framework.utils.exception.ValidationFailedException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

public final class PNMLtoCoqTest {

    private static String fullpath = "";

    @BeforeTest(groups = { "pnmltocoq" })
    public void setUp() throws Exception {
        fullpath = new File("").getAbsolutePath().split("/models")[0]
                + "/models";
        System.out.println("the root folder is "
                + new File("").getAbsolutePath());
        System.out.println("full path to models " + fullpath);
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test(groups = { "pnmltocoq" })
    public void testMain() throws IOException, BadFileFormatException,
            UnhandledNetType, ValidationFailedException, InnerBuildException,
            OCLValidationFailed, OtherException, AssociatedPluginNotFound,
            InvalidIDException, VoidRepositoryException {
       /* String inFile = fullpath + "/model_0.pnml";
        String outFile = fullpath + "/model_0.coq";
        */
        String inFile = fullpath + "/philo.pnml";
        String outFile = fullpath + "/philo.coq";
        //String customfile = fullpath + "/other.xml";
        String customfile = null;
        String[] args = { inFile, outFile, customfile };

        PnmltoCoq.main(args);
    }
}
