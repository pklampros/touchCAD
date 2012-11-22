/*
 * 
 *Originally part of the Processing project - http://processing.org
 *
 *Copyright (c) 2005-08 Ben Fry and Casey Reas
 *
 *This library is free software; you can redistribute it and/or
 *modify it under the terms of the GNU Lesser General Public
 *License as published by the Free Software Foundation; either
 *version 2.1 of the License, or (at your option) any later version.
 *
 *This library is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *Lesser General Public License for more details.
 *
 *You should have received a copy of the GNU Lesser General
 *Public License along with this library; if not, write to the
 *Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *Boston, MA  02111-1307  USA
 */

package net.orangevertex.glCore;

public class vMatrix {
	public float m00, m01, m02, m03;
	public float m10, m11, m12, m13;
	public float m20, m21, m22, m23;
	public float m30, m31, m32, m33;
	final double EPSILON = 2.7755575615628914E-17;

	public vMatrix() {
		reset();
	}

	public void reset() {
		set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}

	public void set(float m00, float m01, float m02, float m03, float m10,
			float m11, float m12, float m13, float m20, float m21, float m22,
			float m23, float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public void rotate(float angle, float v0, float v1, float v2) {
		float norm2 = v0 * v0 + v1 * v1 + v2 * v2;
		if (norm2 < EPSILON) {
			// The vector is zero, cannot apply rotation.
			return;
		}

		if (Math.abs(norm2 - 1) > EPSILON) {
			// The rotation vector is not normalized.
			float norm = (float) Math.sqrt(norm2);
			v0 /= norm;
			v1 /= norm;
			v2 /= norm;
		}
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float t = 1.0f - c;

		apply((t * v0 * v0) + c, (t * v0 * v1) - (s * v2), (t * v0 * v2)
				+ (s * v1), 0, (t * v0 * v1) + (s * v2), (t * v1 * v1) + c, (t
				* v1 * v2)
				- (s * v0), 0, (t * v0 * v2) - (s * v1), (t * v1 * v2)
				+ (s * v0), (t * v2 * v2) + c, 0, 0, 0, 0, 1);
	}

	public void apply(float n00, float n01, float n02, float n03, float n10,
			float n11, float n12, float n13, float n20, float n21, float n22,
			float n23, float n30, float n31, float n32, float n33) {

		float r00 = m00 * n00 + m01 * n10 + m02 * n20 + m03 * n30;
		float r01 = m00 * n01 + m01 * n11 + m02 * n21 + m03 * n31;
		float r02 = m00 * n02 + m01 * n12 + m02 * n22 + m03 * n32;
		float r03 = m00 * n03 + m01 * n13 + m02 * n23 + m03 * n33;

		float r10 = m10 * n00 + m11 * n10 + m12 * n20 + m13 * n30;
		float r11 = m10 * n01 + m11 * n11 + m12 * n21 + m13 * n31;
		float r12 = m10 * n02 + m11 * n12 + m12 * n22 + m13 * n32;
		float r13 = m10 * n03 + m11 * n13 + m12 * n23 + m13 * n33;

		float r20 = m20 * n00 + m21 * n10 + m22 * n20 + m23 * n30;
		float r21 = m20 * n01 + m21 * n11 + m22 * n21 + m23 * n31;
		float r22 = m20 * n02 + m21 * n12 + m22 * n22 + m23 * n32;
		float r23 = m20 * n03 + m21 * n13 + m22 * n23 + m23 * n33;

		float r30 = m30 * n00 + m31 * n10 + m32 * n20 + m33 * n30;
		float r31 = m30 * n01 + m31 * n11 + m32 * n21 + m33 * n31;
		float r32 = m30 * n02 + m31 * n12 + m32 * n22 + m33 * n32;
		float r33 = m30 * n03 + m31 * n13 + m32 * n23 + m33 * n33;

		m00 = r00;
		m01 = r01;
		m02 = r02;
		m03 = r03;
		m10 = r10;
		m11 = r11;
		m12 = r12;
		m13 = r13;
		m20 = r20;
		m21 = r21;
		m22 = r22;
		m23 = r23;
		m30 = r30;
		m31 = r31;
		m32 = r32;
		m33 = r33;
	}

	public glVector mult(glVector source, glVector target) {
		if (target == null) {
			target = new glVector();
		}
		target.x = m00 * source.x + m01 * source.y + m02 * source.z + m03;
		target.y = m10 * source.x + m11 * source.y + m12 * source.z + m13;
		target.z = m20 * source.x + m21 * source.y + m22 * source.z + m23;
		// float tw = m30*source.x + m31*source.y + m32*source.z + m33;
		// if (tw != 0 && tw != 1) {
		// target.div(tw);
		// }
		return target;
	}

	/**
	 * Multiply a three or four element vector against this matrix. If out is
	 * null or not length 3 or 4, a new float array (length 3) will be returned.
	 */
	public float[] mult(float[] source, float[] target) {
		if (target == null || target.length < 3) {
			target = new float[3];
		}
		if (source == target) {
			throw new RuntimeException("The source and target vectors used in "
					+ "PMatrix3D.mult() cannot be identical.");
		}
		if (target.length == 3) {
			target[0] = m00 * source[0] + m01 * source[1] + m02 * source[2]
					+ m03;
			target[1] = m10 * source[0] + m11 * source[1] + m12 * source[2]
					+ m13;
			target[2] = m20 * source[0] + m21 * source[1] + m22 * source[2]
					+ m23;
			// float w = m30*source[0] + m31*source[1] + m32*source[2] + m33;
			// if (w != 0 && w != 1) {
			// target[0] /= w; target[1] /= w; target[2] /= w;
			// }
		} else if (target.length > 3) {
			target[0] = m00 * source[0] + m01 * source[1] + m02 * source[2]
					+ m03 * source[3];
			target[1] = m10 * source[0] + m11 * source[1] + m12 * source[2]
					+ m13 * source[3];
			target[2] = m20 * source[0] + m21 * source[1] + m22 * source[2]
					+ m23 * source[3];
			target[3] = m30 * source[0] + m31 * source[1] + m32 * source[2]
					+ m33 * source[3];
		}
		return target;
	}
}
