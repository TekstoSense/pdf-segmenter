/*******************************************************************************
 * Copyright (c) 2016, TekstoSense and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tekstosense.segmenter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.tekstosense.segmenter.model.DoublyLinkedList;
import com.tekstosense.segmenter.model.LineNode;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Main extends PDFTextStripper{
    
    @Option(name="-p", usage="Specify an optional decryption password.",
            required=false, multiValued=false, metaVar="PASSWD")
    private String password = "";
    
    @Option(name="-m", usage="Also create a text location mask image.",
            required=false, multiValued=false, metaVar="PAGENO")
    private int maskImagePage = 0;
    
    @Argument
    private List<String> filenames = new ArrayList<String>();

    /**
     * Instantiate a new PDFTextStripper object.
     *
     * @throws IOException If there is an error loading the properties.
     */
    public Main() throws IOException {
    }

    private TextExtractor parsePdf(File f) throws IOException {
		PDDocument doc = PDDocument.load(f);
		
		if(doc.isEncrypted()) {
            // Some documents are encrypted with the empty password. Try
		    // to decrypt with this password, or the one passed in on the
		    // command line (if any), and fail if we can't.
            try {
            	doc.setAllSecurityToBeRemoved(false);
                //doc.decrypt(password); // Defaults to the empty string.
            } catch (Exception e) {
                throw new IOException("Can't decrypt document: ", e);
            }
        }
		TextExtractor te = new TextExtractor();
		te.writeText(doc,new OutputStreamWriter(new ByteArrayOutputStream()));

		return te;
	}
	
	private void doMain() {
	    for (String filename : filenames) {
            File inputFile = new File(filename);
            TextExtractor te = null;
            
            try {
                te = parsePdf(inputFile);
                DoublyLinkedList<LineNode> sentenceList = te.toLinkedList();
                sentenceList.displayList();
            } catch (IOException e) {
                System.err.println("Couldn't read file '" + inputFile +"'.");
                System.exit(1);
            }
            
            try {
                if (maskImagePage > 0) {
                    BufferedImage maskImage = te.toMaskImage(maskImagePage);
                    ImageIO.write(maskImage, "png", new File("mask.png"));
                }
            } catch (IOException e) {
                System.err.println("Couldn't write mask image 'mask.png'.");
                System.exit(1);
            }
            
        }
	}
	
	public static void main(String[] args) throws IOException {
	    Main m = new Main();
        CmdLineParser parser = new CmdLineParser(m);
       
        try {
            parser.parseArgument(args);
            
            if (m.filenames.size() == 0) {
                System.err.println("Usage: pdf2xml [options] <FILEs>");
                parser.printUsage(System.err);
            } else {
                m.doMain();
            }
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
        }
	}
}
