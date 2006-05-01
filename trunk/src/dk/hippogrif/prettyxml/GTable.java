/*
    Copyright (C) 2005 Jesper Goertz
    All Rights Reserved, http://hippogrif.dk/sw/prettyxml
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package dk.hippogrif.prettyxml;

import java.awt.Dimension;
import javax.swing.JTable;

/**
 * A table whose preferred size depends on number of rows.
 */
public class GTable extends JTable {

  public Dimension getPreferredSize() {
    Dimension dim = super.getPreferredSize();
    int h = getRowCount()*getRowHeight();
    if (h > dim.height) {
      dim.height = h;
    }
    return dim;
  }
}