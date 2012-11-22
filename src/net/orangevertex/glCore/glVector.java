/*
 Originally a part of the Processing project - http://processing.org

 Copyright (c) 2012 P K
 Copyright (c) 200X Dan Shiffman
 Copyright (c) 2008 Ben Fry and Casey Reas

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General
 Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 Boston, MA  02111-1307  USA
 */

package net.orangevertex.glCore;

//import javax.media.opengl.GL2;

/**
 * A class to describe a two or three dimensional vector.
 * <p>
 * The result of all functions are applied to the vector itself, with the
 * exception of cross(), which returns a new glVector (or writes to a specified
 * 'target' glVector). That is, add() will add the contents of one vector to
 * this one. Using add() with additional parameters allows you to put the result
 * into a new glVector. Functions that act on multiple vectors also include
 * static versions. Because creating new objects can be computationally
 * expensive, most functions include an optional 'target' glVector, so that a
 * new glVector object is not created with each operation.
 * <p>
 * Initially based on the Vector3D class by <a
 * href="http://www.shiffman.net">Dan Shiffman</a>.
 * <p>
 * Extended with some useful additions mostly for drawing stuff
 */
public class glVector {

	/** The x component of the vector. */
	public float x;

	/** The y component of the vector. */
	public float y;

	/** The z component of the vector. */
	public float z;

	/** Array so that this can be temporarily used in an array context */
	protected float[] array;

	/**
	 * Constructor for an empty vector: x, y, and z are set to 0.
	 */
	public glVector() {
	}

	/**
	 * Constructor for a 3D vector.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the y coordinate.
	 */
	public glVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public glVector(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	/**
	 * Constructor for a 2D vector: z coordinate is set to 0.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 */
	public glVector(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	public glVector(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = 0;
	}

	/**
	 * Set x, y, and z coordinates.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Set x, y, and z coordinates from a Vector3D object.
	 * 
	 * @param v
	 *            the glVector object to be copied
	 */
	public void set(glVector v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	/**
	 * Set the x, y (and maybe z) coordinates using a float[] array as the
	 * source.
	 * 
	 * @param source
	 *            array to copy from
	 */
	public void set(float[] source) {
		if (source.length >= 2) {
			x = source[0];
			y = source[1];
		}
		if (source.length >= 3) {
			z = source[2];
		}
	}

	/**
	 * Get a copy of this vector.
	 */
	public glVector get() {
		return new glVector(x, y, z);
	}

	public float[] get(float[] target) {
		if (target == null) {
			return new float[] { x, y, z };
		}
		if (target.length >= 2) {
			target[0] = x;
			target[1] = y;
		}
		if (target.length >= 3) {
			target[2] = z;
		}
		return target;
	}

	/**
	 * Calculate the magnitude (length) of the vector
	 * 
	 * @return the magnitude of the vector
	 */
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Add a vector to this vector
	 * 
	 * @param v
	 *            the vector to be added
	 */
	public void add(glVector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * Add two vectors
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return a new vector that is the sum of v1 and v2
	 */
	static public glVector add(glVector v1, glVector v2) {
		return add(v1, v2, null);
	}

	/**
	 * Add two vectors into a target vector
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @param target
	 *            the target vector (if null, a new vector will be created)
	 * @return a new vector that is the sum of v1 and v2
	 */
	static public glVector add(glVector v1, glVector v2, glVector target) {
		if (target == null) {
			target = new glVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		} else {
			target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		}
		return target;
	}

	/**
	 * Subtract a vector from this vector
	 * 
	 * @param v
	 *            the vector to be subtracted
	 */
	public void sub(glVector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	/**
	 * Subtract one vector from another
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return a new vector that is v1 - v2
	 */
	static public glVector sub(glVector v1, glVector v2) {
		return sub(v1, v2, null);
	}

	static public glVector sub(glVector v1, glVector v2, glVector target) {
		if (target == null) {
			target = new glVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		} else {
			target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		}
		return target;
	}

	/**
	 * Multiply this vector by a scalar
	 * 
	 * @param n
	 *            the value to multiply by
	 */
	public void mult(float n) {
		x *= n;
		y *= n;
		z *= n;
	}

	/**
	 * Multiply a vector by a scalar
	 * 
	 * @param v
	 *            a vector
	 * @param n
	 *            scalar
	 * @return a new vector that is v1 * n
	 */
	static public glVector mult(glVector v, float n) {
		return mult(v, n, null);
	}

	/**
	 * Multiply a vector by a scalar, and write the result into a target
	 * glVector.
	 * 
	 * @param v
	 *            a vector
	 * @param n
	 *            scalar
	 * @param target
	 *            glVector to store the result
	 * @return the target vector, now set to v1 * n
	 */
	static public glVector mult(glVector v, float n, glVector target) {
		if (target == null) {
			target = new glVector(v.x * n, v.y * n, v.z * n);
		} else {
			target.set(v.x * n, v.y * n, v.z * n);
		}
		return target;
	}

	/**
	 * Multiply each element of one vector by the elements of another vector.
	 * 
	 * @param v
	 *            the vector to multiply by
	 */
	public void mult(glVector v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}

	/**
	 * Multiply each element of one vector by the individual elements of another
	 * vector, and return the result as a new glVector.
	 */
	static public glVector mult(glVector v1, glVector v2) {
		return mult(v1, v2, null);
	}

	/**
	 * Multiply each element of one vector by the individual elements of another
	 * vector, and write the result into a target vector.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @param target
	 *            glVector to store the result
	 */
	static public glVector mult(glVector v1, glVector v2, glVector target) {
		if (target == null) {
			target = new glVector(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
		} else {
			target.set(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
		}
		return target;
	}

	/**
	 * Divide this vector by a scalar
	 * 
	 * @param n
	 *            the value to divide by
	 */
	public void div(float n) {
		x /= n;
		y /= n;
		z /= n;
	}

	/**
	 * Divide a vector by a scalar and return the result in a new vector.
	 * 
	 * @param v
	 *            a vector
	 * @param n
	 *            scalar
	 * @return a new vector that is v1 / n
	 */
	static public glVector div(glVector v, float n) {
		return div(v, n, null);
	}

	static public glVector div(glVector v, float n, glVector target) {
		if (target == null) {
			target = new glVector(v.x / n, v.y / n, v.z / n);
		} else {
			target.set(v.x / n, v.y / n, v.z / n);
		}
		return target;
	}

	/**
	 * Divide each element of one vector by the elements of another vector.
	 */
	public void div(glVector v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
	}

	/**
	 * Multiply each element of one vector by the individual elements of another
	 * vector, and return the result as a new glVector.
	 */
	static public glVector div(glVector v1, glVector v2) {
		return div(v1, v2, null);
	}

	/**
	 * Divide each element of one vector by the individual elements of another
	 * vector, and write the result into a target vector.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @param target
	 *            glVector to store the result
	 */
	static public glVector div(glVector v1, glVector v2, glVector target) {
		if (target == null) {
			target = new glVector(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
		} else {
			target.set(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
		}
		return target;
	}

	/**
	 * Calculate the Euclidean distance between two points (considering a point
	 * as a vector object)
	 * 
	 * @param v
	 *            another vector
	 * @return the Euclidean distance between
	 */
	public float dist(glVector v) {
		float dx = x - v.x;
		float dy = y - v.y;
		float dz = z - v.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Calculate the Euclidean distance between two points (considering a point
	 * as a vector object)
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return the Euclidean distance between v1 and v2
	 */
	static public float dist(glVector v1, glVector v2) {
		float dx = v1.x - v2.x;
		float dy = v1.y - v2.y;
		float dz = v1.z - v2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Calculate the dot product with another vector
	 * 
	 * @return the dot product
	 */
	public float dot(glVector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public float dot(float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	static public float dot(glVector v1, glVector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/**
	 * Return a vector composed of the cross product between this and another.
	 */
	public glVector cross(glVector v) {
		return cross(v, null);
	}

	/**
	 * Perform cross product between this and another vector, and store the
	 * result in 'target'. If target is null, a new vector is created.
	 */
	public glVector cross(glVector v, glVector target) {
		float crossX = y * v.z - v.y * z;
		float crossY = z * v.x - v.z * x;
		float crossZ = x * v.y - v.x * y;

		if (target == null) {
			target = new glVector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}

	static public glVector cross(glVector v1, glVector v2, glVector target) {
		float crossX = v1.y * v2.z - v2.y * v1.z;
		float crossY = v1.z * v2.x - v2.z * v1.x;
		float crossZ = v1.x * v2.y - v2.x * v1.y;

		if (target == null) {
			target = new glVector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}

	/**
	 * Normalize the vector to length 1 (make it a unit vector)
	 */
	public void normalize() {
		float m = mag();
		if (m != 0 && m != 1) {
			div(m);
		}
	}

	/**
	 * Normalize this vector, storing the result in another vector.
	 * 
	 * @param target
	 *            Set to null to create a new vector
	 * @return a new vector (if target was null), or target
	 */
	public glVector normalize(glVector target) {
		if (target == null) {
			target = new glVector();
		}
		float m = mag();
		if (m > 0) {
			target.set(x / m, y / m, z / m);
		} else {
			target.set(x, y, z);
		}
		return target;
	}

	/**
	 * Limit the magnitude of this vector
	 * 
	 * @param max
	 *            the maximum length to limit this vector
	 */
	public void limit(float max) {
		if (mag() > max) {
			normalize();
			mult(max);
		}
	}

	/**
	 * Calculate the angle of rotation for this vector (only 2D vectors)
	 * 
	 * @return the angle of rotation
	 */
	public float heading2D() {
		float angle = (float) Math.atan2(-y, x);
		return -1 * angle;
	}

	/**
	 * Calculate the angle between two vectors, using the dot product
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return the angle between the vectors
	 */
	static public float angleBetween(glVector v1, glVector v2) {
		float dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
		double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
		double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
		return (float) Math.acos(dot / (v1mag * v2mag));
	}

	public String toString() {
		return "[ " + x + ", " + y + ", " + z + " ]";
	}

	/**
	 * Return a representation of this vector as a float array. This is only for
	 * temporary use. If used in any other fashion, the contents should be
	 * copied by using the get() command to copy into your own array.
	 */
	public float[] array() {
		if (array == null) {
			array = new float[3];
		}
		array[0] = x;
		array[1] = y;
		array[2] = z;
		return array;
	}

//	public void draw(GL2 gl) {
//		gl.glBegin(GL2.GL_POINTS);
//		gl.glVertex3f((float) x, (float) y, (float) z);
//		gl.glEnd();
//	}
//
//	public static void line(GL2 gl, glVector v1, glVector v2) {
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f((float) v1.x, (float) v1.y, (float) v1.z);
//		gl.glVertex3f((float) v2.x, (float) v2.y, (float) v2.z);
//		gl.glEnd();
//	}
//
//	public static void line(GL2 gl, float v1x, float v1y, float v1z, float v2x,
//			float v2y, float v2z) {
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f((float) v1x, (float) v1y, (float) v1z);
//		gl.glVertex3f((float) v2x, (float) v2y, (float) v2z);
//		gl.glEnd();
//	}
//
//	public static void line2C(GL2 gl, glVector v1, glVector v2, int seg, int c1,
//			int c2) {
//		glVector v3 = v2.get();
//		v3.sub(v1);
//		float sz = v3.mag() / seg;
//		v3.normalize();
//		v3.mult(sz);
//        float cR = (((c1 >> 24) & 0xFF) + ((c2 >> 16) & 0xFF)) / seg;
//        float cG = (((c1 >> 8) & 0xFF) + ((c2 >> 8) & 0xFF)) / seg;
//        float cB = ((c1 & 0xFF) + (c2 & 0xFF)) / seg;
//        float cA = (((c1 >> 24) & 0xFF) + ((c2 >> 24) & 0xFF)) / seg;
//        
////		int c3 = p.color(((c1 >> 24) & 0xFF) + ((c2 >> 16) & 0xFF)) / seg,
////				(((c1 >> 8) & 0xFF) + ((c2 >> 8) & 0xFF)) / seg,
////				((c1 & 0xFF) + (c2 & 0xFF)) / seg,
////				(((c1 >> 24) & 0xFF) + ((c2 >> 24) & 0xFF)) / seg);
//
//		for (int i = 0; i < seg; i++) {
//			gl.glColor4f(cR, cG, cB, cA);
//			p.stroke(((c1 >> 24) & 0xFF) + ((c3 >> 16) & 0xFF) * i,
//					((c1 >> 8) & 0xFF) + ((c3 >> 8) & 0xFF) * i, (c1 & 0xFF)
//							+ (c3 & 0xFF) * i, ((c1 >> 24) & 0xFF)
//							+ ((c3 >> 24) & 0xFF) * i);
//		glVector.line(gl,v1.x + v3.x * i, v1.y + v3.y * i, v1.z + v3.z * i, v1.x
//						+ v3.x * (i + 1), v1.y + v3.y * (i + 1), v1.z + v3.z
//						* (i + 1));
//		}
//	}

	// public static void vVertex(GL2 gl, glVector v, float f1, float f2) {
	// if (v.z == 0.0)
	// p.vertex(v.x, v.y, f1, f2);
	// else
	// p.vertex(v.x, v.y, v.z, f1, f2);
	// }
	//
	// public static void bVertex(GL2 gl, glVector v1, glVector v2, glVector v3)
	// {
	// if (v1.z == 0.0 && v2.z == 0.0 && v3.z == 0.0)
	// p.bezierVertex(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
	// else
	// p.bezierVertex(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, v3.x, v3.y, v3.z);
	// }
	//
	// public static void vBox(GL2 gl, glVector v, float s) {
	// p.pushMatrix();
	// p.translate(v.x, v.y, v.z);
	// p.box(s);
	// p.popMatrix();
	// }
	//
	// public static void vBox(GL2 gl, glVector v, float sX, float sY, float sZ)
	// {
	// p.pushMatrix();
	// p.translate(v.x, v.y, v.z);
	// p.box(sX, sY, sZ);
	// p.popMatrix();
	// }
//	public void zLine(GL2 gl) {
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f(0, 0, 0);
//		gl.glVertex3f((float) x, (float) y, (float) z);
//		gl.glEnd();
//	}
//
//	public static void zLine(GL2 gl, glVector v) {
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f(0, 0, 0);
//		gl.glVertex3f((float) v.x, (float) v.y, (float) v.z);
//		gl.glEnd();
//	}
//
//	public void vertex(GL2 gl) {
//		gl.glVertex3f((float) x, (float) y, (float) z);
//	}
//
//	public static void vCircle(GL2 gl, glVector v, float r) {
//		int sides = 32;
//		float angle = (float) (2 * Math.PI / sides);
//		gl.glBegin(GL2.GL_LINES);
//		for (int i = 0; i < sides; i++) {
//			float x = (float) (v.x + Math.cos(i * angle) * r);
//			float y = (float) (v.y + Math.sin(i * angle) * r);
//			gl.glVertex3f(x, y, (float) v.z);
//		}
//		gl.glVertex3f((float) (v.x + r), 0, (float) v.z);
//		gl.glEnd();
//	}
//
	public static glVector vMid(glVector v1, glVector v2) {
		// returns the vector between two vectors
		return new glVector((float) ((v1.x + v2.x) * 0.5),
				(float) ((v1.y + v2.y) * 0.5), (float) ((v1.z + v2.z) * 0.5));
	}

	public static glVector vMult(glVector base, glVector vec, float multiplier) {
		// scales a vector from a base vector
		vec = glVector.sub(vec, base);
		vec.mult(multiplier);
		vec.add(base);
		return vec;
	}

	// public static glVector vRemap(glVector base, glVector vec, float
	// multiplier) {
	// // scales a vector from a base vector
	// vec = glVector.sub(vec, base);
	// vec.normalize();
	// vec.mult(multiplier);
	// vec.add(base);
	// return vec;
	// }

	public static glVector superRotate(glVector v, glVector angle) {
		vMatrix rotater = new vMatrix();
		glVector result = new glVector(0, 0, 0);
		rotater.rotate(angle.x, 1, 0, 0);
		rotater.rotate(angle.y, 0, 1, 0);
		rotater.rotate(angle.z, 0, 0, 1);
		rotater.mult(v, result);
		return result;
	}

	public static glVector zRotateX(glVector v, float angle) {
		vMatrix rotater = new vMatrix();
		glVector result = new glVector(0, 0, 0);
		rotater.rotate(angle, 1, 0, 0);
		rotater.mult(v, result);
		return result;

	}

	public static glVector zRotateY(glVector v, float angle) {
		vMatrix rotater = new vMatrix();
		glVector result = new glVector(0, 0, 0);
		rotater.rotate(angle, 0, 1, 0);
		rotater.mult(v, result);
		return result;
	}

	public static glVector zRotateZ(glVector v, float angle) {
		vMatrix rotater = new vMatrix();
		glVector result = new glVector(0, 0, 0);
		rotater.rotate(angle, 0, 0, 1);
		rotater.mult(v, result);
		return result;
	}

//	public static void drawAxiz(GL2 gl, float size) {
//
//		gl.glColor3f(1.0f, 0.0f, 0.0f);
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(size, 0.0f, 0.0f);
//		gl.glEnd();
//
//		gl.glColor3f(0.0f, 1.0f, 0.0f);
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(0.0f, size, 0.0f);
//		gl.glEnd();
//
//		gl.glColor3f(0.0f, 0.0f, 1.0f);
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(0.0f, 0.0f, size);
//		gl.glEnd();
//	}
}
