/*
 * Tele Atlas OpenLR Access Layer for SQLite
 * Copyright (C) 2009 Tele Atlas B.V.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.hhu.c2c.openlr.gis;

import java.awt.geom.Path2D;
import java.util.concurrent.atomic.AtomicReference;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

/**
 * Some helper methods to convert a WKB binary encoded {@link LineString} to
 * {@link Path2D}
 * 
 * The original methodsare in use in:
 * 
 * <pre>
 * Group Id: openlr.teleatlas
 * Artifact Id: map-sqlite
 * Version: 1.0.0
 * </pre>
 * 
 * within the OpenLR Maven repository
 * 
 * <pre>
 * Id: openlr
 * URL: http://www.openlr.org/nexus/content/repositories/releases
 * </pre>
 * 
 * The sources can be found at <a
 * href="http://openlr.org/download.html">openlr.org/downloads</a>
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class LineStringHelper {

	final static PrecisionModel precisionModel = new PrecisionModel();
	final static GeometryFactory geometryFactory = new GeometryFactory(
			precisionModel, 4326);
	private static final AtomicReference<WKBReader> WKB_READER = new AtomicReference<WKBReader>(
			new WKBReader(geometryFactory));

	/**
	 * Extracted from
	 * {@link openlr.map.teleatlas.sqlite.impl.LineImpl#getShape()}
	 * 
	 * @see openlr.map.Line#getShape()
	 */
	public static Path2D getPath2D(LineString lineString) {
		final Coordinate p0 = lineString.getPointN(0).getCoordinate();
		final Path2D.Double path = new Path2D.Double();
		path.moveTo(p0.x, p0.y);
		for (int n = 1; n < lineString.getNumPoints(); n++) {
			final Coordinate p = lineString.getPointN(n).getCoordinate();
			path.lineTo(p.x, p.y);
		}
		return path;
	}

	/**
	 * Extracted from
	 * {@link openlr.map.teleatlas.sqlite.helpers.SpatialUtils#lineStringFromWKB(byte[])}
	 * 
	 * Creates a {@link com.vividsolutions.jts.geom.LineString} from a
	 * Well-Known-Binary (WKB) geometry.
	 * 
	 * @param bytes
	 *            the WKB geometry.
	 * @return a {@link com.vividsolutions.jts.geom.LineString} representing the
	 *         WKB geometry specified.
	 */
	public static LineString lineStringFromWKB(final byte[] bytes) {

		LineString lineString;
		try {
			lineString = (LineString) WKB_READER.get().read(bytes);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
		return lineString;
	}
}