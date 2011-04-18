/* LICENSE: GPL2
Copyright (C) 2011 Dmitry Barashev

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package net.sourceforge.ganttproject.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Stores the available primitives and their information (used for painting) and
 * provides methods to retrieve them
 * 
 * @author bard
 */
public class GraphicPrimitiveContainer {
    private ArrayList<Rectangle> myRectangles = new ArrayList<Rectangle>();

    private ArrayList<Line> myLines = new ArrayList<Line>();

    private ArrayList<Text> myTexts = new ArrayList<Text>();

    private Map<Object, GraphicPrimitive> myModelObject2primitive = new WeakHashMap<Object, GraphicPrimitive>();

    private List<GraphicPrimitiveContainer> myLayers = new ArrayList<GraphicPrimitiveContainer>();

    private int myDeltaX;

    private int myDeltaY;

    /** Horizontal alignments for texts */
    public enum HAlignment { CENTER, LEFT, RIGHT };

    /** Vertical alignments for texts */
    public enum VAlignment { CENTER, TOP, BOTTOM };


    static class GraphicPrimitive {
        private Color myBackgroundColor;

        private Color myForegroundColor;

        private String myStyleName;

        private Object myModelObject;

        private boolean isVisible = true;

        private LinkedHashSet<String> myStyles;
        
        private LinkedHashSet<String> getStyles() {
            if (myStyles == null) {
                myStyles = new LinkedHashSet<String>();
            }
            return myStyles;
        }
        
        public void addStyle(String style) {
            getStyles().add(style);
        }
        
        public boolean hasStyle(String style) {
            return getStyles().contains(style);
        }
        
        public void setStyle(String styleName) {
            myStyleName = styleName;
        }

        public String getStyle() {
            return myStyleName;
        }

        public Color getBackgroundColor() {
            return myBackgroundColor;
        }

        public void setBackgroundColor(Color myBackgroundColor) {
            this.myBackgroundColor = myBackgroundColor;
        }

        public Color getForegroundColor() {
            return myForegroundColor;
        }

        public void setForegroundColor(Color myForegroundColor) {
            this.myForegroundColor = myForegroundColor;
        }

        public Object getModelObject() {
            return myModelObject;
        }

        void setModelObject(Object modelObject) {
            myModelObject = modelObject;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }
    }

    public static class Rectangle extends GraphicPrimitive {
        final int myLeftX;

        final int myTopY;

        final int myWidth;

        final int myHeight;

        private Rectangle(int leftx, int topy, int width, int height) {
            myLeftX = leftx;
            myTopY = topy;
            myWidth = width;
            myHeight = height;
        }

        public int getRightX() {
            return myLeftX + myWidth;
        }

        public int getBottomY() {
            return myTopY + myHeight;
        }

        public String toString() {
            return "leftx=" + myLeftX + " topy=" + myTopY + " width=" + myWidth
                    + " height=" + myHeight;
        }

        public int getMiddleY() {
            return myTopY + myHeight / 2;
        }

        public int getMiddleX() {
            return myLeftX + myWidth / 2;
        }
    }

    public static class Line extends GraphicPrimitive {
        private final int myStartX;

        private final int myStartY;

        private final int myFinishX;

        private final int myFinishY;

        private Line(int startx, int starty, int finishx, int finishy) {
            myStartX = startx;
            myStartY = starty;
            myFinishX = finishx;
            myFinishY = finishy;
        }

        public int getStartX() {
            return myStartX;
        }

        public int getStartY() {
            return myStartY;
        }

        public int getFinishX() {
            return myFinishX;
        }

        public int getFinishY() {
            return myFinishY;
        }
    }

    public static class Text extends GraphicPrimitive {
        private final int myLeftX;

        private final int myBottomY;

        private final String myText;

        private Font myFont;

        private int myMaxLength;

        private HAlignment myHAlignment = HAlignment.LEFT;

        private VAlignment myVAlignment = VAlignment.BOTTOM;

        Text(int leftX, int bottomY, String text) {
            myLeftX = leftX;
            myBottomY = bottomY;
            myText = text;
            myMaxLength = -1;
        }

        public void setFont(Font font) {
            myFont = font;
        }

        public void setMaxLength(int maxLength) {
            myMaxLength = maxLength;
        }

        public int getMaxLength() {
            return myMaxLength;
        }

        public Font getFont() {
            return myFont;
        }

        public String getText() {
            return myText;
        }

        public int getLeftX() {
            return myLeftX;
        }

        public int getBottomY() {
            return myBottomY;
        }

        public void setAlignment(HAlignment halignment, VAlignment valignment) {
            myHAlignment = halignment;
            myVAlignment = valignment;
        }

        public HAlignment getHAlignment() {
            return myHAlignment;
        }

        public VAlignment getVAlignment() {
            return myVAlignment;
        }
    }

    public GraphicPrimitiveContainer() {
        this(0,0);
    }

    public GraphicPrimitiveContainer(int deltax, int deltay) {
        myDeltaX = deltax;
        myDeltaY = deltay;
    }

    public void setOffset(int deltax, int deltay) {
        myDeltaX = deltax;
        myDeltaY = deltay;
//        for (GraphicPrimitiveContainer layer : myLayers) {
//            layer.setOffset(deltax, deltay);
//        }
    }
    public Rectangle createRectangle(int leftx, int topy, int width, int height) {
        if (width < 0) {
            width = -width;
            leftx = leftx - width;
        }
        Rectangle result = new Rectangle(leftx+myDeltaX, topy+myDeltaY, width, height);
        myRectangles.add(result);
        return result;
    }

    public Line createLine(int startx, int starty, int finishx, int finishy) {
        Line result = new Line(startx+myDeltaX, starty+myDeltaY, finishx+myDeltaX, finishy+myDeltaY);
        myLines.add(result);
        return result;
    }

    public Text createText(int leftx, int bottomy, String text) {
        Text result = new Text(leftx+myDeltaX, bottomy+myDeltaY, text);
        myTexts.add(result);
        return result;
    }

    void paint(Painter painter) {
        painter.prePaint();
        for (int i = 0; i < myRectangles.size(); i++) {
            Rectangle next = myRectangles.get(i);
            if (next.isVisible()) {
                painter.paint(next);
            }
        }
        for (int i = 0; i < myLines.size(); i++) {
            Line next = myLines.get(i);
            if (next.isVisible()) {
                painter.paint(next);
            }
        }
        for (int i = 0; i < myTexts.size(); i++) {
            Text next = myTexts.get(i);
            if(next.isVisible()) {
                painter.paint(next);
            }
        }
    }

    public void clear() {
        myRectangles.clear();
        myLines.clear();
        myTexts.clear();
        myModelObject2primitive.clear();
    }

    void bind(GraphicPrimitive primitive, Object modelObject) {
        myModelObject2primitive.put(modelObject, primitive);
        primitive.setModelObject(modelObject);
    }

    GraphicPrimitive getPrimitive(Object modelObject) {
        return myModelObject2primitive.get(modelObject);
    }

    public GraphicPrimitive getPrimitive(int x, int y) {
        // System.err.println("looking for primitive under point x="+x+" y="+y);
        return getPrimitive(x, 0, y, 0);
//        for (int i = 0; i < myRectangles.size(); i++) {
//            Rectangle next = (Rectangle) myRectangles.get(i);
//            // System.err.println(" next rectangle="+next);
//            if (next.myLeftX <= x && next.myLeftX + next.myWidth >= x
//                    && next.myTopY <= y && next.myTopY + next.myHeight >= y) {
//                return next;
//            }
//        }
//        return null;
    }

    public GraphicPrimitive getPrimitive(int x, int xThreshold, int y, int yThreshold) {
        for (int i = 0; i < myRectangles.size(); i++) {
            Rectangle next = myRectangles.get(i);
            // System.err.println(" next rectangle="+next);
            if (next.myLeftX <= x+xThreshold && next.myLeftX + next.myWidth >= x-xThreshold
                    && next.myTopY <= y+yThreshold && next.myTopY + next.myHeight >= y-yThreshold) {
                return next;
            }
        }
        return null;
    }

    public List<GraphicPrimitiveContainer> getLayers() {
        return Collections.unmodifiableList(myLayers);
    }
    public GraphicPrimitiveContainer getLayer(int layer) {
        if (layer < 0 || layer >= myLayers.size()) {
            throw new IllegalArgumentException();
        }
        return myLayers.get(layer);
    }

    public GraphicPrimitiveContainer newLayer() {
        GraphicPrimitiveContainer result = new GraphicPrimitiveContainer();
        myLayers.add(result);
        return result;
    }
}