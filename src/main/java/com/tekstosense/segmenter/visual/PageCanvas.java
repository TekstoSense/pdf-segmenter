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
package com.tekstosense.segmenter.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import com.tekstosense.segmenter.data.Page;
import com.tekstosense.segmenter.data.Text;

public class PageCanvas extends JPanel {
    
    private static final float WHEEL_PER_CLICK_ZOOM = 0.1f;
    
    private Page currentPage;
    
    private int pageIndex;
    
    private List<Page> pages;
    
    private Dimension pageSize = new Dimension(600, 800);
    
    public PageCanvas(Page newPage) {
        currentPage = newPage;
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isAltDown()) {
                    int direction = e.getButton() == MouseEvent.BUTTON1 ? 1 : -1;
                    pageSize.width += direction * WHEEL_PER_CLICK_ZOOM
                                    * pageSize.width;
                    pageSize.height += direction * WHEEL_PER_CLICK_ZOOM
                                    * pageSize.height;

                    repaint();
                } else if (pages != null && pages.size() > 1) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        currentPage = pages.get(++pageIndex % pages.size());
                    }
                    repaint();
                }
            }
        });
        
        Dimension standard = new Dimension(600, 800);
        setMinimumSize(standard);
        setMaximumSize(standard);
        setPreferredSize(standard);
    }
    
    public PageCanvas(List<Page> newPages) {
        this(newPages.get(0));
        pages = newPages;
        pageIndex = 0;
    }
    
    @Override
    public Dimension getMaximumSize() {
        return pageSize;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return pageSize;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return pageSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        
        final float pageWidth = currentPage.getClipBox().getWidth();
        final float scalingScalar = pageSize.width / pageWidth;
        
        for (Text t : currentPage.getText()) {
            int style = Font.PLAIN;
            if (t.getFontFace().equals("Bold")) {
                style = Font.BOLD;
            } else if (t.getFontFace().equals("Italic")
                    || t.getFontFace().equals("Oblique")) {
                style = Font.ITALIC;
            }
            Font f = new Font(Font.SANS_SERIF, 
                              style, 
                              (int) (t.getPointSize() * scalingScalar));
            
            int x = (int) (scalingScalar * t.getX());
            int y = (int) (scalingScalar * t.getBaseline());
            g.setFont(f);
            g.setColor(Color.BLACK);
            g.drawString(t.getRun(), x, y);
            
            int top = (int) (scalingScalar * t.getTop());
            int width = (int) (scalingScalar * t.getWidth());
            int height = (int) (scalingScalar * t.getHeight());
            g.setColor(Color.GREEN);
            g.drawRect(x, top, width, height);
        }
    }
    
    
    
}
