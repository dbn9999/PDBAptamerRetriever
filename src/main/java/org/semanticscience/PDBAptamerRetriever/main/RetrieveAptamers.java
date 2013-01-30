/**
 * Copyright (c) 2013  Jose Cruz-Toledo
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.semanticscience.PDBAptamerRetriever.main;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.semanticscience.PDBAptamerRetriever.bin.PDBAptamerIDRetriever;
import org.semanticscience.PDBAptamerRetriever.bin.PDBRecordRetriever;
import org.semanticscience.PDBAptamerRetriever.shared.OptionComparator;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class RetrieveAptamers {
	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = createCliParser();
		String expMeth = null;
		String molT = null;
		boolean concatFasta = false;
		File fastaDir = null;
		File pdbDir = null;
		boolean ligandReport = false;
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("help")) {
				printUsage();
				System.exit(1);
			}
			if (cmd.hasOption("em")) {
				// validate mehtod
				if (validateExpMethod(cmd.getOptionValue("em"))) {
					expMeth = cmd.getOptionValue("em");
				} else {
					System.out.println("Invalid experimental method entered!");
					printUsage();
					System.exit(1);
				}
			}
			if (cmd.hasOption("mt")) {
				// validate moltype
				if (validateMolType(cmd.getOptionValue("mt"))) {
					molT = cmd.getOptionValue("mt");
				} else {
					System.out.println("Invalid molecule type!");
					printUsage();
					System.exit(1);
				}
			}

			if (cmd.hasOption("fastaDir")) {
				fastaDir = new File(cmd.getOptionValue("fastaDir"));
			}
			if (cmd.hasOption("cf")) {
				concatFasta = true;
			}
			if (cmd.hasOption("lr")) {
				ligandReport = true;
			}
			if (cmd.hasOption("pdbDir")) {
				pdbDir = new File(cmd.getOptionValue("pdbDir"));
			}
			PDBAptamerIDRetriever par = new PDBAptamerIDRetriever(molT, expMeth);
			System.out.println("Retrieving Data from PDB ...");
			PDBRecordRetriever prr = new PDBRecordRetriever(par.getPdbids());
			// now write the CSV file
			File csv = new File("summary.csv");
			FileUtils.writeStringToFile(csv, prr.getCSVString());
			System.out.println("summary.csv successfully created!");
			// verify the options
			if (ligandReport) {
				// create the ligand report
				File lr = new File("ligand-report.csv");
				String ligRep = prr.getLigandCSVReport();
				FileUtils.writeStringToFile(lr, ligRep);
				System.out.println("ligand-report.csv successfully created!");
			}
			if (fastaDir != null) {
				try {
					boolean b = par.retrieveFasta(fastaDir, concatFasta);
					if (b) {
						System.out
								.println("FASTA files downloaded successfully!");
					} else {
						System.out
								.println("FASTA files could not be downloaded");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (pdbDir != null) {
				try {
					boolean b = par.retrievePDB(pdbDir);
					if (b) {
						System.out
								.println("PDB files downloaded successfully!");
					} else {
						System.out.println("PDB files could not be downloaded");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			System.out.println("Unable to parse specified options!");
			printUsage();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options o = new Options();
		// help option
		Option help = new Option("help", false, "Print this message");
		Option expMethod = OptionBuilder
				.withArgName("X-RAY")
				.hasArg(true)
				.withDescription(
						"Enter an expeirmental method. Valid options are X-RAY, NMR or all")
				.isRequired().create("em");
		Option moleculeType = OptionBuilder
				.withArgName("RNA")
				.hasArg(true)
				.withDescription(
						"Enter a molecule type. Valid options are DNA, RNA or BOTH")
				.isRequired().create("mt");
		Option outputFastaDir = OptionBuilder
				.withArgName("fastaDir")
				.hasArg(true)
				.withDescription(
						"The directory where you wish to save your FASTA files")
				.create("fastaDir");
		Option concatenateFASTA = OptionBuilder
				.hasArg(false)
				.withDescription(
						"Add this parameter if you want all of your FASTA files to be downloaded into a single file")
				.create("cf");
		Option outputPDBDir = OptionBuilder
				.withArgName("/path/to/local/dir")
				.hasArg(true)
				.withDescription(
						"The directory where you wish to save your PDB files")
				.create("pdbDir");
		Option ligandReport = OptionBuilder
				.hasArg(false)
				.withDescription("Add this parameter to create a ligand report")
				.create("lr");

		o.addOption(help);
		o.addOption(expMethod);
		o.addOption(moleculeType);
		o.addOption(outputFastaDir);
		o.addOption(concatenateFASTA);
		o.addOption(outputPDBDir);
		o.addOption(ligandReport);

		return o;
	}

	private static CommandLineParser createCliParser() {
		return new GnuParser();
	}

	private static void printUsage() {
		HelpFormatter hf = new HelpFormatter();
		hf.setOptionComparator(new Comparator() {
			private final String OPTS_ORDER = "helpemmtcflrfastaDirpdbDir";
			public int compare(Object o1, Object o2) {
				Option opt1 = (Option) o1;
				Option opt2 = (Option) o2;
				return OPTS_ORDER.indexOf(opt1.getOpt()) - OPTS_ORDER.indexOf(opt2.getOpt());
			}
		});
		hf.printHelp("java -jar RetrieveAptamers.jar [OPTIONS]",
				createOptions());
	}

	private static boolean validateExpMethod(String aMethod) {
		if (aMethod.equals("X-RAY") || aMethod.equals("NMR")
				|| aMethod.equals("all")) {
			return true;
		}
		return false;
	}

	private static boolean validateMolType(String aMolType) {
		if (aMolType.equals("DNA") || aMolType.equals("RNA")
				|| aMolType.equals("BOTH")) {
			return true;
		}
		return false;
	}

}
