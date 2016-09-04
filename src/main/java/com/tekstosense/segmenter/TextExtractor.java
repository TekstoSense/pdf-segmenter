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

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.tekstosense.segmenter.data.Page;
import com.tekstosense.segmenter.data.Text;
import com.tekstosense.segmenter.model.DoublyLinkedList;
import com.tekstosense.segmenter.model.LineNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extract text from a PDF document with position and style information.
 * This class attempts to coalesce runs of text on the page; that is,
 *
 * @author Karl Ward
 */
public class TextExtractor extends PDFTextStripper {

    public Page currentPage = null;
    private ArrayList<Page> previousPages = new ArrayList<Page>();
    private ArrayList<PDPage> pagesList = new ArrayList<>();
    private int pageCount = 0;

    public TextExtractor() throws IOException {
        super();
    }

    public void processTextPositionNew(TextPosition tp, PDGraphicsState gs, String text) {
        currentPage.addText(Text.newFor(tp, gs, text));
    }

    private void coalesceRows(Page page, String text) {
        for (Float f : page.getYPosWithText()) {
            List<Text> ts = page.getTextAtY(f);

            Collections.sort(ts);

            int i = 0;
            while (i + 1 < ts.size()) {
                Text first = ts.get(i);
                Text snd = ts.get(i + 1);

                if (first.hasMatchingStyle(snd)) {
                    first.addAfter(snd, text);
                    page.removeText(snd);
                } else {
                    i++;
                }
            }
        }
    }

    // TODO Should handle drop shadow that is positioned on a slightly
    // different x, y.
    private void removeDuplicates(Page page) {
        for (Float f : page.getYPosWithText()) {
            List<Text> ts = page.getTextAtY(f);

            Collections.sort(ts);

            int i = 0;
            while (i + 1 < ts.size()) {
                Text first = ts.get(i);
                Text snd = ts.get(i + 1);

                if (first.getRun().equals(snd.getRun())
                        && first.getX() == snd.getX()) {
                    page.removeText(snd);
                } else {
                    i++;
                }
            }
        }
    }

/*	public String toString() {
        String s = "";
		for (Page page : previousPages) {
			s += "Page @ " + page.getClipBox().getUpperRightY()
						   + ", " + page.getClipBox().getLowerLeftX();
			for (Text t : page.getText()) {
				s += t.getRun() + " @ " + t.getX() + "," + t.getBaseline() 
							+ " w " + t.getWidth()
							+ " : " + t.getBaseFontName() 
							+ " "   + t.getPointSize() + "pt"
							+ " C " + t.getForegroundColor()
							+ "\n";
			}
		}
		return s;
	}
*/

    @Override
    protected void writeString(String string, List<TextPosition> pos) throws IOException {

        PDPage page = this.getCurrentPage();

        if (!pagesList.contains(page)) {
            currentPage = new Page(page.getCropBox(), ++pageCount);
            pagesList.add(page);
        }

        if (!previousPages.contains(currentPage))
            previousPages.add(currentPage);

        PDGraphicsState gs = this.getGraphicsState();
        for (TextPosition tp : pos) {
            this.processTextPositionNew(tp, gs, string);
        }

        coalesceRows(currentPage, string);
        removeDuplicates(currentPage);

    }

    public DoublyLinkedList<LineNode> toLinkedList() {
        DoublyLinkedList<LineNode> lineNodeDoublyLinkedList = new DoublyLinkedList<>();
            for (Page page : previousPages) {
                for (Text t : page.getText()) {
                    if(t.getPointSize() != 0F) {
                        LineNode lineNode = new LineNode(page.getNumber(), t.getTop(), t.getX(), t.getWidth(), t.getHeight(), t.getPointSize(),t.getDescent(),t.getAscent(), t.getFontFamily(), t.getFontFace(), t.getForegroundColor(), t.getRun());
                        lineNodeDoublyLinkedList.addToLast(lineNode);
                    }
                }
            }
            return lineNodeDoublyLinkedList;
    }

    /**
     * @return Answers an image that contains coloured rectangles representing
     * the locations of text runs.
     */
    public BufferedImage toMaskImage(int pageN) {
        Page p = previousPages.get(pageN - 1);
        BufferedImage bi = new BufferedImage((int) p.getClipBox().getWidth(),
                (int) p.getClipBox().getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(Color.BLACK);
        for (Text t : p.getText()) {
            g.fillRect((int) t.getX(),
                    (int) t.getTop(),
                    (int) t.getWidth(),
                    (int) t.getHeight());
        }

        return bi;
    }
}

